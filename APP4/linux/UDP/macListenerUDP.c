/*
* APP4 R�seaux et protocoles, �t� 2016
 *  listenerUDP.c -- a datagram sockets "server"
 *  Pour Linux ou OS X
 * Comparer les deux fichiers, corriger et compl�ter par cons�quent
*/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>

#define MONPORT "1024"	// le port to listen est 1024

#define MAXBUFLEN 25

// retourne addresse en ipv4 ou ipv6
void *get_in_addr(struct sockaddr *sa)
{
	if (sa->sa_family == AF_INET) {
		return &(((struct sockaddr_in*)sa)->sin_addr);
	}

	return &(((struct sockaddr_in6*)sa)->sin6_addr);
}

int main(void)
{
	int sockfd; // declaration de variables pour r�f�rencer un socket
	struct addrinfo hints, *servinfo, *p; // structures d'information sur les adresses
	int retVal;
	int numbytes;
    int numbytes2;
    struct sockaddr_storage their_addr;// structure de storage d'information sur des clients/serveurs
	char buf[MAXBUFLEN];
	socklen_t addr_len;
	char s[INET6_ADDRSTRLEN];

	memset(&hints, 0, sizeof hints);
	hints.ai_family = AF_INET; // ipv6 ou ipv4
	hints.ai_socktype = SOCK_DGRAM;  // UDP
    hints.ai_flags = AI_PASSIVE; // identifier le socket comme un socket qui peut executer des fonctions sp�cifique au serveur comme connect()

	if ((retVal = getaddrinfo(NULL, MONPORT, &hints, &servinfo)) != 0) {
		fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(retVal));
		return 1;
	}

	// cr�er le socket
	for(p = servinfo; p != NULL; p = p->ai_next) {
		if ((sockfd = socket(p->ai_family, p->ai_socktype,
				p->ai_protocol)) == -1) {
			perror("listenerUDP: socket");
			continue;
		}

        // binding du socket
		if (bind(sockfd, p->ai_addr, p->ai_addrlen) == -1) {
			close(sockfd);
			perror("listenerUDP: bind");
			continue;
		}

		break;
	}

	if (p == NULL) {
		fprintf(stderr, "listenerUDP: failed to bind socket\n");
		return 2;
	}

    freeaddrinfo(servinfo); // suprimer les informations dans la structure d'informatin serveur

	
    while(1) {
        // en attente de r�ception du message
        printf("listenerUDP: waiting to recvfrom...\n");
        addr_len = sizeof their_addr;
        if ((numbytes = recvfrom(sockfd, buf, MAXBUFLEN-1 , 0,
                                 (struct sockaddr *)&their_addr, &addr_len)) == -1) {
            perror("recvfrom");
            exit(1); 
        }
        
       
        // imprimer � l'�cran de l'information pertinante sur l'inf
        printf("listenerUDP: got packet from %s\n",
               inet_ntop(their_addr.ss_family,
                         get_in_addr((struct sockaddr *)&their_addr),
                         s, sizeof s));
        printf("listenerUDP: packet is %d bytes long\n", numbytes);
        buf[numbytes] = '\0';
        printf("listenerUDP: packet contains \"%s\"\n", buf);
        
        // envoyer un message
        printf("enter a message : \n");
       
        gets(buf);
        if ((numbytes2 = sendto(sockfd, buf, strlen(buf), 0,
                                (struct sockaddr *)&their_addr, addr_len)) == -1) {
            perror("listenUDP: sendto");
            exit(1);
        }
    }

	return 0;
}
