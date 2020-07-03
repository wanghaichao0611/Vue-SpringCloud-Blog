package com.Service;

import org.springframework.stereotype.Service;

@Service
public interface UsernameToken {

    //根据Token获得User表中的Username
    public String getUsername(String token);
}
