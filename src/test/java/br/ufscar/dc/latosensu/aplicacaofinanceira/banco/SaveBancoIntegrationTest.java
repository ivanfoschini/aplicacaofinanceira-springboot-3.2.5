package br.ufscar.dc.latosensu.aplicacaofinanceira.banco;

import br.ufscar.dc.latosensu.aplicacaofinanceira.util.IntegrationTestUtil;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.BancoRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.SecurityService;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.BancoTestUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SaveBancoIntegrationTest extends IntegrationTestUtil {

    private final String uri = BancoTestUtil.BANCO_SAVE_URI;

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    @Test
    void failsSaveComUsuarioNaoAutorizado() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        String requestBody = objectToString(banco);

        mockMvc
            .perform(post(uri)
                    .contentType(MediaType.APPLICATION_JSON)                 
                    .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.NAO_AUTORIZADO))
                    .content(requestBody))
            .andExpect(status().isForbidden());
    }
    
    @Test
    void failsSaveSemToken() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();

        String requestBody = objectToString(banco);

        mockMvc
            .perform(post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andExpect(status().isForbidden());
    }

    @Test
    void failsSaveComUsuarioFuncionario() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();

        String requestBody = objectToString(banco);

        mockMvc
            .perform(post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.FUNCIONARIO))
                    .content(requestBody))
            .andExpect(status().isForbidden());
    }

    @Test
    void failsSaveSemCamposObrigatorios() throws Exception {
        Banco banco = BancoTestUtil.bancoSemCamposObrigatorios();

        String requestBody = objectToString(banco);

        MvcResult mvcResult = mockMvc
                .perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.ADMIN))
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));

        assertEquals(UNPROCESSABLE_ENTITY, response.get(STATUS).getAsString());
        assertEquals(METHOD_ARGUMENT_NOT_VALID_EXCEPTION, response.get(EXCEPTION).getAsString());
        assertEquals(messageSource.getMessage("causeMethodArgumentNotValidException", null, null), response.get(CAUSE).getAsString());
        assertNotNull(response.get(DATE_TIME));

        JsonArray errors = response.get(ERRORS).getAsJsonArray();

        assertEquals(4, errors.size());

        List<String> getErrorsMessages = getErrorsMessages(errors);

        assertTrue(getErrorsMessages.contains("cnpj: " + messageSource.getMessage("bancoCnpjInvalido", null, null)));
        assertTrue(getErrorsMessages.contains("cnpj: " + messageSource.getMessage("bancoCnpjNaoPodeSerNulo", null, null)));
        assertTrue(getErrorsMessages.contains("nome: " + messageSource.getMessage("bancoNomeNaoPodeSerNulo", null, null)));
        assertTrue(getErrorsMessages.contains("numero: " + messageSource.getMessage("bancoNumeroDeveSerMaiorDoQueZero", null, null)));
    }

    @Test
    void failsSaveComNumeroMenorDoQueUm() throws Exception {
        Banco banco = BancoTestUtil.bancoComNumeroMenorDoQueUm();

        String requestBody = objectToString(banco);

        MvcResult mvcResult = mockMvc
                .perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.ADMIN))
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));

        assertEquals(UNPROCESSABLE_ENTITY, response.get(STATUS).getAsString());
        assertEquals(METHOD_ARGUMENT_NOT_VALID_EXCEPTION, response.get(EXCEPTION).getAsString());
        assertEquals(messageSource.getMessage("causeMethodArgumentNotValidException", null, null), response.get(CAUSE).getAsString());
        assertNotNull(response.get(DATE_TIME));

        JsonArray errors = response.get(ERRORS).getAsJsonArray();

        assertEquals(1, errors.size());

        List<String> getErrorsMessages = getErrorsMessages(errors);

        assertTrue(getErrorsMessages.contains("numero: " + messageSource.getMessage("bancoNumeroDeveSerMaiorDoQueZero", null, null)));
    }

    @Test
    void failsSaveComNumeroDuplicado() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(banco);

        String requestBody = objectToString(banco);

        MvcResult mvcResult = mockMvc
                .perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.ADMIN))
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));

        assertEquals(UNPROCESSABLE_ENTITY, response.get(STATUS).getAsString());
        assertEquals(NOT_UNIQUE_EXCEPTION, response.get(EXCEPTION).getAsString());
        assertEquals(messageSource.getMessage("causeNotUniqueException", null, null), response.get(CAUSE).getAsString());
        assertNotNull(response.get(DATE_TIME));

        JsonArray errors = response.get(ERRORS).getAsJsonArray();

        assertEquals(1, errors.size());

        List<String> getErrorsMessages = getErrorsMessages(errors);

        assertTrue(getErrorsMessages.contains(messageSource.getMessage("bancoNumeroDeveSerUnico", null, null)));
    }

    @Test
    void failsSaveComCnpjInvalido() throws Exception {
        Banco banco = BancoTestUtil.bancoComCnpjInvalido();

        String requestBody = objectToString(banco);

        MvcResult mvcResult = mockMvc
                .perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.ADMIN))
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));

        assertEquals(UNPROCESSABLE_ENTITY, response.get(STATUS).getAsString());
        assertEquals(METHOD_ARGUMENT_NOT_VALID_EXCEPTION, response.get(EXCEPTION).getAsString());
        assertEquals(messageSource.getMessage("causeMethodArgumentNotValidException", null, null), response.get(CAUSE).getAsString());
        assertNotNull(response.get(DATE_TIME));

        JsonArray errors = response.get(ERRORS).getAsJsonArray();

        assertEquals(1, errors.size());

        List<String> getErrorsMessages = getErrorsMessages(errors);

        assertTrue(getErrorsMessages.contains("cnpj: " + messageSource.getMessage("bancoCnpjInvalido", null, null)));
    }

    @Test
    void failsSaveComNomeComMenosDeDoisCaracteres() throws Exception {
        Banco banco = BancoTestUtil.bancoComNomeComMenosDeDoisCaracteres();

        String requestBody = objectToString(banco);

        MvcResult mvcResult = mockMvc
                .perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.ADMIN))
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));

        assertEquals(UNPROCESSABLE_ENTITY, response.get(STATUS).getAsString());
        assertEquals(METHOD_ARGUMENT_NOT_VALID_EXCEPTION, response.get(EXCEPTION).getAsString());
        assertEquals(messageSource.getMessage("causeMethodArgumentNotValidException", null, null), response.get(CAUSE).getAsString());
        assertNotNull(response.get(DATE_TIME));

        JsonArray errors = response.get(ERRORS).getAsJsonArray();

        assertEquals(1, errors.size());

        List<String> getErrorsMessages = getErrorsMessages(errors);

        assertTrue(getErrorsMessages.contains("nome: " + messageSource.getMessage("bancoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null)));
    }

    @Test
    void failsSaveComNomeComMaisDeDuzentosECinquentaECincoCaracteres() throws Exception {
        Banco banco = BancoTestUtil.bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres();

        String requestBody = objectToString(banco);

        MvcResult mvcResult = mockMvc
                .perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.ADMIN))
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));

        assertEquals(UNPROCESSABLE_ENTITY, response.get(STATUS).getAsString());
        assertEquals(METHOD_ARGUMENT_NOT_VALID_EXCEPTION, response.get(EXCEPTION).getAsString());
        assertEquals(messageSource.getMessage("causeMethodArgumentNotValidException", null, null), response.get(CAUSE).getAsString());
        assertNotNull(response.get(DATE_TIME));

        JsonArray errors = response.get(ERRORS).getAsJsonArray();

        assertEquals(1, errors.size());

        List<String> getErrorsMessages = getErrorsMessages(errors);

        assertTrue(getErrorsMessages.contains("nome: " + messageSource.getMessage("bancoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null)));
    }

    @Test
    void succeedsSave() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();

        String requestBody = objectToString(banco);

        MvcResult mvcResult = mockMvc
                .perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.ADMIN))
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();

        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));

        assertNotNull(response.get(BancoTestUtil.BANCO_ID));
        assertEquals(response.get(BancoTestUtil.BANCO_NUMERO).getAsInt(), banco.getNumero());
        assertEquals(response.get(BancoTestUtil.BANCO_CNPJ).getAsString(), banco.getCnpj());
        assertEquals(response.get(BancoTestUtil.BANCO_NOME).getAsString(), banco.getNome());
    }
}
