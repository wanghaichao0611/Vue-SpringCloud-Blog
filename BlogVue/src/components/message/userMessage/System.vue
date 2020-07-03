<template>
  <div class="container">
  <div class="row clearfix">
      <Card style="width: 910px;"  :bordered="false"  >
        <Spin size="large" fix v-if="sysSpinShow"></Spin>
        <Scroll height="450">
        <div  style="text-align:center" >
          <List>
            <ListItem v-for="(item,index) in sysData" :key="item.systemTitle">
              <ListItemMeta >
                <template slot="title">
                  <div class="text-left">
                    <strong>{{ item.systemTitle}}&nbsp;&nbsp;</strong>
                    <small>{{item.createtime}}</small>
                  </div>
                </template>
                <br>
                <br>
                <template slot="description">
                  <p align="left">{{ item.systemMsg }}</p>
                </template>
              </ListItemMeta>
            </ListItem>
          </List>
        </div>
        </Scroll>
      </Card>
    </div>
  </div>
</template>
<script>
    export default {
        inject: ['reload'],
        data(){
            return {
                sysSpinShow: true,
                sysData: [
                    {
                        id: '',
                        userId: '',
                        systemTitle: '',
                        systemMsg: '',
                        readOn: '',
                        createtime: '',
                    }
                ]
            }
        },
        created() {
            //查询所有系统通知
            this.selectAllSysMsg();
        },
        methods:{
            //查询所有系统通知
            selectAllSysMsg(){
                this.$http.post('/whc/blog-customer-user/selectAllSysMsgFeign')
                    .then(res => {
                        if(res.data.success===true){
                            this.sysData=res.data.list;
                            this.sysSpinShow=false;
                        }
                    })
            },
        },
        mounted() {
        }
    }
</script>
