package org.erbalkan.kernel.security.decorator

import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.business.decorators.UseCaseDecorator
import org.erbalkan.kernel.security.data.Role
import org.erbalkan.kernel.security.currentSession
import org.erbalkan.kernel.utilities.results.Result
/**
 * Bu dekoratör, UseCase çalışmadan önce rol kontrolü yapar.
 */
class RoleDecorator<in TRequest, out TResponse : Result>(
    decorated: UseCase<TRequest, TResponse>,
    private val requiredRoles: List<Role>
) : UseCaseDecorator<TRequest, TResponse>(decorated) {

    override suspend fun execute(request: TRequest): TResponse {
        // 1. O anki oturum bilgisini (SecurityContext'ten) çekiyoruz
        val session = currentSession()

        // 2. Oturum yoksa hata dön
        if (session == null) {
            return createError("Bu işlem için giriş yapmalısınız.")
        }

        // 3. Kullanıcının rollerinden en az biri istenen rollerden biriyse izin ver
        val hasPermission = session.roles.any { it in requiredRoles }

        if (!hasPermission) {
            return createError("Bu işlem için yetkiniz yetersiz. Gerekli roller: $requiredRoles")
        }

        // 4. Her şey yolundaysa bir sonraki katmana (veya asıl UseCase'e) geç
        return decorated.execute(request)
    }
}

/*
Bu sınıf, senin UseCaseDecorator
sınıfından miras alacak. Amacı çok basit:
execute edilmeden önce "İçerideki kullanıcının rolü,
bu işi yapmaya yetiyor mu?" diye bakmak.
* */