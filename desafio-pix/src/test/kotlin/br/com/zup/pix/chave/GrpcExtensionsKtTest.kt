package br.com.zup.pix.chave

import br.com.zup.pix.ChavePixRequest
import br.com.zup.pix.cadastro.ChavePixForm
import br.com.zup.pix.pix.paraChavePixForm
import br.com.zup.pix.pix.paraMeuEnum
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class GrpcExtensionsKtTest{

    @Test
    internal fun `deve conveter ChavePixRequest_TipoChave para TipoChave`() {
        val chavePixRequest_TipoChave = br.com.zup.pix.TipoChave.CHAVE_ALEATORIA
        val tipoChave = chavePixRequest_TipoChave.paraMeuEnum()

        assertEquals(TipoChave.CHAVE_ALEATORIA,tipoChave)
    }

    @Test
    internal fun `deve conveter ChavePixRequest_TipoConta para TipoConta`() {
        val chavePixRequest_TipoConta = br.com.zup.pix.TipoConta.CONTA_CORRENTE
        val tipoConta = chavePixRequest_TipoConta.paraMeuEnum()

        assertEquals(TipoConta.CONTA_CORRENTE,tipoConta)
    }

    @Test
    internal fun `deve conveter ChavePixRequest para ChavePixForm`() {
        val idTitular = UUID.randomUUID().toString()
        val chavePixRequest = ChavePixRequest.newBuilder()
            .setChave("testando@email.com")
            .setTipoChave(br.com.zup.pix.TipoChave.EMAIL)
            .setTipoConta(br.com.zup.pix.TipoConta.CONTA_POUPANCA)
            .setIdTitular(idTitular)
            .build()
        val chavePixForm = chavePixRequest.paraChavePixForm()

        val chavePixFormEsperado = ChavePixForm(
            idTitular = idTitular,
            tipoChave = TipoChave.EMAIL,
            tipoConta = TipoConta.CONTA_POUPANCA,
            chave = "testando@email.com"
        )
        assertEquals(chavePixFormEsperado,chavePixForm)
    }
}