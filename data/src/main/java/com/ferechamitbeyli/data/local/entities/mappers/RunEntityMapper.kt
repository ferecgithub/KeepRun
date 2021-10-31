package com.ferechamitbeyli.data.local.entities.mappers

import com.ferechamitbeyli.data.local.entities.RunEntity
import com.ferechamitbeyli.domain.DomainMapper
import com.ferechamitbeyli.domain.entity.Run

object RunEntityMapper : DomainMapper<RunEntity, Run> {

    override fun mapToDomainModel(model: RunEntity): Run = Run(
        imageUrl = model.imageUrl,
        timestamp = model.timestamp,
        avgSpeedInKMH = model.avgSpeedInKMH,
        distanceInMeters = model.distanceInMeters,
        timeInMillis = model.timeInMillis,
        caloriesBurned = model.caloriesBurned,
        steps = model.steps,
        id = model.id
    )

    override fun mapFromDomainModel(domainModel: Run): RunEntity = RunEntity(
        imageUrl = domainModel.imageUrl,
        timestamp = domainModel.timestamp,
        avgSpeedInKMH = domainModel.avgSpeedInKMH,
        distanceInMeters = domainModel.distanceInMeters,
        timeInMillis = domainModel.timeInMillis,
        caloriesBurned = domainModel.caloriesBurned,
        steps = domainModel.steps
    )

    override fun mapToDomainModelList(list: List<RunEntity>): List<Run> =
        list.map { mapToDomainModel(it) }

    override fun mapFromDomainModelList(list: List<Run>): List<RunEntity> =
        list.map { mapFromDomainModel(it) }

}