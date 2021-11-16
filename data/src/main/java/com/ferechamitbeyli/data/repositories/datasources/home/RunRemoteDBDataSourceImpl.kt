package com.ferechamitbeyli.data.repositories.datasources.home

import com.ferechamitbeyli.data.local.db.RunDao
import com.ferechamitbeyli.data.local.entities.RunEntity
import com.ferechamitbeyli.data.remote.entities.RunDto
import com.ferechamitbeyli.data.utils.Constants
import com.ferechamitbeyli.data.utils.Constants.RUNS_STORAGE_REF
import com.ferechamitbeyli.data.utils.EntityMapper
import com.ferechamitbeyli.domain.DomainMapper
import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.entity.Run
import com.ferechamitbeyli.domain.repository.datasources.home.RunRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RunRemoteDBDataSourceImpl @Inject constructor(
    private val runDao: RunDao,
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference,
    private val firebaseStorageReference: StorageReference,
    private val runDtoMapper: DomainMapper<RunDto, Run>,
    private val runDtoToEntityMapper: EntityMapper<RunDto, RunEntity>,
    private val coroutineDispatchers: CoroutineDispatchers
) : RunRemoteDataSource {

    override suspend fun insertMapImageToRemoteDB(
        byteArray: ByteArray,
        timestamp: String
    ): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            val runMapStorageReference = firebaseStorageReference.child(RUNS_STORAGE_REF)
                .child(firebaseAuth.currentUser?.uid.toString()).child(timestamp)
                .putBytes(byteArray)

            val mapImageUrl: String =
                runMapStorageReference.snapshot.storage.downloadUrl.await().toString()

            if (mapImageUrl.isNotBlank()) {
                emit(Resource.Success(mapImageUrl))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun removeMapImageFromRemoteDB(timestamp: String): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            val runMapStorageReference = firebaseStorageReference.child(RUNS_STORAGE_REF)
                .child(firebaseAuth.currentUser?.uid.toString()).child(timestamp)
                .delete().await()

            emit(Resource.Success("Map Image is successfully deleted."))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())


    override suspend fun insertRunToRemoteDB(run: Run, imageUrl: String): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            val runDto = RunDto(
                imageUrl,
                run.timestamp,
                run.avgSpeedInKMH,
                run.distanceInMeters,
                run.timeInMillis,
                run.caloriesBurned,
                run.steps,
                run.id
            )

            databaseReference.child(Constants.RUNS_TABLE_REF).child(firebaseAuth.uid.toString())
                .child(runDto.id.toString())
                .setValue(runDto).await().also {
                    emit(Resource.Success("The run is successfully inserted."))
                }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun removeRunFromRemoteDB(run: Run): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            val runDto = runDtoMapper.mapFromDomainModel(run)

            databaseReference.child(Constants.RUNS_TABLE_REF).child(firebaseAuth.uid.toString())
                .child(runDto.id.toString())
                .removeValue().await().also {
                    emit(Resource.Success("The run is successfully removed."))
                }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun getAllRunsFromRemoteDB(): Flow<Resource<List<Run>>> =
        flow<Resource<List<Run>>> {

            emit(Resource.Loading())

            val runList = mutableListOf<RunDto>()

            databaseReference.child(Constants.RUNS_TABLE_REF).child(firebaseAuth.uid.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (runSnapshot in snapshot.children) {
                                val run = runSnapshot.getValue(RunDto::class.java)
                                runList.add(run!!)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        /** NO-OP **/
                    }

                })

            if (runList.isNotEmpty()) {

                runDao.insertMultiple(
                    *runDtoToEntityMapper.mapToEntityModelList(runList).toTypedArray()
                )

                emit(Resource.Success(runDtoMapper.mapToDomainModelList(runList)))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

}


