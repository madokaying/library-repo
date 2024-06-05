<script>
import http from '@/utils/http'
import BookCardView from "@/views/element_views/card/BookCardView.vue";
export default {
  name: "MyLibrary",
  components: {BookCardView},
  data(){
    return{
      myBorrowBookList:[],
      loading: true,
      pageSize: 9,
      currentPage:1,
    }
  },
  methods:{
    //获取借书申请表
    getMyBorrowList(){
      this.loading = true;
      const UID = JSON.parse(localStorage.getItem('userInfo')).UID;
      http.post(`/user/getMyBorrowList?userId=${UID}`).then(res => {
        if (res.data.code === 200){
          this.myBorrowBookList = res.data.data;
        } else {
          this.$message.error('获取我的借阅书籍失败')
        }
      })
      this.loading = false;
    },
    getPageNum(newPage) {
      this.currentPage = newPage;
    }
  },
  computed:{
    paginatedList() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      return this.myBorrowBookList.slice(startIndex, endIndex); // 根据当前页码和每页显示的条目数返回分页后的数据列表
    },
  },
  mounted() {
    this.getMyBorrowList();
  }
}
</script>

<template>
  <div
      v-loading="loading">
    <span>已借阅的书籍</span>
    <div v-if="myBorrowBookList.length !== 0">
      <el-row>
        <el-col
            :xs="24"
            :sm="12"
            :md="8"
            :lg="8"
            :xl="4"
            :xxl="3"
            v-for="(book,index) in paginatedList"
            :key="index">
          <book-card-view :book="book"></book-card-view>
        </el-col>
      </el-row>
    </div>
    <div v-else>
      <el-empty description="暂无借阅书籍" :image-size="300"></el-empty>
    </div>
    <div>
      <!--分页功能-->
      <el-pagination
          style="padding-bottom: 15px"
          background
          layout="prev, pager, next"
          :page-size="pageSize"
          :total="myBorrowBookList.length"
          hide-on-single-page
          :current-page="currentPage"
          @current-change="getPageNum">
      </el-pagination>
    </div>
  </div>
</template>

<style scoped>

</style>
