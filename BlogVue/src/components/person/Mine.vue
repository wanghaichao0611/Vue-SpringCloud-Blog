<template>
  <div style="position:absolute; top:50%; left:50%; width:480px; height:240px; margin-left:-240px; margin-top:-120px;">
  <el-collapse v-model="activeNames" @change="handleChange">
    <el-collapse-item name="1">
      <template slot="title">
        查看个人信息<i class="el-icon-zoom-in"></i>
      </template>
      <span>你的昵称是: {{ myBlogName }}</span>
      <br>
      <span>你的邮箱是: {{ myEmail }}</span>
      <br>
      <span>你的手机是: {{ myPhone }}</span>
      <br>
    </el-collapse-item>
    <el-collapse-item name="2">
      <template slot="title">
        查看实名认证<i class="el-icon-zoom-in"></i>
      </template>
      <span>你的姓名是: {{ myName }}</span>
      <br>
      <span>你的身份证是: {{ myCard }}</span>
    </el-collapse-item>
    <el-collapse-item name="3">
      <template slot="title">
        查看校园认证<i class="el-icon-zoom-in"></i>
      </template>
      <span>你的姓名是: {{ mySchoolName }}</span>
      <br>
      <span>你的工号是: {{ mySchoolCard }}</span>
      <br>
      <span>你的学校是: {{ mySchool }}</span>
    </el-collapse-item>
  </el-collapse>
  </div>
</template>
<script>
  export default {
      data(){
          return {
              activeNames: ['1'],
              mySchoolName: window.localStorage.getItem('mySchoolName'),
              mySchoolCard: window.localStorage.getItem('mySchoolCard'),
              mySchool: window.localStorage.getItem('mySchool'),
              myEmail: window.localStorage.getItem('myEmail'),
              myPhone: window.localStorage.getItem('myPhone'),
              myName: window.localStorage.getItem('myName'),
              myCard: window.localStorage.getItem('myCard'),
          }
      },
      methods: {
          handleChange(val) {
              console.log(val);
          },
          //获取整个安全的数据(均加密)
          getSecurityAll(){
              this.$http.post('/whc/blog-customer-user/getSecurityLoginFeign')
                  .then(res =>{
                      console.log(res);
                      //保存邮箱
                      window.localStorage.setItem('myEmail',res.data.security.email);
                      //保存手机号
                      window.localStorage.setItem('myPhone',res.data.security.phone);
                      //保存实名认证的姓名
                      window.localStorage.setItem('myName',res.data.security.realname);
                      //保存加密的身份证
                      window.localStorage.setItem('myCard',res.data.security.cardnumber);
                      //保存我的学校
                      window.localStorage.setItem('mySchool',res.data.security.school);
                      //保存我的学校姓名
                      window.localStorage.setItem('mySchoolName',res.data.security.realname);
                      //保存我的学校工号
                      window.localStorage.setItem('mySchoolCard',res.data.security.schoolnumber);
                      this.myEmail=res.data.security.email;
                      this.myPhone=res.data.security.phone;
                      this.myName=res.data.security.realname;
                      this.myCard=res.data.security.cardnumber;
                      this.mySchool=res.data.security.school;
                      this.mySchoolName=res.data.security.realname;
                      this.mySchoolCard=res.data.security.schoolnumber;
                  })
          }
      },
      created() {
          //安全的数据
          this.getSecurityAll();
      },
      mounted() {
      }
  }
</script>
