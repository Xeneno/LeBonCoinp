# LeBonCoinp — Mobile Marketplace App (Spring Boot + React Native)

API backend Spring Boot 3 + frontend React Native, construits dans le cadre du projet Grenoble INP
L’application permet de publier, liker et swiper des annonces façon Tinder, avec stockage des images sur AWS S3 et un système d’authentification JWT.

*PROJET EN COURS*
------------------------------------------------------------
🗺️ ROADMAP
------------------------------------------------------------


* [x] CRUD Items
* [x] Swipes (like/dislike)
* [x] Feed “next unseen”


#### 🟨 Prochaines étapes

* [ ] Upload AWS S3
* [ ] Authentification JWT
* [ ] Filtres (catégorie, prix, distance)
* [ ] Préchargement de plusieurs cartes (3–5 suivantes)
* [ ] Chat et système de notifications push (FCM)
* [ ] Pagination infinie sur le feed et les historiques
* [ ] Tableau de bord admin / modération
* [ ] Règles de rétention et RGPD (suppression compte, anonymisation)
* [ ] Statistiques et indicateurs d’activité (Prometheus / Grafana)
* [ ] Optimisation mobile (cache images, offline mode)


------------------------------------------------------------
STACK TECHNIQUE
------------------------------------------------------------
Backend (Java 21 / Spring Boot 3.x)
- Spring Web / Spring Data JPA / Spring Security (JWT)
- PostgreSQL 16 + Flyway (migrations)
- AWS S3 pour stockage d’images
- Lombok / MapStruct (mapping DTO)
- Springdoc OpenAPI (Swagger UI)
- Actuator + Prometheus (observabilité)
- Docker / Docker Compose (dev)
- Testcontainers (tests d’intégration)

Frontend (React Native)
- React Native 0.7x (Expo ou CLI)
- Axios + React Query (API calls + cache)
- AsyncStorage / SQLite (données offline)
- JWT auth (stockage sécurisé)
- FastImage / ImagePicker (médias)
- FCM (notifications push)
- Expo EAS Build (CI/CD mobile)

------------------------------------------------------------
ARCHITECTURE GLOBALE
------------------------------------------------------------
backend/
  src/main/java/com/example/LeBonCoinp
    Items/        # CRUD items + mapper DTO
    Swipes/       # Swipe + next unseen logic
    Matches/      # Match system (optionnel)
    Users/        # Auth, UserDetails, profil
    Media/        # Uploads S3 (presigned URLs)
    Exceptions/   # Global exception handling
    Config/       # Security, S3, OpenAPI
    Application.java
  resources/
    application.yml
    db/migration/ (Flyway)
frontend/
  src/
    api/          # appels REST axios
    screens/      # UI (Home, Swipe, ItemDetail)
    components/   # cartes, modales, boutons
    hooks/        # useAuth, useFeed
    storage/      # AsyncStorage / SQLite
  App.js

------------------------------------------------------------
FONCTIONNALITÉS ACTUELLES
------------------------------------------------------------
Utilisateurs
- Authentification par JWT
- Création / login / profil personnel

Items
- Création / modification / suppression
- Marquer comme vendu
- GET /items, GET /items/{id}

Swipes / Feed
- GET /swipe/next → prochain item non vu
- POST /swipe → like / dislike / report

Uploads
- POST /uploads → presigned URL S3, upload direct depuis le mobile

------------------------------------------------------------
INTÉGRATION AWS S3
------------------------------------------------------------
Flux
1. Mobile → POST /uploads → backend génère presigned URL
2. Mobile → PUT vers S3 avec la presigned URL
3. Backend stocke l’URL/clé S3 sur l’Item

IAM Policy minimale (à adapter)
- s3:PutObject, s3:GetObject sur arn:aws:s3:::<bucket>/*

Variables d’environnement (exemple)
AWS_REGION=eu-west-3
AWS_ACCESS_KEY_ID=...
AWS_SECRET_ACCESS_KEY=...
S3_BUCKET=lebcp-media
S3_PUBLIC_BASE=https://lebcp-media.s3.eu-west-3.amazonaws.com

------------------------------------------------------------
SÉCURITÉ
------------------------------------------------------------
- JWT Bearer (Authorization: Bearer <token>)
- Tokens courts + refresh
- CORS: origines Expo/mobile
- Validation backend (@Valid, ProblemDetail)
- Rôles: USER, ADMIN (évent. SELLER)

------------------------------------------------------------
LANCER EN LOCAL
------------------------------------------------------------
Backend
- docker compose up -d db
- ./mvnw spring-boot:run
- Swagger: http://localhost:8080/swagger-ui/index.html

Frontend
- npm install
- npx expo start
- API_BASE_URL dans .env (ex: http://10.0.2.2:8080)

------------------------------------------------------------
WORKFLOW UTILISATEUR (MOBILE)
------------------------------------------------------------
1) Auth
2) GET /swipe/next
3) POST /swipe (like/dislike)
4) serveur renvoie l’item suivant
5) Match créé si like

------------------------------------------------------------
ENDPOINTS (EXEMPLES)
------------------------------------------------------------
Auth
- POST /auth/register
- POST /auth/login

Items
- GET /items
- GET /items/{id}
- POST /items
- POST /items/{id}/sold

Swipes / Feed
- GET /swipe/next
- POST /swipe

Uploads
- POST /uploads

------------------------------------------------------------
BASE DE DONNÉES (SIMPLIFIÉE)
------------------------------------------------------------
users(id, email, password, name, role)
items(id, title, description, price, image_url, is_available, seller_id, created_at)
swipe(id, user_id, item_id, liked, swiped_at)
match(id, user_id, item_id, matched_at, status)

Indexes
- item(is_available, created_at desc, id desc)
- UNIQUE swipe(user_id, item_id)

------------------------------------------------------------
CI/CD
------------------------------------------------------------
CI GitHub Actions (backend):
- mvn verify
- docker build & push (GHCR)

CD Mobile (Expo EAS):
- API_BASE_URL, S3_PUBLIC_BASE
- Crashlytics/Sentry

------------------------------------------------------------
OBSERVABILITÉ
------------------------------------------------------------
GET /actuator/health

GET /actuator/metrics

GET /actuator/prometheus

Logs JSON (ELK ou CloudWatch)

Alerting : Grafana / Prometheus


------------------------------------------------------------
🧑‍💻 Auteur
------------------------------------------------------------
El Mehdi TALBI — Grenoble INP – Ensimag (MOSIG M2)
