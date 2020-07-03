<template>
 <div class="container">
   <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
     <el-menu
       :default-active="activeIndex"
       class="el-menu-demo"
       mode="horizontal"
       @select="handleSelect"
       background-color="#f8f8f9"
       text-color="#FFFFFF"
       active-text-color="Transparent" >
       <el-menu-item index="1">
         <router-link :to="{name: 'HomePage'}" style="text-decoration: none;color: #2c3e50">
           <Img src="../../../static/img/firstMain/blogLogo.PNG" style="width: 50px;height: 50px;"></Img>
         </router-link>
       </el-menu-item>
       <el-menu-item index="19" disabled></el-menu-item>
       <el-menu-item index="2" ><router-link :to="{name: 'Recommend'}" style="text-decoration: none;color: #2c3e50"  target="_blank">博客大全</router-link></el-menu-item>
       <el-menu-item index="19" disabled></el-menu-item>
       <el-submenu index="6">
         <template slot="title"><span style="color: #2c3e50">博客分类</span></template>
         <el-menu-item index="6-1"><router-link :to="{name: 'Test'}" target="_blank" style="text-decoration: none;color: #2c3e50">测试</router-link></el-menu-item>
         <el-menu-item index="6-2"><router-link :to="{name: 'Operation'}" target="_blank" style="text-decoration: none;color: #2c3e50">运维</router-link></el-menu-item>
         <el-menu-item index="6-3"><router-link :to="{name: 'FrontEnd'}" target="_blank"  style="text-decoration: none;color: #2c3e50">前端</router-link></el-menu-item>
         <el-menu-item index="6-4"><router-link :to="{name: 'BackEnd'}" target="_blank" style="text-decoration: none;color: #2c3e50">后端</router-link></el-menu-item>
         <el-menu-item index="6-5"><router-link :to="{name: 'FullBack'}" target="_blank" style="text-decoration: none;color: #2c3e50">全栈</router-link></el-menu-item>
         <el-menu-item index="6-6"><router-link :to="{name: 'Framework'}" target="_blank" style="text-decoration: none;color: #2c3e50">架构</router-link></el-menu-item>
         <el-submenu index="6-7">
           <template slot="title"><span style="color: #2c3e50">language</span></template>
           <el-menu-item index="6-7-1"><router-link :to="{name: 'C'}" target="_blank" style="text-decoration: none;color: #2c3e50">C</router-link></el-menu-item>
           <el-menu-item index="6-7-2"><router-link :to="{name: 'CPlus'}" target="_blank" style="text-decoration: none;color: #2c3e50">C++</router-link></el-menu-item>
           <el-menu-item index="6-7-3"><router-link :to="{name: 'Java'}" target="_blank" style="text-decoration: none;color: #2c3e50">Java</router-link></el-menu-item>
           <el-menu-item index="6-7-4"><router-link :to="{name: 'PHP'}" target="_blank" style="text-decoration: none;color: #2c3e50">PHP</router-link></el-menu-item>
           <el-menu-item index="6-7-5"><router-link :to="{name: 'Python'}" target="_blank" style="text-decoration: none;color: #2c3e50">Python</router-link></el-menu-item>
           <el-menu-item index="6-7-6"><router-link :to="{name: 'Go'}" target="_blank" style="text-decoration: none;color: #2c3e50">GO</router-link></el-menu-item>
         </el-submenu>
       </el-submenu>
       <el-menu-item index="19" disabled></el-menu-item>
       <el-menu-item index="3"><router-link :to="{name: 'Power'}" style="text-decoration: none;color: #2c3e50"  target="_blank"> VIP大会员</router-link></el-menu-item>
       <el-menu-item index="16" disabled></el-menu-item>
       <el-menu-item index="8"><router-link :to="{ name: 'Mine'}" target="_blank" style="text-decoration: none;color: #2c3e50">安全中心</router-link></el-menu-item>
       <el-menu-item index="16" disabled></el-menu-item>
       <el-menu-item index="9"><router-link :to="{ name: 'UserMsg'}" target="_blank" style="text-decoration: none;color: #2c3e50">消息中心</router-link></el-menu-item>
       <el-menu-item index="16" disabled></el-menu-item>
       <el-menu-item index="10"><router-link :to="{ name: 'Mine'}" target="_blank" style="text-decoration: none;color: #2c3e50">客服中心</router-link></el-menu-item>
       <el-menu-item index="11" disabled></el-menu-item>
       <el-menu-item index="12">
         <el-autocomplete
           v-model="state"
           :fetch-suggestions="querySearchAsync"
           placeholder="查找文章"
           @select="handleSelectAll"
         ></el-autocomplete>
       </el-menu-item>
       <el-submenu index="14">
         <template slot="title"><span style="color: #2c3e50">博客个人中心</span></template>
         <el-menu-item index="14-1"><router-link :to="{name: 'UserPublish'}" style="text-decoration: none;color: #2c3e50" target="_blank">发表博客</router-link></el-menu-item>
         <el-menu-item index="14-2"><router-link :to="{name: 'UserManage'}" style="text-decoration: none;color: #2c3e50" target="_blank">博客管理</router-link></el-menu-item>
         <el-menu-item index="14-3"><router-link :to="{name: 'UserCollection'}" style="text-decoration: none;color: #2c3e50" target="_blank">收藏订阅</router-link></el-menu-item>
       </el-submenu>
       <el-menu-item index="19" disabled></el-menu-item>
       <el-submenu index="15">
         <template slot="title">
           <Avatar :src="HomePageShow" style="width: 50px;height: 50px"></Avatar>
         </template>
           <Card  :padding="0" shadow style="width: 300px;">
             <div class="text-center" slot="title" v-html="blogName">{{ blogName }}</div>
             <br>
             <CellGroup>
               <Cell title="签到中心" extra="提升等级" to="/home/sign" target="_blank"/>
               <Cell title="我的消息" label="与我有关"  to="/userMsg/reply"  target="_blank">
                 <Badge :count="msgOffTotal" slot="extra" />
               </Cell>
               <Cell title="我的钱包" extra="我的余额" to="/wallet/myWallet" target="_blank" />
               <Cell title="个人资料" extra="修改安全" to="/person/mine" target="_blank" />
               <Cell title="注销">
                 <el-link :underline="false" style="color: black" v-on:click="loginOutShow">注销登录</el-link>
               </Cell>
             </CellGroup>
           </Card>
       </el-submenu>
     </el-menu>
   </nav>
  <div class="row clearfix">
    <Content :style="{padding: '24px 0', minHeight: '580px',backgroundColor: 'Transparent'}">
      <Layout :style="{backgroundColor: 'Transparent'}">
        <Content :style="{padding: '24px', minHeight: '280px',backgroundColor: 'Transparent'}">
          <router-view/>
        </Content>
      </Layout>
    </Content>
  </div>
   <BackTop :height="50" :bottom="50">
     <div class="top"><Icon type="ios-arrow-dropup-circle" /></div>
   </BackTop>
 </div>
</template>
<style>

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .top{
    padding: 1px;
    background: black;
    color: #fff;
    text-align: center;
    border-radius: 2px;
  }

  body {background: url('../../../static/img/blogTwoMain/blogShowMain.jpg');}

</style>
<script>
    export default {
        inject: ['reload'],
        data(){
            return {
                msgOffTotal: '',
                blogName: window.localStorage.getItem('blogName'),
                HomePageShow: window.localStorage.getItem('userPicture'),
                activeIndex: '1',
                formLabelWidth: '100px',

                login: window.localStorage.getItem('token'),

                //搜索框
                restaurants: [],
                state: '',
                timeout: null,
            }
        },
        computed:{

        },
        methods: {
            loginShow() {
                this.$router.to({name: 'Login'});
            },
            loginOutShow() {
                this.login=null;
                window.localStorage.clear();
                this.$router.go(0);
            },
            handleSelect(key, keyPath) {
                console.log(key, keyPath);
            },
            handleChangeCascader(val) {
                console.log(val);
            },
            loadAll() {
                return [
                    {"value": "三全鲜食（北新泾店）", "address": "我也不知道"},
                    {"value": "Hot honey 首尔炸鸡（仙霞路）", "address": "上海市长宁区淞虹路661号"},
                    {"value": "新旺角茶餐厅", "address": "上海市普陀区真北路988号创邑金沙谷6号楼113"},
                    {"value": "泷千家(天山西路店)", "address": "天山西路438号"},
                    {"value": "胖仙女纸杯蛋糕（上海凌空店）", "address": "上海市长宁区金钟路968号1幢18号楼一层商铺18-101"},
                    {"value": "贡茶", "address": "上海市长宁区金钟路633号"},
                    {"value": "豪大大香鸡排超级奶爸", "address": "上海市嘉定区曹安公路曹安路1685号"},
                    {"value": "茶芝兰（奶茶，手抓饼）", "address": "上海市普陀区同普路1435号"},
                    {"value": "十二泷町", "address": "上海市北翟路1444弄81号B幢-107"},
                    {"value": "星移浓缩咖啡", "address": "上海市嘉定区新郁路817号"},
                    {"value": "阿姨奶茶/豪大大", "address": "嘉定区曹安路1611号"},
                    {"value": "新麦甜四季甜品炸鸡", "address": "嘉定区曹安公路2383弄55号"},
                    {"value": "Monica摩托主题咖啡店", "address": "嘉定区江桥镇曹安公路2409号1F，2383弄62号1F"},
                    {"value": "浮生若茶（凌空soho店）", "address": "上海长宁区金钟路968号9号楼地下一层"},
                    {"value": "NONO JUICE  鲜榨果汁", "address": "上海市长宁区天山西路119号"},
                    {"value": "CoCo都可(北新泾店）", "address": "上海市长宁区仙霞西路"},
                    {"value": "快乐柠檬（神州智慧店）", "address": "上海市长宁区天山西路567号1层R117号店铺"},
                    {"value": "Merci Paul cafe", "address": "上海市普陀区光复西路丹巴路28弄6号楼819"},
                    {"value": "猫山王（西郊百联店）", "address": "上海市长宁区仙霞西路88号第一层G05-F01-1-306"},
                    {"value": "枪会山", "address": "上海市普陀区棕榈路"},
                    {"value": "纵食", "address": "元丰天山花园(东门) 双流路267号"},
                    {"value": "钱记", "address": "上海市长宁区天山西路"},
                    {"value": "壹杯加", "address": "上海市长宁区通协路"},
                    {"value": "唦哇嘀咖", "address": "上海市长宁区新泾镇金钟路999号2幢（B幢）第01层第1-02A单元"},
                    {"value": "爱茜茜里(西郊百联)", "address": "长宁区仙霞西路88号1305室"},
                    {"value": "爱茜茜里(近铁广场)", "address": "上海市普陀区真北路818号近铁城市广场北区地下二楼N-B2-O2-C商铺"},
                    {"value": "鲜果榨汁（金沙江路和美广店）", "address": "普陀区金沙江路2239号金沙和美广场B1-10-6"},
                    {"value": "开心丽果（缤谷店）", "address": "上海市长宁区威宁路天山路341号"},
                    {"value": "超级鸡车（丰庄路店）", "address": "上海市嘉定区丰庄路240号"},
                    {"value": "妙生活果园（北新泾店）", "address": "长宁区新渔路144号"},
                    {"value": "香宜度麻辣香锅", "address": "长宁区淞虹路148号"},
                    {"value": "凡仔汉堡（老真北路店）", "address": "上海市普陀区老真北路160号"},
                    {"value": "港式小铺", "address": "上海市长宁区金钟路968号15楼15-105室"},
                    {"value": "蜀香源麻辣香锅（剑河路店）", "address": "剑河路443-1"},
                    {"value": "北京饺子馆", "address": "长宁区北新泾街道天山西路490-1号"},
                    {"value": "饭典*新简餐（凌空SOHO店）", "address": "上海市长宁区金钟路968号9号楼地下一层9-83室"},
                    {"value": "焦耳·川式快餐（金钟路店）", "address": "上海市金钟路633号地下一层甲部"},
                    {"value": "动力鸡车", "address": "长宁区仙霞西路299弄3号101B"},
                    {"value": "浏阳蒸菜", "address": "天山西路430号"},
                    {"value": "四海游龙（天山西路店）", "address": "上海市长宁区天山西路"},
                    {"value": "樱花食堂（凌空店）", "address": "上海市长宁区金钟路968号15楼15-105室"},
                    {"value": "壹分米客家传统调制米粉(天山店)", "address": "天山西路428号"},
                    {"value": "福荣祥烧腊（平溪路店）", "address": "上海市长宁区协和路福泉路255弄57-73号"},
                    {"value": "速记黄焖鸡米饭", "address": "上海市长宁区北新泾街道金钟路180号1层01号摊位"},
                    {"value": "红辣椒麻辣烫", "address": "上海市长宁区天山西路492号"},
                    {"value": "(小杨生煎)西郊百联餐厅", "address": "长宁区仙霞西路88号百联2楼"},
                    {"value": "阳阳麻辣烫", "address": "天山西路389号"},
                    {"value": "南拳妈妈龙虾盖浇饭", "address": "普陀区金沙江路1699号鑫乐惠美食广场A13"}
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
            handleClosePicture(done) {
                this.$confirm('还有未保存的工作哦确定关闭吗？')
                    .then(_ => {
                        done();
                    })
                    .catch(_ => {});
            },
            //查询未读消息总数
            selectMsgOff(){
                this.$http.post('/whc/blog-customer-user/selectMsgOffFeign')
                    .then(res => {
                        this.msgOffTotal=res.data.msgTotal;
                    })
            }
        },
        created(){
            //查询未读消息总数
            this.selectMsgOff();
        },
        //外聚函数
        mounted() {
            //搜索函数
            this.restaurants = this.loadAll();
            //遍历函数
            this.list = this.states.map(item => {
                return { value: item, label: item };
            });
        }
    }
</script>
