<template>
  <div style="min-width: 900px">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>权限管理</el-breadcrumb-item>
      <el-breadcrumb-item>访问控制</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row>
        <el-button type="primary" @click="addDialogVisible=true">添加</el-button>
      </el-row>
      <el-table :data="list" stripe>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="IP" prop="ipInfo"></el-table-column>
        <el-table-column label="创建时间" prop="createTime"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" :disabled="scope.row.ip === '0:0:0:0:0:0:0:1'" type="danger"
                       @click="removeUserById(scope.row.ipInfo)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum"
        :page-sizes="[5,10,15,20]"
        :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </el-card>
    <el-dialog title="添加" :visible.sync="addDialogVisible" width="500px" @close="addDialogClosed" :close-on-click-modal = "false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" status-icon label-width="70px">
        <el-form-item label="提示:" >
          <span>请输入IP（如x.x.x.x）、IP范围（用-分隔,如x.x.x.x-x.x.x.x）或网段（用/分隔，如x.x.x.x/x）</span>
        </el-form-item>
        <el-form-item label="IP" prop="ipInfo">
          <el-input v-model.trim="addForm.ipInfo"  maxlength="16"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addIP">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import Cookies from 'js-cookie'
  export default {
    data() {
      return {
        list: [],
        queryInfo: {
          pagenum: 1,
          pagesize: 10
        },
        total: 0,
        addDialogVisible: false,
        addForm: {
          ipInfo: '',
        },
        addFormRules: {
          ipInfo: [{required: true, message:'请输入IP', trigger: 'blur'}],
        },
      }
    },
    beforeRouteEnter: (to, form, next) => {
      const loginType =Cookies.get('accountTypeLogin');
      if (loginType === '9') {
        next()
      }else{
        return next('/404')
      }
    },
    created() {
      this.getList()
    },
    methods: {
      async getList() {
        const {data: res} = await this.$http.get(`ip/getAllIps/${this.queryInfo.pagenum}/${this.queryInfo.pagesize}?ts=${new Date().getTime()}`);
        if (res.code !== 0) return this.$message.error('获取表格信息失败！');
        this.list = res.data.list;
        this.total = res.data.total;
      },
      handleSizeChange(newSize) {
        this.queryInfo.pagesize = newSize;
        this.getList()
      },
      handleCurrentChange(newPage) {
        this.queryInfo.pagenum = newPage;
        this.getList()
      },
      async removeUserById(id) {
        const confirmResult = await this.$confirm(
          `确定要删除IP：${id}？`, '提示', {
            confirmButtonText: '确定',
            cancelButtontext: '取消',
            type: 'warning'
          }
        ).catch(err => err);
        if (confirmResult !== 'confirm') {
          return
        }
        const {data: res} = await this.$http.post('ip/delIpById',{'ipInfo': id});
        if (res.code !== 0) return this.$message.error(res.msg !== '' && res.msg !== null ? res.msg : '删除失败！');
        this.$message.success('删除成功！');
        this.getList()
      },
      addIP() {
        this.$refs.addFormRef.validate(async valid => {
          if (!valid) return;
          const {data: res} = await this.$http.post('ip/addIp', this.addForm);
          if (res.code !== 0) return this.$message.error(res.msg !== '' && res.msg !== null ? res.msg : '添加失败！');
          this.$message.success("添加成功！");
          this.addDialogVisible = false;
          this.getList()
        })
      },
      addDialogClosed() {
        this.$refs.addFormRef.resetFields();
      },
    }
  }
</script>
<style>

</style>
