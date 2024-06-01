<script>
import http from '@/utils/http'
export default {
  name: "AdminManageBorrowList",
  data() {
    return {
      currentPage:1,
      pageSize:9,
      borrowList:[],
    }
  },
  methods: {
    //获取到所有的借书申请
    getBorrowList(){
      http.post(`admin/getBorrowList?currentPage=${this.currentPage}&pageSize=${this.pageSize}`).then(res => {
        if (res.data.code === 200){
          this.borrowList = res.data.data;
        } else {
          this.$message.error(res.data.msg)
        }
      })
    }
  },
  mounted() {
    this.getBorrowList();
  },
}
</script>

<template>
<div>
  <div
      v-for="(item,index) in borrowList"
      :key="index">
    <p>{{item.userNickname}}于{{item.tbBorrow.createdTime}}申请借《{{item.bookName}}》</p>
  </div>
</div>
</template>

<style scoped>

</style>
