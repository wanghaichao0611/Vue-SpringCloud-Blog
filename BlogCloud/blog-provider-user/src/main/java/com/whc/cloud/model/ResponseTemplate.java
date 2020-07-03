package com.whc.cloud.model;

import lombok.Builder;
import lombok.Data;


//返回给前端的实体
@Builder
@Data
public class ResponseTemplate {

    public Integer code;

    public String message;

    public Object data;

}
