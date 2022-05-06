<template>
  <div class="login_container">
    <div class="headerDiv"></div>
    <div class="bg">
      <div class="wrap">
        <div class="content">
          <div class="contentLeft">
            <p><span>欢迎使用</span>{{Global.projectName}}</p>
            <p>用户管理、权限管理、日志审计等多模块设置</p>
            <p>全新的权限系统</p>
            <p>严格的审核机制</p>
            <p>全方位保障您的信息安全</p>
          </div>
          <el-form ref="loginFormRef" :model="loginForm" :rules="loginFormRules" class="login_form">
            <el-form-item>
              <span class="title">登录</span>
            </el-form-item>
            <el-form-item label="用户名" prop="userName">
              <el-input v-model.trim="loginForm.userName" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" @keyup.enter.native="login" v-model.trim="loginForm.password"
                        placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item class="btns">
              <el-button type="primary" @click="login">登录</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import '../assets/js/profill'
  import '../assets/js/sm3'
 import Cookies from 'js-cookie'

  export default {
    name: 'Login',
    data() {
      return {
        loginForm: {
          userName: '',
          password: '',
        },
        loginFormRules: {}
      }
    },
    methods: {
      login() {
        if (this.loginForm.userName === '' || this.loginForm.password === '') return this.$message.error('请输入用户名和密码！');
        this.$refs.loginFormRef.validate(async valid => {
          if (!valid){
             Cookies.remove("userName");
            Cookies.remove("password");
          }else{
             const newPsd = sm3(this.loginForm.password + this.loginForm.userName);
          const {data: res} = await this.$http.post('login/sysLogin', {
            'password': newPsd, 'userName': this.loginForm.userName
          });
          console.log(res.data)
          if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '登录失败！');    
          this.$router.push('/home')
         Cookies.set('token', res.data.token);
         Cookies.set('loginName', this.loginForm.userName);
          Cookies.set('accountTypeLogin', res.data.accountType);
         Cookies.set('userId', res.data.userId);
          }             
        })
      },
    }
  }
</script>
<style lang="less" scoped>
  .login_container {
    position: relative;
    height: 100%;
    min-width: 950px;
    .headerDiv {
      position: relative;
      height: 5px;
      line-height: 5px;
      padding: 0 30px;
      background-color: #549be9;
      color: rgba(43, 48, 57, 1);
      img {
        position: relative;
        top: -8px;
        width: 119px;
        height: 41px;
        vertical-align: middle;
      }
      .title {
        font-size: 26px;
        letter-spacing: 2px;
        color: rgba(34, 81, 129, 1);
        border-left: 2px solid #c0c4ccab;
        padding: 0 30px;
        margin: 0 30px;
      }
    }
    .bg {
      position: relative;
      height: calc(100% - 5px);
      min-height: 700px;
      background-color: #549be9;
      background-repeat: no-repeat;
      background-position: center;
      .wrap {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100%;
        .content {
          width: 1000px;
          height: 400px;
          .contentLeft {
            float: left;
            color: white;
            font-size: 18px;
            letter-spacing: 2px;
            font-family: serif;
            span {
              padding-right: 15px;
            }
            p {
              padding-bottom: 20px;
            }
            p:first-child {
              padding-bottom: 30px;
              font-size: 26px;
            }
          }
          .el-form {
            float: right;
            width: 400px;
            height: 400px;
            padding: 25px 25px;
            color: #6c6c6c;
            background: #fff;
            border: 1px solid #eee;
            .title {
              font-size: 20px;
              letter-spacing: 2px;
            }
            .el-button {
              width: 100%;
              margin-top: 20px;
            }
          }
        }
      }
    }
  }
</style>
