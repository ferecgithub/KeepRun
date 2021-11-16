package com.ferechamitbeyli.data.remote.entities.mappers

import com.ferechamitbeyli.data.remote.entities.UserDto
import com.ferechamitbeyli.domain.DomainMapper
import com.ferechamitbeyli.domain.entity.User

object UserDtoMapper : DomainMapper<UserDto, User> {

    override fun mapToDomainModel(model: UserDto): User = User(
        email = model.email,
        username = model.username,
        weight = model.weight,
        isNotificationEnable = model.isNotificationEnable,
        photoUrl = model.photoUrl
    )

    override fun mapFromDomainModel(domainModel: User): UserDto = UserDto(
        email = domainModel.email,
        username = domainModel.username,
        weight = domainModel.weight,
        isNotificationEnable = domainModel.isNotificationEnable,
        photoUrl = domainModel.photoUrl
    )

    override fun mapToDomainModelList(list: List<UserDto>): List<User> =
        list.map { mapToDomainModel(it) }

    override fun mapFromDomainModelList(list: List<User>): List<UserDto> =
        list.map { mapFromDomainModel(it) }

}