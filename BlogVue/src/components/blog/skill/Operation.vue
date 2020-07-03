<template>
  <div class="row clearfix">
    <div align="left">
      <Button type="success" v-on:click="reload()">换一批</Button>
    </div>
    <Divider/>
    <div style="text-align:center">
      <List item-layout="vertical" >
        <ListItem v-for="(item,index) in categoryAll" :key="item.articleTitle">
          <ListItemMeta :title="item.articleTitle" >
            <template slot="title">
              <P class="text-left" style="color: #1989FA">
                <el-link :href="blogRealUrl+item.articleId" type="primary" :underline="false" target="_blank">
                  {{item.articleTitle}}
                </el-link>
              </P>
            </template>
          </ListItemMeta>
          <Strong>{{ item.summary }}</Strong>
          <template slot="action">
            <li>
              <p style="color: red">开始发布于：{{ item.articlePublishDate }}</p>
            </li>
            <li>
              <p style="color: red">最后修改于：{{ item.articleUpdateDate }}</p>
            </li>
          </template>
        </ListItem>
      </List>
    </div>
  </div>
</template>
<script>
    export default {
        inject: ['reload'],
        data () {
            return {
                blogRealUrl: 'http://localhost:8088/home/details/',
                categoryName: 'Operation',
                categoryAll: [
                    {
                        articleId: '',
                        articlePublishDate: '',
                        articleTitle: '',
                        articleUpdateDate: '',
                        summary: '',
                    }
                ]
            }
        },
        created() {
            this.esCategoryAll();
        },
        methods: {
            //刷新
            reload(){
                this.$router.go(0);
            },
            //ES分类搜索主页
            esCategoryAll(){
                this.$http.post('/whc/blog-customer-user/esCategoryAllFeign',{
                    categoryName: this.categoryName,
                }).then(res =>{
                    console.log(res);
                    if (res.data.success === true){
                        this.categoryAll=res.data.list;
                    } else {
                        alert("微服务开了小查,请稍后再试!")
                    }
                })
            },
        }
    }
</script>
