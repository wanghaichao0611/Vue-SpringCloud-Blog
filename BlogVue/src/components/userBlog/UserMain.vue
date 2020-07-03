<template>
  <div>
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
        <el-menu
          :default-active="activeIndex"
          class="el-menu-demo"
          mode="horizontal"
          @select="handleSelectUser"
          router="true"
          background-color="#000000"
          text-color="#FFFFFF"
          active-text-color="Transparent">
          <el-menu-item disabled></el-menu-item>
          <el-menu-item ><router-link target="_blank" style="text-decoration: none"  :to="{name: 'Article'}">博客主页</router-link></el-menu-item>
          <el-menu-item disabled></el-menu-item>
          <el-menu-item ><router-link target="_blank" style="text-decoration: none"  :to="{name: 'Recommend'}">博客分类主页</router-link></el-menu-item>
          <el-menu-item disabled></el-menu-item>
          <el-menu-item ><el-link style="text-decoration: none" href="https://github.com/" target="_blank">GitHub官网</el-link></el-menu-item>
          <el-menu-item disabled></el-menu-item>
          <el-menu-item ><el-link style="text-decoration: none" href="https://www.csdn.net/" target="_blank">CSDN论坛</el-link></el-menu-item>
          <el-menu-item disabled></el-menu-item>
          <el-menu-item disabled></el-menu-item>
          <el-menu-item disabled></el-menu-item>
          <el-menu-item disabled></el-menu-item>
          <el-menu-item disabled></el-menu-item>
          <el-menu-item >
            <Button type="info"  ghost>
            <router-link style="text-decoration: none" :to="{name: 'UserPublish'}">发表博客</router-link>
          </Button>
          </el-menu-item>
          <el-menu-item disabled></el-menu-item>
          <el-menu-item>
            <Button type="info"  ghost>
            <router-link style="text-decoration: none" :to="{name: 'UserManage'}">管理博客</router-link>
            </Button>
          </el-menu-item>
          <el-menu-item disabled></el-menu-item>
          <el-menu-item>
            <Button type="info"  ghost>
            <router-link style="text-decoration: none" :to="{name: 'UserCollection'}">博客收藏</router-link>
            </Button>
          </el-menu-item>
          <el-menu-item disabled></el-menu-item>
          <el-submenu index="2">
            <template slot="title"><el-image :src="UserMainShow" style="width: 50px;height: 50px"></el-image></template>
            <div style="padding: 10px;background: #f8f8f9">
              <Card title="欢迎充钱!!!" :padding="0" shadow style="width: 300px;">
                <CellGroup>
                  <Cell title="个人资料" extra="资料设置" to="/person/mine" target="_blank"/>
                  <Cell title="注销">
                    <el-link :underline="false" style="color: black" v-on:click="loginOutShow">注销登录</el-link>
                  </Cell>
                </CellGroup>
              </Card>
            </div>
          </el-submenu>
        </el-menu>
        </nav>
      <Content :style="{padding: '24px 0', minHeight: '580px',backgroundColor: 'Transparent'}">
        <Layout :style="{backgroundColor: 'Transparent'}">
          <Content :style="{padding: '24px', minHeight: '280px',backgroundColor: 'Transparent'}">
            <router-view/>
          </Content>
        </Layout>
      </Content>
    </div>
</template>
<style>
  body {background-color: rgba(100, 249, 244, 0.5);}
</style>
  <script>
      export default {
          inject: ['reload'],
          data() {
              return {
                  theme: 'light',
                  UserMainShow: window.localStorage.getItem('userPicture'),
                  activeIndex: '1'
              };
          },
          methods: {
              //注销登录
              loginOutShow() {
                  window.localStorage.clear();
                  this.$router.go(0);
              },
              handleSelectUser(key, keyPath) {
                  console.log(key, keyPath);
              }
          }
      }
  </script>
