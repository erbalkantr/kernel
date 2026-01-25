package org.erbalkan.kernel.business.abstracts

import org.erbalkan.kernel.utilities.results.Result

/**
 * TRequest: İşlemin girdi tipi (Örn: RegisterUserDto)
 * TResponse: İşlemin çıktı tipi (Örn: DataResult<UserDto>)
 */
interface UseCase<in TRequest, out TResponse: Result> {
    fun execute(request: TRequest): TResponse
    // Hata durumunda dönecek nesneyi oluşturma sorumluluğu
    fun createError(message: String): TResponse
}

/*
Abstraction: Tüm iş mantığı sınıfları aynı tipte olacağı için, Decorator yazarken "herhangi bir Use Case'i sarmalayabilirim" diyebileceğiz.

Type Safety: Giriş ve çıkış tiplerini jenerik yaparak hata payını azalttık.
* */