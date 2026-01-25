package org.erbalkan.kernel.business.validator

interface Validator<in T> {
    fun validate(input: T): String?
    // Hata varsa mesajı, yoksa null döner.
}

/*
class EmailValidator : Validator<String> {
    override fun validate(input: String): String? {
        return if (!input.contains("@")) "Geçersiz email" else null
    }
}
* */