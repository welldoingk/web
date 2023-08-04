package com.kmpc.web.util;

import com.kmpc.web.member.entity.Member;
import com.kmpc.web.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

    public static Member getMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

        return principal.getMember();
    }

    public boolean isWin(){
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) 
        {
            return true;
        }
        return false;
    }
}
