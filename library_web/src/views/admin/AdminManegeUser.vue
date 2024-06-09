<script>
import http from "@/utils/http";

export default {
  name: "AdminManegeUser",
  data() {
    return {
      userList:[],
      total:0,
      loading: true,
      pageSize: 9,
      currentPage:1,
      search:'',
      dialog:{
        updateDialog:false,
      },
      formLabelWidth: '80px',
      userForm:{
        id:'',//UID
        username:'',//用户名
        avatar: '',//头像地址
        background: '',//背景地址
        email: '',//邮箱
        signature: '',//个性签名
        maxBorrow: '',//最多可借书数
        needToPay: '',//待缴金额
        realName:'',//真实姓名
        idCardNumber:'',//身份证号
        phoneNumber:'',//电话号码
        address:'',//地址
        createTime:'',//创建时间
        updateTime:'',//修改时间
      },
    }
  },
  methods:{
    searchUser(){
      this.getUserList();
    },
    //获取书籍列表
    getUserList(){
      this.loading = true;
      http.post(`/admin/getUserList?userId=${this.search}&currentPage=${this.currentPage}&pageSize=${this.pageSize}`).then(res => {
        if(res.data.code === 200){
          this.userList = res.data.data.records;
          this.total = res.data.data.total;
        } else {
          this.$message.error('获取用户列表失败');
        }
        this.loading = false;
      })
    },
    getPageNum(newPage){
      this.currentPage = newPage;
      this.getUserList();
    },
    ban(userId){
      this.$confirm('确定要封禁该用户吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        lockScroll: false,
        type: 'warning'
      }).then(() => {
        http.delete(`/admin/banUser?userId=${userId}`).then(res => {
          if(res.data.code === 200){
            this.$message.success('封禁成功');
            this.getUserList();
          } else {
            this.$message.error('权限不足，若为管理员，请重新登陆获取权限');
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消封禁'
        });
      });
    },
    update(user){
      this.userForm = user;
      this.dialog.updateDialog = true;
    },
    submitUpload(){
      let formData = new FormData();
      formData.append("id", this.userForm.id);
      formData.append("realName", this.userForm.realName);
      formData.append("address", this.userForm.address);
      formData.append("phoneNumber", this.userForm.phoneNumber);
      formData.append("idCardNumber", this.userForm.idCardNumber);
      http.post('/admin/updateUser',formData).then(res => {
        if (res.data.code === 200){
          this.$message.success('更新成功');
          this.dialog.updateDialog = false;
          this.getUserList();
        } else{
          this.$message.error('更新失败');
        }
      })
    },
    addUser(){
      this.$notify({
        title: '服了',
        message: '自己去注册，这代码我不写了',
        showClose: false
      });
    },
  },
  mounted() {
    this.getUserList();
  }
}
</script>

<template>
  <div style="margin-right: 16px">
    <el-input placeholder="请输入查询用户的UID" v-model="search" class="input-with-select" style="width: 90%">
      <el-select v-model="select" slot="prepend" placeholder="UID" style="width: 100px">
        <el-option label="UID" value="1"></el-option>
      </el-select>
      <el-button slot="append" icon="el-icon-search" @click="searchUser"></el-button>
    </el-input>

    <el-button style="width: 10%" type="primary" plain @click="addUser">
      新增用户
    </el-button>

    <el-table
        :data="userList"
        :header-cell-style="{ 'text-align': 'center' }"
        style="width: 100%">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="真实姓名:">
              <span>{{ props.row.realName }}</span>
            </el-form-item>
            <el-form-item label="邮箱:">
              <span>{{ props.row.email }}</span>
            </el-form-item>
            <el-form-item label="电话号码:">
              <span>{{ props.row.phoneNumber }}</span>
            </el-form-item>
            <el-form-item label="住址:">
              <span>{{ props.row.address }}</span>
            </el-form-item>
            <el-form-item label="身份证号:">
              <span>{{ props.row.idCardNumber }}</span>
            </el-form-item>
            <el-form-item label="当前仍可借:">
              <span>{{ props.row.maxBorrow }}</span>
            </el-form-item>
            <el-form-item label="需付款:">
              <span>{{ props.row.needToPay }}元</span>
            </el-form-item>
            <el-form-item label="创建时间:">
              <span>{{ props.row.createTime }}</span>
            </el-form-item>
            <el-form-item label="修改时间:">
              <span>{{ props.row.updateTime }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
          fixed
          prop="id"
          label="UID"
          align="center">
      </el-table-column>
      <el-table-column
          prop="nickname"
          label="用户"
          align="center">
        <template slot-scope="scope">
          <el-image :src="scope.row.avatar" alt="用户头像" fit="scale-down" style="width: 80px">
          </el-image>
          <br>
          <div style="width: 100%">{{scope.row.nickname}}</div>
        </template>
      </el-table-column>
      <el-table-column
          prop="username"
          label="用户名"
          align="center">
      </el-table-column>
      <el-table-column
          prop="signature"
          label="签名"
          align="center">
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作">
        <template slot-scope="scope">
          <el-button
              @click="ban(scope.row.id)"
              type="text"
              size="medium">
            封禁
          </el-button>
          <el-button
              @click="update(scope.row)"
              type="text"
              size="medium">
            实名认证
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        style="padding-bottom: 15px"
        background
        layout="prev, pager, next"
        :page-size="pageSize"
        :total="total"
        hide-on-single-page
        :current-page="currentPage"
        @current-change="getPageNum">
    </el-pagination>

    <!--    实名认证的弹窗-->
    <el-dialog
        custom-class="cardDialog"
        :lock-scroll="false"
        title="实名认证窗口"
        :visible.sync="dialog.updateDialog">
      <el-form :model="userForm" status-icon ref="userForm">
        <el-form-item label="姓名" :label-width="formLabelWidth" prop="realName" required>
          <el-input v-model="userForm.realName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话号码" :label-width="formLabelWidth" prop="phoneNumber" required>
          <el-input v-model="userForm.phoneNumber" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址" :label-width="formLabelWidth" prop="address" required>
          <el-input v-model="userForm.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" :label-width="formLabelWidth" prop="idCardNumber" required>
          <el-input v-model="userForm.idCardNumber" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog.updateDialog = false">取 消</el-button>
        <el-button type="primary" @click="submitUpload">认 证<i class="el-icon-upload el-icon--right"></i></el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>
