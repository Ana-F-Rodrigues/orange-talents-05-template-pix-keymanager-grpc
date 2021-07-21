//package br.com.zup.pix.compartilhados.handlers
//
//
//import br.com.zup.pix.chave.ChavePixNaoEncontradaException
//import br.com.zup.pix.compartilhados.ExceptionHandler
//import io.micronaut.transaction.support.TransactionSynchronization
//import javax.inject.Singleton
//
//
//@Singleton
//class ChavePixNaoEncontradaExceptionHandler : ExceptionHandler<ChavePixNaoEncontradaException> {
//
//    override fun handle(e: ChavePixNaoEncontradaException): ExceptionHandler.StatusWithDetails {
//        return br.com.zup.pix.compartilhados.ExceptionHandler.StatusWithDetails(
//         //   TransactionSynchronization.Status.NOT_FOUND
//                .withDescription(e.message)
//               // .withCause(e)
//        )
//    }
//
//    override fun supports(e: Exception): Boolean {
//        return e is ChavePixNaoEncontradaException
//    }
//}