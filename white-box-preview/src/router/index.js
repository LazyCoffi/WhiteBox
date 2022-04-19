import { createRouter, createWebHashHistory } from 'vue-router'
import SignUpView from "@/views/SignUpView"
import SignInView from "@/views/SignInView";
import homepageView from "@/views/HomepageView";

const routes = [
  {
    path: '/',
    redirect: '/user-access/sign-in'
  },
  {
    path: '/user-access/sign-up',
    name: 'sign-up',
    component: SignUpView
  },
  {
    path: '/user-access/sign-in',
    name: 'sign-in',
    component: SignInView
  },
  {
    path: '/index/homepage',
    name: 'homepage',
    component: homepageView
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path === '/user-access/sign-in') {
    next();
  } else {
    let hasSignIn = localStorage.getItem('hasSignIn');
    if (!hasSignIn) {
      next('/user-access/sign-in');
    } else {
      next();
    }
  }
})

export default router