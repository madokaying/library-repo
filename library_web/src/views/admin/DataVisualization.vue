<script>
import http from '@/utils/http'
  export default {
    name: "DataVisualization",
    data(){
      return{
        //只显示前12的书籍的列表
        bookList:[],
        totalBorrowedTimes:0,
        currentPage:1,
        pageSize:12,
      }
    },
    methods:{
      getBookList(){
        http.post(`/book/getBookRankingList?currentPage=${this.currentPage}&pageSize=${this.pageSize}`).then(res => {
          if (res.data.code === 200){
            this.bookList = res.data.data;
            this.setTotalBorrowedTimes();
          } else {
            this.$message.error('获取书籍列表失败');
          }
        })
      },
      setTotalBorrowedTimes(){
        this.bookList.forEach(book => {
          this.totalBorrowedTimes += book.borrowedTimes;
        });
        if (this.totalBorrowedTimes !== 0){
          // 遍历列表并计算百分比
          this.bookList.forEach(book => {
            book.percent = (book.borrowedTimes / this.totalBorrowedTimes) * 100; // 添加percent属性
          });
        } else {
          // 遍历列表并计算百分比
          this.bookList.forEach(book => {
            book.percent = 0; // 添加percent属性
          })
        }
      },
    },
    mounted() {
      this.getBookList();
    }
  }
</script>

<template>
<div style="margin-right: 16px">
  <div v-for="(book,index) in bookList" :key="index">
    <span>{{book.bookName}}:{{book.borrowedTimes}}(借阅次数)</span>
    <el-progress :text-inside="true" :stroke-width="26" :percentage="book.percent"></el-progress>
  </div>
</div>
</template>

<style scoped>

</style>
