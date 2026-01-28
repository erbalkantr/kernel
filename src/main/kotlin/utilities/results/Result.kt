package org.erbalkan.kernel.utilities.results

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
open class Result(
    open val success: Boolean,
    open val message: String? = null
)

@Serializable
open class DataResult<T>(
    val data: T?,
    // @Transient ile bu alanın JSON'da tekrar edilmesini engelliyoruz
    @Transient override val success: Boolean = false,
    @Transient override val message: String? = null
) : Result(success, message)

@Serializable
class SuccessDataResult<T>(
    val resultData: T? = null,
    @Transient override val message: String? = null
) : DataResult<T>(resultData, true, message) // success her zaman true

@Serializable
class ErrorDataResult<T>(
    val resultData: T? = null,
    @Transient override val message: String? = null
) : DataResult<T>(resultData, false, message) // success her zaman false
