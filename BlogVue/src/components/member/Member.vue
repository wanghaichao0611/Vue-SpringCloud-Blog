<template>
  <div class="col-md-12 column">
  <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal"@select="handleSelect"
             text-color="#000000">
      <el-menu-item disabled></el-menu-item>
      <el-menu-item disabled></el-menu-item>
      <el-menu-item disabled></el-menu-item>
      <el-menu-item index="1">
        <router-link :to="{name: 'Power'}" style="text-decoration: none;color: #2c3e50">
          <i class="el-icon-money"></i>VIP充值中心
        </router-link>
      </el-menu-item>
      <el-menu-item disabled></el-menu-item>
      <el-menu-item disabled></el-menu-item>
      <el-menu-item disabled></el-menu-item>
      <el-menu-item index="2">
        <router-link :to="{name: 'Svip'}" style="text-decoration: none;color: #2c3e50">超级会员</router-link>
      </el-menu-item>
      <el-menu-item disabled></el-menu-item>
      <el-menu-item disabled></el-menu-item>
      <el-menu-item index="3">
      <router-link :to="{name: 'Vip'}" style="text-decoration: none;color: #2c3e50" >普通会员</router-link>
      </el-menu-item>
      <el-menu-item disabled></el-menu-item>
      <el-menu-item disabled></el-menu-item>
      <el-menu-item index="4">
      <router-link :to="{name: 'Power'}" style="text-decoration: none;color: #2c3e50" >会员信息</router-link>
      </el-menu-item>
      <el-menu-item disabled></el-menu-item>
      <el-menu-item disabled></el-menu-item>
      <el-menu-item index="5">
        <router-link to="/pay" target="_blank"><button class="btn btn-info btn-sm" type="button">开通博客会员</button>
        </router-link>
      </el-menu-item>
      <el-menu-item disabled></el-menu-item>
      <el-menu-item index="6"><router-link :to="{name: 'Article'}" style="text-decoration: none;color: #2c3e50" target="_blank" >博客主页</router-link></el-menu-item>
      <el-menu-item index="7"><el-avatar src="../../../static/img/pay/vip.png"/>
      </el-menu-item>
      <el-submenu index="8">
        <template slot="title"><span v-html="blogName">{{ blogName }}</span></template>
        <el-menu-item index="8-1"><router-link to="/person" target="_blank" style="color: black">个人中心</router-link></el-menu-item>
        <el-menu-item index="8-2"><router-link to="/wallet" target="_blank" style="color: black">我的钱包</router-link></el-menu-item>
        <el-menu-item index="8-3"><el-link :underline="false" style="color: black" v-on:click="loginOutShow">注销登录</el-link></el-menu-item>
      </el-submenu>
    </el-menu>
    <router-view/>
  </nav>
  </div>
</template>
<script>
    export default {
        inject: ['reload'],
        data() {
            return {
                formLabelWidth: '100px',
                showResult: false,
                switchValue: true,
                blogName: window.localStorage.getItem('blogName'),
                activeIndex: '1',
            };
        },
        methods: {
            handleSelect(key, keyPath) {
                console.log(key, keyPath);
            },
            //注销登录
            loginOutShow() {
                window.localStorage.clear();
                this.$router.go(0);
            }
        }
    }
</script>
