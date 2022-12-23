package br.ufscar.dc.latosensu.aplicacaofinanceira.banco;

import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.SecurityService;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.IntegrationTestUtil;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.BancoRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.BancoTestUtil;
import com.google.gson.JsonArray;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FindAllBancoIntegrationTest extends IntegrationTestUtil {

    private final String uri = BancoTestUtil.BANCO_LIST_URI;

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Test
    void failsFindAllComUsuarioNaoAutorizado() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(banco);

        mockMvc
            .perform(get(uri)
                    .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.NAO_AUTORIZADO)))
            .andExpect(status().isForbidden());
    }

    @Test
    void failsFindAllSemToken() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();

        bancoRepository.save(banco);

        mockMvc
            .perform(get(uri))
            .andExpect(status().isForbidden());
    }

    @Test
    void succeedsFindAllParaAdmin() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        Banco caixaEconomicaFederal = BancoTestUtil.caixaEconomicaFederal();
        Banco itau = BancoTestUtil.itau();

        bancoRepository.save(bancoDoBrasil);
        bancoRepository.save(caixaEconomicaFederal);
        bancoRepository.save(itau);

        MvcResult mvcResult = mockMvc
            .perform(get(uri)
                    .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.ADMIN)))
            .andExpect(status().isOk())
            .andReturn();

        JsonArray response = stringToJsonArray(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));

        assertEquals(IntegrationTestUtil.DEFAULT_SUCCESS_LIST_SIZE, response.size());
    }

    @Test
    void succeedsFindAllParaFuncionario() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        Banco caixaEconomicaFederal = BancoTestUtil.caixaEconomicaFederal();
        Banco itau = BancoTestUtil.itau();

        bancoRepository.save(bancoDoBrasil);
        bancoRepository.save(caixaEconomicaFederal);
        bancoRepository.save(itau);

        MvcResult mvcResult = mockMvc
                .perform(get(uri)
                        .header(IntegrationTestUtil.TOKEN, securityService.generateToken(IntegrationTestUtil.FUNCIONARIO)))
                .andExpect(status().isOk())
                .andReturn();

        JsonArray response = stringToJsonArray(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));

        assertEquals(IntegrationTestUtil.DEFAULT_SUCCESS_LIST_SIZE, response.size());
    }
}
