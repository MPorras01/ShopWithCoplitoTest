<script setup>
import { computed } from 'vue'
import { RouterLink, RouterView, useRouter } from 'vue-router'
import { authState, clearAuth, isAdminLike, isAuthenticated } from './stores/auth'

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

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>
