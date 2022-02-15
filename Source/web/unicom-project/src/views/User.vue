<template>
  <div class="user">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item>系统用户</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row>
        <el-button type="primary" @click="addDialogVisible = true">添加</el-button>
      </el-row>
      <el-table :data="userlist" stripe>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="用户名" :show-overflow-tooltip="true" prop="userName"></el-table-column>
        <el-table-column label="邮箱" :show-overflow-tooltip="true" prop="email">
          <template slot-scope="scope">
            <span>{{scope.row.email == null || scope.row.email === '' ? '-' : scope.row.email}}</span>
          </template>
        </el-table-column>
        <el-table-column label="账户类型" prop="accountType">
          <template slot-scope="scope">
            <span v-if="scope.row.accountType === 1">应用管理员</span>
            <span v-else-if="scope.row.accountType === 2">安全员</span>
            <span v-else-if="scope.row.accountType === 9">管理员</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" :show-overflow-tooltip="true" prop="comments">
          <template slot-scope="scope">
            <span>{{scope.row.comments == null || scope.row.comments === '' ? '-' : scope.row.comments}}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="deleteById(scope.row.id)"
                       :disabled="scope.row.userName === 'admin'">删除
            </el-button>
            <el-button size="mini" type="primary"
                       @click="psdById(scope.row.id,scope.row.userName,scope.row.email,scope.row.comments)">
              编辑
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
    <el-dialog title="添加" :visible.sync="addDialogVisible" width="700px" @close="addDialogClosed">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="90px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model.trim="addForm.userName"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model.trim="addForm.email"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="psd1">
          <el-input type="password" v-model.trim="addForm.psd1"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="psd2">
          <el-input type="password" v-model.trim="addForm.psd2"></el-input>
        </el-form-item>
        <el-form-item label="用户类型" prop="accountType">
          <el-radio-group v-model.trim="addForm.accountType">
            <el-radio label="1">应用管理员</el-radio>
            <el-radio label="2">安全员</el-radio>
            <el-radio label="9">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="comments">
          <el-input type="textarea" maxlength="200" show-word-limit :rows="5"
                    v-model.trim="addForm.comments"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" v-preventReClick="1000" @click="submitAdd">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="编辑用户信息" :visible.sync="psdDialogVisible" width="650px" @close="psdDialogClosed">
      <el-form :model="psdForm" :rules="psdFormRules" ref="psdFormRef" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model.trim="psdForm.userName" disabled></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model.trim="psdForm.email"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="psd1">
          <el-input type="password" v-model.trim="psdForm.psd1"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="psd2">
          <el-input type="password" v-model.trim="psdForm.psd2"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="comments">
          <el-input type="textarea" maxlength="200" show-word-limit :rows="5"
                    v-model.trim="psdForm.comments"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="psdDialogVisible = false">取 消</el-button>
        <el-button type="primary" v-preventReClick="1000" @click="psdEnsure">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data() {
      const keyBlur = (rule, val, cb) => {
        const reg = /^[a-zA-Z0-9_]{4,16}$/;
        if (reg.test(val)) {
          this.getExternal(val, cb)
        } else {
          cb(new Error('请输入数字、字母、下划线组成的字符，长度为4-16位'))
        }
      };
      const valiUpdatePass = (rule, value, cb) => {//密码
        if (value !== '') {
          const reg = /^(?![0-9_]+$)(?![a-zA-Z]+$)[0-9A-Za-z_!@#$%&*]{8,16}$/;
          if (reg.test(value)) {
            return cb()
          }
          cb(new Error('请输入数字和字母或特殊字符（_!@#$%&*）组成的字符，长度为8-16位'))
        } else {
          cb();
        }
      };
      const valiPass2 = (rule, value, cb) => {
        if (value !== this.psdForm.psd1) {
          cb(new Error('两次输入密码不一致'));
        } else {
          cb();
        }
      };
      const valiPass3 = (rule, value, cb) => {
        if (value === '') {
          cb(new Error('请再次输入密码'));
        } else if (value !== this.addForm.psd1) {
          cb(new Error('两次输入密码不一致'));
        } else {
          cb();
        }
      };
      return {
        accountTypeLogin: '',
        userlist: [],
        queryInfo: {
          pagenum: 1,
          pagesize: 10
        },
        total: 0,
        addDialogVisible: false,
        addForm: {
          userName: '',
          email: '',
          psd1: '',
          psd2: '',
          accountType: '1',
          comments: ''
        },
        addFormRules: {
          userName: [{required: true, validator: keyBlur, trigger: 'blur'}],
          email: [{required: true, validator: this.Global.valiEmail, trigger: 'blur'}],
          psd1: [{required: true, validator: this.Global.valiPass, trigger: 'blur'}],
          psd2: [{required: true, validator: valiPass3, trigger: 'blur'}],
          accountType: [{required: true, message: '请选择用户类型', trigger: 'change'}],
          comments: [{min: 0, max: 200, message: '长度在200个字符以内', trigger: 'blur'}]
        },
        psdDialogVisible: false,
        psdForm: {
          id: '',
          userName: '',
          email: '',
          psd1: '',
          psd2: '',
          comments: ''
        },
        psdFormRules: {
          email: [{required: true, validator: this.Global.valiEmail, trigger: 'blur'}],
          psd1: [{required: false, validator: valiUpdatePass, trigger: 'blur'}],
          psd2: [{required: false, validator: valiPass2, trigger: 'blur'}],
          comments: [{min: 0, max: 200, message: '长度在200个字符以内', trigger: 'blur'}]
        }
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
      this.accountTypeLogin = window.sessionStorage.getItem('accountTypeLogin');
    },
    methods: {
      async getUserList() {
        const {data: res} = await this.$http.get(`user/getAllUsers/${this.queryInfo.pagenum}/${this.queryInfo.pagesize}?ts=${new Date().getTime()}`);
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
      addDialogClosed() {
        this.$refs.addFormRef.resetFields();
      },
      submitAdd() {
        this.$refs.addFormRef.validate(async valid => {
          if (!valid) return;
          let data = {
            userName: this.addForm.userName,
            email: this.addForm.email,
            password: this.addForm.psd2,
            accountType: this.addForm.accountType,
            comments: this.addForm.comments,
          };
          const {data: res} = await this.$http.post('user/addUserInfo', data);
          if (res.code !== 0) return this.$message.error(res.msg === '' || res.msg == null ? '添加失败!' : res.msg);
          this.$message.success('添加成功!');
          this.addDialogVisible = false;
          this.getUserList()
        })
      },
      async deleteById(id) {
        const confirmResult = await this.$confirm(
          '此操作将删除该用户，是否继续？', '提示', {
            confirmButtonText: '确定',
            cancelButtontext: '取消',
            type: 'warning',
          }
        ).catch(err => err);
        if (confirmResult !== 'confirm') {
          return
        }
        const {data: res} = await this.$http.get('user/deleteUserInfo', {params: {'id': id}});
        if (res.code !== 0) return this.$message.error(res.msg === '' || res.msg == null ? '删除失败！' : res.msg);
        this.$message.success('删除成功！');
        this.getUserList()
      },
      psdById(id, name, email, comments) {
        this.psdForm.id = id;
        this.psdForm.userName = name;
        this.psdForm.email = email;
        this.psdForm.comments = comments;
        this.psdDialogVisible = true;
      },
      psdEnsure() {
        this.$refs.psdFormRef.validate(async valid => {
          if (!valid) return;
          const {data: res} = await this.$http.post(
            'user/updateUserInfo', {
              'id': this.psdForm.id,
              'password': this.psdForm.psd2,
              'email': this.psdForm.email,
              'comments': this.psdForm.comments
            });
          if (res.code !== 0) return this.$message.error(res.msg === '' || res.msg == null ? '编辑失败！' : res.msg);
          this.$message.success("编辑成功！");
          this.psdDialogVisible = false;
          this.getUserList()
        })
      },
      psdDialogClosed() {
        this.$refs.psdFormRef.resetFields();
      },
      async getExternal(val, cb) {
        const {data: res} = await this.$http.get('user/userNameCheck', {
          params: {
            'userName': this.addForm.userName
          }
        });
        if (res.code !== 0) return cb(res.msg);
        return cb()
      }
    }
  }
</script>
<style lang="less" scoped>
  .user {
    min-width: 1000px;
    .el-form {
      width: 80%;
      margin: 20px auto;
    }
  }
</style>
