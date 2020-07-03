<template>
  <div>
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
      <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
            background-color="#E0FFFF"
            text-color="#000000"
            active-text-color="#000000">
            <el-menu-item index="1"><router-link :to="{name: 'Mine'}" target="_parent">首页</router-link></el-menu-item>
            <el-menu-item index="2" ><router-link :to="{name: 'Article'}" target="_blank">博客首页</router-link></el-menu-item>
            <el-menu-item index="3"><el-link href="https://github.com/" target="_blank">GitHub官网</el-link></el-menu-item>
            <el-menu-item index="4" class="el-icon-money"><router-link to="/member" target="_blank"> VIP大会员</router-link></el-menu-item>
            <el-menu-item index="5"><el-link href="https://www.csdn.net/" target="_blank"> CSDN论坛</el-link></el-menu-item>
            <el-submenu index="6">
              <template slot="title">博客分类</template>
              <el-menu-item index="6-1"><router-link :to="{name: 'Test'}" target="_blank" style="color: #2c3e50">测试</router-link></el-menu-item>
              <el-menu-item index="6-2"><router-link :to="{name: 'Operation'}" target="_blank" style="color: #2c3e50">运维</router-link></el-menu-item>
              <el-menu-item index="6-3"><router-link :to="{name: 'FrontEnd'}" target="_blank" style="color: #2c3e50">前端</router-link></el-menu-item>
              <el-menu-item index="6-4"><router-link :to="{name: 'BackEnd'}" target="_blank" style="color: #2c3e50">后端</router-link></el-menu-item>
              <el-menu-item index="6-5"><router-link :to="{name: 'FullBack'}" target="_blank" style="color: #2c3e50">全栈</router-link></el-menu-item>
              <el-menu-item index="6-6"><router-link :to="{name: 'Framework'}" target="_blank" style="color: #2c3e50">架构</router-link></el-menu-item>
              <el-submenu index="6-7">
                <template slot="title">language</template>
                <el-menu-item index="6-7-1"><router-link :to="{name: 'C'}" target="_blank" style="color: #2c3e50">C</router-link></el-menu-item>
                <el-menu-item index="6-7-2"><router-link :to="{name: 'CPlus'}" target="_blank" style="color: #2c3e50">C++</router-link></el-menu-item>
                <el-menu-item index="6-7-3"><router-link :to="{name: 'Java'}" target="_blank" style="color: #2c3e50">Java</router-link></el-menu-item>
                <el-menu-item index="6-7-4"><router-link :to="{name: 'PHP'}" target="_blank" style="color: #2c3e50">PHP</router-link></el-menu-item>
                <el-menu-item index="6-7-5"><router-link :to="{name: 'Python'}" target="_blank" style="color: #2c3e50">Python</router-link></el-menu-item>
                <el-menu-item index="6-7-6"><router-link :to="{name: 'Go'}" target="_blank" style="color: #2c3e50">GO</router-link></el-menu-item>
              </el-submenu>
            </el-submenu>
            <el-menu-item index="7"><router-link to="/home" target="_blank"> 热门博客排行榜</router-link></el-menu-item>
      <el-menu-item index="21" disabled></el-menu-item>
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
            <el-submenu index="14">
              <template slot="title">博客管理中心</template>
              <el-menu-item index="14-1">发表博客</el-menu-item>
              <el-menu-item index="14-2">收藏博客</el-menu-item>
              <el-menu-item index="14-3">管理博客</el-menu-item>
            </el-submenu>
            <el-menu-item index="19" disabled></el-menu-item>
            <el-submenu index="15">
              <template slot="title"><el-image :src="SecurityrightShow" style="width: 50px;height: 50px"></el-image></template>
              <el-menu-item index="15-1"><router-link to="/sign" target="_blank" style="color: black">签到中心</router-link></el-menu-item>
              <el-menu-item index="15-2"><router-link to="/wallet" target="_blank" style="color: black">我的钱包</router-link></el-menu-item>
              <el-menu-item index="15-3"><el-link :underline="false" style="color: black" v-on:click="loginOutShow">注销登录</el-link></el-menu-item>
            </el-submenu>
          </el-menu>
    </nav>
    <el-col :span="4">
      <el-menu
        default-active="2-1"
        class="el-menu-vertical-demo"
        @open="handleOpen"
        @close="handleClose"
        background-color="#E0FFFF"
        text-color="#000000"
        active-text-color="#000000">
      <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-setting"></i>
            <span>账号设置</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="1-1" class="el-icon-unlock">&nbsp;
              <router-link :to="{name: 'Change'}">修改密码</router-link>
            </el-menu-item>
            <el-menu-item index="1-2" class="el-icon-check">&nbsp;
              <router-link :to="{name: 'Reset'}">重置密码</router-link>
            </el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group>
            <el-menu-item index="1-3" class="el-icon-message">
              <router-link :to="{name: 'Email'}">&nbsp;我的邮箱</router-link>
            </el-menu-item>
            <el-menu-item index="1-4" class="el-icon-mobile-phone">&nbsp;
              <router-link :to="{name: 'Phone'}">我的手机</router-link>
            </el-menu-item>
          </el-menu-item-group>

          <el-submenu index="1-5">
            <template slot="title">
              <i class="el-icon-s-tools"></i>
              <span>安全认证</span>
            </template>
            <el-menu-item index="1-5-1" class="el-icon-user-solid">&nbsp;
              <router-link :to="{name: 'IdCard'}">实名注册</router-link>
            </el-menu-item>
            <el-menu-item index="1-5-2" class="el-icon-bicycle">&nbsp;
              <router-link :to="{name: 'SchoolCard'}">校园认证</router-link>
            </el-menu-item>
          </el-submenu>
        </el-submenu>

        <el-submenu index="2">
          <template slot="title">
            <i class="el-icon-lock"></i>
            <span>安全中心</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="2-1" class="el-icon-reading">&nbsp;
              <el-button type="text" @click="anquantishi = true">安全求助</el-button>

              <el-dialog
                title="友情提示"
                :visible.sync="anquantishi"
                width="30%"
                center>
                <el-tree
                  :data="anquan"
                  accordion
                  @node-click="handleNodeClick">
                </el-tree>
                <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="anquantishi = false">明 白</el-button>
              </span>
              </el-dialog>
            </el-menu-item>
            <el-menu-item index="2-2" class="el-icon-edit-outline">&nbsp;
              <router-link :to="{name: 'Appeal'}">安全申诉</router-link>
            </el-menu-item>
          </el-menu-item-group>
        </el-submenu>

        <el-submenu index="3">
          <template slot="title">
            <i class="el-icon-document-add"></i>
            <span>我的博客</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="3-1" class="el-icon-unlock">&nbsp;
              <router-link :to="{name: 'ChangeBlog'}">博客信息</router-link>
            </el-menu-item>
            <el-menu-item index="3-2" class="el-icon-check">&nbsp;
              <!-- 个人主页 -->
              <el-button type="text" @click="person = true"> 查看博客主页</el-button>

              <el-dialog title="个人主页" :visible.sync="person">
                  <router-link target="_blank" to="/blogMain"  ><i class="el-icon-s-opportunity"></i>点击跳转博客主页</router-link>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="person = false">取 消</el-button>
                  <el-button type="primary" @click="person = false">确 定</el-button>
                </div>
              </el-dialog>
            </el-menu-item>
          </el-menu-item-group>
        </el-submenu>

        <el-submenu index="4">
          <template slot="title">
            <i class="el-icon-loading"></i>
            <span> 消息中心</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="4-1" class="el-icon-s-opportunity">&nbsp;
              <!-- 消息中心 -->
              <el-button type="text" @click="systemmessage = true">系统通知</el-button>

              <el-dialog title="系统通知" :visible.sync="systemmessage">
                <el-form>
                <el-link href="/userMsg/system" target="_blank" class="el-icon-message-solid">跳转到系统通知</el-link>
                </el-form>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="systemmessage = false">取 消</el-button>
                  <el-button type="primary" @click="systemmessage = false">确 定</el-button>
                </div>
              </el-dialog>
            </el-menu-item>
            <el-menu-item index="4-2" class="el-icon-s-comment">&nbsp;
              <!-- 私人消息 -->
              <el-button type="text" @click="personmessage = true">私人通知</el-button>

              <el-dialog title="私人消息" :visible.sync="personmessage">
                <el-form>
                  <el-link href="/userMsg/myMessage" target="_blank" class="el-icon-light-rain">跳转到你的私人中心</el-link>
                </el-form>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="personmessage = false">取 消</el-button>
                  <el-button type="primary" @click="personmessage = false">确 定</el-button>
                </div>
              </el-dialog>
            </el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="5">
          <template slot="title">
            <i class="el-icon-upload"></i>
            <span> 私密空间</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="5-1" class="el-icon-s-opportunity">&nbsp;
              <!-- 私密中心 -->
              <el-button type="text" @click="cloud = true">博客云</el-button>

              <el-dialog title="收货地址" :visible.sync="cloud">
                <el-form>
                  <el-button type="text" @click="openCloud">点击进入你的博客云</el-button>
                </el-form>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="cloud = false">取 消</el-button>
                  <el-button type="primary" @click="cloud = false">确 定</el-button>
                </div>
              </el-dialog>
            </el-menu-item>
            <el-menu-item index="5-2" class="el-icon-s-comment">&nbsp;
              <!-- 我的相册的Form -->
              <el-button type="text" @click="picture = true">我的相册</el-button>

              <el-drawer
                title="欢迎来到我的相册"
                :visible.sync="picture"
                size="50%">
                <div>
                  <el-button @click="pictureInner = true" >打开你的相册</el-button>
                  <el-drawer
                    title="添加你的图片"
                    :append-to-body="true"
                    :before-close="handleClosePicture"
                    :visible.sync="pictureInner">
                    <span>看到了吗？</span>
                  </el-drawer>
                </div>
              </el-drawer>
            </el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="6">
          <template slot="title">
            <i class="el-icon-female"></i>
            <span> 客服中心</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="6-1" class="el-icon-s-flag">&nbsp;
              <!-- Form -->
              <el-button type="text" @click="kefufankui = true">客服反馈</el-button>

              <el-dialog title="你的意见是对我们最大的进步" :visible.sync="kefufankui">
                <el-form>
                  <div style="margin: 20px 0;"></div>
                  <el-input
                    type="textarea"
                    placeholder="请输入内容"
                    v-model="kefufankuineirong"
                    maxlength="100"
                    show-word-limit
                    rows="5"
                  >
                  </el-input>
                </el-form>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="kefufankui = false">取 消</el-button>
                  <el-button type="primary" @click="kefufankui = false">确 定</el-button>
                </div>
              </el-dialog>
            </el-menu-item>
            <el-menu-item index="6-2" class="el-icon-chat-square">&nbsp;
              <!-- Form -->
              <el-button type="text" @click="kefuOnline = true">在线聊天</el-button>

              <el-drawer
                title="欢饮来到聊天室 !"
                :before-close="handleCloseOnline"
                :visible.sync="kefuOnline"
                direction="rtl"
                custom-class="demo-drawer"
                ref="drawer"
              >
                <div class="demo-drawer__content">
                  <el-form>
                    <el-form-item :label-width="formLabelWidth">
                      <el-link href="/" target="_blank" class="el-icon-message" style="color: blue">跳转到你的聊天室</el-link>
                    </el-form-item>
                  </el-form>
                  <div class="demo-drawer__footer">
                  </div>
                  <el-button @click="kefuOnline = false">关 闭</el-button>
                </div>
              </el-drawer>
            </el-menu-item>
          </el-menu-item-group>
        </el-submenu>
      </el-menu>
    </el-col>
    <router-view/>
  </div>
</template>
<style>

  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 691px;
  }
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

  body {background: url('../../../static/img/person/personMain.gif');}


</style>
<script>
    export default {
        inject: ['reload'],
        data() {
            return {
                //学校的信息
                mySchoolName: window.localStorage.getItem('mySchoolName'),
                mySchoolCard: window.localStorage.getItem('mySchoolCard'),
                mySchool: window.localStorage.getItem('mySchool'),
                myBlogName: window.localStorage.getItem('blogName'),
                SecurityrightShow: window.localStorage.getItem('userPicture'),
                activeIndex: '1',
                formLabelWidth: '100px',

                //搜索框
                restaurants: [],
                state: '',
                timeout: null,

                //安全提示
                anquantishi: true,
                anquan: [{
                    label: '信息安全',
                    children: [{
                        label: '不要向他人泄露个人信息',
                        children: [{
                            label: '不要看黄网'
                        }]
                    }]
                }, {
                    label: '约法三章',
                    children: [{
                        label: '用户言行',
                        children: [{
                            label: '不准辱骂他人以及引战'
                        }]
                    }, {
                        label: '个人信用',
                        children: [{
                            label: '不可以盗用别人的博客发表文章'
                        }]
                    }, {
                        label: '学术歧视',
                        children: [{
                            label: '人人都是程序员'
                        }]
                    }]
                }, {
                    label: '充值业务',
                    children: [{
                        label: '学生全场优惠',
                        children: [{
                            label: '所有消费均返还10%金额券'
                        }]
                    }, {
                        label: '下单成功随机立减红包',
                        children: [{
                            label: '暂时只添加了支付宝后续再说'
                        }]
                    }]
                }],

                //个人主页
                person: false,

                //系统通知
                systemmessage: false,

                //私人通知
                personmessage: false,

                //博客云
                cloud: false,

                //我的相册
                picture: false,
                pictureInner: false,

                //反馈内容
                kefufankui: false,
                kefufankuineirong: '',

                //在线聊天
                kefuOnline: false,

            };
        },
        methods: {
            handleSelect(key, keyPath) {
                console.log(key, keyPath);
            },
            handleOpen(key, keyPath) {
                console.log(key, keyPath);
            },
            handleClose(key, keyPath) {
                console.log(key, keyPath);
            },
            handleNodeClick(data) {
                console.log(data);
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
            openCloud() {
                this.$prompt('请输入你的邮箱', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
                    inputErrorMessage: '邮箱格式不正确'
                }).then(({ value }) => {
                    this.$message({
                        type: 'success',
                        message: '欢迎'+ value +'进入博客云'
                    });
                    this.$router.push('/');
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '进入失败'
                    });
                });
            },
            handleClosePicture(done) {
                this.$confirm('还有未保存的工作哦确定关闭吗？')
                    .then(_ => {
                        done();
                    })
                    .catch(_ => {});
            },
            handleCloseOnline(done) {
                this.$confirm('确定要关闭窗口吗？')
                    .then(_ => {
                        this.loading = true;
                        setTimeout(() => {
                            this.loading = false;
                            done();
                        }, 2000);
                    })
                    .catch(_ => {});
            },
            //注销登录
            loginOutShow() {
                window.localStorage.clear();
                this.$router.go(0);
            }
        },
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
