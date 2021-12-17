package com.ferechamitbeyli.data.remote.entities.mappers

import com.ferechamitbeyli.data.local.entities.RunEntity
import com.ferechamitbeyli.data.remote.entities.RunDto
import com.ferechamitbeyli.data.utils.EntityMapper
import com.ferechamitbeyli.data.utils.HelperFunctions.getBitmapFromUrl

object RunDtoToEntityMapper : EntityMapper<RunDto, RunEntity> {

    override fun mapToEntityModel(model: RunDto): RunEntity = RunEntity(
        imageUrl = model.imageUrl,
        image = model.imageUrl?.let { getBitmapFromUrl(it) },
        timestamp = model.timestamp,
        avgSpeedInKMH = model.avgSpeedInKMH,
        distanceInMeters = model.distanceInMeters,
        timeInMillis = model.timeInMillis,
        caloriesBurned = model.caloriesBurned,
        steps = model.steps,
    )

    override fun mapFromEntityModel(entityModel: RunEntity): RunDto = RunDto(
        imageUrl = entityModel.imageUrl,
        timestamp = entityModel.timestamp,
        avgSpeedInKMH = entityModel.avgSpeedInKMH,
        distanceInMeters = entityModel.distanceInMeters,
        timeInMillis = entityModel.timeInMillis,
        caloriesBurned = entityModel.caloriesBurned,
        steps = entityModel.steps,
        id = entityModel.id
    )

    override fun mapToEntityModelList(list: List<RunDto>): List<RunEntity> =
        list.map { mapToEntityModel(it) }

    override fun mapFromEntityModelList(list: List<RunEntity>): List<RunDto> =
        list.map { mapFromEntityModel(it) }
}