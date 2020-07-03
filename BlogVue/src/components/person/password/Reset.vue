<template>
  <div style="position:absolute; top:50%; left:50%; width:480px; height:240px; margin-left:-240px; margin-top:-120px;">
  <!-- 找回密码的Form -->
  <el-button type="text" @click="ZH = true" v-on:click="selectSecurity">重置密码</el-button>

  <el-dialog :visible.sync="ZH" title="请选择你的重置方式">
    <el-tabs :tab-position="tabPosition" style="height: 350px;">
      <el-tab-pane label="邮箱重置密码" v-if="emailSecurity===1">
        <el-form>
          <el-form-item label="邮箱" :label-width="formLabelWidth">
            <el-input type="text"  v-model="chEmail" autocomplete="off" placeholder="请输入你的邮箱"></el-input>
          </el-form-item>
          <el-form-item label="验证码" :label-width="formLabelWidth">
            <el-input v-model="passwordYzm" autocomplete="off" placeholder="请输入你的验证码" maxlength="6" show-word-limit></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="disable" @click="getCodeResetPassword">{{getCode}}<i class="el-icon-message"></i></el-button>
          </el-form-item>
          <el-form-item label="新密码" :label-width="formLabelWidth">
            <el-input type="password" v-model="chPassword" autocomplete="off" placeholder="请输入你的新密码" maxlength="16" show-word-limit></el-input>
          </el-form-item>
          <el-divider></el-divider>
          <el-form-item>
            <el-button @click="ZH = false">取 消</el-button>
            <el-button type="primary" @click="ZH = false" v-on:click="ResetPassword">确 定</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="邮箱重置密码" v-if="emailSecurity===0">
        <el-form>
          <el-form-irem>
            <span>亲,请完成邮箱绑定呦!</span>
          </el-form-irem>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="手机重置密码" v-if="phoneSecurity===1">
        <el-form>
          <el-form-item label="手机号" :label-width="formLabelWidth">
            <el-input type="text"  v-model="phoneReset" autocomplete="off" placeholder="请输入你的手机号"></el-input>
          </el-form-item>
          <el-form-item label="验证码" :label-width="formLabelWidth">
            <el-input v-model="phoneResetCode" autocomplete="off" placeholder="请输入你的验证码" maxlength="6" show-word-limit></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="disable" @click="getResetCardCode">{{getCode}}<i class="el-icon-message"></i></el-button>
          </el-form-item>
          <el-form-item label="新密码" :label-width="formLabelWidth">
            <el-input type="password" v-model="phonePassword" autocomplete="off" placeholder="请输入你的新密码" maxlength="16" show-word-limit></el-input>
          </el-form-item>
          <el-divider></el-divider>
          <el-form-item>
            <el-button @click="ZH = false">取 消</el-button>
            <el-button type="primary" @click="ZH = false" v-on:click="phoneResetPassword">确 定</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="手机重置密码" v-if="phoneSecurity===0">
        <el-form>
          <el-form-irem>
            <span>亲,请完成手机绑定呦!</span>
          </el-form-irem>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="实名重置密码"  v-if="cardSecurity===1">
        <el-form>
          <el-form-item label="姓名" :label-width="formLabelWidth">
            <el-input type="text"  v-model="cardName" autocomplete="off" placeholder="请输入你的姓名" maxlength="15" show-word-limit></el-input>
          </el-form-item>
          <el-form-item label="身份证" :label-width="formLabelWidth">
            <el-input v-model="cardCode" autocomplete="off" placeholder="请输入你的身份证" maxlength="18" show-word-limit></el-input>
          </el-form-item>
          <el-form-item label="新密码" :label-width="formLabelWidth">
            <el-input type="password" v-model="cardPassword" autocomplete="off" placeholder="请输入你的新密码" maxlength="16" show-word-limit></el-input>
          </el-form-item>
          <br>
          <el-divider></el-divider>
          <el-form-item>
            <el-button @click="ZH = false">取 消</el-button>
            <el-button type="primary" @click="ZH = false" v-on:click="cardResetPassword">确 定</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="实名重置密码"  v-if="cardSecurity===0">
        <el-form>
          <el-form-irem>
            <span>亲,请完成实名认证呦!</span>
          </el-form-irem>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
  </div>
</template>
<script>
  export default {
      inject: ['reload'],
      data() {
          return {
              //重置密码的form
              tabPosition: 'left',
              //邮箱重置密码
              chEmail: '',
              passwordYzm: '',
              chPassword: '',
              //手机重置密码
              phoneReset: '',
              phoneResetCode: '',
              phonePassword: '',
              //身份证重置密码
              cardName: '',
              cardCode: '',
              cardPassword: '',
              formLabelWidth: '100px',
              //安全的显示
              emailSecurity: 1,
              phoneSecurity: 1,
              cardSecurity: 1,
              ZH: false,
              getCode: '获取验证码',
              isGeting: false,
              count: 60,
              disable: false,
          }
      },
      methods: {
          //渲染给用户看
          selectSecurity(){
              this.$http.post('/whc/blog-customer-user/getSecurityAllFeign')
                  .then(res =>{
                      console.log(res);
                      //控制渲染
                      let sort=res.data.sort;
                      // 1.均不未空值
                      if (sort===1){
                          this.emailSecurity=1;
                          this.phoneSecurity=1;
                          this.cardSecurity=1;

                      }
                      //2.手机号为空值
                      else if (sort===2) {
                          this.emailSecurity=1;
                          this.phoneSecurity=0;
                          this.cardSecurity=1;
                      }
                      //3.身份证为空值
                      else if (sort===3){
                          this.emailSecurity=1;
                          this.phoneSecurity=1;
                          this.cardSecurity=0;
                      }
                      //4.均为空值
                      else if (sort===4){
                          this.emailSecurity=0;
                          this.phoneSecurity=0;
                          this.cardSecurity=0;
                      }
                      //5.邮箱为空
                      else if (sort===5) {
                          this.emailSecurity=0;
                          this.phoneSecurity=1;
                          this.cardSecurity=1;
                      }
                      //6.邮箱和手机为空值
                      else if (sort===6){
                          this.emailSecurity=0;
                          this.phoneSecurity=0;
                          this.cardSecurity=1;
                      }
                      //7.邮箱和身份证为空值
                      else if (sort===7){
                          this.emailSecurity=0;
                          this.phoneSecurity=1;
                          this.cardSecurity=0;
                      }
                      //8.身份证和手机为空值
                      else {
                          this.emailSecurity=1;
                          this.phoneSecurity=0;
                          this.cardSecurity=0;
                      }
                  })
          },
          //邮箱重置密码的Button
          getCodeResetPassword(){
              let emailPatterReset=/[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/;
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
              if (this.chEmail==='' || !emailPatterReset.test(this.chEmail)){
                  this.$message.error('邮箱不符合格式');
              }else {
                  this.$http.post('/whc/blog-customer-user/emailButtonReset', {
                      chEmail: this.chEmail,
                  }).then(res => {
                      console.log(res);
                      this.$message.info(res.data.message);
                  });
              }
          },
          //邮箱重置密码的提交
          ResetPassword(){
              let emailPatterResetPassword=/[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/;
              let patterResetPassword=/^.{7,15}$/;
              if (this.chEmail=== '' || !emailPatterResetPassword.test(this.chEmail) || this.chPassword === ''
                  || this.passwordYzm === '' || !patterResetPassword.test(this.chPassword)){
                  this.$message.error('输入不能为空且邮箱和密码要符合格式');
              }else {
                  this.$http.post('/whc/blog-customer-user/emailButtonResetPassword',{
                      chEmail: this.chEmail,
                      passwordYzm: this.passwordYzm,
                      chPassword: this.chPassword,
                  }).then(res => {
                      console.log(res);
                      if (res.data.success === true){
                          this.$notify({
                              title: '成功',
                              message: res.data.message,
                              type: 'success',
                          });
                          this.reload();
                          //this.$router.go(0);
                      }else {
                          this.$message.error(res.data.message);
                      }
                  })
              }
          },
          //手机重置的验证码
          getResetCardCode(){
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
              if (this.phoneReset === '' || !phonePatter.test(this.phoneReset)){
                  this.$message.error('手机号码不符合规则');
              }else {
                  this.$http.post('/whc/blog-customer-user/phoneResetButton',{
                      phoneReset: this.phoneReset,
                  }).then(res =>{
                      console.log(res);
                      this.$message.success(res.data.message);
                  })
              }
          },
          //手机重置密码
          phoneResetPassword(){
              let phonePatterReset=/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;
              let phoneResetPassword=/^.{7,15}$/;
              if (this.phoneReset === '' || this.phoneResetCode === '' || this.phonePassword === '' ||
                  !phoneResetPassword.test(this.phonePassword) || !phonePatterReset.test(this.phoneReset)){
                  this.$message.error('输入不能为空且手机和密码要符合格式');
              }else {
                  this.$http.post('/whc/blog-customer-user/phoneButtonResetPassword',{
                      phoneReset: this.phoneReset,
                      phoneResetCode: this.phoneResetCode,
                      phonePassword: this.phonePassword,
                  }).then(res => {
                      console.log(res);
                      if (res.data.success === true){
                          this.$notify({
                              title: '成功',
                              message: res.data.message,
                              type: 'success',
                          });
                          this.reload();
                          //this.$router.go(0);
                      }else {
                          this.$message.error(res.data.message);
                      }
                  })
              }

          },
          //身份证重置密码
          cardResetPassword(){
              let cardPatterReset=/^[1-9]\d{5}(18|19|20|(3\d))\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
              if (this.cardName === '' || this.cardCode === ''|| this.cardPassword === '' || !cardPatterReset.test(this.cardCode)){
                  this.$message.error('输入不能为空且实名信息和密码要符合格式');
              }else {
                  this.$http.post('/whc/blog-customer-user/cardResetPassword',{
                      cardName: this.cardName,
                      cardCode: this.cardCode,
                      cardPassword: this.cardPassword,
                  }).then(res => {
                      console.log(res);
                      if (res.data.success === true){
                          this.$notify({
                              title: '成功',
                              message: res.data.message,
                              type: 'success',
                          });
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
