package br.com.zup.pix.chave

import br.com.zup.pix.cadastro.ChavePixForm
import br.com.zup.pix.pix.ValidPixKeyValidator
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

internal class ValidPixKeyValidatorTest{

    @Test
    internal fun `deve validar se o TipoChave for Nulo`() {
        val chavePixForm = ChavePixForm(
            tipoChave = null,
            tipoConta = TipoConta.CONTA_POUPANCA,
            chave = "teste",
            idTitular = UUID.randomUUID().toString()
        )

        val valid = ValidPixKeyValidator().isValid(chavePixForm, null)
        assertTrue(valid)
    }

    @Test
    internal fun `deve validar se a Chave for Nula`() {
        val chavePixForm = ChavePixForm(
            tipoChave = TipoChave.CHAVE_ALEATORIA,
            tipoConta = TipoConta.CONTA_POUPANCA,
            chave = null,
            idTitular = UUID.randomUUID().toString()
        )

        val valid = ValidPixKeyValidator().isValid(chavePixForm, null)
        assertTrue(valid)
    }
}