package com.ferechamitbeyli.presentation.uimodels.mappers

import com.ferechamitbeyli.domain.DomainMapper
import com.ferechamitbeyli.domain.entity.Run
import com.ferechamitbeyli.presentation.uimodels.RunUIModel
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.fromBitmap
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.toBitmap

object RunToUIMapper : DomainMapper<RunUIModel, Run> {
    override fun mapToDomainModel(model: RunUIModel): Run = Run(
        imageUrl = model.imageUrl,
        image = model.image?.let { fromBitmap(it) },
        timestamp = model.timestamp,
        avgSpeedInKMH = model.avgSpeedInKMH,
        distanceInMeters = model.distanceInMeters,
        timeInMillis = model.timeInMillis,
        caloriesBurned = model.caloriesBurned,
        steps = model.steps,
        id = model.id
    )

    override fun mapFromDomainModel(domainModel: Run): RunUIModel = RunUIModel(
        imageUrl = domainModel.imageUrl,
        image = domainModel.image?.let { toBitmap(it) },
        timestamp = domainModel.timestamp,
        avgSpeedInKMH = domainModel.avgSpeedInKMH,
        distanceInMeters = domainModel.distanceInMeters,
        timeInMillis = domainModel.timeInMillis,
        caloriesBurned = domainModel.caloriesBurned,
        steps = domainModel.steps,
        id = domainModel.id
    )

    override fun mapToDomainModelList(list: List<RunUIModel>): List<Run> =
        list.map { mapToDomainModel(it) }

    override fun mapFromDomainModelList(list: List<Run>): List<RunUIModel> =
        list.map { mapFromDomainModel(it) }

}