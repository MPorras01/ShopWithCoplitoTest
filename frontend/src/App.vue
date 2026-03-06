<script setup>
import { computed } from 'vue'
import { RouterLink, RouterView, useRouter } from 'vue-router'
import { authState, clearAuth, isAdminLike, isAuthenticated } from './stores/auth'
import { cartCount } from './stores/cart'

const router = useRouter()

const logged = computed(() => isAuthenticated())
const canManage = computed(() => isAdminLike())

function logout() {
  clearAuth()
  router.push('/')
}
</script>

<template>
  <div class="app-shell">
    <header class="topbar">
      <RouterLink to="/" class="brand">Tienda Verde</RouterLink>
      <nav>
        <RouterLink to="/">Catalogo</RouterLink>
        <RouterLink to="/checkout">Checkout ({{ cartCount }})</RouterLink>
        <RouterLink v-if="!logged" to="/login">Login</RouterLink>
        <RouterLink v-if="canManage" to="/admin">Admin</RouterLink>
        <button v-if="logged" @click="logout" class="ghost">Salir</button>
      </nav>
    </header>

    <main>
      <RouterView />
    </main>

    <footer class="footer">
      <p>
        {{ authState.username ? `Sesion: ${authState.username}` : 'Modo visitante' }}
      </p>
    </footer>
  </div>
</template>
