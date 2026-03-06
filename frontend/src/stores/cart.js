import { computed, reactive } from 'vue'

const saved = localStorage.getItem('shop-cart')

const state = reactive({
  items: saved ? JSON.parse(saved) : []
})

function persist() {
  localStorage.setItem('shop-cart', JSON.stringify(state.items))
}

export const cartState = state

export const cartCount = computed(() => state.items.reduce((acc, item) => acc + item.quantity, 0))

export const cartTotal = computed(() => state.items.reduce((acc, item) => acc + (item.price * item.quantity), 0))

export function addToCart(product) {
  const existing = state.items.find((item) => item.id === product.id)

  if (existing) {
    existing.quantity += 1
  } else {
    state.items.push({
      id: product.id,
      name: product.name,
      price: Number(product.price),
      image: (product.imageUrls && product.imageUrls[0]) || '',
      quantity: 1
    })
  }

  persist()
}

export function updateCartItemQuantity(productId, quantity) {
  const item = state.items.find((it) => it.id === productId)
  if (!item) {
    return
  }

  item.quantity = Math.max(1, Number(quantity) || 1)
  persist()
}

export function removeFromCart(productId) {
  state.items = state.items.filter((item) => item.id !== productId)
  persist()
}

export function clearCart() {
  state.items = []
  persist()
}
