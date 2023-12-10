package com.financeTracker.Utility;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAuthentication {

    private static String _userId;

    public static boolean IsRequestAuthenticated()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication!=null && authentication.isAuthenticated();
    }


    public static String GetUserId()
    {
        if(_userId==null || _userId.length()==0)
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            _userId = authentication.getName();
        }
        
        return _userId;
    }
    
}
