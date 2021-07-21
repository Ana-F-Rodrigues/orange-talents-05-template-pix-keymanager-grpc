package br.com.zup.pix.remover

import br.com.zup.pix.UUIDValido
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotNull

@Introspected
data class RemoveChavePixForm (
    @field:NotNull
    @field:UUIDValido
    val idPix: String?,
    @field:NotNull
    @field:UUIDValido
    val idTitular: String?
)