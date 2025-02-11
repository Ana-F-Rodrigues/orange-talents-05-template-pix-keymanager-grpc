package br.com.zup.pix.remover

import br.com.zup.pix.chave.ChavePixNaoEncontradaException
import br.com.zup.pix.chave.ChavePixRepository
import br.com.zup.pix.externos.BancoCentralClient
import br.com.zup.pix.toUUID
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.validation.Valid

@Singleton
@Validated
class RemoveChavePixService(
    val repository: ChavePixRepository,
    val bancoCentralClient: BancoCentralClient
) {

    fun remover(@Valid removeChavePixForm: RemoveChavePixForm){

        val chavePix = repository.findByUuid(removeChavePixForm.idPix!!.toUUID())

        if(chavePix == null){
            throw ChavePixNaoEncontradaException("Chave não encontrada")
        }

        if(chavePix.idTitular != removeChavePixForm.idTitular?.toUUID()){
            throw IllegalArgumentException("A chave pode ser removida somente pelo seu dono")
        }

        val responseBcb = bancoCentralClient.deleta(key = chavePix.chave)
        if ((responseBcb.status != HttpStatus.OK) && (responseBcb.status != HttpStatus.NOT_FOUND)) {
            throw IllegalStateException("Erro ao remover chave Pix no Banco Central do Brasil (BCB)")
        }

        repository.delete(chavePix)
    }
}