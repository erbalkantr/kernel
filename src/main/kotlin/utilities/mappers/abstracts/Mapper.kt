package org.erbalkan.kernel.utilities.mappers.abstracts

import org.erbalkan.kernel.entities.Dto
import org.erbalkan.kernel.entities.Entity

// Tüm mapping motorları için temel arayüz.
interface Mapper<TEntity: Entity<ID>, TDto: Dto, ID> {
    fun toDto(entity: TEntity): TDto
    fun toEntity(dto: TDto): TEntity
    fun toDtoList(entities: List<TEntity>): List<TDto>
    fun toEntityList(dtos: List<TDto>): List<TEntity>
}