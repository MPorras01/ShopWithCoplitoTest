<script setup>
import { onMounted, reactive, ref } from 'vue'
import { createProduct, deleteProduct, getProducts, updateProduct } from '../services/api'

const products = ref([])
const loading = ref(false)
const saving = ref(false)
const error = ref('')
const editId = ref(null)

const form = reactive({
  name: '',
  description: '',
  price: 0,
  stock: 0,
  imageUrlsText: ''
})

function resetForm() {
  editId.value = null
  form.name = ''
  form.description = ''
  form.price = 0
  form.stock = 0
  form.imageUrlsText = ''
}

function toPayload() {
  return {
    name: form.name,
    description: form.description,
    price: Number(form.price),
    stock: Number(form.stock),
    imageUrls: form.imageUrlsText
      .split('\n')
      .map(v => v.trim())
      .filter(Boolean)
  }
}

async function loadProducts() {
  loading.value = true
  error.value = ''
  try {
    products.value = await getProducts()
  } catch (err) {
    error.value = 'No se pudo cargar el inventario.'
  } finally {
    loading.value = false
  }
}

function editProduct(product) {
  editId.value = product.id
  form.name = product.name
  form.description = product.description || ''
  form.price = product.price
  form.stock = product.stock
  form.imageUrlsText = (product.imageUrls || []).join('\n')
}

async function submit() {
  saving.value = true
  error.value = ''
  try {
    if (editId.value) {
      await updateProduct(editId.value, toPayload())
    } else {
      await createProduct(toPayload())
    }
    resetForm()
    await loadProducts()
  } catch (err) {
    error.value = 'Error guardando producto. Verifica permisos.'
  } finally {
    saving.value = false
  }
}

async function removeProduct(id) {
  if (!window.confirm('Deseas eliminar este producto?')) {
    return
  }

  try {
    await deleteProduct(id)
    await loadProducts()
  } catch (err) {
    error.value = 'No se pudo eliminar el producto.'
  }
}

onMounted(loadProducts)
</script>

<template>
  <section class="admin-layout">
    <div class="admin-panel form-panel">
      <h2>{{ editId ? 'Actualizar producto' : 'Nuevo producto' }}</h2>
      <form @submit.prevent="submit" class="product-form">
        <label>Nombre <input v-model="form.name" required /></label>
        <label>Descripcion <textarea v-model="form.description"></textarea></label>
        <label>Precio <input v-model="form.price" type="number" min="0" step="0.01" required /></label>
        <label>Stock <input v-model="form.stock" type="number" min="0" required /></label>
        <label>
          URLs de imagen (una por linea)
          <textarea v-model="form.imageUrlsText" rows="4" placeholder="https://..."></textarea>
        </label>

        <div class="actions">
          <button :disabled="saving">{{ saving ? 'Guardando...' : (editId ? 'Actualizar' : 'Crear') }}</button>
          <button v-if="editId" type="button" class="ghost" @click="resetForm">Cancelar</button>
        </div>
      </form>
      <p v-if="error" class="error">{{ error }}</p>
    </div>

    <div class="admin-panel list-panel">
      <h2>Inventario</h2>
      <p v-if="loading">Cargando...</p>
      <table v-else>
        <thead>
          <tr>
            <th>Producto</th>
            <th>Precio</th>
            <th>Stock</th>
            <th>Imagenes</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in products" :key="product.id">
            <td>{{ product.name }}</td>
            <td>${{ product.price }}</td>
            <td>{{ product.stock }}</td>
            <td>{{ (product.imageUrls || []).length }}</td>
            <td class="table-actions">
              <button class="ghost" @click="editProduct(product)">Editar</button>
              <button class="danger" @click="removeProduct(product.id)">Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>
