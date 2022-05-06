<template>
  <el-container class="home-container">
    <el-header>
      <div>
        <strong>{{Global.projectName}} <i style="font-style: normal">{{accountTypeLogin}}</i></strong>
      </div>
      <div @click="logoutHandle" style="font-size: 17px;cursor: pointer;">
        <span>{{loginName}}</span><i class="el-icon-switch-button"></i>
      </div>
    </el-header>
    <el-container style="height: 500px;">
      <el-aside :width="isCollapse ? '64px':'220px'">
        <el-menu router
                 :default-openeds="defaultOpen"
                 unique-opened :collapse="isCollapse" :collapse-transition="false" :default-active="$route.path">
          <menu-tree :data="menuList"></menu-tree>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
  import menuTree from "../components/MenuTree";
  import Cookies from "js-cookie";
  export default {
    name: 'Home',
    components: {
      menuTree
    },
    data() {
      return {
        accountTypeLogin: '',
        loginName: '',
        menuList: [],
        caozuoList: [
          {id: 130, authName: "应用管理", path: 'applicationManagerUser', children: [
            ], order: 11},
        ],
        auditList: [
          {id: 127, authName: "日志告警", path: 'log',
            children: [
              {id: 134, authName: '日志审计', path: 'log', children: [], order: null},
              {id: 133, authName: '告警信息', path: 'alert', children: [], order: null}
            ],
            order: 8
          },
          {id: 129, authName: "关于", path: 'status', children: [], order: 5}
        ],
        adminList: [
          {
            id: 120,
            authName: "用户管理",
            path: 'user',
            children: [
              {id: 100, authName: '系统用户', path: 'user', children: [], order: null},
              {id: 102, authName: '终端用户', path: 'deviceUser', children: [], order: null},
              {id: 104, authName: "分组管理", path: 'group', children: [], order: null},
            ],
            order: 1
          },
          {
            id: 128, authName: "权限管理", path: 'deviceAuth',
            children: [
              {id: 111, authName: '终端用户权限', path: 'deviceAuth', children: [], order: null},
              {id: 116, authName: "分组权限", path: 'groupAuth', children: [], order: null},
              {id: 112, authName: '资源管理', path: 'resources', children: [], order: null},
              {id: 125, authName: "访问控制", path: 'access', children: [], order: null},
            ],
            order: 9
          },
          {id: 122, authName: "密钥源配置", path: 'keySource', children: [], order: 3},
          {id: 132, authName: "量子密钥统计", path: 'total', children: [], order: 13},
          {id: 130, authName: "应用管理", path: 'applicationUser', children: [], order: 11},
          {id: 126, authName: "系统管理", path: 'manager',children: [],order: 7},
          {id: 127, authName: "日志告警", path: 'log',
            children: [
              {id: 134, authName: '日志审计', path: 'log', children: [], order: null},
              {id: 133, authName: '告警信息', path: 'alert', children: [], order: null}
            ],
            order: 8
          },
          {id: 129, authName: "关于", path: 'status', children: [], order: 10},
        ],
        isCollapse: false,
        defaultOpen: [120],
      }
    },
    created() {
      this.loginName = Cookies.get('loginName');
      let aa = Cookies.get('accountTypeLogin');
      if (aa === '9') {
        this.menuList = this.adminList;
        this.accountTypeLogin = "(管理员)";
      } else if (aa === '2') {
        this.menuList = this.auditList;
        this.accountTypeLogin = "(安全员)";
      } else if (aa === '1') {
        this.menuList = this.caozuoList;
        this.accountTypeLogin = "(应用管理员)";
      }
    },
    methods: {
      async logoutHandle() {
        const confirmResult = await this.$confirm(
          '确定要退出登录？', '提示', {
            confirmButtonText: '确定',
            cancelButtontext: '取消',
            type: 'warning',
          }
        ).catch(err => err);
        if (confirmResult !== 'confirm') {
          return
        }
       Cookies.remove();
        this.menuList.splice(0, this.menuList.length);
        this.$router.push('/login')
      },
    }
  }
</script>
<style lang="less">
  .home-container {
    height: 100%;
    .el-header {
     background-color: #72b3f6;
      display: flex;
      justify-content: space-between;
      padding-left: 0;
      align-items: center;
      font-size: 20px;
      color: #fff;
      > div {
        display: flex;
        align-items: center;
        padding-left: 20px;
        span {
          margin: 0 10px;
        }
        i {
          font-size: 17px;
        }
      }
    }

    .el-aside {
      background-color: #fff;
      .el-menu {
        border-right: none;
      }
    }
    .el-main {
      background-color: #f5f7fa;
    }
    .iconfont {
      margin-right: 10px;
    }
    .el-menu-item.is-active {
      background-color: #f2f6fc;
      color: #409EFF;
      border-right: 3px solid #409EFF;
    }
  }
</style>

