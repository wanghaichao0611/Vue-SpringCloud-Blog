<template>
  <div class="col-md-12 column">
    <Row>
      <Col span="30">
        <Card dis-hover>
          <p slot="title"><Strong v-html="blogName"></Strong> <small>会员时间</small></p>
          <p>充值会延迟一分钟左右，以邮箱，手机，系统通知为准</p>
          <br>
          <p v-if=" vipSign === 1 ">普通会员剩余时间:  {{ vipExpireTime }}天
            <span>
              <router-link :to="{name: 'Pay'}" target="_blank">
                <el-button type="success" size="mini" circle @click="showResult=true">续费
              </el-button>
              </router-link>
               <el-dialog title="支付结果" :visible.sync="showResult" center :modal-append-to-body='false'>
                 <el-divider><i class="el-icon-refresh"></i></el-divider>
                 <div class="col-md-6 column">
                 <el-button type="success" v-on:click="reloadMember">支付成功</el-button>
                 </div>
                 <div class="col-md-16 column">
                 <el-button type="error" v-on:click="reloadMember">支付失败</el-button>
                 </div>
               </el-dialog>
            </span>
          </p>
          <p v-if=" vipSign === 0 ">普通会员：{{ vipExpireTime }}</p>
          <br>
          <p v-if=" svipSign === 1 ">超级会员剩余时间:  {{ svipExpireTime }}天
          <span>
          <router-link :to="{name: 'Pay'}" target="_blank">
            <el-button type="success" size="mini" circle @click="showResult=true">续费
            </el-button>
          </router-link>
          </span>
          </p>
          <p v-if=" svipSign === 0 ">超级会员：{{ svipExpireTime }}</p>
        </Card>
      </Col>
    </Row>
  </div>
</template>
<script>
  export default {
      inject: ['reload'],
      data() {
          return {
              showResult: false,
              blogName: '',
              vipSign: '',
              svipSign: '',
              vipExpireTime: '',
              svipExpireTime: '',
          }
      },
      created() {
          this.getMemberAll();
      },
      methods: {
          //获取整个会员表的情况(是否是会员且存在多少天)
          getMemberAll(){
              this.$http.post('/whc/blog-customer-user/getMemberAllFeign')
                  .then(res =>{
                      console.log(res);
                      this.vipSign = res.data.vipSign;
                      this.svipSign = res.data.svipSign;
                      this.vipExpireTime = res.data.vipExpireTime;
                      this.svipExpireTime = res.data.svipExpireTime;
                      this.blogName=res.data.blogName;
                      //保存blogName
                      window.localStorage.setItem('blogName',res.data.data.blogName);
                  });
          },
          //局部刷新页面
          reloadMember(){
              this.reload();
          }
      }
  }
</script>
