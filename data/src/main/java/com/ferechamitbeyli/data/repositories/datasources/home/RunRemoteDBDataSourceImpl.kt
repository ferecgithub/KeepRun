package com.ferechamitbeyli.data.repositories.datasources.home

import android.util.Log
import com.ferechamitbeyli.data.local.db.RunDao
import com.ferechamitbeyli.data.local.entities.RunEntity
import com.ferechamitbeyli.data.remote.entities.RunDto
import com.ferechamitbeyli.data.utils.DataConstants
import com.ferechamitbeyli.data.utils.DataConstants.RUNS_STORAGE_REF
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
import kotlinx.coroutines.delay
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

            val uploadTask = runMapStorageReference.putBytes(byteArray)

            var mapImageUrl: String? = null

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                runMapStorageReference.downloadUrl

            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mapImageUrl = task.result.toString()
                }
            }

            delay(2500)

            if (mapImageUrl != null) {
                emit(Resource.Success(mapImageUrl.toString()))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun removeMapImageFromRemoteDB(timestamp: String): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            val runMapStorageReference = firebaseStorageReference.child(RUNS_STORAGE_REF)
                .child(firebaseAuth.currentUser?.uid.toString()).child(timestamp)
                .delete().await().also {
                    emit(Resource.Success("Map Image is successfully deleted."))
                }

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

            databaseReference.child(DataConstants.RUNS_TABLE_REF).child(firebaseAuth.uid.toString())
                .child(runDto.timestamp.toString())
                .setValue(runDto).await().also {
                    emit(Resource.Success("The run is successfully saved."))
                }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun removeRunFromRemoteDB(run: Run): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            val runDto = runDtoMapper.mapFromDomainModel(run)

            databaseReference.child(DataConstants.RUNS_TABLE_REF).child(firebaseAuth.uid.toString())
                .child(runDto.timestamp.toString())
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

            databaseReference.child(DataConstants.RUNS_TABLE_REF).child(firebaseAuth.uid.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var number = 0
                            for (runSnapshot in snapshot.children) {
                                val run = runSnapshot.getValue(RunDto::class.java)
                                Log.d("DATA_FROM_Run", "${run?.avgSpeedInKMH} ${number++}")
                                run?.let { runList.add(it) }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        /** NO-OP **/
                    }

                })

            delay(2000)

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


