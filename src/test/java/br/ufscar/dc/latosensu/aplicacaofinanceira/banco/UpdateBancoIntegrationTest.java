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
public class UpdateBancoIntegrationTest extends BaseIntegrationTest {

    private String uri = BancoTestUtil.BANCO_UPDATE_URI + TestUtil.ID_COMPLEMENT_URI;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testUpdateComUsuarioNaoAutorizado() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        String inputJson = super.mapToJson(bancoDoBrasil);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.NAO_AUTORIZADO))
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }
    
    @Test
    public void testUpdateSemToken() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        String inputJson = super.mapToJson(bancoDoBrasil);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }

    @Test
    public void testUpdateComUsuarioFuncionario() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        String inputJson = super.mapToJson(bancoDoBrasil);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.FUNCIONARIO))
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }  
    
    @Test
    public void testUpdateSemCamposObrigatorios() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco bancoSemCamposObrigatorios = BancoTestUtil.bancoSemCamposObrigatorios();
        
        String inputJson = super.mapToJson(bancoSemCamposObrigatorios);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponseDeserializer.getException());
        
        Assert.assertTrue(errorResponseDeserializer.getMessages().contains(messageSource.getMessage("bancoCnpjNaoPodeSerNulo", null, null)));
        Assert.assertTrue(errorResponseDeserializer.getMessages().contains(messageSource.getMessage("bancoCnpjInvalido", null, null)));
        Assert.assertTrue(errorResponseDeserializer.getMessages().contains(messageSource.getMessage("bancoNomeNaoPodeSerNulo", null, null)));
        Assert.assertTrue(errorResponseDeserializer.getMessages().contains(messageSource.getMessage("bancoNumeroDeveSerMaiorDoQueZero", null, null)));
    }
    
    @Test
    public void testUpdateComNumeroMenorDoQueUm() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco bancoComNumeroMenorDoQueUm = BancoTestUtil.bancoComNumeroMenorDoQueUm();
        
        String inputJson = super.mapToJson(bancoComNumeroMenorDoQueUm);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNumeroDeveSerMaiorDoQueZero", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComNumeroDuplicado() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        Banco caixaEconomicaFederal = BancoTestUtil.caixaEconomicaFederal();        
        
        bancoRepository.save(bancoDoBrasil);        
        bancoRepository.save(caixaEconomicaFederal);
        
        Long id = caixaEconomicaFederal.getId();
        
        String inputJson = super.mapToJson(bancoDoBrasil);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.NOT_UNIQUE_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNumeroDeveSerUnico", null, null), errorResponseDeserializer.getMessage());
    }
    
    @Test
    public void testUpdateComCnpjInvalido() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoComCnpjInvalido();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco bancoComCnpjInvalido = BancoTestUtil.bancoComCnpjInvalido();
        
        String inputJson = super.mapToJson(bancoComCnpjInvalido);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("bancoCnpjInvalido", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComNomeComMenosDeDoisCaracteres() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco bancoComNomeComMenosDeDoisCaracteres = BancoTestUtil.bancoComNomeComMenosDeDoisCaracteres();
        
        String inputJson = super.mapToJson(bancoComNomeComMenosDeDoisCaracteres);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComNomeComMaisDeDuzentosECinquentaECincoCaracteres() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres = BancoTestUtil.bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres();
        
        String inputJson = super.mapToJson(bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComBancoInexistente() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        String inputJson = super.mapToJson(bancoDoBrasil);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, 0)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assert.assertEquals(TestUtil.NOT_FOUND_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNaoEncontrado", null, null), errorResponseDeserializer.getMessage());
    }
    
    @Test
    public void testUpdateComSucesso() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco caixaEconomicaFederal = BancoTestUtil.caixaEconomicaFederal();
        
        String inputJson = super.mapToJson(caixaEconomicaFederal);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        Banco updatedBanco = super.mapFromJsonObject(content, Banco.class);        
        caixaEconomicaFederal.setId(updatedBanco.getId());
        
        Assert.assertEquals(HttpStatus.OK.value(), status);
        Assert.assertEquals(caixaEconomicaFederal, updatedBanco);
    }
}
