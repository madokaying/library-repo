<script>
  import http from "@/utils/http";

  export default {
    name: "MyComments",
    data(){
      return{
        myComments:[],
        pageSize:8,
        currentPage:1,
        loading:true,
      }
    },
    methods:{
      //获取我的评论
      getMyComments(){
        this.loading = true;
        const myUID = JSON.parse(localStorage.getItem('userInfo')).UID;
        http.post(`/comment/getUserComments?UID=${myUID}`).then(res => {
          if (res.data.code === 200){
            this.myComments = res.data.data;
          } else {
            this.$message.error('获取评论失败');
          }
          this.loading = false;
        })
      },
      changeCommentList(newPage){
        this.currentPage = newPage;
      },
      deleteComment(commentId){
        this.$confirm('确认删除此评论吗?', '评论删除', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          http.post(`/comment/deleteComment?commentId=${commentId}`).then(res => {
            if (res.data.code === 200){
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              this.getMyComments();
            } else {
              this.$message.error('删除失败，请联系管理员解决');
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
    },
    computed:{
      paginatedList() {
        const startIndex = (this.currentPage - 1) * this.pageSize;
        const endIndex = startIndex + this.pageSize;
        return this.myComments.slice(startIndex, endIndex); // 根据当前页码和每页显示的条目数返回分页后的数据列表
      },
    },
    mounted() {
      this.getMyComments();
    }
  }
</script>

<template>
  <div class="my-comments-wrapper" v-loading="loading">
    <div v-if="myComments.length !== 0">
      <div v-for="(item, index) in paginatedList" :key="index" class="comment-item">
        <div class="me">
          <el-avatar :src="item.userAvatar" :size="50" fit="scale-down" class="avatar"></el-avatar>
          <span class="nickname">{{item.userNickname}}</span>
          <span class="time">{{item.comment.createdTime}}</span>
          <el-button type="danger" class="delete-button" plain @click="deleteComment(item.comment.commentId)">删除</el-button>
        </div>
        <div class="content">
          <span>{{item.comment.content}}</span>
        </div>
        <div class="target">
          <span style="margin-left: 80px">评论对象>>{{item.commentTargetUserNickname}}</span>
        </div>
      </div>
      <el-pagination
          style="padding-bottom: 15px"
          background
          layout="prev, pager, next"
          :page-size="pageSize"
          :total="myComments.length"
          hide-on-single-page
          :current-page="currentPage"
          @current-change="changeCommentList">
      </el-pagination>
    </div>
    <div v-else>
      <el-empty description="您还没有评论过哦" :image-size="300"></el-empty>
    </div>
  </div>
</template>

<style scoped>
.my-comments-wrapper {
  position: relative;
  border-radius: 10px;
  overflow: hidden;
  background-color: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(10px);
  padding-top: 10px;
}

.comment-item {
  border-radius: 10px;
  overflow: hidden;
  margin: 0 10px 10px 10px;
  background-color: white;
  padding: 0;
}

.me {
  position: relative;
  min-height: 70px;
}

.avatar {
  position: absolute;
  left: 20px;
  top: 20px;
}

.nickname {
  position: absolute;
  font-size: 18px;
  left: 80px;
  top: 20px;
}

.time {
  position: absolute;
  font-size: 15px;
  color: #ff5c5c;
  left: 80px;
  top: 50px;
}

.content {
  text-align: left;
  margin: 0 20px 0 80px;
  word-break: break-word;
}

.target {
  text-align: left;
  color: #409EFF;
}

.delete-button {
  position: absolute !important;
  left: 20px !important;
  top: 78px !important;
  width: 50px;
  padding: 5px;
}
</style>
