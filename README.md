# Manual para Levantar la App de Turismo - Proyecto Capachica

## PASO 1: Clonar el Repositorio

Clona tanto la app móvil como el backend a tu máquina local utilizando los siguientes enlaces:

**App**:

```
git clone https://github.com/abdul8377/appTurismo202501.git
```

**Backend**:

```
git clone https://github.com/abdul8377/backendappTurismo.git
```

## PASO 2: Ubicarse en la Ruta Clonada (Backend)

Una vez clonado el repositorio del backend, navega hasta la carpeta del proyecto en tu terminal:

```
cd D:\AppCapachica\backendappTurismo
```

## PASO 3: Verificar el Archivo `.env`

Verifica si el archivo `.env` existe en la raíz del proyecto. Si no está presente, crea el archivo `.env` y pega el siguiente contenido dentro de él:

```
APP_NAME=Laravel
APP_ENV=local
APP_KEY=base64:/XvTBoeUnb0zM4NAZue+UA7uU0BrEruJAOJ2zuhQchk=
APP_DEBUG=true
APP_URL=http://localhost

APP_LOCALE=en
APP_FALLBACK_LOCALE=en
APP_FAKER_LOCALE=en_US

APP_MAINTENANCE_DRIVER=file

PHP_CLI_SERVER_WORKERS=4
BCRYPT_ROUNDS=12

LOG_CHANNEL=stack
LOG_STACK=single
LOG_DEPRECATIONS_CHANNEL=null
LOG_LEVEL=debug

DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=bdappturismov2
DB_USERNAME=root
DB_PASSWORD=

SESSION_DRIVER=database
SESSION_LIFETIME=120
SESSION_ENCRYPT=false
SESSION_PATH=/
SESSION_DOMAIN=null

BROADCAST_CONNECTION=reverb
FILESYSTEM_DISK=public
QUEUE_CONNECTION=database

CACHE_STORE=database

MEMCACHED_HOST=127.0.0.1

REDIS_CLIENT=phpredis
REDIS_HOST=127.0.0.1
REDIS_PASSWORD=null
REDIS_PORT=6379

MAIL_MAILER=smtp
MAIL_HOST=smtp.sendgrid.net
MAIL_PORT=465
MAIL_USERNAME=apikey
MAIL_PASSWORD=
MAIL_ENCRYPTION=tls
MAIL_FROM_ADDRESS=capachicaruraq@gmail.com
MAIL_FROM_NAME="Capachica Ruraq"

AWS_ACCESS_KEY_ID=
AWS_SECRET_ACCESS_KEY=
AWS_DEFAULT_REGION=us-east-1
AWS_BUCKET=
AWS_USE_PATH_STYLE_ENDPOINT=false

VITE_APP_NAME="${APP_NAME}"

# Reverb WebSockets
BROADCAST_DRIVER=reverb
REVERB_APP_ID=realtime
REVERB_APP_KEY=local
REVERB_APP_SECRET=local
REVERB_HOST=127.0.0.1
REVERB_PORT=8080

APP_PLATAFORMA_ID=999
```

## PASO 4: Instalar las Dependencias

1. **Instalar dependencias con Composer**:
   Ejecuta el siguiente comando para instalar todas las dependencias necesarias para el proyecto:

   ```
   composer install
   ```

2. **Instalar Laravel (si no tienes Laravel instalado globalmente)**:
   Si aún no tienes Laravel instalado globalmente, puedes hacerlo ejecutando:

   ```
   composer global require laravel/installer
   ```

## PASO 5: Iniciar el Servidor de Base de Datos

Asegúrate de que el servidor de base de datos esté en funcionamiento. Puedes usar **Laragon**, **XAMPP**, o cualquier otro servidor de bases de datos que prefieras.

## PASO 6: Navegar al Proyecto (Backend)

Navega a la ruta donde clonaste el proyecto (backend):

```
cd D:\AppCapachica\backendappTurismo
```

## PASO 7: Ejecutar las Migraciones de Base de Datos

Ejecuta los siguientes comandos para migrar y poblar la base de datos:

```
php artisan migrate
php artisan migrate:fresh --seed
```

Esto generará los datos de usuario de ejemplo, con las siguientes credenciales de administrador:

* **Administrador**:

  * **Usuario**: `franck@gmail.com`
  * **Contraseña**: `12345678`
* **Turista**:

  * **Usuario**: `Usuario@gmail.com`
  * **Contraseña**: `12345678`
* **Emprendedor**:

  * **Usuario**: `Carlos@gmail.com`
  * **Contraseña**: `12345678`

## PASO 8: Iniciar el Backend de Laravel

Inicia el servidor de Laravel para que el backend esté disponible en la URL `http://localhost:8000`:

```
php artisan serve --host=0.0.0.0 --port=8000
```

## PASO 9: Abrir el Proyecto en Android Studio

Abre el proyecto de la aplicación móvil en **Android Studio**:

```
cd D:\AppCapachica\appTurismo202501
```

## PASO 10: Obtener la Dirección IP de la Máquina

Ejecuta el siguiente comando en la terminal de tu computadora para obtener tu dirección **IPv4**:

```
ipconfig
```

Ejemplo de salida:

```
Dirección IPv4: 192.168.0.198
```

## PASO 11: Actualizar la IP en el Proyecto de Android

1. **Editar la IP en `TokenUtils.kt`**: Reemplaza la IP en la siguiente ruta del proyecto Android:

   ```
   D:\DAD-unidad 3\appTurismo202501\app\src\main\java\pe\edu\upeu\appturismo202501\utils\TokenUtils.kt
   ```

   Cambia la siguiente línea de código con la IP obtenida:

   ```
   var API_URL="http://pegar ip aqui/api/"
   ```

## PASO 12: Eliminar la Carpeta `storage` en el Backend

1. **Eliminar la carpeta `storage`**: Navega hasta el directorio `public` del backend y elimina la carpeta `storage` para evitar problemas con las imágenes de la app móvil.

   ```
   D:\AppCapachica\backendappTurismo\public
   ```

2. **Crear el enlace simbólico**: Ahora, en la terminal, ejecuta el siguiente comando para crear el enlace simbólico a la carpeta `storage`:

   ```
   php artisan storage:link
   ```

## PASO 13: Ejecutar la App Móvil

Ahora puedes ejecutar la app en Android Studio. Recuerda que es recomendable ingresar con las credenciales de **Administrador** para acceder a todas las funciones del sistema.

---

### **INTEGRANTES:**

* **Julmer Quispe Apaza**
* **Abdul Quispe Condori**
* **Franck A. Coaquira Justo**
* **Pretel Ramos Arisapana**

---
