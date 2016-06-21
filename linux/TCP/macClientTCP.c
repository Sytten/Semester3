/*
 * APP4, Réseaux et protocoles de communicaitons, été 2016
** clientTCP.c -- stream socket client
 * Vérifier et modifier le code pour qu'il devienne interactif
 * permettant l'échange de messages dans les deux sens.
*/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/wait.h>
#include <signal.h>


#define PORT "3890" // PORT PAR DEFAUT

#define TAILLEMAX 13 // taille maximum d'un message

// retourne l'addresse ip de format ipv6 ou ipv4 selon sa version
void *get_in_addr(struct sockaddr *sa)
{
	if (sa->sa_family == AF_INET) {
		return &(((struct sockaddr_in*)sa)->sin_addr);
	}

	return &(((struct sockaddr_in6*)sa)->sin6_addr);
}

int main(int argc, char *argv[])
{
	int sockfd, numOctets;  // variable pour référencer le socket et pour compter le nombre d'octets lors d'une communication
	char buf[TAILLEMAX]; // buffer pour mettre les messages dedans
	struct addrinfo hints, *servinfo, *p; // structures décrivant de l'information sur des adresses
	int retVal;
	char s[INET6_ADDRSTRLEN]; // buffer pour l'adresse ip

    // regarder l'usage de la ligne de commande
	if (argc < 2) {
	    fprintf(stderr,"Usage: clientTCP hote [port]\n");
	    exit(1);
    }
    
    // assigner le port de la ligne de commande s'il a été spécifié
    if(argc != 3)
        argv[2] = PORT;
    
    printf("PORT : %s\n", argv[2]);


	memset(&hints, 0, sizeof hints);
	hints.ai_family = AF_UNSPEC; // permet IPv4 ou IPv6
	hints.ai_socktype = SOCK_STREAM; // socket de type TCP

    
	if ((retVal = getaddrinfo(argv[1], argv[2], &hints, &servinfo)) != 0) {
		fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(retVal));
		return 1;
	}

    
	// création du socket
	for(p = servinfo; p != NULL; p = p->ai_next) {
		if ((sockfd = socket(p->ai_family, p->ai_socktype,
				p->ai_protocol)) == -1) {
			perror("clientTCP: socket");
			continue;
		}
        
        // connection au serveur
		if (connect(sockfd, p->ai_addr, p->ai_addrlen) == -1) {
			close(sockfd);
			perror("clientTCP: connect");
			continue;
		}

		break;
	}

	if (p == NULL) {
		fprintf(stderr, "clientTCP: failed to connect\n");
		return 2;
	}

    // imprimer l'addresse ip au format texte
	inet_ntop(p->ai_family, get_in_addr((struct sockaddr *)p->ai_addr),
			s, sizeof s);
	printf("clientTCP: connecting to %s\n", s);

	freeaddrinfo(servinfo); // supprimer les information de l'addresse serveur
    
   do {
       // envoyer un message
        printf("enter a message: ");
        fgets(buf, sizeof(buf), stdin);
        if(strcmp(buf,"quit\n") == 0) {
            printf("EXITING\n");
            break;
        }
        if (send(sockfd, buf, strlen(buf), 0) == -1)
            perror("send");
       
      
        // recevoir un message
        printf("Waiting to receive\n");
        
        if ((numOctets = recv(sockfd, buf, TAILLEMAX-1, 0)) == -1) {
            perror("recv");
            exit(1);
        }

        buf[numOctets] = '\0';

        printf("clientTCP: received '%s'\n",buf);
        
       
        
   } while(1);
    
    close(sockfd); // fermer la connexion
	

	return 0;
}

