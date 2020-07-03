
import axios from 'axios'
import router from '../router/index.js'
import Qs from 'qs'
import ViewUI from 'view-design'


//必须添加可以忽略,路径重写url.
let host = 'http://op.mddyg.com/'

host = window.location.protocol === 'file:' ? host : ''

if (process.env.NODE_ENV === 'development') {

  host = ''

}
//默认的response拦截器也可改写为request请求体拦截器(下面的函数基本都加上了TOKEN的请求体,所以没有改写.)
//axios.interceptors.response.use(response => response, error => Promise.resolve(error.response))

// 添加响应拦截器
axios.interceptors.response.use(function (response) {
  // 对响应数据做点什么
  return response;
}, function (error) {
  // 对响应错误做点什么
  if (error.response.status === 401){
    router.replace({name: 'Login'}).then(res =>{
      alert('身份验证失效,请重新登录!');
      window.localStorage.clear();
    })
  }else{
    ViewUI.LoadingBar.error();
    alert('服务开了小差,请稍后再试!');
  }
  // if (error.response.status === 500)
  return Promise.reject(error);
});



export default {

  post(url, data) {

    return axios({

      method: 'post',

      url: url,

      baseURL: host,

      data: Qs.stringify(data),

      timeout: 30000,

      headers: {

        'X-Requested-With': 'XMLHttpRequest',

        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',

        'TOKEN': window.localStorage.getItem('token'),
      }

    })

  },

  uploadFile(url, data) {

    return axios({

      method: 'post',

      url: url,

      baseURL: host,

      data: data,

      timeout: 30000,

      headers: {

        'X-Requested-With': 'XMLHttpRequest',

        'Content-Type': 'multipart/form-data; boundary=----WebKitFormBoundaryZpsWTsOiRHI0TBW7',

        'TOKEN': window.localStorage.getItem('token'),

      }

    })

  },

  get(url, params) {

    return axios({

      method: 'get',

      url: url,

      baseURL: host,

      params,

      timeout: 30000,

      headers: {

        'X-Requested-With': 'XMLHttpRequest',

        'TOKEN': window.localStorage.getItem('token'),
      }

    })

  }

}

