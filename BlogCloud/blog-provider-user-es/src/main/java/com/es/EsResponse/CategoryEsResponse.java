package com.es.EsResponse;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.Date;


//博客主页的搜索
@Data
@Repository
public class CategoryEsResponse {

    //文章主键Id
    private Long articleId;
    //文章主题
    private String articleTitle;
    //文章摘要
    private String summary;
    //文章发表时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date articlePublishDate;
    //文章更新修改的时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date articleUpdateDate;

}
