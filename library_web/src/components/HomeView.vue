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
            </el-footer>
        </el-container>
    </div>
</template>

<script>
    import HeaderMenu from "@/components/element_components/HeaderMenuView";

    export default {
        name: "HomeVue",
        components:{
            HeaderMenu,
        },
        data(){
            return{
                asideWidth:"150px"
            }
        },
        methods:{

        },
        mounted() {

        }
    }
</script>

<style scoped>
    .el-footer {
        background-color: white;
        color: #333;
        text-align: center;
        line-height: 100px;
    }

    .el-aside {
        background-color: transparent;
        color: #333;
        text-align: center;
        line-height: 200px;
    }

    .el-main {
        background-color: transparent;
        color: #333;
        text-align: center;
        line-height: 160px;
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
        background-image: url("https://cdn.jsdelivr.net/gh/madokaying/PicturesBed/GitImages/202404131540570.jpg");
        background-size: cover;
        background-repeat: no-repeat;
        background-attachment: fixed;
    }

    .set-background-gray {
        /*设置下层的元素的filter，效果是让背景变色*/
        /*backdrop-filter: grayscale(0.6); !* 应用滤镜效果 *!*/
        /*设置半透明罩层*/
        background-color: rgba(255,255,255,.85);
    }

</style>