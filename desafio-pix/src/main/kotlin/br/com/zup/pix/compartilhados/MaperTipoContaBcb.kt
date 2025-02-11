package br.com.zup.pix.compartilhados

import br.com.zup.pix.chave.TipoConta
import br.com.zup.pix.externos.AccountType


private object MaperAccountTypeBcb {
    val tipoContaParaAccountType: Map<TipoConta, AccountType> = mapOf(
        TipoConta.CONTA_POUPANCA to  AccountType.SVGS,
        TipoConta.CONTA_CORRENTE to  AccountType.CACC
    )

    val accountTypeParaTipoConta: Map<AccountType, TipoConta> = mapOf(
        AccountType.SVGS to  TipoConta.CONTA_POUPANCA,
        AccountType.CACC to  TipoConta.CONTA_CORRENTE
    )
}

fun TipoConta.paraAccountType() = MaperAccountTypeBcb.tipoContaParaAccountType.get(this)
fun AccountType.paraMeuEnum() = MaperAccountTypeBcb.accountTypeParaTipoConta.get(this)

fun AccountType.paraEnumGrpc(): br.com.zup.pix.TipoConta {
    return br.com.zup.pix.TipoConta.valueOf(this.paraMeuEnum()!!.name)
}