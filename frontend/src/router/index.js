import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import AdminView from '../views/AdminView.vue'
import { isAuthenticated, isAdminLike } from '../stores/auth'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/login', name: 'login', component: LoginView },
    { path: '/admin', name: 'admin', component: AdminView, meta: { requiresAdmin: true } }
  ]
})

router.beforeEach((to) => {
  if (!to.meta.requiresAdmin) {
    return true
  }

  if (!isAuthenticated()) {
    return { name: 'login' }
  }

  if (!isAdminLike()) {
    return { name: 'home' }
  }

  return true
})

export default router
