"use strict";

import axios from "axios";
import createStore from "../store/index";
import router from "../router/index";

// Full config:  https://github.com/axios/axios#request-config
// axios.defaults.baseURL = process.env.baseURL || process.env.apiUrl || '';
// axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
// axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

let config = {
  // baseURL: process.env.baseURL || process.env.apiUrl || ""
  // timeout: 60 * 1000, // Timeout
  // withCredentials: true, // Check cross-site Access-Control
};

const _axios = axios.create(config);

_axios.interceptors.request.use(  config => {
        if (localStorage.getItem('Authorization')) {
            config.headers.Authorization = localStorage.getItem('Authorization');
        }
        return config;  },
    error => {    return Promise.reject(error);}
);

_axios.interceptors.response.use( config => {
        if (config.data.code == 501) {
            localStorage.removeItem('Authorization');
            localStorage.removeItem('hasSignIn');
            createStore.commit("changeLogin", {
                Authorization : "",
                hasSignIn: false
            });
            router.push("/user-access/sign-in");
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

Plugin.install = function(Vue) {
  Vue.axios = _axios;
  window.axios = _axios;
  Object.defineProperties(Vue.prototype, {
    axios: {
      get() {
        return _axios;
      }
    },
    $axios: {
      get() {
        return _axios;
      }
    },
  });
};



export default _axios;
