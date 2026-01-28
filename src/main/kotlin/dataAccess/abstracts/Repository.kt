package org.erbalkan.kernel.dataAccess.abstracts

import org.erbalkan.kernel.entities.Entity

interface Repository<TEntity: Entity<ID>, ID> {
    suspend fun findById(id: ID): TEntity?
    suspend fun findAll(): List<TEntity>
    suspend fun existsById(id: ID): Boolean
    suspend fun insert(entity: TEntity): TEntity
    suspend fun update(entity: TEntity): TEntity
    suspend fun delete(id: ID)
}