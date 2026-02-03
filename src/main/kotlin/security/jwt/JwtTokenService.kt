package org.erbalkan.kernel.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.erbalkan.kernel.security.data.TokenConfig
import org.erbalkan.kernel.security.data.UserSession
import java.util.Date
import kotlin.time.Clock
import kotlin.time.toJavaInstant

class JwtTokenService {
    /**
     * Kullanıcı bilgilerini ve konfigürasyonu alarak şifreli JWT üretir.
     */
    fun generateToken(config: TokenConfig, session: UserSession): String {
        val now = Clock.System.now()
        val expiration = now.plus(config.expiresIn)

        return JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withClaim("userId", session.userId)
            .withClaim("email", session.email)
            // Rolleri Enum isimleri (String) olarak JWT içine gömüyoruz
            .withArrayClaim("roles", session.roles.map { it.name }.toTypedArray())
            // Ktor/Java kütüphanesi için Date nesnesine dönüştürüyoruz
            .withExpiresAt(Date.from(expiration.toJavaInstant()))
            .sign(Algorithm.HMAC256(config.secret))
    }
}






/*
JwtTokenService, senin TokenConfig
ayarlarını ve UserSession
bilgilerini alıp, dış dünyaya verilecek olan
o şifreli JWT metnini (string) üreten yerdir.
* */