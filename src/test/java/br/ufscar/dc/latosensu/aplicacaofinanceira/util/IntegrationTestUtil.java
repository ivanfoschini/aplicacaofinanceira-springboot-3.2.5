package br.ufscar.dc.latosensu.aplicacaofinanceira.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class IntegrationTestUtil {
    
    @Autowired
    protected MockMvc mockMvc;

    public static final String ADMIN = "admin";
    public static final String FUNCIONARIO = "funcionario";
    public static final String NAO_AUTORIZADO = "naoAutorizado";
    public static final String AUTHORIZATION = "Authorization";

    public static final String DATE_TIME = "dateTime";
    public static final String ERRORS = "errors";
    public static final String EXCEPTION = "exception";
    public static final String CAUSE = "cause";
    public static final String STATUS = "status";

    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String UNPROCESSABLE_ENTITY = "UNPROCESSABLE_ENTITY";

    public static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION = "org.springframework.web.bind.MethodArgumentNotValidException";
    public static final String NOT_FOUND_EXCEPTION = "br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException";
    public static final String NOT_UNIQUE_EXCEPTION = "br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException";

    public static final String ID_COMPLEMENT_URI = "/{id}";

    public final static int DEFAULT_SUCCESS_LIST_SIZE = 3;

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
