<template>
  <div class="row clearfix">
    <div class="col-md-2 column" >
      <Card style="width:280px" dis-hover>
        <div style="text-align:left">
          <el-avatar :size="50" :src="authorAvatar"/>
          &nbsp;<Strong v-html="authorName">{{ authorName}}</Strong>
        </div>
        <Divider/>
          <div style="text-align:left">
            <List item-layout="vertical" size="small" >
              <ListItem v-for="all in userArticleAll" :key="all.originalTotal">
                <template slot="action">
                  <Form>
                    <FormItem>
                      <Strong style="color: black;">博客文章原创：{{ all.originalTotal }}&nbsp;篇原创</Strong>
                    </FormItem>
                    <FormItem>
                      <Strong style="color: black;">博客文章获赞：{{ all.thumpTotal }}&nbsp;个👍</Strong>
                    </FormItem>
                    <FormItem>
                      <Strong style="color: black;">博客文章评论：{{ all.commentTotal }}&nbsp;个评论</Strong>
                    </FormItem>
                    <FormItem>
                      <Strong style="color: black;">博客文章访问：{{ all.readTotal }}&nbsp;访问量</Strong>
                    </FormItem>
                  </Form>
                </template>
              </ListItem>
            </List>
          </div>
      </Card>
      <Anchor  show-ink
               class="el-aside"
               container=".content-wrapper"
               style="position: fixed;font-size: 15px;width: 200px;">
        <AnchorLink
        >
          <AnchorLink href="#basic" title="顶部" />
          <AnchorLink href="#basic" title="点赞" />
          <AnchorLink href="#basic" title="收藏" />
          <AnchorLink href="#basic" title="分享" />
          <AnchorLink href="#comment" title="评论" />
        </AnchorLink>
      </Anchor>
    </div>
    <div class="col-md-10 column">
      <Content :style="{padding: '0 50px'}">
        <Card style="width: 950px;" dis-hover>
          <div style="min-height: 300px;">
            <List item-layout="vertical">
              <ListItem v-for="(item,index) in articleAll" :key="item.articleTitle">
                <ListItemMeta  :title="item.articleTitle" >
                  <template slot="title">
                    <div class="text-left" style="font-size: larger;color: #2c3e50">{{ item.articleTitle }}&nbsp;&nbsp;<span style="color: red" v-if="item.articleExamine===false">审核中</span></div>
                  </template>
                  <template slot="description">
                    <br>
                    <div class="text-left" >
                      <Tag color="error" v-if="item.articleIsOriginal===true" size="large">原创</Tag>
                      <Tag color="error" v-if="item.articleIsForward===true" size="large">转载</Tag>
                      <Tag color="error" v-if="item.articleIsTranslate===true" size="large">翻译</Tag>&nbsp;
                      <strong style="color: cornflowerblue;font-size: medium" v-html="authorName">{{ authorName }}</strong>&nbsp;
                      &nbsp;&nbsp;最后发布于{{ item.articleUpdateDate }} &nbsp;&nbsp;
                      点赞数： <span style="color: black">{{ item.articleThumbUp }}</span>&nbsp;&nbsp;
                      评论数： <span style="color: black">{{ item.articleComment }}</span>&nbsp;&nbsp;
                      收藏数：  <span style="color: black">{{ item.articleCollection }}</span> &nbsp;&nbsp;
                      阅读数： <span style="color: black">{{ item.articleReading }}</span> &nbsp;&nbsp;
                      转发数： <span style="color: black">{{ item.articleForward }}</span> &nbsp;&nbsp;
                      <div class="text-right">
                        <el-link :href="updateUrl+item.id" :underline="false" type="primary" size="small" v-if="editButtonShow===true">编辑</el-link>&nbsp;&nbsp;&nbsp;
                        <Button type="success" size="small" v-if="urlTagShow===false" @click="urlTagShowOn">展开</Button>
                        <Button type="success" size="small" v-if="urlTagShow===true" @click="urlTagShowOff">收起</Button>
                        <Divider/>
                        <div class="text-left" v-if="urlTagShow===true">
                          文章标签:&nbsp;&nbsp;<Tag color="#FFA2D3" size="large" v-for="tag in articleTag" :key="tag">{{ tag }}</Tag>
                          <br>
                          <br>
                          文章分类所属区域:&nbsp;&nbsp;
                          <el-link :href="categoryUrl+articleAll.articleCategoryName" type="primary" target="_blank" :underline="false">
                            <Tag type="border" color="blue" size="large" v-if="articleAll.articleCategoryName==='test'">测试</Tag>
                            <Tag type="border" color="blue" size="large" v-if="articleAll.articleCategoryName==='operation'">运维</Tag>
                            <Tag type="border" color="blue" size="large" v-if="articleAll.articleCategoryName==='frontEnd'">前端</Tag>
                            <Tag type="border" color="blue" size="large" v-if="articleAll.articleCategoryName==='backEnd'">后端</Tag>
                            <Tag type="border" color="blue" size="large" v-if="articleAll.articleCategoryName==='fullStack'">全栈</Tag>
                            <Tag type="border" color="blue" size="large" v-if="articleAll.articleCategoryName==='framework'">架构</Tag>
                            <Tag type="border" color="blue" size="large" v-if="articleAll.articleCategoryName==='c'">C</Tag>
                            <Tag type="border" color="blue" size="large" v-if="articleAll.articleCategoryName==='cPlus'">C++</Tag>
                            <Tag type="border" color="blue" size="large" v-if="articleAll.articleCategoryName==='java'">Java</Tag>
                            <Tag type="border" color="blue" size="large" v-if="articleAll.articleCategoryName==='php'">PHP</Tag>
                            <Tag type="border" color="blue" size="large" v-if="articleAll.articleCategoryName==='python'">Python</Tag>
                            <Tag type="border" color="blue" size="large" v-if="articleAll.articleCategoryName==='go'">Go</Tag>
                          </el-link>
                          <br>
                          <br>
                          <div class="jumbotron well">
                            版权声明：本文为博主原创文章，遵循 <el-link href="https://creativecommons.org/licenses/by-sa/4.0/" type="primary" :underline="false" target="_blank">CC 4.0 BY-SA</el-link> 版权协议，转载请附上原文出处链接和本声明。<br>
                            本文链接：<el-link :href="blogRealUrl+item.id"  target="_blank" type="primary" :underline="false">
                            {{ blogRealUrl+item.id}}
                          </el-link>
                          </div>
                        </div>
                      </div>
                    </div>
                  </template>
                </ListItemMeta>
                  <article  class="markdown-body" style="text-align: left"  v-html="html" >
                  </article>
                <Button type="primary" ghost v-if="thumpButton===true" @click="thumpArticle">点赞</Button>
                <Button type="primary" ghost v-if="thumpButton===false" @click="cancelThump">取消点赞</Button>&emsp;&emsp;&emsp;&emsp;&emsp;
                <Button type="success" ghost v-if="collectButton===true" @click="collectArticle">收藏</Button>&emsp;&emsp;&emsp;&emsp;&emsp;
                <Button type="success" ghost v-if="collectButton===false" @click="cancelCollect">取消收藏</Button>&emsp;&emsp;&emsp;&emsp;&emsp;
                <Button type="error" ghost>分享</Button>
              </ListItem>
            </List>
          </div>
        </Card>
        <br>
        <Card style="width: 950px" dis-hover>
          <div class="text-left" ><Avatar :src="UserBlogShow" size="small"/>  评论者: <span v-html="BlogName">{{ BlogName }}</span></div>
          <br>
            <Input v-model="comment" maxlength="1000" show-word-limit type="textarea" :autosize="{minRows: 3,maxRows: 6}"
                   placeholder="想对作者说点什么"/>
          <br>
          <br>
          <div class="text-right">
            <Button v-on:click="commentButton" type="error">发表评论</Button>
          </div>
          <br>
          <br>
          <List  item-layout="vertical"  style="text-align: left" >
            <ListItem v-for="(item,index) in commentAll" :key="item.commentId">
              <ListItemMeta >
              <template slot="avatar">
                <Avatar :src="userAvatar[index]"></Avatar>
              </template>
              <template slot="title">
                <span v-html="blogName[index]">{{ blogName[index] }}</span>
              </template>
              </ListItemMeta>
              {{ item.commentContent }}
              <template slot="action">
                <li>
                  评论时间: {{item.commentDate}}
                </li>
                <li>
                  <a href="">查看回复</a>
                </li>
                <li>
                  <a href="">回复</a>
                </li>
              </template>
            </ListItem>
          </List>
          <div class="block">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="page"
              :page-sizes="[5, 10, 20, 30]"
              :page-size="count"
              layout="total, sizes, prev, pager, next, jumper"
              :total="commentAll.total">
            </el-pagination>
          </div>
        </Card>
      </Content>
    </div>
  </div>
</template>
<style>

  .el-aside {
    background-color: transparent;
    color: transparent;
    text-align: center;
    line-height: 10px;
  }

  .markdown-body {
    box-sizing: border-box;
    min-width: 200px;
    /** max-width: 980px; **/
    /** padding: 45px; **/
    max-width: 98%;
    margin: 0 auto;
    box-shadow: 2px 4px 6px gray;
    padding-left: 20px;
    padding-right: 15px;
    padding-top: 40px;
    padding-bottom: 45px;
    margin-bottom: 100px
  }
</style>
<script>

    import showdown from 'showdown'
    import 'github-markdown-css/github-markdown.css'


    export default {
        inject: ['reload'],
        data(){
            return {
                page: 1,
                count: 10,
                id: this.$route.params.id,
                //当前用户的累计
                userArticleAll: [
                    {
                        originalTotal: '',
                        thumpTotal: '',
                        commentTotal: '',
                        readTotal: '',
                    }
                ],
                commentAll: [
                    {
                        commentId: '',
                        commentArticleId: '',
                        commentTotal: '',
                        commentatorId: '',
                        commentContent: '',
                        commentDate: '',
                        commentParentId: '',
                        commentResponseId: '',
                        commentResponseDate: '',
                        total: '',
                    }
                ],
                comment: '',
                updateUrl: 'http://localhost:8088/userMain/userUpdate/',
                editButtonShow: false,
                authorName: '',
                authorAvatar: '',
                articleAll: [
                    {
                        id: '',
                        articleAuthorId: '',
                        articleTitle: '',
                        articleSummary: '',
                        articleMdContent: '',
                        articleTagsId: '',
                        articleCategoryId: '',
                        articleReading: '',
                        articleThumbUp: '',
                        articleComment: '',
                        articleCollection: '',
                        articleForward: '',
                        articleIsPublic: '',
                        articleIsPrivate: '',
                        articleIsVip: '',
                        articleIsOriginal: '',
                        articleIsForward: '',
                        articleIsTranslate: '',
                        articleForwardUrl: '',
                        articleTranslateUrl: '',
                        articleExamine: '',
                        articlePublishDate: '',
                        articleUpdateDate: '',
                        description: '',
                        articleCategoryName: '',
                    },
                ],
                html: '',
                articleTag: [],
                urlTagShow: false,
                //外网的情况均切换为真实IP
                categoryUrl: 'http://localhost:8088/blogMain/',
                blogRealUrl: 'http://localhost:8088/home/details/',
                UserBlogShow: window.localStorage.getItem('userPicture'),
                BlogName: window.localStorage.getItem('blogName'),
                thumpButton: true,
                userThumbUpTotal: 0,
                collectButton: true,
                userCollectTotal: 0,
                blogName: [],
                userAvatar: [],
            }
        },
        created(){
            this.selectAllByArticleId(this.$route.params.id);
            this.getCommentPage(this.$route.params.id);
        },
        methods: {
            // 初始页currentPage、初始每页数据数pagesize和数据data
            handleSizeChange: function (size) {
                this.count = size;
                console.log(this.count); //每页下拉显示数据
                this.getCommentPage(this.id);
            },
            handleCurrentChange: function(currentPage){
                this.page = currentPage;
                console.log(this.page);  //点击第几页
                this.getCommentPage(this.id);
            },
            //分页获取评论
            getCommentPage(articleId){
                this.$http.post('/whc/blog-customer-user/commentPageFeign',{
                    articleId: articleId,
                    page: this.page,
                    count: this.count,
                }).then(res =>{
                    if (res.data.success===true){
                        this.commentAll=res.data.list;
                        this.commentAll.total=res.data.total;
                        //循环遍历文章的用户的博客名
                        if (res.data.blogNameAll.length > 0) {
                            let name = [];
                            name = res.data.blogNameAll.split(',');
                            for (let x = 0; x < name.length-1; x++) {
                                let a = name[x];
                                if (a != null) {
                                    this.blogName[x] = a;
                                }
                            }
                        }
                        //循环遍历文章的用户的头像URL
                        if (res.data.avatarUrlAll.length > 0) {
                            let url = [];
                            url = res.data.avatarUrlAll.split(',');
                            for (let i = 0; i < url.length-1; i++) {
                                let t = url[i];
                                if (t != null) {
                                    this.userAvatar[i]= t;
                                }
                            }
                        }
                    }else {
                        alert("服务开了小差,请重新刷新!")
                    }
                })
            },
            //评论的发布按钮
            commentButton(){
                if (this.comment ===''){
                    alert("输入内容不能为空!")
                }else {
                    this.$http.post('/whc/blog-customer-user/commentPublishFeign', {
                        id: this.id,
                        comment: this.comment,
                    }).then(res => {
                        console.log(res);
                        if (res.data.success === true) {
                            alert("评论成功，要遵守规则!");
                            this.reload();
                        }
                    })
                }
            },
            //展开和收起的按钮的转换
            urlTagShowOn(){
                this.urlTagShow=true;
            },
            urlTagShowOff(){
                this.urlTagShow=false;
            },
            //通过ID查找文章信息,后端统计好的数据也传给前端
            selectAllByArticleId(articleId){
                this.$http.post('/whc/blog-customer-user/selectAllByArticleIdFeign',{
                    articleId: articleId,
                }).then(res =>{
                        console.log(res);
                        if (res.data.success===true){
                            this.articleAll=res.data.articleAll;
                            this.articleAll.articleCategoryName=res.data.articleCategoryName;
                            if (res.data.myself===true){
                                this.editButtonShow=true;
                            }
                            //循环遍历文章的标签
                            if (res.data.articleTag.length>0){
                                let  tags=[];
                                tags=res.data.articleTag.split(",");
                                for (let i=0;i<tags.length-1;i++){
                                    let t=tags[i];
                                    if (t!=null){
                                        this.articleTag[i]=t;
                                    }
                                }
                            }
                            let converter = new showdown.Converter();
                            //转换为HTML
                            let html = converter.makeHtml(res.data.articleAll[0].articleMdContent);
                            //两者转换相等
                            this.html=html;
                            //点赞的按钮
                            this.thumpButton=res.data.thumpButton;
                            //收藏的按钮
                            this.collectButton=res.data.collectButton;

                            this.authorName=res.data.authorName.replace(/,$/gi,'');
                            this.authorAvatar=res.data.authorAvatar.replace(/,$/gi,'');

                            this.userArticleAll=res.data.userArticleAll;
                        }else {
                            alert(res.data.message);
                        }
                    })
            },
            //点赞文章
            thumpArticle(){
                this.$http.post('/whc/blog-customer-user/thumpArticleFeign',{
                    articleId: this.id,
                }).then(res =>{
                    if (res.data.success===true){
                        this.$message.success('点赞成功!');
                        this.thumpButton=false;
                        this.articleAll[0].articleThumbUp=res.data.thumpTotal;

                    }else {
                        this.$message.error('您已经点赞过了!');
                    }
                })
            },
            //取消点赞文章
            cancelThump(){
                this.$http.post('/whc/blog-customer-user/cancelThumpFeign',{
                    articleId: this.id,
                }).then(res =>{
                    if (res.data.success===true){
                        this.$message.success('取消点赞成功!');
                        this.thumpButton=true;
                        this.articleAll[0].articleThumbUp=res.data.thumpTotal;
                    }else {
                        alert('微服务出了小状况，请稍后再试!');
                    }
                })
            },
            //收藏文章
            collectArticle(){
                this.$http.post('/whc/blog-customer-user/collectArticleFeign',{
                    articleId: this.id,
                }).then(res =>{
                    if (res.data.success===true){
                        this.$message.success('收藏成功!');
                        this.collectButton=false;
                        this.articleAll[0].articleCollection=res.data.collectTotal;

                    }else {
                        this.$message.error('您已经收藏过了!');
                    }
                })
            },
            //取消收藏文章
            cancelCollect(){
                this.$http.post('/whc/blog-customer-user/cancelCollectFeign',{
                    articleId: this.id,
                }).then(res =>{
                    if (res.data.success===true){
                        this.$message.success('取消收藏成功!');
                        this.collectButton=true;
                        this.articleAll[0].articleCollection=res.data.collectTotal;
                    }else {
                        alert('微服务出了小状况，请稍后再试!');
                    }
                })
            }
        }
    }
</script>
