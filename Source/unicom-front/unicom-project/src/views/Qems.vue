<template>
  <div style="min-width: 900px;">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item v-if="$route.query.urlType" :to="{ path: '/applicationManagerUser' }">应用管理</el-breadcrumb-item>
      <el-breadcrumb-item v-else :to="{ path: '/applicationUser' }">应用管理</el-breadcrumb-item>
      <el-breadcrumb-item>配置</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-form style="margin-top: 20px" ref="formRef" :model="userList" :rules="formRules" label-width="140px">
        <el-form-item label="加密端口:" prop="encPort">
          <el-input v-model="userList.encPort"  maxlength="16"></el-input>
        </el-form-item>
        <el-form-item label="密钥更新频率(秒):" prop="encFreq">
          <el-input v-model="userList.encFreq"  maxlength="10"></el-input>
        </el-form-item>
        <el-form-item label="密钥充注:" required>
          <el-col :span="11">
            <el-form-item prop="startIndex">
              <el-input placeholder="开始值" v-model="userList.startIndex" style="width: 100%;"  maxlength="10"></el-input>
            </el-form-item>
          </el-col>
          <el-col class="line" :span="2" style="text-align: center">-</el-col>
          <el-col :span="11">
            <el-form-item prop="endIndex">
              <el-input placeholder="结束值" v-model="userList.endIndex" style="width: 100%;"  maxlength="10"></el-input>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item label="加密方式:">
          <el-radio-group v-model="userList.encType">
            <el-radio :label="0">不加密</el-radio>
            <el-radio :label="1">AES</el-radio>
            <el-radio :label="2">AES强制加密</el-radio>
            <el-radio :label="6">SM4</el-radio>
            <el-radio :label="7">SM4强制加密</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitUpdate">应用</el-button>
          <el-button @click="updateMethod">刷新</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>
  export default {
    data() {
      const valiEnd = (rule, value, cb) => {
        const reg = /^[1-9]\d*$/;
        if (reg.test(value)) {
          if (parseInt(value) < parseInt(this.userList.startIndex)) {
            cb(new Error("结束值必须大于开始值"))
          } else {
            cb()
          }
        }
        cb(new Error('请输入正整数'))
      };
      const valiStart = (rule, value, cb) => {
        const reg = /^[1-9]\d*$/;
        if (reg.test(value)) {
          if (parseInt(value) > parseInt(this.userList.endIndex)) {
            cb(new Error("开始值必须小于结束值"))
          } else {
            cb()
          }
        }
        cb(new Error('请输入正整数'))
      };
      return {
        userList: {
          encPort: null,
          encFreq: null,
          startIndex: null,
          endIndex: null
        },
        formRules: {
          encPort: [{required: true, validator: this.Global.valiPort, trigger: 'blur'}],
          encFreq: [{required: true, validator: this.Global.valiNumber, trigger: 'blur'}],
          startIndex: [{required: true, validator: valiStart, trigger: 'blur'}],
          endIndex: [{required: true, validator: valiEnd, trigger: 'blur'}],
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
        const {data: res} = await this.$http.get(`qems/getConfig?ts=${new Date().getTime()}`, {
          params: {
            'appId': this.$route.query.id,
          }
        });
        if (res.code !== 0) return this.$message.error('获取用户信息失败！');
        this.userList = res.data;
      },
      updateMethod() {
        this.$refs.formRef.resetFields();
        this.getUserList();
      },
      submitUpdate() {
        this.$refs.formRef.validate(async valid => {
          if (!valid) return;
          if ((this.userList.endIndex - this.userList.startIndex) > 2000) return this.$message.error('密钥充注数不得大于2000');
          let reqData = {
            'appId': this.$route.query.id,
            'encFreq': this.userList.encFreq,
            'encPort': this.userList.encPort,
            'encType': this.userList.encType,
            'startIndex': this.userList.startIndex,
            'endIndex': this.userList.endIndex,
          };
          let reqUrl = `qems/updateConfig`;
          const {data: res} = await this.$http.post(reqUrl, reqData);
          if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '应用失败！');
          this.$message.success('应用成功！');
          this.getUserList()
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
    width: 500px;
  }

  .el-radio {
    margin-bottom: 20px;
  }
</style>
