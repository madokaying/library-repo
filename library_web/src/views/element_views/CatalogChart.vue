<script>
  import http from "@/utils/http";
  import RankingBook from "@/components/RankingBook.vue";

  export default {
    name: "CatalogChart",
    components: {RankingBook},
    data(){
      return{
        tagList:[],
        bookList:[],
        currentPage:1,
        pageSize:9,
        isAllRanking:true,
        loading:true,
      }
    },
    methods:{
      //获取tag列表
      getTagList(){
        http.post('book/getTagList').then(res => {
          if (res.data.code === 200){
            this.tagList = res.data.data;
          }else {
            console.log(res.data.msg);
            this.$message.error('获取标签列表失败');
          }
          this.loading = false;
        })
      },
      getBookListByTag(tagId){
        this.loading = true;
        http.post(`/book/getBookListByTag?tagId=${tagId}&currentPage=${this.currentPage}&pageSize=${this.pageSize}`).then(res => {
          if (res.data.code === 200){
            this.bookList = res.data.data;
          }else {
            console.log(res.data.msg);
            this.$message.error('获取书籍列表失败');
          }
          this.loading = false;
          this.isAllRanking = false;
        })
      },
      toAllRanking(){
        this.loading = true;
        //获取根据收藏数量多少来排序的书籍列表
        http.post(`/book/getBookRankingList?currentPage=${this.currentPage}&pageSize=${this.pageSize}`).then(res => {
          if (res.data.code === 200){
            this.bookList = res.data.data;
          }else {
            console.log(res.data.msg);
            this.$message.error('获取书籍列表失败');
          }
        })
        this.loading = false;
        this.isAllRanking = true;
      },
    },
    mounted() {
      this.getTagList();
      this.toAllRanking();
    }
  }
</script>

<template>
<div>
  <el-row>
    <el-col :span="4">
      <div class="ranking">
        <el-tag type="success" style="width: 100%" @click="toAllRanking">
          总排行榜
        </el-tag>
      </div>
      <div class="tag">
        <el-row>
          <el-col :span="8"  v-for="(item,index) in tagList" :key="index">
            <el-tag type="info" style="width: 100%" @click="getBookListByTag(item.tagId)">
              {{item.tagName}}
            </el-tag>
          </el-col>
        </el-row>
      </div>
    </el-col>
    <el-col :span="20">
<!--      总排行部分-->
      <div
          v-if="isAllRanking"
          v-loading="loading">
        <div v-if="bookList.length === 0">
          <el-empty description="暂无书籍" :image-size="300"></el-empty>
        </div>
        <div v-else>
          <ranking-book :bookList="bookList"></ranking-book>
        </div>
      </div>
<!--分类排行部分-->
      <div
          v-else
          v-loading="loading">
        <div v-if="bookList.length === 0">
          <el-empty description="暂无相关书籍" :image-size="300"></el-empty>
        </div>
        <div v-else>
          <ranking-book :bookList="bookList"></ranking-book>
        </div>
      </div>
    </el-col>
  </el-row>
</div>
</template>

<style scoped>
.el-tag {
  font-size: 18px;
  margin: 5px 5px 0 0;
  cursor: pointer;
  &:hover, &:focus {
    color: #fff;
    background-color: #409EFF;
  }
}
</style>
