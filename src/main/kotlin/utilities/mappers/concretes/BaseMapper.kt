package org.erbalkan.kernel.utilities.mappers.concretes

import org.erbalkan.kernel.entities.Dto
import org.erbalkan.kernel.entities.Entity
import org.erbalkan.kernel.utilities.mappers.abstracts.Mapper

abstract class BaseMapper
<TEntity: Entity<ID>, TDto: Dto, ID> : Mapper<TEntity, TDto, ID> {
    // Liste dönüşümleri her zaman aynıdır; bu yüzden şablonda hallediyoruz.
    // ToDtoList
    override fun toDtoList(entities: List<TEntity>): List<TDto> {
        return entities.map { toDto(it) }
    }
    // ToEntityList
    override fun toEntityList(dtos: List<TDto>): List<TEntity> {
        return dtos.map { toEntity(it) }
    }
}

/*
// Örnek: UserMapper
class UserMapper : BaseMapper<User, UserDto, Long>() {

    override fun toDto(entity: User): UserDto = UserDto(
        id = entity.id,
        name = entity.name,
        email = entity.email
    )

    override fun toEntity(dto: UserDto): User = User(
        id = dto.id,
        name = dto.name,
        email = dto.email
    )
}
*/