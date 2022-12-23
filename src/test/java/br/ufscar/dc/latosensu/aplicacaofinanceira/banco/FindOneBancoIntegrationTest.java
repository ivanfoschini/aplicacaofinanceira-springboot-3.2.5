package br.ufscar.dc.latosensu.aplicacaofinanceira.banco;

import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.SecurityService;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.IntegrationTestUtil;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.BancoRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.BancoTestUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FindOneBancoIntegrationTest extends IntegrationTestUtil {

    private final String uri = BancoTestUtil.BANCO_SHOW_URI + IntegrationTestUtil.ID_COMPLEMENT_URI;

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    @Test
    void failsFindOneComUsuarioNaoAutorizado() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(banco);

        Long id = banco.getId();

        mockMvc
            .perform(get(uri, id)
                     .header(IntegrationTestUtil.AUTHORIZATION, securityService.generateToken(IntegrationTestUtil.NAO_AUTORIZADO)))
            .andExpect(status().isForbidden());
    }

    @Test
    void failsFindOneSemToken() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(banco);

        Long id = banco.getId();

        mockMvc
            .perform(get(uri, id))
            .andExpect(status().isForbidden());
    }

    @Test
    void failsFindOneComBancoInexistente() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(banco);

        MvcResult mvcResult = mockMvc
                .perform(get(uri, 0)
                        .header(IntegrationTestUtil.AUTHORIZATION, securityService.generateToken(IntegrationTestUtil.ADMIN)))
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
    void succeedsFindOneParaAdmin() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(banco);

        Long id = banco.getId();

        MvcResult mvcResult = mockMvc
                .perform(get(uri, id)
                        .header(IntegrationTestUtil.AUTHORIZATION, securityService.generateToken(IntegrationTestUtil.ADMIN)))
                .andExpect(status().isOk())
                .andReturn();

        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));

        assertEquals(response.get(BancoTestUtil.BANCO_ID).getAsLong(), id);
        assertEquals(response.get(BancoTestUtil.BANCO_NUMERO).getAsInt(), banco.getNumero());
        assertEquals(response.get(BancoTestUtil.BANCO_CNPJ).getAsString(), banco.getCnpj());
        assertEquals(response.get(BancoTestUtil.BANCO_NOME).getAsString(), banco.getNome());
    }

    @Test
    void succeedsFindOneParaFuncionario() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(banco);

        Long id = banco.getId();

        MvcResult mvcResult = mockMvc
                .perform(get(uri, id)
                        .header(IntegrationTestUtil.AUTHORIZATION, securityService.generateToken(IntegrationTestUtil.FUNCIONARIO)))
                .andExpect(status().isOk())
                .andReturn();

        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));

        assertEquals(response.get(BancoTestUtil.BANCO_ID).getAsLong(), id);
        assertEquals(response.get(BancoTestUtil.BANCO_NUMERO).getAsInt(), banco.getNumero());
        assertEquals(response.get(BancoTestUtil.BANCO_CNPJ).getAsString(), banco.getCnpj());
        assertEquals(response.get(BancoTestUtil.BANCO_NOME).getAsString(), banco.getNome());
    }
}
