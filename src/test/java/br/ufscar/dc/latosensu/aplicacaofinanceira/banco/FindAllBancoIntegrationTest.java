package br.ufscar.dc.latosensu.aplicacaofinanceira.banco;

import br.ufscar.dc.latosensu.aplicacaofinanceira.BaseIntegrationTest;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.BancoRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.security.SecurityUtil;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.BancoTestUtil;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.TestUtil;
import com.google.gson.JsonArray;
import java.nio.charset.Charset;
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
public class FindAllBancoIntegrationTest extends BaseIntegrationTest {

    private final String uri = BancoTestUtil.BANCO_LIST_URI;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Test
    public void testFindAllComUsuarioNaoAutorizado() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        mockMvc
            .perform(get(uri)
                    .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.NAO_AUTORIZADO)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void testFindAllSemToken() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        mockMvc
            .perform(get(uri))
            .andExpect(status().isUnauthorized());
    }    
    
    @Test
    public void testFindAllComSucessoParaAdmin() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        Banco caixaEconomicaFederal = BancoTestUtil.caixaEconomicaFederal();
        Banco itau = BancoTestUtil.itau();
        
        bancoRepository.save(bancoDoBrasil);
        bancoRepository.save(caixaEconomicaFederal);
        bancoRepository.save(itau);
        
        MvcResult mvcResult = mockMvc
            .perform(get(uri)
                    .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN)))
            .andExpect(status().isOk())
            .andReturn();

        JsonArray response = stringToJsonArray(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")));

        assertEquals(response.size(), TestUtil.DEFAULT_SUCCESS_LIST_SIZE);        
    }

    @Test
    public void testFindAllComSucessoParaFuncionario() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        Banco caixaEconomicaFederal = BancoTestUtil.caixaEconomicaFederal();
        Banco itau = BancoTestUtil.itau();
        
        bancoRepository.save(bancoDoBrasil);
        bancoRepository.save(caixaEconomicaFederal);
        bancoRepository.save(itau);
        
        MvcResult mvcResult = mockMvc
                .perform(get(uri)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.FUNCIONARIO)))
                .andExpect(status().isOk())
                .andReturn();

        JsonArray response = stringToJsonArray(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")));

        assertEquals(response.size(), TestUtil.DEFAULT_SUCCESS_LIST_SIZE);              
    }    
}
