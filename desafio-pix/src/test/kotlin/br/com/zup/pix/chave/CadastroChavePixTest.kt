package br.com.zup.pix.chave

import br.com.zup.pix.ChavePixRequest
import br.com.zup.pix.KeyManagerGRPCServiceGrpc
import br.com.zup.pix.cadastro.ChavePixForm
import br.com.zup.pix.externos.*
import br.com.zup.pix.toUUID
import br.com.zup.pix.valid.getViolacao
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class CadastroChavePixTest(
    val repository: ChavePixRepository,
    val grpcService: KeyManagerGRPCServiceGrpc.KeyManagerGRPCServiceBlockingStub
) {

    @Inject
    lateinit var erpItau: ErpItau
    @Inject
    lateinit var bancoCentralClient: BancoCentralClient

    @BeforeEach
    internal fun setUp() {
        repository.deleteAll()
    }

    @Test
    internal fun `deve cadastrar um chave do tipo EMAIL`() {
        val chavePixForm = ChavePixForm(
            idTitular = UUID.randomUUID().toString(),
            tipoChave = TipoChave.EMAIL,
            tipoConta = TipoConta.CONTA_POUPANCA,
            chave = "ana.flavia@zup.com.br"
        )
        Mockito.`when`(erpItau.buscarConta(chavePixForm.idTitular!!.toUUID(),"CONTA_POUPANCA"))
            .thenReturn(HttpResponse.ok(DADOS_DA_CONTA_RESPONSE))
        Mockito.`when`(bancoCentralClient.cria(CreatePixKeyRequest(DADOS_DA_CONTA_RESPONSE, chavePixForm)))
            .thenReturn(HttpResponse.ok(CreatePixKeyResponse(key = chavePixForm.chave!!)))

        val response = grpcService.cadastro(
            ChavePixRequest.newBuilder()
            .setChave(chavePixForm.chave)
            .setTipoChave(br.com.zup.pix.TipoChave.EMAIL)
            .setTipoConta(br.com.zup.pix.TipoConta.CONTA_POUPANCA)
            .setIdTitular(chavePixForm.idTitular)
            .build())

        assertNotEquals("",response.id)
        val chave = repository.findByUuid(response.id.toUUID())
        assertNotNull(chave)
        assertEquals(chavePixForm.chave,chave!!.chave)
    }

    @Test
    internal fun `nao deve cadastrar um chave invalida do tipo EMAIL`() {
        val email = "ana.flavia@zup.com.br"
        val idTitular = "18e39430-e4a9-11eb-ba80-0242ac130004"
        Mockito.`when`(erpItau.buscarConta(idTitular.toUUID(),"CONTA_POUPANCA"))
            .thenReturn(HttpResponse.ok())


        val error = assertThrows<StatusRuntimeException> {
            grpcService.cadastro(ChavePixRequest.newBuilder()
                .setChave(email)
                .setTipoChave(br.com.zup.pix.TipoChave.EMAIL)
                .setTipoConta(br.com.zup.pix.TipoConta.CONTA_POUPANCA)
                .setIdTitular(idTitular)
                .build())
        }

        assertEquals(Status.INVALID_ARGUMENT.code,error.status.code)
        assertEquals(0,repository.count())
        val violation = error.getViolacao(0)
        assertEquals("Não e um Email valido",violation?.description)
    }

    @Test
    internal fun `deve cadastrar um chave do tipo CHAVE_ALEATORIA`() {
        val chavePixForm = ChavePixForm(
            idTitular = UUID.randomUUID().toString(),
            tipoChave = TipoChave.CHAVE_ALEATORIA,
            tipoConta = TipoConta.CONTA_POUPANCA,
        )
        Mockito.`when`(erpItau.buscarConta(chavePixForm.idTitular!!.toUUID(),"CONTA_POUPANCA"))
            .thenReturn(HttpResponse.ok(DADOS_DA_CONTA_RESPONSE))
        Mockito.`when`(bancoCentralClient.cria(CreatePixKeyRequest(DADOS_DA_CONTA_RESPONSE, chavePixForm)))
            .thenReturn(HttpResponse.ok(CreatePixKeyResponse(key = UUID.randomUUID().toString())))

        val response = grpcService.cadastro(ChavePixRequest.newBuilder()
            .setTipoChave(br.com.zup.pix.TipoChave.CHAVE_ALEATORIA)
            .setTipoConta(br.com.zup.pix.TipoConta.CONTA_POUPANCA)
            .setIdTitular(chavePixForm.idTitular)
            .build())

        val chave = repository.findByUuid(response.id.toUUID())
        assertNotNull(chave)
        assertDoesNotThrow(){
            chave!!.chave.toUUID()
        }
    }

    @Test
    internal fun `nao deve cadastrar um chave do tipo CHAVE_ALEATORIA quado o campo chave estiver preenchido`() {
        val idTitular = "18e39430-e4a9-11eb-ba80-0242ac130004"
        Mockito.`when`(erpItau.buscarConta(idTitular.toUUID(),"CONTA_POUPANCA"))
            .thenReturn(HttpResponse.ok())

        val error = assertThrows<StatusRuntimeException> {
            grpcService.cadastro(ChavePixRequest.newBuilder()
                .setChave("teste")
                .setTipoChave(br.com.zup.pix.TipoChave.CHAVE_ALEATORIA)
                .setTipoConta(br.com.zup.pix.TipoConta.CONTA_POUPANCA)
                .setIdTitular(idTitular)
                .build())
        }

        assertEquals(Status.INVALID_ARGUMENT.code,error.status.code)
        assertEquals(0,repository.count())
        val violation = error.getViolacao(0)
        assertEquals("Para criar uma chave aleatoria o campo 'chave' deve estar em branco",violation?.description)
    }

    @Test
    internal fun `nao deve cadastrar chaves repetidas`() {
        val email = "ana.flavia@zup.com.br"
        val idTitular = "18e39430-e4a9-11eb-ba80-0242ac130004"
        Mockito.`when`(erpItau.buscarConta(idTitular.toUUID(),"CONTA_POUPANCA"))
            .thenReturn(HttpResponse.ok())
        repository.save(ChavePix(
            tipoChave = TipoChave.EMAIL,
            tipoConta = TipoConta.CONTA_POUPANCA,
            idTitular = idTitular.toUUID(),
            chave = email,
            conta = ContaAssociada(
                nomeDoTitular = "Ana Flavia Rodrigues",
                cpfDoTitular = "14025844664",
                instituicao = ContaAssociada.ITAU_UNIBANCO_ISPB,
                numeroDaConta = "123456",
                agencia = "0001"
            )
        ))

        val error = assertThrows<StatusRuntimeException> {
            grpcService.cadastro(ChavePixRequest.newBuilder()
                .setChave(email)
                .setTipoChave(br.com.zup.pix.TipoChave.EMAIL)
                .setTipoConta(br.com.zup.pix.TipoConta.CONTA_POUPANCA)
                .setIdTitular(idTitular)
                .build())
        }

        assertEquals(Status.ALREADY_EXISTS.code,error.status.code)
        assertEquals("ALREADY_EXISTS: ja existe uma chave com este valor",error.message)
        assertEquals(1,repository.count())
    }

    @Test
    internal fun `nao deve cadastrar caso nao encontre no ERP do itau um cliente com o idTitular e com o tipo de conta informado`() {
        val email = "ana.flavia@zup.com.br"
        val idTitular = "18e39430-e4a9-11eb-ba80-0242ac130004"
        Mockito.`when`(erpItau.buscarConta(idTitular.toUUID(),"CONTA_CORRENTE"))
            .thenReturn(HttpResponse.notFound())

        val error = assertThrows<StatusRuntimeException> {
            grpcService.cadastro(ChavePixRequest.newBuilder()
                .setChave(email)
                .setTipoChave(br.com.zup.pix.TipoChave.EMAIL)
                .setTipoConta(br.com.zup.pix.TipoConta.CONTA_CORRENTE)
                .setIdTitular(idTitular)
                .build())
        }

        assertEquals(Status.INVALID_ARGUMENT.code,error.status.code)
        assertEquals("INVALID_ARGUMENT: idTitilar e/ou tipoConta esta incorreto",error.message)
        assertEquals(0,repository.count())
    }

    val DADOS_DA_CONTA_RESPONSE = DadosDaContaResponse(
        agencia = "0001",
        numero = "291900",
        titular = TitularResponse(
            nome = "Ana Flavia",
            cpf = "11122233344"
        )
    )

    @MockBean(BancoCentralClient::class)
    fun bcbClient() = Mockito.mock(BancoCentralClient::class.java)

    @MockBean(ErpItau::class)
    fun erpItau() = Mockito.mock(ErpItau::class.java)

    @Factory
    class FactoryClass{
        @Singleton
        fun grpc(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerGRPCServiceGrpc.KeyManagerGRPCServiceBlockingStub? {
            return KeyManagerGRPCServiceGrpc.newBlockingStub(channel)
        }
    }
}