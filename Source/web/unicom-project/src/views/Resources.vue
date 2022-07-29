<template>
  <div style="min-width: 1100px">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>权限管理</el-breadcrumb-item>
      <el-breadcrumb-item>资源管理</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-table :data="userList"  stripe>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="资源名称" prop="apiName" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="描述" prop="comments" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span>{{scope.row.comments === null || scope.row.comments === '' ? '-' : scope.row.comments}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="goDetail(scope.row.apiId)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        userList: [],
      }
    },
    beforeRouteEnter: (to, form, next) => {
      const loginType = window.sessionStorage.getItem('accountTypeLogin');
      if (loginType === '9') {
        next()
      }else{
        return next('/404')
      }
    },
    created() {
      this.getUserList();
    },
    methods: {
      async getUserList() {
        const {data: res} = await this.$http.post('resource/getCategory');
        if (res.code !== 0) return this.$message.error('获取信息失败！');
        this.userList = res.data;
      },
      async deleteById(id){
        const confirmDelete = await this.$confirm(
          "此操作将删除该资源，是否继续？",
          "提示",
          {
            confimrButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
        ).catch(err => err);
        if (confirmDelete !== "confirm") return;
        const {data: res} = await this.$http.post(`resource/deleteResource?apiId=${id}`);
        if (res.code !== 0) return this.$message.error(res.msg !== '' && res.msg !== null ? res.msg : '删除失败！');
        this.$message.success('删除成功！');
        this.getUserList()
      },
      goDetail(id) {
        this.$router.push({path: '/resourceDetail', query: {'apiId': id}})
      }
    }
  }
</script>
<style lang="less" scoped>
  .el-select {
    display: block;
  }
</style>
