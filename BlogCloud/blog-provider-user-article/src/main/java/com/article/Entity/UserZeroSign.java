package com.article.Entity;

import lombok.Data;
import org.springframework.stereotype.Repository;


@Data
@Repository
public class UserZeroSign {
    Boolean comment;
    Boolean thump;
    Boolean rank;

    public UserZeroSign(Boolean comment, Boolean thump, Boolean rank) {
        this.comment = comment;
        this.thump = thump;
        this.rank = rank;
    }
}
