package org.erbalkan.kernel.dataAccess.abstracts

import org.erbalkan.kernel.entities.Entity

interface Repository<TEntity: Entity<ID>, ID> {
    fun findById(id: ID): TEntity?
    fun findAll(): List<TEntity>
    fun existsById(id: ID): Boolean
    fun insert(entity: TEntity): TEntity
    fun update(entity: TEntity): TEntity
    fun delete(id: ID)
}