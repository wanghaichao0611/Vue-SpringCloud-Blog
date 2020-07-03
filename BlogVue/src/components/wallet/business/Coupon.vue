<template>
  <div>
    <div>
      <h2 v-if="showCouponOff===true">您未获得任何券!!!</h2>
    </div>
    <el-row>
      <el-col :span="8" v-for="(item,index) in couponOff" :key="item.id" :offset="index > 0 ? 0 : 0">
        <el-card :body-style="{ padding: '0px' }">
          <el-image src="../../../static/img/pay/paper.jpg" style="width: 75px;height: 75px;" alt="优惠券">
          </el-image>
          <div style="padding: 14px;">
            <span>{{ item.couponName}}---<strong style="color: red">{{item.couponMoney}}元</strong></span>
            <br>
            <strong>{{ item.createtime}}开始</strong>
            <div class="bottom clearfix">
              <time class="time">
                <strong style="color: red">
                {{ item.couponSubject }}
              </strong>
              </time>
              <br>
              <br>
              <Poptip
                confirm
                transfer="true"
                title="优惠券使用后会失效，您确定要使用吗？"
                @on-ok="exchangeCoupon(item.id,item.orderId)"
                @on-cancel="cancelCoupon">
                <el-button type="text" class="button" >兑换</el-button>
              </Poptip>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <br>
    <br>
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page"
        :page-sizes="[6]"
        :page-size="count"
        layout="total, sizes, prev, pager , next , jumper"
        :total="couponOff.total">
      </el-pagination>
    </div>
  </div>
</template>
<style>
  .time {
    font-size: 13px;
    color: #999;
  }

  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }
</style>

<script>
    export default {
        inject: ['reload'],
        data() {
            return {
                page: 1,
                count: 6,
                showCouponOff: false,
                couponOff: [
                    {
                        id: '',
                        orderId: '',
                        userId: '',
                        couponName: '',
                        couponMoney: '',
                        couponOn: '',
                        couponSubject: '',
                        createtime: '',
                        costtime: '',
                        total: '',
                    }
                ]
            }
        },
        methods:{
            exchangeCoupon(id,orderId) {
                this.$http.post('/whc/blog-customer-user/exchangeCouponFeign',{
                    id: id,
                    orderId: orderId,
                }).then(res =>{
                    console.log(res);
                    if (res.data.success===true){
                        alert('兑换已成功，已到余额中(会有几分钟的延迟，请您稍事等待)');
                        this.reload();
                    }else {
                        alert('服务器出了状况,请稍后再试!');
                    }
                })
            },
            cancelCoupon() {
                this.$Message.info('取消使用返还券！');
            },
            // 初始页currentPage、初始每页数据数pagesize和数据data
            handleSizeChange: function (size) {
                this.count = size;
                console.log(this.count); //每页下拉显示数据
                this.selectOff()
            },
            handleCurrentChange: function(currentPage){
                this.page = currentPage;
                console.log(this.page);  //点击第几页
                this.selectOff()
            },
            //查询未被兑换过的券
            selectOff(){
                this.$http.post('/whc/blog-customer-user/selectCouponOffFeign',{
                    page: this.page,
                    count: this.count,
                }).then(res => {
                    console.log(res);
                    if (res.data.success===true){
                        this.couponOff=res.data.list;
                        this.couponOff.total=res.data.total;
                    }
                    if (res.data.total===0){
                        this.showCouponOff=true;
                    }
                })
            }
        },
        created() {
            this.selectOff();
        }
    }
</script>
