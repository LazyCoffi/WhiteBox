<template>
  <div class="frame">
    <div class="background">
      <el-image :src="backgroundPath" class="backgroundImg"/>
    </div>
    <div class="centerFrame">
      <el-image :src="imagePath" style="margin-bottom: 40px;"/>
      <el-form :inline="true" label-width="auto" label-position="center" style="vertical-align: center">
        <div class="inputFrame">
          <el-form-item label="帐号" style="width: 100%">
            <el-input v-model="account" class="inputArea" size="large" placeholder="帐号"></el-input>
          </el-form-item>
        </div>
        <div class="inputFrame">
          <el-form-item label="密码" style="width: 100%">
            <el-input v-model="password" class="inputArea" size="large" placeholder="密码" type="password" show-password></el-input>
          </el-form-item>
        </div>
        <div class="inputFrame">
          <el-form-item label="重复密码" style="width: 100%">
            <el-input v-model="rePassword" class="inputArea" size="large" placeholder="重复密码" type="password" show-password></el-input>
          </el-form-item>
        </div>
        <div class="inputFrame">
          <el-form-item label="邮箱" style="width: 100%">
            <el-input v-model="email" class="inputArea" size="large" placeholder="邮箱"></el-input>
          </el-form-item>
        </div>
        <div class="inputFrame" style="text-align: left">
          <el-form-item label="验证码" style="width: 80%">
            <el-input v-model="vCode" class="inputArea" size="large" placeholder="验证码"></el-input>
          </el-form-item>
          <el-button type="primary" v-show="timeTrue == true" size="large" @click="sendVeriftCode" style="vertical-align: top; width: 10%">发送</el-button>
          <el-button type="info" v-show="timeTrue == false" loading="true" size="large" style="vertical-align: top; width: 10%">{{time}}秒</el-button>
        </div>
        <div class="inputFrame">
          <el-button @click="SignUpVerify" type="danger" size="large" round style="width: 45%"> 注册 </el-button>
          <el-button @click="goSignIn" type="success" size="large" round style="width: 45%"> 登陆 </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import md5 from "js-md5"
import axios from "../plugins/axios"
import createStore from "../store/index"
import qs from "qs"



export default {
  name: "SignUpView",
  methods : {
    sendVeriftCode() {
      this.buttonInterval();
      axios.post(createStore.state.baseUrl + "user-access/send-signup-vcode",
          qs.stringify({
            account: this.account,
            email: this.email
          })
      )
    },
    buttonInterval() {
      this.timeTrue = false;
      this.time = 60;
      var setTimeoutS = setInterval(() => {
        this.time--;
        if (this.time <= 0) {
          clearInterval(setTimeoutS);
          this.timeTrue = true;
        }
      }, 1000);
    },
    SignUpVerify() {
      if (this.account.length == 0) {
        this.$message("用户名为空");
      }

      if (this.password.length == 0) {
        this.$message("密码为空");
      }

      if (this.rePassword != this.password) {
        this.$message("两次密码不相同");
      }

      /*
        TODO:验证码
       */

      let md5Password = this.encryptPassword(this.account, this.password);

      axios.post(createStore.state.baseUrl + "user-access/sign-up",
          qs.stringify({
            account: this.account,
            password: md5Password,
            vCode: this.vCode
          })
      ).
      then((res) => {
        console.log(res);
      })
    },
    goSignIn() {
      this.$router.push("/user-access/sign-in");
    },
    encryptPassword(account, password) {
      return md5(account + md5(md5(password)));
    }
  },
  data() {
    return {
      imagePath: require('../assets/logo5.png'),
      backgroundPath: require('../assets/background4.png'),
      account : "",
      password : "",
      rePassword: "",
      email: "",
      vCode : "",
      time: 0,
      timeTrue: true
    }
  }
}
</script>

<style scoped>
.frame {
  top: 0px;
  bottom: 0px;
  left: 0px;
  right: 0px;
  position: absolute;
}

.centerFrame {
  -webkit-transform: translateY(50%);
  z-index: 1;
}

.backgroundImg {
  top: 0px;
  bottom: 0px;
  left: 0px;
  right: 0px;
  position: absolute;
}

.background {
  top: 0px;
  bottom: 0px;
  left: 0px;
  right: 0px;
  z-index: -1;
  position: absolute;
}


.inputArea >>> .el-input__inner{
  border-radius: 30px;
  display: inline;
}

.inputFrame {
  width: 30%;
  margin: 0 auto;
}
</style>
