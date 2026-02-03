package org.erbalkan.kernel.security

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.erbalkan.kernel.security.data.Role
import org.erbalkan.kernel.security.data.TokenConfig
import org.erbalkan.kernel.security.data.UserSession

fun Application.configureSecurity(config: TokenConfig) {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "Kernel API"
            verifier(
                JWT.require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                // 1. JWT içindeki claim'leri oku
                val userId = credential.payload.getClaim("userId").asString()
                val email = credential.payload.getClaim("email").asString()

                // 2. Rolleri oku ve bizim Enum (Role) yapımıza çevir
                val rolesClaim = credential.payload.getClaim("roles").asList(String::class.java) ?: emptyList()
                val roles = rolesClaim.mapNotNull { roleName ->
                    runCatching { Role.valueOf(roleName) }.getOrNull()
                }

                // 3. Eğer geçerli bilgiler varsa UserSession oluştur (Artık Principal interface'i gerekmiyor)
                if (!userId.isNullOrBlank() && !email.isNullOrBlank()) {
                    UserSession(userId, email, roles)
                } else {
                    null
                }
            }
        }
    }
}