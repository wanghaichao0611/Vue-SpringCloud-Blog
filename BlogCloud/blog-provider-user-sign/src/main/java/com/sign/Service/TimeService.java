package com.sign.Service;

import org.springframework.stereotype.Service;

@Service
public interface TimeService {

    //设置时间凭证
    int setTime(Long id);

}
