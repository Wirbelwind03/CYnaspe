<h1 align="center"> CYnaspe </h1> <br>

## Sommaire

- [Introduction](#introduction)
- [Guide de démarrage](#Guide)

## Introduction

Le projet CYnapse a pour objectif de concevoir une application graphique Java permettant de générer et résoudre des labyrinthes, en mettant en œuvre des algorithmes sur des graphes non orientés. L’application doit proposer différents modes de génération (parfait ou non), des résolutions par plusieurs algorithmes (DFS, BFS, A*, etc.), une visualisation dynamique, et des modifications locales du labyrinthe.
Ce projet vise à sensibiliser les étudiants à l'algorithmique avancée et à la conception d’interfaces en JavaFX, dans un cadre ludique, interactif et technique. Une version console permet de tester toutes les fonctionnalités indépendamment de l’interface graphique.

## Guide

### Installation
1. Cloner le repo
   ```sh
   git clone https://github.com/Wirbelwind03/CYnaspe.git
   ```
2. Aller dans le répertoire
3. Compiler le projet Java
   #### Window
  ```sh
  javac --module-path "{path to javafx}" --add-modules javafx.controls,javafx.fxml -d out -sourcepath src src\Main.java
  ```
4. Executer le projet Java
   #### Window
  ```sh
  java --module-path "{path to javafx}" --add-modules javafx.controls,javafx.fxml -cp out Main
  ```
