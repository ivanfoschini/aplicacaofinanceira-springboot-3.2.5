package br.ufscar.dc.latosensu.aplicacaofinanceira.banco;

import br.ufscar.dc.latosensu.aplicacaofinanceira.BaseIntegrationTest;
import br.ufscar.dc.latosensu.aplicacaofinanceira.deserializer.ErrorResponseDeserializer;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.BancoRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.security.SecurityUtil;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.BancoTestUtil;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class FindOneBancoIntegrationTest extends BaseIntegrationTest {

    private String uri = BancoTestUtil.BANCO_SHOW_URI + TestUtil.ID_COMPLEMENT_URI;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testFindOneComUsuarioNaoAutorizado() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        Long id = banco.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.NAO_AUTORIZADO)))
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }
    
    @Test
    public void testFindOneSemToken() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        Long id = banco.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri, id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }
    
    @Test
    public void testFindOneComBancoInexistente() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri, 0)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN)))
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString(); 
        
        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assert.assertEquals(TestUtil.NOT_FOUND_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNaoEncontrado", null, null), errorResponseDeserializer.getMessage());
    } 
    
    @Test
    public void testFindOneComSucessoParaAdmin() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        Long id = banco.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN)))
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        Banco savedBanco = super.mapFromJsonObject(content, Banco.class);        
        banco.setId(savedBanco.getId());
        
        Assert.assertEquals(HttpStatus.OK.value(), status);
        Assert.assertEquals(banco, savedBanco);
    }

    @Test
    public void testFindOneComSucessoParaFuncionario() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        Long id = banco.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.FUNCIONARIO)))
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        Banco savedBanco = super.mapFromJsonObject(content, Banco.class);        
        banco.setId(savedBanco.getId());
        
        Assert.assertEquals(HttpStatus.OK.value(), status);
        Assert.assertEquals(banco, savedBanco);
    }    
}
