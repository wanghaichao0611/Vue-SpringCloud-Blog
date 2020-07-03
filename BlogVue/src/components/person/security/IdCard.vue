<template>
  <div style="position:absolute; top:50%; left:50%; width:480px; height:240px; margin-left:-240px; margin-top:-120px;">
  <!-- 安全认证的Form -->
  <el-button type="text" @click="SFRZ = true" v-on:click="getCard">实名认证</el-button>

  <el-dialog title="实名认证" :visible.sync="SFRZ" v-if="realShow===0">
    <el-form>
      <el-form-item label="手机号" :label-width="formLabelWidth">
        <el-input type="text" v-model="phoneSFRZ" autocomplete="off" placeholder="请输入你的账号" maxlength="20" show-word-limit></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :disabled="disable" @click="getCardPhoneCode">{{getCode}}<i class="el-icon-message"></i></el-button>
      </el-form-item>
      <el-form-item label="验证码" :label-width="formLabelWidth">
        <el-input type="text" v-model="phoneCode" autocomplete="off" placeholder="请输入你的验证码" maxlength="6" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="真实的姓名" :label-width="formLabelWidth">
        <el-input type="text" v-model="realnameSFRZ" autocomplete="off" placeholder="请输入你的真实的名字" maxlength="15" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="身份证号码" :label-width="formLabelWidth">
        <el-input type="text" v-model="cardNumber" autocomplete="off" placeholder="请输入你的身份证号码" maxlength="18" show-word-limit></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="SFRZ = false">取 消</el-button>
      <el-button type="primary" @click="SFRZ = false" v-on:click="cardRegister">确 定</el-button>
    </div>
  </el-dialog>
  <el-dialog title="认证成功" :visible.sync="SFRZ" v-else>
    <el-form>
      <span>恭喜你你已经完成实名认证<i class="el-icon-user-solid"></i></span>
      <br>
      <el-divider/>
      <span>你的姓名是: {{ myName }}</span>
      <br>
      <span>你的身份证是: {{ myCard }}</span>
    </el-form>
  </el-dialog>
  </div>
</template>
<script>
  export default {
      inject: ['reload'],
      data() {
          return {
              //身份验证的form
              phoneSFRZ: '',
              phoneCode: '',
              realnameSFRZ: '',
              cardNumber: '',
              realShow: 0,
              myName: window.localStorage.getItem('myName'),
              myCard: window.localStorage.getItem('myCard'),
              SFRZ: false,
              formLabelWidth: '100px',
              getCode: '获取验证码',
              isGeting: false,
              count: 60,
              disable: false,
          }
      },
      methods: {
          //前端获得实名认证的信息
          getCard(){
              this.$http.post('/whc/blog-customer-user/getNameCardButton')
                  .then(res =>{
                      if (res.data.success === true){
                          console.log(res);
                          this.realShow=1;
                          window.localStorage.setItem('myName',res.data.realName);
                          window.localStorage.setItem('myCard',res.data.realCard);
                      }
                      else {
                          console.log(res);
                          this.realShow=0;
                          window.localStorage.setItem('myName',res.data.message);
                          window.localStorage.setItem('myCard',res.data.message);
                          this.$message.info('您还没有实名注册!');
                      }
                  })
          },
          //实名认证需要手机号认证
          getCardPhoneCode(){
              let phonePatterSFRZ=/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;
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
              if (this.phoneSFRZ === '' || !phonePatterSFRZ.test(this.phoneSFRZ)){
                  this.$message.error('手机号码不符合规则');
              }else {
                  this.$http.post('/whc/blog-customer-user/phoneCardCodeButton',{
                      phoneSFRZ: this.phoneSFRZ,
                  }).then(res =>{
                      console.log(res);
                      this.$message.success(res.data.message);
                  })
              }
          },
          //确认实名认证信息
          cardRegister(){
              let phonePatterSFQR=/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;
              let cardPatter=/^[1-9]\d{5}(18|19|20|(3\d))\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
              if (this.phoneSFRZ === '' || this.phoneCode === '' || this.realnameSFRZ === '' || this.cardNumber === ''||
                  !phonePatterSFQR.test(this.phoneSFRZ) || !cardPatter.test(this.cardNumber)){
                  this.$message.error('输入内容不正确');
              }else {
                  this.$http.post('/whc/blog-customer-user/phoneCardRegisterButton',{
                      phoneSFRZ: this.phoneSFRZ,
                      phoneCode: this.phoneCode,
                      realnameSFRZ: this.realnameSFRZ,
                      cardNumber: this.cardNumber,
                  }).then(res =>{
                      console.log(res);
                      if (res.data.success === true){
                          this.$notify({
                              title: '成功',
                              message: '实名认证成功',
                              type: 'success',
                          });
                          window.localStorage.setItem('myName',res.data.realName);
                          window.localStorage.setItem('myCard',res.data.realCard);
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
