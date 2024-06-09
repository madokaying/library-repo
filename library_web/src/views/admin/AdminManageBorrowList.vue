<script>
import http from '@/utils/http'
export default {
  name: "AdminManageBorrowList",
  data() {
    return {
      currentPage:1,
      pageSize:9,
      borrowList:[],
      bookIdentifier:'',//点击tag赋值
      tbBorrow:{
        borrowId:'',
        userId:'',
        bookId:'',
        bookIdentifier:'',
        state:'',
        createdTime:'',
        checkedTime:'',
        deleteFlag:'',
      },
    }
  },
  methods: {
    //获取到借书申请
    getBorrowList(){
      http.post(`admin/getBorrowList`).then(res => {
        if (res.data.code === 200){
          this.borrowList = res.data.data;
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    getPageNum(newPage) {
      this.currentPage = newPage;
    },
    selectIdentifier(Identifier){
      this.bookIdentifier = Identifier;
    },
    updateBorrow(){
      http.post(`admin/updateBorrow`,this.tbBorrow).then(res => {
        if (res.data.code=== 200){
          this.$message({
            type: 'success',
            message: '操作成功'
          });
        }else {
          this.$message.error('操作失败，请联系管理员');
        }
      })
    },
    pass(scope){
      if(this.bookIdentifier === ''){
        this.$message.error('请先选择书籍');
      } else {
        this.$confirm(`确认通过该借书(编号${this.bookIdentifier})申请吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          lockScroll: false
        }).then(() => {
          this.tbBorrow = scope;
          this.tbBorrow.bookIdentifier = this.bookIdentifier;
          this.tbBorrow.state = 1;
          this.updateBorrow();
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作'
          });
        });
      }
    },
    reject(scope){
      this.$confirm(`确认拒绝该申请吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        lockScroll: false
      }).then(() => {
        this.tbBorrow = scope;
        this.tbBorrow.state = 2;
        this.updateBorrow();
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消操作'
        });
      });
    },
    isReturnBook(scope){
      if(scope.state === 1){
        this.$confirm(`确认本书已归还图书馆了吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          lockScroll: false
        }).then(() => {
          this.tbBorrow = scope;
          this.tbBorrow.state = 3;
          this.updateBorrow();
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作'
          });
        });
      }else {
        this.$message.error('只有处于借出状态的书才能归还');
      }
    }
  },
  computed:{
    paginatedList() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      return this.borrowList.slice(startIndex, endIndex); // 根据当前页码和每页显示的条目数返回分页后的数据列表
    },
  },
  mounted() {
    this.getBorrowList();
  },
}
</script>

<template>
<div style="margin-right: 16px">
  <span>待审批列表，这里写分类把，到时候按这个来显示需要的数据</span>
  <el-table
      :data="paginatedList"
      :header-cell-style="{ 'text-align': 'center' }"
      style="width: 100%">
    <el-table-column
        fixed
        prop="userAvatar"
        label="用户"
        align="center">
      <template slot-scope="scope">
        <el-image :src="scope.row.userAvatar" alt="用户头像" fit="scale-down" style="width: 80px">
        </el-image>
        <br>
        <div style="width: 100%">{{scope.row.userNickname}}</div>
      </template>
    </el-table-column>
    <el-table-column
        prop="bookCover"
        label="书籍"
        align="center">
      <template slot-scope="scope">
        <el-image :src="scope.row.bookCover" alt="书籍封面" fit="scale-down" style="width: 80px">
        </el-image>
        <br>
        <div style="width: 100%">{{scope.row.bookName}}</div>
      </template>
    </el-table-column>
    <el-table-column
        prop="tbBorrow.createdTime"
        label="申请时间"
        align="center">
    </el-table-column>
    <el-table-column
        label="审批时间"
        align="center">
      <template slot-scope="scope">
        <div v-if="scope.row.tbBorrow.checkedTime === null" style="text-align: center;width: 100%">待审批</div>
        <div v-else style="text-align: center;width: 100%">{{scope.row.tbBorrow.checkedTime}}</div>
      </template>
    </el-table-column>
    <el-table-column
        label="馆内现存书籍编号"
        align="center">
      <template slot-scope="scope">
        <span v-for="(iem,index) in scope.row.bookIdentifierList" :key="index">
          <el-tag class="tag" @click="selectIdentifier(iem)">{{iem}}</el-tag>
        </span>
      </template>
    </el-table-column>
    <el-table-column
        fixed="right"
        label="操作">
      <template slot-scope="scope">
        <el-button
            @click="pass(scope.row.tbBorrow)"
            type="text"
            v-if="scope.row.tbBorrow.state === 0"
            size="medium">
          通过
        </el-button>
        <el-button
            @click="reject(scope.row.tbBorrow)"
            type="text"
            v-if="scope.row.tbBorrow.state === 0"
            size="medium">
          拒绝
        </el-button>
        <el-button
            @click="isReturnBook(scope.row.tbBorrow)"
            type="text"
            v-if="scope.row.tbBorrow.state === 1"
            size="medium">
          确认归还
        </el-button>
        <el-tag
            v-if="scope.row.tbBorrow.state === 2">
          已拒绝申请
        </el-tag>
        <el-tag
            v-if="scope.row.tbBorrow.state === 3">
          已归还
        </el-tag>
      </template>
    </el-table-column>
  </el-table>
  <!--分页功能-->
  <el-pagination
      style="padding-bottom: 15px"
      background
      layout="prev, pager, next"
      :page-size="pageSize"
      :total="borrowList.length"
      hide-on-single-page
      :current-page="currentPage"
      @current-change="getPageNum">
  </el-pagination>
</div>
</template>

<style scoped>
.tag {
  cursor: pointer;
  font-size: 15px;
  &:hover{
    color: deeppink;
  }
}
</style>
