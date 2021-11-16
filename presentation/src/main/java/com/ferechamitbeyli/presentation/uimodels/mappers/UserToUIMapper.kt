package com.ferechamitbeyli.presentation.uimodels.mappers

import com.ferechamitbeyli.domain.DomainMapper
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.presentation.uimodels.UserUIModel

object UserToUIMapper : DomainMapper<UserUIModel, User> {
    override fun mapToDomainModel(model: UserUIModel): User = User(
        email = model.email,
        username = model.username,
        weight = model.weight,
        isNotificationEnable = model.isNotificationEnable,
        photoUrl = model.photoUrl
    )

    override fun mapFromDomainModel(domainModel: User): UserUIModel = UserUIModel(
        email = domainModel.email,
        username = domainModel.username,
        weight = domainModel.weight,
        isNotificationEnable = domainModel.isNotificationEnable,
        photoUrl = domainModel.photoUrl
    )

    override fun mapToDomainModelList(list: List<UserUIModel>): List<User> =
        list.map { mapToDomainModel(it) }

    override fun mapFromDomainModelList(list: List<User>): List<UserUIModel> =
        list.map { mapFromDomainModel(it) }
}