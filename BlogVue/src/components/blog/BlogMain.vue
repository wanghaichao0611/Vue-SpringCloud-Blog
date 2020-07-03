<template>
  <div>
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
      <el-menu
        :default-active="activeIndex2"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#FFFFFF">
        <el-menu-item index="1"><router-link to="/" target="_parent">首页</router-link></el-menu-item>
        <el-menu-item index="21" disabled></el-menu-item>
        <el-menu-item index="3"><el-link href="https://github.com/" target="_blank">GitHub官网</el-link></el-menu-item>
        <el-menu-item index="21" disabled></el-menu-item>
        <el-menu-item index="5"><el-link href="https://www.csdn.net/" target="_blank"> CSDN论坛</el-link></el-menu-item>
        <el-menu-item index="16" disabled></el-menu-item>
        <el-menu-item index="17" ><router-link to="/person" target="_blank">安全中心</router-link></el-menu-item>
        <el-menu-item index="19" disabled></el-menu-item>
        <el-menu-item index="17" ><router-link to="/member" target="_blank">会员中心</router-link></el-menu-item>
        <el-menu-item index="16" disabled></el-menu-item>
        <el-menu-item index="17" disabled></el-menu-item>
        <el-menu-item index="19" disabled></el-menu-item>
        <el-menu-item index="16" disabled></el-menu-item>
        <el-menu-item index="17" disabled></el-menu-item>
        <el-menu-item index="22">
          <el-autocomplete
            v-model="state"
            :fetch-suggestions="querySearchAsync"
            placeholder="查找文章"
            @select="handleSelectAll"
          ></el-autocomplete>
        </el-menu-item>
        <el-menu-item index="16" disabled></el-menu-item>
        <el-menu-item index="17" disabled></el-menu-item>
        <el-menu-item index="16" disabled></el-menu-item>
        <el-menu-item index="7"><router-link to="/userMain/userPublish" target="_blank">博客管理中心</router-link></el-menu-item>
        <el-menu-item index="17" disabled></el-menu-item>
        <el-submenu index="15">
          <template slot="title"><el-image :src="SecurityrightShow" style="width: 50px;height: 50px"></el-image></template>
          <el-menu-item index="15-1"><router-link :to="{name: 'Sign'}" target="_blank" style="color: white">签到中心</router-link></el-menu-item>
          <el-menu-item index="15-2"><router-link :to="{name: 'MyWallet'}" target="_blank" style="color: white">我的钱包</router-link></el-menu-item>
          <el-menu-item index="15-3"><el-link :underline="false" style="color: white" v-on:click="loginOutShow">注销登录</el-link></el-menu-item>
        </el-submenu>
      </el-menu>
    </nav>
      <div class="col-md-1 column">
        <div class="btn-group btn-group-vertical btn-group-lg" style="width: 100px;height: 200px">
          <br>
          <router-link :to="{name: 'Recommend'}" class="btn btn-default" plain>推荐 <Icon type="ios-sunny-outline" /></router-link>
          <router-link :to="{name: 'BlogVip'}" class="btn btn-default" plain>VIP <Icon type="md-arrow-forward" /></router-link>
          <router-link :to="{name: 'C'}" class="btn btn-default" plain>C <Icon type="md-arrow-forward" /></router-link>
          <router-link :to="{name: 'CPlus'}" class="btn btn-default" plain>C++ <Icon type="md-arrow-forward" /></router-link>
          <router-link :to="{name: 'Java'}" class="btn btn-default" plain>Java <Icon type="md-arrow-forward" /></router-link>
          <router-link :to="{name: 'PHP'}" class="btn btn-default" plain>PHP <Icon type="md-arrow-forward" /></router-link>
          <router-link :to="{name: 'Python'}" class="btn btn-default" plain>Python <Icon type="md-arrow-forward" /></router-link>
          <router-link :to="{name: 'Go'}" class="btn btn-default" plain>GO <Icon type="md-arrow-forward" /></router-link>
          <router-link :to="{name: 'Test'}" class="btn btn-default" plain>测试 <Icon type="md-arrow-forward" /></router-link>
          <router-link :to="{name: 'Operation'}" class="btn btn-default" plain>运维 <Icon type="md-arrow-forward" /></router-link>
          <router-link :to="{name: 'FrontEnd'}" class="btn btn-default" plain>前端 <Icon type="md-arrow-forward" /></router-link>
          <router-link :to="{name: 'BackEnd'}" class="btn btn-default" plain>后端 <Icon type="md-arrow-forward" /></router-link>
          <router-link :to="{name: 'FullStack'}" class="btn btn-default" plain>全找 <Icon type="md-arrow-forward" /></router-link>
          <router-link :to="{name: 'Framework'}" class="btn btn-default" plain>架构 <Icon type="md-arrow-forward" /></router-link>
        </div>
      </div>
      <div class="col-md-11 column">
        <br>
          <Content :style="{padding: '24px 0', minHeight: '580px',backgroundColor: 'Transparent'}">
            <Layout :style="{backgroundColor: 'Transparent'}">
              <Content :style="{padding: '24px', minHeight: '280px',backgroundColor: 'Transparent'}">
                <router-view/>
              </Content>
            </Layout>
          </Content>
      </div>
    </div>
</template>
<style>
  body {background: url('../../../static/img/manageBlog/categoryMain.gif');}
</style>
<script>
    export default {
        data() {
            return {
                blogName: window.localStorage.getItem('blogName'),
                SecurityrightShow: window.localStorage.getItem('userPicture'),
                activeIndex2: '0',
                activeNames: ['1'],
                restaurants: [],
                state: '',
                timeout:  null,
            }
        },
        methods: {
            //注销登录
            loginOutShow() {
                window.localStorage.clear();
                this.$router.go(0);
            },
            loadAll() {
                return [
                    { "value": "三全鲜食（北新泾店）", "address": "长宁区新渔路144号" },
                    { "value": "Hot honey 首尔炸鸡（仙霞路）", "address": "上海市长宁区淞虹路661号" },
                    { "value": "新旺角茶餐厅", "address": "上海市普陀区真北路988号创邑金沙谷6号楼113" },
                    { "value": "泷千家(天山西路店)", "address": "天山西路438号" },
                    { "value": "胖仙女纸杯蛋糕（上海凌空店）", "address": "上海市长宁区金钟路968号1幢18号楼一层商铺18-101" },
                    { "value": "贡茶", "address": "上海市长宁区金钟路633号" },
                    { "value": "豪大大香鸡排超级奶爸", "address": "上海市嘉定区曹安公路曹安路1685号" },
                    { "value": "茶芝兰（奶茶，手抓饼）", "address": "上海市普陀区同普路1435号" },
                    { "value": "十二泷町", "address": "上海市北翟路1444弄81号B幢-107" },
                    { "value": "星移浓缩咖啡", "address": "上海市嘉定区新郁路817号" },
                    { "value": "阿姨奶茶/豪大大", "address": "嘉定区曹安路1611号" },
                    { "value": "新麦甜四季甜品炸鸡", "address": "嘉定区曹安公路2383弄55号" },
                    { "value": "Monica摩托主题咖啡店", "address": "嘉定区江桥镇曹安公路2409号1F，2383弄62号1F" },
                    { "value": "浮生若茶（凌空soho店）", "address": "上海长宁区金钟路968号9号楼地下一层" },
                    { "value": "NONO JUICE  鲜榨果汁", "address": "上海市长宁区天山西路119号" },
                    { "value": "CoCo都可(北新泾店）", "address": "上海市长宁区仙霞西路" },
                    { "value": "快乐柠檬（神州智慧店）", "address": "上海市长宁区天山西路567号1层R117号店铺" },
                    { "value": "Merci Paul cafe", "address": "上海市普陀区光复西路丹巴路28弄6号楼819" },
                    { "value": "猫山王（西郊百联店）", "address": "上海市长宁区仙霞西路88号第一层G05-F01-1-306" },
                    { "value": "枪会山", "address": "上海市普陀区棕榈路" },
                    { "value": "纵食", "address": "元丰天山花园(东门) 双流路267号" },
                    { "value": "钱记", "address": "上海市长宁区天山西路" },
                    { "value": "壹杯加", "address": "上海市长宁区通协路" },
                    { "value": "唦哇嘀咖", "address": "上海市长宁区新泾镇金钟路999号2幢（B幢）第01层第1-02A单元" },
                    { "value": "爱茜茜里(西郊百联)", "address": "长宁区仙霞西路88号1305室" },
                    { "value": "爱茜茜里(近铁广场)", "address": "上海市普陀区真北路818号近铁城市广场北区地下二楼N-B2-O2-C商铺" },
                    { "value": "鲜果榨汁（金沙江路和美广店）", "address": "普陀区金沙江路2239号金沙和美广场B1-10-6" },
                    { "value": "开心丽果（缤谷店）", "address": "上海市长宁区威宁路天山路341号" },
                    { "value": "超级鸡车（丰庄路店）", "address": "上海市嘉定区丰庄路240号" },
                    { "value": "妙生活果园（北新泾店）", "address": "长宁区新渔路144号" },
                    { "value": "香宜度麻辣香锅", "address": "长宁区淞虹路148号" },
                    { "value": "凡仔汉堡（老真北路店）", "address": "上海市普陀区老真北路160号" },
                    { "value": "港式小铺", "address": "上海市长宁区金钟路968号15楼15-105室" },
                    { "value": "蜀香源麻辣香锅（剑河路店）", "address": "剑河路443-1" },
                    { "value": "北京饺子馆", "address": "长宁区北新泾街道天山西路490-1号" },
                    { "value": "饭典*新简餐（凌空SOHO店）", "address": "上海市长宁区金钟路968号9号楼地下一层9-83室" },
                    { "value": "焦耳·川式快餐（金钟路店）", "address": "上海市金钟路633号地下一层甲部" },
                    { "value": "动力鸡车", "address": "长宁区仙霞西路299弄3号101B" },
                    { "value": "浏阳蒸菜", "address": "天山西路430号" },
                    { "value": "四海游龙（天山西路店）", "address": "上海市长宁区天山西路" },
                    { "value": "樱花食堂（凌空店）", "address": "上海市长宁区金钟路968号15楼15-105室" },
                    { "value": "壹分米客家传统调制米粉(天山店)", "address": "天山西路428号" },
                    { "value": "福荣祥烧腊（平溪路店）", "address": "上海市长宁区协和路福泉路255弄57-73号" },
                    { "value": "速记黄焖鸡米饭", "address": "上海市长宁区北新泾街道金钟路180号1层01号摊位" },
                    { "value": "红辣椒麻辣烫", "address": "上海市长宁区天山西路492号" },
                    { "value": "(小杨生煎)西郊百联餐厅", "address": "长宁区仙霞西路88号百联2楼" },
                    { "value": "阳阳麻辣烫", "address": "天山西路389号" },
                    { "value": "南拳妈妈龙虾盖浇饭", "address": "普陀区金沙江路1699号鑫乐惠美食广场A13" }
                ];
            },
            querySearchAsync(queryString, cb) {
                var restaurants = this.restaurants;
                var results = queryString ? restaurants.filter(this.createStateFilter(queryString)) : restaurants;

                clearTimeout(this.timeout);
                this.timeout = setTimeout(() => {
                    cb(results);
                }, 3000 * Math.random());
            },
            createStateFilter(queryString) {
                return (state) => {
                    return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
                };
            },
            handleSelectAll(item) {
                console.log(item);
            },
            handleSelect(item) {
                console.log(item);
            },
            handleClearCurrentRow () {
                this.$refs.currentRowTable.clearCurrentRow();
            }
        },
        mounted() {
            this.restaurants = this.loadAll();
        }
    };
</script>
