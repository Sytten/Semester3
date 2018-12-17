/*
 * APP4, Réseaux et protocoles de communicaitons, été 2016
** server.c -- stream socket server 
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

#define PORT "3890"  // le port d'écoute

#define BACKLOG 10	 //  longueur de la queue des connexions en attente.
#define BUFFER_LENGTH  1024

void sigchld_handler(int s)
{
	while(waitpid(-1, NULL, WNOHANG) > 0);
}

// retourne l'addresse ip en détectant son format ipv4 ou ipv6
void *get_in_addr(struct sockaddr *sa)
{
	if (sa->sa_family == AF_INET) {
		return &(((struct sockaddr_in*)sa)->sin_addr);
	}

	return &(((struct sockaddr_in6*)sa)->sin6_addr);
}

int main(void)
{
	int sockfd, new_fd;  // declaration de variables pour référencer deux sockets
	struct addrinfo hints, *servinfo, *p; // structures d'information sur les adresses
	struct sockaddr_storage their_addr; // structure de storage d'information sur des clients/serveurs
	socklen_t sin_size; // longeur d'un socket
	struct sigaction sa; // un sigaction examine le changement d'un signal
	int yes=1;
	char s[INET6_ADDRSTRLEN]; 
	int rv;
    char buf[BUFFER_LENGTH];
    int numbytes;

	memset(&hints, 0, sizeof hints);
	hints.ai_family = AF_INET; // ipv4
	hints.ai_socktype = SOCK_STREAM; // connexion de type TCP
	hints.ai_flags = AI_PASSIVE; // identifier le socket comme un socket qui peut executer des fonctions spécifique au serveur comme connect()
    
	if ((rv = getaddrinfo(NULL, PORT, &hints, &servinfo)) != 0) {
		fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
		return 1;
	}

	// créer le socket
	for(p = servinfo; p != NULL; p = p->ai_next) {
		if ((sockfd = socket(p->ai_family, p->ai_socktype,
				p->ai_protocol)) == -1) {
			perror("serverTPC: socket");
			continue;
		}
        
        // set les options du socket
		if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &yes,
				sizeof(int)) == -1) {
			perror("setsockopt");
			exit(1);
		}

        // binding du socket
		if (bind(sockfd, p->ai_addr, p->ai_addrlen) == -1) {
			close(sockfd);
			perror("serverTPC: bind");
			continue;
		}

		break;
	}

	if (p == NULL)  {
		fprintf(stderr, "serverTPC: failed to bind\n");
		return 2;
	}

	freeaddrinfo(servinfo); // suprimer les informations dans la structure d'informatin serveur
    
    // ecouter sur le socket
	if (listen(sockfd, BACKLOG) == -1) {
		perror("listen");
		exit(1);
	}

    // paramétrage des signaux
	sa.sa_handler = sigchld_handler; 
	sigemptyset(&sa.sa_mask);
	sa.sa_flags = SA_RESTART;
	if (sigaction(SIGCHLD, &sa, NULL) == -1) {
		perror("sigaction");
		exit(1);
	}

    while (1) {
	
        printf("serverTPC: waiting for connections...\n");
        // attendre une connexion
		sin_size = sizeof their_addr;
		new_fd = accept(sockfd, (struct sockaddr *)&their_addr, &sin_size);
		if (new_fd == -1) {
			perror("accept");
            exit(0);
		}

        // imprimer a l'écran de qui vient la connexion
		inet_ntop(their_addr.ss_family,
			get_in_addr((struct sockaddr *)&their_addr),
			s, sizeof s);
		printf("serverTPC: got connection from %s\n", s);
    
    do {  // en attente de réception d'un message
        if ((numbytes = recv(new_fd, buf, BUFFER_LENGTH-1, 0)) == -1) {
            perror("recv");
            exit(1);
        }
        
        // imprimer le message à l'écran lors de sa réception
        buf[numbytes] = '\0';
        printf("client: received '%s'\n",buf);
            
		if (numbytes != 0) { // envoyer un message de retour
            printf("Entrez un message de retour : ");
            fgets(buf, sizeof(buf), stdin);
			if (send(new_fd, buf, strlen(buf), 0) == -1)
				perror("send");

		}
		
    } while(numbytes != 0);
        
    // fermer le socket 
    close(new_fd);
    
        
    }

	return 0;
}

