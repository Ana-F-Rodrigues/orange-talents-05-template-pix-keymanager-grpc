package br.com.zup.pix.buscar

import br.com.zup.pix.BuscaChavePixPorChave
import br.com.zup.pix.BuscaChavePixPorIdPixRequest
import br.com.zup.pix.BuscaChavePixResponse
import br.com.zup.pix.KeymanagerBuscaGrpcServiceGrpc
import br.com.zup.pix.chave.ChavePixNaoEncontradaException
import br.com.zup.pix.chave.ChavePixRepository
import br.com.zup.pix.compartilhados.ErrorHandler
import br.com.zup.pix.externos.BancoCentralClient
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
@ErrorHandler
class BuscaChavePixGrpc(
    val repository: ChavePixRepository,
    val bcb: BancoCentralClient,
    val buscaChavePixPorIdService: BuscaChavePixPorIdService
): KeymanagerBuscaGrpcServiceGrpc.KeymanagerBuscaGrpcServiceImplBase() {

    override fun buscaPorChave(
        request: BuscaChavePixPorChave,
        responseObserver: StreamObserver<BuscaChavePixResponse>
    ) {
        val chavePix = repository.findByChave(request.chave)

        if(chavePix == null){
            val respostaBcb = bcb.busca(request.chave)
            val dados = respostaBcb.body() ?:  throw ChavePixNaoEncontradaException("Chave Pix não encontrada")
            responseObserver.onNext(dados.paraBuscaChavePixResponse())
        }else{
            responseObserver.onNext(chavePix.paraBuscaChavePixResponse())
        }

        responseObserver.onCompleted()
    }

    override fun buscaPorIdPix(
        request: BuscaChavePixPorIdPixRequest,
        responseObserver: StreamObserver<BuscaChavePixResponse>
    ) {
        val chavePix = buscaChavePixPorIdService.busca(BuscaPorIdForm(
            idTitular = request.idTitular,
            idPix = request.idPix
        ))

        responseObserver.onNext(chavePix.paraBuscaChavePixResponse())
        responseObserver.onCompleted()
    }
}