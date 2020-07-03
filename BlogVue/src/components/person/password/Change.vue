<template>
  <div style="position:absolute; top:50%; left:50%; width:480px; height:240px; margin-left:-240px; margin-top:-120px;">
  <!-- 修改密码的Form -->
  <el-button type="text" @click="xiugaimimaxianshi = true">修改密码</el-button>
  <!-- 新旧密码才能修改-->
  <el-dialog title="修改密码" :visible.sync="xiugaimimaxianshi">
    <el-form>
      <el-form-item label="旧密码" :label-width="formLabelWidth">
        <el-input type="password" v-model="oldPassword" autocomplete="off" placeholder="请输入你的旧密码" maxlength="16" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="新密码" :label-width="formLabelWidth">
        <el-input type="password" v-model="newPassword" autocomplete="off" placeholder="请输入你的新密码，8-16位" maxlength="16" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="新密码" :label-width="formLabelWidth">
        <el-input type="password" v-model="newPasswordAgain" auto-complete="off" placeholder="请输入相同的新密码" maxlength="16" show-word-limit></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="xiugaimimaxianshi = false">取 消</el-button>
      <el-button type="primary" @click="xiugaimimaxianshi = false" v-on:click="xiugaimima">确 定</el-button>
    </div>
  </el-dialog>
  </div>
</template>
<script>
  export default {
      inject: ['reload'],
      data() {
          return {
              //修改密码的form
              xiugaimimaxianshi: false,
              oldPassword: '',
              newPassword: '',
              newPasswordAgain: '',
              formLabelWidth: '100px',
          }
      },
      methods:{
          //修改密码
          xiugaimima(){
              //账号名字的规则
              let patterName=/^[a-z][a-zA-Z0-9\-]{6,15}$/;
              //密码的长度
              let patterPassword=/^.{7,15}$/;

              if (this.oldPassword === '' || this.newPassword === '' || this.newPasswordAgain === ''
                  || this.newPassword !== this.newPasswordAgain || !patterPassword.test(this.newPassword) || !patterName.test(this.usernameXGMM)){
                  if (this.oldPassword === '' || this.newPassword === '' || this.newPasswordAgain === '') {
                      this.$message.error('输入内容不能为空');
                  }
                  else if (this.newPassword !== this.newPasswordAgain){
                      this.$message.error('输入的两次新密码不相同')
                  }else{
                      this.$message.error('输入的密码长度不符合要求')
                  }
              }else {
                  this.$http.post('/whc/blog-customer-user/updatePassword',{
                      oldPassword: this.oldPassword,
                      newPassword: this.newPassword,
                  }).then(res=>{
                      console.log(res);
                      //ResetController已经返回JSON对象,之前写的不太明白.
                      let xiugaimiaJSON=JSON.stringify(res.data.success);
                      let a=JSON.stringify(res.data.code);
                      if (xiugaimiaJSON === 'true') {
                         alert(res.data.message+",请重新登录!!!");
                          window.localStorage.clear();
                          this.$router.go(0);
                      }
                      else if (xiugaimiaJSON === 'false') {
                          this.$message({
                              type: "error",
                              message: res.data.message,
                          })
                      } else {
                          //最开始测试用,可以忽略.
                          this.$message.info('用户过期,请重新登录.');
                          this.$router.push('/login');
                      }
                  })
              }
          },
      }
  }
</script>
