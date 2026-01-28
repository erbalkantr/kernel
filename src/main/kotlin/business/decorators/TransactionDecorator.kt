package org.erbalkan.kernel.business.decorators

import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.utilities.results.Result
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class TransactionDecorator<in TRequest, out TResponse: Result>(
    decorated: UseCase<TRequest,TResponse>
) : UseCaseDecorator<TRequest,TResponse>(decorated) {

    override suspend fun execute(request: TRequest): TResponse {
        // Tüm Use Case'i tek bir veritabanı transaction'ı içine alıyoruz.
        // Eğer içeride bir hata (exception) fırlarsa Exposed otomatik 'rollback' yapar.
        return suspendTransaction { super.execute(request) }
    }
}