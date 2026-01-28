package org.erbalkan.kernel.dataAccess.concretes.exposed

import org.erbalkan.kernel.dataAccess.abstracts.Repository
import org.erbalkan.kernel.entities.Entity
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.statements.UpdateBuilder
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update


abstract class ExposedRepositoryTemplate<TEntity : Entity<ID>, ID : Comparable<ID>, TTable : IdTable<ID>>(
    protected val table: TTable
) : Repository<TEntity, ID> {

    // 1. Soyut Dönüştürücüler (Alt sınıfa bırakılan işler)
    abstract fun rowToEntity(row: ResultRow): TEntity
    abstract fun TTable.mapToTable(statement: UpdateBuilder<*>, entity: TEntity)

    // 2. ID ile Getirme
    override suspend fun findById(id: ID): TEntity? = suspendTransaction {
        table.selectAll().where { table.id eq id }
            .map { rowToEntity(it) }
            .singleOrNull()
    }

    // 3. Hepsini Getirme
    override suspend fun findAll(): List<TEntity> = suspendTransaction {
        table.selectAll().map { rowToEntity(it) }
    }

    // 4. Varlık Kontrolü
    override suspend fun existsById(id: ID): Boolean = suspendTransaction {
        table.selectAll().where { table.id eq id }.count() > 0
    }

    // 5. Kayıt Ekleme
    override suspend fun insert(entity: TEntity): TEntity = suspendTransaction {
        val newId = table.insertAndGetId { mapToTable(it, entity) }
        findById(newId.value)!! // Veritabanındaki en güncel halini (default değerlerle) döner
    }

    // 6. Güncelleme
    override suspend fun update(entity: TEntity): TEntity = suspendTransaction {
        val entityId = entity.id ?: throw IllegalArgumentException("Güncelleme için ID gereklidir.")
        table.update({ table.id eq entityId }) { mapToTable(it, entity) }
        entity
    }

    // 7. Silme
    override suspend fun delete(id: ID): Unit = suspendTransaction {
        table.deleteWhere { table.id eq id }
    }
}

/*
transaction { ... } Bloğu: Exposed'da veritabanına dokunan her
işlem bir transaction içinde olmalıdır.
Eğer işlem sırasında bir hata oluşursa,
transaction her şeyi otomatik olarak geri alır (Rollback).
Bu, veri güvenliği için hayati önem taşır.

findById(newId.value)!!: add metodunda neden nesneyi tekrar çekiyoruz?
 Çünkü veritabanı tarafında otomatik atanan ID'yi
 veya varsayılan değerleri (örneğin kayıt zamanı) nesnemize
 tam olarak yansıtmak istiyoruz.

Single Responsibility: Bu sınıf sadece "Exposed ile CRUD
işlemlerini nasıl yaparım?" sorusuna cevap verir.
Hangi tablonun hangi kolonuna ne yazılacağını bilmez;
onu alt sınıflara (örneğin UserRepository) sorar.
* **/