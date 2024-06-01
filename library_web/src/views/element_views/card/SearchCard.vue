<template>
  <div class="search_card">
    <div class="search_div">
      <input class="input_search" v-model="searchName" type="text" name="password" @keydown.enter="search"
             @focus="isFocus = true" @blur="isFocus = false">
      <!--            当且仅当input出于focus或者searchName值非空时，给label和下面这个div添加动态效果-->
      <div :class="{'underline':isFocus||searchName}"></div>
      <label :class="{'active':isFocus||searchName}">书籍搜索</label>
      <el-button icon="el-icon-search" class="el_btn" @click="search"></el-button>
      <span style="position: absolute;top: 60px;left: 16px;font-size: 14px;color: deeppink;padding: 0">大家都在搜</span>
    </div>
    <div class="tag">
      <el-row>
        <el-col
            :span="8"
            v-for="(tag, index) in searchContents"
            :key="index"
        >
          <el-tag @click="searchByTag(tag.searchContent)">
            {{ tag.searchContent }}
          </el-tag>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import http from "@/utils/http";

export default {
  name: "SearchCard",
  data() {
    return {
      searchName: '',
      isFocus: false,
      tagNum:15,
      searchContents:[],
    }
  },
  methods: {
    search() {
      if (this.searchName !== '') {
        let url = `/book/getBooksList?bookName=${this.searchName}`;
        this.$store.dispatch('syncBookInfo', url);
      } else {
        this.$message('请输入有效字段');
      }
    },
    getSearchContent() {
      http.post(`/book/getSearchContent?num=${this.tagNum}`).then(res => {
        if(res.data.code === 200){
          this.searchContents = res.data.data;
        } else {
          this.$message.error('获取搜索内容失败');
        }
      })
    },
    searchByTag(tag){
      this.searchName = tag;
      this.search();
    },
  },
  mounted() {
    this.getSearchContent();
  }
}
</script>

<style scoped>

input {
  font-family: MaoKenWangXingYuan, sans-serif;
}

.search_card {
  height: 300px;
  border-radius: 20px;
  margin: 15px;
  padding: 0;
  background-color: #F2F6FC;
  overflow: hidden;
  box-shadow: 2px 2px 6px rgba(0, 0, 0, 0.5);
  line-height: 0;
}

.search_card .search_div {
  margin-top: 10px;
  position: relative;
  width: 100%;
  height: 40px;
}

.search_card .search_div .input_search {
  width: 90%;
  height: 20px;
  border: none;
  margin-top: 20px;
  outline: none;
  font-size: 15px;
  border-bottom: 2px solid #c0c0c0;
  background: none;
  color: #000000;
}

label.active {
  /* label上移，同时改变字号、颜色 */
  transform: translateY(-25px);
  font-size: 13px;
  color: hotpink !important;
}

.search_card .search_div label {
  position: absolute;
  bottom: 10px;
  left: 15px;
  color: #808080;
  /* 点击label可以穿透到输入框 */
  pointer-events: none;
  /* 给动画添加过渡，ease平滑过渡 */
  transition: all 0.3s ease;
}

.search_card .search_div div {
  position: absolute;
  left: 13px;
  bottom: -4px;
  height: 2px;
  width: 92%;
  background-color: hotpink;
  /* 沿x缩放为0*/
  transform: scaleX(0);
  /* 动画过渡 */
  transition: all 0.3s ease;
}

.search_card .search_div .underline {
  /* 沿X轴扩张至最大 */
  transform: scaleX(1) !important;
}

.tag {
  position: relative;
  height: 220px;
  top: 30px;
  overflow: hidden;
}

.el-tag {
  font-size: 18px;
  &:hover {
    cursor: pointer;
  }
}
</style>

<style>
.el_btn {
  position: absolute;
  top: 10px;
  right: 5px;
  color: #808080;
  border: none !important;
  outline: none !important;
  background-color: transparent !important;

}

.el_btn:hover,
.el_btn:active,
.el_btn:focus {
  background-color: transparent !important;
}
</style>
