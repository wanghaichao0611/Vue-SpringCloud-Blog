<template>
  <div style="position:absolute; top:50%; left:50%; width:480px; height:240px; margin-left:-240px; margin-top:-120px;">
  <!-- 校园认证的Form -->
    <el-button type="text" @click="XYRZ = true" v-on:click="getSchool">校园认证</el-button>

    <el-dialog title="校园认证" :visible.sync="XYRZ" v-if="schoolShow===0">
      <el-form>
        <el-form-item label="真实的姓名" :label-width="formLabelWidth">
          <el-input type="text" v-model="realnameXYRZ" autocomplete="off" placeholder="请输入你的真实的姓名" maxlength="15" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="个人的工号" :label-width="formLabelWidth">
          <el-input type="text" v-model="schoolNumber" autocomplete="off" placeholder="请输入你的工号" maxlength="50" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="选择你的大学" :label-width="formLabelWidth">
          <el-select
            v-model="schoolName"
            @change="selectValue"
            multiple
            filterable
            remote
            multiple-limit="1"
            reserve-keyword
            placeholder="请输入关键词"
            :remote-method="remoteMethod"
            :loading="loading">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="XYRZ = false">取 消</el-button>
        <el-button type="primary" @click="XYRZ = false" v-on:click="schoolRegister">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="校园认证" :visible.sync="XYRZ" v-if="schoolShow===1">
      <span>先进行实名认证</span>
    </el-dialog>
    <el-dialog title="校园认证" :visible.sync="XYRZ" v-if="schoolShow===2">
      <span>认证成功，学生消费返还10%金额券</span>
    </el-dialog>
  </div>
</template>
<script>
  export default {
      inject: ['reload'],
      data() {
          return {
              //校园验证的form
              realnameXYRZ: '',
              schoolNumber: '',
              schoolName: '',
              schoolShow: 0,
              options: [],
              value: [],
              list: [],
              loading: false,
              XYRZ: false,
              formLabelWidth: '100px',
              states: [
                  "郑州大学", "河南大学", "郑州华北水利水电",
                  "清华大学", "北京大学", "复旦大学",
              ],
          }
      },
      methods: {
          selectValue () {
              console.log(this.schoolName);
          },
          remoteMethod(query) {
              if (query !== '') {
                  this.loading = true;
                  setTimeout(() => {
                      this.loading = false;
                      this.options = this.list.filter(item => {
                          return item.label.toLowerCase()
                              .indexOf(query.toLowerCase()) > -1;
                      });
                  }, 200);
              } else {
                  this.options = [];
              }
          },
          //获得校园认证
          getSchool() {
              this.$http.post('/whc/blog-customer-user/getSchoolButton')
                  .then(res => {
                      if (res.data.success === 1) {
                          console.log(res);
                          this.schoolShow = 2;
                          window.localStorage.setItem('mySchoolName', res.data.mySchoolName);
                          window.localStorage.setItem('mySchoolCard', res.data.mySchoolCard);
                          window.localStorage.setItem('mySchool', res.data.myOwnSchool);

                      } else if (res.data.success === 2) {
                          console.log(res);
                          this.schoolShow = 0;
                          window.localStorage.setItem('mySchoolName', res.data.mySchoolName);
                          window.localStorage.setItem('mySchoolCard', res.data.mySchoolCard);
                          window.localStorage.setItem('mySchool', res.data.myOwnSchool);
                          this.$message.info('您还完成校园绑定,请完成绑定');

                      }else {
                          console.log(res);
                          this.schoolShow = 1;
                          this.$message.info('您还没完成实名认证,请完成实名认证');
                      }
                  })
          },
          //确认校园认证
          schoolRegister(){
              if (this.realnameXYRZ === '' || this.schoolNumber === '' || this.schoolName === ''){
                  this.$message.error('输入内容不可以为空且要和实名认证的姓名一样');
              }else {
                  this.$http.post('/whc/blog-customer-user/registerSchoolButton',{
                      realnameXYRZ: this.realnameXYRZ,
                      schoolNumber: this.schoolNumber,
                      schoolName: this.schoolName.toString(),
                  }).then(res =>{
                      console.log(res);
                      if (res.data.success === true) {
                          this.$notify({
                              title: '成功',
                              message: '校园认证成功',
                              type: 'success',
                          });
                          window.localStorage.setItem('mySchoolName', res.data.message.realname);
                          window.localStorage.setItem('mySchoolCard', res.data.message.schoolnumber);
                          window.localStorage.setItem('mySchool', res.data.message.school);
                          this.reload();
                          //this.$router.go(0);
                      }else {
                          this.$message.error(res.data.message);
                      }
                  })
              }
          },
      },
      mounted() {
          this.list = this.states.map(item => {
              return { value: `${item}`, label: `${item}` };
          });
      },
  }
</script>
