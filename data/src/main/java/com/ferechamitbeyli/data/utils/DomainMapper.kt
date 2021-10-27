package com.ferechamitbeyli.data.utils

interface DomainMapper <T, DomainModel> {
    fun mapToDomainModel(model: T) : DomainModel
    fun mapFromDomainModel(domainModel: DomainModel) : T
    fun mapToDomainModelList(list: List<T>) : List<DomainModel>
    fun mapFromDomainModelList(list: List<DomainModel>) : List<T>
}