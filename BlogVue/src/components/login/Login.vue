<template>
  <div class="container">
    <div class="row clearfix">
      <div class="col-md-12 column">
        <div>
          <el-avatar src="../../../static/img/login/tian.gif"></el-avatar>
        </div>
        <br>
        <h2 style="font-style: italic" class="text-center">基于Vue+SpringCloud博客的设计和实现&emsp;<small>作者-汪海潮</small></h2>
        <br>
        <br>
        <div class="login-wrap">
          <router-link :to="{name: 'Register'}" style="text-decoration: none;color: #2c3e50" target="_blank">没有账号？马上注册</router-link>
          <br>
          <br>
          <input type="text" placeholder="请输入您的用户名" v-model="username">
          <input type="password" placeholder="请输入您的密码" v-model="password">
          <br>
          <br>
          <div id="sc" style="position:absolute; top:118%; left:57.8%; width:250px; height:40px;margin-left:-240px; margin-top:-120px;">
            <remote-js src="//g.alicdn.com/sd/nvc/1.1.112/guide.js" @loaded="initCaptcha"></remote-js>
          </div>
          <div style="position:absolute; top:128%; left:58%; width:250px; height:40px;margin-left:-240px; margin-top:-120px;">
            <br>
            <button @click="login">登录</button>
          </div>
        </div>
        <br>
      </div>
    </div>
  </div>
</template>

<style>
  .nc_scale {
    background: #e8e8e8 !important;
  }
  .nc_scale div.bc_bg {
    background: #7ac23c !important;
  }
  .nc_scale .scale_text2 {
    color: #fff !important;
  }
  .nc_scale span {
    border: 1px solid #ccc !important;
  }
  .errloading {
    border: #faf1d5 1px solid !important;
    color: #ef9f06 !important;
  }
  .login-wrap {
    text-align: center;
  }
  input {
    display: block;
    width: 300px;
    height: 42px;
    line-height: 40px;
    margin: 0 auto;
    margin-bottom: 10px;
    outline: none;
    border: 1px solid #888;
    padding: 10px;
    box-sizing: border-box;
  }
  p {
    color: red;
  }
  button {
    display: block;
    width: 300px;
    height: 42px;
    line-height: 40px;
    margin: 0 auto;
    border: none;
    background-color: #41b883;
    color: #fff;
    font-size: 16px;
    margin-bottom: 5px;
  }
  span {
    cursor: pointer;
  }
  span:hover {
    color: #41b883;
  }

  body {background: url('../../../static/img/login/BJT.jpg');}

</style>
<script>
    export default {
        inject: ['reload'],
        data() {
            return {
                //登录传给前端的四个参数
                scene: '',
                aliToken: '',
                sessionId: '',
                sig: '',
                username: '',
                password: '',
            }
        },
        components: {
            "remote-js": {
                render(createElement) {
                    const self = this;
                    return createElement("script", {
                        attrs: { type: "text/javascript", src: this.src },
                        on: {
                            load() {
                                self.$emit("loaded");
                            }
                        }
                    });
                },
                props: {
                    src: { type: String, required: true }
                }
            },
        },
        created() {
            window.NVC_Opt = {
                appkey: "个人隐私",
                scene: "",
                trans: {"key1": "code0", "nvcCode":200},
                elements: [
                    "//img.alicdn.com/tfs/TB17cwllsLJ8KJjy0FnXXcFDpXa-50-74.png",
                    "//img.alicdn.com/tfs/TB17cwllsLJ8KJjy0FnXXcFDpXa-50-74.png"
                ],
                bg_back_prepared: "//img.alicdn.com/tps/TB1skE5SFXXXXb3XXXXXXXXXXXX-100-80.png",
                bg_front: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABQCAMAAADY1yDdAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAADUExURefk5w+ruswAAAAfSURBVFjD7cExAQAAAMKg9U9tCU+gAAAAAAAAAIC3AR+QAAFPlUGoAAAAAElFTkSuQmCC",
                obj_ok: "//img.alicdn.com/tfs/TB1rmyTltfJ8KJjy0FeXXXKEXXa-50-74.png",
                bg_back_pass: "//img.alicdn.com/tfs/TB1KDxCSVXXXXasXFXXXXXXXXXX-100-80.png",
                obj_error: "//img.alicdn.com/tfs/TB1q9yTltfJ8KJjy0FeXXXKEXXa-50-74.png",
                bg_back_fail: "//img.alicdn.com/tfs/TB1w2oOSFXXXXb4XpXXXXXXXXXX-100-80.png",
                upLang:{"cn":{
                        _ggk_guide: "请摁住鼠标左键，刮出两面盾牌",
                        _ggk_success: "恭喜您成功刮出盾牌<br/>继续下一步操作吧",
                        _ggk_loading: "加载中",
                        _ggk_fail: ["呀，盾牌不见了<br/>请","javascript:noCaptcha.reset()","再来一次","或","https://survey.taobao.com/survey/QgzQDdDd?token=%TOKEN","反馈问题"],
                        _ggk_action_timeout: ["我等得太久啦<br/>请","javascript:noCaptcha.reset()","再来一次","或","https://survey.taobao.com/survey/QgzQDdDd?token=%TOKEN","反馈问题"],
                        _ggk_net_err: ["网络实在不给力<br/>请","javascript:noCaptcha.reset()","再来一次","或","https://survey.taobao.com/survey/QgzQDdDd?token=%TOKEN","反馈问题"],
                        _ggk_too_fast: ["您刮得太快啦<br/>请","javascript:noCaptcha.reset()","再来一次","或","https://survey.taobao.com/survey/QgzQDdDd?token=%TOKEN","反馈问题"]
                    }
                }
            };
        },
        methods: {
            //点击智能验证
            initCaptcha() {
                let _this=this;
                let ic = new smartCaptcha({
                    renderTo: '#sc',
                    width: 300,
                    height: 42,
                    default_txt: "点击按钮开始智能验证",
                    success_txt: "验证成功，请点击登录",
                    fail_txt: "验证失败，请在此点击按钮刷新",
                    scaning_txt: "智能检测中",
                    success: function (data) {
                        console.log(NVC_Opt.token);
                        console.log(data.sessionId);
                        console.log(data.sig);
                        _this.aliToken=NVC_Opt.token;
                        _this.sessionId=data.sessionId;
                        _this.sig=data.sig;
                    },
                });
                ic.init();
            },
            //登录账号
            login() {
                if (this.username === '' || this.password === '' ) {
                    this.$message({
                        type: "error",
                        message: '请输入账号和密码!',
                    })
                } else if (this.aliToken === '' || this.sessionId === '' || this.sig === ''){
                    this.$message({
                        type: "error",
                        message: '请点进验证!',
                    })
                }else {
                    this.$http.post('/whc/blog-customer-user/loginToken', {
                        username: this.username,
                        password: this.password,
                        scene: this.scene,
                        aliToken: this.aliToken,
                        sessionId: this.sessionId,
                        sig: this.sig,
                    }).then(res => {
                        console.log(res);
                        if (res.data.data.status === 200) {
                            console.log(res);
                            //保存Id,只用来模拟连接websocket
                            window.localStorage.setItem('id',res.data.data.id);
                            //保存Token
                            window.localStorage.setItem('token',res.data.data.token);
                            //保存blogName
                            window.localStorage.setItem('blogName',res.data.data.blogName);
                            //保存用户头像
                            window.localStorage.setItem('userPicture',res.data.data.userPicture);
                            //保存加密的手机号
                            window.localStorage.setItem('myPhone',res.data.data.myPhone);
                            //保存会员标志,只要是会员就行，只返回可以看的标志，结果与后端的数据库中的数据为准。
                            window.localStorage.setItem('memberSign',res.data.data.memberSign);
                            this.$message({
                                type: "success",
                                message: JSON.stringify(res.data.message),
                            }).then(
                                this.$router.replace({name: 'Article'}).then(this.$router.go(0))
                            )
                        } else if (res.data.data===false){
                            this.$message.error('智能验证失败');
                            this.reload();
                        } else{
                            this.$message.error('账号与密码不匹配');
                            this.reload();
                        }
                    })
                }
            }
        },
    }
</script>

