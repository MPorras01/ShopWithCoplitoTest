# Flujo Git Requerido

Este repositorio trabaja con el siguiente flujo obligatorio:

1. Crear HU en GitHub (issue) usando la plantilla `Historia de Usuario (HU)`.
2. Crear rama `feature/<nombre>` desde `dev`.
3. Implementar cambios y probar en `dev`.
4. Crear rama de release versionada (`release-vX.Y.Z`) desde `dev`.
5. Crear Merge Request (Pull Request) con la plantilla de PR y asociar la HU (`closes #N`).
6. Solicitar aprobacion explicita antes de mergear.
7. Luego de aprobar:
   - merge `release-vX.Y.Z -> qa`
   - validar pruebas QA
   - merge `qa -> master`

## Versionado
- `X.Y.Z`
- `X`: cambio mayor
- `Y`: cambio menor
- `Z`: correccion de errores

## Regla Operativa
Siempre despues de probar, promover `release -> qa -> master`.
