package org.erbalkan.kernel.entities

import java.time.Instant

interface Entity<ID> {
    val id: ID?
    val createdAt: Instant
    var updatedAt: Instant?
}