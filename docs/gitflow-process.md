# Flujo Git Requerido

Este repositorio trabaja con el siguiente flujo obligatorio:

1. Crear HU en GitHub (issue) usando la plantilla `Historia de Usuario (HU)`.
2. Crear rama de trabajo (`feature/<nombre>` o `process/<nombre>`) y pasar cambios a `dev`.
3. Ejecutar pruebas de desarrollador en `dev`.
4. Crear rama de release versionada (`release-vX.Y.Z`) desde `dev`.
5. Crear Pull Request con la plantilla y asociar la HU (`closes #N`).
6. Con autorizacion del responsable, el agente puede aceptar PR y promover ramas.
7. Flujo de promocion obligatorio despues de pruebas:
   - `release-vX.Y.Z -> qa`
   - validacion QA
   - `qa -> master`

## Politica Operativa Acordada
- El agente puede pasar directamente a `dev` para integracion y pruebas.
- El agente puede solicitar Pull Request hacia `release` o `qa` segun corresponda.
- El agente puede aceptar Pull Request y ejecutar promociones `release -> qa -> master`.

## Versionado
- `X.Y.Z`
- `X`: cambio mayor
- `Y`: cambio menor
- `Z`: correccion de errores

## Regla Operativa
Siempre despues de probar, promover `release -> qa -> master`.
