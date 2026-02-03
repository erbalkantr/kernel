package org.erbalkan.kernel.security

import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.principal
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.withContext
import org.erbalkan.kernel.security.data.UserSession

/**
 * Bu fonksiyon, Ktor Route içinde senin UseCase'lerini
 * bir 'Güvenlik Koridoru'na sokar.
 */
suspend fun <T : Any, R : Any> PipelineContext<T, ApplicationCall>.withSecurityContext(
    block: suspend () -> R
): R {
    // 1. Ktor'un kendi Auth sisteminden gelen kullanıcıyı al
    val session = call.principal<UserSession>()

    // 2. Bu kullanıcıyı bizim yazdığımız SecurityContext içine koy ve
    // Coroutine hattına (Context) enjekte et.
    return withContext(SecurityContext(session)) {
        block()
    }
}

/*
Bu fonksiyon, Ktor'un isteği işleme sürecine (pipeline) sızar,
o anki kullanıcıyı (Principal) alır ve
bizim Coroutine hattımıza (withContext) enjekte eder.
* */

/*
Neden Buna İhtiyacımız Var?

Ktor, kullanıcıyı doğrular (Authentication) ve onu call.principal
içine koyar. Ancak senin Kernel katmanın
Ktor'a bağımlı değildir. Kernel, kullanıcıyı
currentSession() ile Coroutine Context üzerinden arar.
İşte bu fonksiyon, Ktor'daki bilgiyi
Kernel'ın anlayacağı dile (Coroutine Context) tercüme eder.
* */