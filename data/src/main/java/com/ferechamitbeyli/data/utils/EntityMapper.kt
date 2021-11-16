package com.ferechamitbeyli.data.utils

interface EntityMapper <T, EntityModel> {
    fun mapToEntityModel(model: T) : EntityModel
    fun mapFromEntityModel(entityModel: EntityModel) : T
    fun mapToEntityModelList(list: List<T>) : List<EntityModel>
    fun mapFromEntityModelList(list: List<EntityModel>) : List<T>
}