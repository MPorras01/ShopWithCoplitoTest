<script setup>
import { onMounted, ref } from 'vue'
import { getProducts } from '../services/api'

const products = ref([])
const loading = ref(false)
const error = ref('')

async function loadProducts() {
  loading.value = true
  error.value = ''
  try {
    products.value = await getProducts()
  } catch (err) {
    error.value = 'No fue posible cargar productos.'
  } finally {
    loading.value = false
  }
}

onMounted(loadProducts)
</script>

<template>
  <section class="hero">
    <div class="hero-copy">
      <p class="eyebrow">Coleccion de temporada</p>
      <h1>Tienda Verde</h1>
      <p>
        Inspirada en texturas botanicas, tonos tierra y bienestar cotidiano.
        Descubre productos curados para un estilo de vida natural.
      </p>
    </div>
    <div class="hero-shape"></div>
  </section>

  <section class="catalog">
    <header class="section-head">
      <h2>Productos</h2>
      <p>Vista publica para todos los usuarios.</p>
    </header>

    <p v-if="loading">Cargando catalogo...</p>
    <p v-else-if="error" class="error">{{ error }}</p>

    <div v-else class="grid">
      <article v-for="product in products" :key="product.id" class="card">
        <div class="carousel" v-if="product.imageUrls && product.imageUrls.length">
          <img :src="product.imageUrls[0]" :alt="product.name" />
          <span class="badge">{{ product.imageUrls.length }} fotos</span>
        </div>
        <div class="card-body">
          <h3>{{ product.name }}</h3>
          <p>{{ product.description || 'Sin descripcion' }}</p>
          <div class="meta">
            <strong>${{ product.price }}</strong>
            <span>Stock: {{ product.stock }}</span>
          </div>
        </div>
      </article>
    </div>
  </section>
</template>
