package br.com.zup.pix.buscar


import br.com.zup.pix.BuscaChavePixResponse
import br.com.zup.pix.chave.ChavePix
import br.com.zup.pix.compartilhados.Instituicoes
import br.com.zup.pix.compartilhados.paraEnumGrpc
import br.com.zup.pix.externos.PixKeyDetailsResponse
import br.com.zup.pix.pix.paraEnumGrpc
import com.google.protobuf.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId

fun ChavePix.paraBuscaChavePixResponse(): BuscaChavePixResponse {
    return BuscaChavePixResponse.newBuilder()
        .setChave(BuscaChavePixResponse.ChavePix.newBuilder()
            .setChave(this.chave)
            .setTipo(this.tipoChave.paraEnumGrpc())
            .setCriadaEm(this.criadoEm.paraTimestamp())
            .setConta(BuscaChavePixResponse.ContaInfo.newBuilder()
                .setTipo(this.tipoConta.paraEnumGrpc())
                .setAgencia(this.conta.agencia)
                .setNumeroDaConta(this.conta.numeroDaConta)
                .setInstituicao(Instituicoes.nome(this.conta.instituicao))
                .setCpfDoTitular(this.conta.cpfDoTitular)
                .setNomeDoTitular(this.conta.nomeDoTitular)
                .build()
            )
            .build())
        .setIdPix(this.uuid.toString())
        .setIdTitular(this.idTitular.toString())
        .build()
}

fun PixKeyDetailsResponse.paraBuscaChavePixResponse(): BuscaChavePixResponse {
    return BuscaChavePixResponse.newBuilder()
        .setChave(BuscaChavePixResponse.ChavePix.newBuilder()
            .setChave(this.key)
            .setTipo(this.keyType.paraEnumGrpc())
            .setCriadaEm(this.createdAt.paraTimestamp())
            .setConta(BuscaChavePixResponse.ContaInfo.newBuilder()
                .setTipo(this.bankAccount.accountType.paraEnumGrpc())
                .setAgencia(this.bankAccount.branch)
                .setNumeroDaConta(this.bankAccount.accountNumber)
                .setInstituicao(Instituicoes.nome(this.bankAccount.participant))
                .setCpfDoTitular(this.owner.taxIdNumber)
                .setNomeDoTitular(this.owner.name)
                .build()
            )
            .build())
        .build()
}

fun LocalDateTime.paraTimestamp(): Timestamp {
    val instant = this.atZone(ZoneId.of("UTC")).toInstant()
    return Timestamp.newBuilder()
        .setSeconds(instant.epochSecond)
        .setNanos(instant.nano)
        .build()
}