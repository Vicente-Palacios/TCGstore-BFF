```
⠸⣷⣦⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣠⣤⠀⠀⠀
⠀⠙⣿⡄⠈⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠉⣿⡿⠁⠀⠀⠀
⠀⠀⠈⠣⡀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⠀⣰⠟⠀⠀⠀⣀⣀
⠀⠀⠀⠀⠈⠢⣄⠀⡈⠒⠊⠉⠁⠀⠈⠉⠑⠚⠀⠀⣀⠔⢊⣠⠤⠒⠊⠉⠀⡜
⠀⠀⠀⠀⠀⠀⠀⡽⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠩⡔⠊⠁⠀⠀⠀⠀⠀⠀⠇
⠀⠀⠀⠀⠀⠀⠀⡇⢠⡤⢄⠀⠀⠀⠀⠀⡠⢤⣄⠀⡇⠀⠀⠀⠀⠀⠀⠀⢰⠀
⠀⠀⠀⠀⠀⠀⢀⠇⠹⠿⠟⠀⠀⠤⠀⠀⠻⠿⠟⠀⣇⠀⠀⡀⠠⠄⠒⠊⠁⠀
⠀⠀⠀⠀⠀⠀⢸⣿⣿⡆⠀⠰⠤⠖⠦⠴⠀⢀⣶⣿⣿⠀⠙⢄⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⢻⣿⠃⠀⠀⠀⠀⠀⠀⠀⠈⠿⡿⠛⢄⠀⠀⠱⣄⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⢸⠈⠓⠦⠀⣀⣀⣀⠀⡠⠴⠊⠹⡞⣁⠤⠒⠉⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⣠⠃⠀⠀⠀⠀⡌⠉⠉⡤⠀⠀⠀⠀⢻⠿⠆⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠰⠁⡀⠀⠀⠀⠀⢸⠀⢰⠃⠀⠀⠀⢠⠀⢣⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⢶⣗⠧⡀⢳⠀⠀⠀⠀⢸⣀⣸⠀⠀⠀⢀⡜⠀⣸⢤⣶⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠈⠻⣿⣦⣈⣧⡀⠀⠀⢸⣿⣿⠀⠀⢀⣼⡀⣨⣿⡿⠁⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠈⠻⠿⠿⠓⠄⠤⠘⠉⠙⠤⢀⠾⠿⣿⠟⠋
```
TCG STORE

Vicente Palacios 
Isak Chacana
# TCG Store — Backend (API de Inventario)

API REST construida con **Spring Boot 4 + Java 21** para la plataforma **TCG Store**. Expone la gestión de inventario de cartas coleccionables, protegida con JWT de **Amazon Cognito** y pensada para vivir detrás de **AWS API Gateway**, corriendo en un contenedor Docker sobre **EC2**.

> El cliente web (React) que consume esta API está en un repositorio aparte — ver [`my-app-tcgstore`](#).

---

## 🧩 Contexto del proyecto

TCG Store responde al caso de una red de 20 tiendas de cartas coleccionables (TCG) que necesita centralizar ventas, inventario, pedidos, notificaciones, reportes y auditoría en una sola plataforma corporativa, con autenticación centralizada y control de acceso por rol.

Este repositorio implementa el **microservicio de inventario de cartas**, el primer módulo de esa plataforma: un CRUD protegido por rol, con salud del servicio y persistencia en base de datos administrada. Los demás componentes descritos en el caso original (mensajería asíncrona, streaming de eventos, notificaciones, microservicios de pedidos/ventas) quedan como evolución futura del proyecto.

### Decisiones de implementación vs. el enunciado original

El enunciado planteaba una arquitectura de referencia (Azure AD, RDS gestionado tradicional, etc.); este proyecto la adapta usando herramientas nativas de **GitHub** y **AWS**, manteniendo la misma intención (login corporativo centralizado, backend detrás de un gateway, despliegue containerizado):

| Enunciado del caso | Implementado en este proyecto | Motivo |
|---|---|---|
| Login corporativo con Azure AD (IDaaS) | **Amazon Cognito** (User Pool + grupos como roles) | Integración nativa con el resto del stack AWS (API Gateway, IAM) |
| Backend Spring Boot detrás de AWS API Gateway | ✅ Se mantiene tal cual | — |
| Despliegue en AWS EC2 con Docker/Docker Compose | ✅ EC2 + Docker, orquestado vía **GitHub Actions** | CI/CD 100% en GitHub, sin herramientas adicionales de despliegue |
| Base de datos (no especificada explícitamente) | **PostgreSQL gestionado en Supabase** | Provisioning rápido, gratuito para el alcance del proyecto, compatible 1:1 con Spring Data JPA |
| Gestión de secretos (Azure Key Vault, etc.) | **GitHub Actions Secrets/Variables** inyectados como variables de entorno del contenedor | Todo el ciclo de vida (build → push → deploy) ocurre en GitHub, sin depender de un vault externo |
| RabbitMQ, Kafka + Zookeeper | **Amazon SNS → SQS → Lambda** (guía del curso). El BFF solo publica en el topic SNS `report-events`; las colas y Lambdas viven fuera de este repo | Mensajería administrada en AWS, sin broker propio que mantener |

---

## 🛠️ Stack técnico

- **Java 21** + **Spring Boot 4**
- **Spring Web (MVC)**
- **Spring Security** + **OAuth2 Resource Server** (validación de JWT emitido por Cognito)
- **Spring Data JPA** + **PostgreSQL** (driver `org.postgresql`)
- **AWS SDK v2 (SNS)** para publicar solicitudes de informe
- **Gradle** (wrapper incluido)
- **Docker** (imagen `eclipse-temurin:21-jre-jammy`)
- **GitHub Actions** para CI/CD

---

## 🏗️ Arquitectura y flujo de una petición

```
Usuario (React app)
      │  Authorization: Bearer <id_token de Cognito>
      ▼
Amazon Cognito ── valida sesión / emite JWT con "cognito:groups"
      │
      ▼
AWS API Gateway ── agrega header X-Secret-Gateway
      │
      ▼
EC2 (Docker) → Spring Boot
      ├── SecretGatewayFilter   → valida X-Secret-Gateway (bloquea tráfico que no venga del Gateway)
      ├── OAuth2 Resource Server → valida firma/emisor del JWT contra el User Pool de Cognito
      ├── CognitoJwtAuthenticationConverter → mapea "cognito:groups" a ROLE_Admin / ROLE_Colaborador / ROLE_Cliente
      └── @PreAuthorize en cada endpoint → autoriza según rol
      │
      ▼
Supabase (PostgreSQL) ── persistencia de cartas
```

**Doble capa de seguridad** por diseño:
1. `SecretGatewayFilter`: rechaza cualquier request que no incluya el header secreto compartido con API Gateway (evita que alguien le pegue directo a la IP de EC2, saltándose el Gateway).
2. `Spring Security + JWT`: valida que el token venga firmado por el User Pool de Cognito configurado (`issuer-uri`) y aplica autorización por rol en cada endpoint.

---

## 🔐 Seguridad y roles

Los roles se leen del claim `cognito:groups` del JWT y se mapean a authorities `ROLE_<grupo>` de Spring Security:

| Rol | `GET` (listar/ver) | `POST` / `PUT` / `PATCH` | `DELETE` |
|---|:---:|:---:|:---:|
| Cliente | ✅ | ❌ | ❌ |
| Colaborador | ✅ | ✅ | ❌ |
| Admin | ✅ | ✅ | ✅ |

Toda ruta no listada explícitamente requiere autenticación (`anyRequest().authenticated()`), por lo que no hay endpoints públicos salvo que se habiliten explícitamente (ver nota sobre `/api/health` en `SecurityConfig`).

---

## 🔌 Endpoints

Base path: `/api`

### Cartas — `/api/cards`

| Método | Ruta | Descripción | Body | Rol requerido |
|---|---|---|---|---|
| `GET` | `/api/cards` | Lista todas las cartas del inventario | — | Autenticado |
| `GET` | `/api/cards/{id}` | Obtiene una carta por id (404 si no existe) | — | Autenticado |
| `POST` | `/api/cards` | Crea una carta nueva | `CardRequestDto` | Admin, Colaborador |
| `PUT` | `/api/cards/{id}` | Reemplaza todos los campos de una carta | `CardRequestDto` | Admin, Colaborador |
| `PATCH` | `/api/cards/{id}` | Actualiza solo los campos enviados (no nulos) | `CardRequestDto` (parcial) | Admin, Colaborador |
| `DELETE` | `/api/cards/{id}` | Elimina una carta (204 si éxito, 404 si no existe) | — | Admin |

**`CardRequestDto` / `CardResponseDto`**

```json
{
  "name": "Pikachu",
  "game": "Pokémon",
  "setName": "Base Set",
  "rarity": "Común",
  "condition": "Near Mint",
  "price": 1000.0,
  "stock": 1
}
```

`CardResponseDto` agrega el campo `id` (UUID generado por el backend).

### Salud del servicio — `/api/health`

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/health` | Verifica conectividad con la base de datos. Devuelve `{"status":"UP"}` (200) o `{"status":"DOWN","error":"..."}` (503) |

### Informes — `/api/informes`

| Método | Ruta | Descripción | Body | Rol requerido |
|---|---|---|---|---|
| `POST` | `/api/informes/generar` | Publica `report.requested` en el topic SNS `report-events`. Responde `202` con `{"informeId": "...", "status": "SOLICITADO"}` | Opcional: `{"reportType": "top-products", "params": {}}` | Autenticado |

El solicitante (`requester` = email del JWT, `requesterId` = `sub`) lo decide el servidor, no el cuerpo. Mensaje publicado:

```json
{"type":"report.requested","body":{"requestId":"<uuid>","requester":"usuario@correo.cl","requesterId":"<sub>","reportType":"top-products","params":{},"requestedAt":"2026-10-26T21:00:00Z"}}
```

SNS lo reparte por el campo `type` a las colas `report-requests` (Lambda que guarda en Postgres) y `report-generation` (Lambda que arma y publica el aviso).

---

## 📂 Estructura del proyecto

```
src/main/java/com/palacios_chacana/tcgstore/
├── cards/
│   ├── CardController.java        # Endpoints REST de /api/cards
│   ├── CardService.java           # Lógica de negocio (CRUD)
│   ├── CardRepository.java        # Spring Data JPA repository
│   ├── dto/                       # CardRequestDto / CardResponseDto
│   └── entity/Card.java           # Entidad JPA (tabla "cards")
├── config/
│   ├── SecurityConfig.java        # Cadena de filtros, JWT resource server, @PreAuthorize
│   └── SecretGatewayFilter.java   # Valida el header X-Secret-Gateway de API Gateway
├── security/
│   └── CognitoJwtAuthenticationConverter.java  # Mapea cognito:groups → ROLE_*
├── informes/
│   ├── InformeController.java     # POST /api/informes/generar
│   ├── InformeService.java        # Publica el mensaje en SNS
│   └── dto/                       # GenerarInformeRequestDto / InformeMessage
├── health/
│   ├── HealthCheckController.java # GET /api/health
│   └── dto/HealthResponseDto.java
└── TcGstoreApplication.java       # Entry point
```

---

## 🚀 Puesta en marcha local

### Requisitos
- JDK 21
- Una base de datos PostgreSQL accesible (local, o el proyecto de Supabase del equipo)
- Un User Pool de Cognito configurado (o las credenciales del ambiente compartido)

### Variables de entorno

| Variable | Descripción | Ejemplo |
|---|---|---|
| `PORT` | Puerto en el que corre la app | `8080` |
| `PUBLIC_HOST_URL` | URL pública del servicio (informativa) | `http://localhost:8080` |
| `API_GATEWAY_SECRET` | Secreto compartido con API Gateway, validado por `SecretGatewayFilter` | `secreto_local` |
| `DB_URL` | Cadena de conexión JDBC a PostgreSQL (Supabase) | `jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:5432/postgres` |
| `DB_USER` | Usuario de la base de datos | `postgres.xxxx` |
| `DB_PASSWORD` | Password de la base de datos | — |
| `SNS_TOPIC_ARN` | ARN del topic SNS `report-events` | `arn:aws:sns:us-east-2:123456789012:report-events` |
| `AWS_REGION` | Región del topic SNS | `us-east-2` |
| `AWS_ENDPOINT_URL` | Solo en local con LocalStack (junto con `AWS_ACCESS_KEY_ID`/`AWS_SECRET_ACCESS_KEY` de prueba) | `http://localhost:4566` |

En AWS real no se definen claves: el SDK usa el **rol IAM de la instancia EC2**, que necesita el permiso `sns:Publish` sobre el topic.

El `issuer-uri` de Cognito (`spring.security.oauth2.resourceserver.jwt.issuer-uri`) está fijado en `application.properties` apuntando al User Pool del proyecto; si se cambia de User Pool, hay que actualizarlo ahí.

### Ejecutar en local

```bash
# Linux/macOS
export DB_URL="jdbc:postgresql://<host>:5432/postgres"
export DB_USER="postgres"
export DB_PASSWORD="..."
export API_GATEWAY_SECRET="secreto_local"

./gradlew bootRun
```

```powershell
# Windows
$env:DB_URL="jdbc:postgresql://<host>:5432/postgres"
$env:DB_USER="postgres"
$env:DB_PASSWORD="..."
$env:API_GATEWAY_SECRET="secreto_local"

.\gradlew.bat bootRun
```

> ⚠️ Al probar en local sin pasar por API Gateway, cualquier request necesita el header `X-Secret-Gateway: <API_GATEWAY_SECRET>` además del `Authorization: Bearer <jwt>`, o `SecretGatewayFilter` la rechazará con 403.

### Tests

```bash
./gradlew test
```

---

## 🐳 Docker

```bash
./gradlew bootJar -x test
docker build -t tcgstore .
docker run -d -p 80:8080 \
  -e PORT=8080 \
  -e API_GATEWAY_SECRET=... \
  -e DB_URL=... \
  -e DB_USER=... \
  -e DB_PASSWORD=... \
  tcgstore
```

La imagen corre con un usuario sin privilegios (`springuser`) y expone el puerto `80` dentro del contenedor mapeado a `8080` de la app.

---

## ⚙️ CI/CD — GitHub Actions → EC2

Cada `push` a `main` dispara el workflow [`.github/workflows/deploy.yml`](.github/workflows/deploy.yml), que:

1. Compila el proyecto con **Gradle** (JDK 21, `./gradlew bootJar -x test`).
2. Construye la imagen Docker y la publica en **GitHub Container Registry (GHCR)**: `ghcr.io/vicente-palacios/tcgstore:latest`.
3. Se conecta por **SSH** a la instancia **EC2** (Amazon Linux) y, de forma idempotente:
    - Provisiona swap (2GB) si no existe.
    - Instala Docker si no está presente.
    - Descarga la última imagen desde GHCR.
    - Detiene/elimina el contenedor anterior y levanta el nuevo, mapeando el puerto `80` del host al `8080` del contenedor.

### Secrets y variables usados en GitHub

| Nombre | Tipo | Uso |
|---|---|---|
| `EC2_SSH_KEY` | Secret | Llave privada para conectarse por SSH a la instancia EC2 |
| `GH_CR_PAT` | Secret | Token para autenticar el `docker login` en GHCR desde el EC2 |
| `API_GATEWAY_SECRET` | Secret | Se inyecta como variable de entorno del contenedor en producción |
| `SUPABASE_DB_USER` / `SUPABASE_DB_PASSWORD` | Secret | Credenciales de la base de datos en Supabase |
| `SNS_TOPIC_ARN` | Variable (`vars`) | ARN del topic SNS, inyectado como variable de entorno del contenedor |
| `TARGET_HOST_IP` | Variable (`vars`) | IP pública de la instancia EC2 destino |
| `GITHUB_TOKEN` | Automático | Autenticación para publicar la imagen en GHCR |

No se usa ningún gestor de secretos externo (Azure Key Vault, AWS Secrets Manager, etc.): todo el ciclo de despliegue —build, push de imagen y entrega de credenciales al contenedor— vive dentro de GitHub Actions.

---

## ☁️ Infraestructura AWS utilizada

- **Amazon Cognito**: User Pool como proveedor de identidad (IDaaS) y fuente de roles vía grupos.
- **AWS API Gateway**: punto de entrada único de la API; agrega el header secreto que valida `SecretGatewayFilter` y enruta hacia el backend.
- **Amazon EC2**: instancia donde corre el contenedor Docker del backend (Amazon Linux).
- **Amazon SNS / SQS / Lambda**: mensajería de informes (el BFF publica en SNS).
- **Supabase (PostgreSQL)**: base de datos gestionada, usada como reemplazo liviano de un RDS tradicional para este alcance del proyecto.

---
Captura 1 — Respuesta 200 (token válido, GET permitido a cualquier autenticado):

Se realizó una petición GET /api/cards con un token JWT válido de Cognito (usuario con rol Colaborador) y el header X-Secret-Gateway correcto. El backend respondió 200 OK, devolviendo el listado de cartas en formato JSON, confirmando que la autenticación y la conexión a la base de datos funcionan correctamente.

<img width="907" height="466" alt="image" src="https://github.com/user-attachments/assets/69e391b0-97c6-4cef-98ab-ab5b2c7202f2" />

Captura 2 — Respuesta 403 (token válido, pero sin el rol requerido):

Se realizó una petición DELETE /api/cards/{id} con el mismo token válido de un usuario Colaborador. El backend respondió 403 Forbidden con el mensaje insufficient_scope, ya que el endpoint de eliminación requiere el rol Admin (@PreAuthorize("hasRole('Admin')")). Esto confirma que la autorización basada en roles funciona correctamente: el usuario está autenticado, pero no autorizado para esta acción específica.

<img width="883" height="451" alt="image" src="https://github.com/user-attachments/assets/29a859dd-14a4-4116-9e74-4acb811d23f9" />




