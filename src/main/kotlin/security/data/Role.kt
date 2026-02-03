package org.erbalkan.kernel.security.data

/**
 * Sistemdeki kullanıcı yetki seviyeleri.
 */
enum class Role {
    ADMIN,
    USER,
    MANAGER
}

/*
Güvenlik sisteminin ilk adımı "kimin ne yapmaya yetkisi var?"
sorusuna yanıt vermektir. Karmaşayı önlemek
için rolleri bir Enum olarak tanımlıyoruz.
Bu sayede String yazım hatalarından kurtulacağız.
* */