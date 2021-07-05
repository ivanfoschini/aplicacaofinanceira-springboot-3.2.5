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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UpdateBancoIntegrationTest extends BaseIntegrationTest {

    private final String uri = BancoTestUtil.BANCO_UPDATE_URI + TestUtil.ID_COMPLEMENT_URI;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    @Test
    public void testUpdateComUsuarioNaoAutorizado() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        String inputJson = objectToString(bancoDoBrasil);

        mockMvc
            .perform(put(uri, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.NAO_AUTORIZADO))
                    .content(inputJson))                
            .andExpect(status().isForbidden());
    }
    
    @Test
    public void testUpdateSemToken() throws Exception {
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
    public void testUpdateComUsuarioFuncionario() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        String inputJson = objectToString(bancoDoBrasil);

        mockMvc
            .perform(put(uri, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.FUNCIONARIO))
                    .content(inputJson))                
            .andExpect(status().isForbidden());
    }  
    
    @Test
    public void testUpdateSemCamposObrigatorios() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco bancoSemCamposObrigatorios = BancoTestUtil.bancoSemCamposObrigatorios();
        
        String inputJson = objectToString(bancoSemCamposObrigatorios);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, id)
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
    public void testUpdateComNumeroMenorDoQueUm() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco bancoComNumeroMenorDoQueUm = BancoTestUtil.bancoComNumeroMenorDoQueUm();
        
        String inputJson = objectToString(bancoComNumeroMenorDoQueUm);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, id)
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
    public void testUpdateComNumeroDuplicado() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        Banco caixaEconomicaFederal = BancoTestUtil.caixaEconomicaFederal();        
        
        bancoRepository.save(bancoDoBrasil);        
        bancoRepository.save(caixaEconomicaFederal);
        
        Long id = caixaEconomicaFederal.getId();
        
        String inputJson = objectToString(bancoDoBrasil);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, id)
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
    public void testUpdateComCnpjInvalido() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoComCnpjInvalido();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco bancoComCnpjInvalido = BancoTestUtil.bancoComCnpjInvalido();
        
        String inputJson = objectToString(bancoComCnpjInvalido);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, id)
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
    public void testUpdateComNomeComMenosDeDoisCaracteres() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco bancoComNomeComMenosDeDoisCaracteres = BancoTestUtil.bancoComNomeComMenosDeDoisCaracteres();
        
        String inputJson = objectToString(bancoComNomeComMenosDeDoisCaracteres);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, id)
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
    public void testUpdateComNomeComMaisDeDuzentosECinquentaECincoCaracteres() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres = BancoTestUtil.bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres();
        
        String inputJson = objectToString(bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, id)
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
    public void testUpdateComBancoInexistente() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        String inputJson = objectToString(bancoDoBrasil);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, 0)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))                
                .andExpect(status().isNotFound())
                .andReturn();
        
        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")));
        
        assertEquals(response.get(DefaultExceptionAttributes.EXCEPTION).getAsString(), TestUtil.NOT_FOUND_EXCEPTION);
        assertEquals(response.get(DefaultExceptionAttributes.MESSAGE).getAsString(), messageSource.getMessage("bancoNaoEncontrado", null, null));
    }
    
    @Test
    public void testUpdateComSucesso() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco caixaEconomicaFederal = BancoTestUtil.caixaEconomicaFederal();
        
        String inputJson = objectToString(caixaEconomicaFederal);

        MvcResult mvcResult = mockMvc
                .perform(put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TestUtil.TOKEN, new SecurityUtil().getToken(TestUtil.ADMIN))
                        .content(inputJson))                
                .andExpect(status().isOk())
                .andReturn();

        JsonObject response = stringToJsonObject(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")));

        assertNotNull(response.get(BancoTestUtil.BANCO_ID).getAsLong());
        assertEquals(response.get(BancoTestUtil.BANCO_NUMERO).getAsInt(), caixaEconomicaFederal.getNumero());
        assertEquals(response.get(BancoTestUtil.BANCO_CNPJ).getAsString(), caixaEconomicaFederal.getCnpj());
        assertEquals(response.get(BancoTestUtil.BANCO_NOME).getAsString(), caixaEconomicaFederal.getNome());
    }
}
