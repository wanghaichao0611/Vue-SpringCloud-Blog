<template>
  <div class="container">
    <div class="row clearfix">
      <div class="col-md-12 column">
        <Col span="11" offset="2">
          <Card dis-hover>
            <p slot="title"><span v-html="blogName">{{ blogName }}</span>的账户余额</p>
            <p>￥ {{ myMoney }}</p>
            <br>
            <el-button type="warning"  @click="MoneyShow=true">充值</el-button>&nbsp;&nbsp;&nbsp;
            <el-dialog title="冲冲冲" :visible.sync="MoneyShow" center :modal-append-to-body='false'>
              <el-form>
                <el-form-item label="充值金额" :label-width="formLabelWidth">
                  <el-input type="text" v-model="walletMoney"  autocomplete="off"
                            placeholder="单次充值不能超过1万,最低充值必须为整数，最低为1元"
                            maxlength="4" show-word-limit></el-input>
                </el-form-item>
              </el-form>
              <el-form>
                <RadioGroup v-model="vertical" vertical>
                  <Radio label="支付宝">
                    <Icon type="social-apple"></Icon>
                    <span>支付宝</span>
                  </Radio>
                  <Radio label="银行卡">
                    <Icon type="social-android"></Icon>
                    <span>银行卡</span>
                  </Radio>
                </RadioGroup>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="MoneyShow=false">取 消</el-button>
                <el-button type="primary" @click="MoneyShow=false" v-on:click="rechargeMoney">确 定</el-button>
              </div>
            </el-dialog>
            <el-button type="warning" plain>提现</el-button>
          </Card>
        </Col>
        <el-dialog title="支付结果(可能会延迟几分钟，请您稍事等待)" :visible.sync="showPersonPayResult" center :modal-append-to-body='false'>
          <el-divider><i class="el-icon-refresh"></i></el-divider>
          <Strong style="color: black">用户充值不会返还优惠和红包，充值届时会系统消息通知或者刷新即可，请您稍事等待!</Strong>
          <el-divider><i class="el-icon-refresh"></i></el-divider>
          <div class="col-md-6 column">
            <el-button type="success" v-on:click="reloadAccount">支付成功</el-button>
          </div>
          <div class="col-md-16 column">
            <el-button type="error" @click="showPersonPayResult=false">支付失败</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>
<script>
    export default {
        inject: ['reload'],
        data() {
            return {
                showPersonPayResult: false,
                vertical: '支付宝',
                myMoney: '',
                blogName: window.localStorage.getItem('blogName'),
                MoneyShow: false,
                formLabelWidth: '100px',
                walletMoney: '',
            }
        },
        created(){
            this.selectNewMoney();
        },
        methods: {
            //局部页面刷新
            reloadAccount(){
                this.reload();
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
            //充值金额
            rechargeMoney(){
                let moneyTest= /^[1-9]\d{0,3}$/;
                if (this.walletMoney === '' || !moneyTest.test(this.walletMoney) ){
                    this.$message.error('输入正确的金额!');
                }else {
                    this.$http.post('/whc/blog-customer-user/rechargeMoneyFeign', {
                        out_trade_no: new Date().getTime()+this.getCode(),
                        subject:  '余额充值',
                        total_amount: this.walletMoney,
                        body: '充值我的钱包',
                    }).then(res => {
                        console.log(res);
                        this.showPersonPayResult=true;
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
                }
            },
            //查询你最新的金额
            selectNewMoney(){
                this.$http.post('/whc/blog-customer-user/selectNewMoneyFeign')
                    .then(res => {
                        console.log(res);
                        if (res.data.success===true){
                            this.myMoney=res.data.myMoney;
                        }else {
                            alert('服务器出了状况,请稍后再试!');
                        }
                    })
            }
        }
    }
</script>
