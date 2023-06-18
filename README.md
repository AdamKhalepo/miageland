# Miage Land
Projet Spring Boot dans le cadre de la M1 MIAGE pour la matière Application d'Entreprise

## Comment lancer le projet ?
Après avoir cloné le projet, il vous suffit de lancer la commande suivante : 

```mvn spring-boot:run```

Une base de données H2 est intégré dans le code vous permettant d'embarquer la base de données directement dans le projet Spring Boot.

## Comment tester le projet ?
Actuellement, le projet ne dispose pas de front permettant de tester directement les fonctionnalités du back.

Néanmoins, [une collection sur Postman](https://www.postman.com/miageland/workspace/team-workspace/collection/27688876-4f9ef873-054d-4e42-a6c1-de05dc01d963?action=share&creator=27688876) est mise à disposition permettant de tester les différentes fonctionnalités du projet.


Il est également possible d'accéder à la base de données H2 (après avoir lancé le projet), en cliquant sur l'url suivante : 
https://localhost:8080/h2-console

## Améliorations futures
* Créer un projet front-end qui communique avec ce repository
* Rajouter de nouvelles fonctionnalités côté back-end (plus de statistiques, pouvoir avoir des types d'attraction, ...)
* Améliorer les fonctionnalités actuelles (intégrer Spring Security pour mieux gérer la connexion des utilisateurs, utiliser une BDD externe type image docker, ...)
* ...
