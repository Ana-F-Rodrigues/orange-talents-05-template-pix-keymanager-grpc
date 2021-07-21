package br.com.zup.pix


enum class TipoConta(
    val siglaBancoCentral: String
) {
    CONTA_CORRENTE("CACC"),
    CONTA_POUPANCA("SVGS");
}