<!--显示整体的框架-->
<template>
  <div
    v-loading="loading">
    <div v-if="this.$store.state.bookInfo.bookList.length !== 0">
      <el-row>
        <el-col
            :xs="24"
            :sm="12"
            :md="8"
            :lg="8"
            :xl="4"
            :xxl="3"
            v-for="(book,index) in this.$store.state.bookInfo.bookList"
            :key="index">
          <book-card-view :book="book"></book-card-view>
        </el-col>
      </el-row>
    </div>
    <div v-else>
      <el-empty description="暂无书籍" :image-size="300"></el-empty>
    </div>
    <div>
      <!--分页功能-->
      <el-pagination
          style="padding-bottom: 15px"
          background
          layout="prev, pager, next"
          :page-size="pageSize"
          :total="this.$store.state.bookInfo.total"
          hide-on-single-page
          :current-page="currentPage"
          @current-change="getPageNum">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import BookCardView from "@/views/element_views/card/BookCardView.vue";

export default {
  name: "ShowCardView",
  components: {
    BookCardView
  },
  data() {
    return {
      pageSize: 12,/*每页显示数据数*/
      currentPage: JSON.parse(sessionStorage.getItem('currentPage')) || 1, // 当前页码，初始
      loading:true,
    }
  },
  methods: {
    getPageNum(newPage) {
      sessionStorage.setItem('currentPage', newPage);
      let url = `/book/getBooksList?currentPage=${newPage}&pageSize=${this.pageSize}`;
      this.$store.dispatch('syncBookInfo', url);
      this.loading = false;
    },
  },
  mounted() {
    // 从 sessionStorage 获取上次的 currentPage 值，如果存在则应用
    const storedPage = JSON.parse(sessionStorage.getItem('currentPage'));
    if (storedPage) {
      this.currentPage = storedPage; // 设置当前页
      this.getPageNum(storedPage); // 根据存储的页码加载数据
    } else {
      // 如果没有存储的页码，则默认加载第一页数据
      this.getPageNum(this.currentPage);
    }
    this.loading = false;
  }
}
</script>

<style scoped>

</style>

<style>

</style>
