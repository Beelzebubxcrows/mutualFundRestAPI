package com.financeTracker.trackerAPIService.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.Utility.RequestHandler;
import com.financeTracker.Utility.UserAuthentication;
import com.financeTracker.trackerAPIService.Model.ModelRepository;
import com.financeTracker.trackerAPIService.SerializeClass.NavDataSerializeClass;

@RestController
public class ApplicationController {
    
    private static String apiUrl  = "https://api.mfapi.in/mf/%s/latest";

    @Autowired
    ModelRepository modelRepository;

    
    @GetMapping("/")
    public String OnAuthenticationComplete(OAuth2AuthenticationToken authToken)
    {
       
        OAuth2User userInfo = authToken.getPrincipal();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" Welcome "+userInfo.getAttribute("given_name")+" ! You are logged in.<br><br>");

        stringBuilder.append("RESPONSE format : <br>");
        stringBuilder.append("{\"status\":\"\",\"data\":\"\"}");
stringBuilder.append("<br><br>");
        stringBuilder.append("POST APIs : <br><br>");
        
        stringBuilder.append("ENDPOINT : mutualFundData/create <br>");
       
        stringBuilder.append("BODY : application/json <br>");
        stringBuilder.append("JSON format : {\n" + //
                "  \n" + //
                "      \"mutualFundId\": \"122639\",\n" + //
                "      \"mutualFundName\": \"Parag Parikh Flexi Cap Fund - Direct Plan - Growth\",\n" + //
                "      \"investedUnits\": 344.6,\n" + //
                "      \"investedValue\": 19749\n" + //
                "    \n" + //
                "  \n" + //
                "} <br>");
                stringBuilder.append("DESCRIPTION : This creates an entry for the provided mutual fund.");
        stringBuilder.append("<br><br>");

        stringBuilder.append("ENDPOINT : mutualFundData/update <br>");
       
        stringBuilder.append("BODY : application/json <br>");
        stringBuilder.append("JSON format : {\n" + //
                "  \n" + //
                "      \"mutualFundId\": \"122639\",\n" + //
                "      \"mutualFundName\": \"Parag Parikh Flexi Cap Fund - Direct Plan - Growth\",\n" + //
                "      \"investedUnits\": 344.6,\n" + //
                "      \"investedValue\": 19749\n" + //
                "    \n" + //
                "  \n" + //
                "} <br>");
stringBuilder.append("DESCRIPTION : This updates the already present entry.");
stringBuilder.append("<br><br>");

        stringBuilder.append("ENDPOINT : mutualFundData/delete/{mutualFundCode} <br>");
        stringBuilder.append("TYPE : POST <br>");
    stringBuilder.append("DESCRIPTION : This deletes the already present entry.");

stringBuilder.append("<br><br><br>");

stringBuilder.append("GET APIs : <br><br>");

  stringBuilder.append("ENDPOINT : mutualFundData/readAll <br>");
     
        stringBuilder.append("DESCRIPTION : Gives data about all the mutual fund present");
    

stringBuilder.append("<br><br>");

stringBuilder.append("ENDPOINT : mutualFundData/read/{mutualFundCode} <br>");
      
        stringBuilder.append("DESCRIPTION :Gives data about a particular mutual fund present");
    

stringBuilder.append("<br><br>");


stringBuilder.append("ENDPOINT : mutualFundData/getMutualFundReport <br>");
       
        stringBuilder.append("DESCRIPTION :Generates a mutual fund report.");
    

stringBuilder.append("<br><br>");


        return stringBuilder.toString();
    }

    @GetMapping("mutualFundMarket/navValue/{mutualFundCode}")
    public float GetNavData(@PathVariable long mutualFundCode)
    {
        if(UserAuthentication.IsRequestAuthenticated()==false) {
            return 0f;
        }

        return GetMutualFundNavValue(mutualFundCode);
    }


    public static float GetMutualFundNavValue(long mutualFundCode)
    {
        String outputString = RequestHandler.fetchData(String.format(apiUrl, mutualFundCode));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            NavDataSerializeClass navDataSerializeClass = objectMapper.readValue(outputString,NavDataSerializeClass.class);
            if(navDataSerializeClass.data.size()==0)
            {
                return -1;
            }
            return navDataSerializeClass.data.get(0).nav;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } 

        return 0f;

    }


}
