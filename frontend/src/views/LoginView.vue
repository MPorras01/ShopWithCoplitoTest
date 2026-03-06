<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../services/api'
import { setAuth } from '../stores/auth'

const router = useRouter()
const form = reactive({ username: 'admin', password: 'admin123' })
const error = ref('')
const loading = ref(false)

async function submit() {
  loading.value = true
  error.value = ''
  try {
    const response = await login(form.username, form.password)
    setAuth(response)
    router.push('/admin')
  } catch (err) {
    error.value = 'Credenciales invalidas o usuario sin permisos.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <section class="login-wrap">
    <form class="login-card" @submit.prevent="submit">
      <p class="eyebrow">Acceso interno</p>
      <h2>Panel de gestion</h2>

      <label>
        Usuario
        <input v-model="form.username" required />
      </label>

      <label>
        Password
        <input v-model="form.password" type="password" required />
      </label>

      <button :disabled="loading">{{ loading ? 'Ingresando...' : 'Iniciar sesion' }}</button>
      <p v-if="error" class="error">{{ error }}</p>
    </form>
  </section>
</template>
