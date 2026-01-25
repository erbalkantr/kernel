package org.erbalkan.kernel.business.validator

class CompositeValidator<in T>(
    private val validators: List<Validator<T>>
) : Validator<T> {
    override fun validate(input: T): String? {
        // Tüm listeyi gez, ilk hata bulduğun anda o hatayı dön.
        for (v in validators) {
            val error = v.validate(input)
            if (error != null) return error
        }
        return null // Hiç hata yoksa null dön.
    }
}