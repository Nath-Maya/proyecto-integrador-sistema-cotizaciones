# Arquitectura Modelo-Vista-Controlador (MVC)

## En que consiste la arquitectura.

El proyecto ha sido estructurado siguiendo el patrón arquitectónico **Modelo-Vista-Controlador (MVC)**, que separa la aplicación en tres capas:

### 📁 Estructura de Directorios

```
src/main/java/
├── model/                          # MODELO - Datos y lógica de negocio
│   ├── Administrador.java         # Clase que representa un administrador
│   ├── BaseDatos.java             # Gestión de persistencia de datos
│   ├── CostosMateriaPrima.java    # Gestión de costos de materiales
│   ├── Empresa.java               # Clase que representa una empresa
│   └── Pedido.java                # Clase que representa un pedido
├── view/                           # VISTA - Interfaz de usuario
│   └── MenuSistema.java           # Menús e interacción con usuario
├── controller/                     # CONTROLADOR - Lógica de negocio
│   └── Cotizador.java             # Cálculo de cotizaciones
├── util/                           # UTILIDADES - Funciones auxiliares
│   ├── Archivo.java               # Gestión de archivos (persistencia)
│   └── Validaciones.java          # Validaciones de datos
└── Main.java                       # Punto de entrada de la aplicación
```

### 🔧 UTILIDADES (util/)
**Responsabilidad:** Funciones auxiliares reutilizables

- **Archivo.java** - Persistencia en archivo (serialización)
  - Guardar datos (BaseDatos)
  - Cargar datos (BaseDatos)
- **Validaciones.java** - Validaciones de datos
  - Números, texto, enteros
  - Teléfono, correo electrónico

## Beneficios de esta Arquitectura

✅ **Separación de responsabilidades** - Cada componente tiene un único propósito
✅ **Reutilización de código** - Los modelos pueden usarse en diferentes vistas
✅ **Mantenibilidad** - Cambios en una capa no afectan a las otras
✅ **Testabilidad** - Cada componente puede probarse independientemente
✅ **Escalabilidad** - Fácil agregar nuevas funcionalidades

## Instrucciones de Compilación

Asegúrate de compilar desde el directorio raíz del proyecto:

```bash
javac -d bin src/main/java/**/*.java
java -cp bin Main
```


