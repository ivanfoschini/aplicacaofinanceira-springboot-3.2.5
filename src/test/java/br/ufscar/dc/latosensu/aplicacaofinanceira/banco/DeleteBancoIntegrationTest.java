package br.ufscar.dc.latosensu.aplicacaofinanceira.banco;

import br.ufscar.dc.latosensu.aplicacaofinanceira.BaseIntegrationTest;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.DefaultExceptionAttributes;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.BancoRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.security.SecurityUtil;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.BancoTestUtil;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.TestUtil;
import com.google.gson.JsonObject;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;    

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DeleteBancoIntegrationTest extends BaseIntegrationTest {
 
    private final String uri = BancoTestUtil.BANCO_DELETE_URI + TestUtil.ID_COMPLEMENT_URI;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    @Test
    public void testDeleteComUsuarioNaoAutorizado() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        Long id = banco.getId();
        
        mockMvc
            .perform(delete(uri, id)
                    .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.NAO_AUTORIZADO)))                 
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void testDeleteSemToken() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        Long id = banco.getId();
        
        mockMvc
            .perform(delete(uri, id))                 
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void testDeleteComUsuarioFuncionario() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        Long id = banco.getId();
        
        mockMvc
            .perform(delete(uri, id)
                    .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.FUNCIONARIO)))                 
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void testDeleteComBancoInexistente() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        MvcResult mvcResult = mockMvc
                .perform(delete(uri, 0)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN)))
                .andExpect(status().isNotFound())
                .andReturn();
        
        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")));

        assertEquals(response.get(DefaultExceptionAttributes.EXCEPTION).getAsString(), TestUtil.NOT_FOUND_EXCEPTION);
        assertEquals(response.get(DefaultExceptionAttributes.MESSAGE).getAsString(), messageSource.getMessage("bancoNaoEncontrado", null, null));
    } 
    
    @Test
    public void testDeleteComSucesso() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        banco.setAgencias(new ArrayList<>());
        
        bancoRepository.save(banco);
        
        Long id = banco.getId();
        
        mockMvc
            .perform(delete(uri, id)
                    .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN)))
            .andExpect(status().isOk())
            .andReturn();
    }
}
