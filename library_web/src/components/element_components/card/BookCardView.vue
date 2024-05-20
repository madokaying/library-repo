<!--书本的展示卡片，用于显示书本信息-->
<template>
  <div class="book-card" @mouseover="changeImgSize = true" @mouseleave="changeImgSize = false" @click="bookDetail">
    <div class="cover">
      <el-image :src="book.bookCover" alt="封面" fit="scale-down" class="img" :class="{'changeSize':changeImgSize}" lazy>
      </el-image>
    </div>
    <div class="book-information">
      <h2 style="width: 100%">{{ book.bookName }}</h2>
      <p>
        <el-tag
            type='success'
            effect='plain'
            class="el-author-tag">
          作者
        </el-tag>
        {{ book.bookAuthor }}
      </p>
    </div>
  </div>
</template>

<script>
import http from "@/utils/http";

export default {
  name: "BookCardView",
  //props用于接收父vue传来的值
  props: {
    book: Object
  },
  data() {
    return {
      changeImgSize: false,
    }
  },
  methods: {
    bookDetail() {
      //前置判断是否已经登录
      http.post('/user/testToken').then(res => {
        if (res.data.code === 200) {
          this.$router.push({
            name: 'bookDetail',
            params: {bookId: this.book.bookId},
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
    },
  }
}
</script>

<style scoped>
.book-card {
  height: 300px;
  border-radius: 10px;
  overflow: hidden;
  margin: 6px;
  background-color: #F2F6FC;
  &:hover {
    color: #ff90aa;
    /*鼠标移入使元素上移并显示阴影*/
    transform: translateY(-5px);
    /* 添加过渡效果 */
    transition: all 0.5s ease;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  }
}

.cover {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
  /*height: 70%;*/
}

.book-information {
  position: relative;
  align-content: center;
  height: 100px;
  /*将行高设置为0，否则文字会溢出*/
  line-height: 0;
  justify-content: center;
  align-items: center;
  /*允许flex盒子内部的元素换行，有时需要在需要换行的元素那设置width:100%才能达成<br/>的效果*/
  flex-wrap: wrap;
}

</style>

<style>
.el-author-tag {
  background-color: transparent !important;
}

.img {
  width: 100%;
  height: 100%;
  transition: all 0.5s ease;
}

.changeSize {
  width: 102%;
  height: 102%;
}
</style>
