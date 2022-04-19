import { createStore } from 'vuex'

export default createStore({
  state: {
    baseUrl: 'http://localhost:7007/',
    Authorization: localStorage.getItem('Authorization') ? localStorage.getItem('Authorization') : '',
    hasSignIn: localStorage.getItem('hasSignIn') ? localStorage.getItem('hasSignIn') : '',
  },
  getters: {
  },
  mutations: {
    changeLogin (state, user) {
      state.hasSignIn = user.hasSignIn;
      state.Authorization = user.Authorization;
      localStorage.setItem('Authorization', user.Authorization);
      localStorage.setItem('hasSignIn', user.hasSignIn);
    }
  },
  actions: {
  },
  modules: {
  }
})
