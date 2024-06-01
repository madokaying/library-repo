<script>
import http from "@/utils/http";

export default {
  name: "RankingBook",
  props:{
    bookList:Object,
  },
  data(){
    return{

    }
  },
  methods:{
    toBookDetail(bookId){
      //前置判断是否已经登录
      http.post('/user/testToken').then(res => {
        if (res.data.code === 200) {
          this.$router.push({
            name: 'bookDetail',
            params: {bookId: bookId},
          });
        }
        else if (res.data.code === 401) {
          this.$message({
            message: res.data.msg,
            type: 'error',
            duration: '2000',
          })
        }
      })
    }
  },
}
</script>

<template>
<div>
  <el-row>
    <el-col
        :xs="24"
        :sm="12"
        :md="12"
        :lg="12"
        :xl="8"
        :xxl="4"
        v-for="(book,index) in this.bookList"
        :key="index">
      <div class="book-wrapper" @click="toBookDetail(book.bookId)">
        <div class="book-cover">
          <el-image :src="book.bookCover" fit="scale-down">
          </el-image>
        </div>
        <div class="book-info">
          <p class="book-name">{{book.bookName}}</p>
          <p class="book-author"><i class="el-icon-user-solid"></i>{{book.bookAuthor}}</p>
          <p class="book-summary">{{book.bookSummary}}</p>
        </div>
      </div>
    </el-col>
  </el-row>
</div>
</template>

<style scoped>
.book-wrapper {
  position: relative;
  height: 150px;
  overflow: hidden;
  border-radius: 10px;
  border-left: rgba(0, 0, 0, 0) 4px solid;
  &:hover {
    cursor: pointer;
    border-left: hotpink 4px solid;
  }
}

.book-cover {
  position: absolute;
  top: 10px;
  left: 10px;
  width: 100px;
  height: 140px;
}

.book-info {
  position: absolute;
  top: 10px;
  line-height: 20px;
}

.book-name {
  margin: 0 0 7px 110px;
  font-size: 20px;
}

.book-author {
  margin: 0 0 7px 110px;
  font-size: 16px;
  color: darkgray;
}

.book-summary {
  margin: 0 0 0 110px;
  height: 80px;
  overflow: hidden;
}
</style>
