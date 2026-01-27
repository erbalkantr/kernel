package org.erbalkan.kernel.business.builders

import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.business.decorators.LoggingDecorator
import org.erbalkan.kernel.business.decorators.TransactionDecorator
import org.erbalkan.kernel.business.decorators.ValidationDecorator
import org.erbalkan.kernel.business.validator.CompositeValidator
import org.erbalkan.kernel.business.validator.Validator
import org.erbalkan.kernel.utilities.results.Result

class UseCaseBuilder<TRequest,TResponse: Result>(
    private var useCase: UseCase<TRequest,TResponse>
){
    // Kuralları biriktirdiğimiz liste
    private val validators = mutableListOf<Validator<TRequest>>()
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
     * Sıralama önemlidir: Genelde Logging -> Transaction -> Validation -> UseCase
     */
    fun build(): UseCase<TRequest, TResponse> {
        // Önce validasyonları tek bir paket (Composite) yapalım ve sarmalayalım
        if (validators.isNotEmpty()) {
            val composite = CompositeValidator(validators)
            useCase = ValidationDecorator(useCase, composite)
        }

        // Sonra Transaction katmanını ekleyelim
        if (isTransactionEnabled) {
            useCase = TransactionDecorator(useCase)
        }

        // En dışa Logging katmanını ekleyelim (Tüm süreci izlemek için)
        if (isLoggingEnabled) {
            useCase = LoggingDecorator(useCase)
        }

        return useCase
    }
}

/*
Neyi Çözdük? Neden Bu Daha İyi?

    Performans (Katman Yönetimi): Eğer 10 tane kuralın varsa, Builder bunları validators listesinde toplar. build() anında tek bir ValidationDecorator oluşturur. Eğer her kural için ayrı dekoratör oluşturulsaydı, hafızada 10 katmanlı bir nesne olacaktı. Şimdi sadece 1 katman var.

    Okunabilirlik: Kullanıcı (geliştirici) withRule diyerek hızlıca kural ekleyebilir veya withValidator diyerek karmaşık bir validator sınıfını enjekte edebilir.

    Sıralama Kontrolü: build() metodunun içinde dekoratörlerin eklenme sırasını biz kontrol ediyoruz.

        Örnek: Loglamayı en dışa koyduk ki validasyon hatası alsa bile loglansın.

        Örnek: Validasyonu transaction'ın içine veya dışına koyma kararını merkezi olarak buradan yönetebiliriz.


val registerUseCase = CreateUserUseCase(repo, mapper)
    .decorate()
    .withLogging()
    .withTransaction()
    .withRule { req -> if (req.email.isBlank()) "Email boş olamaz" else null }
    .withRule { req -> if (req.password.length < 8) "Şifre kısa" else null }
    .withValidator(ComplexUserValidator(repo)) // Veritabanı kontrolü yapan ağır kural
    .build()

    Mentor Notu

Fark ettiysen, CompositeValidator'ı kullanıcının gözünden sakladık. Kullanıcı sadece kural eklediğini sanıyor ama biz arka planda Composite Pattern kullanarak işi optimize ediyoruz. İşte "Uzman Yazılım Geliştirici" bakış açısı tam olarak budur: Karmaşıklığı içeride çöz, dışarıya tertemiz bir API sun.
*/


/**
 * Tüm UseCase'ler için 'decorate' yeteneği kazandırır.
 * Bu sayede 'CreateUserUseCase(...).decorate()' diyerek zinciri başlatabiliriz.
 */
fun <TRequest, TResponse : Result> UseCase<TRequest, TResponse>.decorate(): UseCaseBuilder<TRequest, TResponse> {
    return UseCaseBuilder(this)
}