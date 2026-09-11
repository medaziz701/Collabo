# Collabo

> Plateforme web collaborative de gestion de projets de développement, inspirée de GitHub, permettant la création de projets, l'assignation de tâches, le suivi de l'avancement et la communication en temps réel entre les membres de l'équipe.

![screenshot](./screenshots/preview.png)

## 🚀 Stack technique

### Frontend
- **Angular 16.2.0** - Framework frontend
- **Angular Material** - Composants UI
- **RxJS 7.8.0** - Programmation réactive
- **SweetAlert2 11.21.0** - Alertes modales
- **Angular Calendar 0.31.1** - Gestion de calendrier
- **Zego Cloud 2.14.3** - Appels vidéo
- **Highlight.js 11.9.0** - Coloration syntaxique
- **@auth0/angular-jwt 5.2.0** - Gestion JWT

### Backend
- **Spring Boot 3.4.2** - Framework Java
- **Java 17** - Langage
- **Spring Data JPA** - ORM
- **PostgreSQL** - Base de données
- **Spring Security** - Sécurité
- **JWT (jjwt 0.7.0)** - Authentification
- **Spring Mail** - Envoi d'emails
- **Spring Actuator** - Monitoring

### Déploiement
- **Render** - Backend
- **Netlify** - Frontend
- **Railway** - Alternative déploiement

## 📋 Prérequis

- Node.js 18+ et npm
- Java 17+
- Maven 3.6+
- PostgreSQL 14+

## ⚙️ Installation

```bash
# 1. Cloner le repo
git clone https://github.com/ton-username/Collabo.git
cd Collabo-main

# 2. Installer les dépendances Backend
cd pfe
mvn clean install

# 3. Configurer les variables d'environnement
cp .env.example .env
# Remplir les valeurs dans .env (POSTGRES_USER, POSTGRES_PASSWORD, MAIL_USERNAME, etc.)

# 4. Lancer le backend
mvn spring-boot:run

# 5. Installer les dépendances Frontend (dans un nouveau terminal)
cd ../front
npm install

# 6. Configurer l'API URL (si nécessaire)
# Modifier front/src/environments/environment.ts

# 7. Lancer le frontend
npm start

# 8. Installer les dépendances Admin (dans un nouveau terminal)
cd ../Admin
npm install

# 9. Lancer l'admin
npm start
```

## ✨ Fonctionnalités

### Pour les Développeurs
- Dashboard personnel avec statistiques de projets
- Gestion des tâches assignées
- Marquage des tâches comme terminées
- Chat privé avec les membres de l'équipe
- Éditeur de code collaboratif
- Appels vidéo avec Zego Cloud
- Planning personnel

### Pour les Chefs d'Équipe
- Dashboard avec vue d'ensemble des projets
- Création et gestion des projets
- Assignation des tâches aux développeurs
- Gestion des équipes
- Planning des équipes
- Suivi des commentaires sur les tâches
- Tableau de bord Power BI

### Pour les Clients
- Dashboard avec vue des projets
- Création de nouveaux projets
- Suivi de l'avancement des projets
- Feedback sur les projets
- Contact avec l'équipe

### Pour les Administrateurs
- Gestion des utilisateurs (développeurs, chefs d'équipe, clients)
- Gestion des projets
- Gestion des contacts
- Tableau de bord Power BI avec statistiques globales

## 🌐 Démo live

En cours de déploiement

## 👤 Auteur

**Mohamed Aziz Chaabani**  
Portfolio : https://portfolio-chaabeni-mohamed-aziz.netlify.app  
GitHub : https://github.com/ton-username

## 📝 Structure du projet

```
Collabo-main/
├── Admin/              # Application Angular pour l'administration
├── front/              # Application Angular principale (utilisateurs)
├── pfe/                # Backend Spring Boot
└── screenshots/        # Captures d'écran
```

## 🔐 Sécurité

- Authentification JWT
- Rôles basés sur l'accès (RBAC)
- Variables d'environnement pour les secrets
- CORS configuré

## 📄 Licence

Ce projet est un projet académique.
