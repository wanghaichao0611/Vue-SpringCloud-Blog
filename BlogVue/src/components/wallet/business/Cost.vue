<template>
  <div>
    <br>
    <Button type="primary"  @click="exportData(1)">
      <Icon type="ios-download-outline"/>
      导出当前账单Csv
    </Button>
    &emsp; &emsp; &emsp;
    <Button type="success"  @click="orderExcel = true">
      <Icon type="ios-download-outline"/>
      导出月账单Csv
    </Button>
    &emsp; &emsp; &emsp;
    <Button type="info"  @click="orderExcel = true">
      <Icon type="ios-download-outline"/>
      导出全部账单Csv
    </Button>
    <Divider/>
    <Drawer
      title="支付宝账单"
      v-model="orderExcel"
      width="720"
      :mask-closable="true"
      :styles="styles"
      :transfer="false"
    >
      <Form>
        <Row :gutter="32">
          <Col>
            <FormItem label-position="top">
              <h4 class="text-center" style="color: #2c3e50">请选择你的月份账单</h4>
            </FormItem>
          </Col>
        </Row>
        <br>
        <Row :gutter="32">
          <Col>
            <FormItem label="请选择账单的月份" label-position="top">
              <p>{{monthStart}}</p>
              <DatePicker v-model="monthStart" :options="monthOptions" format="yyyy-MM" type="month" placeholder="请选择月份" style="width: 200px"/>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="32">
          <Col span="12">
            <FormItem label="导出月份账单" label-position="top">
             <Button type="primary" ghost  v-on:click="orderMonth">导出指定月份的支付宝账单</Button>
            </FormItem>
          </Col>
        </Row>
        <Divider/>
        <Row :gutter="32">
          <Col>
            <FormItem label-position="top">
              <h4 class="text-center" style="color: #2c3e50">全部的账单</h4>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="32">
          <Col span="12">
            <FormItem label="导出全部账单" label-position="top">
              <Button type="primary" ghost @click="orderAll=true">导出所有支付宝账单</Button>
              <Modal
                title="手机安全验证"
                v-model="orderAll"
                :transfer="false"
                @on-ok="ok"
                @on-cancel="cancel">
                <el-form>
                  <el-form-item>
                    <span slot="label">验证手机号才能导出所有的账单:{{ myPhone }}</span>
                    <el-button type="primary" size="medium" :disabled="disable" @click="orderTest">{{getCode}}<i class="el-icon-message"></i></el-button>
                  </el-form-item>
                  <el-form-item>
                    <el-input type="text" v-model="phoneCode" autocomplete="off" placeholder="请输入你的手机验证码" maxlength="6" show-word-limit></el-input>
                  </el-form-item>
                </el-form>
              </Modal>
            </FormItem>
          </Col>
        </Row>
      </Form>
      <br>
    </Drawer>
    <Table :loading="loading" :columns="orderColumns" :data="order" ref="table" size="small">
      <template slot-scope="{ row, index }" slot="action">
        <Button type="primary" size="small" style="margin-right: 5px" @click="show(index)">查看</Button>
        <Button type="error" size="small" @click="remove(index)">删除</Button>
      </template>
      <template slot-scope="{ row }" slot="tradestatus">
        <strong v-if="row.tradestatus==='支付成功'" style="color: red">{{ row.tradestatus }}</strong>
        <strong v-if="row.tradestatus==='支付失败'"  style="color: black">{{ row.tradestatus }}</strong>
        <strong v-if="row.tradestatus==='等待支付'"  style="color: blue">{{ row.tradestatus }}</strong>
      </template>
      <template slot-scope="{ row }" slot="fundchannel">
        <span v-if="row.fundchannel==null" style="color: black">未完成交易</span>
        <span v-else style="color: #1989FA">{{ row.fundchannel }}</span>
      </template>
    </Table>
    <br>
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page"
        :page-sizes="[6]"
        :page-size="count"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>
<script>
    export default {
        inject: ['reload'],
        data () {
            return {
                page: 1,
                count: 6,
                total: '',
                loading: true,
                orderExcel: false,
                orderAll: false,
                myPhone: window.localStorage.getItem('myPhone'),
                getCode: '获取验证码',
                isGeting: false,
                timeCount: 60,
                disable: false,
                phoneCode: '',
                monthStart: '',
                visitDate: '',
                styles: {
                    height: 'calc(100% - 55px)',
                    overflow: 'auto',
                    paddingBottom: '53px',
                    position: 'static'
                },
                orderColumns: [
                    {
                        title: '操作',
                        slot: 'action',
                        width: 150,
                        align: 'center'
                    },
                    {
                        title: '商品名称',
                        key: 'subject',
                        width: 100,
                        align: 'center'
                    },
                    {
                        title: '商品金额(￥RMB)',
                        key: 'totalAmount',
                        width: 100,
                        align: 'center'
                    },
                    {
                        title: '交易状态',
                        key: 'tradestatus',
                        slot: 'tradestatus',
                        width: 100,
                        align: 'center'
                    },
                    {
                        title: '实收金额(￥RMB)',
                        key: 'receiptAmount',
                        width: 100,
                    },
                    {
                        title: '付款方式',
                        key: 'fundchannel',
                        slot: 'fundchannel',
                        width: 200,
                        align: 'center'
                    },
                    {
                        title: '商品说明',
                        key: 'body',
                        width: 200,
                        align: 'center'
                    },
                    {
                        title: '交易开始时间',
                        key: 'createtime',
                        width: 200,
                        align: 'center'
                    },
                    {
                        title: '交易订单号',
                        key: 'outTradeNo',
                        width: 200,
                        align: 'center'
                    },
                    {
                        title: '交易结束时间',
                        key: 'endtime',
                        width: 200,
                        align: 'center'
                    },
                ],
                order: [
                    {
                        subject: '',
                        totalAmount: '',
                        tradestatus: '',
                        receiptAmount: '',
                        fundchannel: '',
                        body: '',
                        createtime: '',
                        outTradeNo: '',
                        endtime: '',
                    }
                ],
                monthOptions: {
                    disabledDate (date) {
                        const now=new Date();
                        const end = new Date();
                        const start = new Date(new Date(now.setDate(now.getMonth()+1))
                            .toLocaleDateString());
                        const year = new Date(new Date().getFullYear(), 0);
                        return date && date.valueOf() < year.getTime() || date && date> start.getTime();
                    }
                },
            }
        },
        created() {
            //初始化账单并且分页
            this.orderPageAll();
        },
        methods: {
            //导出指定月份的账单
            orderMonth(){
                if (this.monthStart === ''){
                    alert('月份不能为空!');
                }else {
                    //导出指定月份的账单
                    this.exportData(2);
                }
            },
            //验证的手机号(共用的服务)
            orderTest(){
                var countDown = setInterval(() => {
                    if (this.count < 1) {
                        this.isGeting = false;
                        this.disable = false;
                        this.getCode = '获取验证码';
                        this.timeCount = 60;
                        clearInterval(countDown);
                    } else {
                        this.isGeting = true;
                        this.disable = true;
                        this.getCode = this.timeCount-- + 's后重发';
                    }
                }, 1000);
                this.$http.post('/whc/blog-customer-user/eachServerCodeFeign')
                    .then(res => {
                        if (res.data.success === true) {
                            this.$message({
                                type: "success",
                                message: '发送成功',
                            })
                        }else {
                            this.$message({
                                type: "error",
                                message: '服务器开了小差,稍微再试!',
                            })
                        }
                    })
            },
            ok() {
                let phoneTest=/^[0-9]{6}$/;
                if (this.phoneCode === '' || !phoneTest.test(this.phoneCode)){
                    this.$Message.error('验证码格式不正确');
                }else {
                    this.$http.post('/whc/blog-customer-user/checkServerCodeFeign',{
                        phoneCode: this.phoneCode,
                    }).then(res =>{
                        if (res.data.success===true) {
                            this.$Message.info('验证成功，账单已导出!');
                            //导出全部账单
                            this.exportData(3);
                        }else {
                            this.$Message.error(res.data.message);
                        }
                    })
                }
            },
            cancel () {
                this.$Message.info('取消验证!');
            },
            // 初始页currentPage、初始每页数据数pagesize和数据data
            handleSizeChange: function (size) {
                this.count = size;
                console.log(this.count); //每页下拉显示数据
                this.orderPageAll();
            },
            handleCurrentChange: function (currentPage) {
                this.page = currentPage;
                console.log(this.page);  //点击第几页
                this.orderPageAll();
            },
            //账单的分页
            orderPageAll() {
                this.$http.post('/whc/blog-customer-user/orderPageAllFeign', {
                    page: this.page,
                    count: this.count,
                }).then(res => {
                    console.log(res);
                    if (res.data.success === true) {
                        this.order = res.data.list;
                        this.total = res.data.total;
                        this.loading = false;
                    } else {
                        alert('微服务出了小状况,请再次刷新!');
                    }
                })
            },
            exportData (type) {
                if (type === 1) {
                    this.$refs.table.exportCsv({
                        filename: '支付宝用户指定的账单'
                    });
                } else if (type === 2) {
                    this.$http.post('/whc/blog-customer-user/orderMonthAllFeign',{
                        monthStart: this.monthStart.toString(),
                    }).then(res =>{
                        console.log(res);
                        this.$refs.table.exportCsv({
                            filename: '支付宝月份账单',
                            columns: this.orderColumns,
                            data: res.data.list,
                        });
                    })
                } else if (type === 3) {
                    this.$http.post('/whc/blog-customer-user/orderAllFeign')
                        .then(res =>{
                            console.log(res);
                            this.$refs.table.exportCsv({
                                filename: '支付宝用户全部的账单',
                                columns: this.orderColumns,
                                data: res.data.list,
                            });
                    })
                }
            },
            show (index) {
                this.$notify({
                    title: `${this.order[index].subject}`,
                    type: 'success',
                    dangerouslyUseHTMLString: true,
                    showClose: true,
                    message: `商品名称：${this.order[index].subject}<br>
                              商品说明: ${this.order[index].body}<br>
                              商品金额：${this.order[index].totalAmount}<br>
                              交易状态：<span style="color: red">${this.order[index].tradestatus}</span><br>
                              支付金额：${this.order[index].receiptAmount}<br>
                              付款方式：${this.order[index].fundchannel}<br>
                              订单号码: ${this.order[index].outTradeNo}<br>
                              支付时间：${this.order[index].endtime}`
                })
            },
            remove (index) {
                this.$confirm('确定删除此条记录，删除后不可恢复！',"提示",{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                }).then(() => {
                    this.$http.post('/whc/blog-customer-user/orderDeleteFeign',{
                        orderId: this.order[index].id,
                    }).then(res =>{
                        if (res.data.success===true){
                           alert("删除成功");
                           this.reload();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            }
        },
    }
</script>
