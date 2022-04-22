<template>
  <div class="keyInfo" style="min-width: 900px">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item :to="{path:'/deviceUser'}">终端用户</el-breadcrumb-item>
      <el-breadcrumb-item>量子密钥管控</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <h2>量子密钥管控详情：</h2>
   
      <el-col :span="7">
      <div style="display: flex;justify-content: flex-start;padding: 40px 30px">
        <div class="content" @click="getKey">
          <img src="../assets/image/04.png" alt="">
          <el-button type="text" style="font-size:25px">分配额度</el-button>
        </div>
        <!-- <div class="content" @click="exportKey">
          <img src="../assets/image/02.png" alt="">
          <el-button type="text">导出</el-button>
        </div>
        <div class="content" @click="recallKey">
          <img src="../assets/image/03.png" alt="">
          <el-button type="text">撤回</el-button>
        </div>
        <div class="content" @click="reductionKey">
          <img src="../assets/image/10.png" alt="">
          <el-button type="text">还原</el-button>
        </div>
        <div class="content" @click="destoryKey">
          <img src="../assets/image/05.png" alt="">
          <el-button type="text">销毁</el-button>
        </div> -->
      </div>
      <div id="pie"></div>
      </el-col>
      <el-col :span="14" >
        <el-table :data="userList" stripe  :header-cell-style="{ 'text-align': 'center', background: '#fff' }" :cell-style="{ 'text-align': 'center' }">
        <el-table-column type="index" label="序号" width="80"></el-table-column>
        <el-table-column label="密钥ID" :show-overflow-tooltip="true" width="400" prop="keyId" ></el-table-column>
        <el-table-column label="操作时间" width="200" :show-overflow-tooltip="true" prop="createTime">
        </el-table-column>
      </el-table>
       <el-pagination
        @current-change="handleCurrentChange"
        :current-page="queryInfo.offset"
        :page-size="queryInfo.pagesize"
        layout="total, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
      </el-col> 
    </el-card>
    <el-dialog :title="title" :visible.sync="keyDialogVisible" width="520px" @close="keyDialogClosed">
      <el-form :model="keyForm" :rules="keyFormRules" ref="keyFormRef" label-width="80px">
        <el-form-item label="密钥ID" prop="keyId">
          <el-select clearable style="width: 100%" v-model="keyForm.keyId" placeholder="请选择"
                     @visible-change="oneClick($event)">
            <el-option
              v-for="(item,index) in options"
              :key="index"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-collapse accordion @change="handleChange">
          <el-collapse-item :name="1" title="其他">
            <el-form-item prop="keyIdInput">
              <el-input clearable v-model.trim="keyForm.keyIdInput" placeholder="请输入密钥ID"></el-input>
            </el-form-item>
          </el-collapse-item>
        </el-collapse>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="keyDialogVisible = false">取 消</el-button>
        <el-button type="primary" v-preventReClick="1000" @click="keyEnsure">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import * as echarts from 'echarts';

  export default {
    data() {
      const checkBase64 = (rule, value, cb) => {
        const reg = /^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$/;
        if (value !== '') {
          if (reg.test(value)) {
            return cb()
          }
          cb(new Error('请输入base64格式的字符串'))
        } else {
          return cb()
        }
      };
      return {
        userList:[],
        queryInfo:{
          query:'',
          offset:1,
          pagesize:10,
        },
        total:'',
        options: [],
        title: '',
        deviceName: this.$route.query.deviceName,
        keyDialogVisible: false,
        keyForm: {
          keyId: '',
          keyIdInput: ''
        },
        keyFormRules: {
          keyIdInput: [{required: false, validator: checkBase64, trigger: 'blur'}]
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
      this.getUserList1()
    },
    methods: {
      async getUserList() {
        const {data: res} = await this.$http.get(`keyInfo/getKeyUsedInfo?ts=${new Date().getTime()}`, {params: {'applicant': this.deviceName}});
        if (res.code !== 0) return this.$message.error((res.msg !== '' && res.msg !== null) ? res.msg : '请求失败！');
        this.chart(res.data.totalNum, res.data.usedNum)
      },
      handleChange(val) {
        if (val === '') {
          this.keyForm.keyIdInput = '';
        }
      },
      recallKey() {
        this.keyDialogVisible = true;
        this.title = '撤回';
      },
      destoryKey() {
        this.keyDialogVisible = true;
        this.title = '销毁';
      },
      reductionKey(){
        this.keyDialogVisible = true;
        this.title = '还原';
      },
      keyEnsure() {
        this.$refs.keyFormRef.validate(async valid => {
          if (!valid) return;
          if (this.keyForm.keyId === '' && this.keyForm.keyIdInput === '') return this.$message.error('请选择密钥ID或者手动输入密钥ID！');
          let url = '';
          if (this.title === '撤回') {
            url = 'keyInfo/recallKey'
          } else if(this.title === '销毁'){
            url = 'keyInfo/destroyKey'//销毁
          }else if(this.title === '还原'){
            url = 'keyInfo/reductionKey'//还原
          }
          let id = this.keyForm.keyId === '' ? this.keyForm.keyIdInput : this.keyForm.keyId;
          const {data: res} = await this.$http.post(url, {'keyId': id});
          if (res.code !== 0) return this.$message.error((res.msg !== '' && res.msg !== null) ? res.msg : this.title + '失败！');
          this.$message.success(this.title + '成功！');
          this.keyDialogVisible = false;
        })
      },
      keyDialogClosed() {
        this.$refs.keyFormRef.resetFields();
      },
      oneClick(even) {
        if (even === true) {//下拉框显示
          this.getSelectValue();
        } else {
          this.options = [];
        }
      },
      async getSelectValue() {
        let url = '';
        if (this.title === '撤回') {
          url = 'keyInfo/getApplicantKeyId'
        } else if(this.title === '销毁'){
          url = 'keyInfo/getCanDeleteApplicantKeyId'//销毁
        }else if(this.title === '还原'){
          url = 'keyInfo/getCanReductionApplicantKeyId'//还原
        }
        const {data: res} = await this.$http.post(url, {'applicant': this.deviceName});
        if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '获取密钥ID失败！');
        this.options = res.data;
      },
      async getKey() {
        const confirmResult = await this.$prompt('请输入量子密钥额度', '分配额度', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValidator: (value) => {
            const reg = /^[1-9]\d*$/;
            if (reg.test(value)) {
              if (value >= 1 && value <= 2000) {
                return
              } else {
                return '请输入1-2000范围内的整数'
              }
            }
            return '请输入1-2000范围内的整数'
          },
          inputType: 'text',
        }).catch(err => err);
        if (confirmResult.action !== 'confirm') {
          return
        }
        const {data: res} = await this.$http.get(`keyInfo/updateKeyLimit?ts=${new Date().getTime()}`, {
          params: {
            'applicant': this.deviceName,
            'keyNum': confirmResult.value
          }
        });
        if (res.code !== 0) return this.$message.error((res.msg !== '' && res.msg !== null) ? res.msg : '分配额度失败！');
        this.$message.success('分配额度成功！');
        this.getUserList();
      },
      exportKey() {
        let saveUrl = 'keyInfo/exportKeyInfos';
        let reqData = {'applicant': this.deviceName};
        this.$http.post(`${saveUrl}`, reqData, {responseType: 'blob'}).then(res => {
          const filename = decodeURI(res.headers['content-disposition'].split(';')[1].split('=')[1]);
          this.Global.downloadFile(res.data, filename);
        })
      },
      chart(total, use) {
        let myChart = echarts.init(document.getElementById('pie'));
        myChart.setOption({
          title: [{text: `量子密钥额度:${total}     量子密钥使用数量:${use}`, left: '50%', top: '90%', textAlign: 'center'}],
          series: [
            {
              type: 'gauge',
              radius: '65%',
              startAngle: 90,
              endAngle: -270,
              center: ['50%', '40%'],
              itemStyle: {
                color: '#409EFF',
              },
              pointer: {
                show: false
              },
              progress: {
                show: true,
                overlap: false,
                roundCap: true,
                clip: false,
                itemStyle: {
                  borderWidth: 1,
                  borderColor: '#409EFF'
                }
              },
              axisLine: {
                lineStyle: {
                  width: 15
                }
              },
              splitLine: {
                show: false,
                distance: 0,
                length: 10
              },
              axisTick: {//刻度样式
                show: false
              },
              axisLabel: {
                show: false,
                distance: 50
              },
              data: [
                {value: ((use / total) * 100).toFixed(2)=='NaN'?0:((use / total) * 100).toFixed(2)}
              ],
              detail: {//仪表盘详情，用于显示数据
                valueAnimation: true,
                fontSize: 30,
                color: 'auto',
                formatter: '{value}%',
                offsetCenter: ['0%', '0%'],
              }
            }
          ]
        });
        window.addEventListener("resize", function () {
          myChart.resize();
        });
      },
       //表格数据
     async  getUserList1(){
        const {data: res} = await this.$http.get(`/keyInfo/getDeviceKeyUsedInfo/${this.queryInfo.offset}?deviceName=${this.deviceName}&&?ts=${new Date().getTime()}`);
        if (res.code === 0) {
          this.userList = res.data.list;
          this.total = res.data.total;
        } else {
          return this.$message.error('获取信息失败！')
        }
        console.log(this.userList)
       },
       handleCurrentChange(offset){
        this.queryInfo.offset = offset;
        this.getUserList1();
       }
    }
  }

</script>
<style lang="less">
  .keyInfo {
    #pie {
      width: 450px;
      height: 350px;
    }
    h2 {
      border-bottom: 1px solid #eee;
      padding: 20px 30px;
    }
    .content {
      display: flex;
      flex-direction: row;
      justify-content: center;
      align-items: center;
      width: 300px;
      height: 100px;
      margin-left: 50px;
      background-color: #ecf5ff;
      border-radius: 6px;
      cursor: pointer;
      padding: 20px 30px;
      img {
        width: 42px;
        height: 42px;
      }
      .el-button--text {
        color: #409EFF;
        background: 0 0;
        font-size: 18px;
        padding: 0 10px;
      }
     
    }
    .el-table{
      margin-left: 120px;
    }
    .el-collapse-item__header {
      font-size: 16px;
      padding-left: 24px;
    }
    .el-pagination{
      margin-left: 120px;
    }
  }
</style>

