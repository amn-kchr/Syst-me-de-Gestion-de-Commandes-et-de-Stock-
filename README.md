# Projet POO : Système de Gestion de Commandes et de Stock (Client-Serveur)

## Présentation

Ce projet a été réalisé dans le cadre d’un module de Programmation Orientée Objet (POO) en Java. Il met en œuvre une architecture client-serveur permettant de simuler la gestion d’un stock de produits, la réception de commandes par des clients, et le traitement de celles-ci côté serveur.

L’objectif est de consolider la maîtrise des concepts fondamentaux de la POO (encapsulation, héritage, exceptions, etc.) et d’introduire la communication réseau entre entités distribuées.

## Objectifs pédagogiques

* Concevoir une architecture logicielle modulaire
* Implémenter des classes métiers en Java
* Gérer les erreurs à l'aide d'exceptions personnalisées
* Établir une communication réseau simple client-serveur
* Manipuler des flux de données (I/O) et la sérialisation

## Architecture du projet

```
ProjetPOO/
├── Chariot.java                    # Classe représentant un panier de commande
├── ClientHandler.java              # Gestionnaire côté serveur pour chaque client connecté
├── Commande.java                   # Structure de commande (produits, quantités)
├── Produit.java                    # Entité représentant un produit en stock
├── GestionnaireStockClient.java   # Interface client (émission de commandes)
├── GestionnaireStockServer.java   # Serveur central recevant et traitant les commandes
├── InvalidOrderException.java     # Exception levée pour commande invalide
├── StockUnavailableException.java # Exception levée si le stock est insuffisant
├── Les commandes.txt              # Fichier texte d’exemples de commandes clients
```

## Fonctionnement

* Le client prépare une commande via un `Chariot`.
* Il l’envoie au serveur via `GestionnaireStockClient`.
* Le serveur, via `GestionnaireStockServer`, traite la commande et vérifie la disponibilité des produits.
* En cas de problème, des exceptions personnalisées sont levées (`InvalidOrderException`, `StockUnavailableException`).

## Lancement de l'application

### Compilation

Assurez-vous d’avoir Java installé (JDK 8 ou supérieur) :

```bash
javac *.java
```

### Exécution du serveur

```bash
java GestionnaireStockServer
```

### Exécution du client

Dans un autre terminal :

```bash
java GestionnaireStockClient
```

## Auteurs

* À compléter avec vos noms et prénoms

## Documentation supplémentaire

* `Les commandes.txt` : Exemples de commandes pouvant être utilisées pour tester l'application.

## Auteur

[KECHIR Amine](https://github.com/amn-kchr)

---

> Ce projet constitue un exercice complet d’application des principes orientés objet dans un contexte client-serveur. Il est adapté pour une initiation à la programmation réseau et à la modélisation métier logicielle.
