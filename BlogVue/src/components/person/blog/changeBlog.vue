<template>
  <div style="position:absolute; top:50%; left:50%; width:480px; height:240px; margin-left:-240px; margin-top:-120px;">
  <div>
  <!-- 博客信息 -->
  <el-button type="text" @click="userblog = true"> 修改博客信息</el-button>

  <el-dialog title="完善个人信息" :visible.sync="userblog">
    <el-form ref="form" label-width="80px">
      <el-form-item label="博客名">
        <el-input v-model="blogname" type="text" placeholder="博客名要符合社会主义价值观"
                  maxlength="13" show-word-limit>
        </el-input>
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
      <el-form-item label="出生日期">
        <div class="block">
          <span class="demonstration"></span>
          <el-date-picker
            v-model="birth"
            type="date"
            placeholder="生日日期">
          </el-date-picker>
        </div>
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
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="userblog = false">取 消</el-button>
      <el-button type="primary" @click="userblog = false" v-on:click="xiugaiuser">确 定</el-button>
    </div>
  </el-dialog>
  </div>
  </div>
</template>
<script>
  export default {
      inject: ['reload'],
      data() {
          return {
              //博客信息
              userblog: false,
              blogname: '',
              job: '',
              birth: '',
              delivery: false,
              sex: '',
              introduce: '',
              imageUrl: '',
              myHeaders: {'TOKEN': window.localStorage.getItem('token')},
          }
      },
      methods: {
          //修改博客信息
          xiugaiuser() {
              let patterBlogName = /^.{6,12}$/;
              if (this.delivery === false || this.blogname === '' || !patterBlogName.test(this.blogname)) {
                  this.$message.error('确认信息按钮');
              } else {
                  this.$http.post('/whc/blog-customer-user/personUpdate', {
                      blogname: this.blogname,
                      sex: this.sex,
                      birth: this.birth,
                      job: this.job,
                      introduce: this.introduce,
                  }).then(res => {
                      console.log(res);
                      let personRegister = JSON.stringify(res.data.success);
                      if (personRegister === 'true') {
                          this.$message.success('修改成功');
                          window.localStorage.setItem('blogName',this.blogname);
                          this.reload();
                      } else {
                          this.$message({
                              type: "error",
                              message: JSON.stringify(res.data.message),
                          })
                      }
                  })
              }
          },
          //上传文件
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
          },
      }
  }
</script>
