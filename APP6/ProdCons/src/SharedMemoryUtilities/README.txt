Pour utiliser le package "SharedMemoryUtilities" dans l'environnement centOS (Linux):

A- Creation de la librarie "libSharedMemory.so"
===============================================

A1- Depuis une console, rendez vous dans le dossier "SharedMemoryUtilities"
A2- Executer la commande:   
       make 
        
Le fichier "Makefile" est préparé pour creer la librairie partagée "libSharedMemory.so" dans l'environnement linux "CentOS" 


B- Exemple d'utilisation de la librarie "libSharedMemory.so" à l'aide de la console
===================================================================================

B1- Depuis une console, rendez vous dans le dossier "SharedMemoryUtilities" et compilez-z tous les programmes .java 
    en exécutant la commande 
       javac *.java

B2- Definissez ensuite la variable d'environnement LD_LIBRARY_PATH: exécutez dans la console la commande 
       export LD_LIBRARY_PATH=./SharedMemoryUtilities

B3- Pour executez le programme qui va creer un espace de memoire partagee: 
    
    Placez vous dans le répertoire parent contenant  SharedMemoryUtilities 
    Executez la commande
       java SharedMemoryUtilities/SharedMemoryCreateBuffer
	
    Le programme affichera l'adresse XXXX où la mémoire partagée a été placée.

	
B4- Pour écrire dans la mémoire partagée, ouvrez une autre fenêtre de connexion (console):
    Définissez de nouveau la variable d'environnement LD_LIBRARY_PATH (voir point B2) 
    Assurez vous d'être dans le répertoire parent contenant SharedMemoryUtilities 
    Exécutez la commande 
       java SharedMemoryUtilities/SharedMemoryWrite XXXX
    XXXX étant l'adresse indiquée par le programme SharedMemoryCreateBuffer.

B5- Pour lire dans la mémoire partagée, 
    Ouvrez de nouveau une fenêtre console et définissez-y la variable d'environnement LD_LIBRARY_PATH (voir point B2) 
    Exécutez la commande  
        java SharedMemoryUtilities/SharedMemoryRead XXXX
    XXXX étant l'adresse indiquée par le programme SharedMemoryCreateBuffer




