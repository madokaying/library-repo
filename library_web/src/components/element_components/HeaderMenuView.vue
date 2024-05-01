<template>
    <el-header style="background: rgba(255,255,255,1);padding: 0">
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal"
                 style="width: 100vw;background: rgba(255,255,255,.1);backdrop-filter: blur(20px);border-top: 1px solid rgba(255,255,255,.5);
                 z-index: 200;position: fixed;border-left: 1px solid rgba(255,255,255,0.5);">
            <el-menu-item index="0" @click="home" class="cancel-hover-underline">
                <el-image
                        style="height: 55px"
                        :src="url"
                        :fit="fit"></el-image></el-menu-item>
            <el-menu-item index="1" @click="toHome"><i class="el-icon-s-home"></i>首页</el-menu-item>
            <el-menu-item index="2">分类</el-menu-item>
            <el-menu-item index="3">论坛</el-menu-item>
        </el-menu>
    </el-header>
</template>

<script>
    export default {
        name: "HeaderMenu",
        data(){
            return{
                // url内放的是el-menu内第一个图片的地址，最好用来放logo
                url:'https://cdn.jsdelivr.net/gh/madokaying/PicturesBed/GitImages/202403191440918.png',
                url1:'https://cdn.jsdelivr.net/gh/madokaying/PicturesBed/GitImages/202403190911616.jpg',
                fit:'scale-down'
            }
        },
        methods:{
            toHome(){
                //重置书籍信息
                this.$store.dispatch('syncBookInfo','/book/getBooksList');
                this.$router.push({name:'mainBody',params:{units:'homePage'}});
            }
        }
    }
</script>

<style scoped>
    .el-header {
        background-color: white;
        color: #333;
        text-align: center;
        line-height: 60px;
    }

    .el-menu.el-menu--horizontal {
        border-bottom: solid 0 #e6e6e6;
    }

    .el-menu-item:hover,
    .el-menu-item.is-active,
    .el-menu-item:focus{
        /*设置字体和下划线的颜色*/
        color: hotpink !important;
        border-bottom-color: hotpink !important;
        /*设置透明*/
        background-color: rgba(255,255,255,0) !important;
        /*设置文字不能选中*/
        user-select: none !important;
    }

    .cancel-hover-underline:hover,
    .cancel-hover-underline.is-active,
    .cancel-hover-underline:focus {
        border-bottom-color: transparent !important;
    }
</style>
