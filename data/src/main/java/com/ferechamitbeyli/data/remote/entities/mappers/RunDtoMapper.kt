package com.ferechamitbeyli.data.remote.entities.mappers

import com.ferechamitbeyli.data.remote.entities.RunDto
import com.ferechamitbeyli.domain.DomainMapper
import com.ferechamitbeyli.domain.entity.Run

object RunDtoMapper : DomainMapper<RunDto, Run> {

    override fun mapToDomainModel(model: RunDto): Run = Run(
        imageUrl = model.imageUrl,
        timestamp = model.timestamp,
        avgSpeedInKMH = model.avgSpeedInKMH,
        distanceInMeters = model.distanceInMeters,
        timeInMillis = model.timeInMillis,
        caloriesBurned = model.caloriesBurned,
        steps = model.steps,
        id = model.id
    )

    override fun mapFromDomainModel(domainModel: Run): RunDto = RunDto(
        imageUrl = domainModel.imageUrl,
        timestamp = domainModel.timestamp,
        avgSpeedInKMH = domainModel.avgSpeedInKMH,
        distanceInMeters = domainModel.distanceInMeters,
        timeInMillis = domainModel.timeInMillis,
        caloriesBurned = domainModel.caloriesBurned,
        steps = domainModel.steps,
        id = domainModel.id
    )

    override fun mapToDomainModelList(list: List<RunDto>): List<Run> =
        list.map { mapToDomainModel(it) }

    override fun mapFromDomainModelList(list: List<Run>): List<RunDto> =
        list.map { mapFromDomainModel(it) }


}