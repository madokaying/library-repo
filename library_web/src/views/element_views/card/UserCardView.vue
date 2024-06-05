<!--这是个用户卡片，内含背景，头像展示，以及登录、注册，进入个人主页的按钮-->
<template>
  <div
      class="user-card"
      v-loading="loading"
  >
    <div class="background">
      <img :src="userInfo.bgUrl" alt="" height="100%" width="100%">
    </div>
    <div class="avatar">
      <el-avatar :size="100" :fit="userInfo.fit" :src="userInfo.avatarUrl"></el-avatar>
    </div>
    <div class="user-content">
      <el-row style="top: 70px">
        <span style="font-size: 20px">{{ userInfo.nickname }}</span>
      </el-row>
      <el-row style="top: 100px">
        <el-button type="primary" round @click="dialog.loginDialog = true" v-show="button.loginButton">登录</el-button>
        <el-button round @click="dialog.registerDialog = true" v-show="button.registerButton">注册</el-button>
        <el-button type="primary" plain @click="myInfo" v-show="button.personalInformationButton"
                   icon="el-icon-s-custom">个人主页
        </el-button>
        <el-button type="info" plain @click="logout" v-show="button.logoutButton">登出</el-button>
      </el-row>
    </div>

    <!--显示登录弹窗-->
    <el-dialog
        center
        custom-class="cardDialog"
        :lock-scroll="false"
        title="登录"
        :visible.sync="dialog.loginDialog">
      <el-form :model="loginForm" status-icon :rules="rules" ref="loginForm">
        <el-form-item label="用户名" :label-width="formLabelWidth" prop="username" required>
          <el-input v-model="loginForm.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" :label-width="formLabelWidth" prop="password" required>
          <el-input v-model="loginForm.password" autocomplete="new-password" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog.loginDialog = false">取 消</el-button>
        <el-button type="primary" @click="submitLoginForm('loginForm')">确 定</el-button>
      </div>
    </el-dialog>

    <!--显示注册弹窗-->
    <!--        TODO 输入用户名时查询数据库是否已存在该用户，存在则不允许注册,添加检测规则，不能输入!空格等非法符号-->
    <el-dialog
        custom-class="cardDialog"
        :lock-scroll="false"
        title="注册"
        :visible.sync="dialog.registerDialog">
      <el-form :model="registerForm" status-icon :rules="rules" ref="registerForm">
        <el-form-item label="用户名" :label-width="formLabelWidth" prop="username" required>
          <el-input v-model="registerForm.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" :label-width="formLabelWidth" prop="password" required>
          <el-input v-model="registerForm.password" autocomplete="new-password" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码" :label-width="formLabelWidth" prop="checkPassword" required>
          <el-input v-model="registerForm.checkPassword" autocomplete="new-password" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog.registerDialog = false">取 消</el-button>
        <el-button type="primary" @click="submitRegisterForm('registerForm')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import http from '@/utils/http';
import userBackground from '@/assets/images/defaultBackground.jpg';
import userAvatar from '@/assets/images/defaultAvatar.jpg';

export default {
  name: "LoginMiniView",
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('用户名不能为空'));
      } else if (value.length < 9 || value.length > 15) {
        return callback(new Error('用户名长度应处于9~15之间'));
      } else {
        http.post(`/user/judgeUsernameExisted?username=${this.registerForm.username}`).then(res => {
          if (res.data.code !== 200) {
            return callback(new Error('用户名已存在'));
          } else {
            callback();
          }
        })
      }
    };
    const validatePassword = (rule, value, callback) => {
      // 定义一个正则表达式，用于匹配不被允许的字符
      const illegalCharsPattern = /[\s'/\\:;|<>[\]{}()]+/;
      if (value === '') {
        return callback(new Error('请输入密码'));
      } else if (value.length < 9 || value.length > 15) {
        return callback(new Error('密码长度应处于9~15之间'));
      } else if (illegalCharsPattern.test(value)) {
        // 如果密码中包含非法字符，则返回错误信息
        return callback(new Error('密码中包含不允许的字符'));
      } else {
        callback();
      }
    };
    const checkPassword = (rule, value, callback) => {
      if (value === '') {
        return callback(new Error('确认密码不能为空'));
      } else if (value !== this.registerForm.password) {
        return callback(new Error('密码不一致'));
      } else {
        callback();
      }
    };
    return {
      userInfo: {
        nickname: '请先登录',
        bgUrl: null,
        avatarUrl: null,
        fit: 'scale-down',
      },
      dialog: {
        loginDialog: false,
        registerDialog: false,
      },
      button: {
        loginButton: true,
        registerButton: true,
        personalInformationButton: false,
        logoutButton: false,
      },
      formLabelWidth: '80px',
      loginForm: {
        username: '',
        password: '',
      },
      registerForm: {
        username: '',
        password: '',
        checkPassword: '',
      },
      loading:true,
      rules: {
        username: [
          {validator: validateUsername, trigger: 'blur'}
        ],
        password: [
          {validator: validatePassword, trigger: 'blur'}
        ],
        checkPassword: [
          {validator: checkPassword, trigger: 'blur'}
        ]
      }
    }
  },

  methods: {
    submitLoginForm(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          http.post("/user/login", this.loginForm).then(res => {
            if (res.data.code === 200) {
              //获取并存入用户token和UID
              this.$store.commit('setTokenAndUID', res);
              http.post('/user/getUserInfoByUID?UID=' + JSON.parse(localStorage.getItem('userInfo')).UID).then(response => {
                if (response.data.code === 200) {
                  //存入剩余需要用到的信息
                  this.$store.commit('setUserInfo', response);
                  //同步localStorage数据到当前的data内
                  this.dataSync();
                  this.changeButton();
                  this.$message({
                    message: '欢迎回来,' + this.userInfo.nickname,
                    type: 'success',
                    duration: '2000',
                  });
                  //隐藏登录窗口
                  this.dialog.loginDialog = false;
                  //刷新页面，调度mounted让localStorage内的数据及时刷新到页面上
                  // location.reload()
                }
              })
            } else {
              this.$message.error('账号或密码不正确');
            }
            //清空输入框
            this.$refs[form].resetFields();
          })
        } else {
          this.$message.error('输入不合法，请重新输入');
          return false;
        }
      });

    },
    submitRegisterForm(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          http.post("/user/register", this.registerForm).then(res => {
            if (res.data.code === 200) {
              this.$message({
                message: '注册成功',
                type: 'success'
              });
              this.dialog.registerDialog = false;
            } else {
              this.$message.error('注册失败，若有疑问请联系管理员');
            }
            //清空输入框
            this.$refs[form].resetFields();
          })
        } else {
          this.$message.error('输入不合法，请重新输入');
          return false;
        }
      });
    },
    logout() {
      this.$confirm('真的要登出吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        lockScroll: false
      }).then(() => {
        http.post('/user/logout').then(res => {
          if (res.data.code === 200) {
            //清空localStorage中用户的信息
            localStorage.removeItem('userInfo');
            this.$message({
              message: '登出成功',
              type: 'success'
            });
            location.reload();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消登出'
        });
      });
    },
    myInfo() {
      this.$router.push({name: 'myInfo', params: {units: 'personalInfo'}})
    },
    //将用户的信息同步到data内
    dataSync() {
      this.userInfo.nickname = JSON.parse(localStorage.getItem('userInfo')).nickname;
      this.userInfo.avatarUrl = JSON.parse(localStorage.getItem('userInfo')).avatar;
      this.userInfo.bgUrl = JSON.parse(localStorage.getItem('userInfo')).background;
    },
    //切换按钮显示
    changeButton() {
      this.button = {
        loginButton: !this.button.loginButton,
        registerButton: !this.button.registerButton,
        personalInformationButton: !this.button.personalInformationButton,
        logoutButton: !this.button.logoutButton,
      };
    },
    testToken(){
      // localStorage.clear();
      /*让nickname显示正确的用户昵称,存在vuex内的数据会因为刷新而重置，
      localStorage,localStorage内的数据为永久储存
      sessionStorage，让数据只在单次会话内存在*/
      //查询localStorage内是否有用户信息
      if (localStorage.getItem('userInfo') != null) {
        //判断用户登录是否合法(主要判断token是否过期了)
        http.post('/user/testToken').then(res => {
          if (res.data.code === 200) {
            //同步用户信息到data
            this.dataSync();
            this.loading = false;
            //显示欢迎提示
            // this.$message({
            //   message: '欢迎回来,' + this.userInfo.nickname,
            //   type: 'success',
            //   duration: '2000',
            // })
            //切换按钮显示
            this.changeButton();
          }
          //若登录已过期或者token非法，则清空localStorage并给出提示
          else if (res.data.code === 401) {
            this.$message.error('登录非法或者已过期，请重新登录');
            localStorage.removeItem('userInfo');
            this.loading = false;
            location.reload();
          }
        })
      } else {
        this.userInfo.avatarUrl = userAvatar;
        this.userInfo.bgUrl = userBackground;
        this.loading = false;
      }
    },
  },
  mounted() {
    this.testToken();
  }
}
</script>

<style scoped>
.user-card {
  position: relative;
  height: 400px;
  border-radius: 20px;
  margin: 15px;
  background-color: #F2F6FC;
  /*将超出部分隐藏，让图片超出圆角的部分隐藏*/
  overflow: hidden;
  /* 阴影
   第一个参数是x轴阴影段长度
  第二个参数是y轴阴影段长度
  第三个参数是往四周阴影段长度
  第四个参数是阴影段颜色*/
  box-shadow: 2px 2px 6px rgba(0, 0, 0, 0.5);
}

.background {
  /*设置定位，方便设置z-index让头像部分能够显示于最上层*/
  position: absolute;
  width: 100%;
  height: 200px;
  overflow: hidden;
  z-index: 0;
}

.avatar {
  position: absolute;
  justify-content: center;
  top: 150px;
  width: 100%;
  height: 0;
  z-index: 2;
}

.user-content {
  position: absolute;
  width: 100%;
  height: 200px;
  z-index: 0;
  top: 200px;
}
</style>
<style>
/*设置登录/注册dialog的样式*/
.el-dialog.cardDialog {
  border-radius: 30px;
  line-height: 20px;
  width: 30%;
}

</style>
