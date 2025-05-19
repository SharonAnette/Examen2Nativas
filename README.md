# Examen2Nativas 🔐📲

Aplicación Android nativa desarrollada en Kotlin que implementa un sistema completo de autenticación con **Firebase**, gestión de **roles de usuario (Normal y Administrador)**, y un sistema de **notificaciones push** utilizando **Firebase Cloud Messaging (FCM)** y **Firebase Cloud Functions**.

---

## 📦 Contenido del Proyecto

- `MainActivity.kt` – Verifica sesión activa y redirige al flujo correcto según rol.
- `LoginScreen.kt` – Inicio de sesión de usuarios.
- `RegisterScreen.kt` – Registro de usuario normal y opción especial para administrador.
- `UserHomeScreen.kt` – Interfaz para usuarios normales: perfil, edición, historial de notificaciones.
- `AdminHomeScreen.kt` – Interfaz para administradores: todo lo anterior + lista de usuarios y envío de notificaciones.
- `NotificationSenderScreen.kt` – Composición y envío de notificaciones a uno, varios o todos los usuarios.
- `NotificationHistoryScreen.kt` – Visualización del historial de notificaciones recibidas y opción para eliminarlo.
- `MyFirebaseMessagingService.kt` – Servicio para recepción de notificaciones push.

---

## 🔑 Autenticación con Firebase

### Funcionalidades

- Registro de usuario normal y administrador
- Inicio de sesión
- Persistencia de sesión
- Cierre de sesión

### Roles de usuario

- `Usuario Normal`: recibe notificaciones, edita su perfil, consulta y elimina su historial
- `Administrador`: puede enviar notificaciones y ver todos los usuarios

### 🔐 Contraseña maestra para crear administradores

- Clave definida por el desarrollador: `ClaveMaestra2025`
- Solo si se introduce correctamente se habilita el registro de administrador

---

## 🔔 Notificaciones Push

- Integración con **Firebase Cloud Messaging**
- Recepción de notificaciones en primer y segundo plano
- Visualización en la barra de estado y pantalla interna
- Historial local de notificaciones recibidas
- Eliminación del historial por parte del usuario
- Envío desde app (no desde consola Firebase) vía **Cloud Functions**

### Panel de Envío

- Administrador puede enviar a:
  - Un usuario específico
  - Varios usuarios seleccionados
  - Todos los usuarios registrados
- Composición de mensaje con título y cuerpo

---

## 💾 Almacenamiento en Firestore

La información de cada usuario se guarda en la colección `users` con los siguientes campos:

- `name`: nombre del usuario (editable)
- `email`: correo electrónico
- `role`: `"normal"` o `"admin"`
- `fcmToken`: token único de FCM para notificaciones

---

## 🛠 Tecnologías Utilizadas

- **Kotlin**: Lenguaje principal
- **Firebase Authentication**: Gestión de usuarios
- **Firebase Firestore**: Almacenamiento de datos de usuario
- **Firebase Cloud Messaging**: Notificaciones push
- **Firebase Cloud Functions**: Envío personalizado desde la app
- **Jetpack Compose**: UI moderna y adaptable
- **Coroutines / State Management**: Lógica reactiva

---

## 🚀 Ejecución y Pruebas

1. Configura tu proyecto Firebase y descarga `google-services.json`.
2. Abre el proyecto en Android Studio y coloca el archivo en `app/`.
3. Ejecuta la app en un emulador o dispositivo real con conexión a internet.
4. Regístrate como usuario normal o introduce la **clave maestra** para registrar como administrador.
5. Inicia sesión y accede a las funcionalidades según tu rol.
6. Si eres administrador, prueba el panel de envío de notificaciones, ademas de las funcionalidades del usuario normal.
7. Como usuario normal, edita tu perfil, revisa tus notificaciones, y elimina el historial si lo deseas.

---

## Explicación de la implementación de autenticación

La autenticación en la aplicación se implementó utilizando Firebase Authentication con el método de correo electrónico y contraseña.
Los usuarios pueden registrarse como usuarios normales o administradores, y según su rol, la app redirige al panel correspondiente.
Durante el inicio de sesión:
Se valida el correo y contraseña.
Se recupera el rol del usuario desde Firestore.
Se guarda el token FCM en la base de datos para el envío de notificaciones.
Esto permite separar funciones y pantallas para cada tipo de usuario de manera segura y dinámica.

## Explicación del sistema de notificaciones

El sistema de notificaciones push se realiza con Firebase Cloud Messaging (FCM) y Cloud Functions:
Cuando el usuario se registra o inicia sesión, se guarda su token FCM en Firestore.
Desde el panel del administrador, se puede enviar una notificación:
A un usuario específico (usando radio button).
A múltiples usuarios seleccionados (opcionalmente con checkbox).
La función sendNotification en Firebase Functions toma el token, título y mensaje, envía la notificación y guarda el historial en Firestore.
Tanto administradores como usuarios pueden visualizar las notificaciones recibidas en un historial dentro de la app.
Este sistema permite comunicación directa en tiempo real con los usuarios autenticados.
