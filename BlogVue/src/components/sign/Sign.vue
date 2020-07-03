<template>
  <div class="container">
    <div class="row clearfix">
      <div class="col-md-12 column">
        <el-divider><i class="el-icon-s-flag">签到</i></el-divider>
        <el-tabs type="border-card">
          <el-tab-pane label="用户签到">
            <el-tooltip content="VIP经验值翻倍,冲冲冲!" placement="top">
              <el-button type="primary" @click="dialogVisible = true" circle>充值VIP经验翻倍</el-button>
            </el-tooltip>
            <el-dialog
              :visible.sync="dialogVisible"
              width="40%">
              <img class="img-circle" alt="我的个人头像" style="width: 50px;height: 50px" align="left" :src="rightShow"/>
              <br>
              <span v-html="blogName" style="color: black">{{ blogName }}</span>
              <el-divider></el-divider>
              <el-tabs :tab-position="tabPosition" style="height: 380px;">
                <el-tab-pane label="超级会员Svip">超级会员Svip</el-tab-pane>
                <el-tab-pane label="普通会员Vip">普通会员Vip</el-tab-pane>
              </el-tabs>
            </el-dialog>
            <el-calendar  v-model="experience">
            </el-calendar>
            <el-button type="success" class="el-icon-document-checked" v-on:click="sign" v-if="signButtonShow===1">签到</el-button>
            <el-button type="success" class="el-icon-document-checked" v-on:click="sign" v-if="signButtonShow===0" :disabled="true">已签到</el-button>
          </el-tab-pane>
          <el-tab-pane label="升级规则">
            <el-button
              plain
              type="success"
              @click="open1" class="el-icon-light-rain">
              温馨提示
            </el-button>
            <el-divider></el-divider>
            <span style="color: black;font-style: initial">
          您在博客头衔的提升取决于经验值的多少，随着经验值的增加，等级与本博客头衔都会随之提升。
          经验值是根据您在博客中的行为获得的，以下方式可以帮助您获取经验值
          </span>
            <el-divider></el-divider>
            <el-table
              :data="tableDataSign"
              :span-method="objectSpanMethodSign"
              border
              style="width: 100%; margin-top: 20px">
              <el-table-column
                prop="date"
                label="连续签到"
                align="center"
                width="100">
              </el-table-column>
              <el-table-column
                prop="experience"
                label="经验值"
                align="center"
                width="100">
              </el-table-column>
              <el-table-column
                prop="specific"
                align="center"
                label="完成连续签到的特殊成就">
              </el-table-column>
              <el-table-column
                prop="vip"
                align="center"
                label="会员加成">
              </el-table-column>
            </el-table>
            <el-divider></el-divider>
            <el-table
              :data="tableDataBlog"
              :span-method="objectSpanMethodBlog"
              border>
              <el-table-column
                prop="behaviour"
                label="博客行为"
                align="center"
                width="200">
              </el-table-column>
              <el-table-column
                prop="rule"
                label="规则"
                align="center"
                width="200">
              </el-table-column>
              <el-table-column
                prop="sum"
                label="累计"
                align="center">
              </el-table-column>
              <el-table-column
                prop="blogExperience"
                align="center"
                label="经验值">
              </el-table-column>
              <el-table-column
                prop="vip"
                align="center"
                label="会员经验加成">
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="等级规则">
            <el-table
              :data="tableDataClassRule"
              border
              style="width: 100%">
              <el-table-column
                prop="power"
                label="特权"
                align="center">
              </el-table-column>
              <el-table-column
                prop="one"
                label="1"
                align="center">
              </el-table-column>
              <el-table-column
                prop="two"
                label="2"
                align="center">
              </el-table-column>
              <el-table-column
                prop="three"
                label="3"
                align="center">
              </el-table-column>
              <el-table-column
                prop="four"
                label="4"
                align="center">
              </el-table-column>
              <el-table-column
                prop="five"
                label="5"
                align="center">
              </el-table-column>
              <el-table-column
                prop="six"
                label="6"
                align="center">
              </el-table-column>
              <el-table-column
                prop="seven"
                label="7"
                align="center">
              </el-table-column>
              <el-table-column
                prop="eight"
                label="8"
                align="center">
              </el-table-column>
              <el-table-column
                prop="nine"
                label="9"
                align="center">
              </el-table-column>
              <el-table-column
                prop="ten"
                label="10"
                align="center">
              </el-table-column>
              <el-table-column
                prop="eleven"
                label="11"
                align="center">
              </el-table-column>
              <el-table-column
                prop="twelve"
                label="12"
                align="center">
              </el-table-column>
              <el-table-column
                prop="thirteen"
                label="13"
                align="center">
              </el-table-column>
              <el-table-column
                prop="fourteen"
                label="14"
                align="center">
              </el-table-column>
              <el-table-column
                prop="fifteen"
                label="15"
                align="center">
              </el-table-column>
              <el-table-column
                prop="sixteen"
                label="16"
                align="center">
              </el-table-column>
              <el-table-column
                prop="seventeen"
                label="17"
                align="center">
              </el-table-column>
              <el-table-column
                prop="eighteen"
                label="18"
                align="center">
              </el-table-column>
              <el-table-column
                prop="nineteen"
                label="19"
                align="center">
              </el-table-column>
              <el-table-column
                prop="twenty"
                label="20"
                align="center">
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="奖励中心">
            <div align="right">
            <el-button v-on:click="signReload" size="small" class="el-icon-refresh" type="success">刷新奖励延迟</el-button>
            </div>
            <h3 style="color: darkviolet">尊敬的&nbsp;<span v-html="blogName">{{ blogName }}</span>~您已经连续签到{{ continueSign }}天~继续努力✌</h3>
            <div align="left">
              <el-popover
                placement="top"
                width="160"
                v-model="continueShow">
                <p>这是您的连续签到的奖励!</p>
                <div style="text-align: right; margin: 0">
                  <el-button size="mini" type="text" @click="continueShow = false">取消</el-button>
                  <el-button type="primary" size="mini" @click="continueShow = false" v-on:click="continueSignReward">确定</el-button>
                </div>
                <el-button slot="reference" size="mini" type="primary" v-if="continueButtonShow===1" @click="continueShow=true">一键领取</el-button>
              </el-popover>
              <el-button size="mini" type="primary" v-if="continueButtonShow===0" :disabled="true">继续努力签到</el-button>
            </div>
            <br>
            <el-steps :active="continueActive" finish-status="success">
              <el-step title="首次签到奖励" description="经验值+20" ></el-step>
              <el-step title="连续签到5天"  description="经验值+30" ></el-step>
              <el-step title="连续签到30天" description="vip7天的体验时间" ></el-step>
              <el-step title="连续签到90天" description="vip30天的体验时间" ></el-step>
              <el-step title="连续签到180天" description="经验值+500" ></el-step>
              <el-step title="连续签到360天" description="vip90天的体验时间" ></el-step>
              <el-step title="连续签到720天" description="vip180天的体验时间" ></el-step>
            </el-steps>
            <br>
            <el-divider></el-divider>
            <h3 style="color: darkviolet">尊敬的 <span v-html="blogName">{{ blogName }}</span>~您已经累计签到{{ totalSign }}天~继续努力✌</h3>
            <div align="left">
              <el-popover
                placement="top"
                width="160"
                v-model="totalShow">
                <p>这是您的累计签到的奖励!</p>
                <div style="text-align: right; margin: 0">
                  <el-button size="mini" type="text" @click="totalShow = false">取消</el-button>
                  <el-button type="primary" size="mini" @click="totalShow = false" v-on:click="totalSignReward">确定</el-button>
                </div>
                <el-button slot="reference" size="mini" type="primary" v-if="totalButtonShow===1" @click="totalShow=true">一键领取</el-button>
              </el-popover>
              <el-button size="mini" type="primary" v-if="totalButtonShow===0" :disabled="true">继续努力签到</el-button>
            </div>
            <br>
            <el-steps :active="totalActive" finish-status="success">
                <el-step title="累计30天签到奖励" description="经验值+100"></el-step>
                <el-step title="累计60天签到奖励" description="经验值+200"></el-step>
                <el-step title="累计90天签到奖励" description="经验值+300"></el-step>
                <el-step title="累计120天签到奖励" description="vip30天的体验时间"></el-step>
                <el-step title="累计180天签到奖励" description="经验值+500"></el-step>
                <el-step title="累计360天签到奖励" description="经验值+2000" ></el-step>
                <el-step title="累计720天签到奖励" description="经验值+5000"></el-step>
              </el-steps>
            <el-divider></el-divider>
            <p class="lead text-success text-center">
              <em style="color: red">签到奖励</em> 每个账号只能获得 <strong style="color: blue">领取一次奖励</strong> 且最终解释权为 <small style="color: black">作者本人</small> 所有。
            </p>
          </el-tab-pane>
          <el-tab-pane label="每日任务">
            <Form>
              <FormItem>
                <h1>博客次数任务</h1>
              </FormItem>
              <FormItem>
                发表文章：&nbsp;{{ signArticle }}/1次，5（无任何vip）/10（vip）/15（svip）经验值
              </FormItem>
              <FormItem>
                点赞奖励：{{ signThump }}/10次，1（无任何vip）/2（vip）/3（svip）经验值×10次
              </FormItem>
              <FormItem>
                评论奖励：{{ signComment }}/5次，20（无任何vip）/40（vip）/60（svip）经验值×5次
              </FormItem>
              <Divider/>
              <FormItem>
                <FormItem>
                  <h1>当日博客任务</h1>
                </FormItem>
                <FormItem>
                  <br>
                  <span v-if="signArticle===1">
                    您已完成发表文章，与他人互动，发表的文章凌晨在排行中，则会增加一定的经验值，发表好的文章更容易做到哦（当天凌晨结算）
                    <ui>
                      <li>
                        文章有效点赞👍>=20
                      </li>
                      <li>
                        博客有效回复>=10
                      </li>
                      <li>
                        凌晨时间在排行榜中
                      </li>
                    </ui>
                  </span>
                  <span v-else>您还未发表过任何文章，请发表一篇好文章来吸引博友</span>
                </FormItem>
              </FormItem>
            </Form>
          </el-tab-pane>
          <el-tab-pane label="我的经验">
            <div class="container">
              <div class="row clearfix">
                <div class="col-md-2 column">
                  <img class="img-thumbnail" alt="我的个人头像" style="width: 140px;height: 140px" :src="rightShow"/>
                  <el-divider></el-divider>
                  <div class="col-md-12 column">
                    <p align="left">排名: {{ rank }}</p>
                    <p align="left">经验值: {{ myExperience }}</p>
                    <p align="left">签到等级: {{ signRank }}</p>
                    <p align="left">总签到次数: {{ totalSign }}</p>
                    <p align="left">连续签到次数: {{ continueSign }}</p>
                  </div>
                  <div class="col-md-6 column">
                  </div>
                  </div>
                <div class="col-md-10 column">
                  <blockquote class="pull-left">
                    <p>
                      只有充钱才能变的更强&nbsp;<router-link :to="{name: 'Pay'}" target="_blank"><el-button type="primary" circle>没有VIP?</el-button></router-link>
                    </p> <small>腾讯 <cite>马化腾</cite></small>
                    <el-divider></el-divider>
                    <span v-html="blogName">{{ blogName }}</span>
                    -<i class="el-icon-s-shop" v-if="memberSign==='1'">会员</i>
                  </blockquote>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>
<style>
  .is-selected {
    color: #1989FA;
  }

  body {background: url('../../../static/img/sign/sign.jpg');}
</style>
<script>
    export default {
        inject: ['reload'],
        data() {
            return {
                //签到每天的任务
                signArticle: '',
                signThump: '',
                signComment: '',
                memberSign: window.localStorage.getItem('memberSign'),
                //签到按钮的显示
                signButtonShow: 0,
                //连续签到的按钮显示
                continueButtonShow: 0,
                //总数签到的按钮显示
                totalButtonShow: 0,
                //排名
                rank: '',
                //等级
                signRank: '',
                //经验值
                myExperience: '',
                //连续签到
                continueSign: '',
                //总数签到
                totalSign: '',
                id: window.localStorage.getItem('id'),
                tabPosition: 'left',
                dialogVisible: false,
                continueActive: 0,
                continueShow: false,
                totalActive: 0,
                totalShow: false,
                login: 0,
                experience: new Date(),
                rightShow: window.localStorage.getItem('userPicture'),
                blogName: window.localStorage.getItem('blogName'),
                tableDataSign: [{
                    date: '1天',
                    experience: '+3',
                    specific: '经验值+20',
                    vip: '首次签到VIP翻5倍-SVIP翻10倍'
                }, {
                    date: '5天',
                    experience: '+5',
                    specific: '经验值+30',
                    vip: 'VIP翻2倍-SVIP翻3倍'
                }, {
                    date: '30天',
                    experience: '+7',
                    specific: 'vip7天的体验时间',
                    vip: 'VIP翻2倍-SVIP翻3倍'
                }, {
                    date: '90天',
                    experience: '+9',
                    specific: 'vip30天的体验时间',
                    vip: 'VIP翻3倍-SVIP翻4倍'
                }, {
                    date: '180天',
                    experience: '+11',
                    specific: '经验值+500',
                    vip: 'VIP翻3倍-SVIP翻4倍'
                }, {
                    date: '360天',
                    experience: '+13',
                    specific: 'vip90天的体验时间',
                    vip: 'VIP翻4倍-SVIP翻5倍'
                }, {
                    date: '720天',
                    experience: '+15',
                    specific: 'vip180天的体验时间',
                    vip: 'VIP翻4倍-SVIP翻5倍'
                }],
                tableDataBlog: [
                    {
                        behaviour: '发表博客',
                        rule: '每日这一种行为的累加',
                        sum: '1',
                        blogExperience: '+5',
                        vip: 'VIP翻2倍-SVIP翻3倍'
                    }, {
                        behaviour: '点赞他人博客',
                        rule: '每日这一种行为的累加',
                        sum: '10',
                        blogExperience: '+10',
                        vip: 'VIP翻2倍-SVIP翻3倍'
                    }, {
                        behaviour: '评论他人博客',
                        rule: '每日这一种行为的累加',
                        sum: '5',
                        blogExperience: '+20',
                        vip: 'VIP翻2倍-SVIP翻3倍'
                    }, {
                        behaviour: '自己博客的有效回复>=10',
                        rule: '每日这一种行为的累加',
                        sum: '1',
                        blogExperience: '+10',
                        vip: 'VIP翻3倍-SVIP翻5倍'
                    }, {
                        behaviour: '自己博客的有效点赞>=20',
                        rule: '每日这一种行为的累加',
                        sum: '1',
                        blogExperience: '+10',
                        vip: 'VIP翻3倍-SVIP翻5倍'
                    }, {
                        behaviour: '博客达到排行榜',
                        rule: '每日这一种行为的累加',
                        sum: '1',
                        blogExperience: '+100',
                        vip: 'VIP翻3倍-SVIP翻5倍'
                    }
                ],
                tableDataClassRule: [
                    {
                        power: '经验值',
                        one: '1',
                        two: '100',
                        three: '200',
                        four: '500',
                        five: '1000',
                        six: '2000',
                        seven: '3000',
                        eight: '5000',
                        nine: '10000',
                        ten: '20000',
                        eleven: '30000',
                        twelve: '60000',
                        thirteen: '100000',
                        fourteen: '180000',
                        fifteen: '300000',
                        sixteen: '500000',
                        seventeen: '600000',
                        eighteen: '700000',
                        nineteen: '1000000',
                        twenty: '1500000',
                    },
                    {
                        power: '等级头衔',
                        one: '√',
                        two: '√',
                        three: '√',
                        four: '√',
                        five: '√',
                        six: '√',
                        seven: '√',
                        eight: '√',
                        nine: '√',
                        ten: '√',
                        eleven: '√',
                        twelve: '√',
                        thirteen: '√',
                        fourteen: '√',
                        fifteen: '√',
                        sixteen: '√',
                        seventeen: '√',
                        eighteen: '√',
                        nineteen: '√',
                        twenty: '√',
                    },{
                        power: '免验证码',
                        one: '√',
                        two: '√',
                        three: '√',
                        four: '√',
                        five: '√',
                        six: '√',
                        seven: '√',
                        eight: '√',
                        nine: '√',
                        ten: '√',
                        eleven: '√',
                        twelve: '√',
                        thirteen: '√',
                        fourteen: '√',
                        fifteen: '√',
                        sixteen: '√',
                        seventeen: '√',
                        eighteen: '√',
                        nineteen: '√',
                        twenty: '√',
                    },
                    {
                        power: '优先处理反馈',
                        one: '√',
                        two: '√',
                        three: '√',
                        four: '√',
                        five: '√',
                        six: '√',
                        seven: '√',
                        eight: '√',
                        nine: '√',
                        ten: '√',
                        eleven: '√',
                        twelve: '√',
                        thirteen: '√',
                        fourteen: '√',
                        fifteen: '√',
                        sixteen: '√',
                        seventeen: '√',
                        eighteen: '√',
                        nineteen: '√',
                        twenty: '√',
                    },
                    {

                        power: '博客收藏',
                        one: '❌',
                        two: '❌',
                        three: '❌',
                        four: '√',
                        five: '√',
                        six: '√',
                        seven: '√',
                        eight: '√',
                        nine: '√',
                        ten: '√',
                        eleven: '√',
                        twelve: '√',
                        thirteen: '√',
                        fourteen: '√',
                        fifteen: '√',
                        sixteen: '√',
                        seventeen: '√',
                        eighteen: '√',
                        nineteen: '√',
                        twenty: '√',
                    },
                    {
                        power: '博客评论',
                        one: '❌',
                        two: '❌',
                        three: '❌',
                        four: '❌',
                        five: '❌',
                        six: '√',
                        seven: '√',
                        eight: '√',
                        nine: '√',
                        ten: '√',
                        eleven: '√',
                        twelve: '√',
                        thirteen: '√',
                        fourteen: '√',
                        fifteen: '√',
                        sixteen: '√',
                        seventeen: '√',
                        eighteen: '√',
                        nineteen: '√',
                        twenty: '√',
                    },
                    {
                        power: '评论表情',
                        one: '❌',
                        two: '❌',
                        three: '❌',
                        four: '❌',
                        five: '❌',
                        six: '❌',
                        seven: '√',
                        eight: '√',
                        nine: '√',
                        ten: '√',
                        eleven: '√',
                        twelve: '√',
                        thirteen: '√',
                        fourteen: '√',
                        fifteen: '√',
                        sixteen: '√',
                        seventeen: '√',
                        eighteen: '√',
                        nineteen: '√',
                        twenty: '√',
                    },
                    {
                        power: '抽奖',
                        one: '❌',
                        two: '❌',
                        three: '❌',
                        four: '❌',
                        five: '❌',
                        six: '❌',
                        seven: '√',
                        eight: '√',
                        nine: '√',
                        ten: '√',
                        eleven: '√',
                        twelve: '√',
                        thirteen: '√',
                        fourteen: '√',
                        fifteen: '√',
                        sixteen: '√',
                        seventeen: '√',
                        eighteen: '√',
                        nineteen: '√',
                        twenty: '√',
                    },
                    {
                        power: '博客云',
                        one: '❌',
                        two: '❌',
                        three: '❌',
                        four: '❌',
                        five: '❌',
                        six: '❌',
                        seven: '❌',
                        eight: '√',
                        nine: '√',
                        ten: '√',
                        eleven: '√',
                        twelve: '√',
                        thirteen: '√',
                        fourteen: '√',
                        fifteen: '√',
                        sixteen: '√',
                        seventeen: '√',
                        eighteen: '√',
                        nineteen: '√',
                        twenty: '√',
                    }
                    ,
                    {
                        power: 'vip8折年费优惠',
                        one: '❌',
                        two: '❌',
                        three: '❌',
                        four: '❌',
                        five: '❌',
                        six: '❌',
                        seven: '❌',
                        eight: '❌',
                        nine: '❌',
                        ten: '❌',
                        eleven: '❌',
                        twelve: '❌',
                        thirteen: '❌',
                        fourteen: '❌',
                        fifteen: '❌',
                        sixteen: '√',
                        seventeen: '√',
                        eighteen: '√',
                        nineteen: '√',
                        twenty: '√',
                    },
                    {
                        power: 'Svip9折年费优惠',
                        one: '❌',
                        two: '❌',
                        three: '❌',
                        four: '❌',
                        five: '❌',
                        six: '❌',
                        seven: '❌',
                        eight: '❌',
                        nine: '❌',
                        ten: '❌',
                        eleven: '❌',
                        twelve: '❌',
                        thirteen: '❌',
                        fourteen: '❌',
                        fifteen: '❌',
                        sixteen: '√',
                        seventeen: '√',
                        eighteen: '√',
                        nineteen: '√',
                        twenty: '√',
                    },
                    {
                        power: '人工服务',
                        one: '❌',
                        two: '❌',
                        three: '❌',
                        four: '❌',
                        five: '❌',
                        six: '❌',
                        seven: '❌',
                        eight: '❌',
                        nine: '❌',
                        ten: '❌',
                        eleven: '❌',
                        twelve: '❌',
                        thirteen: '❌',
                        fourteen: '❌',
                        fifteen: '❌',
                        sixteen: '❌',
                        seventeen: '√',
                        eighteen: '√',
                        nineteen: '√',
                        twenty: '√',
                    }
                ]
            }
        },
        methods: {
            handleClick(tab, event) {
                console.log(tab, event);
            },
            open1() {
                const h = this.$createElement;

                this.$notify({
                    title: '每日经验上限',
                    message: h('i', {style: 'color: teal'}, '每日获得的经验上限不能超过1500')
                });
            },
            //签到积分的合并
            objectSpanMethodSign({row, column, rowIndex, columnIndex}) {
                if (columnIndex === 3) {
                    if (rowIndex % 2 === 0) {
                        return {
                            rowspan: 1,
                            colspan: 1
                        };
                    } else {
                        return {
                            rowspan: 0,
                            colspan: 0
                        };
                    }
                }
            },
            //博客积分的合并
            objectSpanMethodBlog({row, column, rowIndex, columnIndex}) {
                if (columnIndex === 4) {
                    if (rowIndex % 2 === 0) {
                        return {
                            rowspan: 3,
                            colspan: 1
                        };
                    } else {
                        return {
                            rowspan: 0,
                            colspan: 0
                        };
                    }
                }
            },
            sign() {
                let now = new Date();
                if (this.experience.getDay() === now.getDay()) {
                    this.$http.post('/whc/blog-customer-user/getExperienceButton')
                        .then(res => {
                            console.log(res);
                            if (res.data.success === true) {
                                this.$notify({
                                    title: '成功',
                                    message: '签到成功~已经成功获得经验值(会员翻倍加成)',
                                    type: 'success',
                                });
                                this.reload();
                                //保存排名
                                window.localStorage.setItem('rank',res.data.rank);
                                //保存经验值
                                window.localStorage.setItem('myExperience', res.data.message);
                                //保存等级
                                window.localStorage.setItem('signRank',res.data.signRank);
                                //保存总签到数
                                window.localStorage.setItem('totalSign',res.data.totalSign);
                                //保存连续签到数
                                window.localStorage.setItem('continueSign',res.data.continueSign);
                            } else {
                                this.$message.error('今天的机会用完了,请明天再来!')
                            }
                        })
                } else {
                    console.log(this.experience.getDay());
                    this.$message.error('请改天再来!');
                }
            },
            //奖励的刷新按钮
            signReload(){
                this.$http.post('/whc/blog-customer-user/getSignAllFeign')
                    .then(res =>{
                        console.log(res);
                        //保存排名
                        window.localStorage.setItem('rank',res.data.rank);
                        //保存经验值
                        window.localStorage.setItem('myExperience',res.data.myExperience);
                        //保存等级
                        window.localStorage.setItem('signRank',res.data.signRank);
                        //保存总签到数
                        window.localStorage.setItem('totalSign',res.data.totalSign);
                        //保存连续签到数
                        window.localStorage.setItem('continueSign',res.data.continueSign);

                        this.reload();

                    })
            },
            //连续签到的奖励
            continueSignReward(){
                this.$http.post('/whc/blog-customer-user/getContinueRewardFeign')
                    .then(res =>{
                        console.log(res);
                        if (res.data.success === true) {
                            this.$notify({
                                title: '成功',
                                message: '奖励领取成功(奖励只能获得一次哦!)',
                                type: 'success',
                            });
                            //保存排名
                            window.localStorage.setItem('rank',res.data.rank);
                            //保存经验值
                            window.localStorage.setItem('myExperience',res.data.myExperience);
                            //保存等级
                            window.localStorage.setItem('signRank',res.data.signRank);
                            //保存总签到数
                            window.localStorage.setItem('totalSign',res.data.totalSign);
                            //保存连续签到数
                            window.localStorage.setItem('continueSign',res.data.continueSign);
                            //局部刷新
                            this.reload();
                        }
                        else {
                            this.$message.success('不存在可以领取的奖励!');
                        }
                    })
            },
            //连续签到的奖励
            totalSignReward(){
                this.$http.post('/whc/blog-customer-user/getTotalRewardFeign')
                    .then(res =>{
                        console.log(res);
                        if (res.data.success === true) {
                            this.$notify({
                                title: '成功',
                                message: '奖励领取成功(奖励只能获得一次哦!)',
                                type: 'success',
                            });
                            //保存排名
                            window.localStorage.setItem('rank',res.data.rank);
                            //保存经验值
                            window.localStorage.setItem('myExperience',res.data.myExperience);
                            //保存等级
                            window.localStorage.setItem('signRank',res.data.signRank);
                            //保存总签到数
                            window.localStorage.setItem('totalSign',res.data.totalSign);
                            //保存连续签到数
                            window.localStorage.setItem('continueSign',res.data.continueSign);
                            //局部刷新
                            this.reload();
                        }
                        else {
                            this.$message.success('不存在可以领取的奖励!');
                        }
                    })
            },
            //控制连续签到的Button的按钮
            showContinueButton(){
                this.$http.post('/whc/blog-customer-user/showContinueButton')
                    .then(res => {
                        console.log(res);
                        if (res.data.success === true) {
                            //显示连续签到的按钮
                            this.continueButtonShow=1;
                        }else {
                            this.continueButtonShow=0;
                        }
                    })
            },
            //控制总数签到的Button的按钮
            showTotalButton(){
                this.$http.post('/whc/blog-customer-user/showTotalButton')
                    .then(res => {
                        console.log(res);
                        if (res.data.success === true) {
                            //显示总数签到的按钮
                            this.totalButtonShow=1;
                        }else {
                            this.totalButtonShow=0;
                        }
                    })
            },
            //签到的Button是否已经重置
            showSignButton(){
                this.$http.post('/whc/blog-customer-user/showSignFeign')
                    .then(res => {
                        console.log(res);
                        if (res.data.success === true) {
                            //显示总数签到的按钮
                            this.signButtonShow=1;
                        }else {
                            this.signButtonShow=0;
                        }
                    })
            },
            //获取签到数据的函数
            getSignAll() {
                this.$http.post('/whc/blog-customer-user/getSignAllFeign')
                    .then(res =>{
                        console.log(res);
                        //保存排名
                        window.localStorage.setItem('rank',res.data.rank);
                        //保存经验值
                        window.localStorage.setItem('myExperience',res.data.myExperience);
                        //保存等级
                        window.localStorage.setItem('signRank',res.data.signRank);
                        //保存总签到数
                        window.localStorage.setItem('totalSign',res.data.totalSign);
                        //保存连续签到数
                        window.localStorage.setItem('continueSign',res.data.continueSign);
                        this.rank=res.data.rank;
                        this.myExperience=res.data.myExperience;
                        this.signRank=res.data.signRank;
                        this.totalSign=res.data.totalSign;
                        this.continueSign=res.data.continueSign;
                        //展示到连续签到哪个阶段(后端已经控制安全到位,前端只有显示的功能,后端有增加判断)
                        let continueSign=res.data.continueSign;
                        if (continueSign===null)
                        {
                            this.continueActive=0;
                        }
                        if (continueSign>=1){
                            this.continueActive=1;
                        }
                        if (continueSign>=5){
                            this.continueActive=2;
                        }
                        if (continueSign>=30){
                            this.continueActive=3;
                        }
                        if (continueSign>=90){
                            this.continueActive=4;
                        }
                        if (continueSign>=180){
                            this.continueActive=5;
                        }
                        if (continueSign>=360){
                            this.continueActive=6;
                        }
                        if (continueSign>=720){
                            this.continueActive=7;
                        }
                        //总数签到的显示在哪个阶段
                        let totalSign=res.data.totalSign;
                        if (totalSign===null)
                        {
                            this.totalActive=0;
                        }
                        if (totalSign>=30){
                            this.totalActive=1;
                        }
                        if (totalSign>=60){
                            this.totalActive=2;
                        }
                        if (totalSign>=90){
                            this.totalActive=3;
                        }
                        if (totalSign>=120){
                            this.totalActive=4;
                        }
                        if (totalSign>=180){
                            this.totalActive=5;
                        }
                        if (totalSign>=360){
                            this.totalActive=6;
                        }
                        if (totalSign>=720){
                            this.totalActive=7;
                        }

                    })
            },
            //查询当天的完成情况
            selectZeroSign(){
                this.$http.post('/whc/blog-customer-user/selectZeroFeign')
                    .then(res => {
                        if (res.data.success===true){
                            this.signArticle=res.data.signArticle;
                            this.signThump=res.data.signThump;
                            this.signComment=res.data.signComment;
                        }
                    })
            },
        },
        created(){
          this.selectZeroSign();
        },
        mounted() {
            this.showContinueButton();
            this.showTotalButton();
            this.showSignButton();
            this.getSignAll();
        }
    }
</script>
