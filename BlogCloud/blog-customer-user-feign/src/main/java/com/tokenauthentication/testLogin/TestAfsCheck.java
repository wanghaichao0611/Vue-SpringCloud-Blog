package com.tokenauthentication.testLogin;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigRequest;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.requset.IAcsTokenRequest;


public class TestAfsCheck {

    public int testLogin(IAcsTokenRequest iAcsTokenRequest) throws Exception{
        //YOUR ACCESS_KEY、YOUR ACCESS_SECRET请替换成您的阿里云accesskey id和secret
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "个人隐私", "个人隐私");
        IAcsClient client = new DefaultAcsClient(profile);
        DefaultProfile.addEndpoint("自行配置");

        AuthenticateSigRequest request = new AuthenticateSigRequest();
        request.setSessionId(iAcsTokenRequest.getSessionId());// 必填参数，从前端获取，不可更改，android和ios只传这个参数即可
        request.setSig(iAcsTokenRequest.getSig());// 必填参数，从前端获取，不可更改
        request.setToken(iAcsTokenRequest.getAliToken());// 必填参数，从前端获取，不可更改
        request.setScene(iAcsTokenRequest.getScene());// 必填参数，从前端获取，不可更改
        request.setAppKey("个人隐私");// 必填参数，后端填写
        request.setRemoteIp("1");// 必填参数，后端填写

        try {
            AuthenticateSigResponse response = client.getAcsResponse(request);
            if(response.getCode() == 100) {
                return 1;
            } else {
               return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}