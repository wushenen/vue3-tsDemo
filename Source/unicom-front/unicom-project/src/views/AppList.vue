<template>
  <div style="min-width: 1100px;">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/applicationUser' }">应用管理</el-breadcrumb-item>
      <el-breadcrumb-item>绑定应用管理员</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row style="margin-bottom: 20px">
        <el-button type="primary" @click="editMethod">绑定</el-button>
        <el-button type="danger" :disabled="!multipleSelection.length > 0" @click="deleteMethod">解绑</el-button>
      </el-row>
      <el-table :data="userlist" ref="multipleTable" stripe :row-key="getRowKeys"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" :reserve-selection="true" width="55"></el-table-column>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="应用管理员" prop="userName" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="备注" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span>{{scope.row.comments == null ||  scope.row.comments == '' ? '-' : scope.row.comments}}</span>
          </template>
        </el-table-column>
        <el-table-column label="添加时间" prop="createTime">
          <template slot-scope="scope">
            <span>{{scope.row.createTime == null ||  scope.row.createTime == '' ? '-' : scope.row.createTime}}</span>
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
    <el-dialog title="绑定应用管理员" :visible.sync="keyDialogVisible" width="500px" @close="keyDialogClosed" :close-on-click-modal = "false">
      <el-form :model="keyForm" :rules="keyFormRules" ref="keyFormRef" label-width="100px">
        <el-form-item label="应用管理员" prop="userManager">
          <el-select multiple clearable  style="width: 100%" v-model="keyForm.userManager" placeholder="请选择">
            <el-option
              v-for="item in appList"
              :key="item.id"
              :label="item.userName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="keyDialogVisible = false">取 消</el-button>
        <el-button type="primary" v-preventReClick="1000" @click="addMethod">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        userlist: [],
        queryInfo: {
          query: '',
          pagenum: 1,
          pagesize: 10
        },
        total: 0,
        appList:[],
        multipleSelection: [],
        addCode: [],
        keyDialogVisible: false,
        keyForm: {
          userManager: [],
        },
        keyFormRules: {
          userManager: [{required: true, message: '请选择', trigger: 'change'}],
        },
      }
    },
    beforeRouteEnter: (to, form, next) => {
      const loginType = window.sessionStorage.getItem('accountTypeLogin');
      if (loginType === '9') {
        next()
      } else {
        return next('/404')
      }
    },
    created() {
      this.getUserList();
    },
    methods: {
      async getUserList() {
        const {data: res} = await this.$http.get(`userApp/getAppManagers/${this.queryInfo.pagenum}/${this.queryInfo.pagesize}?ts=${new Date().getTime()}`, {
          params: {'appId': this.$route.query.id}
        });
        if (res.code !== 0) return this.$message.error('获取应用管理信息失败！');
        this.userlist = res.data.list;
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
      keyDialogClosed() {
        this.$refs.keyFormRef.resetFields();
      },
      async editMethod() {
        const {data: res} = await this.$http.get('user/getAppManager');
        if (res.code !== 0) return this.$message.error(res.msg !== '' && res.msg !== null ? res.msg : '获取应用管理员失败！');
        this.appList=res.data;
        this.keyDialogVisible = true;
      },
      async addMethod() {
        const {data: res} = await this.$http.post('userApp/addUserApp', {
          'appId': this.$route.query.id, 'userIds': this.keyForm.userManager
        });
        if (res.code !== 0) return this.$message.error(res.msg !== '' && res.msg !== null ? res.msg : '绑定失败！');
        this.$message.success('绑定成功！');
        this.keyDialogVisible = false;
        this.getUserList();
      },
      getRowKeys(row) {
        return row.userId
      },
      handleSelectionChange(val) {//批量选中数据
        this.multipleSelection = val;
      },
      async deleteMethod() {
        for (let i = 0; i < this.multipleSelection.length; i++) {
          this.addCode.push(this.multipleSelection[i].userId);
        }
        const {data: res} = await this.$http.post('userApp/deleteUserApp', {
          'appId': this.$route.query.id, 'userIds': this.addCode
        });
        if (res.code !== 0) return this.$message.error(res.msg !== '' && res.msg !== null ? res.msg : '解绑失败！');
        this.$message.success('解绑成功！');
        this.addCode = [];
        this.$refs.multipleTable.clearSelection();
        this.getUserList();
      }
    }
  }
</script>
<style lang="less" scoped>

</style>
