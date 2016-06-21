////////////////////////////////////////////////////////////////////////////////
//
//		Fichier:  socket client.c
// Compléter le code avant de l'utiliser
//
////////////////////////////////////////////////////////////////////////////////


#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <winsock.h>

#define BUFFER_LENGTH  1025
#define TEXT_LENGH	   1025
#define END_OF_STRING  '\0'

#pragma comment(lib, "Ws2_32.lib")

main(argc, argv)  int argc; char *argv[];

{	int sock;
	struct sockaddr_in server;
	struct hostent *hp;
	char buf[BUFFER_LENGTH];
	int len;
	int rval;
	char text[TEXT_LENGH]="Bonjour";

	WSADATA wsadata;
	WORD version = (1 << 8) + 1;  /* Version 1.1 */


	if (argc != 1)
	{	fprintf(stderr,"%s manque d'arguments\n", argv[0]);
		exit(1);
	}

	/* initialisation de Winsock */
	if (WSAStartup(version, &wsadata) != 0)
	{
		perror("lors de l'initialisation winsock");
		exit(1);
	}

	/* Création d'un socket de communication  */
	if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0)
	{
		perror("lors de l'ouverture du socket");
		exit(1);
	}

	server.sin_family = AF_INET;  
	printf("Entrer le nom de la machine distante: "); 
	gets_s(buf, BUFFER_LENGTH);
	hp = gethostbyname(buf);      
	if (hp == 0)
	{	fprintf(stderr, "%s: machine inconnue\n", buf);
		exit(1);
	}
	memcpy((char *)&server.sin_addr, (char *)hp->h_addr, hp->h_length);  
	printf("Entrer le numero de port dans la machine distante: ");   
	gets_s(buf, BUFFER_LENGTH);
	server.sin_port = htons((short) atoi(buf)); 

    /*  A faire: connection au serveur  */
	if (connect(sock, (struct sockaddr *) &server, sizeof(server)) != 0) 
	{
		perror("lors de la connection au serveur");
		exit(1);
	}
	
	do {
		printf("Entrer le message ‡ envoyer: ");
		gets_s(text, TEXT_LENGH);

		len = strlen(text);
		if (len != 0)  {
			if (send(sock, text, len, 0) < 0)
			{
				perror("lors de l'ecriture dans le socket");
				exit(1);
			}
		}

		if (strcmp(text, "quit") == 0)
			break;
		
		if ((rval = recv(sock, text, len, 0)) < 0)
		{
			perror("lors de la lecture du message");
		}

		if (rval != 0)
		{
			text[rval] = END_OF_STRING;
			printf("Recu: '%s'\n", text);
		}
	} while (rval != 0);

	printf("Fin de connection\n\n");
	gets_s(text, TEXT_LENGH);
	closesocket(sock);
	WSACleanup();

	return 0;
}  
