package br.com.zup.pix.chave


enum class TipoConta(
    val siglaBancoCentral: String
) {
    CONTA_CORRENTE("CACC"),
    CONTA_POUPANCA("SVGS");
}