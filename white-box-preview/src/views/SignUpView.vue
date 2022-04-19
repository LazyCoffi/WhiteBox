<template>
  <div class="centerFrame">
    <el-image :src="imagePath" />
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
      <div class="inputFrame" style="text-align: left">
        <el-form-item label="验证码" style="width: 80%">
          <el-input v-model="vCode" class="inputArea" size="large" placeholder="验证码"></el-input>
        </el-form-item>
        <el-button type="primary" size="large" style="vertical-align: top; width: 10%">发送</el-button>
      </div>
      <div class="inputFrame">
        <el-button @click="SignUpVerify" type="danger" size="large" round style="width: 45%"> 注册 </el-button>
        <el-button @click="goSignIn" type="success" size="large" round style="width: 45%"> 登陆 </el-button>
      </div>
    </el-form>
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
      imagePath : require('../assets/logo.png'),
      account : "",
      password : "",
      rePassword: "",
      vCode : "",
    }
  }
}
</script>

<style scoped>
.centerFrame {
  -webkit-transform: translateY(50%);
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
