// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import jquery1 from 'jquery'
import  'bootstrap/dist/css/bootstrap.css'
import 'bootstrap/dist/js/bootstrap.min'
import 'bootstrap/dist/js/bootstrap'
import 'bootstrap/dist/js/npm'
import  axios from "./api/axios"
import qs from 'qs'
import ViewUI from 'view-design'
import elementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'view-design/dist/styles/iview.css'
import Alert from './api/Alert'
import mavonEditor from 'mavon-editor'
import hljs from "highlight.js"
import 'highlight.js/styles/default.css'
import VCharts from 'v-charts'
import nocaptcha from 'vue-nocaptcha'

Vue.use(nocaptcha);

Vue.use(VCharts);

Vue.use(Alert);

Vue.prototype.$qs = qs;

Vue.use(jquery1);

Vue.config.productionTip = false;

Vue.prototype.$http=axios;

Vue.use(ViewUI);

Vue.use(elementUI);

Vue.use(mavonEditor);

// 定义自定义指令 highlight 代码高亮
Vue.directive('highlight',function (el) {
  let blocks = el.querySelectorAll('pre code');
  blocks.forEach((block)=>{
    hljs.highlightBlock(block)
  })
})

//钩子方法验证Token是否存在
router.beforeEach((to, from, next) => {
  //改变网页的标题
  window.document.title = to.meta.title;
  //强制登录并且检测token
  let token = localStorage.getItem('token');
  if (!token && to.path !== '/login' && to.path !== '/register' && to.path !== '/') {
    return next('/login')
  }
  ViewUI.LoadingBar.start();
  next();
});

//返回页面的顶端
router.afterEach(route => {
  window.scrollTo(0,0);
  ViewUI.LoadingBar.finish();
});

/* eslint-disable no-new */
new Vue({
  el: '#app',//document.getElementById('app')=#app，el用于指定一个页面已存在的DOM元素来挂载Vue实例
  router,
  template: '<App/>',
  components: { App }
})


