<template>
  <div>
    <el-backtop>
      <i class="el-icon-arrow-up"></i>
    </el-backtop>
    <home-view></home-view>
  </div>
</template>

<script>
import HomeView from "@/views/HomeView.vue";
export default {
  components: {
    HomeView
  },
  data() {
    return {}
  },
  methods: {},
}
</script>

<style>

body {
  font-family: "MaoKenWangXingYuan",serif;
  font-size: 18px;
  height: 100%;
  overflow: auto;
  /*设置margin为0可防止出现两个滚动条造成窗口抖动*/
  margin: 0;
}

.el-button,.el-input .el-input__inner,.el-input .el-input__placeholder ,.el-textarea__inner{
  font-family: "MaoKenWangXingYuan",serif;
  font-size: 16px;
}

/*
设置整个滚动条的样式
*/

/* 整个滚动条 */
::-webkit-scrollbar {
  width: 10px;
  height: 10px;
}

/* 滚动条上的滚动滑块 */
::-webkit-scrollbar-thumb {
  background-color: #49b1f5;
  /* 关键代码 */
  background-image: -webkit-linear-gradient(45deg,
  rgba(255, 255, 255, 0.4) 25%,
  transparent 25%,
  transparent 50%,
  rgba(255, 255, 255, 0.4) 50%,
  rgba(255, 255, 255, 0.4) 75%,
  transparent 75%,
  transparent);
  border-radius: 32px;
}

/* 滚动条轨道 */
::-webkit-scrollbar-track {
  background-color: #dbeffd;
  border-radius: 32px;
}
</style>
