package br.ufscar.dc.latosensu.aplicacaofinanceira.banco;

import br.ufscar.dc.latosensu.aplicacaofinanceira.BaseIntegrationTest;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.DefaultExceptionAttributes;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.BancoRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.security.SecurityUtil;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.BancoTestUtil;
import br.ufscar.dc.latosensu.aplicacaofinanceira.util.TestUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.nio.charset.Charset;
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
public class InsertBancoIntegrationTest extends BaseIntegrationTest {

    private final String uri = BancoTestUtil.BANCO_SAVE_URI;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    @Test
    public void testSaveComUsuarioNaoAutorizado() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        String inputJson = objectToString(banco);

        mockMvc
            .perform(post(uri)
                    .contentType(MediaType.APPLICATION_JSON)                 
                    .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.NAO_AUTORIZADO))
                    .content(inputJson))
            .andExpect(status().isForbidden());
    }
    
    @Test
    public void testSaveSemToken() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        String inputJson = objectToString(banco);

        mockMvc
            .perform(post(uri)
                    .contentType(MediaType.APPLICATION_JSON)                                           
                    .content(inputJson))                
            .andExpect(status().isForbidden());
    }
    
    @Test
    public void testSaveComUsuarioFuncionario() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        String inputJson = objectToString(banco);

        mockMvc
            .perform(post(uri)
                    .contentType(MediaType.APPLICATION_JSON)                   
                    .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.FUNCIONARIO))
                    .content(inputJson))                
            .andExpect(status().isForbidden());
    }

    @Test
    public void testSaveSemCamposObrigatorios() throws Exception {
        Banco banco = BancoTestUtil.bancoSemCamposObrigatorios();
        
        String inputJson = objectToString(banco);

        MvcResult mvcResult = mockMvc
                .perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))             
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
        
        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")));
        
        assertEquals(response.get(DefaultExceptionAttributes.EXCEPTION).getAsString(), TestUtil.VALIDATION_EXCEPTION);
        
        JsonArray errors = response.get(DefaultExceptionAttributes.MESSAGES).getAsJsonArray();
        
        assertEquals(errors.size(), 4);
        
        List<String> getErrorsMessages = getErrorsMessages(errors);
        
        assertTrue(getErrorsMessages.contains(messageSource.getMessage("bancoCnpjInvalido", null, null)));
        assertTrue(getErrorsMessages.contains(messageSource.getMessage("bancoCnpjNaoPodeSerNulo", null, null)));
        assertTrue(getErrorsMessages.contains(messageSource.getMessage("bancoNomeNaoPodeSerNulo", null, null)));
        assertTrue(getErrorsMessages.contains(messageSource.getMessage("bancoNumeroDeveSerMaiorDoQueZero", null, null)));
    }
    
    @Test
    public void testSaveComNumeroMenorDoQueUm() throws Exception {
        Banco banco = BancoTestUtil.bancoComNumeroMenorDoQueUm();
        
        String inputJson = objectToString(banco);

        MvcResult mvcResult = mockMvc
                .perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))
                .andExpect(status().isUnprocessableEntity())                
                .andReturn();
        
        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")));
        
        assertEquals(response.get(DefaultExceptionAttributes.EXCEPTION).getAsString(), TestUtil.VALIDATION_EXCEPTION);
        
        JsonArray errors = response.get(DefaultExceptionAttributes.MESSAGES).getAsJsonArray();
        
        assertEquals(errors.size(), 1);
        
        List<String> getErrorsMessages = getErrorsMessages(errors);
        
        assertTrue(getErrorsMessages.contains(messageSource.getMessage("bancoNumeroDeveSerMaiorDoQueZero", null, null)));
    }
    
    @Test
    public void testSaveComNumeroDuplicado() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        String inputJson = objectToString(banco);

        MvcResult mvcResult = mockMvc
                .perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))    
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
        
        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")));
        
        assertEquals(response.get(DefaultExceptionAttributes.EXCEPTION).getAsString(), TestUtil.NOT_UNIQUE_EXCEPTION);
        assertEquals(response.get(DefaultExceptionAttributes.MESSAGE).getAsString(), messageSource.getMessage("bancoNumeroDeveSerUnico", null, null));
    }
    
    @Test
    public void testSaveComCnpjInvalido() throws Exception {
        Banco banco = BancoTestUtil.bancoComCnpjInvalido();
        
        String inputJson = objectToString(banco);

        MvcResult mvcResult = mockMvc
                .perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))     
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
        
        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")));
        
        assertEquals(response.get(DefaultExceptionAttributes.EXCEPTION).getAsString(), TestUtil.VALIDATION_EXCEPTION);
        
        JsonArray errors = response.get(DefaultExceptionAttributes.MESSAGES).getAsJsonArray();
        
        assertEquals(errors.size(), 1);
        
        List<String> getErrorsMessages = getErrorsMessages(errors);
        
        assertTrue(getErrorsMessages.contains(messageSource.getMessage("bancoCnpjInvalido", null, null)));
    }
    
    @Test
    public void testSaveComNomeComMenosDeDoisCaracteres() throws Exception {
        Banco banco = BancoTestUtil.bancoComNomeComMenosDeDoisCaracteres();
        
        String inputJson = objectToString(banco);

        MvcResult mvcResult = mockMvc
                .perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))    
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
        
        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")));
        
        assertEquals(response.get(DefaultExceptionAttributes.EXCEPTION).getAsString(), TestUtil.VALIDATION_EXCEPTION);
        
        JsonArray errors = response.get(DefaultExceptionAttributes.MESSAGES).getAsJsonArray();
        
        assertEquals(errors.size(), 1);
        
        List<String> getErrorsMessages = getErrorsMessages(errors);
        
        assertTrue(getErrorsMessages.contains(messageSource.getMessage("bancoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null)));
    }
    
    @Test
    public void testSaveComNomeComMaisDeDuzentosECinquentaECincoCaracteres() throws Exception {
        Banco banco = BancoTestUtil.bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres();
        
        String inputJson = objectToString(banco);

        MvcResult mvcResult = mockMvc
                .perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
        
        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")));
        
        assertEquals(response.get(DefaultExceptionAttributes.EXCEPTION).getAsString(), TestUtil.VALIDATION_EXCEPTION);
        
        JsonArray errors = response.get(DefaultExceptionAttributes.MESSAGES).getAsJsonArray();
        
        assertEquals(errors.size(), 1);
        
        List<String> getErrorsMessages = getErrorsMessages(errors);
        
        assertTrue(getErrorsMessages.contains(messageSource.getMessage("bancoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null)));
    }
    
    @Test
    public void testSaveComSucesso() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        String inputJson = objectToString(banco);

        MvcResult mvcResult = mockMvc
                .perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))
                .andExpect(status().isOk())
                .andReturn();

        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")));

        assertNotNull(response.get(BancoTestUtil.BANCO_ID).getAsLong());
        assertEquals(response.get(BancoTestUtil.BANCO_NUMERO).getAsInt(), banco.getNumero());
        assertEquals(response.get(BancoTestUtil.BANCO_CNPJ).getAsString(), banco.getCnpj());
        assertEquals(response.get(BancoTestUtil.BANCO_NOME).getAsString(), banco.getNome());
    }
}
