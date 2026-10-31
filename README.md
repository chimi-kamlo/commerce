### 🛒 API E-commerce

### Présentation
API REST développée avec Java et Spring Boot permettant de gérer une plateforme e-commerce.

### Fonctionnalités

-> Gestion des utilisateurs (inscription, authentification).

-> Authentification et autorisation avec Spring Security et JWT.

-> Gestion des rôles et permissions.

-> Gestion des produits et catégories.

-> Gestion des commandes.

-> Validation des données avec Jakarta Validation.

-> Gestion centralisée des exceptions.

-> Pagination et tri des résultats.

### Technologies utilisées

-> Java

-> Spring Boot

-> Spring Security

-> Spring Data JPA / Hibernate

-> MariaDB

-> JWT

-> Maven

-> Git / GitHub

### Architecture du projet

![Capture.PNG](../Capture.PNG)
### Prérequis

Avant de démarrer le projet, assurez-vous de disposer des outils suivants :

-> JDK 21 ou version compatible.
-> Maven.
-> MariaDB 13.0.2 ou version compatible.
-> Un IDE tel qu'IntelliJ IDEA, Eclipse ou VS Code.

### Installation

1-Cloner le dépôt :

git clone https://github.com/chimi-kamlo/commerce.git 

2-Accéder au projet :

cd ecommerce
### Configuration

L'application utilise des variables d'environnement pour configurer la connexion à la base de données et la clé secrète JWT.

Avant de démarrer l'application, configurez les variables suivantes :

-> DB_URL(URL de connexion à la base de données MariaDB)
-> DB_USERNAME(Nom d'utilisateur de la base de données)
-> DB_PASSWORD(Mot de passe de la base de données)
-> JWT_SECRET(Clé secrète utilisée pour signer les tokens JWT)
### comment configurer
 1- dans Dans IntelliJ IDEA aller à :
 Run → Edit Configurations → Environment variables
puis introduiser les variables suivante avec les valeurs correctes :
 -> DB_URL=jdbc:mariadb://localhost:3306/database_name
 -> DB_USERNAME=commerce_user
 -> DB_PASSWORD=ton_mot_de_passe
 -> JWT_SECRET=ta_cle_secrete
 Documentation API

### La documentation interactive des endpoints est disponible avec Swagger UI :

http://localhost:8080/swagger-ui/index.html

### Auteur
GitHub : https://github.com/chimi-kamlo

LinkedIn : https://www.linkedin.com/in/romaric-chimi

