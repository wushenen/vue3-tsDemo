<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>关于</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <div>
        <p><span>系统状态:</span><span>{{info.systemStatus}}</span></p>
        <p><span>系统版本:</span><span>{{info.systemVersion}}</span></p>
        <p><span>数据库:</span><span>{{info.mysqlVersion}}</span></p>
      </div>
    </el-card>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        info: '',
        version:{}
      }
    },
    beforeRouteEnter: (to, form, next) => {
      const loginType = window.sessionStorage.getItem('accountTypeLogin');
      if (loginType === '1' || loginType === '9' || loginType === '2') {
        next()
      }else{
        return next('/404')
      }
    },
    methods: {
      async getInfo() {
        const {data: res} = await this.$http.get(`system/getVersion?ts=${new Date().getTime()}`);
        if (res.code === 0) {
          this.info = res.data;
        }
        else{
          return this.$message.error('获取状态信息失败！')
        }
      }
    },
    created() {
      this.getInfo()
    }
  }

</script>
<style lang="less" scoped>
  p {
    line-height: 35px;
    padding: 10px 10px;
    font-family: serif;
    font-size: 18px;
    > span {
      margin-right: 10px;
    }
  }
</style>
