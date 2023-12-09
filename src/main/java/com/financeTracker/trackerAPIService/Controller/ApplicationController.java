package com.financeTracker.trackerAPIService.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.Utility.RequestHandler;
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

        stringBuilder.append("mutualFundData/create  - This takes in application-json.<br>");
        stringBuilder.append("mutualFundData/read/{mutualFundCode}  - This returns the data about mutual fund.<br>");
        stringBuilder.append("mutualFundData/update  - This takes in application-json.<br>");
        stringBuilder.append("mutualFundData/delete/{mutualFundCode}  - This deletes the mutual fund entry.<br>");
        stringBuilder.append("mutualFundMarket/navValue/{mutualFundCode}  - This returns the current market value of a mutual fund.");
       

        return stringBuilder.toString();
    }

    @GetMapping("mutualFundMarket/navValue/{mutualFundCode}")
    public float GetNavData(@PathVariable long mutualFundCode)
    {
        if(IsRequestAuthenticated()==false) {
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

    private boolean IsRequestAuthenticated()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication!=null && authentication.isAuthenticated();
    }


}
