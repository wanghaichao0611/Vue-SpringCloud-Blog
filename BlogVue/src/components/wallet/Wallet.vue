<template>
  <div class="container">
        <div class="row clearfix">
          <div class="col-md-12 column">
        <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
          <div class="layout">
            <Layout>
              <Header>
                <Menu mode="horizontal" theme="dark" active-name="1">
                  <div class="layout-logo">
                    <router-link :to="{name: 'MyWallet'}">
                      <el-image src="../../../static/img/pay/wallet.png"/>
                    </router-link>
                  </div>
                  <div class="layout-nav">
                    <MenuItem name="1">
                      <Icon type="ios-paper" />
                      <router-link :to="{name: 'Recommend'}" target="_blank" style="text-decoration: none;color: white">博客中心</router-link>
                    </MenuItem>
                    <MenuItem name="2">
                      <Icon type="ios-people" />
                      <router-link :to="{name: 'Home'}" target="_blank" style="text-decoration: none;color: white">博客首页</router-link>
                    </MenuItem>
                    <Submenu name="3">
                      <template slot="title">
                        <el-avatar :src="userAvatar" style="width: 60px;height: 60px"></el-avatar>
                        您好
                      </template>
                      <div style="padding: 10px;background: #f8f8f9">
                        <Card  :padding="0" shadow style="width: 300px;">
                            <Cell title="注销">
                              <el-link :underline="false" style="color: black" v-on:click="loginOutShow">注销登录</el-link>
                            </Cell>
                        </Card>
                      </div>
                    </Submenu>
                  </div>
                </Menu>
              </Header>
              <Layout :style="{padding: '0 50px'}">
                <Divider orientation="left">欢迎~<span v-html="blogName">{{ blogName }}</span>~来到支付中心</Divider>
                <Content :style="{padding: '24px 0', minHeight: '580px', background: '#fff'}">
                  <Layout>
                    <Sider hide-trigger :style="{background: '#fff'}">
                      <Menu active-name="1-1" theme="light" width="auto" :open-names="['0']" accordion>
                        <Submenu name="1">
                          <template slot="title">
                            <Icon type="md-briefcase" />
                            钱包首页
                          </template>
                          <MenuItem name="1-1">
                            <router-link :to="{name: 'MyWallet'}" style="color: #2c3e50;text-decoration: none">钱包首页</router-link>
                          </MenuItem>
                        </Submenu>
                        <Submenu name="2">
                          <template slot="title">
                            <Icon type="ios-navigate"></Icon>
                            账户总览
                          </template>
                          <MenuItem name="2-1"><router-link :to="{name: 'Account'}"  style="color: #2c3e50;text-decoration: none">账户余额</router-link></MenuItem>
                          <MenuItem name="2-2"><router-link :to="{name: 'SecuritySet'}" style="color: #2c3e50;text-decoration: none">密码设置</router-link></MenuItem>
                        </Submenu>
                        <Submenu name="3">
                          <template slot="title">
                            <Icon type="ios-keypad"></Icon>
                            订单管理
                          </template>
                          <MenuItem name="3-1"><router-link :to="{name: 'Cost'}" style="color: #2c3e50;text-decoration: none">费用账单</router-link></MenuItem>
                        </Submenu>
                        <Submenu name="4">
                          <template slot="title">
                            <Icon type="ios-analytics"></Icon>
                            兑换中心
                          </template>
                          <MenuItem name="4-1"><router-link :to="{name: 'Coupon'}" style="color: #2c3e50;text-decoration: none">可兑换券</router-link></MenuItem>
                          <MenuItem name="4-2"><router-link :to="{name: 'CouponInvalid'}" style="color: #2c3e50;text-decoration: none">已失效券</router-link></MenuItem>
                        </Submenu>
                        <Submenu name="5">
                          <template slot="title">
                            <i class="el-icon-bank-card"></i>
                            银行卡管理
                          </template>
                          <MenuItem name="5-1">
                            <router-link :to="{name:'BankCard'}" style="color: #2c3e50;text-decoration: none">银行卡</router-link>
                          </MenuItem>
                        </Submenu>
                      </Menu>
                    </Sider>
                    <Content :style="{padding: '24px', minHeight: '280px', background: '#fff'}">
                      <router-view/>
                    </Content>
                  </Layout>
                </Content>
              </Layout>
              <Footer class="layout-footer-center">
                <a class="alert-link" href="http://www.beian.miit.gov.cn/">豫ICP备19033675号</a>
              </Footer>
            </Layout>
          </div>
        </nav>
      </div>
    </div>
  </div>
</template>
<style scoped>
  .layout{
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
  }
  .layout-logo{
    width: 100px;
    height: 30px;
    border-radius: 3px;
    float: left;
    position: relative;
    top: 15px;
    left: 20px;
  }
  .layout-nav{
    width: 420px;
    margin: 0 auto;
    margin-right: 20px;
  }
  .layout-footer-center{
    text-align: center;
  }
</style>
<script>
    export default {
        data() {
            return{
                userAvatar: window.localStorage.getItem('userPicture'),
                theme1: 'light',
                blogName: window.localStorage.getItem('blogName'),
            }
        },
        methods: {
            //注销登录
            loginOutShow() {
                window.localStorage.clear();
                this.$router.go(0);
            },
        }
    }
</script>

