<template>
  <div class="appMuser">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>应用管理</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
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
        <el-table-column label="操作" width="330px">
          <template slot-scope="scope">
            <el-button type="success" size="mini" @click="goDeviceDetail(scope.row.appId,scope.row.appType)">终端列表
            </el-button>
            <el-button type="warning" size="mini" @click="goDeviceTotal(scope.row.appId)">量子密钥统计</el-button>
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
  </div>
</template>
<script>
  export default {
    data() {
      return {
        userId: null,
        isShow: '',
        userlist: [],
        queryInfo: {
          pagenum: 1,
          pagesize: 10
        },
        total: 0,
        expands:[],
        getRowKeys (row) {
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
      const loginType = window.sessionStorage.getItem('accountTypeLogin');
      if (loginType === '9' || loginType === '1') {
        next()
      } else {
        return next('/404')
      }
    },
    created() {
      this.userId = window.sessionStorage.getItem('userId');
      this.getUserList();
    },
    methods: {
      async getUserList() {
        const {data: res} = await this.$http.get(`userApp/getManagerApps/${this.queryInfo.pagenum}/${this.queryInfo.pagesize}?ts=${new Date().getTime()}`, {
          params: {'userId': this.userId}
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
      expandChange (row, expandedRows) {
        if (expandedRows.length) {
          this.expands = [];
          if (row) {
            this.expands.push(row.appId)
          }
        } else {
          this.expands = []
        }
      },
      goDeviceDetail(id, type) {
        this.$router.push({path: '/pwsp', query: {'id': id, 'type': type, 'urlType': 2}})
      },
      goDeviceConfig(id) {
        this.$router.push({path: '/qems', query: {'id': id, 'urlType': 2}})
      },
      goDeviceTotal(id) {
        this.$router.push({path: '/total', query: {'id': id, 'urlType': 2}})
      }
    }
  }
</script>
<style lang="less" scoped>
  .appMuser {
    min-width: 1000px;
    .demo-table-expand .el-form-item {
      margin-right: 0;
      margin-bottom: 0;
      width: 50%;
    }
  }
</style>
