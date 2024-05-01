<template>
    <div>
        <el-container class="fullContainer">
            <el-aside style="width: 400px">
                图书推荐
                <br>
                <img src="https://r2.touchgal.net/2023/01/cad0a147c5154256.jpg" alt="" width="300px">
                <br>
                简介部分
            </el-aside>
            <el-container>
                <el-header>图书馆登录界面</el-header>
                <el-main class="main">
                    <div class="login">
                        <el-form ref="form" :model="form" label-width="50px">
                            <el-form-item label="账号">
                                <el-input v-model="form.account"></el-input>
                            </el-form-item>
                            <el-form-item label="密码">
                                <el-input v-model="form.password"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="onSubmit">登录</el-button>
                                <el-button>注册</el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "LoginView",
        data() {
            return {
                form: {
                    account: '',
                    password: '',
                }
            }
        },
        methods: {
            onSubmit() {
                console.log('submit!');
            }
        }
    }
</script>

<style scoped>
    .el-header {
        background-color: #B3C0D1;
        color: #333;
        text-align: center;
        line-height: 60px;
    }

    .el-aside {
        background-color: #D3DCE6;
        color: #333;
        text-align: center;
        line-height: 200px;
    }

    .el-main {
        background-color: #E9EEF3;
        /*background-color: rgba(255,255,255,0.1);*/
        /*!* 背景模糊 *!*/
        /*backdrop-filter: blur(20px);*/
        color: #333;
        text-align: center;
        line-height: 160px;
    }

    .fullContainer{
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;

        min-height: 100vh;
        /*设置最小高度为窗口100%高度*/
        background-image: url("https://r2.touchgal.net/2023/01/cad0a147c5154256.jpg");
        background-size: 100% 100%;
        background-repeat: no-repeat;
    }

    .main {
        /* 弹性布局 水平垂直居中 */
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .login {
        height: 200px;
        width: 500px;
        padding: 20px;
    }
</style>