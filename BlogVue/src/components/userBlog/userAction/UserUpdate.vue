<template>
  <div class="row clearfix">
    <div class="col-md-12 column">
      <div class="col-md-10 column">
        <nav class="navbar navbar-default navbar-static-top" role="navigation">
          <Input v-model="articleTitle" type="textarea" maxlength="100" show-word-limit :autosize="{minRows: 2,maxRows: 3}" placeholder="请输入你的文章的标题" />
        </nav>
      </div>
      <div class="col-md-2 column">
        <Button type="error" v-on:click="articleShowClick" long style="height: 50px;">修改文章</Button>
        <el-drawer
          :visible.sync="articleShow"
          :direction="direction"
          :modal-append-to-body='false'
          :show-close="false"
          size="37%">
          <Form :label-width="80">
            <FormItem>
              <div style="border-bottom: 1px solid #e9e9e9;padding-bottom:6px;margin-bottom:6px;">
                <Strong><p align="center" style="color: black">修改文章</p></Strong>
              </div>
              <el-popover
                placement="right"
                width="400"
                trigger="click">
                <el-checkbox-group v-model="articleTag" :min="1" :max="6" >
                  <el-checkbox v-for="city in skills" :label="city" :key="city">{{city}}</el-checkbox>
                </el-checkbox-group>
                <el-button type="text" slot="reference"><Strong style="color: green;"><i class="el-icon-circle-plus"></i> 选择1-6个博客标签</Strong></el-button>
              </el-popover>
            </FormItem>
            <FormItem label="文章标签" prop="文章标签">
              <Tag v-for="item in articleTag" :key="item" :name="item" color="#FFA2D3">{{ item }}</Tag>
            </FormItem>
            <FormItem label="文章分类">
              <label>
                <Select v-model="articleCategory" clearable style="width:200px">
                  <Option v-for="item in categoryList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                </Select>
              </label>
            </FormItem>
            <FormItem label="文章类型">
              <label>
                <Select v-model="articleType" clearable style="width:200px">
                  <Option v-for="item in typeList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                </Select>
              </label>
            </FormItem>
            <FormItem v-if="articleType==='original'">
              <span style="color: red">请尊重原创作者,切勿随意抄袭他人的智慧!!!</span>
            </FormItem>
            <FormItem v-if="articleType==='forward'">
              <Input v-model="forWardUrl" placeholder="请输入原文链接" type="text" clearable>
              </Input>
              <el-tooltip content="我已经认真阅读信息并且遵守规则" placement="top" v-if="forWardUrl!==''">
                <el-switch
                  v-model="forWardSwitch"
                  active-color="#13ce66"
                  inactive-color="#ff4949">
                </el-switch>
              </el-tooltip>
              原文允许转载或者本次转载已经获得原文作者授权
              <p style="color: red">注意：转载请确认原文允许转载，或者您已经获得原文作者授权。转载文章不能设置为VIP可见。</p>
            </FormItem>
            <FormItem v-if="articleType==='translate'">
              <Input placeholder="请输入原文连接" v-model="translateUrl" type="text" clearable>
              </Input>
              <el-tooltip content="我已经认真阅读信息并且遵守规则" placement="top" v-if="translateUrl!==''">
                <el-switch
                  v-model="translateSwitch"
                  active-color="#13ce66"
                  inactive-color="#ff4949">
                </el-switch>
              </el-tooltip>
              原文允许翻译或者本次翻译已经获得原文作者授权
              <p style="color: red">注意：翻译请确认原文允许翻译，或者您已经获得原文作者授权翻译。翻译文章请尽量填写原文链接。</p>
            </FormItem>
            <FormItem label="文章保密">
              <RadioGroup v-model="articleSecurity">
                <Radio label="public" border>公开</Radio>
                <Radio label="private" border>私密</Radio>
                <Radio label="vip"  v-if="articleType!=='forward' && memberSign!=='0'" border>会员</Radio>
              </RadioGroup>
            </FormItem>
            <FormItem>
              <p style="color: red">温馨提示: 请勿发布涉及政治、广告、营销、翻墙、违反国家法律法规等内容</p>
              <br>
              <Button type="error" ghost @click="articleShow=false">退出修改</Button>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <Button type="error" @click="articleShow=false" v-on:click="articleUpdate">修改文章</Button>
            </FormItem>
          </Form>
        </el-drawer>
      </div>
    </div>
    <div class="markdown">
      <mavon-editor v-model="articleContent" ref="md"  @imgAdd="articleImg" @change="change" style="min-height: 500px"/>
    </div>
  </div>
</template>
<style>

</style>
<script>

    import { mavonEditor } from 'mavon-editor'
    import 'mavon-editor/dist/css/index.css'

    const articleTagUpdate=['测试','运维','前端','后端','全栈','架构','C语言','CPlus','Java','PHP','Python','Go','大数据',
        'SQL','Oracle','Mysql','中间件','RPC','JSP','SSH','SSM','爬虫','网络','Spring','SpringMVC','SpringBoot','Http','SpringCloud','微服务','分布式','缓存','面试','码神',
        '大神','汇编','嵌入式','JavaScript','HTML','CSS','恰饭','人工智能AI','高并发','设计模式','Netty','JVM','数据结构','源码'];

    export default {
        inject: ['reload'],
        name: "",
        props: [],
        components: {
            mavonEditor,
        },
        data() {
            return {
                id: '',
                memberSign: window.localStorage.getItem('memberSign'),
                forWardUrl: '',
                forWardSelectHttps: 'https',
                forWardSwitch: false,
                translateUrl: '',
                translateSelectHttps: 'https',
                translateSwitch: false,
                skills: articleTagUpdate,
                direction: 'rtl',
                formLabelWidth: '100px',
                articleContent:'',
                html:'',
                configs: {},
                articleTitle: '',
                articleShow: false,
                articleTag: ['测试'],
                articleSecurity: '',
                styles: {
                    height: 'calc(100% - 55px)',
                    overflow: 'auto',
                    paddingBottom: '53px',
                    position: 'static'
                },
                categoryList: [
                    {
                        value: 'test',
                        label: '测试'
                    },
                    {
                        value: 'operation',
                        label: '运维'
                    },
                    {
                        value: 'frontEnd',
                        label: '前端'
                    },
                    {
                        value: 'backEnd',
                        label: '后端'
                    },
                    {
                        value: 'fullStack',
                        label: '全栈'
                    },
                    {
                        value: 'framework',
                        label: '架构'
                    },
                    {
                        value: 'c',
                        label: 'C'
                    },
                    {
                        value: 'cPlus',
                        label: 'C++'
                    },
                    {
                        value: 'java',
                        label: 'Java'
                    },
                    {
                        value: 'php',
                        label: 'PHP'
                    },
                    {
                        value: 'python',
                        label: 'Python'
                    },
                    {
                        value: 'go',
                        label: 'Go'
                    }
                ],
                articleCategory: '',
                typeList: [
                    {
                        value: 'original',
                        label: '原创',
                    },
                    {
                        value: 'forward',
                        label: '转载',
                    },
                    {
                        value: 'translate',
                        label: '翻译',
                    }
                ],
                articleType: '',
            }
        },
        created(){
            //let articleId=this.$route.params.id;
            //查询标题和文章给予修改
            this.selectTitleMd(this.$route.params.id);
        },
        methods: {
            //获取文章和标题,其它则可以重新修改.
            selectTitleMd(articleId){
                this.$http.post('/whc/blog-customer-user/articleTitleMdFeign',{
                    articleId: articleId,
                }).then(res =>{
                    console.log(articleId);
                    if (res.data.success===true){
                        this.id=res.data.id;
                        this.articleTitle=res.data.articleTitle;
                        this.articleContent=res.data.articleMdContent;
                    }else {
                        this.$router.push({name: 'UserPublish'});
                        this.$router.go(0);
                        alert("非法访问!!!")
                    }
                })
            },
            // 将图片上传到服务器，返回地址替换到md中
            articleImg(pos, $file){
                let blogImg = new FormData();
                blogImg.append('blogImg', $file);
                this.$http.uploadFile('/whc/blog-customer-user/articlePublishImgFeign',blogImg).then(res => {
                    console.log(res);
                    if (res.status===200) {
                        this.$refs.md.$img2Url(pos, res.data);
                    }
                }).catch(err => {
                    console.log(err);
                    this.$Message.error('服务开了小差~请稍后再试!');
                })
            },
            // 所有操作都会被解析重新渲染
            change(value, render){
                // render 为 markdown 解析后的结果[html]
                this.html = render;
            },
            //选择文章细节的按钮
            articleShowClick(){
                if (this.articleTitle ==='' || this.articleContent === '' ||  this.articleTitle.match(/^[ ]*$/)
                    || this.articleContent.match(/^[ ]*$/)){
                    this.$Message.error('标题和内容不能为空!');
                    this.articleShow=false;
                }else {
                    this.articleShow=true;
                }
            },
            //修改文章的按钮
            articleUpdate(){
                let urlYz=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
                if (this.articleTitle === '' || this.articleContent === '' || this.articleTag === '' ||
                    this.articleCategory === '' || this.articleType === '' || this.articleSecurity === '') {
                    this.$Message.error('所选内容不能为空!');
                }else if (!urlYz.test(this.forWardUrl) && this.forWardSwitch === true){
                    this.$Message.error('转载链接的URL规则不正确!');
                }else if (!urlYz.test(this.translateUrl) && this.translateSwitch === true){
                    this.$Message.error('翻译连接的URL规则不正确!');
                }
                else {
                    this.$http.post('/whc/blog-customer-user/articleUpdateFeign', {
                        id: this.id,
                        articleTitle: this.articleTitle,
                        articleContent: this.articleContent,
                        articleTag: this.articleTag.toString(),
                        articleCategory: this.articleCategory,
                        articleType: this.articleType,
                        forWardUrl: this.forWardUrl,
                        translateUrl: this.translateUrl,
                        articleSecurity: this.articleSecurity,
                    }).then(res => {
                        console.log(res);
                        if (res.data.success===true){
                            alert("修改成功!!!");
                            this.$router.replace({name: 'UserPublish'});
                        }else {
                            this.$Message.error('服务开了小差~请稍后再试!');
                        }
                    })
                }
            }
        },
        mounted() {

        }
    }
</script>

