<template>
  <div class="container">
    <div class="row clearfix">
  <Card style="width: 100%;" dis-hover>
    <Alert type="primary"><h3 style="color: black">博客分类搜索</h3></Alert>
    <el-input placeholder="请输入你想知道的关键字" v-model="esInput" class="searchClass">
      <div slot="prepend">
        <div class="centerClass">
          <el-select v-model="categoryName" placeholder="分类" style="width: 90px">
            <el-option label="C" value="c"></el-option>
            <el-option label="C++" value="cPlus"></el-option>
            <el-option label="Java" value="java"></el-option>
            <el-option label="PHP" value="php"></el-option>
            <el-option label="Python" value="python"></el-option>
            <el-option label="Go" value="go"></el-option>
            <el-option label="测试" value="test"></el-option>
            <el-option label="运维" value="operation"></el-option>
            <el-option label="前端" value="frontEnd"></el-option>
            <el-option label="后端" value="backEnd"></el-option>
            <el-option label="全栈" value="fullStack"></el-option>
            <el-option label="架构" value="framework"></el-option>
          </el-select>
        </div>
        <div class="centerClass">
          <div class="line"></div>
        </div>
      </div>
      <el-button slot="append" icon="el-icon-search" v-on:click="flushHomeEs"></el-button>
    </el-input>
    <Divider/>
    <Alert v-if="total===0" type="error" show-icon>未找到结果,请重新输入关键字!</Alert>
    <div style="text-align:center">
  <List item-layout="vertical">
    <ListItem v-for="(item,index) in searchAll"  :key="item.articleId">
      <ListItemMeta>
      <template slot="title">
        <div class="text-left" v-html="item.title"></div>
      </template>
      <template slot="description">
        <div class="text-center" v-html="item.summary"></div>
      </template>
      </ListItemMeta>
      <template slot="action">
        <li>
          <span style="color: #2c3e50">分类的区域</span>&nbsp;
          <el-link :href="categoryUrl+item.category" type="primary" target="_blank" :underline="false">
            <Tag type="border" color="blue" size="large" v-if="item.category==='test'">测试</Tag>
            <Tag type="border" color="blue" size="large" v-if="item.category==='operation'">运维</Tag>
            <Tag type="border" color="blue" size="large" v-if="item.category==='frontEnd'">前端</Tag>
            <Tag type="border" color="blue" size="large" v-if="item.category==='backEnd'">后端</Tag>
            <Tag type="border" color="blue" size="large" v-if="item.category==='fullStack'">全栈</Tag>
            <Tag type="border" color="blue" size="large" v-if="item.category==='framework'">架构</Tag>
            <Tag type="border" color="blue" size="large" v-if="item.category==='c'">C</Tag>
            <Tag type="border" color="blue" size="large" v-if="item.category==='cPlus'">C++</Tag>
            <Tag type="border" color="blue" size="large" v-if="item.category==='java'">Java</Tag>
            <Tag type="border" color="blue" size="large" v-if="item.category==='php'">PHP</Tag>
            <Tag type="border" color="blue" size="large" v-if="item.category==='python'">Python</Tag>
            <Tag type="border" color="blue" size="large" v-if="item.category==='go'">Go</Tag>
          </el-link>
        </li>
        <li>
          开始发布于{{item.articlePublishDate}}
        </li>
        <li>
          最后修改于{{item.articleUpdateDate}}
        </li>
        <li>
          <el-link :href="blogRealUrl+item.articleId" type="primary" :underline="false" target="_blank">浏览</el-link>
        </li>
      </template>
    </ListItem>
  </List>
      <Divider/>
    </div>
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page"
        :page-sizes="[5, 10, 20, 30]"
        :page-size="count"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </Card>
  </div>
  </div>
</template>
<style>
  .searchClass{
    border: 1px solid #c5c5c5;
    border-radius: 20px;
    background: #fff;
  }

  .searchClass .el-input-group__prepend {
    border: none;
    background-color: transparent;
    padding: 0 10px 0 30px;
  }

  .searchClass .el-input-group__append {
    border: none;
    background-color: transparent;
  }

  .searchClass .el-input__inner {
    height: 36px;
    line-height: 36px;
    border: none;
    background-color: transparent;
  }

  .searchClass .el-icon-search{
    font-size: 16px;
  }

  .searchClass .centerClass {
    height: 100%;
    line-height: 100%;
    display: inline-block;
    vertical-align: middle;
    text-align: right;
  }

  .searchClass .line {
    width: 1px;
    height: 39px;
    background-color: #ffffff;
    margin-left: 14px;
  }

  .searchClass:hover{
    border: 1px solid #c5c5c5;
    background: #fff;
  }

  .searchClass:hover .line {
    background-color: #D5E3E8;
  }

  .searchClass:hover .el-icon-search{
    color: #409eff;
    font-size: 16px;
  }

</style>
<script>
    export default {
        inject: ['reload'],
        data(){
            return {
                esInput: '',
                categoryName: '',
                //page代表从第几条开始，count代表查询多少条,类似mysql的limit分页参数
                page: 0,
                count: 5,
                total: '',
                category: this.$route.query.categoryName,
                blogRealUrl: 'http://localhost:8088/home/details/',
                categoryUrl: 'http://localhost:8088/blogMain/',
                searchAll: [
                    {
                        articleId: '',
                        articlePublishDate: '',
                        articleUpdateDate: '',
                        summary: '',
                        title: '',
                    }
                ]
            }
        },
        created() {
            this.categoryName=this.$route.query.categoryName;
            this.esInput=this.$route.query.keyWord;
            this.esPageAll(this.page,this.count,this.$route.query.categoryName,this.$route.query.keyWord);
        },
        methods: {
            //主页搜索重置搜索刷新
            flushHomeEs(){
                //输入内容的长度
                let patterInput=/^.{1,20}$/;
                if (this.categoryName === '' || this.esInput === '') {
                    alert('分类和内容均不能为空!');
                }else if (!patterInput.test(this.esInput)){
                    alert('分类的内容需在1-20个字体之内!');
                }
                else {
                    let routeUrl = this.$router.resolve({
                        name: "ArticleSearch",
                        query: {
                            categoryName: this.categoryName,
                            keyWord: this.esInput,
                        }
                    });
                    this.$router.push(routeUrl.href);
                    this.reload();
                }
            },
            //page代表从第几条开始，count代表查询多少条,类似mysql的limit分页参数
            handleSizeChange: function (size) {
                this.page=0;
                this.count = size;
                console.log(this.count); //每页下拉显示数据
                this.esPageAll(this.page,size,this.$route.query.categoryName,this.$route.query.keyWord);
            },
            handleCurrentChange: function(currentPage){
                //page代表从第几条开始，count代表查询多少条,类似mysql的limit分页参数
                if (this.page===this.count){
                    //返回的参数
                    this.page=this.count*(currentPage-1);
                    this.esPageAll(this.page,this.count,this.$route.query.categoryName,this.$route.query.keyWord);
                }else {
                    this.page = this.count;
                    let con = this.count * currentPage;
                    console.log(this.page);  //点击第几页
                    this.esPageAll(this.page, con, this.$route.query.categoryName, this.$route.query.keyWord);
                }
            },
            //分页查询搜索的结果,查找数据量之差。
            esPageAll(page,count,categoryName,keyWord){
                this.$http.post('/whc/blog-customer-user/esPageAllFeign',{
                    page: page,
                    count: count,
                    categoryName: categoryName,
                    keyWord: keyWord,
                }).then(res =>{
                    console.log(res);
                    if (res.data.success === true){
                        this.searchAll=res.data.searchAll;
                        this.total=res.data.total
                    } else {
                        alert("微服务开了小查,请稍后再试!")
                    }
                })
            }
        }
    }
</script>
