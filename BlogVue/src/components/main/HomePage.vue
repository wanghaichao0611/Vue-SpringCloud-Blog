<template>
    <div>
      <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
        <el-menu
          :default-active="activeIndex"
          class="el-menu-demo"
          mode="horizontal"
          @select="handleSelect"
          background-color="#f8f8f9"
          text-color="#FFFFFF"
          active-text-color="Transparent" >
          <el-menu-item index="19" disabled></el-menu-item>
          <el-menu-item index="1">
            <Img class="img-circle" src="../../../static/img/firstMain/blogLogo.PNG" style="width: 50px;height: 50px;"></Img>
          </el-menu-item>
          <el-menu-item index="16" disabled></el-menu-item>
          <el-menu-item index="19"><router-link :to="{name: 'Recommend'}" style="text-decoration: none;color: #2c3e50" target="_blank">博客主页</router-link></el-menu-item>
          <el-menu-item index="19" disabled></el-menu-item>
          <el-submenu index="12">
            <template slot="title"><span style="color: #2c3e50">博客中心</span></template>
            <el-menu-item index="12-1"><router-link :to="{name: 'UserPublish'}" style="text-decoration: none;color: #2c3e50" target="_blank">发表博客</router-link></el-menu-item>
            <el-menu-item index="12-2"><router-link :to="{name: 'UserManage'}" style="text-decoration: none;color: #2c3e50" target="_blank">博客管理</router-link></el-menu-item>
            <el-menu-item index="12-3"><router-link :to="{name: 'UserCollection'}" style="text-decoration: none;color: #2c3e50" target="_blank">收藏订阅</router-link></el-menu-item>
          </el-submenu>
          <el-menu-item index="16" disabled></el-menu-item>
          <el-menu-item index="19" disabled></el-menu-item>
          <el-menu-item index="22">
            <el-input placeholder="请输入你想知道的关键字" v-model="esInput" class="searchClass">
              <div slot="prepend">
                <div class="centerClass">
                  <el-select v-model="categoryName" placeholder="分类" style="width: 90px">
                    <el-option label="C" value="c"></el-option>
                    <el-option label="C++" value="cPlus"></el-option>
                    <el-option label="Java" value="java"></el-option>
                    <el-option label="PHP" value="php"></el-option>
                    <el-option label="Python" value="python"></el-option>
                    <el-option label="Go" value="go"></el-option>
                    <el-option label="测试" value="test"></el-option>
                    <el-option label="运维" value="operation"></el-option>
                    <el-option label="前端" value="frontEnd"></el-option>
                    <el-option label="后端" value="backEnd"></el-option>
                    <el-option label="全栈" value="fullStack"></el-option>
                    <el-option label="架构" value="framework"></el-option>
                  </el-select>
                </div>
                <div class="centerClass">
                  <div class="line"></div>
                </div>
              </div>
              <el-button slot="append" icon="el-icon-search" v-on:click="homePageSearch"></el-button>
            </el-input>
          </el-menu-item>
          <el-menu-item index="16" disabled></el-menu-item>
          <el-menu-item index="19" disabled></el-menu-item>
          <el-menu-item index="16" disabled></el-menu-item>
          <el-menu-item index="19" disabled></el-menu-item>
          <el-menu-item index="16" disabled></el-menu-item>
          <el-menu-item index="19" disabled></el-menu-item>
          <el-menu-item index="16" disabled></el-menu-item>
          <el-menu-item index="16">
            <Avatar  v-if="HomePageShow!==null" :src="HomePageShow" size="large"></Avatar>
            <Avatar  v-else style="color: #f8f8f9;background-color: transparent" icon="ios-person" size="large"></Avatar>
          </el-menu-item>
          <el-menu-item index="19">
            <router-link v-if="login===null"  target="_blank" style="text-decoration: none;color: #2c3e50" :to="{name: 'Login'}">注册</router-link>
            <el-link :underline="false"  v-if="login!==null"   style="text-decoration: none;color: #2c3e50" v-else v-on:click="loginOutShow">注销</el-link>
          </el-menu-item>
          <el-menu-item index="19" disabled></el-menu-item>
          <el-submenu index="14">
            <template slot="title"><span style="text-decoration: none;color: #2c3e50">中文</span></template>
            <el-menu-item index="14-1"><span style="text-decoration: none;color: #2c3e50">中文</span></el-menu-item>
            <el-menu-item index="14-2"><span style="text-decoration: none;color: #2c3e50">英语</span></el-menu-item>
            <el-menu-item index="14-3"><span style="text-decoration: none;color: #2c3e50">日语</span></el-menu-item>
            <el-menu-item index="14-1"><span style="text-decoration: none;color: #2c3e50">韩语</span></el-menu-item>
            <el-menu-item index="14-2"><span style="text-decoration: none;color: #2c3e50">阿拉伯</span></el-menu-item>
            <el-menu-item index="14-3"><span style="text-decoration: none;color: #2c3e50">西班牙</span></el-menu-item>
          </el-submenu>
        </el-menu>
      </nav>
      <video id='video'  autoplay loop>
        <source src="../../../static/img/other/seve.mp4" type="video/mp4"/>
      </video>
      <div>
        <div style="position:relative;">
          　<img
          style="width: 100%; height: 100%;"
          src="../../../static/img/blogTwoMain/blogTitle.jpg" alt="author"/>
          <div style="position:absolute; top:50%; left:50%; width:480px; height:240px; margin-left:-240px; margin-top:-120px;">
            <div  v-if="login!==null">
              <h1 style="color: lightskyblue">
                欢迎&nbsp;<span v-html="welcomeTitle">{{ welcomeTitle }}</span>&nbsp;</h1>
              <router-link :to="{name: 'Article'}" target="_blank">
                <br>
                <Button shape="circle"  size="large" ghost>
                  <span style="text-decoration: none;color: #000000">Get into Blog</span>
                </Button>
              </router-link>
            </div>
            <div v-else>
              <div style="position:absolute; top:50%; left:50%; width:480px; height:240px; margin-left:-240px; margin-top:-120px;">
                <h1 style="color: blueviolet">遇见你是一种缘分</h1>
                <br>
                <router-link :to="{name: 'Login'}" style="text-decoration: none;color: #2c3e50" target="_blank"><Button ghost>登录</Button></router-link>
              </div>
            </div>
          </div>
        </div>
        <br>
        <br>
        <br>
      </div>
      <div class="col-md-1 column"></div>
      <div class="col-md-10 column">
        <div class="col-md-12 column" id="basic">
          <div class="col-md-12 column" >
            <div class="col-md-4 column">
              <Card style="width: 320px;">
                <div style="text-align:center">
                  <img class="image" src="../../../static/img/language/c.png" alt="author">
                  <br>
                  <h3 class="text-center" style="color: lightseagreen">
                    C语言
                  </h3>
                  <br>
                  <small>
                    C语言是一门面向过程的、抽象化的通用程序设计语言。
                  </small>
                  <p><Icon type="md-arrow-dropdown" /></p>
                  <router-link :to="{name: 'C'}" class="btn" target="_blank">
                    <button class="btn btn-info btn-sm" type="button">Go Back</button>
                  </router-link>
                </div>
              </Card>
            </div>
            <div class="col-md-4 column">
                <Card style="width: 320px;">
                  <div style="text-align:center">
                    <img class="image" src="../../../static/img/language/cPlus.png" alt="author">
                    <br>
                    <h3 class="text-center" style="color: lightseagreen">
                      C++
                    </h3>
                    <br>
                    <small>
                      C++是C语言的继承，可以进行C语言的过程化程序设计。
                    </small>
                    <p><Icon type="md-arrow-dropdown" /></p>
                    <router-link :to="{name: 'CPlus'}" class="btn" target="_blank">
                      <button class="btn btn-info btn-sm" type="button">Go Back</button>
                    </router-link>
                  </div>
                </Card>
              </div>
            <div class="col-md-4 column">
              <Card style="width: 320px;">
                <div style="text-align:center">
                  <img class="image" src="../../../static/img/language/java.png" alt="author">
                  <br>
                  <h3 class="text-center" style="color: lightseagreen">
                    Java
                  </h3>
                  <br>
                  <small>
                    Java是一门综合性较强的编程语言。
                  </small>
                  <p><Icon type="md-arrow-dropdown" /></p>
                  <router-link :to="{name: 'Java'}" class="btn" target="_blank">
                    <button class="btn btn-info btn-sm" type="button">Go Back</button>
                  </router-link>
                </div>
              </Card>
            </div>
          </div>
          &emsp;&emsp;&emsp;
        </div>
        <div class="col-md-12 column" id="new">
          <div class="col-md-12 column">
            <div class="col-md-4 column">
              <Card style="width: 320px;">
                <div style="text-align:center">
                  <img class="image" src="../../../static/img/language/php.png" alt="author">
                  <br>
                  <h3 class="text-center" style="color: lightseagreen">
                    PHP
                  </h3>
                  <br>
                  <small>
                    PHP主要适用于Web开发领域。
                  </small>
                  <p><Icon type="md-arrow-dropdown" /></p>
                  <router-link :to="{name: 'C'}" class="btn" target="_blank">
                    <button class="btn btn-info btn-sm" type="button">Go Back</button>
                  </router-link>
                </div>
              </Card>
            </div>
            <div class="col-md-4 column">
              <Card style="width: 320px;">
                <div style="text-align:center">
                  <img class="image" src="../../../static/img/language/python.png" alt="author">
                  <br>
                  <h3 class="text-center" style="color: lightseagreen">
                    Python
                  </h3>
                  <br>
                  <small>
                    Python是一种跨平台的计算机程序设计语言。
                  </small>
                  <p><Icon type="md-arrow-dropdown" /></p>
                  <router-link :to="{name: 'Python'}" class="btn" target="_blank">
                    <button class="btn btn-info btn-sm" type="button">Go Back</button>
                  </router-link>
                </div>
              </Card>
            </div>
            <div class="col-md-4 column">
              <Card style="width: 320px;">
                <div style="text-align:center">
                  <img class="image" src="../../../static/img/language/go.png" alt="author">
                  <br>
                  <h3 class="text-center" style="color: lightseagreen">
                    Go
                  </h3>
                  <br>
                  <small>
                    Go（又称 Golang) 是一种静态强类型、编译型语言。
                  </small>
                  <p><Icon type="md-arrow-dropdown" /></p>
                  <router-link :to="{name: 'Go'}" class="btn" target="_blank">
                    <button class="btn btn-info btn-sm" type="button">Go Back</button>
                  </router-link>
                </div>
              </Card>
            </div>
          </div>
          &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
        </div>
        <div class="col-md-12 column">
          <div class="col-md-12 column">
        <Carousel autoplay autoplay-speed="10000" v-model="carouselDefault" loop dots="none" arrow="hover">
          <CarouselItem>
            <div class="demo-carousel">
              <div style="position:relative;">
                　<img
                style="width: 100%; height: 50%"
                src="../../../static/img/firstMain/loginOne.jpg" alt="author"/>
                <div style="position:absolute; top:50%; left:50%; width:480px; height:240px; margin-left:-240px; margin-top:-120px;">
                  <div  v-if="login!==null">
                    <h1 style="color: lightskyblue">
                      欢迎&nbsp;<span v-html="welcomeTitle">{{ welcomeTitle }}</span>&nbsp;</h1>
                    <router-link :to="{name: 'Article'}">
                      <br>
                      <Button shape="circle"  size="large" ghost>
                        <span style="text-decoration: none;color: #000000">Get into Blog</span>
                      </Button>
                    </router-link>
                  </div>
                  <div v-else>
                    <Button type="primary" @click="loginTo"  ghost>登录</Button>
                  </div>
                </div>
              </div>
            </div>
          </CarouselItem>
          <CarouselItem>
            <div class="demo-carousel">
              <img
                style="width: 100%; height: 100%"
                src="../../../static/img/firstMain/loginTwo.jpg" alt="author"/>
            </div>
          </CarouselItem>
          <CarouselItem>
            <div class="demo-carousel">
              <img
                style="width: 100%; height: 100%"
                src="../../../static/img/firstMain/loginThree.jpg" alt="author"/>
            </div>
          </CarouselItem>
          <CarouselItem>
            <div class="demo-carousel">
              <img
                style="width: 100%; height: 100%"
                src="../../../static/img/firstMain/loginFour.jpg" alt="author"/>
            </div>
          </CarouselItem>
          <CarouselItem>
            <div class="demo-carousel">
              <img
                style="width: 100%; height: 100%"
                src="../../../static/img/firstMain/loginFive.jpg" alt="author"/>
            </div>
          </CarouselItem>
          <CarouselItem>
            <div class="demo-carousel">
              <img
                style="width: 100%; height: 100%"
                src="../../../static/img/firstMain/loginSix.jpg" alt="author"/>
            </div>
          </CarouselItem>
        </Carousel>
          <br>
          <br>
          <br>
          </div>
      </div>
        <div class="col-md-12 column" id="articleRank">
          <div class="col-md-12 column">
            <Card style="width: 1230px;">
              <div style="text-align:center">
                <Divider orientation="right"></Divider>
                <h3>恶意刷热度且涉及黄赌毒会被封号处理,情节严重则送往公安机关处理</h3>
                <Divider orientation="left">博客热度排行榜</Divider>
                <div>
                  <Table highlight-row ref="currentRowTable" :columns="rankColumns" :data="rankData">
                    <template slot-scope="{ row, index }" slot="action">
                      <el-link :href="blogRealUrl+rankData[index].id"  target="_blank" type="primary" :underline="false">
                      <Button type="success" size="small" style="margin-right: 5px">查看</Button>
                      </el-link>
                      <Button type="error" size="small">举报</Button>
                    </template>
                    <template slot-scope="{ row }" slot="name">
                      <span v-html="row.name">{{ row.name }}</span>
                    </template>
                  </Table>
                </div>
              </div>
            </Card>
          </div>
        </div>
        &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
        <div class="col-md-12 column" id="myself">
          <div class="col-md-12 column">
            <Card style="width: 1230px;">
              <div style="text-align:center">
                <img width="150" height="150" src="../../../static/img/firstMain/xuancaie.jpg" alt="author">
                <Divider/>
                <h5><Strong>作者---<cite>汪海潮</cite></Strong></h5>
                <br>
                <Alert>
                  这是一个学习的博客，会随着时间的推移，不断完善这个博客.
                  <template slot="desc">
                    <p style="color: #2c3e50">
                      本博客采用前后端分离(前端:Vue+element-UI+IView-ui，后端:SpringCloud+Mysql+Redis+RabbitMQ+ES)，开源的代码都放在我的GitHub，也可以关注我的GitHub上面的
                      <a href="https://github.com/Study-Up-Up/study/wiki" target="_blank">Wiki</a>
                    </p>
                    <Divider style="color: #e8eaec" />
                    <p align="center">
                      <a class="btn btn-info btn-large" target="_blank" href="https://github.com/Study-Up-Up/study">Learn more on my GitHub</a>
                    </p>
                  </template>
                </Alert>
              </div>
            </Card>
          </div>
          &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
        </div>
        <div class="col-md-12 column" id="vueCloud">
          <div class="col-md-12 column">
            <Card style="width: 1230px;">
              <div style="text-align:center">
                <div class="tabbable" >
                  <h3 class="text-center text-success">
                    下面是前端和后端的解析视频
                  </h3>
                  <ul class="nav nav-tabs">
                    <li>
                      <a href="#panel-790408" data-toggle="tab">前端</a>
                    </li>
                    <li class="active">
                      <a href="#panel-509578" data-toggle="tab">后端</a>
                    </li>
                  </ul>
                  <div class="tab-content">
                    <div class="tab-pane" id="panel-790408">
                      <br>
                      <p  style="color: cornflowerblue" >
                        前端采用IDE的是WebStorm，Jquery@3.4.1(popper@1.12.5)+Vue@2.5.2+bootStrap@3.3.7+element-UI@2.12.0+axios@0.19.0          </p>
                      <hr>
                    </div>
                    <div class="tab-pane active" id="panel-509578">
                      <br>
                      <p style="color: cornflowerblue">
                        后端采用的IDE是IDEA，SpringCloud+MySQL+Redis+ActiveMQ+ES
                      </p>
                      <hr>
                    </div>
                  </div>
                </div>
              </div>
              <div class="media">
                <a class="pull-left" href="#"><img class="media-object" src="http://cdn.ibootstrap.cn/lorempixel.com/64/64/default.jpg" alt='' /></a>
                <div class="media-body">
                  <h4 class="media-heading" style="color: darkgreen">
                    前端环境安装和集成(左边第一个)
                  </h4> 这个视频主要讲解搭建分离后的前端环境，可以少了不少的坑，对于页面的布局和单点处理以及使用Vue组件等，还要处理前后端交互，对于前后端分离感兴趣的或者初学者都可以观看。
                  <div class="media">
                    <a class="pull-left" href="#"><img class="media-object" src="http://cdn.ibootstrap.cn/lorempixel.com/64/64/default.jpg" alt='' /></a>
                    <div class="media-body">
                      <h4 class="media-heading" style="color: darkgreen">
                        后端的主要开发讲解(左边第二个)
                      </h4>后端主要是用SpringCloud后端框架，重要的是理解微服务和HTTP通信，这是SpringCloud的重点,在学习阶段暂时不用考虑那么复杂，尽可能地实现功能，不一定要求高可用和非常细化地熔断。
                    </div>
                  </div>
                </div>
              </div>
            </Card>
          </div>
          &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
        </div>
        <div class="col-md-12 column"  id="end">
            <p class="text-info text-center">
              <a class="alert-link" href="http://www.beian.miit.gov.cn/">豫ICP备19033675号</a>
            </p>
          </div>
      </div>
      <div class="col-md-1 column">
          <Anchor show-ink style="font-size: medium;">
            <AnchorLink
            >
              <AnchorLink href="#basic" title="C" />
              <AnchorLink href="#basic" title="C++" />
              <AnchorLink href="#basic" title="Java" />
            </AnchorLink>
            <AnchorLink
            >
              <AnchorLink href="#new" title=" PHP" />
              <AnchorLink href="#new" title=" Python" />
              <AnchorLink href="#new" title=" Go" />
            </AnchorLink>
            <AnchorLink>
              <AnchorLink href="#articleRank" title=" 排行榜" />
            </AnchorLink>
            <AnchorLink
              title="关于作者"
              href="#myself"
            ></AnchorLink>
            <AnchorLink
              title="关于项目"
              href="#vueCloud"
            ></AnchorLink>
            <AnchorLink
              title="结尾"
              href="#end"
            ></AnchorLink>
          </Anchor>
      </div>
    <BackTop :height="50" :bottom="50">
      <div class="top"><Icon type="ios-arrow-dropup-circle" /></div>
    </BackTop>
  </div>
</template>
<style>

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .top{
    padding: 1px;
    background: black;
    color: #fff;
    text-align: center;
    border-radius: 2px;
  }

  .searchClass{
    border: 1px solid #c5c5c5;
    border-radius: 20px;
    background: #fff;
  }

  .searchClass .el-input-group__prepend {
    border: none;
    background-color: transparent;
    padding: 0 10px 0 30px;
  }

  .searchClass .el-input-group__append {
    border: none;
    background-color: transparent;
  }

  .searchClass .el-input__inner {
    height: 36px;
    line-height: 36px;
    border: none;
    background-color: transparent;
  }

  .searchClass .el-icon-search{
    font-size: 16px;
  }

  .searchClass .centerClass {
    height: 100%;
    line-height: 100%;
    display: inline-block;
    vertical-align: middle;
    text-align: right;
  }

  .searchClass .line {
    width: 1px;
    height: 26px;
    background-color: #ffffff;
    margin-left: 14px;
  }

  .searchClass:hover{
    border: 1px solid #c5c5c5;
    background: #fff;
  }

  .searchClass:hover .line {
    background-color: #D5E3E8;
  }

  .searchClass:hover .el-icon-search{
    color: #409eff;
    font-size: 16px;
  }

  .image {
    width: 50%;
    display: block;
  }

  video {
    position: fixed;
    right: 0;
    min-width: 100%;
    min-height: 100%;
    width: auto;
    height: auto;
    z-index: -999;
  }

  source{
    min-width: 100%;
    min-height: 100%;
    height: auto;
    width: auto;
  }

</style>
<script>
    export default {
       inject: ['reload'],
       data(){
           return {
               blogRealUrl: 'http://localhost:8088/home/details/',
               fits: ['fill', 'contain', 'cover', 'none', 'scale-down'],
               carouselDefault: 0,
               esInput: '',
               categoryName: '',
               welcomeTitle: window.localStorage.getItem('blogName'),
               HomePageShow: window.localStorage.getItem('userPicture'),
               activeIndex: '1',
               formLabelWidth: '100px',
               login: window.localStorage.getItem('token'),
               rankColumns: [
                   {
                       type: 'index',
                       title: '排名',
                       width: 60,
                       align: 'center'
                   },
                   {
                       title: '发表人',
                       key: 'name',
                       slot: 'name',
                       width: 200,
                       align: 'center',
                   },
                   {
                       title: '文章的标题',
                       key: 'title',
                       width: 430,
                       align: 'center',
                   },
                   {
                       title: '发表的时间',
                       key: 'createTime',
                       width: 200,
                       align: 'center',
                   },
                   {
                       title: '用户的点赞量',
                       key: 'total',
                       width: 150,
                       align: 'center',

                   },
                   {
                       title: '操作',
                       slot: 'action',
                       width: 150,
                       align: 'center'
                   }
               ],
               rankData: [
                   {
                       id: '',
                       name: '',
                       title: '',
                       createTime: '',
                       total: '',
                   }
               ]
           }
       },
        created(){
           this.articleRank();
        },
       methods: {
           loginTo(){
             this.$router.push({name: 'Login'});
             this.reload();
           },
           loginOutShow() {
               this.login=null;
               window.localStorage.clear();
               this.reload();
           },
           musicSound(){
               document.getElementById("video").volume = 0;
           },
           handleSelect(key, keyPath) {
               console.log(key, keyPath);
           },
           handleSelectAll(item) {
               console.log(item);
           },
           //主页的搜索
           homePageSearch() {
               //输入内容的长度
               let patterInput=/^.{1,20}$/;
               if (this.categoryName === '' || this.esInput === '') {
                   alert('分类和内容均不能为空!');
               }else if (!patterInput.test(this.esInput)){
                   alert('分类的内容需在1-20个字体之内!');
               }
               else {
                   let routeUrl = this.$router.resolve({
                       name: "ArticleSearch",
                       query: {
                           categoryName: this.categoryName,
                           keyWord: this.esInput,
                       }
                   });
                   window.open(routeUrl.href, '_blank');
               }
           },
           //排行榜的加载
           articleRank(){
               this.$http.post('/whc/blog-customer-user/thumpRankFeign')
                   .then(res =>{
                     console.log(res);
                     this.rankData=res.data.list;
                   })
           }
       },
       mounted() {
           this.musicSound();
       }
   }
</script>
