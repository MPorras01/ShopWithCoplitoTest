# Ecommerce Roadmap

## Estado general
- Fase 1 (Modelo de dominio ecommerce): completada.

## Fase 1: Modelo de dominio ecommerce
- Paso 1: Extender Product para costo y precio de venta.
  - Estado: completado.
  - Resultado: `Product` ahora soporta `cost`, `price`, `salePrice` y reglas de margen/validacion.
- Paso 2: Crear Category y relacion con Product.
  - Estado: completado.
  - Resultado: CRUD base de categorias + filtro de productos por `categoryId`.
- Paso 3: Base de entidades carrito/pedido/pago.
  - Estado: completado.
  - Resultado: entidades `CartItem`, `Order`, `OrderItem`, `Address`, `Payment` con repositorios y servicios base.

## Fase 2: APIs de carrito y checkout
- Paso 1: Endpoints REST de carrito (`add`, `update`, `remove`, `list`).
  - Estado: pendiente.
- Paso 2: Endpoint de checkout para crear orden desde carrito.
  - Estado: pendiente.
- Paso 3: Endpoint de confirmacion de pago para transicion de orden.
  - Estado: pendiente.
