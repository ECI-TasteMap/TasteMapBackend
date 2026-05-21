# 🍽️ TasteMap - Backend

TasteMap es una aplicación móvil que centraliza la información de restaurantes, cafeterías y bares en
Colombia, facilitando encontrar lugares confiables incluso si no aparecen en mapas tradicionales.
Integra localización, filtros avanzados y un motor de recomendaciones con IA basado en preferencias
reales del usuario.

Este repositorio corresponde al **backend del proyecto**.  
Su objetivo es proveer los servicios y lógica de negocio necesarios para la aplicación.

---

## 🚀 Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener instaladas las siguientes herramientas:

- [Java JDK 21 o superior](https://adoptium.net/)
- [Apache Maven 3.8+](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/)
- [Docker](https://www.docker.com/)
- (Opcional) [IntelliJ IDEA](https://www.jetbrains.com/idea/)

Verifica las versiones con:

```bash
java -version
mvn -version
git --version
docker --version
```

## 📦 Clonar el repositorio

Abre una terminal en el directorio donde quieras guardar el proyecto y ejecuta:

```bash
git clone https://github.com/Dinoprogramadores/Backend-TasteMap.git
cd Backend-TasteMap
```

## ⚙️ Configuración de variables de entorno

---

**IMPORTANTE!!**

*Por seguridad, las variables de entorno no deben ser subidas al repositorio.*

---

Este proyecto utiliza MongoDB Atlas como base de datos en la nube y Supabase como proveedor de autenticación.
Para que la aplicación pueda conectarse correctamente:

Crea un archivo llamado `.env` en la raíz del proyecto (mismo nivel que `docker-compose.yml`).

**Nota:** Usa como base el archivo `.env.example`

Dentro de ese archivo, configura las siguientes variables de entorno:

```env
MONGODB_URI=tu_cadena_de_conexion_de_atlas
SUPABASE_URL=tu_url_de_supabase
SUPABASE_KEY=tu_clave_de_supabase
```

En el archivo `application.properties`, asegúrate de igualmente modificar `spring.data.mongodb.uri=mongodb://mongo:27017/tastemap`
por la cadena de conexión de Atlas.

## 🐳 Ejecutar con Docker

Este proyecto incluye un archivo `docker-compose.yml` preparado para usar MongoDB Atlas,
por lo que no necesitas levantar un contenedor local de MongoDB.

Para construir y ejecutar el backend:
````bash
docker compose up --build
````

Si quieres ejecutarlo en segundo plano:
````bash
docker compose up --build -d
````

Luego accede a:

http://localhost:8080/

## 🧰 Ejecución local (sin Docker)

1. Para compilar el proyecto:
```bash
mvn clean package
```
2. Para ejecutar la aplicación en local:
```bash
mvn spring-boot:run
```
3. Ingresa a:
```
http://localhost:8080/
```
4. Para ver la documentación swagger del proyecto ingresa a:
```
http://localhost:8080/swagger-ui/index.html
```

Cuando realices cambios (NO a `docker-compose.yml` ni la configuración
de la base de datos) ejecuta nuevamente el paso 1 y 2.

## 🧪 Pruebas

Ejecuta las pruebas unitarias con:
```bash
mvn test
```

## 🧤 Contribuciones

1. Crea una nueva rama:
```bash
git checkout -b feat/nueva-funcionalidad
```

2. Realiza tus cambios y súbelos:
```bash
git commit -m "Agrega nueva funcionalidad"
git push origin feat/nueva-funcionalidad
```

3. Abre un Pull Request y espera la revisión de otro miembro del equipo.

---
🗺️

Proyecto desarrollado por el equipo TasteMap 🧠💻  
Hecho con ☕ y ❤️ usando Spring Boot + MongoDB Atlas + Supabase + Docker + Render
