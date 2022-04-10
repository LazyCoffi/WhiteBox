<template>
  <div class="centerFrame">
    <el-image :src="imagePath" />
    <el-form :inline="true" label-width="auto" label-position="center" style="vertical-align: center">
      <div class="inputFrame">
        <el-form-item label="帐号" style="width: 100%">
          <el-input class="inputArea" size="large" v-model="value" placeholder="帐号"></el-input>
        </el-form-item>
      </div>
      <div class="inputFrame">
        <el-form-item label="密码" style="width: 100%">
          <el-input class="inputArea" size="large" placeholder="密码"></el-input>
        </el-form-item>
      </div>
      <div class="inputFrame" style="text-align: left">
        <el-form-item label="验证码" style="width: 80%">
          <el-input class="inputArea" size="large" placeholder="验证码"></el-input>
        </el-form-item>
        <el-button type="primary" size="large" style="vertical-align: top; width: 10%">发送</el-button>
      </div>
      <div class="inputFrame">
        <el-button type="success" size="large" round style="width: 45%"> 登陆 </el-button>
        <el-button type="danger" size="large" round style="width: 45%"> 注册 </el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "LoginView",
  data() {
    return {
      imagePath : require('../assets/logo.png'),
      value: ""
    }
  },
  methods : {
    inputAccount() {
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