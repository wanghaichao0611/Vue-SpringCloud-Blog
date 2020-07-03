<template>
  <div class="row clearfix">
    <div class="col-md-3 column">
  <Card style="width:350px" dis-hover>
    <h5>个人博客简介</h5>
    <el-divider/>
    <div style="text-align:center">
      <el-image :src="UserManageShow" style="width: 100px;height: 100px"></el-image>
      <br>
      <Strong>个人简介的介绍</Strong>
      <Divider orientation="right" v-html="BlogName">{{ BlogName }}</Divider>
    </div>
  </Card>
      <br>
      <Card style="width:350px" dis-hover>
        <div style="text-align:center">
          <h5>博客流量统计</h5>
          <el-divider/>
          <List border size="small">
            <ListItem>文章总数:  {{this.mineAll.total}} 篇 </ListItem>
          </List>
        </div>
      </Card>
    </div>
    <div class="col-md-9 column">
      <Card style="width: 100%" dis-hover>
        <div style="text-align:center">
      <List item-layout="vertical">
        <ListItem v-for="item in mineAll" :key="item.articleTitle">
          <ListItemMeta  :title="item.articleTitle">
            <template slot="title">
              <div class="text-left" style="color: black" v-if="item.articleIsOriginal===true"><Tag color="error" size="small">原创</Tag>&nbsp;<Strong style="color: black">{{ item.articleTitle }}</Strong>&nbsp;&nbsp;<small style="color: blueviolet">开始发布于 {{ item.articlePublishDate }}</small>&nbsp;&nbsp;<span style="color: red" v-if="item.articleExamine===false">审核中</span></div>
              <div class="text-left" style="color: black" v-if="item.articleIsForward===true"><Tag color="error" size="small">转载</Tag>&nbsp;<Strong style="color: black">{{ item.articleTitle }}</strong>&nbsp;&nbsp;<small style="color: blueviolet">开始发布于 {{ item.articlePublishDate }}</small>&nbsp;&nbsp;<span style="color: red" v-if="item.articleExamine===false">审核中</span></div>
              <div class="text-left" style="color: black" v-if="item.articleIsTranslate===true"><Tag color="error" size="small">翻译</Tag>&nbsp;<Strong style="color: black">{{ item.articleTitle }}</strong>&nbsp;&nbsp;<small style="color: blueviolet">开始发布于 {{ item.articlePublishDate }}</small>&nbsp;&nbsp;<span style="color: red" v-if="item.articleExamine===false">审核中</span></div>
            </template>
          </ListItemMeta>
          {{ item.articleSummary }}
          <template slot="action">
            <li>
              <div class="text-left" style="color: black" v-if="item.articleIsPublic===true"><Tag color="success" size="small">所有游客可见</Tag></div>
              <div class="text-left" style="color: black" v-if="item.articleIsPrivate===true"><Tag color="#FFA2D3" size="small">作者本人可见</Tag></div>
              <div class="text-left" style="color: black" v-if="item.articleIsVip===true"><Tag color="error" size="small">会员完整阅读</Tag></div>
            </li>
            <li>
              <Icon type="ios-thumbs-up-outline" />&nbsp;&nbsp;{{ item.articleThumbUp }}
            </li>
            <li>
              <Icon type="ios-chatboxes" />&nbsp;&nbsp; {{ item.articleComment }}
            </li>
            <li>
              <Icon type="ios-star-outline" />&nbsp;&nbsp;{{ item.articleCollection }}
            </li>
            <li>
              <Icon type="ios-eye" />&nbsp;&nbsp;{{ item.articleReading }}
            </li>
            <li>
              <Icon type="ios-redo-outline" />&nbsp;&nbsp;{{ item.articleForward }}
            </li>
            <li>
              <el-link :href="blogSelectUrl+item.id" type="primary" :underline="false" target="_blank">查看</el-link>
            </li>
            <li>
              <el-link :href="blogUpdateUrl+item.id" type="primary" :underline="false" target="_blank">编辑</el-link>
            </li>
            <li style="color: blueviolet">
              最后修改于 {{ item.articleUpdateDate }}
            </li>
            <li>
              <Poptip
                confirm
                transfer="true"
                title="删除的文章无法再恢复!"
                @on-ok="deleteArticleId(item.id)"
                @on-cancel="cancelDelete">
                <Button type="error" ghost>删除文章</Button>
              </Poptip>
            </li>
          </template>
        </ListItem>
      </List>
        </div>
      </Card>
      <br>
      <div class="block" v-if="showPage===true">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page"
          :page-sizes="[5, 10, 20, 30]"
          :page-size="count"
          layout="total, sizes, prev, pager, next, jumper"
          :total="mineAll.total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>
<script>
    export default {
        inject: ['reload'],
        data(){
            return {
                deleteArticle: false,
                showPage: true,
                page: 1,
                count: 5,
                UserManageShow: window.localStorage.getItem('userPicture'),
                BlogName: window.localStorage.getItem('blogName'),
                blogSelectUrl: 'http://localhost:8088/home/details/',
                blogUpdateUrl: 'http://localhost:8088/userMain/userUpdate/',
                //博客的实体与博客的总数
                mineAll: [
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
                        total: '',
                    }
                ],
            }
        },
        created() {
            this.selectPageMine();
        },
        methods: {
            deleteArticleId(articleId) {
                this.$http.post('/whc/blog-customer-user/articleDeleteOneFeign',{
                    articleId: articleId,
                }).then(res =>{
                    console.log(res);
                    if (res.data.success===true){
                        alert('删除文章成功!');
                        this.reload();
                    }else {
                        alert(res.data.message);
                    }
                })
            },
            cancelDelete() {
                this.$Message.info('取消删除文章！');
            },
            // 初始页currentPage、初始每页数据数pagesize和数据data
            handleSizeChange: function (size) {
                this.count = size;
                console.log(this.count); //每页下拉显示数据
                this.selectPageMine();
            },
            handleCurrentChange: function(currentPage){
                this.page = currentPage;
                console.log(this.page);  //点击第几页
                this.selectPageMine();
            },
            //查询自己所发表过的博客
            selectPageMine(){
                this.$http.post('/whc/blog-customer-user/articlePageMineFeign',{
                    page: this.page,
                    count: this.count,
                }).then(res =>{
                    console.log(res);
                    this.mineAll=res.data.list;
                    this.mineAll.total=res.data.total;
                    if (res.data.total===0){
                        this.showPage=false;
                    }
                })
            }
        },
        mounted() {
        }
    }
</script>
