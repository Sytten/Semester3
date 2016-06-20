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
#define MAXBUFLEN 25

int main(int argc, char *argv[])
{
	int sockfd;
	struct addrinfo hints, *servinfo, *p;
	int retVal;
	int numOctets;
    int numbytes;
    struct sockaddr_storage their_addr;
    socklen_t addr_len;
    char buf[MAXBUFLEN];
    char s[INET6_ADDRSTRLEN];

	if (argc < 2) {
		fprintf(stderr,"usage: ClientUDP hote [port]\n");
		exit(1);
	}
    if(argc != 3)
        argv[2] = PORTSERVEUR;
    
    printf("PORT : %s\n", argv[2]);

	memset(&hints, 0, sizeof hints);
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_DGRAM;
	if ((retVal = getaddrinfo(argv[1], argv[2], &hints, &servinfo)) != 0) {
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
    do {
        // send a message
        printf("enter a message : \n");
        gets(buf);
        if ((numOctets = sendto(sockfd, buf, strlen(buf), 0,
                                p->ai_addr, p->ai_addrlen)) == -1) {
            perror("ClientUDP: sendto");
            exit(1);
        }
        
        // receiving message
        printf("listenerUDP: waiting to recvfrom...\n");
        addr_len = sizeof their_addr;
        if ((numbytes = recvfrom(sockfd, buf, MAXBUFLEN-1 , 0, (struct sockaddr *)&their_addr,
                                 &addr_len)) == -1) {
            perror("recvfrom");
            exit(1);
        }
        
        
        
        printf("listenerUDP: packet is %d bytes long\n", numbytes);
        buf[numbytes] = '\0';
        printf("listenerUDP: packet contains \"%s\"\n", buf);
        
    }while(strcmp(buf,"quit") != 0);

	freeaddrinfo(servinfo);

	printf("ClientUDP: sent %d bytes to %s\n", numOctets, argv[1]);

	return 0;
}
