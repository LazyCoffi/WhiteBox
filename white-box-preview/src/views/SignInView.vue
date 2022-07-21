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
        <div class="inputFrame" style="text-align: left">
          <el-form-item label="验证码" style="width: 80%">
            <el-input v-model="vCode" class="inputArea" size="large" placeholder="验证码" type="password" show-password></el-input>
          </el-form-item>
          <el-button type="primary" v-show="timeTrue == true" size="large" @click="sendVeriftCode" style="vertical-align: top; width: 10%">发送</el-button>
          <el-button type="info" v-show="timeTrue == false" loading="true" size="large" style="vertical-align: top; width: 10%">{{time}}秒</el-button>
        </div>
        <div class="inputFrame">
          <el-button @click="SignInVerify" type="success" size="large" round style="width: 45%"> 登陆 </el-button>
          <!--
          <el-button @click="dangerSignInVerify" type="danger" size="large" round style="width: 45%"> 危险登陆 </el-button>
          -->
          <el-button @click="goSignUp" type="danger" size="large" round style="width: 45%"> 注册 </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import md5 from "js-md5"
import axios from "../plugins/axios"
import createStore from "../store/index"
import { ElNotification } from "element-plus"
import qs from "qs";
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
      imagePath: require('../assets/logo5.png'),
      backgroundPath: require('../assets/background4.png'),
      account: "",
      password: "",
      vCode: "",
      timeTrue: true,
      time: 0
    }
  },
  methods : {
    sendVeriftCode() {
      this.buttonInterval();
      axios.post(createStore.state.baseUrl + "user-access/send-signin-vcode",
        qs.stringify({
         account: this.account,
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
    dangerSignInVerify() {
      if (this.account.length == 0) {
        ElNotification({
          title: 'Fail',
          message: '用户名不能为空!',
          type: 'fail',
        })
      }

      if (this.password.length == 0) {
        ElNotification({
          title: 'Fail',
          message: '密码不能为空!',
          type: 'fail',
        })
      }

      if (this.vCode.length == 0) {
        ElNotification({
          title: 'Fail',
          message: '验证码不能为空!',
          type: 'fail',
        })
      }

      let md5Password = this.password;

      axios.get(createStore.state.baseUrl + "user-access/danger-sign-in", {
        params: {
          account: this.account,
          password: md5Password,
          vCode: this.vCode
        }
      })

          .then((res) => {
            console.log(res);
            if (res.data.code == 200) {
              ElNotification({
                title: 'Success',
                message: '登陆成功!',
                type: 'success',
              })
              let token = res.data.token;
              createStore.commit("changeLogin", {
                Authorization : token,
                hasSignIn: true
              });

              this.$router.push("/index/homepage");
            }
            else {
              ElNotification({
                title: 'Fail',
                message: res.data.msg,
                type: 'warning',
              })
            }
          });

    },
    SignInVerify() {
      if (this.account.length == 0) {
        ElNotification({
          title: 'Fail',
          message: '用户名不能为空!',
          type: 'fail',
        })
      }

      if (this.password.length == 0) {
        ElNotification({
          title: 'Fail',
          message: '密码不能为空!',
          type: 'fail',
        })
      }

      if (this.vCode.length == 0) {
        ElNotification({
          title: 'Fail',
          message: '验证码不能为空!',
          type: 'fail',
        })
      }

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
          ElNotification({
            title: 'Success',
            message: '登陆成功!',
            type: 'success',
          })
          let token = res.data.token;
          createStore.commit("changeLogin", {
            Authorization : token,
            hasSignIn: true
          });

          this.$router.push("/index/homepage");
        }
        else {
          ElNotification({
            title: 'Fail',
            message: res.data.msg,
            type: 'warning',
          })
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