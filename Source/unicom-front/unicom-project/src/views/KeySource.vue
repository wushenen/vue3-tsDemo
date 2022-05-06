<template>
  <div class="sourceWrap">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>密钥源配置</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-table :data="userList" stripe height="300">
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="密钥源" :show-overflow-tooltip="true" prop="keySource">
          <template slot-scope="scope">
            <span v-if="scope.row.keySource === 1">QRNG</span>
            <span v-else-if="scope.row.keySource === 2">高速QRNG</span>
            <span v-else-if="scope.row.keySource === 3">QKD</span>
          </template>
        </el-table-column>
        <el-table-column label="优先级" prop="priority"></el-table-column>
        <el-table-column label="发送端IP" prop="sourceIp" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span>{{scope.row.sourceIp === null || scope.row.sourceIp === '' ? '-' : scope.row.sourceIp}}</span>
          </template>
        </el-table-column>
        <el-table-column label="发送端备用IP" prop="sourceIp2" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span>{{scope.row.sourceIp2 === null || scope.row.sourceIp2 === '' ? '-' : scope.row.sourceIp2}}</span>
          </template>
        </el-table-column>
        <el-table-column label="随机数数量" prop="keyGenerateNum">
          <template slot-scope="scope">
            <span>{{scope.row.keyGenerateNum === null || scope.row.keyGenerateNum === '' ? '-' : scope.row.keyGenerateNum}}</span>
          </template>
        </el-table-column>
        <el-table-column label="获取速率" prop="keyGenerateRate">
          <template slot-scope="scope">
            <span>{{scope.row.keyGenerateRate === null || scope.row.keyGenerateRate === '' ? '-' : scope.row.keyGenerateRate}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="350px">
          <template slot-scope="scope">
            <el-button type="warning" size="mini" v-if="scope.row.priority === 4"
                       @click="addByName(scope.row.priority,scope.row.id)">启用
            </el-button>
            <el-button type="danger" size="mini" v-else
                       @click="deleteByName(scope.row.priority,scope.row.id)">禁用
            </el-button>
            <el-button type="success" size="mini"  :disabled="scope.row.priority === 4"
                       @click="editMethod(scope.row.priority,scope.row.id)">修改优先级
            </el-button>
            <el-button type="primary" size="mini" v-if="scope.row.keySource === 2 || scope.row.keySource === 3"
                       @click="editIpMethod(scope.row)">修改IP
            </el-button>
            <el-button type="info" size="mini" v-if="scope.row.keySource === 3"
                       @click="editQkdMethod(scope.row)">修改参数
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog title="启用该密钥源配置" :visible.sync="addDialogVisible" width="500px" @close="addDialogClosed">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="80px">
        <el-form-item label="优先级" prop="priority">
          <el-select style="width: 100%" v-model="addForm.priority" placeholder="请选择">
            <el-option :label="1" :value="1"></el-option>
            <el-option :label="2" :value="2"></el-option>
            <el-option :label="3" :value="3"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitAdd">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改IP" :visible.sync="editDialogVisible" width="500px" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
        <el-form-item label="发送端IP" prop="sourceIp">
          <el-input v-model.trim="editForm.sourceIp"  maxlength="16"></el-input>
        </el-form-item>
        <el-form-item label="发送端备用IP" prop="sourceIp2">
          <el-input v-model.trim="editForm.sourceIp2"  maxlength="16"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitEdit">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改优先级" :visible.sync="keyDialogVisible" width="500px" @close="keyDialogClosed">
      <el-form :model="keyForm" :rules="keyFormRules" ref="keyFormRef" label-width="80px">
        <el-form-item label="优先级" prop="priority">
          <el-select style="width: 100%" v-model="keyForm.priority" placeholder="请选择">
            <el-option :label="1" :value="1"></el-option>
            <el-option :label="2" :value="2"></el-option>
            <el-option :label="3" :value="3"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="keyDialogVisible = false">取 消</el-button>
        <el-button type="primary" v-preventReClick="1000" @click="keyEnsure">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改参数" :visible.sync="configDialogVisible" width="500px" @close="configDialogClosed" >
      <el-form :model="configForm" :rules="configFormRules" ref="configFormRef" label-width="130px">
        <el-form-item label="发送端用户名" prop="localName">
          <el-input v-model.trim="configForm.localName"  maxlength="32"></el-input>
        </el-form-item>
        <el-form-item label="接收端用户名" prop="peerName">
          <el-input v-model.trim="configForm.peerName"  maxlength="32"></el-input>
        </el-form-item>
        <el-form-item label="发送端设备密钥" prop="devKey">
          <el-input type="textarea" v-model.trim="configForm.devKey"  maxlength="32"></el-input>
        </el-form-item>
        <el-form-item label="发送端加密密钥" prop="cryptKey">
          <el-input type="textarea" v-model.trim="configForm.cryptKey"  maxlength="32"></el-input>
        </el-form-item>
        <el-collapse accordion @change="handleChange">
          <el-collapse-item :name="1" title="备用参数">
            <el-form-item label="发送端用户名" prop="localName2">
              <el-input v-model.trim="configForm.localName2"  maxlength="32"></el-input>
            </el-form-item>
            <el-form-item label="接收端用户名" prop="peerName2">
              <el-input v-model.trim="configForm.peerName2"  maxlength="32"></el-input>
            </el-form-item>
            <el-form-item label="发送端设备密钥" prop="devKey2">
              <el-input type="textarea" v-model.trim="configForm.devKey2"  maxlength="32"></el-input>
            </el-form-item>
            <el-form-item label="发送端加密密钥" prop="cryptKey2">
              <el-input type="textarea" v-model.trim="configForm.cryptKey2"  maxlength="32"></el-input>
            </el-form-item>
          </el-collapse-item>
        </el-collapse>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="configDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitConfig">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import Cookies from 'js-cookie'
  export default {
    data() {
      const checkIp = (rule, value, cb) => {
        const reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
        if (value !== '') {
          if (reg.test(value)) {
            return cb()
          }
          cb(new Error('请输入正确的IP'))
        } else {
          return cb()
        }
      };
      const checkSource = (rule, value, cb) => {
        const reg = /^[0-9a-fA-F]+$/;
        if (reg.test(value)) {
          return cb()
        }
        cb(new Error('请输入16进制的字符串'))
      };
      const checkSource2 = (rule, value, cb) => {
        const reg = /^[0-9a-fA-F]+$/;
        if (value !== '') {
          if (reg.test(value)) {
            return cb()
          }
          cb(new Error('请输入16进制的字符串'))
        } else {
          return cb()
        }
      };
      return {
        userList: [],
        total: 0,
        addDialogVisible: false,
        addId: null,
        addName: '',
        addForm: {
          priority: null,
        },
        addFormRules: {
          priority: [{required: true, message: '请选择', trigger: 'change'}],
        },
        editDialogVisible: false,
        editId2: '',
        editForm: {
          sourceIp: '',
          sourceIp2: '',
        },
        editFormRules: {
          sourceIp: [{required: true, validator: this.Global.valiIp, trigger: 'blur'}],
          sourceIp2: [{required: false, validator: checkIp, trigger: 'blur'}],
        },
        keyDialogVisible: false,
        editPriority: '',
        editId: null,
        keyForm: {
          priority: null,
        },
        keyFormRules: {
          priority: [{required: true, message: '请选择', trigger: 'change'}],
        },
        configDialogVisible: false,
        configForm: {
          localName: '',
          peerName: '',
          devKey: '',
          cryptKey: '',
          localName2: '',
          peerName2: '',
          devKey2: '',
          cryptKey2: '',
        },
        configFormRules: {
          localName: [{required: true, message: '请输入本端用户名', trigger: 'blur'}],
          peerName: [{required: true, message: '请输入对端用户名', trigger: 'blur'}],
          devKey: [{required: true, validator: checkSource, trigger: 'blur'}],
          cryptKey: [{required: true, validator: checkSource, trigger: 'blur'}],
          devKey2: [{required: false, validator: checkSource2, trigger: 'blur'}],
          cryptKey2: [{required: false, validator: checkSource2, trigger: 'blur'}],
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
        const {data: res} = await this.$http.get(`sourceConfig/get?ts=${new Date().getTime()}`);
        if (res.code !== 0) return this.$message.error((res.msg !== '' && res.msg !== null) ? res.msg : '获取数据失败！');
        this.userList = res.data;
        this.total = res.data.total;
      },
      async deleteByName(name, id) {
        const confirmResult = await this.$confirm(
          '此操作将禁用该密钥源配置，是否继续？', '提示', {
            confirmButtonText: '确定',
            cancelButtontext: '取消',
            type: 'warning',
          }
        ).catch(err => err);
        if (confirmResult !== 'confirm') {
          return
        }
        const {data: res} = await this.$http.get(`sourceConfig/delete?ts=${new Date().getTime()}`, {
          params: {
            'priority': name,
            'id': id
          }
        });
        if (res.code !== 0) return this.$message.error((res.msg !== '' && res.msg !== null) ? res.msg : '禁用失败！');
        this.$message.success('禁用成功！');
        this.getUserList();
      },
      async addByName(name, id) {
        this.addDialogVisible = true;
        this.addId = id;
        this.addName = name;
      },
      submitAdd() {
        this.$refs.addFormRef.validate(async valid => {
          if (!valid) return;
          const {data: res} = await this.$http.get(`sourceConfig/add?ts=${new Date().getTime()}`, {
            params: {
              'priority': this.addForm.priority,
              'id': this.addId
            }
          });
          if (res.code !== 0) return this.$message.error((res.msg !== '' && res.msg !== null) ? res.msg : '启用失败！');
          this.$message.success('启用成功！');
          this.addDialogVisible = false;
          this.getUserList();
        })
      },
      addDialogClosed() {
        this.$refs.addFormRef.resetFields();
      },
      editIpMethod(row) {
        this.editDialogVisible = true;
        this.editForm.sourceIp = row.sourceIp;
        this.editForm.sourceIp2 = row.sourceIp2;
        this.editId2 = row.id;
      },
      submitEdit() {
        this.$refs.editFormRef.validate(async valid => {
          if (!valid) return;
          const {data: res} = await this.$http.post('sourceConfig/update', {
            'id': this.editId2,
            'sourceIp': this.editForm.sourceIp,
            'sourceIp2': this.editForm.sourceIp2
          });
          if (res.code !== 0) return this.$message.error((res.msg !== '' && res.msg !== null) ? res.msg : '修改IP失败！');
          this.$message.success('修改IP成功！');
          this.editDialogVisible = false;
          this.getUserList();
        })
      },
      editDialogClosed() {
        this.$refs.editFormRef.resetFields();
      },
      editMethod(name, id) {
        this.keyDialogVisible = true;
        this.editPriority = name;
        this.editId = id;
        this.keyForm.priority = Number(name);
      },
      keyEnsure() {
        this.$refs.keyFormRef.validate(async valid => {
          if (!valid) return;
          const {data: res} = await this.$http.get(`sourceConfig/updatePriority?ts=${new Date().getTime()}`, {
            params: {
              'id': this.editId,
              'newPriority': this.keyForm.priority,
              'oldPriority': this.editPriority
            }
          });
          if (res.code !== 0) return this.$message.error((res.msg !== '' && res.msg !== null) ? res.msg : '修改优先级失败！');
          this.$message.success('修改优先级成功！');
          this.keyDialogVisible = false;
          this.getUserList();
        })
      },
      keyDialogClosed() {
        this.$refs.keyFormRef.resetFields();
      },
      handleChange(val) {},
      async editQkdMethod(row) {
        this.configDialogVisible = true;
        const {data: res} = await this.$http.get(`sourceConfig/getQKD?ts=${new Date().getTime()}`);
        if (res.code !== 0) return this.$message.error((res.msg !== '' && res.msg !== null) ? res.msg : '请求失败！');
        const config = res.data.config;
        const config2 = res.data.config2;
        this.configForm.cryptKey = config.cryptKey;
        this.configForm.devKey = config.devKey;
        this.configForm.localName = config.localName;
        this.configForm.peerName = config.peerName;
        this.configForm.cryptKey2 = config2.cryptKey;
        this.configForm.devKey2 = config2.devKey;
        this.configForm.localName2 = config2.localName;
        this.configForm.peerName2 = config2.peerName;
      },
      submitConfig() {
        this.$refs.configFormRef.validate(async valid => {
          if (!valid) return;
          const {data: res} = await this.$http.post('sourceConfig/updateQKD',
            {
              "config": {
                "cryptKey": this.configForm.cryptKey,
                "devKey": this.configForm.devKey,
                "localName": this.configForm.localName,
                "peerName": this.configForm.peerName,
              },
              "config2": {
                "cryptKey": this.configForm.cryptKey2,
                "devKey": this.configForm.devKey2,
                "localName": this.configForm.localName2,
                "peerName": this.configForm.peerName2,
              }
            }
          );
          if (res.code !== 0) return this.$message.error((res.msg !== '' && res.msg !== null) ? res.msg : '修改参数失败！');
          this.$message.success('修改参数成功！');
          this.configDialogVisible = false;
          this.getUserList();
        })
      },
      configDialogClosed() {
        this.$refs.configFormRef.resetFields();
      },
    }
  }
</script>
<style lang="less">
  .sourceWrap {
    min-width: 1000px;
    .el-select {
      width: 100%;
    }
    .el-table__row > td {
      border: none;
    }
    .el-table::before {
      height: 0;
    }
    .el-collapse-item__header {
      font-size: 16px;
      padding-left: 24px;
    }
  }
</style>
