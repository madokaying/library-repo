<template>
  <div class="book-wrapper">
    <div class="menu-wrapper">
      <!--
                  TODO 收藏，在线阅读，购买（电子，实体，借书），下载都放《作品信息》内
                  TODO 这个菜单按钮的跳转是锚点跳转，内容大体会在content部分一直连着
      -->
      <div class="menu">
        <div class="menu-item" @click="selectedBookInfo" :class="{'selectedItem':selectedItem === 1}">
          <span>作品信息</span>
        </div>
        <div class="menu-item" @click="selectedUserComments" :class="{'selectedItem':selectedItem === 2}">
          <span>用户评论</span>
        </div>
        <div class="menu-item" @click="selectedCatalog" :class="{'selectedItem':selectedItem === 3}">
          <span>目录</span>
        </div>
        <div class="menu-item" @click="goBack">
          <span>返回</span>
        </div>
      </div>
    </div>
    <div class="content">
      <div class="book-info-wrapper" ref="bookInfo">
        <div class="book-cover">
          <el-image
              :src="book.bookCover"
              fit="contain"
              lazy
          >
          </el-image>
        </div>
        <div class="book-info">
          书籍信息
        </div>
      </div>
      <div class="user-comments" ref="userComments">
        用户评论
      </div>
      <div
          class="catalog"
          ref="catalog"
          v-loading="loading.catalog">
        <div v-if="tableOfContents.length !== 0" style="overflow-y: auto;height: 600px">
          <el-row style="text-align: left;margin: 20px">
            <span style="font-size: 30px;margin-right: 20px"><b>目录</b></span>
            <span style="border-radius: 10px;background-color: #409EFF;color: white;padding: 5px 10px;font-size: 13px">共{{tableOfContents.length}}章</span>
          </el-row>
          <hr>
          <el-row>
            <el-col
                :xs="24"
                :sm="12"
                :md="8"
                :lg="6"
                :xl="4"
                :xxl="3"
                v-for="(tableOfContent, index) in tableOfContents"
                :key="index"
            >
              <div class="chapter">
                {{ tableOfContent.chapter }}
              </div>
            </el-col>
          </el-row>
        </div>
        <div v-else>
          <el-empty description="暂无目录信息" :image-size="300" style="height: 600px"></el-empty>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import http from '@/utils/http'
import _ from 'lodash';

export default {
  name: "BookId",
  props: {
    bookId: Object,
  },
  data() {
    return {
      book: Object,
      selectedItem: null,/*判断是否选中的flag*/
      tableOfContents: [],/*书籍的目录信息*/
      loading: {
        comments:true,
        catalog:true,
      },/*目录部分是否显示加载界面*/
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    selectedBookInfo() {
      // const desiredOffset = 0; // 回到元素顶端
      // window.scrollTo({
      //   top: desiredOffset,
      //   behavior: 'smooth'
      // });
      //由于设置了各content的高度，且顶部有一个fixed的顶栏，所以这里用center可以满足内容不被顶栏挡住的效果
      this.$refs.bookInfo.scrollIntoView({
        behavior: 'smooth',
        block: 'center'
      });
      this.selectedItemByScroll(); // 添加这一行以确保样式同步更新
    },
    selectedUserComments() {
      // const bookInfoRect = this.$refs.bookInfo.getBoundingClientRect();
      // const desiredOffset = bookInfoRect.height; // 移动到bookInfo底部
      // window.scrollTo({
      //   top: desiredOffset,
      //   behavior: 'smooth'
      // });
      this.$refs.userComments.scrollIntoView({
        behavior: 'smooth',
        block: 'center'
      });
      this.selectedItemByScroll(); // 添加这一行以确保样式同步更新
    },
    selectedCatalog() {
      // const bookInfoRect = this.$refs.bookInfo.getBoundingClientRect();
      // const userCommentsRect = this.$refs.userComments.getBoundingClientRect();
      // const desiredOffset = bookInfoRect.height + userCommentsRect.height; // 移动到userComments底部
      // window.scrollTo({
      //   top: desiredOffset,
      //   behavior: 'smooth'
      // });
      this.$refs.catalog.scrollIntoView({
        behavior: 'smooth',
        block: 'center'
      });
      this.selectedItemByScroll(); // 添加这一行以确保样式同步更新
    },
    selectedItemByScroll() {
      const scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
      const bookInfoRect = this.$refs.bookInfo.getBoundingClientRect();
      const userCommentsRect = this.$refs.userComments.getBoundingClientRect();
      const catalogRect = this.$refs.catalog.getBoundingClientRect();
      if (scrollTop >= 0 && scrollTop < bookInfoRect.height) {
        this.selectedItem = 1;
      } else if (scrollTop >= bookInfoRect.height && scrollTop < bookInfoRect.height + userCommentsRect.height) {
        this.selectedItem = 2;
      } else if (scrollTop >= bookInfoRect.height + userCommentsRect.height && scrollTop < bookInfoRect.height + userCommentsRect.height + catalogRect.height) {
        this.selectedItem = 3;
      }
    }
  },
  mounted() {
    /*访问后端数据库获取书籍的基本信息*/
    /*TODO 后续后端新写一个实体类，这里只访问一个端口，就返回所需的所有数据*/
    http.post('book/getBookDetailById?bookId=' + this.bookId).then(res => {
      if (res.data.code === 200) {
        this.book = res.data.data;
      } else {
        this.$message({
          message: res.data.msg,
          type: 'error',
          duration: '2000',
        })
      }
    })
    /*通过书籍id获取书籍的章节目录*/
    http.post('book/getBookTableOfContentsById?bookId=' + this.bookId).then(res => {
      if (res.data.code === 200) {
        this.tableOfContents = res.data.data;
      } else {
        this.$message({
          message: res.data.msg,
          type: 'error',
          duration: '2000',
        })
      }
      this.loading.catalog = false;
    })
    /*为滚动条添加监听器,使用节流处理，300ms触发一次，提升性能*/
    this.selectedItemByScroll = _.throttle(this.selectedItemByScroll, 300);
    window.addEventListener('scroll', this.selectedItemByScroll);
    /*退出组件时移除监听器*/
    this.$once('hook:beforeDestroy', () => {
      window.removeEventListener('scroll', this.selectedItemByScroll);
    })
  }
}
</script>

<style scoped>
.book-wrapper {
  display: flex;
  border-radius: 10px;
  overflow: hidden;
  margin: 6px;
}

.menu-wrapper {
  display: inline-block;
  width: 15%;
}

.menu {
  /*固定操作栏的位置*/
  position: fixed;
  /*宽度与menu-wrapper保持一直防止抖动，.menu 的 position 设置为 fixed 时，这个元素会脱离正常的文档流。
    这意味着 .menu 不再受其父元素 .menu-wrapper 宽度限制的影响。
    此宽度为除去aside的宽度（两个8%）再乘15%所得，确保与menu-wrapper实际宽度一致*/
  width: 12.6%;
}

.menu-item {
  color: rgba(0, 0, 0, .5);
  height: 50px;
  line-height: 50px;
  font-size: 20px;
  margin: 10px;
  border-radius: 10px;
  user-select: none !important;
  &:hover {
    cursor: pointer;
    background-color: rgba(0, 0, 0, .03);
  }
}

.selectedItem {
  border-left:2px solid #409EFF;
  border-right:2px solid #409EFF;
  color: #409EFF;
}

.book-wrapper .content {
  display: inline-block;
  width: 85%;
}

.book-info-wrapper {
  display: flex;
  border-radius: 10px;
  overflow: hidden;
  margin: 0 15px 10px 15px;
  background-color: white;
  padding: 20px;

}

.book-info-wrapper .book-cover {
  width: 20%;
}

.book-info-wrapper .book-info {
  width: 80%;
}

.user-comments {
  border-radius: 10px;
  overflow: hidden;
  margin: 0 15px 10px 15px;
  background-color: white;
  padding: 0;
  height: 600px;
}

.catalog {
  border-radius: 10px;
  overflow: hidden;
  margin: 0 15px 10px 15px;
  background-color: white;
  padding: 0;
}

.chapter {
  border-radius: 10px;
  &:hover {
    cursor: pointer;
    background-color: rgba(0, 0, 0, .03);
  }
}
</style>
