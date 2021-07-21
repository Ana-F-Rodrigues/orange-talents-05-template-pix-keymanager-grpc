package br.com.zup.pix.compartilhados


data class ErroNaValidacao(
    val posuiErro: Boolean,
    val menssagem : String = ""
)