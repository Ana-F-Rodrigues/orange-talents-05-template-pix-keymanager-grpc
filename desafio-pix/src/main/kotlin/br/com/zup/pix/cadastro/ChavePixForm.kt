package br.com.zup.pix.cadastro

import br.com.zup.pix.chave.TipoChave
import br.com.zup.pix.pix.ValidKeyPix
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull
import javax.validation.constraints.Size

@ValidKeyPix
@Introspected
data class ChavePixForm(
    @field:NonNull
    @field:UUIDValido
    val idTitular: String?,
    @field:NonNull
    val tipoChave: TipoChave?,
    @field:NonNull
    val tipoConta: TipoConta?,
    @field:NonNull
    @field:Size(max = 77)
    val chave: String? = ""
){

    fun paraChavePix(chave: String,contaAssociada: ContaAssociada): ChavePix {
        return ChavePix(
            idTitular = idTitular!!.toUUID(),
            tipoChave = tipoChave!!,
            tipoConta = tipoConta!!,
            chave = chave,
            conta = contaAssociada
        )
    }
}