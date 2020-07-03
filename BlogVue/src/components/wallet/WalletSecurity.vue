<template>
  <div class="container">
    <div class="row clearfix">
      <div class="col-md-12 column">
        <h1>我的钱包开通的注意事项</h1>
        <el-divider></el-divider>
        <ol>
          <li>
            开通钱包方可使用,并且已经完成实名注册
          </li>
          <li>
            注意保护个人的隐私,切勿泄露支付密码给他人
          </li>
          <li>
            最终解释权为本作者所有
          </li>
        </ol>
        <el-divider></el-divider>
        <span>协议大全有空再弥补,绿色则代表同意</span>
        <br>
        <el-switch
          v-model="passwordOn"
          active-color="#13ce66"
          inactive-color="#ff4949">
        </el-switch>
        <el-divider></el-divider>
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="密码" prop="walletPassword">
            <el-input type="password" v-model="ruleForm.walletPassword" maxlength="6" show-word-limit autocomplete="off" ></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="walletPasswordAgain">
            <el-input type="password" v-model="ruleForm.walletPasswordAgain" maxlength="6" show-word-limit autocomplete="off" ></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="realName">
            <el-input v-model="ruleForm.realName" maxlength="20" show-word-limit placeholder="姓名不能为空且必须为实名注册的姓名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" v-on:click="setPassword">提交</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-form>

      </div>
    </div>
  </div>
</template>
<script>
    export default {
        data() {
            var checkName = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('姓名不能为空且必须为实名注册的姓名'));
                }

            };
            var validatePassword = (rule, value, callback) => {
                let namePattern=/^[0-9]{6}$/;
                if (value === '' || !namePattern.test(value)) {
                    callback(new Error('六位数字密码'));
                } else {
                    if (this.ruleForm.walletPasswordAgain !== '') {
                        this.$refs.ruleForm.validateField('walletPasswordAgain');
                    }
                    callback();
                }
            };
            var validatePasswordAgain = (rule, value, callback) => {
                let namePatternAgain=/^[0-9]{6}$/;
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (value !== this.ruleForm.walletPassword) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                passwordOn: false,
                ruleForm: {
                    walletPassword: '',
                    walletPasswordAgain: '',
                    realName: ''
                },
                rules: {
                    walletPassword: [
                        { validator: validatePassword, trigger: 'blur' }
                    ],
                    walletPasswordAgain: [
                        { validator: validatePasswordAgain, trigger: 'blur' }
                    ],
                    realName: [
                        { validator: checkName, trigger: 'blur' }
                    ]
                }
            };
        },
        methods: {
            setPassword(){
                if (this.passwordOn===false){
                    this.$message.error('确认打勾协议规则!');
                }else {
                    this.$http.post('/whc/blog-customer-user/setWalletFeign', {
                        walletPassword: this.ruleForm.walletPassword,
                        realName: this.ruleForm.realName,
                    }).then(res => {
                        console.log(res);
                        if (res.data.success === true) {
                            this.$router.replace('/wallet');
                        } else {
                            this.$message.error('姓名要为实名注册的名字!');
                        }
                    })
                }
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        }
    }
</script>
