package br.ufscar.dc.latosensu.aplicacaofinanceira;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
public class BaseIntegrationTest {

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)                
                .build();
    }

    protected String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    protected <T> T mapFromJsonObject(String json, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
    
    protected <T> T mapFromJsonArray(String array) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();        
        TypeReference<List<Object>> mapType = new TypeReference<List<Object>>() {};
    	
        return objectMapper.readValue(array, mapType);        
    }    
}
