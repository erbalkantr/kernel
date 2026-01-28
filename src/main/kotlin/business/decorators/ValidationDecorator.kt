package org.erbalkan.kernel.business.decorators

import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.business.validator.Validator
import org.erbalkan.kernel.utilities.results.Result

class ValidationDecorator<in TRequest, out TResponse: Result>(
    decorated: UseCase<TRequest, TResponse>,
    private val validator: Validator<TRequest>
    // Buraya compositeValidator gelebilir.
) : UseCaseDecorator<TRequest, TResponse>(decorated) {

    override suspend fun execute(request: TRequest): TResponse {
        val error = validator.validate(request)
        if(error != null) return super.createError(error)
        return decorated.execute(request)
    }
}

/*
Builder'ın içindeki withValidator metodu,
senin gönderdiğin kuralları alır
ve gerekirse onları bir CompositeValidator içinde birleştirir.
* */