<template>
  <div>
    <div>
      <h2 v-if="showCouponOn===true">您未获得任何券!!!</h2>
    </div>
    <el-row>
      <el-col :span="8" v-for="(item,index) in couponOn" :key="item.id" :offset="index > 0 ? 0 : 0">
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
                title="优惠券删除后会失效，您确定要删除吗？"
                @on-ok="deleteCoupon(item.id,item.orderId)"
                @on-cancel="cancelDeleteCoupon">
                <el-button type="text" class="button" >删除</el-button>
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
        :total="couponOn.total">
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
                showCouponOn: false,
                page: 1,
                count: 6,
                couponOn: [
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
            deleteCoupon(id,orderId) {
                this.$http.post('/whc/blog-customer-user/deleteCouponFeign',{
                    id: id,
                    orderId: orderId,
                }).then(res =>{
                    console.log(res);
                    if (res.data.success===true){
                        alert('删除成功，祝您生活愈快!');
                        this.reload();
                    }else {
                        alert('服务器出了状况,请稍后再试!');
                    }
                })
            },
            cancelDeleteCoupon() {
                this.$Message.info('取消删除返还券！');
            },
            // 初始页currentPage、初始每页数据数pagesize和数据data
            handleSizeChange: function (size) {
                this.count = size;
                console.log(this.count); //每页下拉显示数据
                this.selectOn()
            },
            handleCurrentChange: function(currentPage){
                this.page = currentPage;
                console.log(this.page);  //点击第几页
                this.selectOn()
            },
            //查询使用过的兑换券
            selectOn(){
                this.$http.post('/whc/blog-customer-user/selectCouponOnFeign',{
                    page: this.page,
                    count: this.count,
                }).then(res => {
                    console.log(res);
                    if (res.data.success===true){
                        this.couponOn=res.data.list;
                        this.couponOn.total=res.data.total;
                    }
                    if (res.data.total===0){
                        this.showCouponOn=true;
                    }
                })
            }
        },
        created() {
            this.selectOn();
        }
    }
</script>
