package org.erbalkan.kernel.security.data

import kotlinx.serialization.Serializable
import kotlin.time.Duration

/**
 * JWT üretimi ve doğrulanması için gerekli yapılandırma bilgileri.
 */
@Serializable
data class TokenConfig(
    val issuer: String,      // Token'ı üreten (Örn: "erbalkan-kernel")
    val audience: String,    // Token'ın hedef kitlesi (Örn: "erbalkan-users")
    val expiresIn: Duration, // Token geçerlilik süresi (Örn: 24.hours)
    val secret: String       // Şifreleme anahtarı (Çok gizli tutulmalı!)
)

/*
TokenConfig, JWT ayarlarını
(gizli anahtar, süre, kitle vb.)
derli toplu bir şekilde tuttuğumuz sınıftır.
* */