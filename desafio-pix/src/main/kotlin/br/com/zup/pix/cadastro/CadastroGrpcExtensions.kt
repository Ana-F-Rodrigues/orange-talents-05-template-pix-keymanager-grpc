package br.com.zup.pix.pix

import br.com.zup.pix.ChavePixRequest
import br.com.zup.pix.cadastro.ChavePixForm
import br.com.zup.pix.chave.TipoChave
import br.com.zup.pix.chave.TipoConta


fun ChavePixRequest.paraChavePixForm(): ChavePixForm {
    return ChavePixForm(
        idTitular = idTitular,
        tipoChave = tipoChave.paraMeuEnum(),
        tipoConta = tipoConta.paraMeuEnum(),
        chave = chave
    )
}

fun br.com.zup.pix.TipoConta.paraMeuEnum(): TipoConta? {
    return when(this){
        br.com.zup.pix.TipoConta.DESCONHECIDO_TIPO_CONTA -> null
        else -> TipoConta.valueOf(this.name)
    }
}


fun br.com.zup.pix.TipoChave.paraMeuEnum(): TipoChave? {
    return when(this){
        br.com.zup.pix.TipoChave.DESCONHECIDO_TIPO_CHAVE -> null
        else -> TipoChave.valueOf(this.name)
    }
}

fun TipoConta.paraEnumGrpc(): br.com.zup.pix.TipoConta {
    return br.com.zup.pix.TipoConta.valueOf(this.name)
}


fun TipoChave.paraEnumGrpc(): br.com.zup.pix.TipoChave {
    return br.com.zup.pix.TipoChave.valueOf(this.name)
}