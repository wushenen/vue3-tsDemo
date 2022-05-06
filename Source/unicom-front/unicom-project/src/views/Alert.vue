<template>
  <div style="min-width: 1000px;">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>日志告警</el-breadcrumb-item>
      <el-breadcrumb-item>告警信息</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row>
        <el-button type="primary" @click="setLevel">修改发件人信息</el-button>
      </el-row>
      <el-table :data="userlist" stripe>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="邮箱" prop="destination">
          <template slot-scope="scope">
            <span>{{scope.row.destination == null|| scope.row.destination === '' ? '-' : scope.row.destination}}</span>
          </template>
        </el-table-column>
        <el-table-column label="发送状态" prop="mailStatus">
          <template slot-scope="scope">
            <span v-if="scope.row.mailStatus === true">成功</span>
            <span v-else>失败</span>
          </template>
        </el-table-column>
        <el-table-column label="详情" :show-overflow-tooltip="true" prop="detail"></el-table-column>
        <el-table-column label="发送时间" :show-overflow-tooltip="true" prop="updateTime"></el-table-column>
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
    <el-dialog title="修改发件人信息" :visible.sync="addDialogVisible" width="600px" @close="addDialogClosed" :close-on-click-modal = "false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px">
        <el-form-item label="发件人邮箱" prop="emailUsername">
          <el-input v-model.trim="addForm.emailUsername"></el-input>
        </el-form-item>
        <el-form-item label="发件人密码/授权码" prop="emailPassword">
          <el-input type="password" v-model.trim="addForm.emailPassword"></el-input>
        </el-form-item>
        <el-form-item label="服务器地址" prop="emailHost">
          <el-input v-model.trim="addForm.emailHost"></el-input>
        </el-form-item>
        <el-form-item label="邮箱协议" prop="emailProtocol">
          <el-input v-model.trim="addForm.emailProtocol"></el-input>
        </el-form-item>
        <el-form-item label="协议端口号" prop="emailPort">
          <el-input v-model.trim="addForm.emailPort"></el-input>
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
        userlist: [],
        queryInfo: {
          pagenum: 1,
          pagesize: 10
        },
        total: 0,
        senderInfo: {},
        addDialogVisible: false,
        addForm: {
          emailHost: '',
          emailPassword: '',
          emailPort: '',
          emailUsername: '',
          emailemailProtocol: '',
        },
        addFormRules: {
          emailHost: [{required: true,message: '请输入服务器地址', trigger: 'blur'}],
          emailPassword: [{required: true, message: '请输入发件人密码/授权码', trigger: 'blur'}],
          emailPort: [{required: true, validator: this.Global.valiPort, trigger: 'blur'}],
          emailUsername: [{required: true, validator: this.Global.valiEmail, trigger: 'blur'}],
          emailProtocol: [{required: true, message: '请输入邮箱协议', trigger: 'blur'}],
        },
      }
    },
    created() {
      this.getUserList();
    },
    methods: {
      async getUserList() {
        const {data: res} = await this.$http.get(`mail/getMailLogs/${this.queryInfo.pagenum}/${this.queryInfo.pagesize}?ts=${new Date().getTime()}`);
        if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '获取日志失败！');
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
      async getLevel() {//获取发件人
        const {data: res} = await this.$http.get(`mail/getMailConfig?ts=${new Date().getTime()}`);
        if (res.code !== 0) return this.$message.error(res.msg !== '' && res.msg !== null ? res.msg : '获取失败!');
        this.addForm = res.data;
      },
      setLevel() {
        this.addDialogVisible = true;
        this.getLevel()
      },
      submitAdd() {
        this.$refs.addFormRef.validate(async valid => {
          if (!valid) return;
          let data = {
            'emailHost': this.addForm.emailHost,
            'emailPassword': this.addForm.emailPassword,
            'emailPort': this.addForm.emailPort,
            'emailUsername': this.addForm.emailUsername,
            'emailProtocol': this.addForm.emailProtocol,
          };
          const {data: res} = await this.$http.post('mail/updateMailConfig', data);
          if (res.code !== 0) return this.$message.error(res.msg === '' || res.msg == null ? '修改失败!' : res.msg);
          this.$message.success('修改成功!');
          this.addDialogVisible = false;
          this.getUserList()
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
