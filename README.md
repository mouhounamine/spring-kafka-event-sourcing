# E-commerce Microservices System

Ce projet implémente une architecture microservices pour un système de gestion de commandes e-commerce utilisant Spring Boot, Apache Kafka et MongoDB. L'architecture permet le suivi du cycle de vie complet d'une commande, depuis sa création jusqu'à sa livraison.

## Architecture du système

Le système est composé de microservices interagissant via Apache Kafka pour assurer une communication asynchrone et découplée:

```
┌───────────────┐                 ┌───────┐                 ┌────────────────┐
│               │      publie     │       │     écoute      │                │
│ ORDER-SERVICE │ ──────────────► │ Kafka │ ◄─────────────  │ SHIPPING-SERVICE │
│               │                 │       │                 │                │
└───────┬───────┘                 └───────┘                 └────────┬───────┘
        │                                                           │
        │                                                           │
        │                                                           │
        ▼                                                           ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                             │
│                                  MongoDB                                    │
│                                                                             │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │                          Collection: orders                           │  │
│  │                                                                       │  │
│  │  ID        ORDER_ID        DATE_TIME             STATUS              │  │
│  │  1         abcdr24          27-02-24T10:14:12     CREATED             │  │
│  │  2         abcdr24          27-02-24T10:30:10     CONFIRMED           │  │
│  │  3         abcdr24          27-02-24T11:50:36     SHIPPED             │  │
│  │  4         abcdr24          27-02-26T08:00:16     DELIVERED           │  │
│  │                                                                       │  │
│  └───────────────────────────────────────────────────────────────────────┘  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Fonctionnalités

- **Gestion du cycle de vie des commandes** avec quatre états principaux:
    - `CREATED`: Commande initialement créée
    - `CONFIRMED`: Commande confirmée et prête pour expédition
    - `SHIPPED`: Commande expédiée au client
    - `DELIVERED`: Commande livrée au client

- **Communication asynchrone** entre services via Kafka
- **Persistance des données** dans MongoDB avec traçabilité complète
- **Architecture événementielle** permettant une évolutivité horizontale

## Composants du système

### 1. Order Service
- Gère la création et la mise à jour des commandes
- Publie des événements vers Kafka lors des changements d'état
- Stocke les informations de commande dans MongoDB

### 2. Shipping Service
- Traite les commandes confirmées
- Met à jour le statut d'expédition et de livraison
- Publie des événements de retour vers Kafka pour notifier le service des commandes

### 3. Kafka
- Broker de messages assurant la communication entre services
- Garantit la livraison des événements même en cas de panne temporaire

### 4. MongoDB
- Base de données NoSQL stockant l'historique complet des commandes
- Permet des requêtes flexibles et une scalabilité horizontale

## Installation et démarrage

### Prérequis
- Java 17+
- Apache Kafka 3.x
- MongoDB atlas
- Maven 3.8+
