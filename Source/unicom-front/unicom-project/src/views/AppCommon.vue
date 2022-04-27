<template>
  <div style="min-width: 1100px;">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item v-if="$route.query.urlType" :to="{ path: '/applicationManagerUser' }">应用管理</el-breadcrumb-item>
      <el-breadcrumb-item v-else :to="{ path: '/applicationUser' }">应用管理</el-breadcrumb-item>
      <el-breadcrumb-item>终端列表</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row style="margin-bottom: 20px">
        <el-button type="primary" @click="addDialogVisible = true">绑定</el-button>
      </el-row>
      <el-table :data="userlist" stripe>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="终端ID" prop="deviceName" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="终端IP">
          <template slot-scope="scope">
            <span>{{scope.row.deviceIp == null ||  scope.row.deviceIp == '' ? '-' : scope.row.deviceIp}}</span>
          </template>
        </el-table-column>
        <el-table-column label="密钥使用量" prop="keyNum">
          <template slot-scope="scope">
            <span>{{scope.row.keyNum == null ||  scope.row.keyNum == '' ? '-' : scope.row.keyNum}}</span>
          </template>
        </el-table-column>
        <el-table-column label="在线时间" prop="workTime" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span>{{scope.row.workTime == null ||  scope.row.workTime == '' ? '-' : scope.row.workTime}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200px">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="deleteByName(scope.row.deviceId)">解绑</el-button>
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

    <el-dialog title="绑定终端用户" :visible.sync="addDialogVisible" width="500px" @close="addDialogClosed" :close-on-click-modal = "false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef"
               label-width="100px">
        <el-form-item label="终端用户：" prop="value">
          <el-select ref="inputValue"
                     v-model="addForm.value"
                     multiple
                     placeholder="请选择用户名"
                     :loading="loading"
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
        <el-button type="primary" v-preventReClick="1000" @click="submitAdd">确 定</el-button>
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
      }
    },
    beforeRouteEnter: (to, form, next) => {
      const loginType = window.sessionStorage.getItem('accountTypeLogin');
      if (loginType === '9' || loginType === '1') {
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
        const {data: res} = await this.$http.get(`status/getCurrentAppDeviceInfo/${this.queryInfo.pagenum}/${this.queryInfo.pagesize}?ts=${new Date().getTime()}`, {
          params: {'appId': this.$route.query.id}
        });
        if (res.code !== 0) return this.$message.error('获取用户信息失败！');
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
      submitAdd() {
        this.$refs.addFormRef.validate(async valid => {
          if (!valid) return;
          const {data: res} = await this.$http.post('appDevice/addAppDevice', {
            'appId': this.$route.query.id,
            'deviceIds': this.addForm.value
          });
          if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '绑定失败！');
          this.$message.success('绑定成功!');
          this.addDialogVisible = false;
          this.getUserList()
        })
      },
      async deleteByName(id) {
        const confirmResult = await this.$confirm(
          '确定要解绑？', '提示', {
            confirmButtonText: '确定',
            cancelButtontext: '取消',
            type: 'warning',
          }
        ).catch(err => err);
        if (confirmResult !== 'confirm') {
          return
        }
        const {data: res} = await this.$http.post('appDevice/deleteAppDevice',
          {'appId': parseInt(this.$route.query.id),'deviceIds':[id]}
        );
        if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '解绑失败！');
        this.$message.success('解绑成功！');
        this.getUserList()
      },
    }
  }
</script>
<style lang="less" scoped>
  .el-select {
    width: 100%;
  }

  .demo-table-expand {
    font-size: 0;
  }

  .demo-table-expand label {
    width: 120px;
    color: #99a9bf;
  }

  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }

  .el-form--inline .el-form-item__content > span {
    display: inline-block;
    max-width: 500px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
</style>
