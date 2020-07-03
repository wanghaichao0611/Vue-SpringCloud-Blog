<template>
  <div class="container">
    <div class="row clearfix">
      <div class="col-md-12 column">
        <div>
          <el-avatar src="../../../static/img/login/tian.gif"></el-avatar>
        </div>
        <br>
        <div class="login-wrap">
          <h2 style="font-style: italic" class="text-center">基于Vue+SpringCloud博客的设计和实现&emsp;<small>作者-汪海潮</small></h2>
          <br>
          <br>
          <router-link :to="{name: 'Login'}" style="text-decoration: none;color: #2c3e50" target="_blank">已有账号？马上登录</router-link>
          <br>
          <br>
          <input type="text" placeholder="小写字母开头且满足8-16位" maxlength="16" show-word-limit v-model="newUsername">
          <input type="password" placeholder="请输入密码,密码的长度为8-16位" maxlength="16" show-word-limit v-model="newPassword">
          <input type="password" placeholder="请再次输入相同的密码" maxlength="16" show-word-limit v-model="newPassword2">
          <br>
          <div id="sc2" style="position:absolute; top:103%; left:57.8%; width:250px; height:40px;margin-left:-240px; margin-top:-120px;">
            <remote-js src="//g.alicdn.com/sd/nvc/1.1.112/guide.js" @loaded="initCaptchaRegister"></remote-js>
          </div>
          <br>
          <br>
          <button v-on:click="register">注册</button>
        </div>
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
      data(){
          return{
              scene: '',
              newUsername: '',
              newPassword: '',
              newPassword2: '',
              aliToken: '',
              sessionId: '',
              sig: '',
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

      methods:{
          //初始化智能验证组件
          initCaptchaRegister(){
              let _thisR=this;
              let ic2 = new smartCaptcha({
                  renderTo: '#sc2',
                  width: 300,
                  height: 42,
                  default_txt: "点击按钮开始智能验证",
                  success_txt: "验证成功，请点击注册",
                  fail_txt: "验证失败，请在此点击按钮刷新",
                  scaning_txt: "智能检测中",
                  success: function (data) {
                      console.log(NVC_Opt.token);
                      console.log(data.sessionId);
                      console.log(data.sig);
                      _thisR.aliToken=NVC_Opt.token;
                      _thisR.sessionId=data.sessionId;
                      _thisR.sig=data.sig;
                  },
              });
              ic2.init();
          },
          //注册账号
          register() {
              //账号名字的规则
              let patterName=/^[a-z][a-zA-Z0-9\-]{6,15}$/;

              //密码的长度
              let patterPassword=/^.{7,15}$/;

              if (this.newUsername === '' || this.newPassword === '' || this.newPassword2 === '' ||
                  this.newPassword !== this.newPassword2 || !patterPassword.test(this.newPassword) ||
                  this.value === false || !patterName.test(this.newUsername)
                  || this.aliToken === '' || this.sessionId === '' || this.sig === '') {
                  if (this.newUsername === '' || this.newPassword === '' || this.newPassword2 === '') {
                      this.$message({
                          type: "error",
                          message: '账号和密码不能为空',
                      })
                  } else if (this.newPassword !== this.newPassword2) {
                      this.$message({
                          type: "error",
                          message: '两个密码不相同',
                      })
                  } else if (!patterName.test(this.newUsername)) {
                      this.$message({
                          type: "error",
                          message: '用户名不符合',
                      })
                  }else if (!patterName.test(this.newPassword)){
                      this.$message({
                          type: "error",
                          message: '密码不符合长度要求',
                      })
                  }else if (this.aliToken === '' || this.sessionId === '' || this.sig === '') {
                      this.$message({
                          type: "error",
                          message: '请点进验证!',
                      })
                  } else {
                      this.$message({
                          type: "error",
                          message: '请选择绿色',
                      })
                  }
              } else {
                  this.$http.post('/whc/blog-customer-user/register',{
                      newUsername: this.newUsername,
                      newPassword: this.newPassword,
                      scene: this.scene,
                      aliToken: this.aliToken,
                      sessionId: this.sessionId,
                      sig: this.sig,
                  }).then(res=>{
                      console.log(res);
                      //let registerLogin=JSON.stringify(res.data.success);
                      if (res.data.data.status === 200) {
                          //保存Id,只用来模拟连接websocket
                          window.localStorage.setItem('id',res.data.data.id);
                          //保存注册的Token
                          window.localStorage.setItem('token',res.data.data.token);
                          this.$message({
                              type: "success",
                              message: '注册成功',
                          }.then(
                              this.$router.replace({name: 'Mation',
                                  query:{
                                      newUsername: this.newUsername,
                                  }
                              }), this.$router.go(0)))
                      } else if (res.data.data===false) {
                          this.$message.error('智能验证失败');
                          this.reload();
                      } else {
                          this.$message.error(res.data.message);
                      }
                  })
              }
          }
      },
  }
</script>
