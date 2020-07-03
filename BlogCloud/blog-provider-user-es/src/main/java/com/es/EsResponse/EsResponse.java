package com.es.EsResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.Date;


//自定义高亮关键字搜索，实体与索引不一致。
@Data
@Repository
public class EsResponse {

    //文章主键Id
    private Long articleId;
    //分类
    private String category;
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

    public EsResponse(Long articleId, String category, String title, String summary, Date articlePublishDate, Date articleUpdateDate) {
        this.articleId = articleId;
        this.category = category;
        this.title = title;
        this.summary = summary;
        this.articlePublishDate = articlePublishDate;
        this.articleUpdateDate = articleUpdateDate;
    }
}
