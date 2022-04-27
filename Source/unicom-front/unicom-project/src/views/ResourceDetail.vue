<template>
  <div style="min-width: 1100px">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>权限管理</el-breadcrumb-item>
      <el-breadcrumb-item :to="{path:'resources'}">资源管理</el-breadcrumb-item>
      <el-breadcrumb-item>资源详情</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-table :data="userList" stripe>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="接口资源名称" prop="apiName" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="接口URL" prop="apiURL" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="描述" prop="comments" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span>{{scope.row.comments === null || scope.row.comments === '' ? '-' : scope.row.comments}}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog :title="title" :visible.sync="addFormDialogVisible" width="500px" @close="addDialogClosed" :close-on-click-modal = "false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="120px">
        <el-form-item label="接口资源名称" prop="apiName">
          <el-input v-model.trim="addForm.apiName"></el-input>
        </el-form-item>
        <el-form-item label="接口URL" prop="apiURL">
          <el-input v-model.trim="addForm.apiURL"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="comments">
          <el-input type="textarea" :rows="8" v-model.trim="addForm.comments"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addFormDialogVisible = false">取 消</el-button>
        <el-button type="primary"  v-preventReClick="1000" @click="addEnsure">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        userList: [],
        title: '',
        editId: null,
        addFormDialogVisible: false,
        addForm: {
          apiName: '',
          apiURL: '',
          comments: '',
        },
        addFormRules: {
          apiName: [{required: true, message: '必填项不能为空', trigger: 'blur'},{ min: 1, max: 32, message: '长度不能超过32个字符', trigger: 'blur' }],
          apiURL: [{required: true, message: "必填项不能为空", trigger: 'blur'}],
          comments: [{min: 0, max: 128, message: '长度不能超过128个字符', trigger: 'blur'}],
        },
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
        const {data: res} = await this.$http.get(`resource/getApiResource?apiId=${this.$route.query.apiId}`);
        if (res.code !== 0) return this.$message.error((res.msg !== '' && res.msg !== null) ? res.msg : '获取权限失败！');
        this.userList = res.data;
      },
      async deleteById(id){
        const confirmDelete = await this.$confirm(
          "此操作将删除该接口资源，是否继续？",
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
      openAdd() {
        this.title = '添加接口资源';
        this.addFormDialogVisible = true;
      },
      addDialogClosed() {
        this.$refs.addFormRef.resetFields();
      },
      addEnsure() {
        this.$refs.addFormRef.validate(async valid => {
          if (!valid) return;
          let reqData;
          let reqUrl;
          if (this.title === '添加接口资源') {
            reqData = {
              'apiName': this.addForm.apiName,
              'comments': this.addForm.comments,
              'apiURL':this.addForm.apiURL,
              'parentId':this.$route.query.apiId
            };
            reqUrl = `resource/addResource`
          } else {
            reqData = {
              'apiName': this.addForm.apiName,
              'comments': this.addForm.comments,
              'apiURL':this.addForm.apiURL,
              'apiId': this.editId,
              'parentId':this.$route.query.apiId
            };
            reqUrl = `resource/updateResource`
          }
          const alertMsg = this.title === '添加接口资源' ? '添加' : '编辑';
          const {data: res} = await this.$http.post(reqUrl, reqData);
          if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : alertMsg + '失败！');
          this.$message.success(alertMsg + '成功！');
          this.addFormDialogVisible = false;
          this.getUserList();
        })
      },
      editById(row) {
        this.editId = row.apiId;
        this.addForm.apiName = row.apiName;
        this.addForm.apiURL = row.apiURL;
        this.addForm.comments = row.comments;
        this.title = '编辑接口资源';
        this.addFormDialogVisible = true;
      },
    }
  }
</script>
<style lang="less" scoped>
  .el-select {
    display: block;
  }
</style>
