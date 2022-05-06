<template>
  <div class="appUser">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>应用管理</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row>
        <el-button type="primary" @click="addDialogVisible = true">添加</el-button>
      </el-row>
      <el-table :data="userlist" stripe :row-key="getRowKeys"
                :expand-row-keys="expands" @expand-change="expandChange">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="appKey:">
                <span>{{ props.row.appKey === '' || props.row.appKey == null ? '-' : props.row.appKey }}</span>
              </el-form-item>
            </el-form>
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="appSecret:">
                <span>{{ props.row.appSecret === '' || props.row.appSecret == null ? '-' : props.row.appSecret }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="应用名称" prop="appName" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="应用类型" prop="appType">
          <template slot-scope="scope">
            <span v-if="scope.row.appType === 1">专用应用</span>
            <span v-else-if="scope.row.appType === 2">通用应用</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="comments" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span>{{ scope.row.comments === '' ||  scope.row.comments == null ? '-' : scope.row.comments }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="操作" width="400px">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="deleteByName(scope.row.appId)">删除</el-button>
            <el-button type="success" size="mini" v-if="scope.row.appType === 1" @click="goDeviceDetail(scope.row.appId,scope.row.appType)">终端列表
            </el-button>
            <el-button type="success" size="mini" v-if="scope.row.appType === 2" @click="goDeviceDetail2(scope.row.appId,scope.row.appType)">终端列表
            </el-button>
            <el-button type="warning" size="mini" @click="goAppList(scope.row.appId)">绑定应用管理员</el-button>
            <el-button type="primary" size="mini" v-if="scope.row.appType === 1"
                       @click="goDeviceConfig(scope.row.appId)">配置
            </el-button>
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
    <el-dialog title="添加应用" :visible.sync="addDialogVisible" width="650px" @close="addDialogClosed" :close-on-click-modal = "false">
      <el-form style="width: 80%;margin: 20px auto;" :model="addForm" :rules="addFormRules" ref="addFormRef"
               label-width="80px">
        <el-form-item label="应用名称" prop="loginName">
          <el-input v-model.trim="addForm.loginName"  maxlength="16"></el-input>
        </el-form-item>
        <el-form-item label="应用类型" prop="appType">
          <el-radio-group v-model.trim="addForm.appType" @change="radioChange">
            <el-radio :label="1">专用应用</el-radio>
            <el-radio :label="2">通用应用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="comments">
          <el-input type="textarea" maxlength="200" show-word-limit :rows="10"
                    v-model.trim="addForm.comments"></el-input>
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
import Cookies from 'js-cookie'
  export default {
    data() {
      return {
        isShow: '',
        userlist: [],
        queryInfo: {
          pagenum: 1,
          pagesize: 10
        },
        total: 0,
        expands: [],
        getRowKeys(row) {
          return row.appId
        },
        addDialogVisible: false,
        addForm: {
          loginName: '',
          appType: null,
          comments: ''
        },
        addFormRules: {
          loginName: [{required: true, message: '请输入应用名', trigger: 'blur'},
            {min: 2, max: 16, message: '长度为2-16位', trigger: 'blur'}],
          appType: [{required: true, message: '请选择应用类型', trigger: 'change'}],
          comments: [{min: 0, max: 200, message: '长度在200个字符以内', trigger: 'blur'}]
        },
      }
    },
    beforeRouteEnter: (to, form, next) => {
      const loginType =Cookies.get('accountTypeLogin');
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
        const {data: res} = await this.$http.get(`app/getApps/${this.queryInfo.pagenum}/${this.queryInfo.pagesize}?ts=${new Date().getTime()}`);
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
      expandChange(row, expandedRows) {// 折叠面板每次只能展开一行
        if (expandedRows.length) {
          this.expands = [];
          if (row) {
            this.expands.push(row.appId)
          }
        } else {
          this.expands = []
        }
      },
      addDialogClosed() {
        this.$refs.addFormRef.resetFields();
      },
      submitAdd() {
        this.$refs.addFormRef.validate(async valid => {
          if (!valid) return;
          const {data: res} = await this.$http.post('app/addApp', {
            'appName': this.addForm.loginName,
            'appType': this.addForm.appType,
            'comments': this.addForm.comments,
          });
          if (res.code !== 0) return this.$message.error('添加失败!');
          this.$message.success('添加成功!');
          this.addDialogVisible = false;
          this.getUserList()
        })
      },
      async deleteByName(name) {
        const confirmResult = await this.$confirm(
          '此操作将删除该应用，是否继续？', '提示', {
            confirmButtonText: '确定',
            cancelButtontext: '取消',
            type: 'warning',
          }
        ).catch(err => err);
        if (confirmResult !== 'confirm') {
          return
        }
        const {data: res} = await this.$http.get('app/deleteApp', {
          params: {
            'appId': name
          }
        });
        if (res.code !== 0) return this.$message.error('删除失败！');
        this.$message.success('删除成功！');
        this.getUserList()
      },
      radioChange(value) {
        this.addForm.comments = value === 1 ? '密钥云终端形式' : '标准协议形式';
      },
      goDeviceDetail(id, type) {
        this.$router.push({path: '/appSpecial', query: {'id': id, 'type': type}})
      },
      goDeviceDetail2(id, type) {
        this.$router.push({path: '/appCommon', query: {'id': id, 'type': type}})
      },
      goDeviceConfig(id) {
        this.$router.push({path: '/qems', query: {'id': id}})
      },
      goAppList(id) {
        this.$router.push({path: '/appList', query: {'id': id}})
      }
    }
  }
</script>
<style lang="less" scoped>
  .appUser {
    min-width: 1000px;
    .demo-table-expand .el-form-item {
      margin-right: 0;
      margin-bottom: 0;
      width: 50%;
    }
  }
</style>
