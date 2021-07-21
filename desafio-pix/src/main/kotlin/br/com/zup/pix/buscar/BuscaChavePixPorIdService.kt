package br.com.zup.pix.buscar

import br.com.zup.pix.UUIDValido
import br.com.zup.pix.chave.ChavePix
import br.com.zup.pix.chave.ChavePixNaoEncontradaException
import br.com.zup.pix.chave.ChavePixRepository
import br.com.zup.pix.externos.BancoCentralClient
import br.com.zup.pix.toUUID
import io.micronaut.core.annotation.Introspected
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Singleton
@Validated
class BuscaChavePixPorIdService(
    val repository: ChavePixRepository,
    val bancoCentral: BancoCentralClient
) {

    val logger = LoggerFactory.getLogger(this::class.java)

    fun busca(@Valid busca: BuscaPorIdForm): ChavePix {

        val chavePix = repository.findByUuid(busca.idPix.toUUID())

        if(chavePix == null){
            throw ChavePixNaoEncontradaException("Chave Pix não encontrada")
        }

        if(!chavePix.pertense(busca.idTitular.toUUID())){
            throw IllegalArgumentException("A chave so pode ser vista pelo seu dono")
        }

        val respostaBcb = bancoCentral.busca(chavePix.chave)
        if(respostaBcb.body() == null){
            logger.warn("Chave não encontrada no BCB")
            throw ChavePixNaoEncontradaException("Chave Pix não encontrada")
        }
        respostaBcb.body() ?:  throw ChavePixNaoEncontradaException("Chave Pix não encontrada")

        return chavePix
    }
}

@Introspected
data class BuscaPorIdForm(
    @field:NotBlank
    @field:UUIDValido
    val idPix: String,
    @field:NotBlank
    @field:UUIDValido
    val idTitular: String,
)