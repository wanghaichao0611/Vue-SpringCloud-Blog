<template>
    <div class="container">
      <div class="row clearfix">
        <div class="col-md-3 column">
          <p class="text-left">你好~<span style="color: blue">{{myName}}</span> &emsp; 当前账户余额  ￥{{myMoney}} </p>
          <br>
          <p class="text-left">银行卡总数: <span style="color: red">{{cards}}</span>张</p>
          <br>
          <Card style="width:220px" v-if="this.myBankCardOne!==null">
            <div style="text-align:center">
              <img src="../../../../static/img/bankCard/CCB.png" alt="CCB">
              <p>{{myBankCardOne}}</p>
            </div>
          </Card>
          <Card style="width:220px" v-if="this.myBankCardTwo!==null">
            <div style="text-align:center">
              <img src="../../../../static/img/bankCard/ICB.png" alt="ICB">
              <p>{{myBankCardTwo}}</p>
            </div>
          </Card>
          <Card style="width:220px" v-if="this.myBankCardThree!==null">
            <div style="text-align:center">
              <img src="../../../../static/img/bankCard/CNB.png" alt="CNB">
              <p>{{myBankCardThree}}</p>
            </div>
          </Card>
      </div>
        <div class="col-md-9 column">
          <div class="block">
            <el-date-picker
              v-model="visitDate"
              size="small"
              type="datetimerange"
              :picker-options="pickerOptions"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              align="right">
            </el-date-picker>
            &emsp;
            <Button type="error" v-on:click="getCost" size="small">查看消费</Button>
            <Divider/>
            <ve-histogram  :data="chartData" :settings="chartSettings" :extend="extend"></ve-histogram>
          </div>
        </div>
      </div>
    </div>
</template>
<style>

</style>
<script>
  export default {
      data() {
          return {
              myName: window.localStorage.getItem("myBankRealName"),
              myMoney: '',
              cards: '',
              myBankCardOne: '',
              myBankCardTwo: '',
              myBankCardThree: '',
              visitDate: '',
              chartData: {
                  columns: ['totalPayPercent','totalPay', 'totalPayMoney', 'totalOrder', 'totalOrderMoney'],
                  rows: [
                      {
                          totalPayPercent: '',
                          totalPay: '',
                          totalPayMoney: '',
                          totalOrder: '',
                          totalOrderMoney: '',
                      }
                  ],
              },
              extend: {
                  series: {
                      label: {show: true, position: "top"}
                  }
              },
              chartSettings: {
                  xAxisName: '消费比重',
                  labelMap: {
                      'totalPayPercent': '消费比重',
                      'totalPay': '消费总数',
                      'totalPayMoney': '消费总额',
                      'totalOrder': '下单总数',
                      'totalOrderMoney': '下单总额',
                  }
              },
              pickerOptions: {
                      disabledDate (date) {
                          const now=new Date();
                          const end = new Date();
                          const start = new Date(new Date(now.setDate(now.getMonth()+1))
                              .toLocaleDateString());
                          const year = new Date(new Date().getFullYear(), 0);
                          return date && date.valueOf() < year.getTime() || date && date> end.getTime();
                      },
                  shortcuts: [
                      {
                      text: '今天',
                      onClick(picker) {
                          const end = new Date();
                          const start = new Date(new Date().toLocaleDateString());
                          picker.$emit('pick', [start, end]);
                      }
                  },{
                      text: '本月',
                      onClick(picker) {
                          const now=new Date();
                          const end = new Date();
                          now.setDate(1);
                          const start = new Date(new Date(now).toLocaleDateString());
                          picker.$emit('pick', [start, end]);
                      }
                  },{
                      text: '最近三天',
                      onClick(picker) {
                          const now=new Date();
                          const end = new Date();
                          const start = new Date(new Date(now.setTime(now.getTime() - 3600 * 1000 * 24 * 3))
                              .toLocaleDateString());
                          picker.$emit('pick', [start, end]);
                      }
                  },{
                      text: '最近一星期',
                      onClick(picker) {
                          const now=new Date();
                          const end = new Date();
                          const start = new Date(new Date(now.setTime(now.getTime() - 3600 * 1000 * 24 * 7))
                              .toLocaleDateString());
                          picker.$emit('pick', [start, end]);
                      }
                  }, {
                      text: '最近一个月',
                      onClick(picker) {
                          const now=new Date();
                          const end = new Date();
                          const start = new Date(new Date(now.setTime(now.getTime() - 3600 * 1000 * 24 * 30))
                              .toLocaleDateString());
                          picker.$emit('pick', [start, end]);
                      }
                  }, {
                      text: '最近三个月',
                      onClick(picker) {
                          const now=new Date();
                          const end = new Date();
                          const start = new Date(new Date(now.setTime(now.getTime() - 3600 * 1000 * 24 * 90))
                              .toLocaleDateString());
                          picker.$emit('pick', [start, end]);
                      }
                  }, {
                      text: '最近六个月',
                      onClick(picker) {
                          const now=new Date();
                          const end = new Date();
                          const start = new Date(new Date(now.setTime(now.getTime() - 3600 * 1000 * 24 * 180))
                              .toLocaleDateString());
                          picker.$emit('pick', [start, end]);
                      }
                  }, {
                      text: '今年至今天',
                      onClick(picker) {
                          const end = new Date();
                          const start = new Date(new Date().getFullYear(), 0);
                          picker.$emit('pick', [start, end]);
                      }
                  }]
              },
          }
      },
      methods: {
          //查看消费图
          getCost(){
              if (this.visitDate === ''){
                  this.$message.error('时间不能为空!')
              }else {
                  this.$http.post('/whc/blog-customer-user/getCostFeign',{
                      costStart: this.visitDate[0].toString(),
                      costEnd: this.visitDate[1].toString(),
                  }).then(res =>{
                      if (res.data.success === true){
                          this.chartData.rows=res.data.list;
                          this.$message.success('查询成功!');
                      }else {
                          alert('不存在消费下单!');
                      }
                  })
              }
          },
          //确认是否开通钱包并且设置自己的安全密码(首先确认实名注册)
          setPassword(){
              this.$http.post('/whc/blog-customer-user/confirmWalletFeign')
                  .then(res =>{
                      console.log(res);
                      if (res.data.success===0){
                          alert('请先完成实名认证!');
                          this.$router.push('/person/idCard');
                      }
                      else if (res.data.success===1){
                          //确认可以开通钱包并且签订协议
                          this.$router.replace('/walletSecurity');

                      }else {
                          this.$message({
                              type: "info",
                              message: '注意个人信息安全!',
                          });
                      }
                  })
          },
          //查询整个支付表
          getBankAll(){
              this.$http.post('/whc/blog-customer-user/getBankAllFeign')
                  .then(res =>{
                      console.log(res);
                      if (res.data.success === true){
                          //保存余额给用户显示
                          window.localStorage.setItem('myMoney',res.data.bank.walletMoney);
                          this.myMoney=res.data.bank.walletMoney;
                          let total=0;
                          if (res.data.bank.bankcardOnesign===1){
                              total++;
                          }
                          if (res.data.bank.bankcardTwosign===1){
                              total++;
                          }
                          if (res.data.bank.bankcardThreesign===1){
                              total++;
                          }
                          //保存银行卡的数量
                          window.localStorage.setItem('myBankCards',total);
                          this.cards=total;
                          //保存三个银行卡的隐藏号码
                          window.localStorage.setItem('myBankCardOne',res.data.bank.bankcardOne);
                          window.localStorage.setItem('myBankCardTwo',res.data.bank.bankcardTwo);
                          window.localStorage.setItem('myBankCardThree',res.data.bank.bankcardThree);
                          this.myBankCardOne=res.data.bank.bankcardOne;
                          this.myBankCardTwo=res.data.bank.bankcardTwo;
                          this.myBankCardThree=res.data.bank.bankcardThree;
                          //保存银行卡的名字
                          window.localStorage.setItem('myBankRealName',res.data.bank.realname);
                          this.myName=res.data.bank.realname;
                      }
                  })
          },
          //获取手机号给用户看
          getPhone(){
              this.$http.post('/whc/blog-customer-user/getPhoneButton')
                  .then(res =>{
                      if (res.data.success === true){
                          console.log(res);
                          window.localStorage.setItem('myPhone',res.data.message);
                      }
                      else {
                          console.log(res);
                          this.$message.info('您还没完成手机注册,请绑定手机');
                          window.localStorage.setItem('myPhone',res.data.message);
                      }
                  })
          },
      },
      created() {
          this.setPassword();
          this.getBankAll();
          this.getPhone();
      },
      mounted() {
      }
  }
</script>
