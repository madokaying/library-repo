<template>
  <div class="book-wrapper">
    <div class="menu-wrapper">
      <div class="menu">
        <div v-if="isReadingMode">
          <div class="menu-item" @click="previousChapter">
            <span>上一章</span>
          </div>
          <div class="menu-item" @click="nextChapter">
            <span>下一章</span>
          </div>
          <div class="menu-item" @click="goBackToBookDetail">
            <span>返回</span>
          </div>
        </div>
        <div v-else>
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
    </div>
    <div class="content">
      <div
          v-if="isReadingMode"
          v-html="chapterContent"
          v-loading="loading.chapter"
          class="chapterContentWrapper"
      >
      </div>
      <div v-else>
        <div class="book-info-wrapper" ref="bookInfo" v-loading="loading.bookInfo">
          <div class="book-cover">
            <el-image
                :src="book.bookCover"
                fit="contain"
                lazy
            >
            </el-image>
          </div>
          <div class="book-info">
            <span style="font-size: 25px"><b>{{book.bookName}}</b></span>
            <p>
              <el-tag
                  type='success'
                  effect='plain'
                  class="el-author-tag">
                作者
              </el-tag>
              {{book.bookAuthor}}
            </p>
            <p>{{book.bookSummary}}</p>
            <p style="text-align: left;margin-left: 22px">
              热度:
              <span style="font-size: 20px;margin-right: 5px">{{borrowTimes}}</span><span style="font-size: 15px;color: gray;margin-right: 10px">借阅</span>
              <span style="font-size: 20px;margin-right: 5px">{{collectTimes}}</span><span style="font-size: 15px;color: gray;margin-right: 10px">收藏</span>
            </p>
            <p style="text-align: left;margin-left: 22px">
              标签:
              <span v-for="(tag,index) in tags" :key="index" style="border-radius: 10px;border: #49b1f5 solid 1px;color: #49b1f5;margin-right: 10px;padding: 0 10px;cursor: pointer">{{ tag }}</span>
            </p>
            <div>
              <!--操作按钮区域-->
              <el-button type="primary" plain @click="borrowBook">借书申请</el-button>
              <el-button type="primary" plain @click="buyBook">购买</el-button>
              <el-button type="primary" :icon="starIcon" @click="starBook" plain>
                <span v-if="isStarred">已收藏</span>
                <span v-else>收藏</span>
              </el-button>
              <el-button v-if="readingRecord !== null" @click="goChapter(readingRecord)" type="primary" plain>继续阅读</el-button>
            </div>
          </div>
        </div>
        <div ref="userComments">
          <comments-box :id="bookId" :type="1" :bookDetail="this"></comments-box>
        </div>
        <div
            class="catalog"
            ref="catalog"
            v-loading="loading.catalog">
          <div v-if="tableOfContents.length !== 0" style="overflow-y: auto;height: 600px">
            <el-row style="text-align: left;margin: 20px">
              <span style="font-size: 30px;margin-right: 20px"><b>目录</b></span>
              <span v-if="readingRecord !== null" style="border-left: #409EFF solid 4px;color: #409EFF;font-size: 18px">
                共{{tableOfContents.length}}章，上次阅读至**{{readingRecord.chapter}}**
              </span>
              <span v-else style="border-radius: 10px;background-color: #409EFF;color: white;padding: 5px 10px;font-size: 13px">共{{tableOfContents.length}}章，尚未开始阅读</span>
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
                <div class="chapter" @click="goChapter(tableOfContent)">
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
  </div>
</template>

<script>
import http from '@/utils/http'
import _ from 'lodash';
import CommentsBox from "@/components/CommentsBox.vue";

export default {
  name: "BookId",
  components:{
    CommentsBox,
  },
  props: {
    bookId: Object,
  },
  data() {
    return {
      book: Object,
      tags: [],
      borrowTimes: 0,/*借阅次数*/
      collectTimes: 0,/*收藏次数*/
      selectedItem: 1,/*判断是否选中的flag*/
      tableOfContents: [],/*书籍的目录信息*/
      loading: {
        bookInfo: true,
        comments: true,
        catalog: true,
        chapter: true,
      },/*目录部分是否显示加载界面*/
      isReadingMode: false,/*是否处于阅读模式*/
      chapterContent: null,/*章节内容*/
      maxRetryTime: 1,/*重试次数*/
      retryTime: 0,/*重试次数*/
      readingRecord: JSON.parse(localStorage.getItem(JSON.stringify(this.bookId))),/*阅读记录*/
      starIcon: 'el-icon-star-off',/*收藏图标*/
      isStarred: false,/*是否收藏*/
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    goBackToBookDetail() {
      //返回图书界面并重新添加对滚动条的监听事件
      this.selectedItemByScroll = _.throttle(this.selectedItemByScroll, 300);
      window.addEventListener('scroll', this.selectedItemByScroll);
      this.isReadingMode = false;
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
      const bookInfoRect = this.$refs.bookInfo.getBoundingClientRect();
      const fix = 10;
      const desiredOffset = bookInfoRect.height + fix; // 移动到bookInfo底部
      window.scrollTo({
        top: desiredOffset,
        behavior: 'smooth'
      });
      // this.$refs.userComments.scrollIntoView({
      //   behavior: 'smooth',
      //   block: 'center'
      // });
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
    /**
     * 根据滚动位置选择当前项。
     * 根据滚动条的位置，更新 selectedItem 的值
     */
    selectedItemByScroll() {
      // 获取当前文档的滚动位置
      const scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
      // 获取各个参考区域的位置信息（书信息、用户评论、目录）
      const bookInfoRect = this.$refs.bookInfo.getBoundingClientRect();
      const userCommentsRect = this.$refs.userComments.getBoundingClientRect();
      const catalogRect = this.$refs.catalog.getBoundingClientRect();

      // 根据滚动位置确定当前选中的项
      if (scrollTop >= 0 && scrollTop < bookInfoRect.height) {
        this.selectedItem = 1; // 选中书信息部分
      } else if (scrollTop >= bookInfoRect.height && scrollTop < bookInfoRect.height + userCommentsRect.height) {
        this.selectedItem = 2; // 选中用户评论部分
      } else if (scrollTop >= bookInfoRect.height + userCommentsRect.height && scrollTop < bookInfoRect.height + userCommentsRect.height + catalogRect.height) {
        this.selectedItem = 3; // 选中目录部分
      }
    },

    goChapter(tableOfContent) {
      let href = tableOfContent.href;
      //本地存储每本书的当前阅读章节和章节链接,记录上一次在本书中阅读到哪个章节
      localStorage.setItem(JSON.stringify(this.bookId), JSON.stringify(tableOfContent));
      //每次点击章节，都手动重置一次readingRecord的值
      this.readingRecord = JSON.parse(localStorage.getItem(JSON.stringify(this.bookId)));
      //存一下当前阅读章节的href，便于使用上一章和下一章的功能
      sessionStorage.setItem('currentChapterHref', href);
      //进入阅读模式，修改当前页面显示（By isReadingMode），并将获取到的章节内容用v-html显示出来,由于#会被认为是锚点链接，导致href传输不全，bookId直接没法传到后端，因此要把href中的#替换成%23
      //判断当前href内是否存在#，存在则替换成%23
      if (href.indexOf('#') !== -1) {
        href = href.replace(/#/g, '%23');
      }
      http.post(`book/getBookChapterByHref?href=${href}&bookId=${this.bookId}`).then(res => {
        if (res.data.code === 200) {
          this.chapterContent = res.data.data;
          //启动阅读模式,并且关掉对滚动条的监听
          window.removeEventListener('scroll', this.selectedItemByScroll);
          this.isReadingMode = true;
          const desiredOffset = 0; // 回到元素顶端
          window.scrollTo({
            top: desiredOffset,
            behavior: 'smooth'
          });
          this.loading.chapter = false;
        } else {
          this.$message({
            message: res.data.msg,
            type: 'error',
            duration: '2000',
          })
        }
      })
    },
    previousChapter() {
      this.loading.chapter = true;
      const currentHref = sessionStorage.getItem('currentChapterHref');
      let currentIndex = this.tableOfContents.findIndex(
          toc => toc.href === currentHref
      );
      // 如果找不到当前章节，可能是异步问题，等待一下再试
      if (currentIndex === -1) {
        //重试次数未达到最高值
        if(this.retryTime < this.maxRetryTime){
          this.retryTime += 1;
          setTimeout(() => {
            this.previousChapter();
          }, 100);
          return;
        }else {
          this.retryTime = 0;
          this.$message({
            message: '获取上一章失败，请检查网络连接或刷新页面后重试',
            type: 'error',
            duration: '2000',
          });
        }
      }

      if (currentIndex === 0) {
        this.$message({
          message: '已经是第一章了',
          type: 'warning',
          duration: '2000',
        });
      } else {
        this.goChapter(this.tableOfContents[currentIndex - 1]);
      }
    },
    nextChapter() {
      this.loading.chapter = true;
      const currentHref = sessionStorage.getItem('currentChapterHref');
      let currentIndex = this.tableOfContents.findIndex(
          toc => toc.href === currentHref
      );

      // 如果找不到当前章节，可能是异步问题，等待一下再试
      if (currentIndex === -1) {
        //重试次数未达到最高值
        if(this.retryTime < this.maxRetryTime){
          this.retryTime += 1;
          setTimeout(() => {
            this.nextChapter();
          }, 100);
          return;
        }else {
          this.retryTime = 0;
          this.$message({
            message: '获取下一章失败，请检查网络连接或刷新页面后重试',
            type: 'error',
            duration: '2000',
          });
        }
      }

      if (currentIndex === (this.tableOfContents.length - 1)) {
        this.$message({
          message: '已经是最后一章了',
          type: 'warning',
          duration: '2000',
        });
      } else {
        this.goChapter(this.tableOfContents[currentIndex + 1]);
      }
    },
    getBookDetailById(){
      http.post(`book/getBookDetailById?bookId=${this.bookId}`).then(res => {
        if (res.data.code === 200) {
          this.book = res.data.data;
        } else {
          this.$message({
            message: res.data.msg,
            type: 'error',
            duration: '2000',
          })
        }
        this.loading.bookInfo = false;
      })
    },
    getTableOfContentsById(){
      http.post(`book/getBookTableOfContentsById?bookId=${this.bookId}`).then(res => {
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
    },
    getTagsOfBook() {
      http.post(`book/getTagsOfBookById?bookId=${this.bookId}`).then(res => {
        if (res.data.code === 200){
          this.tags = res.data.data;
        } else {
          this.$message({
            message: res.data.msg,
            type: 'error',
            duration: '2000',
          })
        }
      })
    },
    //判断该书是否已经被此用户收藏
    judgeIsStar(){
      const UID = JSON.parse(localStorage.getItem('userInfo')).UID;
      http.post(`book/isCollectedBook?bookId=${this.bookId}&userId=${UID}`).then(res => {
        if (res.data.code === 200){
          this.isStarred = true;
          this.starIcon = 'el-icon-star-on';
        }
      })
    },
    //获取该书的收藏人数
    getCollectTimes(){
      http.post(`/book/getBookCollectedNumber?bookId=${this.bookId}`).then(res => {
        if(res.data.code === 200){
          this.collectTimes = res.data.data;
        } else {
          this.$message({
            message: '获取收藏数失败',
            type: 'error',
            duration: '2000',
          })
        }
      })
    },
    starBook(){
      const UID = JSON.parse(localStorage.getItem('userInfo')).UID;
      //如果手机已被收藏，点击则取消收藏，否则反之
      if (this.isStarred){
        http.post(`book/cancelCollectBook?bookId=${this.bookId}&userId=${UID}`).then(res =>{
          if (res.data.code === 200){
            this.isStarred = false;
            this.starIcon = 'el-icon-star-off';
            this.$message({
              message: '取消收藏成功',
              type: 'success',
              duration: '2000',
            })
          } else {
            this.$message({
              message: res.data.msg,
              type: 'error',
              duration: '2000',
            })
          }
        })
      } else {
        http.post(`book/collectBook?bookId=${this.bookId}&userId=${UID}`).then(res =>{
          if (res.data.code === 200){
            this.isStarred = true;
            this.starIcon = 'el-icon-star-on';
            this.$message({
              message: '收藏成功',
              type: 'success',
              duration: '2000',
            })
          } else {
            this.$message({
              message: res.data.msg,
              type: 'error',
              duration:'2000',
            })
          }
        })
      }
    },
    //借书申请功能
    borrowBook(){
      const role = JSON.parse(localStorage.getItem('userInfo')).role;
      const maxBorrow = JSON.parse(localStorage.getItem('userInfo')).maxBorrow;
      if (role === '未实名用户'){
        this.$message({
          message: '您还未实名认证，请先线下实名认证后申请',
          type: 'warning',
          duration: '2000',
        });
      } else if(maxBorrow < 1){
        this.$message({
          message: '您已达到最大借书数量，请先还书后申请',
          type: 'warning',
          duration: '2000',
        });
      }else {
        const UID = JSON.parse(localStorage.getItem('userInfo')).UID;
        http.post(`/user/judgeIsBorrowed?bookId=${this.bookId}&userId=${UID}`).then(res => {
          if(res.data.code === 200 || res.data.code === 201){
            http.post(`user/borrowBook?bookId=${this.bookId}&userId=${UID}`).then(res => {
              if (res.data.code === 200){
                this.$message({
                  message:'申请成功，请去我的书库确认申请进度',
                  type: 'success',
                  duration: '2000',
                })
              } else {
                this.$message({
                  message: res.data.msg,
                  type: 'error',
                  duration: '2000',
                })
              }
            })
          } else if (res.data.code === 401){
            this.$message({
              message: '您已经申请过该书且请求仍在审核，请勿重复申请',
              type: 'error',
              duration: '2000',
            })
          } else if (res.data.code === 402) {
            this.$message({
              message: '您已经借过该书，请先还书后申请',
              type: 'error',
              duration: '2000',
            })
          } else {
            this.$message({
              message: '未知状况，请联系管理员解决',
              type: 'error',
              duration: '2000',
            })
          }
        })
      }
    },
    buyBook(){
      this.$message({
        message: '代码还没写呢，别点了',
        type: 'error',
        duration: '2000',
      });
    }
  },
  onMounted() {
    const record = localStorage.getItem(JSON.stringify(this.bookId));
    if (record){
      try {
        this.readingRecord = JSON.parse(record);
      } catch (error) {
        console.error('Error parsing data:', error);
        this.readingRecord = null;
      }
    } else {
      console.info('No data found in localStorage');
      this.readingRecord = null;
    }
  },
  mounted() {
    //获取该书收藏人数
    this.getCollectTimes();
    //判断书籍是否已被该用户收藏
    this.judgeIsStar();
    /*访问后端数据库获取书籍的基本信息*/
    this.getBookDetailById();
    /*通过书籍id获取书籍的章节目录*/
    this.getTableOfContentsById();
    /*通过书籍id获取书籍标签信息*/
    this.getTagsOfBook();
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

/*.user-comments {
  border-radius: 10px;
  overflow: hidden;
  margin: 0 15px 10px 15px;
  background-color: white;
  padding: 0;
  height: 600px;
}*/

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

.chapterContentWrapper {
  border-radius: 10px;
  overflow: hidden;
  margin: 0 15px 10px 15px;
  background-color: white;
  padding: 20px;
}
</style>
