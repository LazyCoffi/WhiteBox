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
      <div class="inputFrame" style="text-align: left">
        <el-form-item label="验证码" style="width: 80%">
          <el-input v-model="vCode" class="inputArea" size="large" placeholder="验证码" type="password" show-password></el-input>
        </el-form-item>
        <el-button type="primary" size="large" style="vertical-align: top; width: 10%">发送</el-button>
      </div>
      <div class="inputFrame">
        <el-button @click="SignInVerify" type="success" size="large" round style="width: 45%"> 登陆 </el-button>
        <el-button @click="goSignUp" type="danger" size="large" round style="width: 45%"> 注册 </el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import md5 from "js-md5"
import axios from "../plugins/axios"
import createStore from "../store/index"
export default {
  name: "SignInView",
  mounted() {
    axios.get(createStore.state.baseUrl + "user-access/token-auth")
        .then( () => {
          console.log(typeof localStorage.getItem('hasSignIn'));
          if (localStorage.getItem('hasSignIn') == true) {
            console.log(localStorage.getItem('hasSignIn'));
            console.log(localStorage.getItem('Authorization'));
            this.$router.push("/index/homepage");
          }
        } );
  },
  data() {
    return {
      imagePath : require('../assets/logo.png'),
      account: "",
      password: "",
      vCode: "",
    }
  },
  methods : {
    SignInVerify() {
      if (this.account.length == 0) {
        this.$message("帐号为空");
      }

      if (this.password.length == 0) {
        this.$message("密码为空");
      }

      /*
      TODO:验证码
      */

      let md5Password = this.encryptPassword(this.account, this.password);
      axios.get(createStore.state.baseUrl + "user-access/sign-in", {
        params: {
          account: this.account,
          password: md5Password,
          vCode: this.vCode
        }
      })
      .then((res) => {
        console.log(res);
        if (res.data.code == 200) {
          console.log("Sign in successfully");
          let token = res.data.token;
          createStore.commit("changeLogin", {
            Authorization : token,
            hasSignIn: true
          });

          this.$router.push("/index/homepage");
        }
        else {
          console.log("Sign in fail");
          this.$message(res.data.msg);
        }
      });

    },
    goSignUp() {
      this.$router.push("/user-access/sign-up");
    },
    encryptPassword(account, password) {
      return md5(account + md5(md5(password)));
    },
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