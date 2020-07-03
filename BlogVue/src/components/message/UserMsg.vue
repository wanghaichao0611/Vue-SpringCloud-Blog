<template>
    <div class="row clearfix">
      <div class="col-md-12 column">
        <div class="layout">
          <Layout>
            <Sider ref="side1" hide-trigger  :collapsed-width="78" v-model="isCollapsed">
              <div class="layout-logo-left">
                <Menu active-name="1-2" theme="dark" width="auto" :class="menuitemClasses">
                  <MenuItem name="1-1" disabled>
                    <span><Strong>消息中心</Strong></span>
                  </MenuItem>
                  <Divider/>
                  <MenuItem name="1-2">
                    <Icon type="ios-chatbubbles" />
                    <span>
                      <el-badge :value="commentMsg" :max="99">
                      <router-link :to="{name: 'Reply'}" style="text-decoration: none;color: white;">
                        <el-button type="text" @click="updateCommentMsgOn" style="color: white" size="medium">回复我的</el-button>
                      </router-link>
                      </el-badge>
                    </span>
                  </MenuItem>
                  <MenuItem name="1-3">
                    <Icon type="md-at" />
                    <span>
                      <el-badge :value="atMsg" :max="99">
                      <router-link :to="{name: 'At'}" style="text-decoration: none;color: white;">
                        <el-button type="text" @click="updateAtMsgOn" style="color: white" size="medium">@&nbsp;&nbsp;&nbsp;我的</el-button>
                      </router-link>
                      </el-badge>
                    </span>
                  </MenuItem>
                  <MenuItem name="1-4">
                    <Icon type="ios-thumbs-up" />
                    <span>
                      <el-badge :value="thumpMsg" :max="99">
                      <router-link :to="{name: 'Love'}" style="text-decoration: none;color: white;">
                        <el-button type="text" @click="updateThumpMsgOn" style="color: white" size="medium">收到的赞</el-button>
                      </router-link>
                      </el-badge>
                    </span>
                  </MenuItem>
                  <MenuItem name="1-5">
                    <Icon type="ios-paper-plane" />
                    <span>
                      <el-badge :value="systemMsg" :max="99">
                        <router-link :to="{name: 'System'}"  style="text-decoration: none;color: white;" >
                          <el-button type="text" @click="updateSysMsgOn" style="color: white" size="medium">系统通知</el-button>
                        </router-link>
                      </el-badge>
                    </span>
                  </MenuItem>
                  <MenuItem name="1-6">
                    <Icon type="md-mail" />
                    <span>
                       <el-badge :value="myMsg" :max="99">
                      <router-link  :to="{name: 'MyMessage'}" style="text-decoration: none;color: white;">
                        <el-button type="text" @click="updateMyMsgOn" style="color: white" size="medium">我的消息</el-button>
                      </router-link>
                       </el-badge>
                    </span>
                  </MenuItem>
                  <MenuItem name="1-7">
                    <Icon type="md-add" />
                    <span>
                      <el-badge :value="friendsMsg" :max="99">
                      <router-link :to="{name: 'Friends'}" style="text-decoration: none;color: white;">
                        <el-button type="text" @click="updateFriendsMsgOn" style="color: white" size="medium">好友通知</el-button>
                      </router-link>
                      </el-badge>
                    </span>
                  </MenuItem>
                  <Divider/>
                  <MenuItem name="1-8">
                    <Icon type="ios-settings" />
                    <span>
                      <router-link :to="{name: 'MessageConfig'}" style="text-decoration: none;color: white;">
                        消息设置
                      </router-link>
                    </span>
                  </MenuItem>
                </Menu>
              </div>
            </Sider>
            <Layout>
              <Header :style="{padding: 0}" class="layout-header-bar">
                <span v-html="blogName">{{ blogName }}</span>
              </Header>
              <Content :style="{margin: '20px', minHeight: '490px'}">
                <router-view/>
              </Content>
            </Layout>
          </Layout>
        </div>
      </div>
    </div>
</template>
<style scoped>
  .layout{
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
  }
  .layout-header-bar{
    background: #fff;
    box-shadow: 0 1px 1px rgba(0,0,0,.1);
  }
  .layout-logo-left{
    width: 90%;
    height: 30px;
    background: #5b6270;
    border-radius: 3px;
    margin: 15px auto;
  }
  .menu-item span{
    display: inline-block;
    width: 100px;
    text-overflow: ellipsis;
    white-space: nowrap;
    vertical-align: bottom;
    transition: width .2s ease .2s;
  }
  .menu-item i{
    transform: translateX(0px);
    transition: font-size .2s ease, transform .2s ease;
    vertical-align: middle;
    font-size: 16px;
  }
  .collapsed-menu span{
    width: 0px;
    transition: width .2s ease;
  }
  .collapsed-menu i{
    transform: translateX(5px);
    transition: font-size .2s ease .2s, transform .2s ease .2s;
    vertical-align: middle;
    font-size: 22px;
  }

  body {background: url('../../../static/img/message/girl.jpg');}

</style>
<script>
    export default {
        data () {
            return {
                blogName: window.localStorage.getItem('blogName'),
                commentMsg: 0,
                atMsg: 0,
                thumpMsg: 0,
                systemMsg: 0,
                myMsg: 0,
                friendsMsg: 0,
                isCollapsed: false,
            }
        },
        computed: {
            menuitemClasses () {
                return [
                    'menu-item',
                    this.isCollapsed ? 'collapsed-menu' : ''
                ]
            }
        },
        created(){
            this.selectEveryOneOff();
        },
        methods: {
            collapsedSider () {
                this.$refs.side1.toggleCollapse();
            },
            //查询未读消息总数(各个消息表的分开未读)
            selectEveryOneOff(){
                this.$http.post('/whc/blog-customer-user/selectEveryOneOffFeign')
                    .then(res => {
                        this.systemMsg=res.data.msgTotal;
                        this.thumpMsg=res.data.thumpTotal;
                    })
            },
            //更新所有回复我的消息为已读
            updateCommentMsgOn(){

            },
            //更新所有At我的消息为已读
            updateAtMsgOn(){

            },
            //更新所有收到的赞为已读
            updateThumpMsgOn(){

            },
            //更新所有系统消息均为已读
            updateSysMsgOn(){
                this.$http.post('/whc/blog-customer-user/updateSysMsgOnFeign')
                    .then(res =>{
                        this.systemMsg=0;
                    })
            },
            //更新我的消息为已读
            updateMyMsgOn(){

            },
            //更新好友通知为已经读
            updateFriendsMsgOn(){

            }
        }
    }
</script>
