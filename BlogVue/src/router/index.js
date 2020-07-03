import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/login/Login'
import Register from '@/components/login/Register'
import Customer from '@/components/customer/Customer'
import HomePage from '@/components/main/Homepage'
import Home from '@/components/main/Home'
import UserMsg from '@/components/message/UserMsg'
import Reply from '@/components/message/userMessage/Reply'
import At from '@/components/message/userMessage/At'
import Love from '@/components/message/userMessage/Love'
import System from '@/components/message/userMessage/System'
import MyMessage from '@/components/message/userMessage/MyMessage'
import Friends from '@/components/message/userMessage/Friends'
import MessageConfig from '@/components/message/userMessage/MessageConfig'
import Article from '@/components/main/home/Article'
import ArticleSearch from '@/components/main/home/ArticleSearch'
import Mation from '@/components/login/Mation'
import MationNext from '@/components/login/MationNext'
import Person from "@/components/person/Person"
import Mine from "@/components/person/Mine"
import Change from "@/components/person/password/Change"
import Reset from "@/components/person/password/Reset"
import Email from "@/components/person/technology/Email"
import Phone from "@/components/person/technology/Phone"
import IdCard from "@/components/person/security/IdCard"
import SchoolCard from "@/components/person/security/SchoolCard"
import Appeal from "@/components/person/security/Appeal"
import ChangeBlog from "@/components/person/blog/ChangeBlog"
import BlogMain from '@/components/blog/BlogMain'
import Recommend from '@/components/blog/others/Recommend'
import BlogVip from '@/components/blog/others/BlogVip'
import C from '@/components/blog/language/C'
import CPlus from '@/components/blog/language/CPlus'
import Java from '@/components/blog/language/Java'
import PHP from '@/components/blog/language/PHP'
import Python from '@/components/blog/language/Python'
import Go from '@/components/blog/language/Go'
import Test from '@/components/blog/skill/Test'
import Operation from '@/components/blog/skill/Operation'
import FrontEnd from '@/components/blog/skill/FrontEnd'
import BackEnd from '@/components/blog/skill/BackEnd'
import FullStack from '@/components/blog/skill/FullStack'
import Framework from '@/components/blog/skill/Framework'
import Sign from '@/components/sign/Sign'
import Member from '@/components/member/Member'
import Svip from '@/components/member/vip/Svip'
import Vip from '@/components/member/vip/Vip'
import Power from '@/components/member/vip/Power'
import Pay from '@/components/pay/Pay'
import PaySuccess from '@/components/pay/PaySuccess'
import PayRedirect from '@/components/pay/PayRedirect'
import Wallet from '@/components/wallet/Wallet'
import WalletSecurity from '@/components/wallet/WalletSecurity'
import MyWallet from '@/components/wallet/business/MyWallet'
import Account from '@/components/wallet/business/Account'
import SecuritySet from '@/components/wallet/business/SecuritySet'
import Cost from '@/components/wallet/business/Cost'
import BankCard from '@/components/wallet/business/BankCard'
import Coupon from '@/components/wallet/business/Coupon'
import CouponInvalid from '@/components/wallet/business/CouponInvalid'
import UserMain from '@/components/userBlog/UserMain'
import UserPublish from '@/components/userBlog/userAction/UserPublish'
import UserUpdate from '@/components/userBlog/userAction/UserUpdate'
import UserManage from '@/components/userBlog/userAction/UserManage'
import UserCollection from '@/components/userBlog/userAction/UserCollection'
import Details from '@/components/userBlog/urlShow/Details'

Vue.use(Router);

export default new Router({
  //HTML的路由模式，可以不使用。
  mode: 'history',
  routes: [
    {
      path: '/customer',
      meta: {
        title: '博客的客服中心'
      },
      name: 'Customer',
      component: Customer
    },
    {
      path: '/',
      meta: {
        title: '博客的主页面'
      },
      name: 'HomePage',
      component: HomePage,
    },
    {
      path: '/login',
      meta: {
        title: '博客的登录与注册'
      },
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      meta: {
        title: '博客的登录与注册'
      },
      name: 'Register',
      component: Register
    },
    {
      path: '/mation',
      meta: {
        title: '博客的注册个人信息'
      },
      name: 'Mation',
      component: Mation
    },
    {
      path: '/mationNext',
      meta: {
        title: '博客的注册个人信息'
      },
      name: 'MationNext',
      component: MationNext
    },
    {
      path: '/home',redirect: '/home/article',
      meta: {
        title: '博客的主页面'
      },
      name: 'Home',
      component: Home,
      children: [
        {
          path: 'sign',
          meta: {
            title: '博客的签到中心'
          },
          name: 'Sign',
          component: Sign
        },
        {
          path: 'article',
          meta: {
            title: '博客的浏览中心'
          },
          name: 'Article',
          component: Article
        },
        {
          path: 'details/:id',
          meta: {
            title: '博客的展示中心'
          },
          name: 'Details',
          component: Details
        },
        {
          path: 'articleSearch',
          meta: {
            title: '博客的搜索中心'
          },
          name: 'ArticleSearch',
          component: ArticleSearch
        },
        {
          path: '/userMsg',redirect: '/userMsg/reply',
          meta: {
            title: '博客的消息中心'
          },
          name: 'UserMsg',
          component: UserMsg,
          children:[
            {
              path: 'reply',
              meta:{
                title: '回复我的博客'
              },
              name: 'Reply',
              component: Reply
            },
            {
              path: 'at',
              meta:{
                title: 'at我的博客'
              },
              name: 'At',
              component: At
            },
            {
              path: 'love',
              meta:{
                title: '点赞我的博客'
              },
              name: 'Love',
              component: Love
            },
            {
              path: 'system',
              meta:{
                title: '系统通知中心'
              },
              name: 'System',
              component: System
            },
            {
              path: 'myMessage',
              meta:{
                title: '我的博客消息'
              },
              name: 'MyMessage',
              component: MyMessage
            },
            {
              path: 'friends',
              meta:{
                title: '好友通知中心'
              },
              name: 'Friends',
              component: Friends
            },
            {
              path: 'messageConfig',
              meta:{
                title: '消息隐私设置'
              },
              name: 'MessageConfig',
              component: MessageConfig
            }
          ]
        }
      ]
    },
    {
      path: '/person',redirect: '/person/mine',
      meta: {
        title: '博客的安全中心'
      },
      name: 'Person',
      component: Person,
      children: [{
        path: 'change',
        meta: {
          title: '博客的修改密码中心'
        },
        name: 'Change',
        component: Change
      },{
        path: 'reset',
        meta: {
          title: '博客的重置密码中心'
        },
        name: 'Reset',
        component: Reset
      },{
        path: 'email',
        meta: {
          title: '博客的邮箱中心'
        },
        name: 'Email',
        component: Email
      },{
        path: 'phone',
        meta: {
          title: '博客的手机中心'
        },
        name: 'Phone',
        component: Phone
      },{
        path: 'idCard',
        meta: {
          title: '博客的实名认证中心'
        },
        name: 'IdCard',
        component: IdCard
      },{
        path: 'schoolCard',
        meta: {
          title: '博客的校园认证中心'
        },
        name: 'SchoolCard',
        component: SchoolCard
      },{
        path: 'appeal',
        meta: {
          title: '博客的账号申诉中心'
        },
        name: 'Appeal',
        component: Appeal
      },{
        path: 'changeBlog',
        meta: {
          title: '博客的个人信息中心'
        },
        name: 'ChangeBlog',
        component: ChangeBlog
      },{
        path: 'mine',
        meta: {
          title: '博客的安全中心个人页面'
        },
        name: 'Mine',
        component: Mine
      }]
    },
    {
      path: '/blogMain',redirect: '/blogMain/recommend',
      meta: {
        title: '博客的浏览中心'
      },
      name: 'BlogMain',
      component: BlogMain,
      children: [{
        path: 'recommend',
        meta: {
          title: '博客的推荐中心'
        },
        name: 'Recommend',
        component: Recommend
      },{
        path: 'blogVip',
        meta: {
          title: '博客的vip中心'
        },
        name: 'BlogVip',
        component: BlogVip
      },{
        path: 'c',
        meta: {
          title: '博客的C语言中心'
        },
        name: 'C',
        component: C
      },{
        path: 'cPlus',
        meta: {
          title: '博客的C++语言中心'
        },
        name: 'CPlus',
        component: CPlus
      },{
        path: 'java',
        meta: {
          title: '博客的Java语言中心'
        },
        name: 'Java',
        component: Java
      },{
        path: 'php',
        meta: {
          title: '博客的php语言中心'
        },
        name: 'PHP',
        component: PHP
      },{
        path: 'python',
        meta: {
          title: '博客的python语言中心'
        },
        name: 'Python',
        component: Python
      },{
        path: 'go',
        meta: {
          title: '博客的GO语言中心'
        },
        name: 'Go',
        component: Go
      },{
        path: 'test',
        meta: {
          title: '博客的测试中心'
        },
        name: 'Test',
        component: Test
      },{
        path: 'operation',
        meta: {
          title: '博客的运维中心'
        },
        name: 'Operation',
        component: Operation
      },{
        path: 'frontEnd',
        meta: {
          title: '博客的前端中心'
        },
        name: 'FrontEnd',
        component: FrontEnd
      },{
        path: 'BackEnd',
        meta: {
          title: '博客的后端中心'
        },
        name: 'BackEnd',
        component: BackEnd
      },{
        path: 'fullStack',
        meta: {
          title: '博客的全栈中心'
        },
        name: 'FullStack',
        component: FullStack
      },{
        path: 'framework',
        meta: {
          title: '博客的架构中心'
        },
        name: 'Framework',
        component: Framework
      }]
    },
    {
      path: '/member',redirect: '/member/power',
      meta: {
        title: '博客的会员中心'
      },
      name: 'Member',
      component: Member,
      children: [{
        path: 'svip',
        meta: {
          title: '博客的超级会员'
        },
        name: 'Svip',
        component: Svip
      },{
        path: 'vip',
        meta: {
          title: '博客的普通会员'
        },
        name: 'Vip',
        component: Vip
      },{
        path: 'power',
        meta: {
          title: '博客的会员特权'
        },
        name: 'Power',
        component: Power
      }]
    },
    {
      path: '/wallet',redirect: '/wallet/myWallet',
      meta: {
        title: '博客的账户中心'
      },
      name: 'Wallet',
      component: Wallet,
      children: [
        {
          path: 'myWallet',
          meta: {
            title: '博客的账户中心'
          },
          name: 'MyWallet',
          component: MyWallet,
        },
        {
          path: 'account',
          meta: {
            title: '博客的账户中心'
          },
          name: 'Account',
          component: Account,
        },
        {
          path: 'securitySet',
          meta: {
            title: '博客的收支中心'
          },
          name: 'SecuritySet',
          component: SecuritySet,
        }
        ,{
        path: 'cost',
        meta: {
          title: '博客的账单中心'
        },
        name: 'Cost',
        component: Cost,
      },
        {
          path: 'bankcard',
          meta: {
            title: '博客的账单中心'
          },
          name: 'BankCard',
          component: BankCard,
        },
        {
          path: 'coupon',
          meta: {
            title: '博客的兑换券中心'
          },
          name: 'Coupon',
          component: Coupon,
        },
        {
          path: 'couponInvalid',
          meta: {
            title: '博客的兑换券失效中心'
          },
          name: 'CouponInvalid',
          component: CouponInvalid,
        }]
    },{
      path: '/walletSecurity',
      meta: {
        title: '博客的账户开通中心'
        },
        name: 'WalletSecurity',
        component: WalletSecurity
    },
    {
      path: '/pay',
      meta: {
        title: '博客的支付中心'
      },
      name: 'Pay',
      component: Pay
    },
    {
      path: '/paySuccess',
      meta: {
        title: '博客的支付成功中心'
      },
      name: 'PaySuccess',
      component: PaySuccess
    },
    {
      path: '/payRedirect',
      meta: {
        title: '博客的支付中心'
      },
      name: 'PayRedirect',
      component: PayRedirect
    },
    {
      path: '/userMain',redirect: '/userMain/userPublish',
      meta: {
        title: '博客的用户主页面'
      },
      name: 'UserMain',
      component: UserMain,
      children: [{
        path: 'userPublish',
        meta: {
          title: '博客的发表中心'
        },
        name: 'UserPublish',
        component: UserPublish,

      },{
        path: 'userManage',
        meta: {
          title: '博客的管理中心'
        },
        name: 'UserManage',
        component: UserManage,
      },{
        path: 'userCollection',
        meta: {
          title: '博客的收藏中心'
        },
        name: 'UserCollection',
        component: UserCollection
      },
        {
          path: 'userUpdate/:id',
          meta: {
            title: '博客的修改中心'
          },
          name: 'UserUpdate',
          component: UserUpdate
        }
      ]
    },
  ]
})
