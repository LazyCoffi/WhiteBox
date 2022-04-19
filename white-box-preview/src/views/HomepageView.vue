<template>
  <div>
    <el-button @click="tokenTest"> tokenTest </el-button>
    <el-button @click="logout"> Logout </el-button>
  </div>
</template>

<script>
import createStore from "../store/index"
import axios from "@/plugins/axios";

export default {
  name: "HomepageView",
  methods : {
    tokenTest() {
      axios.get(createStore.state.baseUrl + "test/token-test")
          .then( () => {
                console.log("success");
                console.log(createStore.state.Authorization);
                console.log(createStore.state.hasSignIn);
              }
          )
    },
    logout() {
      localStorage.removeItem('Authorization');
      localStorage.removeItem('hasSignIn');
      this.$router.push("/user-access/sign-in");
    }
  }
}
</script>

<style scoped>

</style>