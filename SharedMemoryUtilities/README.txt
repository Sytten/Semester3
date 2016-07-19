Pour utiliser le package "SharedMemoryUtilities" dans l'environnement centOS (Linux):

A- Creation de la librarie "libSharedMemory.so"
===============================================

A1- Depuis une console, rendez vous dans le dossier "SharedMemoryUtilities"
A2- Executer la commande:   
       make 
        
Le fichier "Makefile" est pr�par� pour creer la librairie partag�e "libSharedMemory.so" dans l'environnement linux "CentOS" 


B- Exemple d'utilisation de la librarie "libSharedMemory.so" � l'aide de la console
===================================================================================

B1- Depuis une console, rendez vous dans le dossier "SharedMemoryUtilities" et compilez-z tous les programmes .java 
    en ex�cutant la commande 
       javac *.java

B2- Definissez ensuite la variable d'environnement LD_LIBRARY_PATH: ex�cutez dans la console la commande 
       export LD_LIBRARY_PATH=./SharedMemoryUtilities

B3- Pour executez le programme qui va creer un espace de memoire partagee: 
    
    Placez vous dans le r�pertoire parent contenant  SharedMemoryUtilities 
    Executez la commande
       java SharedMemoryUtilities/SharedMemoryCreateBuffer
	
    Le programme affichera l'adresse XXXX o� la m�moire partag�e a �t� plac�e.

	
B4- Pour �crire dans la m�moire partag�e, ouvrez une autre fen�tre de connexion (console):
    D�finissez de nouveau la variable d'environnement LD_LIBRARY_PATH (voir point B2) 
    Assurez vous d'�tre dans le r�pertoire parent contenant SharedMemoryUtilities 
    Ex�cutez la commande 
       java SharedMemoryUtilities/SharedMemoryWrite XXXX
    XXXX �tant l'adresse indiqu�e par le programme SharedMemoryCreateBuffer.

B5- Pour lire dans la m�moire partag�e, 
    Ouvrez de nouveau une fen�tre console et d�finissez-y la variable d'environnement LD_LIBRARY_PATH (voir point B2) 
    Ex�cutez la commande  
        java SharedMemoryUtilities/SharedMemoryRead XXXX
    XXXX �tant l'adresse indiqu�e par le programme SharedMemoryCreateBuffer




