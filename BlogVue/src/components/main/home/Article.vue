<template>
  <div class="row clearfix">
    <Card style="width: 105%;" dis-hover>
      <div style="text-align:center">
        <List item-layout="vertical" >
          <ListItem  v-for="(item,index) in visitAll"  :key="item.articleTitle" >
            <ListItemMeta  :avatar="userAvatar" :title="item.articleTitle">
              <template slot="avatar">
                <Avatar  :src="userAvatar[index]"/>&nbsp;
              </template>
              <template slot="title">
                <div class="text-left">&nbsp;&nbsp;<el-link style="color: transparent" :underline="false" target="_blank" :href="articleUrl+item.id"><Strong style="color: black">{{ item.articleTitle }}</Strong></el-link></div>
              </template>
            </ListItemMeta>
            {{ item.articleSummary }}
            <template slot="action">
              <li>
                <div class="text-left" style="color: black" v-if="item.articleIsOriginal===true"><Tag color="error" size="small">原创</Tag>&nbsp;</div>
                <div class="text-left" style="color: black" v-if="item.articleIsForward===true"><Tag color="error" size="small">转载</Tag></div>
                  <div class="text-left" style="color: black" v-if="item.articleIsTranslate===true"><Tag color="error" size="small">翻译</Tag></div>
              </li>
              <li>
                <span v-html="blogName[index]">{{blogName[index]}}</span>
              </li>
              <li>
                最后发布于 {{ item.articlePublishDate }}
              </li>
              <li>
                <Icon type="ios-thumbs-up-outline" />&nbsp;&nbsp;{{ item.articleThumbUp }}
              </li>
              <li>
                <Icon type="ios-chatboxes" />&nbsp;&nbsp; {{ item.articleComment }}
              </li>
              <li>
                <Icon type="ios-star" />&nbsp;&nbsp; {{ item.articleCollection}}
              </li>
              <li>
                <Icon type="ios-eye" />&nbsp;&nbsp;{{ item.articleReading }}
              </li>
              <li>
                <Icon type="ios-redo-outline" />&nbsp;&nbsp;{{ item.articleForward }}
              </li>
            </template>
            <template slot="extra" v-if="blogFirstUrl[index]!=='' && blogFirstUrl[index]!==null">
              <img :src="blogFirstUrl[index]" style="height: 150px;width: 280px;" alt="自己博客中的图片" />
            </template>
            <template slot="extra" v-else>
              <img src="../../../../static/img/blogTwoMain/tree.jpg" style="width: 280px" alt="官方博客配的图片" />
            </template>
          </ListItem>
        </List>
      </div>
    </Card>
  </div>
</template>
<style>
  .top{
    padding: 1px;
    background: black;
    color: #fff;
    text-align: center;
    border-radius: 2px;
  }
</style>
<script>
    export default {
        inject: ['reload'],
        data(){
            return {
                personBlogName: window.localStorage.getItem('blogName'),
                articleUrl: 'http://localhost:8088/home/details/',
                //当前的页数
                page: 1,
                //每页的条数
                count: 5,
                UserManageShow: window.localStorage.getItem('userPicture'),
                blogRealUrl: 'http://localhost:8088/home/details/',
                //博客的实体与博客的总数
                visitAll: [
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
                        pages: '',
                    }
                ],
                blogName: [],
                userAvatar: [],
                blogFirstUrl: [],
            }
        },
        created() {
            //查询分页数据
            this.selectOtherPage();
            //页面路滑加载
            window.addEventListener('scroll', this.onScroll);
        },
        methods: {
            //轮滑加载博客
            onScroll() {
                //可滚动容器的高度
                let innerHeight = document.querySelector('#app').clientHeight;
                //屏幕尺寸高度
                let outerHeight = document.documentElement.clientHeight;
                //可滚动容器超出当前窗口显示范围的高度
                let scrollTop = document.documentElement.scrollTop;
                if (innerHeight < (outerHeight + scrollTop)) {
                    //加载更多操作
                    this.selectOtherPage();

                }else if (this.count>this.visitAll.total+5) {
                    //取消监听轮滑容器
                   window.removeEventListener('scroll', this.onScroll);
                   alert("没有更多了!")
                }
            },
            //查询最新发的博客100张博客,都必须保持登录状态。
            selectOtherPage(){
                this.$http.post('/whc/blog-customer-user/articleVisitAllFeign', {
                    page: this.page,
                    count: this.count,
                }).then(res => {
                    console.log(res);
                    this.visitAll = res.data.articleAll.list;
                    this.visitAll.total = res.data.articleAll.total;
                    this.visitAll.pages = res.data.articleAll.pages;
                    this.count = this.count + 2;

                    //循环遍历文章的用户的博客名
                    if (res.data.blogNameAll.length > 0) {
                        let name = [];
                        name = res.data.blogNameAll.split(',');
                        for (let x = 0; x < name.length - 1; x++) {
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
                        for (let i = 0; i < url.length - 1; i++) {
                            let t = url[i];
                            if (t != null) {
                                this.userAvatar[i] = t;
                            }
                        }
                    }
                    //遍历文章中的第一张图篇，没有用放好的图片
                    if (res.data.articleAll.list.length > 0) {
                        let picUrl = [];
                        let reg= /^((ht|f)tps?):\/\/[\w\-]+(\.[\w\-]+)+([\w\-\.,@?^=%&:\/~\+#]*[\w\-\@?^=%&\/~\+#])?$/;
                        for (let i = 0; i < res.data.articleAll.list.length; i++) {
                            let a=res.data.articleAll.list[i].articleMdContent.indexOf('](')+2;
                            let b=res.data.articleAll.list[i].articleMdContent.indexOf(')');
                            if (a>=0 && b>=0){
                                picUrl[i] = res.data.articleAll.list[i].articleMdContent
                                    .substring(a,b);
                                if ( picUrl[i].match('http',picUrl[i])){
                                    this.blogFirstUrl[i] =picUrl[i];
                                    console.log(picUrl[i]);
                                }else {
                                    picUrl[i]='';
                                    this.blogFirstUrl[i] =picUrl[i];
                                }
                            }else {
                                picUrl[i]='';
                                this.blogFirstUrl[i] =picUrl[i];
                            }
                        }
                    }
                })
            }
        },
        mounted() {
        }
    }
</script>

