/*
** APP4 Réseaux et protocoles, Été 2016
 * ClientUDP.c -- a datagram "client"
 * Pour Linux ou OS X
 * Ajouter des commentaires
 * Comparer les deux fichiers, corriger et compléter par conséquent
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

#define PORTSERVEUR "1024"	// port

int main(int argc, char *argv[])
{
	int sockfd;
	struct addrinfo hints, *servinfo, *p;
	int retVal;
	int numOctets;

	if (argc != 4) {
		fprintf(stderr,"usage: ClientUDP hote message\n");
		exit(1);
	}

	memset(&hints, 0, sizeof hints);
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_DGRAM;
	if ((retVal = getaddrinfo(argv[1], PORTSERVEUR, &hints, &servinfo)) != 0) {
		fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(retVal));
		return 1;
	}

	// ????
	for(p = servinfo; p != NULL; p = p->ai_next) {
		if ((sockfd = socket(p->ai_family, p->ai_socktype,
				p->ai_protocol)) == -1) {
			perror("ClientUDP: socket");
			continue;
		}

		break;
	}

	if (p == NULL) {
		fprintf(stderr, "ClientUDP: failed to bind socket\n");
		return 2;
	}
        //??????????????
	if ((numOctets = sendto(sockfd, argv[2], strlen(argv[2]), 0,
			 p->ai_addr, p->ai_addrlen)) == -1) {
		perror("ClientUDP: sendto");
		exit(1);
	}

	freeaddrinfo(servinfo);

	printf("ClientUDP: sent %d bytes to %s\n", numOctets, argv[1]);

	return 0;
}
