<script>
import http from '@/utils/http'

export default {
    name: "CommentsBox",
    props: {
      id: Object,/*书或者帖子的id*/
      type:Object,/*判断是书还是帖子：书为1，帖子为2*/
      bookDetail:Object,/*书籍详情组件，用于调用里面的方法在评论区跳页时重新回到评论区顶部*/
    },
    data(){
      return{
        commentList:[],/*评论列表*/
        pageSize:8,/*每页评论数*/
        currentPage:1,/*当前页数*/
        loading:true,/*是否显示加载画面*/
        /*isShowChildrenComments:false,/!*是否显示子评论*!/*/
        currentCommentId:null,/*当前评论id*/
        /*评论*/
        comment:{
          content:null,/*评论内容*/
          commentingTargetId:null,/*评论对象id*/
          commentingTarget:1,/*评论对象类型，1为书籍，2为帖子，3为评论  默认评论书籍*/
          userId:null,/*评论用户的id*/
        },
        placeholder:'快来留下您的精彩评论吧',/*评论框提示*/
        drawer:{
          size:80,
          isShow:false,/*评论框是否显示*/
          direction:'btt',/*评论框出现方向*/
        },
      }
    },
    methods:{
      changeCommentList(newPage){
        this.currentPage = newPage;
        this.bookDetail.selectedUserComments();
      },
      getCommentList(){
        this.loading = true;
        http.post(`/comment/getParentCommentList?commentTargetType=${this.type}&commentTargetId=${this.id}`).then(res => {
          if (res.data.code === 200) {
            this.commentList = res.data.data;
          } else {
            this.$message({
              message: res.data.msg,
              type: 'error',
              duration: '2000',
            })
          }
          this.loading = false;
        })
      },
      showChildrenComments(CommentId){
        this.currentCommentId = CommentId;
      },
      hideChildrenComments(){
        this.currentCommentId = null;
      },
      sendComment(){
        this.comment.userId = this.UID;
        http.post('/comment/addComment',this.comment).then(res => {
          if (res.data.code === 200) {
            this.$message({
              message: res.data.msg,
              type: 'success',
              duration: '1000',
            })
            this.getCommentList();
          } else {
            this.$message({
              message: res.data.msg,
              type: 'error',
              duration: '2000',
            })
          }
        })
      },
      putComment(){
        if (this.comment.content === null){
          this.$message({
            message: '评论内容不能为空',
            type: 'error',
            duration: '2000',
          })
        }else{
          this.loading = true;
          if (this.comment.commentingTarget === 1){
            this.comment.commentingTargetId = this.id;
          }
          this.sendComment();
          this.loading = false;
        }
      },
      reply(comment){
        this.comment.commentingTarget = 3;
        this.comment.commentingTargetId = comment.comment.commentId;
        this.placeholder = '回复:@'+comment.userNickname;
        this.drawer.isShow = true;
      },
      reset(){
        this.comment.commentingTarget = 1;
        this.comment.commentingTargetId = this.id;
        this.comment.content = null;
        this.placeholder = '快来留下您的精彩评论吧';
      },
    },
    computed: {
      UID(){
        return JSON.parse(localStorage.getItem('userInfo')).UID;
      },
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
        <span style="border-radius: 10px;background-color: #409EFF;color: white;padding: 5px 10px;font-size: 15px;margin-left: 20px">
          共{{totalCommentList}}条评论
        </span>
      </div>
      <div class="add-comment">
        <el-input :placeholder="placeholder" v-model="comment.content"  @keydown.enter.native="putComment">
          <el-button slot="prepend" @click="reset">重置</el-button>
          <el-button slot="append" icon="el-icon-s-promotion" @click="putComment" style="background-color: rgba(0,191,255,.2);color: black">发布</el-button>
        </el-input>
      </div>
      <div v-for="(comment,index) in paginatedList" :key="index" class="parent-comment">
        <hr>
<!--        一级评论区-->
        <div class="comment-user">
          <el-avatar :size="50" fit="scale-down" :src="comment.userAvatar" class="avatar"></el-avatar>
          <span class="nickname">{{comment.userNickname}}</span>
          <span class="comment-time">{{comment.comment.createdTime}}</span>
        </div>

        <div class="comment-content">
          <span>{{comment.comment.content}}</span>
        </div>

        <div style="display: flex">
          <div>
            <span style="color: hotpink;margin-left: 80px;cursor: pointer" @click="reply(comment)">回复</span>
          </div>
          <div v-if="comment.childrenCommentLength > 0" style="text-align: left">
            <span v-if="currentCommentId !== comment.comment.commentId" class="showChildrenCommentsMenu" @click="showChildrenComments(comment.comment.commentId)">查看全部{{comment.childrenCommentLength}}条评论></span>
            <span v-else class="showChildrenCommentsMenu" @click="hideChildrenComments">收起</span>
          </div>
        </div>

        <div v-if="currentCommentId === comment.comment.commentId" class="children-comments-wrapper">
<!--          子评论区-->
          <div class="children-comment" v-for="(childrenComment,index) in comment.childrenCommentList" :key="index">
            <div class="comment-user">
              <el-avatar :size="50" fit="scale-down" :src="childrenComment.userAvatar" class="avatar"></el-avatar>
              <span class="nickname">{{childrenComment.userNickname}}</span>
              <span class="comment-time">{{childrenComment.comment.createdTime}}</span>
            </div>

            <div class="comment-content">
              <span style="margin-left: -20px">
                <span v-if="childrenComment.commentTargetUserNickname != null">
                  <span style="margin-right: 10px">回复</span>
                  <span style="color: deeppink">@{{childrenComment.commentTargetUserNickname}}:</span>
                </span>
                {{childrenComment.comment.content}}
              </span>
              <div>
                <span style="color: hotpink;margin-left: -20px;cursor: pointer" @click="reply(childrenComment)">回复</span>
              </div>
              <hr>
            </div>
          </div>
        </div>
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

    <el-drawer
        :size="drawer.size"
        :visible.sync="drawer.isShow"
        :direction="drawer.direction"
        :lock-scroll="false"
        :with-header="false">
      <div class="add-comment" style="padding-top: 20px">
        <el-input :placeholder="placeholder" v-model="comment.content"  @keydown.enter.native="putComment">
          <el-button slot="prepend" @click="reset">重置</el-button>
          <el-button slot="append" icon="el-icon-s-promotion" @click="putComment" style="background-color: rgba(0,191,255,.2);color: black">发布</el-button>
        </el-input>
      </div>
    </el-drawer>

  </div>
</template>

<style scoped>

  hr {
    margin: 0;
  }

  .el-select .el-input {
    width: 130px;
  }
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
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
    word-break: break-word;
    margin: 0 20px 0 80px;
    font-size: 18px;
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
    //color: deepskyblue;
    color: hotpink;
    cursor: pointer;
    user-select: none;
  }

  .add-comment {
    padding: 5px;
    //height: 40px;
    //position: relative;
  }
</style>
