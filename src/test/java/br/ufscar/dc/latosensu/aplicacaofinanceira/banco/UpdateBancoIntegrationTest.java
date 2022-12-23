package br.ufscar.dc.latosensu.aplicacaofinanceira.banco;

import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.SecurityService;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.IntegrationTestUtil;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.BancoRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UpdateBancoIntegrationTest extends IntegrationTestUtil {

    private final String uri = BancoTestUtil.BANCO_UPDATE_URI + IntegrationTestUtil.ID_COMPLEMENT_URI;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    @Test
    void failsUpdateComUsuarioNaoAutorizado() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(bancoDoBrasil);

        Long id = bancoDoBrasil.getId();

        String inputJson = objectToString(bancoDoBrasil);

        mockMvc
            .perform(put(uri, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(IntegrationTestUtil.AUTHORIZATION, securityService.generateToken(IntegrationTestUtil.NAO_AUTORIZADO))
                    .content(inputJson))
            .andExpect(status().isForbidden());
    }

    @Test
    void failsUpdateSemToken() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(bancoDoBrasil);

        Long id = bancoDoBrasil.getId();

        String inputJson = objectToString(bancoDoBrasil);

        mockMvc
            .perform(put(uri, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(inputJson))
            .andExpect(status().isForbidden());
    }

    @Test
    void failsUpdateComUsuarioFuncionario() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(bancoDoBrasil);

        Long id = bancoDoBrasil.getId();

        String inputJson = objectToString(bancoDoBrasil);

        mockMvc
            .perform(put(uri, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(IntegrationTestUtil.AUTHORIZATION, securityService.generateToken(IntegrationTestUtil.FUNCIONARIO))
                    .content(inputJson))
            .andExpect(status().isForbidden());
    }

    @Test
    void failsUpdateSemCamposObrigatorios() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(bancoDoBrasil);

        Long id = bancoDoBrasil.getId();

        Banco bancoSemCamposObrigatorios = BancoTestUtil.bancoSemCamposObrigatorios();

        String inputJson = objectToString(bancoSemCamposObrigatorios);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IntegrationTestUtil.AUTHORIZATION, securityService.generateToken(IntegrationTestUtil.ADMIN))
                        .content(inputJson))
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
    void failsUpdateComNumeroMenorDoQueUm() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(bancoDoBrasil);

        Long id = bancoDoBrasil.getId();

        Banco bancoComNumeroMenorDoQueUm = BancoTestUtil.bancoComNumeroMenorDoQueUm();

        String inputJson = objectToString(bancoComNumeroMenorDoQueUm);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IntegrationTestUtil.AUTHORIZATION, securityService.generateToken(IntegrationTestUtil.ADMIN))
                        .content(inputJson))
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
    void failsUpdateComNumeroDuplicado() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        Banco caixaEconomicaFederal = BancoTestUtil.caixaEconomicaFederal();

        bancoRepository.save(bancoDoBrasil);
        bancoRepository.save(caixaEconomicaFederal);

        Long id = caixaEconomicaFederal.getId();

        String inputJson = objectToString(bancoDoBrasil);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IntegrationTestUtil.AUTHORIZATION, securityService.generateToken(IntegrationTestUtil.ADMIN))
                        .content(inputJson))
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
    void failsUpdateComCnpjInvalido() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoComCnpjInvalido();

        bancoRepository.save(bancoDoBrasil);

        Long id = bancoDoBrasil.getId();

        Banco bancoComCnpjInvalido = BancoTestUtil.bancoComCnpjInvalido();

        String inputJson = objectToString(bancoComCnpjInvalido);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IntegrationTestUtil.AUTHORIZATION, securityService.generateToken(IntegrationTestUtil.ADMIN))
                        .content(inputJson))
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
    void failsUpdateComNomeComMenosDeDoisCaracteres() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(bancoDoBrasil);

        Long id = bancoDoBrasil.getId();

        Banco bancoComNomeComMenosDeDoisCaracteres = BancoTestUtil.bancoComNomeComMenosDeDoisCaracteres();

        String inputJson = objectToString(bancoComNomeComMenosDeDoisCaracteres);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IntegrationTestUtil.AUTHORIZATION, securityService.generateToken(IntegrationTestUtil.ADMIN))
                        .content(inputJson))
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
    void failsUpdateComNomeComMaisDeDuzentosECinquentaECincoCaracteres() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(bancoDoBrasil);

        Long id = bancoDoBrasil.getId();

        Banco bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres = BancoTestUtil.bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres();

        String inputJson = objectToString(bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IntegrationTestUtil.AUTHORIZATION, securityService.generateToken(IntegrationTestUtil.ADMIN))
                        .content(inputJson))
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
    void failsUpdateComBancoInexistente() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(bancoDoBrasil);

        String inputJson = objectToString(bancoDoBrasil);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, 0)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IntegrationTestUtil.AUTHORIZATION, securityService.generateToken(IntegrationTestUtil.ADMIN))
                        .content(inputJson))
                .andExpect(status().isNotFound())
                .andReturn();

        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));

        assertEquals(NOT_FOUND, response.get(STATUS).getAsString());
        assertEquals(NOT_FOUND_EXCEPTION, response.get(EXCEPTION).getAsString());
        assertEquals(messageSource.getMessage("causeNotFoundException", null, null), response.get(CAUSE).getAsString());
        assertNotNull(response.get(DATE_TIME));

        JsonArray errors = response.get(ERRORS).getAsJsonArray();

        assertEquals(1, errors.size());

        List<String> getErrorsMessages = getErrorsMessages(errors);

        assertTrue(getErrorsMessages.contains(messageSource.getMessage("bancoNaoEncontrado", null, null)));
    }

    @Test
    void succeedsUpdate() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(bancoDoBrasil);

        Long id = bancoDoBrasil.getId();

        Banco caixaEconomicaFederal = BancoTestUtil.caixaEconomicaFederal();

        String inputJson = objectToString(caixaEconomicaFederal);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IntegrationTestUtil.AUTHORIZATION, securityService.generateToken(IntegrationTestUtil.ADMIN))
                        .content(inputJson))
                .andExpect(status().isOk())
                .andReturn();

        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));

        assertNotNull(response.get(BancoTestUtil.BANCO_ID));
        assertEquals(response.get(BancoTestUtil.BANCO_NUMERO).getAsInt(), caixaEconomicaFederal.getNumero());
        assertEquals(response.get(BancoTestUtil.BANCO_CNPJ).getAsString(), caixaEconomicaFederal.getCnpj());
        assertEquals(response.get(BancoTestUtil.BANCO_NOME).getAsString(), caixaEconomicaFederal.getNome());
    }
}
