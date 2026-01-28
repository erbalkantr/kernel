package org.erbalkan.kernel.business.decorators

import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.utilities.results.Result
import java.util.logging.Logger

class LoggingDecorator<in TRequest, out TResponse: Result>(
    decorated: UseCase<TRequest,TResponse>
) : UseCaseDecorator<TRequest,TResponse>(decorated) {

    private val logger = Logger.getLogger(decorated.javaClass.name)
    override suspend fun execute(request: TRequest): TResponse {
        val useCaseName = decorated.javaClass.simpleName
        logger.info("[START] Executing $useCaseName with request: $request")

        val startTime = System.currentTimeMillis()
        // Asıl işi yap
        val result = super.execute(request)
        val duration = System.currentTimeMillis() - startTime

        if(result.success){
            logger.info("[SUCCESS] $useCaseName finished in $duration ms")
        } else {
            logger.warning("[FAILURE] $useCaseName failed in $duration ms. Message: ${result.message}")
        }
        return result
    }
}