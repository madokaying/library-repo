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
      dialog:{
        updateDialog:false,
      },
      formLabelWidth: '80px',
      bookForm:{
        bookName:null,
        bookAuthor:null,
        bookCover:null,
        bookSummary:null,
        publisher:null,
        physicalBookPrice:null,
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
      // this.$refs[formName].validate((valid) => {
      //   if (valid) {
      //     // 提交表单逻辑，包括图片URL和其他表单项数据
      //     console.log(this.form);
      //     // 这里可以调用API发送数据到服务器
      //   } else {
      //     console.log('表单验证失败！');
      //     return false;
      //   }
      // });
      this.bookForm = book;
      this.dialog.updateDialog = true;
    },
    // 图片上传前的验证
    beforeCoverUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传书籍封面只能是 JPG 或 PNG 格式!');
        return false;
      }
      if (!isLt2M) {
        this.$message.error('上传书籍封面大小不能超过 2MB!');
        return false;
      }
      return true;
    },

    // 图片上传成功的处理
    handleCoverSuccess(response) {
      this.bookForm.bookCover = response.data.bookCover;
    },
  },
  mounted() {
    this.getBookList();
  }
}
</script>

<template>
  <div style="margin-right: 16px">
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
              class="cover-uploader"
              action="http://your-upload-api-url"
              :show-file-list="false"
              :on-success="handleCoverSuccess"
              :before-upload="beforeCoverUpload">
            <el-image v-if="bookForm.bookCover" :src="bookForm.bookCover" class="cover"></el-image>
            <i v-else class="el-icon-plus cover-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog.updateDialog = false">取 消</el-button>
        <el-button type="primary" @click="submitRegisterForm('updateForm')">提 交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.cover-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.cover-uploader .el-upload:hover {
  border-color: #409EFF;
}
.cover-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.cover {
  width: 178px;
  height: 210px;
  display: block;
}
</style>
