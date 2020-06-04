package br.ufscar.dc.latosensu.aplicacaofinanceira;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class BaseIntegrationTest {
    
    @Autowired
    protected MockMvc mockMvc;
    
    protected List<String> getErrorsMessages(JsonArray errors) {
        List<String> errorsMessages = new ArrayList<>();
        
        for (int i = 0; i < errors.size(); i++) {
            errorsMessages.add(errors.get(i).getAsString());
        }
        
        return errorsMessages;
    }
    
    protected String objectToString(Object object) {
        return new Gson().toJson(object);
    }
    
    protected JsonArray stringToJsonArray(String string) {
        return new Gson().fromJson(string, JsonArray.class);
    }
    
    protected JsonObject stringToJsonObject(String string) {
        return new Gson().fromJson(string, JsonObject.class);
    }    
}
