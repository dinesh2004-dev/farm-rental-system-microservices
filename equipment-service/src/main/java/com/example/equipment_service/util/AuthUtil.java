package com.example.equipment_service.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.Objects;

public class AuthUtil {
    public static String getUserId(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null){

            return null;
        }

        Object details = auth.getDetails();

        if (details instanceof Map<?, ?> map) {
            Object id = map.get("userId");
            return id == null ? null : id.toString();
        }

        return null;
    }
}
