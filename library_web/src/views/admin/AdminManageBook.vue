<script>
import http from '@/utils/http'
export default {
  name: "AdminManageBookList",
  data() {
    return {
      bookList:[],
      total:0,
      loading: true,
      pageSize: 9,
      currentPage:1,
      search:'',
      select:'',
      selectFile:null,
      dialog:{
        updateDialog:false,
      },
      formLabelWidth: '80px',
      bookForm:{
        bookId:'',
        bookName:'',
        bookAuthor:'',
        bookCover:'',
        bookSummary:'',
        publisher:'',
        physicalBookPrice:'',
      },
    }
  },
  methods:{
    //获取书籍列表
    getBookList(){
      this.loading = true;
      http.get(`/book/getBooksList?currentPage=${this.currentPage}&pageSize=${this.pageSize}`).then(res => {
        if(res.data.code === 200){
          this.bookList = res.data.data.records;
          this.total = res.data.data.total;
        } else {
          this.$message.error('获取书籍列表失败');
        }
        this.loading = false;
      })
    },
    getPageNum(newPage){
      this.currentPage = newPage;
      this.getBookList();
    },
    withdraw(bookId){
      this.$confirm('确定要下架此书吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        lockScroll: false,
        type: 'warning'
      }).then(() => {
        http.delete(`/book/withdraw?bookId=${bookId}`).then(res => {
          if(res.data.code === 200){
            this.$message.success('下架成功');
            this.getBookList();
          } else {
            this.$message.error('权限不足，若为管理员，请重新登陆获取权限');
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    update(book){
      this.bookForm = book;
      this.dialog.updateDialog = true;
    },
    handleImageChange(file) {
      // if(file.size < 1 || file.name === ''){
      //   this.$message.error('请上传有效文件');
      //   return;
      // }
      this.selectFile = file.raw;
      //console.log(file.type);
      //file.type不能正确识别文件的类型(undefined)，原因暂时不明，出此下策,取.后面的后缀
      const fileName = file.name.toLowerCase();
      const isJPGByExtension = ['.jpg', '.jpeg'].includes(fileName.slice(fileName.lastIndexOf('.')));

      const isLt5M = file.size / 1024 / 1024 < 1;

      if (!isJPGByExtension) {
        this.$message.error('上传封面图片只能是 JPG 格式!');
        return;
      }
      if (!isLt5M) {
        this.$message.error('上传封面图片大小不能超过 5MB!');
        return;
      }
      this.bookForm.bookCover = URL.createObjectURL(file.raw);
    },
    updateRealBook(){

    },
    submitUpload(){
      let formData = new FormData();
      formData.append("file", this.selectFile);
      formData.append("bookId", this.bookForm.bookId);
      formData.append("bookName", this.bookForm.bookName);
      formData.append("bookAuthor", this.bookForm.bookAuthor);
      formData.append("bookSummary", this.bookForm.bookSummary);
      formData.append("publisher", this.bookForm.publisher);
      formData.append("physicalBookPrice", this.bookForm.physicalBookPrice);
      http.post('/book/updateBook',formData).then(res => {
       if (res.data.code === 200){
         this.$message.success('更新成功');
         this.dialog.updateDialog = false;
         this.getBookList();
       } else{
         this.$message.error('更新失败');
       }
      })
    }
  },
  mounted() {
    this.getBookList();
  }
}
</script>

<template>
  <div style="margin-right: 16px">
    <el-input placeholder="请输入查询内容" v-model="search" class="input-with-select" style="width: 90%">
      <el-select v-model="select" slot="prepend" placeholder="请选择" style="width: 100px">
        <el-option label="书名" value="1"></el-option>
        <el-option label="作者" value="2"></el-option>
        <el-option label="编号" value="3"></el-option>
        <el-option label="重置" value="4"></el-option>
      </el-select>
      <el-button slot="append" icon="el-icon-search"></el-button>
    </el-input>

    <el-button style="width: 10%" type="primary" plain>
      添加书籍
    </el-button>

    <el-table
        :data="bookList"
        :header-cell-style="{ 'text-align': 'center' }"
        style="width: 100%">
      <el-table-column
          fixed
          prop="bookId"
          label="书籍编号"
          align="center">
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
          prop="bookAuthor"
          label="作者"
          align="center">
      </el-table-column>
      <el-table-column
          prop="createTime"
          label="上架时间"
          align="center">
      </el-table-column>
      <el-table-column
          prop="borrowedTimes"
          label="借阅次数"
          align="center">
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作">
        <template slot-scope="scope">
          <el-button
              @click="withdraw(scope.row.bookId)"
              type="text"
              size="medium">
            下架
          </el-button>
          <el-button
              @click="update(scope.row)"
              type="text"
              size="medium">
            修改
          </el-button>
          <el-button
              @click="updateRealBook"
              type="text"
              size="medium">
            修改实际书籍
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

<!--    修改书籍的弹窗-->
    <el-dialog
        custom-class="cardDialog"
        :lock-scroll="false"
        title="书籍修改窗口"
        :visible.sync="dialog.updateDialog">
      <el-form :model="bookForm" status-icon ref="bookForm">
        <el-form-item label="书名" :label-width="formLabelWidth" prop="bookName" required>
          <el-input v-model="bookForm.bookName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="作者" :label-width="formLabelWidth" prop="bookAuthor" required>
          <el-input v-model="bookForm.bookAuthor" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="简介" :label-width="formLabelWidth" prop="bookSummary" required>
          <el-input
              v-model="bookForm.bookSummary"
              type="textarea"
              :rows="2"
              placeholder="请输入内容"
              autocomplete="off">
          </el-input>
        </el-form-item>
        <el-form-item label="出版社" :label-width="formLabelWidth" prop="publisher" required>
          <el-input v-model="bookForm.publisher" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="价格" :label-width="formLabelWidth" prop="physicalBookPrice" required>
          <el-input v-model="bookForm.physicalBookPrice" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="书籍封面">
          <el-upload
              ref="upload"
              :action="''"
              :auto-upload="false"
              :on-change="handleImageChange"
              :show-file-list="false"
              >
            <el-image
                style="width: 200px;height: 250px"
                fit="scale-down"
                :src="bookForm.bookCover"></el-image>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog.updateDialog = false">取 消</el-button>
        <el-button type="primary" @click="submitUpload">提 交<i class="el-icon-upload el-icon--right"></i></el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>

</style>
