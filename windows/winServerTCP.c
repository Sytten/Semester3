////////////////////////////////////////////////////////////////////////////////
//
//		Fichier:  Sockets server.c
//
////////////////////////////////////////////////////////////////////////////////


#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <winsock.h>

#define BUFFER_LENGTH  1024
#define END_OF_STRING  '\0'

#pragma comment(lib, "Ws2_32.lib")

main(argc, argv)
int argc;
char *argv[];

{	int sock, msgsock;
	int length;
	int rval;
	char buf[BUFFER_LENGTH];
	struct sockaddr_in server;
	struct sockaddr_in client;
	struct hostent *cname;
	int port;

	WSADATA wsadata;
	WORD version = (1 << 8) + 1;  /* Version 1.1 de WINSOCK*/

	if (argc > 2)
	{	fprintf(stderr,"Usage: %s [portnumber]\n", argv[0]);
		exit(1);
	}


	/* Initialisation de la librairie, les détails seront stockés dans wsadata */

	if (WSAStartup(version, &wsadata) != 0)    
	{	perror("lors de l'initialisation winsock");
		exit(1);
	}
	
	/* Creation d'un socket de type TCP */
	if ( (sock = socket(AF_INET, SOCK_STREAM, 0)) < 0)
	{	perror("lors de l'ouverture du socket");
		exit(1);
	}


	server.sin_family = AF_INET;    /* Type d'IP du serveur IPV4 */
	server.sin_addr.s_addr = INADDR_ANY; /* IP locale */
	if (argc == 2)  /* Nombre d'arguments passés au programme est égal à 2 */
		port = htons((short) atoi(argv[1]));  /* Transforme le string en port valide */
	else
		port = 0;  /* Port par default est 0 */
	server.sin_port = port; /* Assignation du port du server */
	if (bind(sock, (struct sockaddr *)&server, sizeof(server)) < 0) /* Associer adresse du serveur au socket */
	{	perror("lors de l'association au numero de port");
		exit(1);
	}

	/* Verification de l'assignation */

	length = sizeof(server);
	if (getsockname(sock, (struct sockaddr *)&server, &length) < 0) /* Tentative de retrouver le sockaddr du socket */
	{	perror("lors de l'interrogation sur le socket");
		exit(1);
	}
	printf("Le port %d a ete associe au socket\n", ntohs(server.sin_port));

	/* Commencer a ecouter sur le port specifie */
	listen(sock, 5);

	/* Boucle d'opération */
	while (1)  /* Boucle infinie */
	{	length = sizeof(client);
		msgsock = accept(sock, (struct sockaddr *) &client, &length); /* Call bloquant jusqu'a l'arrivee d'une connection */
		cname = gethostbyaddr((char *) &client.sin_addr, 4, AF_INET); /* Retrouver des informations a propos du client */
		if (cname == NULL)
			printf("Connection d'une machine inconnue (%s), ",
				inet_ntoa(client.sin_addr));
		else
			printf("Connection de la machine %s (%s), ", cname->h_name, /* Imprimer a l'ecran le nom du client connecte */
				inet_ntoa(client.sin_addr));
		printf("au port %d\n", client.sin_port); /* Imprimer a l'ecran le numero de port */

		if (msgsock < 0)
		{	perror("lors de l'acceptation"); /* Message d'erreur systeme */
		}

		/* Reception du message */
		else do  /* Boucle de reception */
		{	if ((rval = recv(msgsock, buf, sizeof(buf), 0)) < 0)
			{	perror("lors de la lecture du message");
			}
			if (rval == 0)
				printf("Fin de connection\n\n");
			else
			{	buf[rval] = END_OF_STRING;
				printf("Recu: '%s'\n", buf);
				printf("Entrer les caracteres+retour de chariot pour envoyer, ou juste retour de chariot pour terminer:\n ");
				gets_s(buf, BUFFER_LENGTH);
				length = strlen(buf);
				if (length == 0)  break;
				if (send(msgsock, buf, length, 0) < 0) /* Envoyer le message de retour au client */
					{	perror("lors de l'ecriture dans le socket");
						exit(1);
					}
			}
		} while (rval != 0);


		closesocket(msgsock); /* Fermer le socket connecte au client */


	} 

	closesocket(sock); /* Fermer le socket du serveur */
	WSACleanup(); /* Terminer correctement la librairie */

	return 0;
}  