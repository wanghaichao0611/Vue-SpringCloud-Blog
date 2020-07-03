package com.ServiceImpl;

import com.Service.UsernameToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

@Service
public class UsernameTokenImpl implements UsernameToken {

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String getUsername(String token) {

        //根据Redis中的双向绑定可以根据Token来获得username
            Jedis jedis = new Jedis("localhost", 6379);
            String username="";
            username=jedis.get(token);
            return username;
    }
}
