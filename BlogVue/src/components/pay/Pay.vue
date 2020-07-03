<template>
<div class="container">
  <div class="row clearfix">
    <div class="col-md-12 column">
      <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
        <Menu mode="horizontal" theme="dark" active-name="1">
            <Submenu name="1">
              <template slot="title">
                <span v-html="blogName"><span>{{ blogName }}</span></span>
              </template>
              <MenuGroup title="操作">
                <MenuItem name="1-2"><router-link to="/person" target="_blank" style="color: black">个人资料</router-link></MenuItem>
                <MenuItem name="1-3"><el-link :underline="false" style="color: black" v-on:click="loginOutShow">注销登录</el-link></MenuItem>
              </MenuGroup>
            </Submenu>
        </Menu>
      </nav>
    </div>
    <div class="col-md-12 column" id="loginPanel" style="position:absolute; top:30%; left:50%; width:480px; height:240px; margin-left:-240px; margin-top:-120px;">
      <Row :gutter="500">
        <Col span="12" class="demo-tabs-style1" style="background: #e3e8ee;padding:16px;width: 900px;height: 600px">
          <Tabs type="card">
            <TabPane label="超级会员">
              <br>
              <br>
              <div class="demo-drawer-profile">
                <Row>
              <Col span="2">
                当前账号:
              </col>
                  <Col span="5">
                    <span v-html="blogName">{{ blogName }}</span>
                  </Col>
                  <Col span="2">
                    <el-image src="../../../static/img/pay/pay.png"></el-image>
                  </Col>
                  <Col span="6">
                    <span>我的余额：&nbsp;&nbsp;&nbsp;<Strong style="color: black">{{ myMoney }}&nbsp;元</Strong></span>
                  </Col>
                  <Col span="2">
                    <img src="../../../static/img/pay/alipay.png" alt="aliPay">
                  </Col>
                </Row>
                <br>
                <Row>
              <Col span="2">
                开通时长:
              </Col>
                  <Col span="6">
                      <Button v-on:click="yearCode" type="primary" size="large">
                        1年<span>300便宜60巨划算</span>
                      </Button>
                  </Col>
                  <Col span="6">
                    <Button v-on:click="threeCode" type="primary" size="large">
                      3个月<span>80便宜10超划算</span>
                    </Button>
                  </Col>
                  <Col span="6">
                    <Button v-on:click="monthCode" type="primary" size="large">
                      1个月<span>30不划算</span>
                    </Button>
                  </Col>
                  <Col span="3">
                    支付宝二维码
                  </Col>
                </Row>
                <Divider/>
                <Row>
                  <Col span="2">
                    <p class="text-left">支付方式:</p>
                  </Col>
                  <Col span="5">
                    &nbsp;&nbsp;
                    <Button size="large" v-on:click="balanceButtonSvip=true" >余额购买超级会员</Button>
                    <Modal v-model="balanceButtonSvip" :loading="balanceSvipLoading"
                           @on-ok="asyncSvip" draggable scrollable title="余额支付超级会员(余额支付无优惠且不返还任何福利)">
                      <Form>
                        <FormItem>
                          <div>我的余额是多少：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong style="color: black">{{ myMoney }}</strong>&nbsp;&nbsp;&nbsp;元</div>
                        </FormItem>
                        <FormItem>
                          超级会员(30一个月)：&nbsp;&nbsp;&nbsp;<Strong style="color: red">{{ balanceSvip }}</Strong>&nbsp;&nbsp;元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                          <InputNumber v-model="balanceSvipNumber" max="99" min="0" @Click="balanceMoneySvip" :editable="true"></InputNumber>
                          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;一共&nbsp;&nbsp;<Strong style="color: red">{{ balanceSvipNumber}}</Strong>&nbsp;&nbsp;月超级会员
                        </FormItem>
                        <FormItem>
                          需要支付&nbsp;&nbsp;&nbsp;{{balanceSvip}}元&nbsp;&nbsp;&nbsp;余额还剩&nbsp;&nbsp;&nbsp;{{ myMoney-balanceSvip}}元
                          <span v-if="myMoney-balanceSvip<0" style="color: red">&nbsp;&nbsp;&nbsp;余额不足，无法支付</span>
                        </FormItem>
                      </Form>
                    </Modal>
                  </Col>
                </Row>
              </div>
              <vue-qr v-if="yearShow===1" :logoSrc="imageUrl" :size="200" :text="yearText"></vue-qr>
              <vue-qr v-if="threeShow===1" :logoSrc="imageUrl" :size="200" :text="threeText"></vue-qr>
              <vue-qr v-if="monthShow===1" :logoSrc="imageUrl" :size="200" :text="monthText"></vue-qr>
            </TabPane>
            <TabPane label="普通会员">
              <br>
              <br>
              <div class="demo-drawer-profile">
                <Row>
                  <Col span="2">
                    当前账号:
                  </col>
                  <Col span="5">
                    <span v-html="blogName">{{ blogName }}</span>
                  </Col>
                  <Col span="2">
                    <el-image src="../../../static/img/pay/pay.png"></el-image>
                  </Col>
                  <Col span="6">
                    <span>我的余额：&nbsp;&nbsp;&nbsp;<Strong style="color: black">{{ myMoney }}&nbsp;元</Strong></span>
                  </Col>
                  <Col span="2">
                    <img src="../../../static/img/pay/alipay.png" alt="aliPay">
                  </Col>
                </Row>
                <br>
                <Row>
                  <Col span="2">
                    开通时长:
                  </Col>
                  <Col span="6">
                    <Button type="primary" size="large" @click="vipYearButton">
                      1年<span>150便宜50巨划算</span>
                    </Button>
                  </Col>
                  <Col span="6">
                    <Button type="primary" size="large" @click="vipThreeButton" >
                      3个月<span>50便宜10超划算</span>
                    </Button>
                  </Col>
                  <Col span="6">
                    <Button  type="primary" size="large" @click="vipMonthButton" >
                      1个月<span>20不划算</span>
                    </Button>
                  </Col>
                  <Col span="3">
                    支付宝原始支付
                  </Col>
                </Row>
              </div>
              <Divider/>
              <Row>
                <Col span="2">
                  <p class="text-left">余额支付:</p>
                </Col>
                <Col span="5">
                  &nbsp;&nbsp;
                  <Button size="large" v-on:click="balanceButtonVip=true" >余额购买普通会员</Button>
                  <Modal v-model="balanceButtonVip" :loading="balanceVipLoading"
                         @on-ok="asyncVip" draggable scrollable title="余额支付普通会员(余额支付无优惠且不返还任何福利)">
                    <Form>
                      <FormItem>
                        <div>我的余额是多少：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong style="color: black">{{ myMoney }}</strong>&nbsp;&nbsp;&nbsp;元</div>
                      </FormItem>
                      <FormItem>
                        普通会员(20一个月)：&nbsp;&nbsp;&nbsp;<Strong style="color: red">{{ balanceVip }}</Strong>&nbsp;&nbsp;元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <InputNumber v-model="balanceVipNumber" max="99" min="0" @Click="balanceMoneyVip" :editable="true"></InputNumber>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;一共&nbsp;&nbsp;<Strong style="color: red">{{ balanceVipNumber}}</Strong>&nbsp;&nbsp;月普通会员
                      </FormItem>
                      <FormItem>
                        需要支付&nbsp;&nbsp;&nbsp;{{balanceVip}}元&nbsp;&nbsp;&nbsp;余额还剩&nbsp;&nbsp;&nbsp;{{ myMoney-balanceVip}}元
                        <span v-if="myMoney-balanceVip<0" style="color: red">&nbsp;&nbsp;&nbsp;余额不足，无法支付</span>
                      </FormItem>
                    </Form>
                  </Modal>
                </Col>
              </Row>
              <br>
              <br>
              <br>
              <br>
              <Col class="container">
                <el-button type="success" v-if="alipayButtonYear===1"  @click="showPayResult=true" v-on:click="vipYearPay" >点击年费支付宝支付</el-button>
                <el-button type="success" v-if="alipayButtonThreeMonths===1"  @click="showPayResult=true" v-on:click="vipThreePay" >点击包季支付宝支付</el-button>
                <el-button type="success" v-if="alipayButtonMonth===1"  @click="showPayResult=true" v-on:click="vipMonthPay" >点击包月支付宝支付</el-button>
              </Col>
            </TabPane>
          </Tabs>
        </Col>
      </Row>
    </div>
  </div>
  <el-dialog title="支付结果(可能会延迟几分钟，请您稍事等待)" :visible.sync="showPayResult" center :modal-append-to-body='false'>
    <el-divider><i class="el-icon-refresh"></i></el-divider>
    <Strong style="color: black">优惠券或者随机立减红包需要几分钟会到账，届时会系统消息通知，请您稍事等待!</Strong>
    <el-divider><i class="el-icon-refresh"></i></el-divider>
    <div class="col-md-6 column">
      <router-link :to="{name: 'Power'}" style="text-decoration: none">
        <el-button type="success">支付成功</el-button>
      </router-link>
    </div>
    <div class="col-md-16 column">
      <el-button type="error" @click="showPayResult=false">支付失败</el-button>
    </div>
  </el-dialog>
</div>
</template>
<style>

  .demo-drawer-profile{
    font-size: 14px;
  }
  .demo-tabs-style1 > .ivu-tabs-card > .ivu-tabs-content {
    height: 550px;
    margin-top: -16px;
  }

  .demo-tabs-style1 > .ivu-tabs-card > .ivu-tabs-content > .ivu-tabs-tabpane {
    background: #fff;
    padding: 16px;
  }

  .demo-tabs-style1 > .ivu-tabs.ivu-tabs-card > .ivu-tabs-bar .ivu-tabs-tab {
    width: 430px;
    border-color: transparent;
  }

  .demo-tabs-style1 > .ivu-tabs-card > .ivu-tabs-bar .ivu-tabs-tab-active {
    border-color: #fff;
  }
</style>
<script>
    import VueQr from 'vue-qr'
    export default {
        components: {
            VueQr,
        },
        inject: ['reload'],
        data() {
          return {
              id: window.localStorage.getItem('id'),
              walletPassword: '',
              myMoney: '',
              //VIP余额支付
              balanceVip: 0,
              balanceVipNumber: '',
              balanceVipLoading: true,
              balanceButtonVip: false,
              //SVIP余额支付
              balanceSvip: 0,
              balanceSvipNumber: '',
              balanceSvipLoading: true,
              balanceButtonSvip: false,
              showPayResult: false,
              alipayButtonYear: 0,
              alipayButtonThreeMonths: 0,
              alipayButtonMonth: 0,
              yearShow: 0,
              threeShow: 0,
              monthShow: 0,
              out_trade_no:'',
              subject: '',
              total_amount: '',
              body: '',
              store_id: '',
              yearText: '',
              threeText: '',
              monthText: '',
              imageUrl: require("../../../static/img/pay/wallet.png"),
              userAvatar: window.localStorage.getItem('userPicture'),
              theme1: 'light',
              blogName: window.localStorage.getItem('blogName'),

          }
      },
        computed:{
            //计算普通会员
            balanceMoneyVip(){
                let totalVip=0;
                totalVip=(totalVip+20)*this.balanceVipNumber;
                this.balanceVip=totalVip;
                return this.balanceVip;
            },
            //计算超级会员
            balanceMoneySvip(){
                let totalSvip=0;
                totalSvip=(totalSvip+30)*this.balanceSvipNumber;
                this.balanceSvip=totalSvip;
                return this.balanceSvip;
            },
        },
        created(){
            this.selectRealMoney();
            // 页面创建生命周期函数
            this.initWebSocket()
        },
        destroyed() {
            // 离开页面生命周期函数
            this.websocketclose();
        },
        methods: {
            initWebSocket() {
                console.log(this.id);
                // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
                this.websock = new WebSocket("ws://localhost:8017/websocket/"+this.id);
                this.websock.onopen = this.websocketonopen;
                this.websock.onerror = this.websocketonerror;
                this.websock.onmessage = this.websocketonmessage;
                this.websock.onclose = this.websocketclose;
            },
            websocketonopen() {
                console.log("WebSocket连接成功");
            },
            websocketonerror() {
                console.log("WebSocket连接发生错误");
            },
            websocketonmessage: function (res) {
                console.log(res);
                this.$router.push(res.data);
            },
            websocketclose: function (e) {
                console.log("connection closed (" + e.code + ")");
            },
            //查询真实的金额(未开通则无法使用)
            selectRealMoney(){
                this.$http.post('/whc/blog-customer-user/selectBalanceFeign')
                    .then(res =>{
                        if (res.data.success===true){
                            this.myMoney=res.data.myMoney;
                            //保存余额给用户显示
                            window.localStorage.setItem('myMoney',res.data.myMoney);
                        }
                        if (res.data.success===false){
                            alert("您还未完成我的钱包注册!");
                            this.myMoney=res.data.myMoney;
                            //保存余额给用户显示
                            window.localStorage.setItem('myMoney',res.data.myMoney);
                        }
                    })
            },
          //异步VIP体验良好
            asyncVip () {
                if (this.myMoney-this.balanceVip<0){
                    this.$message.error('余额不足，无法支付!');
                    this.balanceButtonVip = false;
                }else if (this.balanceVip===0){
                    this.balanceButtonVip = false;
                    this.$message.error('请选择充值月份!');
                } else{
                    setTimeout(() => {
                        this.balanceButtonVip = false;
                    }, 2000);
                    this.$prompt('请输入你的支付密码', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        inputPattern: /^[0-9]{6}$/,
                        inputType: 'password',
                        inputErrorMessage: '支付密码格式不正确'
                    }).then(({ value }) => {
                        this.walletPassword=value;
                        this.$http.post('/whc/blog-customer-user/payVipBalanceFeign',{
                            out_trade_no: new Date().getTime()+this.getCode(),
                            subject: 'VIP余额消费',
                            body: '用户余额充值VIP',
                            walletPassword: this.walletPassword,
                            balanceVip: this.balanceVip,
                            balanceVipNumber: this.balanceVipNumber,
                        }).then(res =>{
                            if (res.data.success === 1){
                                this.$message({
                                    type: 'success',
                                    message: '恭喜你支付成功，会有一定的延迟到账，请查看通知且及时刷新!'
                                });
                                this.$router.push({name: 'Power'});
                            }else {
                                alert(res.data.message);
                            }
                        })
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '输入支付密码失败!'
                        });
                    });
                }
            },
            //异步Svip体验良好
            asyncSvip () {
                if (this.myMoney-this.balanceSvip<0){
                    this.$message.error('余额不足，无法支付!');
                    this.balanceButtonSvip = false;
                }else if (this.balanceSvip===0){
                    this.balanceButtonSvip = false;
                    this.$message.error('请选择充值月份!');
                } else{
                    setTimeout(() => {
                        this.balanceButtonSvip = false;
                    }, 2000);
                    this.$prompt('请输入你的支付密码', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        inputPattern: /^[0-9]{6}$/,
                        inputType: 'password',
                        inputErrorMessage: '支付密码格式不正确'
                    }).then(({ value }) => {
                        this.walletPassword=value;
                        this.$http.post('/whc/blog-customer-user/paySvipBalanceFeign',{
                            out_trade_no: new Date().getTime()+this.getCode(),
                            subject: 'SVIP余额消费',
                            body: '用户余额充值SVIP',
                            walletPassword: this.walletPassword,
                            balanceSvip: this.balanceSvip,
                            balanceSvipNumber: this.balanceSvipNumber,
                        }).then(res =>{
                            if (res.data.success === 1){
                                this.$message({
                                    type: 'success',
                                    message: '恭喜你支付成功，会有一定的延迟到账，请查看通知且及时刷新!'
                                });
                                this.$router.push({name: 'Power'});
                            }else {
                                alert(res.data.message);
                            }
                        })
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '输入支付密码失败!'
                        });
                    });
                }
            },

            //vip年费支付Button的展示
            vipYearButton(){
                this.alipayButtonYear=1;
                this.alipayButtonThreeMonths=0;
                this.alipayButtonMonth=0;
            },
            //vip包季支付Button的展示
            vipThreeButton(){
                this.alipayButtonYear=0;
                this.alipayButtonThreeMonths=1;
                this.alipayButtonMonth=0;
            },
            //vip包月支付Button的展示
            vipMonthButton(){
                this.alipayButtonYear=0;
                this.alipayButtonThreeMonths=0;
                this.alipayButtonMonth=1;
            },
          //code随机码
            getCode(){
                let code = "";
                let codeLength = 5;//验证码的长度
                let random = new Array(0,1,2,3,4,5,6,7,8,9);//随机数
                for(let i = 0; i < codeLength; i++) {
                    //循环操作
                    let index = Math.floor(Math.random()*10);//取得随机数的索引（0~35）
                    code += random[index];//根据索引取得随机数加到code上
                }
                return code;
            },
          //支付宝原始支付(年费)
            vipYearPay(){
                this.$http.post('/whc/blog-customer-user/alipayVipFeign',{
                    out_trade_no: new Date().getTime()+this.getCode(),
                    subject:  'VIP年费',
                    total_amount: '150.00',
                    body: 'VIP年费优惠会员',
                }).then(res => {
                    console.log(res);
                    let dwSafari;
                    dwSafari = window.open('http://localhost:8088/payRedirect');
                    dwSafari.document.open();
                    let dataObj = res.data;
                    let html = dataObj.replace(/[^\u0000-\u00FF]/g, function ($0) {
                        return escape($0).replace(/(%u)(\w{4})/gi, "&#x$2;")
                    });
                    dwSafari.document.write("<html><head><title></title><meta charset='utf-8'><body>" + dataObj + "</body></html>");
                    dwSafari.document.forms[0].submit();
                    dwSafari.document.close();
                })
            },
            //支付宝原始支付(包季)
            vipThreePay(){
                this.$http.post('/whc/blog-customer-user/alipayVipFeign',{
                    out_trade_no: new Date().getTime()+this.getCode(),
                    subject:  'VIP包季',
                    total_amount: '50.00',
                    body: 'VIP包季优惠会员',
                }).then(res =>{
                    console.log(res);
                    let dwSafari;
                    dwSafari=window.open('http://localhost:8088/payRedirect');
                    dwSafari.document.open();
                    let dataObj=res.data;
                    let html=  dataObj.replace(/[^\u0000-\u00FF]/g,function($0){return escape($0).replace(/(%u)(\w{4})/gi,"&#x$2;")});
                    dwSafari.document.write("<html><head><title></title><meta charset='utf-8'><body>"+dataObj+"</body></html>");
                    dwSafari.document.forms[0].submit();
                    dwSafari.document.close();
                })
            },
            //支付宝原始支付(包月)
            vipMonthPay(){
                this.$http.post('/whc/blog-customer-user/alipayVipFeign',{
                    out_trade_no: new Date().getTime()+this.getCode(),
                    subject:  'VIP包月',
                    total_amount: '20.00',
                    body: 'VIP包月优惠会员',
                }).then(res =>{
                    console.log(res);
                    let dwSafari;
                    dwSafari=window.open('http://localhost:8088/payRedirect');
                    dwSafari.document.open();
                    let dataObj=res.data;
                    let html=  dataObj.replace(/[^\u0000-\u00FF]/g,function($0){return escape($0).replace(/(%u)(\w{4})/gi,"&#x$2;")});
                    dwSafari.document.write("<html><head><title></title><meta charset='utf-8'><body>"+dataObj+"</body></html>");
                    dwSafari.document.forms[0].submit();
                    dwSafari.document.close();
                })
            },
            //SVIP年费会员
            yearCode(){
                this.yearShow=1;
                this.threeShow=0;
                this.monthShow=0;
                this.$http.post('/whc/blog-customer-user/alipaySvipQrcodeFeign',{
                    out_trade_no: new Date().getTime()+this.getCode(),
                    subject:  'SVIP年费',
                    total_amount: '300.00',
                    body: 'SVIP年费优惠',
                    store_id: '2088102179614159',
                }).then(res =>{
                    console.log(res);
                    this.yearText=res.data;
                })
            },
            //SVIP包季
            threeCode(){
                this.threeShow=1;
                this.monthShow=0;
                this.yearShow=0;
                this.$http.post('/whc/blog-customer-user/alipaySvipQrcodeFeign',{
                    out_trade_no: new Date().getTime()+this.getCode(),
                    subject:  'SVIP包季',
                    total_amount: '80.00',
                    body: 'SVIP包季优惠',
                    store_id: '2088102179614159',
                }).then(res =>{
                    console.log(res);
                    this.threeText=res.data;
                })
            },
            //SVIP包月
            monthCode(){
                this.monthShow=1;
                this.yearShow=0;
                this.threeShow=0;
                this.$http.post('/whc/blog-customer-user/alipaySvipQrcodeFeign',{
                    out_trade_no: new Date().getTime()+this.getCode(),
                    subject:  'SVIP包月',
                    total_amount: '30.00',
                    body: 'SVIP包月优惠',
                    store_id: '2088102179614159',
                }).then(res =>{
                    console.log(res);
                    this.monthText=res.data;
                })
            },
            //注销登录
            loginOutShow() {
                window.localStorage.clear();
                this.$router.go(0);
            }
        }
  }
</script>
