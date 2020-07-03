package com.requset;

import lombok.Data;
import org.springframework.stereotype.Repository;

@Data
@Repository
public class IAcsTokenRequest {

    private String Scene;
    private String aliToken;
    private String sessionId;
    private String sig;

}
