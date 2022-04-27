<template>
  <div style="min-width: 600px">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统管理</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="修改IP" name="first">
          <el-form :model="ipForm" ref="ipFormRef" :rules="ipFormRules" label-width="110px">
            <el-form-item label="IP地址" prop="ip">
              <el-input v-model.trim="ipForm.ip"  maxlength="16"></el-input>
            </el-form-item>
            <el-form-item label="子网掩码" prop="netMask">
              <el-input v-model.trim="ipForm.netMask"  maxlength="16"></el-input>
            </el-form-item>
            <el-form-item label="默认网关" prop="gateWay">
              <el-input v-model.trim="ipForm.gateWay"  maxlength="16"></el-input>
            </el-form-item>
            <el-form-item label="网卡名" prop="nicName">
              <el-select placeholder="请选择" v-model="ipForm.nicName">
                <el-option label="eth0" value="eth0"></el-option>
                <el-option label="eth1" value="eth1"></el-option>
                <el-option label="eth2" value="eth2"></el-option>
                <el-option label="eth3" value="eth3"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" v-preventReClick="1000"  @click="submitIPForm">提交</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="初始化" name="second">
          <div class="btn_div">
            <el-button type="primary"  @click="submitInit">初始化</el-button>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
<script>
  export default {
    data() {
      const checkIP = (rule, value, cb) => {
        const reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
        if (reg.test(value)) {
          return cb()
        }
        cb(new Error('请输入合法的IP地址'))
      };
      const checkMask = (rule, value, cb) => {
        const reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
        if (value !== '') {
          if (reg.test(value)) {
            return cb()
          }
          cb(new Error('请输入合法的子网掩码'))
        }
      };
      const checkWay = (rule, value, cb) => {
        const reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
        if (value !== '') {
          if (reg.test(value)) {
            return cb()
          }
          cb(new Error('请输入合法的默认网关'))
        }
      };
      return {
        activeName: 'first',
        ipForm: {
          ip: '',
          netMask: '',
          gateWay: '',
          nicName: ''
        },
        ipFormRules: {
          ip: [{required: true, validator: checkIP, trigger: 'blur'}],
          netMask: [{required: false, validator: checkMask, trigger: 'blur'}],
          gateWay: [{required: false, validator: checkWay, trigger: 'blur'}],
        },
      };
    },
    beforeRouteEnter: (to, form, next) => {
      const loginType = window.sessionStorage.getItem('accountTypeLogin');
      if (loginType === '1' || loginType === '9') {
        next()
      }else{
        return next('/404')
      }
    },
    methods: {
      handleClick() {
      },
      submitIPForm() {//修改IP
        this.$refs.ipFormRef.validate(async valid => {
          if (!valid) return;
          let data = {};
          if (this.ipForm.ip !== "") {
            data.ip = this.ipForm.ip;
          }
          if (this.ipForm.netMask !== "") {
            data.netMask = this.ipForm.netMask;
          }
          if (this.ipForm.gateWay !== "") {
            data.gateWay = this.ipForm.gateWay;
          }
          if (this.ipForm.nicName !== "") {
            data.nicName = this.ipForm.nicName;
          }
          this.$http({
            url: 'system/updateIpNetmaskAndGateway',
            method: 'post',
            data: data,
          }).then(res => {
            this.$message.success('修改成功，服务已经断开，请使用新的IP地址重新登录！');
          }).catch(err => {
            console.log(err)
          });
          this.$confirm('修改成功，服务已经断开，将跳转到登录页面，请使用新的链接重新登录!', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            window.sessionStorage.clear();
            this.$router.push('/login');
            window.location.reload()
          }).catch(() => {
            return
          })
        })
      },
      async submitInit() {
        const confirmResult = await this.$confirm(
          '此操作将初始化密码卡信息，是否继续？', '提示', {
            confirmButtonText: '确定',
            cancelButtontext: '取消',
            type: 'warning',
          }
        ).catch(err => err);
        if (confirmResult !== 'confirm') {
          return
        }
        const {data: res} = await this.$http.post('system/init');
        if (res.code !== 0) return this.$message.error(res.msg !== '' ? res.msg : '请求失败！');
        this.$message.success('初始化成功！');
      },
    }
  };
</script>
<style lang="less" scoped>
  .el-card {
    min-height: 450px;
    .el-form {
      width: 500px;
      padding: 20px;
      .el-select {
        width: 100%;
      }
    }
    .btn_div {
      padding: 20px;
    }
  }

</style>
