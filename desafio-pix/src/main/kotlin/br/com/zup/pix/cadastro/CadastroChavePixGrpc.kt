package br.com.zup.pix.cadastro

import br.com.zup.pix.ChavePixRequest
import br.com.zup.pix.ChavePixResponse
import br.com.zup.pix.KeyManagerGRPCServiceGrpc
import br.com.zup.pix.compartilhados.ErrorHandler
import br.com.zup.pix.pix.paraChavePixForm
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
@ErrorHandler
class CadastroChavePixGrpc(
    val cadastroChavePixService: CadastroChavePixService
): KeyManagerGRPCServiceGrpc.KeyManagerGRPCServiceImplBase() {

    override fun cadastro(request: ChavePixRequest, responseObserver: StreamObserver<ChavePixResponse>) {
        val chavePixForm = request.paraChavePixForm()

        val chavePix = cadastroChavePixService.cadastro(chavePixForm)

        responseObserver.onNext(ChavePixResponse
            .newBuilder()
            .setId(chavePix.uuid.toString())
            .build())
        responseObserver.onCompleted()
    }
}