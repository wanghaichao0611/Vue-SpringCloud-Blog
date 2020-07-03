package com.article.requset;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Data
@Repository
public class EsRequest {

    //分类
    private String category;
    //文章主键Id
    private Long articleId;
    //文章主题
    private String title;
    //文章摘要
    private String summary;
    //文章发表时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date articlePublishDate;
    //文章更新修改的时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date articleUpdateDate;
}
