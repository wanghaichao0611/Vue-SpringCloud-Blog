package com.article.Entity;

import lombok.Data;
import org.springframework.stereotype.Repository;

import java.io.Serializable;


@Data
@Repository
public class UserArticleRank implements Serializable {

    private Integer blogId; //博客Id
    private Long total;//博客总点赞数

    public UserArticleRank(Integer blogId, Long total) {
        this.blogId = blogId;
        this.total = total;
    }
}
