<h1 align="center"> CYnaspe </h1> <br>

Le but de cette application est de créer une interface graphique
permettant de générer puis de résoudre des labyrinthes.

## Sommaire

- [Introduction](#introduction)
- [Requirements](#requirements)
- [Guide de démarrage](#guide)
- [Utilisation](#utilisation)

## Introduction

Le projet CYnapse a pour objectif de concevoir une application graphique Java permettant de générer et résoudre des labyrinthes, en mettant en œuvre des algorithmes sur des graphes non orientés. L’application doit proposer différents modes de génération (parfait ou non), des résolutions par plusieurs algorithmes (DFS, BFS, A*, etc.), une visualisation dynamique, et des modifications locales du labyrinthe.
  
Ce projet vise à sensibiliser les étudiants à l'algorithmique avancée et à la conception d’interfaces en JavaFX, dans un cadre ludique, interactif et technique. Une version console permet de tester toutes les fonctionnalités indépendamment de l’interface graphique.  
  
L'application contient deux modes :

- **Console**  
  Dans ce mode, l'utilisateur peut créer, charger, résoudre et sauvegarder des labyrinthes via la console.  
  Lors de la sauvegarde, le fichier est stocké dans un dossier nommé `mazeOutputs`, qui est créé automatiquement s'il n'existe pas. Ce dossier se trouve à la racine du projet.

- **Interface Graphique**  
  Ce mode permet à l'utilisateur de réaliser les mêmes actions que dans la console, mais avec une vue visuelle du labyrinthe.  
  La génération est plus détaillée, notamment grâce à un mode pas à pas.


## Requirements

### JavaFX
Testée avec la version 21.0.7 <br>
https://gluonhq.com/products/javafx/

## Guide

### Installation
1. Cloner le repo
   ```sh
   git clone https://github.com/Wirbelwind03/CYnaspe.git
   ```
2. Aller dans le répertoire
   ```sh
   cd {current path}/cynaspe/cynaspe
   ```
4. Compiler le projet Java
   #### Window
  ```sh
  javac --module-path "{path to javafx}/lib" --add-modules javafx.controls,javafx.fxml -d out -sourcepath src src\Main.java
  ```
4. Executer le projet Java
   #### Window
  ```sh
  java --module-path "{path to javafx}/lib" --add-modules javafx.controls,javafx.fxml -cp out Main
  ```

## Utilisation

### Mode Console
Entrer l'argument 
```sh 
console
```

### Mode Interface Graphique
Par défault, ou entrer l'argument 
```sh 
ui
```
