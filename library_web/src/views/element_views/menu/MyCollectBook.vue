<script>
import http from "@/utils/http";
import BookCardView from "@/views/element_views/card/BookCardView.vue";

export default {
  name: "MyCollectBook",
  components: {BookCardView},
  data(){
    return{
      myCollectBooks:[],
      loading: true,
      pageSize: 9,
      currentPage:1,
    }
  },
  methods:{
    getMyCollectBooks(){
      const UID = JSON.parse(localStorage.getItem('userInfo')).UID;
      http.post(`book/getMyCollectBooks?userId=${UID}`).then(res => {
        if (res.data.code === 200){
          this.myCollectBooks = res.data.data;
          this.loading = false;
        } else {
          this.$message.error('获取收藏书籍失败，请联系管理员');
        }
      })
    },
    getPageNum(newPage) {
      this.currentPage = newPage;
    }
  },
  computed:{
    paginatedList() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      return this.myCollectBooks.slice(startIndex, endIndex); // 根据当前页码和每页显示的条目数返回分页后的数据列表
    },
  },
  mounted() {
    this.getMyCollectBooks();
  }
}
</script>

<template>
  <div
      v-loading="loading">
    <div v-if="myCollectBooks.length !== 0">
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
      <el-empty description="暂无收藏书籍,快去收藏试试看吧" :image-size="300"></el-empty>
    </div>
    <div>
      <!--分页功能-->
      <el-pagination
          style="padding-bottom: 15px"
          background
          layout="prev, pager, next"
          :page-size="pageSize"
          :total="myCollectBooks.length"
          hide-on-single-page
          :current-page="currentPage"
          @current-change="getPageNum">
      </el-pagination>
    </div>
  </div>
</template>

<style scoped>

</style>
