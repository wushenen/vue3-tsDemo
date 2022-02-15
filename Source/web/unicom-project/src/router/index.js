import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
// import Home from '../views/Home.vue'
const Home = () => import("../views/Home.vue");
const Welcome = () => import("../views/Welcome.vue");
const User = () => import("../views/User.vue");
const DeviceUser = () => import("../views/DeviceUser.vue");
const Log = () => import("../views/Log.vue");
const Access = () => import("../views/Access.vue");
const ApplicationUser = () => import("../views/ApplicationUser.vue");
const Manager = () => import("../views/Manager.vue");
const Status = () => import("../views/Status.vue");
const KeySource = () => import("../views/KeySource.vue");
const KeyInfo = () => import("../views/KeyInfo.vue");
const DeviceAuth = () => import("../views/DeviceAuth.vue");
const Resources = () => import("../views/Resources.vue");
const ResourceDetail = () => import("../views/ResourceDetail.vue");
const Group = () => import("../views/Group.vue");
const GroupDetail = () => import("../views/GroupDetail.vue");
const Pwsp = () => import("../views/Pwsp.vue");
const Total = () => import("../views/Total.vue");
const Alert = () => import("../views/Alert.vue");
const Qems = () => import("../views/Qems.vue");
const GroupAuth = () => import("../views/GroupAuth.vue");
const NoFound = () => import('../views/NoFound.vue');
const ApplicationManagerUser = () => import("../views/ApplicationManagerUser.vue");
const AppList = () => import("../views/AppList.vue");
import Cookies from 'js-cookie'

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '*',
    name: 'NoFound',
    component: NoFound
  },
  {
    path: '/home',
    component: Home,
    redirect: '/welcome',
    children: [
      {path: '/welcome', component: Welcome},
      {path: '/user', component: User},
      {path: '/deviceUser', component: DeviceUser},
      {path: '/applicationUser', component: ApplicationUser},
      {path: '/log', component: Log},
      {path: '/manager', component: Manager},
      {path: '/status', component: Status},
      {path: '/access', component: Access},
      {path: '/keySource', component: KeySource},
      {path: '/keyInfo', component: KeyInfo},
      {path: '/deviceAuth', component: DeviceAuth},
      {path: '/resources', component: Resources},
      {path: '/resourceDetail', component: ResourceDetail},
      {path: '/total', component: Total},
      {path: '/alert', component: Alert},
      {path: '/group', component: Group},
      {path: '/groupDetail', component: GroupDetail},
      {path: '/pwsp', component: Pwsp},
      {path: '/qems', component: Qems},
      {path: '/groupAuth', component: GroupAuth},
      {path: '/applicationManagerUser', component: ApplicationManagerUser},
      {path: '/appList', component: AppList},
    ]
  }
];

const router = new VueRouter({
  routes,
});

router.beforeEach((to, from, next) => {
//to将要访问的路径，from代表从哪一个路径跳转而来
  if (to.path === '/login') return next();
  const tokenStr = window.sessionStorage.getItem('token');
  if (!tokenStr) {
    window.sessionStorage.clear();
    return next('/login');
  }
  next()
});
export default router
