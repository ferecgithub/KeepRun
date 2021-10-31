package com.ferechamitbeyli.presentation.uimodels.mappers

import com.ferechamitbeyli.domain.DomainMapper
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.presentation.uimodels.UserUIModel

object UserToUIMapper : DomainMapper<UserUIModel, User> {
    override fun mapToDomainModel(model: UserUIModel): User = User(
        uid = model.uid,
        email = model.email,
        username = model.username,
        isNotificationEnable = model.isNotificationEnable,
        photoUrl = model.photoUrl
    )

    override fun mapFromDomainModel(domainModel: User): UserUIModel = UserUIModel(
        uid = domainModel.uid,
        email = domainModel.email,
        username = domainModel.username,
        isNotificationEnable = domainModel.isNotificationEnable,
        photoUrl = domainModel.photoUrl
    )

    override fun mapToDomainModelList(list: List<UserUIModel>): List<User> =
        list.map { mapToDomainModel(it) }

    override fun mapFromDomainModelList(list: List<User>): List<UserUIModel> =
        list.map { mapFromDomainModel(it) }
}