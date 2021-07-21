package br.com.zup.pix.listar

import br.com.zup.pix.*
import br.com.zup.pix.buscar.paraTimestamp
import br.com.zup.pix.chave.ChavePix
import br.com.zup.pix.chave.ChavePixRepository
import br.com.zup.pix.compartilhados.ErrorHandler
import br.com.zup.pix.pix.paraEnumGrpc

import  io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
@ErrorHandler
class ListaChavePixGrpc(
    val repository: ChavePixRepository,
    val uuidValidator: UUIDValidator
): KeymanagerListarGrpcServiceGrpc.KeymanagerListarGrpcServiceImplBase() {

    override fun listar(request: ListaChavesRequest, responseObserver: StreamObserver<ListaChavesResponse>) {
        if (!uuidValidator.isValid(request.idTitular,null)){
            throw IllegalArgumentException("UUID Invalido")
        }

        val chaves = repository
            .findByIdTitular(idTitular = request.idTitular.toUUID())
            .map { it.paraChavesResponse() }
            .let {
                ListaChavesResponse.newBuilder()
                    .addAllChaves(it)
                    .build()
            }
        responseObserver.onNext(chaves)
        responseObserver.onCompleted()
    }
}

fun ChavePix.paraChavesResponse(): ListaChavesResponse.ChavesResponse{
    return ListaChavesResponse.ChavesResponse.newBuilder()
        .setTipoChave(tipoChave.paraEnumGrpc())
        .setValorChave(chave)
        .setIdTitular(idTitular.toString())
        .setIdPix(uuid.toString())
        .setTipoConta(tipoConta.paraEnumGrpc())
        .setCriadoEm(criadoEm.paraTimestamp())
        .build()
}