package br.com.zup.pix.compartilhados

import br.com.zup.pix.chave.TipoChave
import br.com.zup.pix.externos.keyType


private object MaperKeyTypeBcb {
    val tipoChaveParakeyType: Map<TipoChave, keyType> = mapOf(
        TipoChave.CPF to  keyType.CPF,
        TipoChave.TELEFONE_CELULAR to  keyType.PHONE,
        TipoChave.EMAIL to  keyType.EMAIL,
        TipoChave.CHAVE_ALEATORIA to  keyType.RANDOM,
    )

    val keyTypeParaTipoChave: Map<keyType, TipoChave> = mapOf(
        keyType.CPF to  TipoChave.CPF,
        keyType.PHONE to  TipoChave.TELEFONE_CELULAR,
        keyType.EMAIL to  TipoChave.EMAIL,
        keyType.RANDOM to  TipoChave.CHAVE_ALEATORIA
    )
}

fun TipoChave.paraKeyType() = MaperKeyTypeBcb.tipoChaveParakeyType.get(this)
fun keyType.paraMeuEnum() = MaperKeyTypeBcb.keyTypeParaTipoChave.get(this)

fun keyType.paraEnumGrpc(): br.com.zup.pix.TipoChave {
    return br.com.zup.pix.TipoChave.valueOf(this.paraMeuEnum()!!.name)
}