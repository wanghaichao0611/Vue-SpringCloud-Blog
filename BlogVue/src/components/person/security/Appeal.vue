<template>
  <div style="position:absolute; top:50%; left:50%; width:480px; height:240px; margin-left:-240px; margin-top:-120px;">
  <!-- 个人申诉的Form -->
  <el-button type="text" @click="shensu = true" v-on:click="getReset">个人申诉</el-button>

  <el-dialog title="安全申诉" :visible.sync="shensu">
    <div class="tabbable" id="tabs-64615">
      <ul class="nav nav-tabs">
        <li class="active">
          <a href="#panel-846867" data-toggle="tab">重置邮箱</a>
        </li>
        <li>
          <a href="#panel-723838" data-toggle="tab">更换手机</a>
        </li>
        <li>
          <a href="#panel-723839" data-toggle="tab">重置手机</a>
        </li>
      </ul>
      <div class="tab-content">
        <div class="tab-pane active" id="panel-846867">
          <div style="margin: 20px;"></div>
          <el-form v-if="resetEmailShow===0">
            <el-form-item label="手机号" :label-width="formLabelWidth">
              <el-input type="text"  v-model="ssPhone" autocomplete="off" placeholder="请输入你的手机号"></el-input>
            </el-form-item>
            <el-form-item label="验证码" :label-width="formLabelWidth">
              <el-input v-model="ssYzm" autocomplete="off" placeholder="请输入你的验证码" maxlength="6" show-word-limit></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="disable" @click="getSsEmail">{{getCode}}<i class="el-icon-message"></i></el-button>
            </el-form-item>
            <el-form-item label="新邮箱" :label-width="formLabelWidth">
              <el-input type="text" v-model="ssEmail" autocomplete="off" placeholder="请输入你的新邮箱" maxlength="30" show-word-limit></el-input>
            </el-form-item>
            <el-form-item label="确认新邮箱" :label-width="formLabelWidth">
              <el-input type="text" v-model="ssEmailAgain" autocomplete="off" placeholder="请再次输入你的新邮箱" maxlength="30" show-word-limit></el-input>
            </el-form-item>
            <el-form-item>
              <el-button @click="shensu = false">取 消</el-button>
              <el-button type="primary" @click="shensu = false" v-on:click="resetEmail">确 定</el-button>
            </el-form-item>
          </el-form>
          <el-form v-if="resetEmailShow===1">
            <el-form-irem>
              <span>亲,请完成手机和邮箱认证呦!</span>
            </el-form-irem>
          </el-form>
        </div>
        <div class="tab-pane" id="panel-723838">
          <div style="margin: 20px;"></div>
          <el-form v-if="resetPhoneShow===0">
            <el-form-item label="旧手机号" :label-width="formLabelWidth">
              <el-input type="text" v-model="oldPhone" autocomplete="off" placeholder="请输入你的旧手机号" maxlength="30" show-word-limit></el-input>
            </el-form-item>
            <el-form-item label="验证码" :label-width="formLabelWidth">
              <el-input v-model="phoneChangeYzm" autocomplete="off" placeholder="请输入你的验证码" maxlength="6" show-word-limit></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="disable" @click="getChangePhone">{{getCode}}<i class="el-icon-message"></i></el-button>
            </el-form-item>
            <el-form-item label="新手机号" :label-width="formLabelWidth">
              <el-input type="text"  v-model="newPhone" autocomplete="off" placeholder="请输入你的新手机号"></el-input>
            </el-form-item>
            <el-form-item label="确认新手机号" :label-width="formLabelWidth">
              <el-input type="text"  v-model="newPhoneAgain" autocomplete="off" placeholder="请再次输入你的新手机号"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button @click="shensu = false">取 消</el-button>
              <el-button type="primary" @click="shensu = false" v-on:click="changePhone">确 定</el-button>
            </el-form-item>
          </el-form>
          <el-form v-if="resetPhoneShow===1">
            <el-form-irem>
              <span>亲,请完成手机认证呦!</span>
            </el-form-irem>
          </el-form>
        </div>
        <div class="tab-pane" id="panel-723839">
          <div style="margin: 20px;"></div>
          <el-form v-if="resetShow===0">
            <el-form-item>
              <span style="font-style: italic; color: darkviolet;font-size: 20px">温馨提示<i class="el-icon-warning"></i>:一天只能进行一次成功的实名认证~请把握好机会!</span>
              <el-divider></el-divider>
            </el-form-item>
            <el-form-item label="姓名" :label-width="formLabelWidth">
              <el-input type="text"  v-model="resetName" autocomplete="off" placeholder="请输入你的姓名" maxlength="15" show-word-limit></el-input>
            </el-form-item>
            <el-form-item label="身份证" :label-width="formLabelWidth">
              <el-input v-model="resetCode" autocomplete="off" placeholder="请输入你的身份证" maxlength="18" show-word-limit></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="success" v-on:click="testCard">验证实名信息</el-button>
            </el-form-item>
          </el-form>
          <el-form v-if="resetShow===1">
            <el-form-item label="新的手机号" :label-width="formLabelWidth">
              <el-input type="text" v-model="resetNewPhone" autocomplete="off" placeholder="请输入你的新的手机号" maxlength="20" show-word-limit></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="disable" @click="getNewPhoneCode">{{getCode}}<i class="el-icon-message"></i></el-button>
            </el-form-item>
            <el-form-item label="验证码" :label-width="formLabelWidth">
              <el-input v-model="resetPhoneCode" autocomplete="off" placeholder="请输入你的验证码" maxlength="6" show-word-limit></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="success" class="el-icon-success" v-on:click="resetPhone">确定</el-button>
            </el-form-item>
          </el-form>
          <el-form v-if="resetShow===2">
            <el-form-irem>
              <span>亲,请完成实名认证呦!</span>
            </el-form-irem>
          </el-form>
        </div>
      </div>
    </div>
  </el-dialog>
  </div>
</template>
<script>
  export default {
      inject: ['reload'],
      data() {
          return {
              //申诉
              //手机重置邮箱
              ssPhone: '',
              ssYzm: '',
              ssEmail: '',
              ssEmailAgain: '',
              //换手机号绑定
              oldPhone: '',
              newPhone: '',
              phoneChangeYzm: '',
              newPhoneAgain: '',
              //身份证重置邮箱+手机
              resetName: '',
              resetCode: '',
              resetNewPhone: '',
              resetPhoneCode: '',
              //三种控制渲染
              resetEmailShow: 0,
              resetPhoneShow: 0,
              resetShow: 0,
              shensu: false,
              formLabelWidth: '100px',
              getCode: '获取验证码',
              isGeting: false,
              count: 60,
              disable: false,
          }
      },
      methods: {
          //前端获取重置的渲染
          getReset(){
              this.$http.post('/whc/blog-customer-user/resetShowFeign')
                  .then(res =>{
                      console.log(res);
                      //控制渲染
                      let sort=res.data.sort;
                      // 1.均不未空值
                      if (sort===1){
                          this.resetEmailShow=0;
                          this.resetPhoneShow=0;
                          this.resetShow=0;

                      }
                      //2.身份证为空值.
                      else if (sort===2) {
                          this.resetEmailShow=0;
                          this.resetPhoneShow=0;
                          this.resetShow=2;
                      }
                      //3.手机为null而身份证不为null(不可能实现)
                      else if (sort===3){
                          this.resetEmailShow=1;
                          this.resetPhoneShow=1;
                          this.resetShow=0;
                      }
                      //4.均为空值
                      else {
                          this.resetEmailShow=1;
                          this.resetPhoneShow=1;
                          this.resetShow=2;
                      }

                  })
          },
          //手机重置邮箱的验证码
          getSsEmail(){
              let ssPhonePatter=/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;
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
              if (this.ssPhone === '' || !ssPhonePatter.test(this.ssPhone)){
                  this.$message.error('手机号码不符合规则');
              }else {
                  this.$http.post('/whc/blog-customer-user/resetEmailButton',{
                      ssPhone: this.ssPhone,
                  }).then(res =>{
                      console.log(res);
                      this.$message.success(res.data.message);
                  })
              }
          },
          //手机重置邮箱
          resetEmail(){
              let resetPhonePatter=/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;
              let resetEmailPatter=/[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/;
              if (this.ssEmail=== '' || !resetEmailPatter.test(this.ssEmail) || this.ssYzm === '' ||
                  !resetPhonePatter.test(this.ssPhone) || this.ssPhone === '' || this.ssEmailAgain === ''
                  || !resetEmailPatter.test(this.ssEmailAgain) || this.newPhone !== this.newPhoneAgain){
                  this.$message.error('输入内容不能为空且要符合格式')
              }else {
                  this.$http.post('/whc/blog-customer-user/resetEmailFeign',{
                      ssPhone: this.ssPhone,
                      ssYzm: this.ssYzm,
                      ssEmail: this.ssEmail,
                  }).then(res=>{
                      console.log(res);
                      if (res.data.success === true){
                          this.$notify({
                              title: '成功',
                              message: '邮箱重置成功',
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
          //获取旧手机号的验证码
          getChangePhone(){
              let resetPhonePatter=/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;
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
              if (this.oldPhone === '' || !resetPhonePatter.test(this.oldPhone)){
                  this.$message.error('手机号码不符合规则');
              }else {
                  this.$http.post('/whc/blog-customer-user/changePhoneButton',{
                      oldPhone: this.oldPhone,
                  }).then(res =>{
                      console.log(res);
                      this.$message.success(res.data.message);
                  })
              }
          },
          //更换新的手机号
          changePhone(){
              let changePhonePatter=/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;
              if (this.oldPhone=== '' || this.phoneChangeYzm === '' || this.newPhone === '' ||
                  !changePhonePatter.test(this.oldPhone) || !changePhonePatter.test(this.newPhone)
                  || this.newPhoneAgain === '' || !changePhonePatter.test(this.newPhoneAgain) || this.ssEmail !== this.ssEmailAgain){
                  this.$message.error('输入内容不能为空且要符合格式')
              }else {
                  this.$http.post('/whc/blog-customer-user/changePhoneFeign',{
                      oldPhone: this.oldPhone,
                      phoneChangeYzm: this.phoneChangeYzm,
                      newPhone: this.newPhone,
                  }).then(res=>{
                      console.log(res);
                      if (res.data.success === true){
                          this.$notify({
                              title: '成功',
                              message: '手机更换成功',
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
          },
          //验证实名信息
          testCard(){
              if (this.resetName === '' || this.resetCode === ''){
                  this.$message.error('输入的内容不能为空值');
              }else {
                  this.$http.post('/whc/blog-customer-user/testCardButton',{
                      resetName: this.resetName,
                      resetCode: this.resetCode,
                  }).then(res=>{
                      console.log(res);
                      if (res.data.success === true){
                          this.$notify({
                              title: '成功',
                              message: res.data.message,
                              type: 'success',
                          });
                          this.resetShow=1;
                          //this.reload();
                          //this.$router.go(0);
                      }else {
                          this.$message.error(res.data.message);
                          this.resetShow=0;
                      }
                  })
              }
          },
          //新手机发送验证码
          getNewPhoneCode(){
              let resetCardPhonePatter=/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;
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
              if (this.resetNewPhone === '' || !resetCardPhonePatter.test(this.resetNewPhone)){
                  this.$message.error('手机号码不符合规则');
              }else {
                  this.$http.post('/whc/blog-customer-user/resetNewPhoneButton',{
                      resetNewPhone: this.resetNewPhone,
                  }).then(res =>{
                      console.log(res);
                      this.$message.success(res.data.message);
                  })
              }
          },
          //获取实名验证的新手机的信息
          resetPhone(){
              let cardResetPhonePatter=/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;
              if ( this.resetNewPhone === '' || this.resetPhoneCode === '' ||
                  !cardResetPhonePatter.test(this.resetNewPhone)){
                  this.$message.error('输入内容不能为空且要符合格式')
              }else {
                  this.$http.post('/whc/blog-customer-user/resetNewPhoneFeign',{
                      resetNewPhone: this.resetNewPhone,
                      resetPhoneCode: this.resetPhoneCode,
                  }).then(res=>{
                      console.log(res);
                      if (res.data.success === true){
                          this.$notify({
                              title: '成功',
                              message: '手机重置成功',
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
