<template>
  <div style="position:absolute; top:50%; left:50%; width:480px; height:240px; margin-left:-240px; margin-top:-120px;">
  <!-- 邮箱验证的Form -->
  <el-button type="text" @click="EYZ = true" v-on:click="getEmail">邮箱绑定</el-button>

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
  </div>
</template>
<script>
  export default {
      inject: ['reload'],
      data(){
          return {
              //邮箱验证的form和按钮
              getCode: '获取验证码',
              isGeting: false,
              count: 60,
              disable: false,
              email: '',
              emailYzm: '',
              emailShow: 0,
              myEmail: window.localStorage.getItem('myEmail'),
              EYZ: false,
              formLabelWidth: '100px',
          }
      },
      methods: {
          //获得账号是否已经邮箱注册
          getEmail(){
              this.$http.post('/whc/blog-customer-user/getEmailButton')
                  .then(res =>{
                      if (res.data.success === true){
                          console.log(res);
                          this.emailShow=1;
                          window.localStorage.setItem('myEmail',res.data.message);
                      }
                      else {
                          console.log(res);
                          this.emailShow=0;
                          window.localStorage.setItem('myEmail',res.data.message);
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
          }
      }
  }
</script>
