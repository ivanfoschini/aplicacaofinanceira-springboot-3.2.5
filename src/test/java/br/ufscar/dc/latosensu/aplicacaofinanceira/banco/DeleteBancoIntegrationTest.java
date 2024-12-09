package br.ufscar.dc.latosensu.aplicacaofinanceira.banco;

import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.SecurityService;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.IntegrationTestUtil;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.BancoRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.BancoTestUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DeleteBancoIntegrationTest extends IntegrationTestUtil {

    private final String uri = BancoTestUtil.BANCO_DELETE_URI + IntegrationTestUtil.ID_COMPLEMENT_URI;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private BancoRepository bancoRepository;

    @Autowired
    private MessageSource messageSource;

    @Test
    void failsDeleteComUsuarioNaoAutorizado() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(banco);

        Long id = banco.getId();

        mockMvc
            .perform(delete(uri, id)
                    .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.NAO_AUTORIZADO)))
            .andExpect(status().isForbidden());
    }

    @Test
    void failsDeleteSemToken() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(banco);

        Long id = banco.getId();

        mockMvc
            .perform(delete(uri, id))
            .andExpect(status().isForbidden());
    }

    @Test
    void failsDeleteComUsuarioFuncionario() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(banco);

        Long id = banco.getId();

        mockMvc
            .perform(delete(uri, id)
                    .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.FUNCIONARIO)))
            .andExpect(status().isForbidden());
    }

    @Test
    void failsDeleteComBancoInexistente() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(banco);

        MvcResult mvcResult = mockMvc
                .perform(delete(uri, 0)
                        .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.ADMIN)))
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
    void succeedsDelete() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        banco.setAgencias(new ArrayList<>());

        bancoRepository.save(banco);

        Long id = banco.getId();

        mockMvc
            .perform(delete(uri, id)
                    .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.ADMIN)))
            .andExpect(status().isNoContent());
    }
}
