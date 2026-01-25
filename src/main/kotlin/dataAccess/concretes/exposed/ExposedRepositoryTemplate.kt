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
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

abstract class ExposedRepositoryTemplate
<TEntity: Entity<ID>, ID: Comparable<ID>, TTable: IdTable<ID>>
    (protected val table: TTable) : Repository<TEntity, ID> {
    // --- Soyut Metodlar: Alt sınıflar (Örn: UserRepository) bunları dolduracak ---
    // Veritabanı satırını (ResultRow) alıp kotlin nesnesine (TEntity) çevirir.
    abstract fun rowToEntity(row: ResultRow): TEntity
    // TEntity verilerini veritabanı kolonlarına(statement) yerleştirir
    abstract fun TTable.mapToTable(statement: UpdateBuilder<*>, entity: TEntity)
    // --- Okuma işlemleri (Read) ---
    // FindById
    override fun findById(id: ID): TEntity? = transaction {
        table.selectAll().where { table.id eq id }
            .map { rowToEntity(it) }.singleOrNull()
    }
    // FindAll
    override fun findAll(): List<TEntity> = transaction {
        table.selectAll().map { rowToEntity(it) }
    }
    // ExistById
    override fun existsById(id: ID): Boolean = transaction {
        table.selectAll().where { table.id eq id }.count() > 0
    }
    // --- Yazma işlemleri (Write) ---
    // Insert
    override fun insert(entity: TEntity): TEntity = transaction {
        // insertAndGetId -> tablodaki otomatik artan ID'yi döner.
        val newId = table.insertAndGetId { mapToTable(it,entity) }
        // eklenen nesneyi yeni ID'si ile beraber tekrar çekip dönüyoruz.
        findById(newId.value)!!
    }
    // Update
    override fun update(entity: TEntity): TEntity = transaction {
        val entityId = entity.id ?: throw IllegalArgumentException("Güncelleme için ID gereklidir.")
        table.update(where = {table.id eq entityId}){
            mapToTable(it,entity)
        }
        entity
    }
    // Delete
    override fun delete(id: ID) = transaction {
        table.deleteWhere { table.id eq id }
        Unit // Kotlin'de void karşılığıdır.
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