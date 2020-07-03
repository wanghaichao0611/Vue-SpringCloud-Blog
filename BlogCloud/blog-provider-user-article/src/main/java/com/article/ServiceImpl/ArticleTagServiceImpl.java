package com.article.ServiceImpl;

import com.article.Entity.UserArticleTag;
import com.article.Mapper.UserArticleTagMapper;
import com.article.Service.ArticleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ArticleTagServiceImpl implements ArticleTagService {

    //标签的Mapper
    @Autowired
    UserArticleTagMapper userArticleTagMapper;

    //通过文章标签的ID找到所属文章的标签
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public UserArticleTag findTagAllById(String tagId) {
        UserArticleTag userArticleTag = new UserArticleTag();
        //防止前端获得的时候出现NULL
        userArticleTag.setTagName("");
        if (tagId.length()>0) {
            String[] tags = tagId.split(",");
            for (String id : tags) {
                if (id != null) {
                    //查询标签的名称
                    UserArticleTag tagAll = userArticleTagMapper.selectAllById(Long.parseLong(id));
                    //循环遍历到标签中
                    userArticleTag.setTagName(userArticleTag.getTagName()+tagAll.getTagName()+",");
                }
            }
        }
        return userArticleTag;
    }
}
