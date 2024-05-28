<script>
import http from '@/utils/http'
  export default {
    name: "CommentsBox",
    props: {
      bookId: Object,
      type:Object,
    },
    data(){
      return{
        commentList:[],/*评论列表*/
        pageSize:8,/*每页评论数*/
        currentPage:1,/*当前页数*/
        loading:true,/*是否显示加载画面*/
        isShowChildrenComments:false,/*是否显示子评论*/
        currentCommentId:null,/*当前评论id*/
        /*评论*/
        comment:{
          content:null,/*评论内容*/
          commentingTargetId:null,/*评论对象*/
          commentingTarget:null,/*评论对象类型，1为书籍，2为帖子，3为评论*/
          userId:null,
        },
      }
    },
    methods:{
      changeCommentList(newPage){
        this.currentPage = newPage;
      },
      getCommentList(){
        http.post(`/comment/getParentCommentList?commentTargetType=${this.type}&commentTargetId=${this.bookId}`).then(res => {
          if (res.data.code === 200) {
            this.commentList = res.data.data;
            this.loading = false;
          } else {
            this.$message({
              message: res.data.msg,
              type: 'error',
              duration: '2000',
            })
          }
        })
      },
      showChildrenComments(CommentId){
        this.currentCommentId = CommentId;
        this.isShowChildrenComments = true;
      },
      hideChildrenComments(){
        this.currentCommentId = null;
        this.isShowChildrenComments = false;
      },
      putComment(){

      },
    },
    computed: {
      paginatedList() {
        const startIndex = (this.currentPage - 1) * this.pageSize;
        const endIndex = startIndex + this.pageSize;
        return this.commentList.slice(startIndex, endIndex); // 根据当前页码和每页显示的条目数返回分页后的数据列表
      },
      totalCommentList(){
        let total = 0;
        for(let i = 0;i<this.commentList.length;i++){
          total += this.commentList[i].childrenCommentLength;
        }
        total += this.commentList.length;
        return total;
      },
    },
    mounted() {
      this.getCommentList();
    }
  }
</script>

<template>
  <div class="comment-wrapper" v-loading="loading">
    <div v-if="commentList.length !== 0">
      <div class="comment-title">
        <b>评论区</b>
        <span style="border-radius: 10px;background-color: #409EFF;color: white;padding: 5px 10px;font-size: 13px;margin-left: 20px">
          共{{totalCommentList}}条评论
        </span>
      </div>
      <hr>
      <div v-for="(comment,index) in paginatedList" :key="index" class="parent-comment">
        <div class="comment-user">
          <el-avatar :size="50" fit="scale-down" :src="comment.userAvatar" class="avatar"></el-avatar>
          <span class="nickname">{{comment.userNickname}}</span>
          <span class="comment-time">{{comment.comment.createdTime}}</span>
        </div>

        <div class="comment-content">
          <span style="padding-left: 80px">{{comment.comment.content}}</span>
        </div>

        <div style="display: flex">
          <div>
            <span style="color: deepskyblue;margin-left: 80px;cursor: pointer">回复</span>
          </div>
          <div v-if="comment.childrenCommentLength > 0" style="text-align: left">
            <span v-if="!isShowChildrenComments" class="showChildrenCommentsMenu" @click="showChildrenComments(comment.comment.commentId)">查看全部{{comment.childrenCommentLength}}条评论></span>
            <span v-if="isShowChildrenComments" class="showChildrenCommentsMenu" @click="hideChildrenComments">收起</span>
          </div>
        </div>

        <div v-if="currentCommentId === comment.comment.commentId" class="children-comments-wrapper">
          <div class="children-comment" v-for="(childrenComment,index) in comment.childrenCommentList" :key="index">
            <div class="comment-user">
              <el-avatar :size="50" fit="scale-down" :src="childrenComment.userAvatar" class="avatar"></el-avatar>
              <span class="nickname">{{childrenComment.userNickname}}</span>
              <span class="comment-time">{{childrenComment.comment.createdTime}}</span>
            </div>

            <div class="comment-content">
              <span style="padding-left: 60px">
                <span v-if="childrenComment.commentTargetUserNickname != null">
                  <span style="margin-right: 10px">回复</span>
                  <span style="color: deepskyblue">@{{childrenComment.commentTargetUserNickname}}:</span>
                </span>
                {{childrenComment.comment.content}}
              </span>
              <hr>
            </div>
          </div>
        </div>
        <hr>
      </div>
      <el-pagination
          style="padding-bottom: 15px"
          background
          layout="prev, pager, next"
          :page-size="pageSize"
          :total="commentList.length"
          hide-on-single-page
          :current-page="currentPage"
          @current-change="changeCommentList">
      </el-pagination>
    </div>
    <div v-else>
      <el-empty description="暂无评论" :image-size="300"></el-empty>
    </div>
    <div class="add-comment">
      <el-input v-model="comment.content" placeholder="请输入评论" style="width: 90%;position:absolute;left: 20px"></el-input>
      <el-button type="primary" icon="el-icon-s-promotion" style="position: absolute;right: 20px;width: 10%" @click="putComment">发布</el-button>
    </div>
  </div>
</template>

<style scoped>

  hr {
    margin: 0;
  }

  .comment-wrapper {
    position: relative;
    border-radius: 10px;
    overflow: hidden;
    margin: 0 15px 10px 15px;
    background-color: white;
    padding: 0;
  }

  .comment-title {
    text-align: left;
    font-size: 30px;
    margin: 20px;
  }

  .comment-user {
    min-height: 70px;
  }

  .avatar {
    position: absolute;
    margin-top: 20px;
    left: 20px;
  }

  .nickname {
    position: absolute;
    margin-top: 27px;
    left: 80px;
    font-size: 20px;
  }

  .comment-time {
    position: absolute;
    margin-top: 55px;
    left: 80px;
    font-size: 15px;
    color: lightgrey;
  }

  .comment-content {
    text-align: left;
    font-size: 15px;
    padding: 10px 20px 0 0;
  }

  .children-comments-wrapper {
    position: relative;
    border-radius: 10px;
    margin: 0 20px 20px 80px;
    padding: 0 0 0 20px;
    background-color: rgba(211,211,211,.15);
  }

  .showChildrenCommentsMenu {
    padding-left: 20px;
    color: deepskyblue;
    cursor: pointer;
    user-select: none;
  }

  .add-comment {
    padding: 20px;
    height: 40px;
    position: relative;
  }
</style>
