<template>
  <div style="min-width: 1200px;">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>日志告警</el-breadcrumb-item>
      <el-breadcrumb-item>日志审计</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row>
        <el-form :inline="true" :model="formInline" ref="formInline" class="demo-form-inline">
          <el-form-item prop="operator">
            <el-select clearable v-model.trim="formInline.operator" placeholder="选择操作员" @visible-change="oneClick($event)">
              <el-option
                v-for="(item,index) in optionsOne"
                :key="index"
                :label="item"
                :value="item">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="operateModel">
            <el-select clearable v-model.trim="formInline.operateModel" placeholder="选择操作模块" @visible-change="twoClick($event)">
              <el-option
                v-for="(item,index) in optionsTwo"
                :key="index"
                :label="item"
                :value="item">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="detail">
            <el-select clearable v-model.trim="formInline.detail" placeholder="选择操作功能" @visible-change="threeClick($event)">
              <el-option
                v-for="(item,index) in optionsThree"
                :key="index"
                :label="item"
                :value="item">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="startDate">
            <el-date-picker type="date" placeholder="请输入开始日期" value-format="yyyy-MM-dd"
                            :picker-options="pickerStartDate" v-model.trim="formInline.startDate"></el-date-picker>
          </el-form-item>
          <el-form-item prop="endDate">
            <el-date-picker type="date" placeholder="请输入结束日期" value-format="yyyy-MM-dd"
                            :picker-options="pickerCloseDate" v-model.trim="formInline.endDate"></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="getUserList">查询</el-button>
            <el-button  @click="resetForm('formInline')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-row>
      <el-table :data="userlist" stripe>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="操作员" prop="operator">
          <template slot-scope="scope">
            <span>{{scope.row.operator === null ? '-' : scope.row.operator}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作模块" :show-overflow-tooltip="true" prop="operateModel"></el-table-column>
        <el-table-column label="操作功能" :show-overflow-tooltip="true" prop="detail"></el-table-column>
        <el-table-column label="IP地址" :show-overflow-tooltip="true" prop="operateIp">
          <template slot-scope="scope">
            <span>{{scope.row.operateIp === null ? '-' : scope.row.operateIp}}</span>
          </template>
        </el-table-column>
        <el-table-column label="耗时(毫秒)" prop="execTime"></el-table-column>
        <el-table-column label="操作结果" prop="operateStatus">
          <template slot-scope="scope">
            <span v-if="scope.row.operateStatus === 0">成功</span>
            <span v-else-if="scope.row.operateStatus === 1">失败</span>
          </template>
        </el-table-column>
        <el-table-column label="操作时间" width="140" :show-overflow-tooltip="true" prop="updateTime">
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
  </div>
</template>
<script>
  export default {
    data() {
      return {
        userlist: [],
        optionsOne:[],
        optionsThree:[],
        optionsTwo:[],
        formInline: {
          operator:'',
          operateModel:'',
          detail:'',
          startDate: '',
          endDate: '',
        },
        queryInfo: {
          pagenum: 1,
          pagesize: 10
        },
        total: 0,
        pickerStartDate: {
          disabledDate: time => {
            if (this.formInline.endDate) {
              return time.getTime() > new Date(this.formInline.endDate).getTime()
            } else {
              return time.getTime() > Date.now();
            }
          }
        },
        pickerCloseDate: {
          disabledDate: time => {
            if (this.formInline.startDate) {
              return (
                time.getTime() > Date.now() ||
                time.getTime() < new Date(this.formInline.startDate).getTime() - 8.64e7
              );
            } else {
              return time.getTime() > Date.now();
            }
          }
        },
      }
    },
    beforeRouteEnter: (to, form, next) => {
      const loginType = window.sessionStorage.getItem('accountTypeLogin');
      if (loginType === '2' || loginType === '9') {
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
        // let dataParam = {};
        // if (this.formInline.startDate !== "") {
        //   dataParam.startTime = this.formInline.startDate;
        // }
        // if (this.formInline.endDate !== "") {
        //   dataParam.endTime = this.formInline.endDate;
        // }
        const {data: res} = await this.$http.post(`logger/listOperateLogs/${this.queryInfo.pagenum}/${this.queryInfo.pagesize}?ts=${new Date().getTime()}`, {
          "detail":this.formInline.detail,
          "endTime": this.formInline.endDate,
          "operateModel": this.formInline.operateModel,
          "operator": this.formInline.operator,
          "startTime": this.formInline.startDate
        });
        if (res.code === 0) {
          this.userlist = res.data.list;
          this.total = res.data.total;
        } else {
          return this.$message.error('获取日志失败！')
        }
      },
      handleSizeChange(newSize) {
        this.queryInfo.pagesize = newSize;
        this.getUserList()
      },
      handleCurrentChange(newPage) {
        this.queryInfo.pagenum = newPage;
        this.getUserList()
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
        this.getUserList()
      },
      oneClick(even) {//true为下拉
        if (even !== true) return this.optionsOne = [];
        this.getSelectOne();
      },
      twoClick(even) {
        if (even !== true) return this.optionsTwo = [];
        this.getSelectTwo();
      },
      threeClick(even) {
        if (even !== true) return this.optionsThree = [];
        this.getSelectThree();
      },
      async getSelectOne() {
        const {data: res} = await this.$http.get('logger/getOperator');
        if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '获取操作员失败！');
        this.optionsOne = res.data;
      },
      async getSelectTwo() {
        const {data: res} = await this.$http.get('logger/getOperateModel',{params:{
            'operator':this.formInline.operator
          }});
        if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '获取操作模块失败！');
        this.optionsTwo = res.data;
      },
      async getSelectThree() {
        const {data: res} = await this.$http.get('logger/getOperateDetail',{params:{
            'operator':this.formInline.operator,'operateModel':this.formInline.operateModel
          }});
        if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '获取操作功能失败！');
        this.optionsThree = res.data;
      },
    }
  }
</script>
<style lang="less" scoped>
  .el-form-item{
    .el-input{
      width: 216px;
    }
  }
</style>
