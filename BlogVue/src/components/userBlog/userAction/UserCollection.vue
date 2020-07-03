<template>
  <div class="row clearfix">
    <div class="col-md-12 column">
  <Table border highlight-row :loading="loading" :columns="collectColumns" :data="collectData">
    <template slot-scope="{ row }" slot="name">
      <strong v-html="row.name">{{ row.name }}</strong>
    </template>
  </Table>
      <br>
  <div class="block" v-if="showPage===true">
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
  </div>
  </div>
</template>
<script>
    export default {
        inject: ['reload'],
        data () {
            return {
                page: 1,
                count: 5,
                total: '',
                loading: true,
                showPage: true,
                collectColumns: [
                    {
                        title: '文章作者',
                        key: 'name',
                        slot: 'name',
                        width: 300,
                        align: 'center',
                    },
                    {
                        title: '文章标题',
                        key: 'title',
                        align: 'center',
                    },
                    {
                        title: '文章开始发表时间',
                        key: 'articlePublishTime',
                        width: 200,
                        align: 'center',
                    },
                    {
                        title: '文章最后修改时间',
                        key: 'articleUpdateTime',
                        width: 200,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Icon', {
                                    props: {
                                        type: 'date'
                                    }
                                }),
                                h('strong',params.row.articleUpdateTime)
                            ]);
                        }
                    },
                    {
                        title: '文章收藏总数',
                        key:   'total',
                        width: 150,
                        align: 'center',
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 200,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'primary',
                                        size: 'small',
                                        ghost: '',
                                    },
                                    style: {
                                        marginRight: '15px'
                                    },
                                    on: {
                                        click: () => {
                                            this.show(params.index)
                                        }
                                    }
                                }, '点击查看'),
                                h('Button', {
                                    props: {
                                        type: 'error',
                                        size: 'small',
                                        ghost: '',
                                    },
                                    on: {
                                        click: () => {
                                            this.remove(params.index,this.collectData[params.index].id)
                                        }
                                    }
                                }, '取消收藏')
                            ]);
                        }
                    }
                ],
                collectData: [
                    {
                        id: '',
                        name: '',
                        title: '',
                        articlePublishTime: '',
                        articleUpdateTime: '',
                        total: '',
                    }
                ]
            }
        },
        created() {
            this.pageCollection();
        },
        methods: {
            //分页查询收藏
            pageCollection(){
                this.$http.post('/whc/blog-customer-user/articleCollectionFeign',{
                    page: this.page,
                    count: this.count,
                }).then(res =>{
                    console.log(res);
                    this.collectData=res.data.list;
                    this.total=res.data.total;
                    this.loading=false;
                    if (res.data.total===0){
                        this.showPage=false;
                    }
                })
            },
            // 初始页currentPage、初始每页数据数pagesize和数据data
            handleSizeChange: function (size) {
                this.count = size;
                console.log(this.count); //每页下拉显示数据
                this.loading=true;
                this.pageCollection();
            },
            handleCurrentChange: function(currentPage){
                this.page = currentPage;
                console.log(this.page);  //点击第几页
                this.loading=true;
                this.pageCollection();
            },
            //查看指定的博客
            show (index) {
                window.open('http://localhost:8088/home/details/'+this.collectData[index].id);
            },
            //取消博客收藏
            remove (index,articleId) {
                this.$http.post('/whc/blog-customer-user/cancelCollectFeign',{
                    articleId: articleId,
                }).then(res =>{
                    if (res.data.success===true){
                        this.$message.success('取消收藏成功!');
                        this.collectData.splice(index, 1);
                        this.reload();
                    }else {
                        alert('微服务出了小状况，请稍后再试!');
                    }
                })
            }
        }
    }
</script>
