<template>
  <div class="container">
    <div class="row clearfix">
      <div class="col-md-12 column">
        <h1>请您完成手机与邮箱注册来保护你的账号安全</h1>
        <br>
        <br>
        <!-- 手机验证的Form -->
        <el-button type="text" @click="PYZ = true" v-on:click="getPhone"><h2>手机绑定</h2></el-button>

        <el-dialog title="手机绑定" :visible.sync="PYZ" v-if="phoneShow===0">

          <el-form>
            <el-form-item label="手机" :label-width="formLabelWidth">
              <el-input type="text" v-model="phone" autocomplete="off" placeholder="请输入你的手机号" maxlength="20" show-word-limit></el-input>
            </el-form-item>
            <el-form-item label="验证码" :label-width="formLabelWidth">
              <el-input type="text" v-model="phoneYzm" autocomplete="off" placeholder="请输入你的手机验证码" maxlength="6" show-word-limit></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="disable" @click="getPhoneCode">{{getCode}}<i class="el-icon-message"></i></el-button>
            </el-form-item>
          </el-form>

          <div slot="footer" class="dialog-footer">
            <el-button @click="PYZ = false">取 消</el-button>
            <el-button type="primary" @click="PYZ = false" v-on:click="phoneRegister">绑 定</el-button>
          </div>
        </el-dialog>
        <el-dialog :visible.sync="PYZ" v-else title="绑定成功">
          <el-form>
            <span>恭喜你你已经完成手机注册<i class="el-icon-phone"></i></span>
            <br>
            <el-divider/>
            <span>你的手机是: {{ myPhone }}</span>
          </el-form>
        </el-dialog>
        <br>
        <br>
        <br>
        <!-- 邮箱验证的Form -->
        <el-button type="text" @click="EYZ = true" v-on:click="getEmail"><h1>邮箱绑定</h1></el-button>

        <el-dialog title="邮箱绑定" :visible.sync="EYZ"  v-if="emailShow===0">
          <el-form>
            <el-form-item label="邮箱" :label-width="formLabelWidth">
              <el-input type="text" v-model="email" autocomplete="off" placeholder="请输入你的邮箱"></el-input>
            </el-form-item>
            <el-form-item label="验证码" :label-width="formLabelWidth">
              <el-input type="text" v-model="emailYzm" autocomplete="off" placeholder="请输入你的邮箱验证码" maxlength="6" show-word-limit></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="disable" @click="getEmailCode">{{getCode}}<i class="el-icon-message"></i></el-button>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="EYZ = false">取 消</el-button>
            <el-button type="primary" @click="EYZ = false" v-on:click="registerEmail">绑 定</el-button>
          </div>
        </el-dialog>
        <el-dialog  :visible.sync="EYZ" v-else title="绑定成功">
          <el-form>
            <span>恭喜你你已经完成邮箱注册<i class="el-icon-message"></i></span>
            <br>
            <el-divider/>
            <span>你的邮箱是: {{ myEmail }}</span>
          </el-form>
        </el-dialog>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <el-button v-on:click="toMain()" >我已完成绑定</el-button>
      </div>
    </div>
  </div>
</template>
<style>
  body {background: url('../../../static/img/login/BJT.jpg');}
</style>
<script>
  export default {
      inject: ['reload'],
      data(){
          return{
              //手机验证的form
              phone: '',
              phoneYzm: '',
              phoneShow: 0,
              myPhone: window.localStorage.getItem('myPhone'),
              PYZ: false,
              formLabelWidth: '100px',
              getCode: '获取验证码',
              isGeting: false,
              count: 60,
              disable: false,

              //邮箱验证的form和按钮
              email: '',
              emailYzm: '',
              emailShow: 0,
              myEmail: window.localStorage.getItem('myEmail'),
              EYZ: false,
          }
      },
      methods: {
          toMain(){
              if (this.myPhone === null || this.myEmail === null){
                  this.$message.error('手机与邮箱都必须完成注册!');
              }else {
                  window.localStorage.clear();
                  this.$router.go(0);
              }
          },
          //获得账号是否已经邮箱注册
          getEmail(){
              this.$http.post('/whc/blog-customer-user/getEmailButton')
                  .then(res =>{
                      if (res.data.success === true){
                          console.log(res);
                          this.emailShow=1;
                          this.myEmail=res.data.message;
                      }
                      else {
                          console.log(res);
                          this.emailShow=0;
                          this.$message.info('您还没完成邮箱注册,请绑定邮箱');
                      }
                  })
          },
          //绑定邮箱的按钮发送
          getEmailCode() {
              let emailPatterBU=/[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/;
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
              if (this.email==='' || !emailPatterBU.test(this.email)){
                  this.$message.error('邮箱不符合格式');
              }else {
                  this.$http.post('/whc/blog-customer-user/emailButtonRequire', {
                      email: this.email,
                  }).then(res => {
                      console.log(res);
                      this.$message.success(res.data.message);
                  });
              }
          },
          //绑定邮箱的发送
          registerEmail(){
              let emailPatter=/[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/;
              if (this.email=== '' || !emailPatter.test(this.email) || this.emailYzm === ''){
                  this.$message.error('输入内容不能为空且邮箱要符合格式')
              }else {
                  this.$http.post('/whc/blog-customer-user/emailButtonRegister',{
                      email: this.email,
                      emailYzm: this.emailYzm,
                  }).then(res=>{
                      console.log(res);
                      if (res.data.success === true){
                          this.$notify({
                              title: '成功',
                              message: '邮箱绑定成功',
                              type: 'success',
                          });
                          window.localStorage.setItem('myEmail',res.data.message);
                          this.reload();
                          //this.$router.go(0);
                      }else {
                          this.$message.error(res.data.message);
                      }
                  })
              }
          },
          //获取手机号给用户看
          getPhone(){
              this.$http.post('/whc/blog-customer-user/getPhoneButton')
                  .then(res =>{
                      if (res.data.success === true){
                          console.log(res);
                          this.phoneShow=1;
                          //window.localStorage.setItem('myPhone',res.data.message);
                      }
                      else {
                          console.log(res);
                          this.phoneShow=0;
                          this.$message.info('您还没完成手机注册,请绑定手机');
                          //window.localStorage.setItem('myPhone',res.data.message);
                      }
                  })
          },
          //获得注册手机的验证码
          getPhoneCode(){
              let phonePatter=/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;
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
              if (this.phone === '' || !phonePatter.test(this.phone)){
                  this.$message.error('手机号码不符合规则');
              }else {
                  this.$http.post('/whc/blog-customer-user/phoneCodeButton',{
                      phone: this.phone,
                  }).then(res =>{
                      console.log(res);
                      this.$message.success(res.data.message);
                  })
              }
          },
          //提交注册手机的验证码
          phoneRegister(){
              let phonePatterRegister=/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;
              if (this.phone === '' || this.phoneYzm === '' || !phonePatterRegister.test(this.phone)){
                  this.$message.error('手机或者验证码不符合规则');
              }else {
                  this.$http.post('/whc/blog-customer-user/phoneRegisterButton',{
                      phone: this.phone,
                      phoneYzm: this.phoneYzm,
                  }).then(res =>{
                      console.log(res);
                      if (res.data.success === true){
                          this.$notify({
                              title: '成功',
                              message: '手机绑定成功',
                              type: 'success',
                          });
                          window.localStorage.setItem('myPhone',res.data.message);
                          this.reload();
                          //this.$router.go(0);
                      }else {
                          this.$message.error(res.data.message);
                      }
                  })
              }
          }
      }
  }
</script>
