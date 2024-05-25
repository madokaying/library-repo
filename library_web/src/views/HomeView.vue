<template>
  <div class="window">
    <el-container class="set-background-gray">
      <header-menu></header-menu>
      <el-container>
        <el-aside :width="asideWidth"></el-aside>
        <el-main>
          <router-view></router-view>
        </el-main>
        <el-aside :width="asideWidth"></el-aside>
      </el-container>
      <el-footer>
        <!--                TODO 页脚，目前考虑放管理员相关信息，比如电话邮箱之类的，方便用户咨询，后续有可以考虑参考其他网站放点别的链接-->
        友情链接
        <br>
        管理员联系方式：123@gmail.com
      </el-footer>
    </el-container>
  </div>
</template>

<script>
import HeaderMenu from "@/views/element_views/HeaderMenuView.vue";

export default {
  name: "HomeVue",
  components: {
    HeaderMenu,
  },
  data() {
    return {
      asideWidth: "8%",
    }
  },
  methods: {},
  mounted() {

  }
}
</script>

<style scoped>
.el-footer {
  background-color: white;
  color: #333;
  text-align: center;
  line-height: 25px;
}

.el-aside {
  background-color: transparent;
  color: #333;
  text-align: center;
  line-height: 200px;
}

@media (max-width: 767px) { /* 宽度低于767px时将el-aside宽度设置为0 */
  .el-aside {
    width: 0 !important;
    overflow: hidden; /* 防止内容溢出 */
  }
}

.el-main {
  background-color: transparent;
  color: #333;
  min-height: 90vh !important;
  text-align: center;
  line-height: 25px;
  padding: 0;
}

body > .el-container {
  margin-bottom: 40px;
}

.window {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;

  /*设置背景图片*/
  background-image: url('@/assets/images/20240413153917.jpg');
  background-size: cover;
  background-repeat: no-repeat;
  background-attachment: fixed;
}

.set-background-gray {
  /*设置下层的元素的filter，效果是让背景变色*/
  /*backdrop-filter: grayscale(0.6); !* 应用滤镜效果 *!*/
  /*设置半透明罩层*/
  background-color: rgba(255, 255, 255, .85);
}

</style>
