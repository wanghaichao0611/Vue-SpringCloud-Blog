package com.es.Conifg;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
@Configuration
public class EsConfig {
    @Bean
    public TransportClient getTransportClient() throws UnknownHostException {
        // ES默认TCP端口是9300
        InetSocketTransportAddress node = new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300);
        TransportClient client = null;
        try {
            //注意：需要修改安装的elasticsearch的配置文件/config/elasticsearch.yml的第17行的集群名对应自己这里“my-es”和第55行的ip对应自己这里的“127.0.0.1” ，并打开这两行注释。
            Settings settings = Settings.builder().put("cluster.name","whc-es").build();//集群名字：es需要对应elasticsearch.yml中cluster.name
            client = new PreBuiltTransportClient(settings);
            // 多个client多次new InetSocketTransportAddress，多次添加就行
            client.addTransportAddress(node);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("elasticsearch TransportClient create error!!");
        }
        return client;

    }

}