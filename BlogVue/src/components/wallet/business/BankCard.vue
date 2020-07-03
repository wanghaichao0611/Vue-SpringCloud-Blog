<template>
  <div class="container">
    <div class="row clearfix">
      <div class="col-md-12 column">
        <h3>
          {{ bankName }}的银行卡&nbsp;<small>共 <strong>{{ myBankCards }}</strong> 张(暂时只有16-19位数的三种银行卡)</small>
          <el-button type="primary" icon="el-icon-circle-plus" circle @click="bankShow=true">添加</el-button>
          <el-dialog title="绑定银行卡" :visible.sync="bankShow" center :modal-append-to-body='false'>
            <el-form>
              <el-form-item label="银行卡的类型" :label-width="formLabelWidth">
                <el-select v-model="cardName" filterable placeholder="请选择">
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label-width="formLabelWidth">
                <span slot="label">{{realName}}的银行卡</span>
                <el-input
                  type="text"
                  placeholder="请输入本人16-19位的银行卡"
                  v-model="myCard"
                  maxlength="19"
                  show-word-limit
                >
                </el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="bankShow=false">取 消</el-button>
              <el-button type="primary" @click="bankShow=false" v-on:click="bindBank">绑 定</el-button>
            </div>
          </el-dialog>
        </h3>
        <br>
        <el-collapse v-model="activeNames" @change="handleChange">
          <el-collapse-item title="中国建设银行" name="1" v-if="myBankCardOne!=='null' ">
           <div>
             你的卡号: {{ myBankCardOne }} &nbsp;<el-button type="danger" @click="deleteOne" round size="mini">解绑</el-button>
           </div>
            <div>
              有待完善......
            </div>
          </el-collapse-item>
          <el-collapse-item title="中国工商银行" name="2" v-if="myBankCardTwo!=='null'">
            <div>
              你的卡号: {{ myBankCardTwo }} &nbsp;<el-button type="danger" @click="deleteTwo" round size="mini">解绑</el-button>
            </div>
            <div>
              有待完善......
            </div>
          </el-collapse-item>
          <el-collapse-item title="中国银行" name="3" v-if="myBankCardThree!=='null'">
            <div>
              你的卡号: {{ myBankCardThree }} &nbsp;<el-button type="danger" @click="deleteThree" round size="mini">解绑</el-button>
            </div>
            <div>
              有待完善......
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>
  </div>
</template>
<script>
    export default {
        inject: ['reload'],
        data() {
            return {
                bankName: window.localStorage.getItem('myBankRealName'),
                CCB: '中国建设银行',
                ICB: '中国工商银行',
                CNB: '中国银行',
                realName: window.localStorage.getItem('myName'),
                myBankCards: window.localStorage.getItem('myBankCards'),
                myBankCardOne: window.localStorage.getItem('myBankCardOne'),
                myBankCardTwo: window.localStorage.getItem('myBankCardTwo'),
                myBankCardThree: window.localStorage.getItem('myBankCardThree'),
                activeNames: ['1'],
                bankShow: false,
                formLabelWidth: '100px',
                myCard: '',
                options: [{
                    value: '中国建设银行',
                    label: '中国建设银行'
                }, {
                    value: '中国工商银行',
                    label: '中国工商银行'
                }, {
                    value: '中国银行',
                    label: '中国银行'
                }],
                cardName: '',
            };
        },
        methods: {
            handleChange(val) {
                console.log(val);
            },
            //绑定银行卡
            bindBank() {
                let bankPattern = /^[0-9]{19}$/;
                if (this.cardName === '' || this.myCard === '' || !bankPattern.test(this.myCard)) {
                    this.$message({
                        type: "error",
                        message: '绑定规则不符合',
                    });
                } else {
                    this.$http.post('/whc/blog-customer-user/bindBankFeign', {
                        cardName: this.cardName,
                        myCard: this.myCard,
                    }).then(res => {
                        console.log(res);
                        if (res.data.success===true){
                            this.$notify({
                                title: '成功',
                                message: '银行卡绑定成功',
                                type: 'success',
                            });
                            let total=0;
                            if (res.data.sign.bankcardOnesign===1){
                                total++;
                            }
                            if (res.data.sign.bankcardTwosign===1){
                                total++;
                            }
                            if (res.data.sign.bankcardThreesign===1){
                                total++;
                            }
                            //保存银行卡的数量
                            window.localStorage.setItem('myBankCards',total);
                            //保存银行卡号
                            window.localStorage.setItem('myBankCardOne',res.data.bank.bankcardOne);
                            window.localStorage.setItem('myBankCardTwo',res.data.bank.bankcardTwo);
                            window.localStorage.setItem('myBankCardThree',res.data.bank.bankcardThree);
                            this.reload();
                        }else {
                            this.$message({
                                type: "error",
                                message: res.data.message,
                            });
                        }
                    })
                }
            },
            //解绑中国建设银行银行卡
            deleteOne() {
                this.$prompt('请输入支付密码', '解除绑定中国建设银行卡', {
                    inputType: 'password',
                    roundButton: true,
                    center: true,
                    inputPlaceholder: '六位有效数字',
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern: /^[0-9]{6}$/,
                    inputErrorMessage: '密码格式不正确'
                }).then(({ value }) => {
                    this.$http.post('/whc/blog-customer-user/deleteBankOneFeign',{
                        CCB: this.CCB,
                        payPassword: value,
                    }).then(res =>{
                        console.log(res);
                        if (res.data.success === true){
                            this.$notify({
                                title: '成功',
                                message: res.data.message,
                                type: 'success',
                            });
                            let total=0;
                            if (res.data.sign.bankcardOnesign===1){
                                total++;
                            }
                            if (res.data.sign.bankcardTwosign===1){
                                total++;
                            }
                            if (res.data.sign.bankcardThreesign===1){
                                total++;
                            }
                            //保存银行卡的数量
                            window.localStorage.setItem('myBankCards',total);
                            //保存银行卡号
                            window.localStorage.setItem('myBankCardOne',res.data.bankcardOne);
                            window.localStorage.setItem('myBankCardTwo',res.data.bankcardTwo);
                            window.localStorage.setItem('myBankCardThree',res.data.bankcardThree);
                            this.reload();
                        }else {
                            this.$message({
                                type: 'error',
                                message: res.data.message
                            });
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '取消解绑!'
                    });
                });
            },
            //解绑中国工商银行卡
            deleteTwo() {
                this.$prompt('请输入支付密码', '解除绑定中国工商银行卡', {
                    inputType: 'password',
                    roundButton: true,
                    center: true,
                    inputPlaceholder: '六位有效数字',
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern: /^[0-9]{6}$/,
                    inputErrorMessage: '密码格式不正确'
                }).then(({ value }) => {
                    this.$http.post('/whc/blog-customer-user/deleteBankTwoFeign',{
                        ICB: this.ICB,
                        payPassword: value,
                    }).then(res =>{
                        console.log(res);
                        if (res.data.success === true){
                            this.$notify({
                                title: '成功',
                                message: res.data.message,
                                type: 'success',
                            });
                            let total=0;
                            if (res.data.sign.bankcardOnesign===1){
                                total++;
                            }
                            if (res.data.sign.bankcardTwosign===1){
                                total++;
                            }
                            if (res.data.sign.bankcardThreesign===1){
                                total++;
                            }
                            //保存银行卡的数量
                            window.localStorage.setItem('myBankCards',total);
                            //保存银行卡号
                            window.localStorage.setItem('myBankCardOne',res.data.bankcardOne);
                            window.localStorage.setItem('myBankCardTwo',res.data.bankcardTwo);
                            window.localStorage.setItem('myBankCardThree',res.data.bankcardThree);
                            this.reload();
                        }else {
                            this.$message({
                                type: 'error',
                                message: res.data.message
                            });
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '取消解绑!'
                    });
                });
            },
            deleteThree() {
                this.$prompt('请输入支付密码', '解除绑定中国银行卡', {
                    inputType: 'password',
                    roundButton: true,
                    center: true,
                    inputPlaceholder: '六位有效数字',
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern: /^[0-9]{6}$/,
                    inputErrorMessage: '密码格式不正确'
                }).then(({ value }) => {
                    this.$http.post('/whc/blog-customer-user/deleteBankThreeFeign',{
                        CNB: this.CNB,
                        payPassword: value,
                    }).then(res =>{
                        console.log(res);
                        if (res.data.success === true){
                            this.$notify({
                                title: '成功',
                                message: res.data.message,
                                type: 'success',
                            });
                            let total=0;
                            if (res.data.sign.bankcardOnesign===1){
                                total++;
                            }
                            if (res.data.sign.bankcardTwosign===1){
                                total++;
                            }
                            if (res.data.sign.bankcardThreesign===1){
                                total++;
                            }
                            //保存银行卡的数量
                            window.localStorage.setItem('myBankCards',total);
                            //保存银行卡号
                            window.localStorage.setItem('myBankCardOne',res.data.bankcardOne);
                            window.localStorage.setItem('myBankCardTwo',res.data.bankcardTwo);
                            window.localStorage.setItem('myBankCardThree',res.data.bankcardThree);
                            this.reload();
                        }else {
                            this.$message({
                                type: 'error',
                                message: res.data.message
                            });
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '取消解绑!'
                    });
                });
            }

        }
    }
</script>
