<template>
  <div style="min-width: 1000px;">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item>终端用户</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row>
        <el-col :span="16" style="float: left">
          <el-button type="primary" @click="addDialogVisible = true">添加</el-button>
          <el-button type="primary" @click="exportModel">下载模板</el-button>
          <label for="fileId">
            <span class="butSpan">导入</span>
          </label>
          <input type="file" id="fileId" hidden ref="fileId" @change="onFileChange">
          <el-button v-show="multipleSelection.length > 0" type="danger" @click="batchDeleteBuild" plain>批量删除</el-button>
          <el-button v-show="multipleSelection.length > 0" type="primary" @click="batchExportBuild" plain>批量导出</el-button>
        </el-col>
        <el-col :span="8" style="float: right">
          <el-input placeholder="请输入用户名" v-model.trim="queryInfo.query" clearable @clear="getUserList" @keyup.enter.native="getUserList">
            <el-button slot="append" icon="el-icon-search" @click="getUserList"></el-button>
          </el-input>
        </el-col>
      </el-row>
      <el-table :data="userlist" ref="multipleTable" :row-key="getRowKeys" stripe
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" :reserve-selection="true" width="55"></el-table-column>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="用户名" :show-overflow-tooltip="true" prop="deviceName"></el-table-column>
        <el-table-column label="用户类型" :show-overflow-tooltip="true" prop="userType">
          <template slot-scope="scope">
            <span>{{scope.row.userType === 0 ? '软件用户' : '硬件用户'}}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" :show-overflow-tooltip="true" prop="comments">
          <template slot-scope="scope">
            <span>{{scope.row.comments === null || scope.row.comments === '' ? '-' : scope.row.comments}}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" :show-overflow-tooltip="true" prop="createTime">
        </el-table-column>
        <el-table-column label="操作" width="300px">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="editById(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" @click="psdByName(scope.row.deviceId)">修改密码
            </el-button>
            <el-button size="mini" type="success" @click="lookById(scope.row.deviceName)">量子密钥管控</el-button>
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
    <el-dialog title="添加终端用户" :visible.sync="addDialogVisible" width="650px" @close="addDialogClosed" :close-on-click-modal = "false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="80px">
        <el-form-item label="用户名" prop="deviceName">
          <el-input v-model.trim="addForm.deviceName"  maxlength="32" autoComplete="new-text"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pws1">
          <el-input type="password" v-model.trim="addForm.pws1"  maxlength="16" autoComplete="new-password"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="pws2">
          <el-input type="password" v-model.trim="addForm.pws2"  maxlength="16" autoComplete="new-password"></el-input>
        </el-form-item>
        <el-form-item label="用户类型" prop="userType">
          <el-radio-group v-model.trim="addForm.userType">
            <el-radio :label="0">软件用户</el-radio>
            <el-radio :label="1">硬件用户</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="comments">
          <el-input type="textarea" maxlength="200" show-word-limit :rows="10" v-model.trim="addForm.comments"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" v-preventReClick="1000" @click="submitAdd">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="编辑终端用户" :visible.sync="editDialogVisible" width="650px" @close="editDialogClosed" :close-on-click-modal = "false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="80px">
        <el-form-item label="用户名" prop="deviceName">
          <el-input v-model.trim="editForm.deviceName" disabled></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="comments">
          <el-input type="textarea" maxlength="200" show-word-limit :rows="10" v-model.trim="editForm.comments"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" v-preventReClick="1000" @click="submitEdit">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改密码" :visible.sync="psdDialogVisible" width="650px" @close="psdDialogClosed" :close-on-click-modal = "false">
      <el-form :model="psdForm" :rules="psdFormRules" ref="psdFormRef" label-width="80px">
        <el-form-item label="新密码" prop="psd1">
          <el-input type="password" v-model.trim="psdForm.psd1"  maxlength="16" autoComplete="new-password"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="psd2">
          <el-input type="password" v-model.trim="psdForm.psd2"  maxlength="16" autoComplete="new-password"></el-input>
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
import Cookies from 'js-cookie'
  export default {
    data() {
     const valiPass = (rule, value, cb) => {//密码
  const reg = /^(?![0-9_]+$)(?![a-zA-Z]+$)[0-9A-Za-z_]{8,16}$/;
  if (reg.test(value)) {
    return cb()
  }
  cb(new Error('请输入数字和字母组成的字符，长度为8-16位'))
};
      const valiPws2 = (rule, value, cb) => {
        if (value === '') {
          cb(new Error('请再次输入密码'));
        } else if (value !== this.addForm.pws1) {
          cb(new Error('两次输入密码不一致'));
        } else {
          cb();
        }
      };
      const valiPws3 = (rule, value, cb) => {
        if (value === '') {
          cb(new Error('请再次输入密码'));
        } else if (value !== this.psdForm.psd1) {
          cb(new Error('两次输入密码不一致'));
        } else {
          cb();
        }
      };
      return {
        userlist: [],
        queryInfo: {
          query: '',
          pagenum: 1,
          pagesize: 10
        },
        total: 0,
        addDialogVisible: false,
        addForm: {
          deviceName: '',
          pws1: '',
          pws2: '',
          comments: '',
          userType: 0
        },
        addFormRules: {
          deviceName: [{required: true, validator: this.Global.valiUser, trigger: 'blur'}],
          pws1: [{required: true, validator: valiPass, trigger: 'blur'}],
          pws2: [{required: true, validator: valiPws2, trigger: 'blur'}],
          comments: [{min: 0, max: 200, message: '长度在200个字符以内', trigger: 'blur'}],
          userType: [{required: true, message: '请选择用户类型', trigger: 'change'}]
        },
        editId: '',
        editDialogVisible: false,
        editForm: {
          deviceName: '',
          comments: '',
        },
        editFormRules: {
          deviceName: [{required: true, validator: this.Global.valiUser, trigger: 'blur'}],
          comments: [{min: 0, max: 200, message: '长度在200个字符以内', trigger: 'blur'}]
        },
        multipleSelection: [],//删除
        deleteCode: [],

        psdDialogVisible: false,
        psdForm: {
          psd1: '',
          psd2: '',
          deviceId: '',
        },
        psdFormRules: {
          password: [
            {required: true, message: "请输入旧密码", trigger: 'blur'}
          ],
          psd1: [{required: true, validator: valiPass, trigger: 'blur'}],
          psd2: [{required: true, validator: valiPws3, trigger: 'blur'}],
        }
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
      this.getUserList();
    },
    methods: {
      async getUserList() {
        const {data: res} = await this.$http.get(`device/getAllDevice/${this.queryInfo.pagenum}/${this.queryInfo.pagesize}?ts=${new Date().getTime()}`, {
          params: {'deviceName': this.queryInfo.query}
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
      lookById(name){
        this.$router.push({path: '/keyInfo', query: {'deviceName': name}});
      },
      async onFileChange() {
        const file = this.$refs.fileId;
        if (file.files[0].type !== "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" && file.files[0].type !== "application/vnd.ms-excel") return this.$message.error('只能导入扩展名为xlsx/xls的文件，请重新选择！');
        const fd = new FormData();
        fd.append('excelFile', file.files[0]);
        const {data: res} = await this.$http.post('device/importDeviceUser', fd,
          {headers: {'Content-Type': 'multipart/form-data'},}
        );
        this.$refs.fileId.value = '';
        if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '导入失败!');
        this.$message.success('导入成功!');
        this.getUserList()
      },
      addDialogClosed() {
        this.$refs.addFormRef.resetFields();
      },
      submitAdd() {
        this.$refs.addFormRef.validate(async valid => {
          if (!valid) return;
          let reqData = {
            "comments": this.addForm.comments,
            "password": this.addForm.pws2,
            "deviceName": this.addForm.deviceName,
            "userType": this.addForm.userType
          };
          const {data: res} = await this.$http.post('device/addDeviceUser', reqData);
          if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '添加失败！');
          this.$message.success('添加成功！');
          this.addDialogVisible = false;
          this.getUserList()
        })
      },
      editById(row) {
        this.editDialogVisible = true;
        this.editForm.comments = row.comments;
        this.editForm.deviceName = row.deviceName;
        this.editId = row.deviceId;
      },
      editDialogClosed() {
        this.$refs.editFormRef.resetFields();
      },
      submitEdit() {
        this.$refs.editFormRef.validate(async valid => {
          if (!valid) return;
          let reqData = {
            "comments": this.editForm.comments,
            "deviceName": this.editForm.deviceName,
            "deviceId": this.editId
          };
          const {data: res} = await this.$http.post('device/updateDevice', reqData);
          if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '编辑失败！');
          this.$message.success('编辑成功！');
          this.editDialogVisible = false;
          this.getUserList()
        })
      },
      getRowKeys(row) {
        return row.deviceId
      },
      handleSelectionChange(val) {//批量删除选中数据方法
        this.multipleSelection = val;
      },
      batchExportBuild() {
        for (let i = 0; i < this.multipleSelection.length; i++) {
          this.deleteCode.push(this.multipleSelection[i].deviceId);
        }
        let saveUrl = 'device/exportDeviceUsers';
        let reqData = {'deviceIds': this.deleteCode};
        this.$http.post(`${saveUrl}`, reqData, {responseType: 'blob'}).then(res => {
          const filename = decodeURI(res.headers['content-disposition'].split(';')[1].split('=')[1]);//拿到文件名
          this.Global.downloadFile(res.data, filename);
          this.deleteCode = [];
          this.$refs.multipleTable.clearSelection();
          this.getUserList()
        })
      },
      async batchDeleteBuild() {
        const confirmDelete = await this.$confirm(
          "此操作将删除选中用户，是否继续？",
          "提示",
          {
            confimrButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
        ).catch(err => err);
        if (confirmDelete !== "confirm") return;
        for (let i = 0; i < this.multipleSelection.length; i++) {
          this.deleteCode.push(this.multipleSelection[i].deviceId);
        }
        const {data: res} = await this.$http.post('device/deleteDevice', {
          'deviceId': this.deleteCode
        });
        if (res.code !== 0) return this.$message.error(res.msg !== '' && res.msg !== null ? res.msg : '删除失败！');
        this.$message.success('删除成功！');
        this.deleteCode = [];
        this.$refs.multipleTable.clearSelection();
        this.getUserList()
      },
      psdByName(deviceId) {//修改密码
        this.psdForm.deviceId = deviceId;
        this.psdDialogVisible = true;
      },
      psdEnsure() {
        this.$refs.psdFormRef.validate(async valid => {
          if (!valid) return;
          let reqData = {
            "password": this.psdForm.psd2,
            "deviceId": this.psdForm.deviceId
          };
          const {data: res} = await this.$http.post('device/updateDevice', reqData);
          if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '修改密码失败！');
          this.$message.success('修改密码成功！');
          this.psdDialogVisible = false;
        })
      },
      psdDialogClosed() {
        this.$refs.psdFormRef.resetFields();
      },
      exportModel() {
        const saveUrl='device/getDeviceUserModel';
        this.$http.post(`${saveUrl}`, '', {responseType: 'blob'}).then(res => {
          const filename = decodeURI(res.headers['content-disposition'].split(';')[1].split('=')[1]);
          this.Global.downloadFile(res.data, filename);
        })
      },
    }
  }
</script>
<style lang="less" scoped>
  .el-select {
    width: 100%;
  }

  .el-form {
    width: 80%;
    margin: 20px auto;
  }

  .butSpan {
    display: inline-block;
    margin: 0 10px;
    color: #FFF;
    background-color: #409EFF;
    border: 1px solid #409EFF;
    font-size: 14px;
    cursor: pointer;
    border-radius: 4px;
    white-space: nowrap;
    box-sizing: border-box;
    text-align: center;
    height: 40px;
    line-height: 40px;
    width: 70px;
    position: relative;
    top: .5px;
  }

</style>
