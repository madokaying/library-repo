<template>
  <div>
    <div class="my-pic-info">
      <div class="my-background">
        <el-image :src="userInfo.background" alt="" style="height: 100%;width: 100%">
          <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline"></i>
          </div>
        </el-image>
        <!--                修改背景-->
        <el-popover
            placement="top-end"
            width="100px"
            popper-class="my-custom-popover"
            trigger="click"
        >
          <div @click="setBackgroundData" class="keep-mouse-style">修改壁纸</div>
          <div class="background-for-more" slot="reference">
            <i class="el-icon-more" style="font-size: 25px"></i>
          </div>
        </el-popover>

      </div>
      <div class="my-avatar" @click="setAvatarData" @mouseover="changeAvatarTips = true"
           @mouseleave="changeAvatarTips = false">
        <el-image :src="userInfo.avatar" alt="" style="height: 140px;width: 140px;border-radius: 10px" fit="cover">
          <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline"></i>
          </div>
        </el-image>
        <div class="show-change-avatar-tips" :class="{'up-after-show':changeAvatarTips}">
          <div>
            <div style="margin-bottom: 10px">
              <i class="el-icon-camera" style="font-size: 20px"></i>
            </div>
            <span>修改头像</span>
          </div>
        </div>
      </div>
      <div class="my-info">
        <span class="my-nickname"><strong>{{ userInfo.nickname }}</strong></span>
        <span class="my-role" v-if="userInfo.role === '管理员'"
              style="background-color: #ff5700;color: white;border-radius: 6px;padding: 1px"> {{ userInfo.role }}<i
            class="el-icon-check"></i></span>
        <span class="my-role" v-else-if="userInfo.role === '实名用户'"
              style="background-color: #2eff1d;color: white;border-radius: 6px;padding: 1px"> {{ userInfo.role }}<i
            class="el-icon-circle-check"></i></span>
        <span class="my-role" v-else-if="userInfo.role === '未实名用户'"
              style="background-color: #ff5c5c;color: white;border-radius: 6px;padding: 1px"> {{ userInfo.role }}<i
            class="el-icon-circle-close"></i></span>
        <span class="my-signature"><i class="el-icon-user-solid"
                                      style="font-size: 17px"></i>{{ userInfo.signature }}</span>
        <!--                我的消息-->
        <div class="my-message" @click="showMyMessage">
          <i class="el-icon-bell" style="font-size: 25px"></i>
        </div>
      </div>
    </div>
    <!--        TODO 可操作处的展示,把我的个人信息，我的书库，我的借阅记录，我的评论，已借书籍，借阅申请等功能加入到这里-->
    <div>
      <el-row>
        <!--        TODO 用户可选选项-->
        <el-col :span="6">
          <!--                    通用部分-->
          <div class="common-data">
            <div style="line-height: 18px">
              <table style="width: 100%">
                <tr style="height: 50px;font-size: 30px">
                  <td>0</td>
                  <td>0</td>
                  <td>0</td>
                  <td>0</td>
                </tr>
                <tr style="height: 20px">
                  <td>评论</td>
                  <td>已购</td>
                  <td>帖子</td>
                  <td>收藏</td>
                </tr>
              </table>
            </div>
          </div>
          <!--                    基础菜单-->
          <div class="basic-menu-header">功能菜单</div>
          <div class="basic-menu">
            <el-row>
              <el-col :span="6">
                <div class="basic-menu-item" @click="toPersonalInfo">
                  <div>
                    <svg class="icon" aria-hidden="true">
                      <use xlink:href="#icon-user"/>
                    </svg>
                  </div>
                  <span>个人资料</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="basic-menu-item" @click="toMyMessage">
                  <div>
                    <svg class="icon" aria-hidden="true">
                      <use xlink:href="#icon-notice"/>
                    </svg>
                  </div>
                  <span>我的消息</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="basic-menu-item" @click="toMyComments">
                  <div>
                    <svg class="icon" aria-hidden="true">
                      <use xlink:href="#icon-comment"/>
                    </svg>
                  </div>
                  <span>我的评论</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="basic-menu-item" @click="toMyBookCollection">
                  <div>
                    <svg class="icon" aria-hidden="true">
                      <use xlink:href="#icon-book-collection"/>
                    </svg>
                  </div>
                  <span>我的书库</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="basic-menu-item" @click="toMyPosts">
                  <div>
                    <svg class="icon" aria-hidden="true">
                      <use xlink:href="#icon-posts"/>
                    </svg>
                  </div>
                  <span>我的帖子</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="basic-menu-item" @click="toMyCollection">
                  <div>
                    <svg class="icon" aria-hidden="true">
                      <use xlink:href="#icon-collection1"/>
                    </svg>
                  </div>
                  <span>我的收藏</span>
                </div>
              </el-col>
            </el-row>

          </div>
          <!--                    TODO 细分部分-->
          <div v-if="userInfo.role === '管理员'">

          </div>
          <div v-if="userInfo.role === '未实名用户' || userInfo.role === '实名用户'">
            <!--                 由于目前角色结构简单，基本除了用户就是管理员，还未添加别的角色，因此角色这栏预计先空着，无特殊功能，用户的操作菜单全在通用部分-->
          </div>
        </el-col>
        <!--        TODO 选项所展示内容-->
        <el-col :span="18">
          <!--                    默认展示个人的信息，表现为form表单的信息，实现展示信息的同时方便在已有信息基础上修改-->
          <div v-if="units === 'personalInfo'">
            <personal-info></personal-info>
          </div>
        </el-col>
      </el-row>
    </div>

    <!--显示头像上传弹窗-->
    <el-dialog
        custom-class="cardDialog"
        :lock-scroll="false"
        :title="dialog.title"
        :visible.sync="dialog.uploadDialog">
      <vue-cropper
          ref="cropper"
          :img="options.img"
          :info="true"
          :autoCrop="options.autoCrop"
          :autoCropWidth="options.autoCropWidth"
          :autoCropHeight="options.autoCropHeight"
          :fixedBox="options.fixedBox"
          :view-mode="1"
          style="width: 100%;height: 300px"
      />
      <el-upload
          class="upload-demo"
          ref="upload"
          action="#"
          :http-request="requestUpload"
          :on-success="handleSuccess"
          :show-file-list="false"
          :before-upload="beforeUpload"
      >
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">
          上传到服务器
          <i class="el-icon-upload el-icon--right"></i>
        </el-button>
        <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过20MB</div>
      </el-upload>
    </el-dialog>
  </div>
</template>

<script>
import http from "@/utils/http";
import {VueCropper} from 'vue-cropper'
import PersonalInfo from "@/components/element_components/menu/PersonalInfo";
import {getAndSyncUserInfo} from '@/utils/getAndSyncUserInfo'

export default {
  name: "MyInfo",
  props: [
    'units',
  ],
  components: {
    VueCropper,
    PersonalInfo,
  },
  data() {
    return {
      userInfo: {
        nickname: JSON.parse(localStorage.getItem('userInfo')).nickname,
        background: JSON.parse(localStorage.getItem('userInfo')).background,
        avatar: JSON.parse(localStorage.getItem('userInfo')).avatar,
        signature: JSON.parse(localStorage.getItem('userInfo')).signature,
        email: JSON.parse(localStorage.getItem('userInfo')).email,
        maxBorrow: JSON.parse(localStorage.getItem('userInfo')).maxBorrow,
        needToPay: JSON.parse(localStorage.getItem('userInfo')).needToPay,
        role: JSON.parse(localStorage.getItem('userInfo')).role,
      },
      dialog: {
        uploadDialog: false,
        title: '头像上传',
      },
      options: {
        img: JSON.parse(localStorage.getItem('userInfo')).avatar, //裁剪图片的地址
        autoCrop: true, // 是否默认生成截图框
        autoCropWidth: 200, // 默认生成截图框宽度
        autoCropHeight: 200, // 默认生成截图框高度
        fixedBox: true, // 固定截图框大小 不允许改变
        changeType: 'avatar',//设置上传的是头像还是背景,默认上传头像
      },
      changeAvatarTips: false,
      fileType: 'jpg',
      visible: false,

    }
  },
  methods: {
    //修改上传图像的属性，以便更符合修改图像的需求
    setBackgroundData() {
      this.dialog.title = '背景上传';
      this.options.changeType = 'background';
      this.options.autoCropHeight = 130;
      this.options.autoCropWidth = 400;
      this.options.img = JSON.parse(localStorage.getItem('userInfo')).background;
      this.dialog.uploadDialog = true;
    },

    setAvatarData() {
      this.dialog.title = '头像上传';
      this.options.changeType = 'avatar';
      this.options.autoCropWidth = 200;
      this.options.autoCropHeight = 200;
      this.options.img = JSON.parse(localStorage.getItem('userInfo')).avatar;
      this.dialog.uploadDialog = true;
    },
    //TODO 跳转到显示自己收到的信息界面
    showMyMessage() {

    },

    submitUpload() {
      //获取到截图之后的数据
      this.$refs.cropper.getCropBlob(data => {
        let formData = new FormData();
        formData.append("file", data);
        formData.append("UID", JSON.parse(localStorage.getItem('userInfo')).UID);
        formData.append("fileType", this.fileType);
        let url = '/user/setUserImg/' + this.options.changeType;
        http.post(url, formData).then(response => {
          if (response.data.code === 200) {
            getAndSyncUserInfo(this, '上传成功', '上传失败');
            this.dialog.uploadDialog = false;
          } else {
            this.$message.error(response.data.data.msg);
          }
        });
      });
    },
    //同步数据
    syncData() {
      this.userInfo.avatar = JSON.parse(localStorage.getItem('userInfo')).avatar;
      this.userInfo.role = JSON.parse(localStorage.getItem('userInfo')).role;
      this.userInfo.needToPay = JSON.parse(localStorage.getItem('userInfo')).needToPay;
      this.userInfo.maxBorrow = JSON.parse(localStorage.getItem('userInfo')).maxBorrow;
      this.userInfo.signature = JSON.parse(localStorage.getItem('userInfo')).signature;
      this.userInfo.background = JSON.parse(localStorage.getItem('userInfo')).background;
      this.userInfo.nickname = JSON.parse(localStorage.getItem('userInfo')).nickname;
      this.userInfo.email = JSON.parse(localStorage.getItem('userInfo')).email;
    },
    handleSuccess() {
      // let userInfo = localStorage.getItem('userInfo');
      // userInfo.avatar =
    },
    // 覆盖默认的上传行为
    requestUpload() {
    },
    // 上传预处理
    beforeUpload(file) {
      if (file.type.indexOf("image/") === -1) {
        this.$message({
          message: '文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件',
          type: 'error',
          duration: '2000',
        })
      } else if (file.size / 1024 / 1024 > 20) {
        this.$message({
          message: '上传的文件过大，请上传大小小于20MB的文件',
          type: 'error',
          duration: '2000',
        })
      } else if (file.size < 1) {
        this.$message({
          message: '上传不能为空',
          type: 'error',
          duration: '2000',
        })
      } else {
        //获取图片类型，取image/后面的部分
        this.fileType = file.type.substring(6);
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          this.options.img = reader.result;
        };
      }
    },
    //跳转到个人资料部分
    toPersonalInfo() {
      this.$router.push({name: 'myInfo', params: {units: 'personalInfo'}});
    },
    //跳转到我的消息部分
    toMyMessage() {
      this.$router.push({name: 'myInfo', params: {units: 'myMessage'}});
    },
    //跳转到我的评论部分
    toMyComments() {
      this.$router.push({name: 'myInfo', params: {units: 'myComments'}});
    },
    //跳转到我的书库
    toMyBookCollection() {
      this.$router.push({name: 'myInfo', params: {units: 'myBookCollection'}});
    },
    //跳转到我的帖子
    toMyPosts() {
      this.$router.push({name: 'myInfo', params: {units: 'myPosts'}});
    },
    //跳转到我的收藏
    toMyCollection() {
      this.$router.push({name: 'myInfo', params: {units: 'myCollection'}});
    },
  },
  mounted() {
    //安全性判断,防止未登录输入路由跳转
    if (localStorage.getItem('userInfo') != null) {
      //判断用户登录是否合法(主要判断token是否过期了)
      http.post('/user/testToken').then(res => {
        //过期则跳转回首页，并给出提示
        if (res.data.code !== 200) {
          this.$message.error('登录已过期，请重新登录后访问');
          localStorage.clear();
          this.$store.dispatch('syncBookInfo', '/book/getBooksList');
          this.$router.push({name: 'mainBody', params: {units: 'homePage'}});
        }
      })
    } else {
      //若localStorage内没有用户信息，则直接返回首页并给出提示
      this.$message.error('还没登录哦，请重新登录后访问');
      this.$store.dispatch('syncBookInfo', '/book/getBooksList');
      this.$router.push({name: 'mainBody', params: {units: 'homePage'}});
    }
  }
}
</script>

<style scoped>
.my-pic-info {
  position: relative;
  border-radius: 10px;
  height: 500px;
  overflow: hidden;
  margin: 15px;
  background-color: #F2F6FC;
  padding: 0;
  line-height: 0;
}

.my-background {
  position: absolute;
  z-index: 0;
  width: 100%;
  height: 380px;
}

.background-for-more {
  position: absolute;
  top: 340px;
  right: 30px;
  background-color: rgba(0, 0, 0, 0.1);
  border-radius: 90px;
  padding: 2px;
  &:hover {
    color: hotpink;
    transition: all 0.4s ease;
  }
}

.my-avatar {
  position: absolute;
  display: inline-block;
  top: 310px;
  left: 40px;
  padding: 5px;
  background-color: white;
  height: 140px;
  width: 140px;
  border-radius: 10px;
  z-index: 2;
}

.my-info {
  position: absolute;
  width: 100%;
  height: 115px;
  top: 385px;
  /*background-color: #ffb6dc;*/
  background-color: #cbddf8;
}

.my-nickname {
  position: absolute;
  top: 25px;
  left: 210px;
  font-size: 30px;
  font-family: "楷体";
}

.my-signature {
  position: absolute;
  top: 50px;
  left: 210px;
  background-color: rgba(203, 221, 236, .9);
  border-radius: 3px;
  padding: 2px;
}

.my-message {
  position: absolute;
  top: 40px;
  right: 30px;
  background-color: rgba(0, 0, 0, 0.1);
  border-radius: 90px;
  padding: 2px;
  &:hover {
    color: hotpink;
    transition: all 0.4s ease;
  }
}

.my-role {
  position: absolute;
  bottom: 15px;
  left: 40px;
}

.show-change-avatar-tips {
  position: absolute;
  top: 70px;
  left: 43px;
  color: white;
  opacity: 0;
  transition: opacity 0.3s ease, all 0.6s ease !important;
  transform: translateY(10px) !important;
}

.up-after-show {
  /* 动画过渡 */
  opacity: 1;
  transition: opacity 0.3s ease, all 0.6s ease !important;
  transform: translateY(-10px) !important;
}

.keep-mouse-style {
  /*将鼠标改成小手*/
  cursor: pointer;
}

.common-data {
  border-radius: 10px;
  height: 80px;
  overflow: hidden;
  margin: 0 15px 10px 15px;
  background-color: rgba(64, 189, 160, 0.1);
  backdrop-filter: blur(1px);
  padding: 0;
  line-height: 0;
}

.basic-menu {
  border-radius: 10px;
  overflow: hidden;
  margin: 0 15px 10px 15px;
  background-color: rgba(255, 241, 116, 0.2);
  backdrop-filter: blur(1px);
  padding: 0;
  line-height: 0;
  font-size: 10px;
}

.basic-menu-header {
  border-radius: 10px;
  height: 18px;
  overflow: hidden;
  margin: 0 15px 10px 15px;
  background-color: rgba(50, 103, 184, 0.1);
  backdrop-filter: blur(1px);
  padding-top: 15px;
  line-height: 0;
  font-size: 15px;
  cursor: default;
}

.basic-menu-item {
  height: 65px;
  cursor: pointer;
}

.basic-menu-item div svg {
  width: 55%;
  height: 50px;
}

.basic-menu-item span {
  position: relative;
  top: 4px;
  font-size: 12px;
  margin-left: -5px
}
</style>

<style>
/*设置dialog的样式*/
.el-dialog.cardDialog {
  border-radius: 30px;
  line-height: 20px;
  width: 30%;
}

.my-custom-popover {
  border-color: transparent !important;
  background-color: rgba(255, 255, 255, .5);
  color: black;
  text-align: center;
  /*默认150px，不设置最宽度就是150px，不符合需求*/
  min-width: 50px !important;
}

.my-custom-popover[x-placement^="top"] .popper__arrow::after {
  border-top-color: transparent !important;
}

.my-custom-popover[x-placement^="top"] .popper__arrow {
  border-top-color: rgba(255, 255, 255, .5) !important;
}
</style>
