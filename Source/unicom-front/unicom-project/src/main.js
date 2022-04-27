import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import $ from 'jquery'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css';
import './assets/fonts/iconfont.css'
import './assets/css/global.css'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {message} from "./assets/js/resetMessa";
import global from './assets/js/global'
import preventReClick from './assets/js/preventRepeatClick'

Vue.config.productionTip = false;
Vue.prototype.Global = global;
 axios.defaults.baseURL = './';//打包的配置
//axios.defaults.baseURL = './unicom/';//本地的配置
Vue.use(ElementUI);
Vue.use(preventReClick);
Vue.prototype.$message = message;
axios.interceptors.request.use(config => {
  NProgress.start();
  config.headers.Token = window.sessionStorage.getItem('token');
  return config
}, error => {
  return Promise.reject(error)
});
axios.interceptors.response.use(config => {
  NProgress.done();
  if (config.headers.token != null) {
    window.sessionStorage.setItem('token', config.headers.token);
  }
  if (config.data.code === 401) {
    message({
      type: 'error',
      message: '身份过期,请重新登录！',
      duration: 2000,
      onClose: () => {
        window.sessionStorage.clear();
        router.replace({
          path: '/login'
        });
        window.location.reload()
      }
    });
  } else if (config.data.code === 403) {
    message.error("该用户没有权限！");
  } else {
    return config
  }
}, error => {
  if (error && error.response) {
    switch (error.response.status) {
      case 400:
        message.error("找不到这个请求，请检查！");
        break;
      case 401:
        message({
          type: 'error',
          message: '身份过期,请重新登录！',
          duration: 2000,
          onClose: () => {
            window.sessionStorage.clear();
            router.replace({
              path: '/login'
            });
            window.location.reload()
          }
        });
        break;
      case 500:
        message({
          type: 'error',
          message: '账号被占用或者连接中断，请重新登录！',
          duration: 2000,
          onClose: () => {
            window.sessionStorage.clear();
            router.replace({
              path: '/login'
            });
            window.location.reload()
          }
        });
        break;
      default:
        console.log(error)
    }
  }
});
Vue.prototype.$http = axios;
new Vue({
  router,
  render: h => h(App)
}).$mount('#app');
