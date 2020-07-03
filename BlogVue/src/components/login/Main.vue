<template>
  <div class="container">
    <div class="row clearfix">
      <div class="col-md-12 column">
        <div class="page-header">
          <h1 style="color: orchid" v-model="welcomeTitle">
            欢迎&nbsp;{{ welcomeTitle }}&nbsp;来到汪海潮的博客 <small style="color: silver">Study for Blog</small>
          </h1>
        </div>
        <ul class="nav nav-tabs">
          <li class="active">
            <a href="#" style="color: blueviolet">主页</a>
          </li>
          <li class="dropdown pull-right">
            <a class="dropdown-toggle" href="#" data-toggle="dropdown" style="color: red">博客管理中心<strong class="caret"></strong></a>
            <ul class="dropdown-menu">
              <li>
                <router-link :to="{name: 'UserPublish'}" target="_blank">发表博客</router-link>
              </li>
              <li>
                <router-link :to="{name: 'UserManage'}" target="_blank">博客管理</router-link>
              </li>
              <li>
                <router-link :to="{name: 'UserCollection'}" target="_blank">收藏订阅</router-link>
              </li>
              <li class="divider">
              </li>
              <li>
                <router-link :to="{name: 'Recommend'}" target="_blank">热门博客排行榜</router-link>
              </li>
            </ul>
          </li>
        </ul>
        <br>
        <nav class="navbar navbar-default navbar-inverse navbar-fixed-top" role="navigation">
          <div class="navbar-header">
            <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button> <a class="navbar-brand" href="#">首页</a>
          </div>

          <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
              <li>
                <router-link :to="{name: 'Recommend'}" target="_blank">浏览博客</router-link>
              </li>
              <li>
                <a href="#">留言反馈</a>
              </li>
              <li class="dropdown">
                <a class="dropdown-toggle" href="#" data-toggle="dropdown">安全中心<strong class="caret"></strong></a>
                <ul class="dropdown-menu">
                  <li>
                    <router-link :to="{name: 'IdCard'}" style="color: black" target="_blank">实名认证</router-link>
                  </li>
                  <li>
                    <router-link :to="{name: 'SchoolCard'}" style="color: black" target="_blank">校园认证</router-link>
                  </li>
                  <li>
                    <router-link :to="{name: 'Change'}" style="color: black" target="_blank">修改密码</router-link>
                  </li>
                  <li>
                    <router-link to="/member" target="_blank" style="color: black">高贵的VIP会员</router-link>
                  </li>
                </ul>
              </li>
            </ul>
            <form class="navbar-form navbar-left" role="search" v-model="search">
              <div class="form-group">
                <label>
                  <input class="form-control" type="text" />
                </label>
               <button class="btn btn-default" type="submit" style="color: black ">搜索文章</button>
              </div>
            </form>
            <ul class="nav navbar-nav navbar-right">
              <li>
                <a href="#">消息中心</a>
              </li>
              <li class="dropdown">
                <a class="dropdown-toggle" href="#" data-toggle="dropdown"><el-image :src="rightShow" style="width: 20px;height: 20px"></el-image><strong class="caret"></strong></a>
                <ul class="dropdown-menu">
                  <li>
                    <router-link :to="{ name: 'Home'}">个人中心</router-link>
                  </li>
                  <li>
                    <router-link to="/sign" target="_blank">签到中心</router-link>
                  </li>
                  <li>
                    <router-link to="/wallet" target="_blank">我的钱包</router-link>
                  </li>
                  <li class="divider">
                  </li>
                  <li>
                      <el-link :underline="false" style="color: black" v-if="login===null" v-on:click="loginShow"><i class="el-icon-lock"></i> 登录</el-link>
                      <el-link :underline="false" style="color: black" v-else v-on:click="loginOutShow">注销登录</el-link>
                  </li>
                </ul>
              </li>
            </ul>
          </div>
        </nav>
        <div class="carousel slide" id="carousel-920250">
          <ol class="carousel-indicators">
            <li data-target="#carousel-920250" data-slide-to="0">
            </li>
            <li class="active" data-target="#carousel-920250" data-slide-to="1">
            </li>
            <li data-target="#carousel-920250" data-slide-to="2">
            </li>
          </ol>
          <div class="carousel-inner">
            <div class="item">
              <img alt="" src="http://cdn.ibootstrap.cn/lorempixel.com/1600/500/sports/1/default.jpg" />
              <div class="carousel-caption">
                <h4>
                  第一张图片
                </h4>
                <p>
                  Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit.
                </p>
              </div>
            </div>
            <div class="item active">
              <img alt="" src="http://cdn.ibootstrap.cn/lorempixel.com/1600/500/sports/2/default.jpg" />
              <div class="carousel-caption">
                <h4>
                  第二张图片
                </h4>
                <p>
                  Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit.
                </p>
              </div>
            </div>
            <div class="item">
              <img alt="" src="http://cdn.ibootstrap.cn/lorempixel.com/1600/500/sports/3/default.jpg" />
              <div class="carousel-caption">
                <h4>
                  第三张图片
                </h4>
                <p>
                  Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit.
                </p>
              </div>
            </div>
          </div> <a class="left carousel-control" href="#carousel-920250" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a> <a class="right carousel-control" href="#carousel-920250" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
        </div>
      </div>
    </div>
    <hr>
    <div class="row clearfix">
      <div class="col-md-4 column">
        <span class="label label-danger">TOP1</span>
        <h2>
          C
        </h2>
        <p>
          C is the most important language
        </p>
        <p>
          <a class="btn" href="#">View C »</a>
        </p>
      </div>
      <div class="col-md-4 column">
        <span class="label label-danger">TOP2</span>
        <h2>
          C++
        </h2>
        <p>
          C + + is an upgraded version of C
        </p>
        <p>
          <a class="btn" href="#">View C++ »</a>
        </p>
      </div>
      <div class="col-md-4 column">
        <span class="label label-info">TOP3</span>
        <h2>
          Java
        </h2>
        <p>
          Java is the best language in the world
        </p>
        <p>
          <a class="btn" href="#">View Java »</a>
        </p>
      </div>
    </div>
    <hr>
    <div class="row clearfix">
      <div class="col-md-4 column">
        <span class="label label-info">TOP4</span>
        <h2>
          PHP
        </h2>
        <p>
          PHP is an open source scripting language
        </p>
        <p>
          <a class="btn" href="#">View PHP »</a>
        </p>
      </div>
      <div class="col-md-4 column">
        <span class="label label-primary">TOP5</span>
        <h2>
          python
        </h2>
        <p>
          Python is a language for artificial intelligence
        </p>
        <p>
          <a class="btn" href="#">View python »</a>
        </p>
      </div>
      <div class="col-md-4 column">
        <span class="label label-primary">TOP6</span>
        <h2>
          GO
        </h2>
        <p>
          Go is a language like C
        </p>
        <p>
          <a class="btn" href="#">View GO »</a>
        </p>
      </div>
    </div>

    <hr>
    <div class="row clearfix">
      <div class="col-md-12 column">
        <hr>
        <img class="img-circle" alt="200×200" width="150" height="150" src="../../../static/img/firstMain/xuancaie.jpg" />
        <div class="row clearfix">
          <div class="col-md-12 column">
            <blockquote>
              <p>
                咸鱼
              </p>  <small>作者 <cite>汪海潮</cite></small>
            </blockquote>
          </div>
        </div>
        <hr>
        <div class="jumbotron well" style="color: orchid">
          <h3>
            这是一个学习的博客，会随着时间的推移，不断完善这个博客.
          </h3>
          <p style="color: #2c3e50">
            本博客采用前后端分离(前端:Vue+BootStrap+Jquery+element-UI，后端:SpringCloud+Mysql+Redis+ActiveMQ+ES)，开源的代码都放在我的GitHub，也可以关注我的GitHub上面的
            <a href="https://github.com/Study-Up-Up/study/wiki">Wiki</a>
          </p>
          <p>
            <a class="btn btn-info btn-large" href="https://github.com/Study-Up-Up/study">Learn more on my GitHub</a>
          </p>
        </div>
      </div>
    </div>
    <hr>
    <br>
    <div class="tabbable" id="tabs-982462">
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
    <hr>
    <hr>
    <div class="row clearfix">
      <div class="col-md-12 column">
        <p class="text-info text-center">
          <a class="alert-link" href="http://www.beian.miit.gov.cn/">豫ICP备19033675号</a>
        </p>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
      data() {
          return{
              login: window.localStorage.getItem('token'),
              welcomeTitle: window.localStorage.getItem('blogName'),
              rightShow: window.localStorage.getItem('userPicture'),
          }
      },
      methods: {
          loginShow() {
              this.$router.push('/login');
          },
          loginOutShow() {
              this.login=null;
              window.localStorage.clear();
              //this.$router.go(0);
          }
      }
  }
</script>
