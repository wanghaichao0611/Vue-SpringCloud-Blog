package com.article.response;


import lombok.Data;
import org.springframework.stereotype.Repository;

@Data
@Repository
public class UserArticleCountAll {

    private int originalTotal;
    private int thumpTotal;
    private int commentTotal;
    private int readTotal;

}
