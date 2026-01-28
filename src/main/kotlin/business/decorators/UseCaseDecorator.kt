package org.erbalkan.kernel.business.decorators

import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.utilities.results.Result

abstract class UseCaseDecorator<in TRequest, out TResponse: Result>(
    protected val decorated: UseCase<TRequest,TResponse>
) : UseCase<TRequest,TResponse> {
    override suspend fun execute(request: TRequest): TResponse {
        // Alt sınıflar(logging,validation vb.) burada ek işlevler ekleyecek.
        return decorated.execute(request)
    }

    // Yeni eklediğimiz metod: Hata üretimini de sarmalanan sınıfa delege ediyoruz.
    // Bu sayede tip güvenliğini (Type Safety) korumuş oluyoruz.
    override fun createError(message: String): TResponse {
        return decorated.createError(message)
    }
}