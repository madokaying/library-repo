<template>
  <div class="personal-info-wrapper">
    <!--        TODO 添加验证规则，长度不能超过数据库设置的长度，邮箱需符合格式-->
    <el-form
        :model="personalInfoForm"
        :label-position="formStyle.labelPosition"
        :label-width="formStyle.labelWidth"
        :rules="rules"
        status-icon
        ref="editPersonalInfoForm">
      <el-form-item label="昵称" prop="nickname" required>
        <el-input v-model="personalInfoForm.nickname" placeholder="请输入您的昵称"></el-input>
      </el-form-item>
      <el-form-item label="签名" prop="signature">
        <el-input v-model="personalInfoForm.signature" placeholder="请输入您的个性签名"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="personalInfoForm.email" placeholder="请输入您的邮箱"></el-input>
      </el-form-item>
    </el-form>
    <div class="other-info">
      <div class="show-box">
        <el-descriptions title="用户信息" :column="2" direction="vertical" border>
          <el-descriptions-item label="姓名">{{ otherInfo.realName }}</el-descriptions-item>
          <el-descriptions-item label="地址">{{ otherInfo.address }}</el-descriptions-item>
          <el-descriptions-item label="电话号码">{{ otherInfo.phoneNumber }}</el-descriptions-item>
          <el-descriptions-item label="可借书数">{{ otherInfo.maxBorrow }}</el-descriptions-item>
          <el-descriptions-item label="待付款">{{ otherInfo.needToPay }}</el-descriptions-item>
          <el-descriptions-item label="备注">
            <span v-if="otherInfo.role === '未实名用户'">尚未实名，请先持身份证线下进行实名操作，以便享受借书等服务</span>
            <span v-else>已实名</span>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag size="small">{{ otherInfo.role }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div class="show-box">
        <el-form
            :model="passwordForm"
            :label-position="formStyle.labelPosition"
            :label-width="formStyle.labelWidth"
            status-icon
            :rules="rules"
            ref="editPasswordForm">
          <div style="font-size: 16px;text-align: left;margin-bottom: 10px">
            <b>密码修改区</b>
          </div>
          <el-form-item label="原密码" prop="oldPassword" required>
            <el-input v-model="passwordForm.oldPassword" placeholder="请输入您当前的密码" show-password></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword" required>
            <el-input v-model="passwordForm.newPassword" placeholder="请输入您的新密码" show-password></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword" required>
            <el-input v-model="passwordForm.confirmPassword" placeholder="请确认您的密码" show-password></el-input>
          </el-form-item>
          <el-button type="primary" @click="changePassword" plain>修改密码</el-button>
        </el-form>
      </div>
    </div>
    <el-button type="primary" @click="submit" plain>提交修改</el-button>
  </div>
</template>

<script>
import http from "@/utils/http";
import {getAndSyncUserInfo} from '@/utils/getAndSyncUserInfo'

export default {
  name: "PersonalInfo",
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        return callback(new Error('密码不能为空'));
      } else if (value.length < 9 || value.length > 15) {
        return callback(new Error('密码长度应处于9~15之间'));
      } else {
        callback();
      }
    };
    const checkPassword = (rule, value, callback) => {
      if (value === '') {
        return callback(new Error('确认密码不能为空'));
      } else if (value !== this.passwordForm.newPassword) {
        return callback(new Error('密码不一致'));
      } else {
        callback();
      }
    };
    const checkNickname = (rule, value, callback) => {
      if (value === '') {
        return callback(new Error('昵称不能为空'));
      } else if (value.length > 12) {
        return callback(new Error('昵称长度不能大于12'));
      } else {
        callback();
      }
    };
    const checkSignature = (rule, value, callback) => {
      if (value.length > 64) {
        return callback(new Error('签名长度不能大于64'));
      } else {
        callback();
      }
    };
    const checkEmail = (rule, value, callback) => {
      const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      if (value.length > 64) {
        return callback(new Error('邮箱长度不能大于64'));
      } else if (!emailRegex.test(value)) {
        return callback(new Error('请输入正确的邮箱地址'));
      } else {
        callback();
      }
    };
    return {
      formStyle: {
        labelPosition: 'right',
        labelWidth: 'auto',
      },
      personalInfoForm: {
        nickname: JSON.parse(localStorage.getItem('userInfo')).nickname,
        signature: JSON.parse(localStorage.getItem('userInfo')).signature,
        email: JSON.parse(localStorage.getItem('userInfo')).email,
        id: parseInt(JSON.parse(localStorage.getItem('userInfo')).UID),
      },
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: '',
        uid: parseInt(JSON.parse(localStorage.getItem('userInfo')).UID),
      },
      otherInfo: {
        role: JSON.parse(localStorage.getItem('userInfo')).role,
        realName: '',
        address: '',
        phoneNumber: '',
        maxBorrow: '',
        needToPay: '',
      },
      rules: {
        oldPassword: [
          {validator: validatePassword, trigger: 'blur'}
        ],
        newPassword: [
          {validator: validatePassword, trigger: 'blur'}
        ],
        confirmPassword: [
          {validator: checkPassword, trigger: 'blur'}
        ],
        nickname: [
          {validator: checkNickname, trigger: 'blur'}
        ],
        signature: [
          {validator: checkSignature, trigger: 'blur'}
        ],
        email: [
          {validator: checkEmail, trigger: 'blur'}
        ],
      },
    }
  },
  methods: {
    //修改密码
    changePassword() {
      this.$refs['editPasswordForm'].validate((valid) => {
        if (valid) {
          // 修改成功则登出要求重新登录
          http.post("/user/changePassword", this.passwordForm).then(res => {
            if (res.data.code === 200) {
              http.post('/user/logout').then(res1 => {
                if (res1.data.code === 200) {
                  //清空localStorage
                  localStorage.clear();
                  this.$message({
                    message: res.data.msg,
                    type: 'success',
                    duration: '2000',
                  });
                  setTimeout(() => {
                    location.reload();
                  }, 2000);
                }
              })
            } else {
              this.$message({
                message: res.data.msg,
                type: 'error',
                duration: '2000',
              });
            }
          })
        } else {
          this.$message.error('输入不合法，请重新输入');
          return false;
        }
      })
    },
    //提交修改
    submit() {
      this.$refs['editPersonalInfoForm'].validate((valid) => {
        if (valid) {
          //修改用户数据
          http.post("/user/changeInfoByUser", this.personalInfoForm).then(res => {
            if (res.data.code === 200) {
              //同步用户数据
              getAndSyncUserInfo(this, '修改成功', '同步数据失败');
            } else {
              this.$message({
                message: res.data.msg,
                type: 'error',
                duration: '2000',
              });
            }
          })
        } else {
          this.$message.error('输入不合法，请重新输入');
          return false;
        }
      })
    },
    //同步数据
    syncData() {
      this.personalInfoForm.nickname = JSON.parse(localStorage.getItem('userInfo')).nickname;
      this.personalInfoForm.signature = JSON.parse(localStorage.getItem('userInfo')).signature;
      this.personalInfoForm.email = JSON.parse(localStorage.getItem('userInfo')).email;
    },
  },
}
</script>

<style scoped>
.personal-info-wrapper {
  border-radius: 10px;
  overflow: hidden;
  margin: 0 15px 10px 15px;
  background-color: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(10px);
  padding: 8px 30px 8px 30px;
  line-height: 20px;
}

.other-info {
  display: flex;
}

.show-box {
  width: 50%;
  border-radius: 10px;
  overflow: hidden;
  margin: 0 15px 10px 15px;
  background-color: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(10px);
  padding: 8px;
}
</style>

<style>
.personal-info-wrapper .el-input__inner {
  border-color: transparent !important;
  background-color: transparent !important;
  &:hover {
    border-bottom-color: hotpink !important;
    transition: all 0.9s ease;
  }
}
</style>
