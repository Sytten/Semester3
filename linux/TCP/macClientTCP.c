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


#define PORT "3890"//???????

#define TAILLEMAX 13 // ?????????????

// ?????????????????????
void *get_in_addr(struct sockaddr *sa)
{
	if (sa->sa_family == AF_INET) {
		return &(((struct sockaddr_in*)sa)->sin_addr);
	}

	return &(((struct sockaddr_in6*)sa)->sin6_addr);
}

int main(int argc, char *argv[])
{
	int sockfd, numOctets;  
	char buf[TAILLEMAX];
	struct addrinfo hints, *servinfo, *p;
	int retVal;
	char s[INET6_ADDRSTRLEN];

	if (argc < 3) {
	    fprintf(stderr,"Usage: clientTCP hote message\n");
	    exit(1);
	}

	memset(&hints, 0, sizeof hints);
	hints.ai_family = AF_UNSPEC;
	hints.ai_socktype = SOCK_STREAM;

	if ((retVal = getaddrinfo(argv[1], PORT, &hints, &servinfo)) != 0) {
		fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(retVal));
		return 1;
	}

	// ??????????????????????
	for(p = servinfo; p != NULL; p = p->ai_next) {
		if ((sockfd = socket(p->ai_family, p->ai_socktype,
				p->ai_protocol)) == -1) {
			perror("clientTCP: socket");
			continue;
		}

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

	inet_ntop(p->ai_family, get_in_addr((struct sockaddr *)p->ai_addr),
			s, sizeof s);
	printf("clientTCP: connecting to %s\n", s);

	freeaddrinfo(servinfo); // ????

    if (send(sockfd, argv[2], strlen(argv[2]), 0) == -1)
        perror("send");
    
	if ((numOctets = recv(sockfd, buf, TAILLEMAX-1, 0)) == -1) {
	    perror("recv");
	    exit(1);
	}

	buf[numOctets] = '\0';

	printf("clientTCP: received '%s'\n",buf);

	close(sockfd);

	return 0;
}

