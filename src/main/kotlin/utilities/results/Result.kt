package org.erbalkan.kernel.utilities.results

sealed class Result(val success: Boolean, val message: String?)

class SuccessResult(message: String? = null) : Result(true, message)
class ErrorResult(message: String? = null) : Result(false, message)

// İçinde veri de barındıran sonuçlar
sealed class DataResult<T>(success: Boolean, message: String?, val data: T?) : Result(success, message)

class SuccessDataResult<T>(data: T, message: String? = null) : DataResult<T>(true, message, data)
class ErrorDataResult<T>(message: String? = null, data: T? = null) : DataResult<T>(false, message, data)

