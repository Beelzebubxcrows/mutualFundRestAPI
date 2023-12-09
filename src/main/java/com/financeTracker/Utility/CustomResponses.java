package com.financeTracker.Utility;
import java.util.HashMap;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;

public class CustomResponses {

    public static HashMap<String, String> getForbiddenReponse() {
        HashMap<String, String> response = new HashMap<>();
      response.put("Status", "403");
      response.put("Data", "");
      
      return response;
    }

    public static HashMap<String, String> getSuccessResponse(String data)
    {
        HashMap<String, String> response = new HashMap<>();
    
        response.put("Status", Integer.toString(HTTPResponse.SC_OK));
        response.put("Data", data);
      
        return response;
    }

    public static HashMap<String, String> getErrorResponse(String data)
    {
        HashMap<String, String> response = new HashMap<>();
        response.put("Status", Integer.toString(HTTPResponse.SC_BAD_REQUEST));
        response.put("Data", data);
      
        return response;
    }
    
    
}
