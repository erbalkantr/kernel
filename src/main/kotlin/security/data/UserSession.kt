package org.erbalkan.kernel.security.data

import kotlinx.serialization.Serializable

/**
 * Giriş yapmış kullanıcının sistemdeki kimlik kartı.
 */
@Serializable
data class UserSession(
    val userId: String,
    val email: String,
    val roles: List<Role> // Kullanıcının sahip olduğu Roller
)

/*
Bir kullanıcı sisteme giriş yaptığında (login olduğunda),
elimizde o kullanıcıya dair hangi bilgilerin
olması gerektiğini belirlemeliyiz. Bu nesne
hem JWT içine gömülecek hem de uygulama içinde
"şu anki kullanıcı kim?" sorusuna yanıt verecek.
* */