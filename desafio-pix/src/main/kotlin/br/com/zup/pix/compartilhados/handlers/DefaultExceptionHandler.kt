//package br.com.zup.pix.compartilhados.handlers
//
//import br.com.zup.pix.compartilhados.ExceptionHandler
//import io.grpc.Status.FAILED_PRECONDITION
//import io.micronaut.transaction.support.TransactionSynchronization
//
//
///
//class DefaultExceptionHandler : ExceptionHandler<Exception> {
//
//    override fun handle(e: Exception) {
//        val status = when (e) {
//            is IllegalArgumentException -> TransactionSynchronization.Status.INVALID_ARGUMENT.withDescription(e.message)
//            is IllegalStateException -> TransactionSynchronization.Status.FAILED_PRECONDITION.withDescription(e.message)
//            else -> TransactionSynchronization.Status.UNKNOWN
//        }
//        return ExceptionHandler.StatusWithDetails(status.withCause(e))
//    }
//
//    override fun supports(e: Exception): Boolean {
//        return true
//    }
//
//}