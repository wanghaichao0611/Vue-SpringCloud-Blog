<template>
  <div class="container">
    <div class="row clearfix">
      <div class="col-md-12 column">
        <div>
          <el-avatar> Blog </el-avatar>
        </div>
        <br>
        <h3 class="text-center text-success">
          请{{ this.$route.query.newUsername }}完善个人博客信息(30分钟内完成注册，否则失效)
        </h3>
        <br>
        <div>
        </div>
        <hr>
        <el-form ref="form" label-width="80px">
          <el-form-item label="用户名">
            <el-input type="text" v-model="username"  :placeholder="username" disabled></el-input>
          </el-form-item>
          <el-form-item label="博客名">
            <el-input type="text" v-model="blogname" placeholder="博客名在7-13个字符之间"
                      maxlength="13" show-word-limit></el-input>
          </el-form-item>
          <el-form-item label="职业">
            <el-select v-model="job" placeholder="请选择你的职业">
              <el-option label="测试" value="测试"></el-option>
              <el-option label="运维" value="运维"></el-option>
              <el-option label="前端" value="前端"></el-option>
              <el-option label="后端" value="后端"></el-option>
              <el-option label="全栈" value="全栈"></el-option>
              <el-option label="UI设计" value="UI设计"></el-option>
              <el-option label="产品经理" value="产品经理"></el-option>
              <el-option label="技术总监" value="技术总监"></el-option>
              <el-option label="架构师" value="架构师"></el-option>
              <el-option label="BOSS老板" value="BOSS老板"></el-option>
            </el-select>
          </el-form-item>
            <el-form-item label="生日日期">
                <el-date-picker type="date" placeholder="选择日期" v-model="birth">
                </el-date-picker>
            </el-form-item>
          <el-form-item label="用户头像">
            <el-upload
              class="avatar-uploader"
              action="/whc/blog-customer-user/userPictureFeign"
              :headers="myHeaders"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
              enctype="multipart/form-data">
              <img v-if="imageUrl" :src="imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="sex">
              <el-radio label="男" value="男"></el-radio>
              <el-radio label="女" value="女"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="个人简介">
            <el-input
              type="textarea"
              placeholder="请输入三十个字以内"
              v-model="introduce"
              maxlength="30"
              show-word-limit
            >
            </el-input>
          </el-form-item>
          <el-form-item label="确认信息">
            <el-switch v-model="delivery"></el-switch>
          </el-form-item>
          <el-form-item>
            <el-button  v-on:click="postPerson">确定下一步</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }

  body {background: url('../../../static/img/login/BJT.jpg');}

</style>
<script>
  export default {
      data(){
          return {
                  username: this.$route.query.newUsername,
                  blogname: '',
                  job: '',
                  birth: '',
                  delivery: false,
                  type: [],
                  sex: '',
                  introduce: '',
                  imageUrl: '',
                  myHeaders: {'TOKEN': window.localStorage.getItem('token')},
          }
      },
      methods: {
          postPerson() {
              let patter = /^.{6,12}$/;
              if (this.blogname === '' || this.delivery === false ||
                  !patter.test(this.blogname)) {
                  if (this.blogname === '') {
                      this.$message({
                          type: "error",
                          message: '博客名不能为空',
                      })
                  } else if (!patter.test(this.blogname)) {
                      this.$message({
                          type: 'error',
                          message: '你的博客名不符合规则',
                      })
                  } else {
                      this.$message.error('确认信息按钮')
                  }
              } else {
                  this.$http.post('/whc/blog-customer-user/personRegister', {
                      blogname: this.blogname,
                      sex: this.sex,
                      birth: this.birth,
                      job: this.job,
                      introduce: this.introduce,
                  }).then(res => {
                      console.log(res);
                      let personRegister = JSON.stringify(res.data.success);
                      if (personRegister === 'true') {
                          //保存博客名
                          window.localStorage.setItem('blogName', this.blogname);
                          this.$router.replace({name: 'MationNext'});
                          this.$router.go(0);
                          this.$message({
                              type: "success",
                              message: res.data.message,
                          });
                      } else {
                          this.$message({
                              type: "error",
                              message: res.data.message,
                          })
                      }
                  })
              }
          },
          handleAvatarSuccess(res, file) {
              console.log(res);
              this.imageUrl = URL.createObjectURL(file.raw);
              window.localStorage.setItem('userPicture',res);
          },
          beforeAvatarUpload(file) {
              const isJPG = file.type === 'image/jpeg';
              const isLt2M = file.size / 1024 / 1024 < 2;

              if (!isJPG) {
                  this.$message.error('上传头像图片只能是 JPG 格式!');
              }
              if (!isLt2M) {
                  this.$message.error('上传头像图片大小不能超过 2MB!');
              }
              return isJPG && isLt2M;
          }
      }
  }
</script>
