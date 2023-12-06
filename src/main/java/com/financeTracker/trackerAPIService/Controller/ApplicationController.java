package com.financeTracker.trackerAPIService.Controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {
    
    @GetMapping("/")
    public String OnAuthenticationComplete(OAuth2AuthenticationToken authToken)
    {
        OAuth2User userInfo = authToken.getPrincipal();
        return "Welcome "+userInfo.getName()+" !\n You are logged in.";
    }


}
