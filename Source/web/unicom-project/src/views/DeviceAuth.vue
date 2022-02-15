<template>
  <div class="deviceAuth">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>权限管理</el-breadcrumb-item>
      <el-breadcrumb-item>终端用户权限</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row :gutter="20">
        <el-col :span="8" style="float: right">
          <el-input placeholder="请输入用户名" v-model.trim="queryInfo.query" clearable @clear="getUserList" @keyup.enter.native="getUserList">
            <el-button slot="append" icon="el-icon-search"  v-preventReClick="1000" @click="getUserList"></el-button>
          </el-input>
        </el-col>
      </el-row>
      <el-table :data="userList" stripe :row-key="getRowKeys"
                :expand-row-keys="expands" @expand-change="expandChange">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-row v-if="props.row.ruleItemData.length > 0">
              <span style="display:inline-block;margin:5px;" v-for="item in props.row.ruleItemData"
                    :key="item.apiId">
              <el-tag closable  @close="removeRightById(props.row, item.authId)">{{item.apiName}}</el-tag>
            </span>
            </el-row>
            <el-row v-else>
              暂无配置权限
            </el-row>
          </template>
        </el-table-column>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="用户名" prop="deviceName"></el-table-column>
        <el-table-column label="时间" prop="createTime">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="openAdd(scope.row)">添加权限</el-button>
            <el-button size="mini" type="danger" @click="removeAllById(scope.row.deviceId)">删除权限</el-button>
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
    <el-dialog title="添加权限" :visible.sync="addDialogVisible" width="600px" @close="addDialogClosed">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="90px">
        <el-form-item label="选择权限:" prop="checkedAuth">
          <div class="block">
            <el-cascader style="width: 100%"
              v-model="addForm.checkedAuth"
              :options="options"
              :props="prop"
              :show-all-levels="false"
              clearable
              ></el-cascader>
          </div>
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
        getRowKeys(row) {
          return row.deviceId
        },
        expands: [],
        userList: [],
        queryInfo: {
          pagenum: 1,
          pagesize: 10,
          query:''
        },
        total: 0,
        addDeviceId: '',
        addDialogVisible: false,
        checkaddCode: [],
        addForm: {
          checkedAuth: [],
        },
        addFormRules: {},
        options: [],
        prop: {
          multiple: true,
          emitPath:false,
          value: 'apiId',
          label: 'apiName',
          children: 'apiResources',
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
        const {data: res} = await
          this.$http.get(`device/getAllDevice/${this.queryInfo.pagenum}/${this.queryInfo.pagesize}?ts=${new Date().getTime()}`,{
            params:{'deviceName':this.queryInfo.query}
          });
        if (res.code !== 0) return this.$message.error('获取终端用户信息失败！');
        //给每行数据强制追加一个数据项
        res.data.list.map(item => {
          item.ruleItemData = [];
        });
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
      async expandChange(row, expandedRows) {
        this.expands = [];
        if (expandedRows.length > 0) {//展开
          row ? this.expands.push(row.deviceId) : '';
          this.getAuthority(row.deviceId);
        }
      },
      async getAuthority(deviceId) {//获取指定用户已添加的权限
        const {data: res} = await
          this.$http.get(`device/getDeviceAuthority?deviceId=${deviceId}&ts=${new Date().getTime()}`);
        if (res.code !== 0) return this.$message.error((res.msg !== '' && res.msg !== null) ? res.msg : '获取权限失败！');
        this.userList.forEach((temp, index) => {
          if (temp.deviceId === deviceId) {
            this.userList[index].ruleItemData = res.data;
          }
        });
      },
      async openAdd(row) {
        this.addDeviceId = row.deviceId;
        const {data: res} = await this.$http.get(`device/getDeviceAuthority?deviceId=${row.deviceId}&ts=${new Date().getTime()}`);//获取指定用户已添加的权限
        if (res.code !== 0) return this.$message.error((res.msg !== '' && res.msg !== null) ? res.msg : '获取权限失败！');
        const huiXianCheck = res.data;
        if (huiXianCheck.length > 0) {
          for (let i = 0; i < huiXianCheck.length; i++) {
            this.checkaddCode.push(huiXianCheck[i].apiId);//拿到选中的权限，进行回显
          }
          this.addForm.checkedAuth = [].concat(this.checkaddCode);
        }
        this.addDialogVisible = true;
        this.getCategory()
      },
      addDialogClosed() {
        this.$refs.addFormRef.clearValidate();
        this.addForm.checkedAuth = [];
        this.checkaddCode = [];
      },
      submitAdd() {
        if(this.addForm.checkedAuth.length < 1) return this.$message.error('请选择权限！');
        this.$refs.addFormRef.validate(async valid => {
          if (!valid) return;
          let reqData = {
            "apiId": this.addForm.checkedAuth,
            "deviceId": this.addDeviceId
          };
          const {data: res} = await this.$http.post('device/addDeviceAuthority', reqData);
          if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '添加权限失败！');
          this.$message.success('添加权限成功！');
          this.addDialogVisible = false;
          this.getAuthority(this.addDeviceId);
        })
      },
      async removeRightById(row, authId) {//根据authId删除对应的权限
        const confirmResult = await
          this.$confirm(
            '此操作将删除该权限, 是否继续?',
            '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }
          ).catch(err => err);
        if (confirmResult !== 'confirm') return;
        const {data: res} = await
          this.$http.get(`device/delDeviceAuthority?authId=${authId}&ts=${new Date().getTime()}`);
        if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '删除权限失败！');
        this.$message.success('删除权限成功！');
        this.getAuthority(row.deviceId);
      },
      async removeAllById(deviceId) {//根据groupAuthId删除对应的权限
        const confirmResult = await
          this.$confirm(
            '此操作将删除该终端用户下所有权限, 是否继续?',
            '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }
          ).catch(err => err);
        if (confirmResult !== 'confirm') return;
        const {data: res} = await
          this.$http.get(`device/delDeviceAllAuthority?deviceId=${deviceId}&ts=${new Date().getTime()}`);
        if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '删除权限失败！');
        this.$message.success('删除所有权限成功！');
        this.getAuthority(deviceId);
      },
      async getCategory() {//一次性获取接口资源信息
        const {data: res} = await this.$http.post('resource/getResource');
        if (res.code !== 0) return this.$message.error('获取权限信息失败！');
        this.options = res.data;
      },
    }
  }
</script>
<style lang="less">
  .deviceAuth{
    min-width: 1000px;
    .el-form {
      width: 80%;
      margin: 20px auto;
    }
    .el-cascader__tags .el-tag>span {
      flex: auto;
    }
    .el-cascader-node__postfix {
      top: 50%;
      transform: translateY(-50%);
    }
  }



</style>
