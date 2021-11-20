Polytech-Nice Sophia SI3, 
Projet semestre 5 : Takenoko 
Groupe GigaBoss : 
	Eric Naud, 
	Loic Madern, 
	Enzo Manuel, 
	Antoine Le Calloch

Le programme se lance avec la commande suivante:
mvn clean package
mvn exec:java 


Comportement du programme:

La simulation du jeu se fait par l’instanciation d’un objet Game qui possède une liste de bots en argument. 
Le nombre de joueurs peut varier de 1 à 4. 

La sortie standard affiche le pourcentage de victoire, de défaite et d’égalité ainsi que le score moyen pour chaque bot, pour un total de 1000 parties.
On peut aussi activer l'affichage du detail du jeu à chaque tour.


Deux parties se lancent automatiquement : 

La première partie est composé d'un Parcel Bot, un Panda Bot, d'un Intelligent Bot et d'un Random Bot
La deuxième partie est composé de deux Intelligent Bot

Ces parties n'affichent pas les détails du jeu.

Les tests couvrent 91% des lignes de code.




















