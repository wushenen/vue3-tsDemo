<template>
  <div style="min-width: 1000px">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item :to="{path:'/group'}">分组管理</el-breadcrumb-item>
      <el-breadcrumb-item>终端用户</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row>
        <el-button type="primary" @click="addDialogVisible=true">添加成员</el-button>
        <el-button v-show="multipleSelection.length > 0" @click="batchDeleteBuild" plain>批量删除</el-button>
      </el-row>
      <el-table :data="userList" ref="multipleTable" :row-key="getRowKeys" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" :reserve-selection="true" width="55">
        </el-table-column>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="用户名" prop="deviceName"></el-table-column>
        <el-table-column label="备注" :show-overflow-tooltip="true" prop="comments">
          <template slot-scope="scope">
            <span>{{scope.row.comments === null || scope.row.comments === '' ? '-' : scope.row.comments}}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime"> </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="deleById(scope.row.id)">删除</el-button>
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
    <el-dialog title="添加成员" :visible.sync="addDialogVisible" width="550px" @close="addDialogClosed" :close-on-click-modal = "false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="110px">
        <el-form-item label="终端用户：" prop="value">
          <el-select ref="inputValue"
                     v-model="addForm.value"
                     multiple
                     placeholder="请选择用户名"
                     :loading="loading"
                     clearable
                     @visible-change="remoteMethod($event)">
                     
            <el-option
              v-for="item in options"
              :key="item.deviceId"
              :label="item.deviceName"
              :value="item.deviceId">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" v-preventReClick="1000"  @click="submitAdd">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        userList: [],
        queryInfo: {
          pagenum: 1,
          pagesize: 10
        },
        total: 0,
        addDialogVisible: false,
        options: [],
        loading: false,
        addForm: {
          value: [],
        },
        addFormRules: {
          value: [{required: true, message: '必填项不能为空', trigger: 'change'}],
        },
        multipleSelection: [],
        deleteCode: [],
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
        const {data: res} = await this.$http.get(`groupDeviceUser/list/${this.queryInfo.pagenum}/${this.queryInfo.pagesize}?ts=${new Date().getTime()}`, {
          params: {"groupId": this.$route.query.groupId}
        });
        if (res.code !== 0) return this.$message.error('获取用户信息失败！');
        this.userList = res.data.list;
        this.total = res.data.total;
      },
      handleSizeChange(newSize) {
        this.queryInfo.pagesize = newSize;
        this.getUserList()
      },
      handleCurrentChange(newPage) {
        this.queryInfo.pagenum = newPage;
        this.getUserList()
      },
      async remoteMethod(even) {
        if (even !== true) return this.options = [];
          this.loading = true;
          const {data: res} = await this.$http.get(`device/queryDeviceUser?ts=${new Date().getTime()}`, {
            params: {'deviceName': ''}
          });
          if (res.code !== 0) return this.options = [];
          this.loading = false;
          this.options = res.data;
      },
      addDialogClosed() {
        this.$refs.addFormRef.resetFields();
        this.options = [];
      },
      async submitAdd() {
        this.$refs.addFormRef.validate(async valid => {
          if (!valid) return;
          const {data: res} = await this.$http.post('groupDeviceUser/add', {
            'groupId': this.$route.query.groupId, 'deviceId': this.addForm.value
          });
          if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '添加失败！');
          this.$message.success('添加成功！');
          this.addDialogVisible = false;
          this.getUserList();
        })
      },
      async deleById(id) {//单个删除
        const confirmResult = await this.$confirm(
          `此操作将删除该用户，是否继续？`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
        ).catch(err => err);
        if (confirmResult !== 'confirm') return;
        this.deleteCode.push(id);
        this.deleteMethod();
      },
      getRowKeys(row) {
        return row.id
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      async batchDeleteBuild() {  //批量删除选中数据方法
        const confirmDelete = await this.$confirm(
          "此操作将删除勾选用户，是否继续？",
          "提示",
          {
            confimrButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
        ).catch(err => err);
        if (confirmDelete !== "confirm") return;
        for (let i = 0; i < this.multipleSelection.length; i++) {//将选中的数据推到deleteCode数组中
          this.deleteCode.push(this.multipleSelection[i].id);
        }
       this.deleteMethod();
      },
      async deleteMethod(){
        const {data: res} = await this.$http.post('groupDeviceUser/delete', {
          'ids': this.deleteCode
        });
        if (res.code !== 0) return this.$message.error(res.msg !== '' && res.msg !== null ? res.msg : '删除失败！');
        this.$message.success('删除成功！');
        this.deleteCode = [];
        this.$refs.multipleTable.clearSelection();
        this.getUserList()
      }
    }
  }
</script>
<style lang="less" >
  .el-select {
    display: block;
    width: 100%;
   
  }
   .el-tag--small { 
      height: 25px !important;
      background-color: pink;
     }
     .el-select-dropdown{
       top:300px !important;
     }
    

</style>
