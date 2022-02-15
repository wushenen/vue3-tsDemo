<template>
  <div style="min-width: 1100px">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item>分组管理</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row>
        <el-button type="primary" @click="addDialogVisible = true">创建</el-button>
      </el-row>
      <el-table :data="userList" stripe>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="分组名称" prop="groupName" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="分组唯一标志" prop="groupCode" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="分组描述" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            {{scope.row.groupDescribe == null || scope.row.groupDescribe == '' ? '-' : scope.row.groupDescribe}}
          </template>
        </el-table-column>
        <el-table-column label="时间" prop="updateTime" width="170px"></el-table-column>
        <el-table-column label="操作" width="300px">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="deleById(scope.row.groupId,scope.row.groupName)">删除
            </el-button>
            <el-button size="mini" type="primary" @click="editById(scope.row)">编辑
            </el-button>
            <el-button size="mini" type="info" @click="goDeviceDetail(scope.row.groupId)">详情(终端)
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
    <el-dialog title="创建分组" :visible.sync="addDialogVisible" width="500px" @close="addDialogClosed">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="110px">
        <el-form-item label="分组名称" prop="groupName">
          <el-input v-model.trim="addForm.groupName"></el-input>
        </el-form-item>
        <el-form-item label="分组唯一标识" prop="groupCode">
          <el-input v-model.trim="addForm.groupCode"></el-input>
        </el-form-item>
        <el-form-item label="分组描述" prop="groupDescribe">
          <el-input type="textarea" maxlength="128" show-word-limit :rows="8" v-model.trim="addForm.groupDescribe"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" v-preventReClick="1000"  @click="submitAdd">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="编辑分组" :visible.sync="editDialogVisible" width="500px" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="110px">
        <el-form-item label="分组名称" prop="groupName">
          <el-input v-model.trim="editForm.groupName"></el-input>
        </el-form-item>
        <el-form-item label="分组唯一标识" prop="groupCode">
          <el-input v-model.trim="editForm.groupCode" disabled></el-input>
        </el-form-item>
        <el-form-item label="分组描述" prop="groupDescribe">
          <el-input type="textarea" maxlength="128" show-word-limit :rows="8" v-model.trim="editForm.groupDescribe"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" v-preventReClick="1000" @click="submitEdit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        userList: [],
        queryInfo: {
          pagenum: 1,
          pagesize: 10
        },
        total: 0,
        addDialogVisible: false,
        addForm: {
          groupName: '',
          groupCode: '',
          groupDescribe: '',
        },
        addFormRules: {
          groupName: [{required: true, message: '必填项不能为空', trigger: 'blur'},{ min: 1, max: 32, message: '长度不能超过32个字符', trigger: 'blur' }],
          groupCode: [{required: true, message: '必填项不能为空', trigger: 'blur'},{ min: 1, max: 32, message: '长度不能超过32个字符', trigger: 'blur' }],
          groupDescribe:[{ min: 0, max: 128, message: '长度不能超过128个字符', trigger: 'blur' }],
        },
        editId:'',
        editDialogVisible: false,
        editForm: {
          groupName: '',
          groupCode: '',
          groupDescribe: '',
        },
        editFormRules: {
          groupName: [{required: true, message: '必填项不能为空', trigger: 'blur'},{ min: 1, max: 32, message: '长度不能超过32个字符', trigger: 'blur' }],
          groupCode: [{required: true, message: '必填项不能为空', trigger: 'blur'},{ min: 1, max: 32, message: '长度不能超过32个字符', trigger: 'blur' }],
          groupDescribe:[{ min: 0, max: 128, message: '长度不能超过128个字符', trigger: 'blur' }],
        },
      }
    },
    beforeRouteEnter: (to, form, next) => {
      const loginType = window.sessionStorage.getItem('accountTypeLogin');
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
        const {data: res} = await this.$http.get(`group/groupList/${this.queryInfo.pagenum}/${this.queryInfo.pagesize}?ts=${new Date().getTime()}`);
        if (res.code !== 0) return this.$message.error('获取用户信息失败！');
        this.userList = res.data.list;
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
          const {data: res} = await this.$http.post('group/addGroup', this.addForm);
          if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '创建失败！');
          this.$message.success('创建成功！');
          this.addDialogVisible = false;
          this.getUserList();
        })
      },
      editById(row){
        this.editDialogVisible=true;
        this.editForm.groupCode=row.groupCode;
        this.editForm.groupDescribe=row.groupDescribe;
        this.editForm.groupName=row.groupName;
        this.editId=row.groupId;
      },
      editDialogClosed() {
        this.$refs.editFormRef.resetFields();
        this.getUserList();
      },
      submitEdit() {
        this.$refs.editFormRef.validate(async valid => {
          if (!valid) return;
          const {data: res} = await this.$http.post('group/updateGroupInfo', {
            'groupId':this.editId,'groupName':this.editForm.groupName,'groupDescribe':this.editForm.groupDescribe});
          if (res.code !== 0) return this.$message.error(res.msg !== null && res.msg !== '' ? res.msg : '编辑失败！');
          this.$message.success('编辑成功！');
          this.editDialogVisible = false;
          this.getUserList();
        })
      },
      async deleById(id,name) {
        const confirmResult = await this.$confirm(
          `此操作将删除分组:${name}，是否继续？`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
        ).catch(err => err);
        if (confirmResult !== 'confirm') {
          return
        }
        const {data: res} =await this.$http.get(`group/deleteGroup?ts=${new Date().getTime()}`, {params: {'groupId': id}});
        if (res.code !== 0) return this.$message.error(res.msg !== '' && res.msg !== null ? res.msg : '删除失败！');
        this.$message.success('删除成功！');
        this.getUserList()
      },
      goDeviceDetail(id) {
        this.$router.push({path: '/groupDetail', query: {'groupId': id}})
      },
    }
  }
</script>
<style>

</style>
