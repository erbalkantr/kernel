package org.erbalkan.kernel.business.builders

import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.business.decorators.LoggingDecorator
import org.erbalkan.kernel.business.decorators.TransactionDecorator
import org.erbalkan.kernel.business.decorators.ValidationDecorator
import org.erbalkan.kernel.business.validator.CompositeValidator
import org.erbalkan.kernel.business.validator.Validator
import org.erbalkan.kernel.security.data.Role
import org.erbalkan.kernel.security.decorator.RoleDecorator
import org.erbalkan.kernel.utilities.results.Result

class UseCaseBuilder<TRequest, TResponse : Result>(
    private var useCase: UseCase<TRequest, TResponse>
) {
    // Kuralları ve Rolleri biriktirdiğimiz listeler
    private val validators = mutableListOf<Validator<TRequest>>()
    private val requiredRoles = mutableListOf<Role>()

    private var isLoggingEnabled = false
    private var isTransactionEnabled = false

    // 1. Dışarıdan hazır bir validator sınıfı eklemek için
    fun withValidator(validator: Validator<TRequest>): UseCaseBuilder<TRequest, TResponse> {
        validators.add(validator)
        return this
    }

    // 2. Anlık (lambda) kural eklemek için (Pratik kullanım)
    fun withRule(rule: (TRequest) -> String?): UseCaseBuilder<TRequest, TResponse> {
        validators.add(object : Validator<TRequest> {
            override fun validate(input: TRequest): String? = rule(input)
        })
        return this
    }

    // 3. Yetki/Rol eklemek için
    fun withRole(role: Role): UseCaseBuilder<TRequest, TResponse> {
        requiredRoles.add(role)
        return this
    }

    fun withLogging(): UseCaseBuilder<TRequest, TResponse> {
        isLoggingEnabled = true
        return this
    }

    fun withTransaction(): UseCaseBuilder<TRequest, TResponse> {
        isTransactionEnabled = true
        return this
    }

    /**
     * Zinciri inşa ettiğimiz yer.
     * Sıralama Mantığı: Logging (En dış) -> Transaction -> Role Check -> Validation -> UseCase (En iç)
     */
    fun build(): UseCase<TRequest, TResponse> {

        // Katman 1: Validasyon (İş mantığına en yakın katman)
        if (validators.isNotEmpty()) {
            val composite = CompositeValidator(validators)
            useCase = ValidationDecorator(useCase, composite)
        }

        // Katman 2: Rol Kontrolü (Validasyondan geçse bile yetkisi yoksa burada durur)
        if (requiredRoles.isNotEmpty()) {
            useCase = RoleDecorator(useCase, requiredRoles)
        }

        // Katman 3: Veritabanı Transaction yönetimi
        if (isTransactionEnabled) {
            useCase = TransactionDecorator(useCase)
        }

        // Katman 4: Loglama (Hataları ve tüm süreci en dıştan izlemek için)
        if (isLoggingEnabled) {
            useCase = LoggingDecorator(useCase)
        }

        return useCase
    }
}

/**
 * Tüm UseCase'ler için 'decorate' yeteneği kazandırır.
 */
fun <TRequest, TResponse : Result> UseCase<TRequest, TResponse>.decorate(): UseCaseBuilder<TRequest, TResponse> {
    return UseCaseBuilder(this)
}