package org.erbalkan.kernel.security

import kotlinx.coroutines.currentCoroutineContext
import org.erbalkan.kernel.security.data.UserSession
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * Bu sınıf, UserSession'ı Coroutine hattında taşımamızı sağlayan bir "kutu"dur.
 */
class SecurityContext(val session: UserSession?) : AbstractCoroutineContextElement(SecurityContext) {
    companion object Key : CoroutineContext.Key<SecurityContext>
}

/**
 * İşte en kritik fonksiyon.
 * 'currentCoroutineContext()' kullanarak o anki iş hattındaki session'ı bulur.
 */
suspend fun currentSession(): UserSession? {
    return currentCoroutineContext()[SecurityContext]?.session
}

/*
Aşağıdaki kod, UserSession nesnesini
o anki iş parçacığı (coroutine) hattına bağlar.
Böylece uygulama içinde herhangi bir yerden
"şu an kim işlem yapıyor?" diye sorabileceğiz.
* */