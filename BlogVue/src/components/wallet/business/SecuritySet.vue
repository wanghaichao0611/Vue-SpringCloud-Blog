<template>
  <div class="container">
    <div class="row clearfix">
      <div class="col-md-6 column">
        <h2>旧密码修改支付密码</h2>
        <el-divider></el-divider>
        <el-form>
          <el-form-item label="旧支付密码">
            <el-input
              type="password"
              placeholder="请输入旧的支付密码"
              v-model="oldPay"
              maxlength="6"
              show-word-limit
            >
            </el-input>
          </el-form-item>
            <el-form-item label="新支付密码">
              <el-input
                type="password"
                placeholder="请输入新的支付密码"
                v-model="newPay"
                maxlength="6"
                show-word-limit
              >
              </el-input>
            </el-form-item>
              <el-form-item label="确认新支付密码">
                <el-input
                  type="password"
                  placeholder="请确认新的支付密码"
                  v-model="newPayAgain"
                  maxlength="6"
                  show-word-limit
                >
                </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="danger" icon="el-icon-edit" circle v-on:click="modifyPassword"></el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="col-md-6 column">
        <h2>手机重置支付密码</h2>
        <el-divider></el-divider>
        <el-form v-if="myPhone!=='null'">
            <el-form-item>
              <span slot="label">验证手机号才能重置支付密码:&nbsp;&nbsp;&nbsp;<Strong style="color: blue">{{ myPhone }}</Strong></span>
              <el-button type="primary" :disabled="disable" @click="resetPay">{{getCode}}<i class="el-icon-message"></i></el-button>
            </el-form-item>
            <el-form-item label="验证码">
              <el-input
                type="text"
                placeholder="请输入新的支付密码"
                v-model="payCode"
                maxlength="6"
                show-word-limit
              >
              </el-input>
            </el-form-item>
            <el-form-item label="新支付密码">
              <el-input
                type="password"
                placeholder="请确认新的支付密码"
                v-model="resetPassword"
                maxlength="6"
                show-word-limit
              >
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="danger" icon="el-icon-edit" circle v-on:click="resetPayCode"></el-button>
            </el-form-item>
        </el-form>
        <el-form v-if="myPhone===null">
          <h1>请绑定手机号!</h1>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
      inject: ['reload'],
      data(){
          return{
              oldPay: '',
              newPay: '',
              newPayAgain: '',
              myPhone: window.localStorage.getItem('myPhone'),
              payCode: '',
              resetPassword: '',
              getCode: '获取验证码',
              isGeting: false,
              count: 60,
              disable: false,
          }
      },
      methods: {
          //旧支付密码修改新支付密码
          modifyPassword(){
              let payPattern=/^[0-9]{6}$/;
              if (this.oldPay === '' || this.newPay === '' || this.newPayAgain === '' ||
                  this.newPay !== this.newPayAgain || !payPattern.test(this.newPay)){
                  this.$message({
                      type: "error",
                      message: '绑定规则不符合',
                  });
              }else {
                  this.$http.post('/whc/blog-customer-user/modifyPayFeign', {
                      oldPay: this.oldPay,
                      newPay: this.newPay,
              }).then(res =>{
                  console.log(res);
                  if (res.data.success===true){
                      this.$notify({
                          title: '成功',
                          message: res.data.message,
                          type: 'success',
                      });
                      this.reload();
                  }else {
                      this.$message({
                          type: "error",
                          message: res.data.message,
                      });
                  }
                  })
              }
          },
          //验证重置支付密码的手机号(共用的服务)
          resetPay(){
              var countDown = setInterval(() => {
                  if (this.count < 1) {
                      this.isGeting = false;
                      this.disable = false;
                      this.getCode = '获取验证码';
                      this.count = 60;
                      clearInterval(countDown);
                  } else {
                      this.isGeting = true;
                      this.disable = true;
                      this.getCode = this.count-- + 's后重发';
                  }
              }, 1000);
              this.$http.post('/whc/blog-customer-user/eachServerCodeFeign')
                  .then(res => {
                      if (res.data.success === true) {
                          this.$message({
                              type: "success",
                              message: '发送成功',
                          })
                      }else {
                          this.$message({
                              type: "error",
                              message: '服务器开了小差,稍微再试!',
                          })
                      }
                  })
          },
          //手机验证码重置支付密码
          resetPayCode(){
              let resetPattern=/^[0-9]{6}$/;
              if (this.payCode === '' || this.resetPassword === '' || !resetPattern.test(this.resetPassword)){
                  this.$message({
                      type: "error",
                      message: '输入规则不正确',
                  });
              }else {
                  this.$http.post('/whc/blog-customer-user/resetPayFeign',{
                      payCode: this.payCode,
                      resetPassword: this.resetPassword,
                  }).then(res =>{
                      if (res.data.success === true) {
                          this.$notify({
                              title: '成功',
                              message: res.data.message,
                              type: 'success',
                          });
                          this.reload();
                      }else {
                          this.$message({
                              type: "error",
                              message: res.data.message,
                          })
                      }
                  })
              }
          }
      }
  }
</script>
