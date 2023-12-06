package com.financeTracker.Utility;


import org.springframework.web.client.RestTemplate;

public class RequestHandler {

    public static String fetchData(String apiUrl) 
    {
       RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(apiUrl, String.class);
        return result;
    }
}
