package br.com.zup.pix.cadastro

import br.com.zup.pix.chave.ChavePix
import br.com.zup.pix.chave.ChavePixExistenteException
import br.com.zup.pix.chave.ChavePixRepository
import br.com.zup.pix.externos.BancoCentralClient
import br.com.zup.pix.externos.CreatePixKeyRequest
import br.com.zup.pix.externos.ErpItau
import br.com.zup.pix.toUUID
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.validation.Valid

@Validated
@Singleton
class CadastroChavePixService(
    val repository: ChavePixRepository,
    val erpItau: ErpItau,
    val bancoCentral: BancoCentralClient
) {

    fun cadastro(@Valid chavePixForm: ChavePixForm): ChavePix {

        if (repository.existsByChave(chavePixForm.chave!!)) // 1
            throw ChavePixExistenteException("ja existe uma chave com este valor")

        val respostaItau = erpItau.buscarConta(chavePixForm.idTitular!!.toUUID(), chavePixForm.tipoConta!!.name)
        val dadosDaConta = respostaItau.body() ?: throw IllegalArgumentException("idTitilar e/ou tipoConta esta incorreto")

        val respostaBcb = bancoCentral.cria(CreatePixKeyRequest(dadosDaConta,chavePixForm))
        val key = respostaBcb.body()?.key ?: throw IllegalArgumentException("?") //TODO

        val chavePix = chavePixForm.paraChavePix(key,dadosDaConta.paraContaAssociada())

        return repository.save(chavePix)
    }
}