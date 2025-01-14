package br.com.zup.pix.valid

import br.com.zup.pix.chave.TipoChave
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ValidarFormatoChaveTest{

    @Test
    internal fun `deve validar uma chave do tipo CPF`() {
        val cpfValido = "14025844664"
        val tipoChave = TipoChave.CPF
        val erro = tipoChave.validarFormatoChave(cpfValido)

        assertFalse(erro.posuiErro)
        assertEquals("",erro.menssagem)
    }

    @Test
    internal fun `deve invalidar uma chave do tipo CPF mal formatado`() {
        val cpfMalFormatado = "140.258.446-64"
        val tipoChave = TipoChave.CPF
        val erro = tipoChave.validarFormatoChave(cpfMalFormatado)

        assertTrue(erro.posuiErro)
        assertEquals("Não e um CPF valido ou esta mal formatado",erro.menssagem)
    }

    @Test
    internal fun `deve invalidar uma chave do tipo CPF invalido`() {
        val cpfMalFormatado = "140258446645"
        val tipoChave = TipoChave.CPF
        val erro = tipoChave.validarFormatoChave(cpfMalFormatado)

        assertTrue(erro.posuiErro)
        assertEquals("Não e um CPF valido ou esta mal formatado",erro.menssagem)
    }

    @Test
    internal fun `deve validar uma chave do tipo TELEFONE_CELULAR`() {
        val telefoneValido = "+5585988714077"
        val tipoChave = TipoChave.TELEFONE_CELULAR
        val erro = tipoChave.validarFormatoChave(telefoneValido)

        assertFalse(erro.posuiErro)
        assertEquals("",erro.menssagem)
    }

    @Test
    internal fun `deve invalidar uma chave do tipo TELEFONE_CELULAR invalido`() {
        val telefoneInvalido = "558598871407a"
        val tipoChave = TipoChave.TELEFONE_CELULAR
        val erro = tipoChave.validarFormatoChave(telefoneInvalido)

        assertTrue(erro.posuiErro)
        assertEquals("Não e um Telefone valido",erro.menssagem)
    }

    @Test
    internal fun `deve validar uma chave do tipo EMAIL`() {
        val emailValido = "ana.flavia@zup.com.br"
        val tipoChave = TipoChave.EMAIL
        val erro = tipoChave.validarFormatoChave(emailValido)

        assertFalse(erro.posuiErro)
        assertEquals("",erro.menssagem)
    }

    @Test
    internal fun `deve invalidar uma chave do tipo EMAIL invalido`() {
        val emailInvalido = "ana.flaviazup.com.br"
        val tipoChave = TipoChave.EMAIL
        val erro = tipoChave.validarFormatoChave(emailInvalido)

        assertTrue(erro.posuiErro)
        assertEquals("Não e um Email valido",erro.menssagem)
    }

    @Test
    internal fun `deve validar uma chave do tipo CHAVE_ALEATORIA quando a chave estiver em branco`() {
        val chaveEmBranco = ""
        val tipoChave = TipoChave.CHAVE_ALEATORIA
        val erro = tipoChave.validarFormatoChave(chaveEmBranco)

        assertFalse(erro.posuiErro)
        assertEquals("",erro.menssagem)
    }

    @Test
    internal fun `deve invalidar uma chave do tipo CHAVE_ALEATORIA quando a chave não estiver em branco`() {
        val chavePreenchida = "2153245636"
        val tipoChave = TipoChave.CHAVE_ALEATORIA
        val erro = tipoChave.validarFormatoChave(chavePreenchida)

        assertTrue(erro.posuiErro)
        assertEquals("Para criar uma chave aleatoria o campo 'chave' deve estar em branco",erro.menssagem)
    }


}