<script setup>
import { computed, reactive } from 'vue'
import { cartState, cartTotal, clearCart, removeFromCart, updateCartItemQuantity } from '../stores/cart'

const WHATSAPP_NUMBER = import.meta.env.VITE_WHATSAPP_NUMBER || '573000000000'

const customer = reactive({
  name: '',
  phone: '',
  address: '',
  notes: ''
})

const hasItems = computed(() => cartState.items.length > 0)

function formatMoney(value) {
  return Number(value || 0).toFixed(2)
}

function sendOrderWhatsApp() {
  if (!hasItems.value) {
    window.alert('No hay productos en el carrito.')
    return
  }

  if (!customer.name || !customer.phone || !customer.address) {
    window.alert('Completa nombre, telefono y direccion para enviar el pedido.')
    return
  }

  const lines = [
    '*Nuevo pedido - Tienda Verde*',
    '',
    '*Cliente:* ' + customer.name,
    '*Telefono:* ' + customer.phone,
    '*Direccion:* ' + customer.address,
    customer.notes ? '*Notas:* ' + customer.notes : '',
    '',
    '*Detalle de compra:*'
  ].filter(Boolean)

  cartState.items.forEach((item, idx) => {
    lines.push(`${idx + 1}. ${item.name} x${item.quantity} - $${formatMoney(item.price * item.quantity)}`)
  })

  lines.push('')
  lines.push(`*Total:* $${formatMoney(cartTotal.value)}`)

  const message = encodeURIComponent(lines.join('\n'))
  const url = `https://wa.me/${WHATSAPP_NUMBER}?text=${message}`
  window.open(url, '_blank')
}

function finalizeLocal() {
  clearCart()
  customer.name = ''
  customer.phone = ''
  customer.address = ''
  customer.notes = ''
}
</script>

<template>
  <section class="checkout-layout">
    <div class="checkout-panel">
      <h2>Checkout</h2>
      <p>Confirma tus productos y envia el pedido directo por WhatsApp.</p>

      <div v-if="!hasItems" class="empty-cart">
        Tu carrito esta vacio.
      </div>

      <div v-else class="checkout-items">
        <article v-for="item in cartState.items" :key="item.id" class="checkout-item">
          <img v-if="item.image" :src="item.image" :alt="item.name" />
          <div>
            <h3>{{ item.name }}</h3>
            <p>${{ formatMoney(item.price) }} c/u</p>
          </div>
          <input
            type="number"
            min="1"
            :value="item.quantity"
            @input="updateCartItemQuantity(item.id, $event.target.value)"
          />
          <strong>${{ formatMoney(item.price * item.quantity) }}</strong>
          <button class="danger" @click="removeFromCart(item.id)">Quitar</button>
        </article>
      </div>

      <div v-if="hasItems" class="checkout-total">
        <span>Total</span>
        <strong>${{ formatMoney(cartTotal) }}</strong>
      </div>
    </div>

    <div class="checkout-panel">
      <h2>Datos de entrega</h2>
      <form class="product-form" @submit.prevent="sendOrderWhatsApp">
        <label>Nombre completo <input v-model="customer.name" required /></label>
        <label>Telefono <input v-model="customer.phone" required /></label>
        <label>Direccion <textarea v-model="customer.address" required></textarea></label>
        <label>Notas del pedido <textarea v-model="customer.notes"></textarea></label>

        <div class="actions">
          <button type="submit">Enviar pedido por WhatsApp</button>
          <button type="button" class="ghost" @click="finalizeLocal">Limpiar carrito</button>
        </div>
      </form>
      <p class="muted">
        Numero destino actual: {{ WHATSAPP_NUMBER }}
      </p>
    </div>
  </section>
</template>
