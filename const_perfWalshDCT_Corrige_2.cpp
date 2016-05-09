/* perfWalshDCT.cpp                                                         */
/* -------------------------------------------------------------------------*/
/* Evaluation de performance des algorihtmes de transformee de DCT et Walsh */
/* D.Dalle APP No 1 S3 g. info, 24 aout 2015                                */
/* VERSION 5.00a                                                            */
/* Revision par Amer Al-Canaan, avril 2016                                  */
/* -------------------------------------------------------------------------*/
/* References (Walsh):                                                      */
/* A. D. Poularikas, S. Seely "Signals and Systems"                         */
/* (c) PWS-Kent Publishing Company 1991                                     */
/* Quelques fonctions d'evaluation du temps par Al Aburto                   */ 
/* http://home.iae.nl/users/mhx/fft.c                                       */
/*                                                                          */
/*--------------------------------------------------------------------------*/ 
/* Compilation (optimisation O0 par exemple)                                */
/* Sur UNIX, LINUX debian ou OS X:                                          */
/* avec mesure du temps du processus getrusage:                             */
/*   g++  perfWalshDCT.cpp -o perfWalshDCT -O0 -D UNIX -lm                  */
/*   (ou gcc)                                                               */
/* avec mesure du temps selon mesure du temps gettimeofday (moins precis):  */
/*   gcc  perfWalshDCT.cpp -o perfWalshDCT -O0 -D UNIXTIMEDAY -lm           */
/*                                                                          */
/*--------------------------------------------------------------------------*/

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <time.h>

// Parametre d'evaluation de performance (adapter au besoin selon la machine)
// Le programme cesse les itérations à l'occurence de l'une des deux possibilités:
//          occurence du nombre maximum d'itération
//    ou    occurence du temps d'exécution maximum imposé
//
  const int NwalMax = 10000; // Nombre de fois maximum que le calcul (Walsh) est repete pour
                             // produire un temps significatif
  const int NdctMax = 10000; // Nombre de fois maximum que le calcul (DCT) est repete pour
                             // produire un temps significatif
  const double Tmax = 1.0;   // temps maximum pour une serie d'iterations

// constantes et parametres
    const  int kr = 3;         // constante kr avec n = 2**kr  CHANGER AU BESOIN
                             // exemple: avec kr = 6: dimension nxn de D: 64x64 = 4096
    const int increment = 100;

  const  int dim_max_2D = 4096;
  const  int dim_max_1D = 64;
                             // constante pi  http://pi.lacim.uqam.ca/piDATA/pi.html
  const double pi = 3.1415926535897932384626433832795028841971;                          

  // Structure pour passer les parametres de chronometrage
  // Cette structure permet de grouper et de passer plusieurs variable par référence
  struct ParamChrono { 
      int niterWbase;         // nombre d'itérations du calcul de la transformée de Walsh
      int niterWopt1;         // nombre d'itérations du calcul de la transformée de Walsh
      int niterDCT;           // nombre d'itérations du calcul de la transformée DCT
      //
      double runtimeWbase;    // temps d'exécution du calcul de la transformée de Walsh
      double runtimeWopt1;    // temps d'exécution du calcul de la transformée de Walsh
      double runtimeDCT;      // temps d'exécution du calcul de la transformée DCT
      //
      char la_date[0x100];
  };


/****************************************************/
/*  Windows NT (32 bit) utime() routine             */
/*  Provided by: Piers Haken, piersh@microsoft.com  */
/****************************************************/
// fonctions utilitaires de mesure du temps
#ifdef WIN32
#include <windows.h>

double utime(void)
{
 double q;
 q = (double)GetTickCount() * 1.0e-03;
 return q;
}

void ladate_c(char* strdate);
void ladate_c(char* strdate)
{
    time_t t;      // strutures requises a la lecture
                   // de la date et de l'heure systeme
    time(&t);
    char str[0x100];
    if(!ctime_s(str, 0x100, &t))
        strcpy_s (strdate,0x100, str);
    else
        strcpy_s (strdate, 13, "erreur ctime_s");
    return;
}
#endif

#if defined( UNIX) || defined (__APPLE__)
#include <sys/time.h>
#include <sys/resource.h>
struct rusage rusage;

double utime();
double utime()
{
 double q;
 getrusage(RUSAGE_SELF,&rusage);
 q = (double)(rusage.ru_utime.tv_sec);
 q = q + (double)(rusage.ru_utime.tv_usec) * 1.0e-06;	
 return q;
}

void ladate_c(char* strdate);
void ladate_c(char* strdate)
{
    time_t t;        // strutures requises a la lecture
                     // de la date et de l'heure systeme
    time(&t);
    strcpy(strdate, ctime (&t));
    return;
}


#endif

#ifdef UNIXTIMEDAY
// si la fonction getrusage donnait un resultat
// incorrect, alors on peut utiliser
// cette version avec gettimeofday qui mesure le temps ecoule total
// verifier (avec la commande top par exemple) que le programme consomme
// presque 100 % du temps (un test montre  > 97 %)
// D. Dalle aout 2010
#include <sys/time.h>
#include <sys/resource.h>
double utime()
{
 struct timeval letemps;    
 double secondes;
 gettimeofday(&letemps, NULL);
 secondes = (double) letemps.tv_sec + 1.0e-06 * (double) letemps.tv_usec;    
 return secondes;
}
#endif

#ifdef UNIX2
#include <sys/time.h>
#include <sys/resource.h>
double utime()
{
 struct rusage rusage;
 struct timeval letemps;    
 double q;
 double secondes;
 getrusage(RUSAGE_SELF,&rusage);
 q = (double)(rusage.ru_utime.tv_sec);
 q = q + (double)(rusage.ru_utime.tv_usec) * 1.0e-06;	
 //printf ("utime trace: q = %12.6f, \n", q) ;
 gettimeofday(&letemps, NULL);
 secondes = (double) letemps.tv_sec + 1.0e-06 * (double) letemps.tv_usec;    
 printf ("utime UNIX2 trace: q = %12.6f, secondes = %12.6f, \n", q, secondes) ;    
 return q;
//return secondes;
}

double d2time()
{
 struct timeval letemps;    
 double secondes;
 gettimeofday(&letemps, NULL);
 secondes = (double) letemps.tv_sec + 1.0e-06 * (double) letemps.tv_usec;    
 if (verbose) printf ("d2time trace:  secondes = %12.6f, \n",  secondes) ;    
 return secondes;
}
#endif


// génération de nombres pseudo-aléatoires
// generateur de variables 32 bits
int genrand_v32 (int x);
int genrand_v32 (int x) 
{
    // ref Rosen exemple 19 p 114 chap. 2
    const int m = 0x7FFFFFFF; // 2**31-1
    const int a = 16807; // 7**5
    const int c = 0;
    return (a*x + c ) % m;
};


// genere des variables entieres de valeurs de 16 bits significatifs
// a partir d'un generateur de variables 32 bits
void genere_ran_D (int D[], int r, int &s);
void genere_ran_D (int D[], int r, int &s)
{
	int bla = 1;
     int n;
     int u, v, uv;
     n = (1 << r);
     for (v = 0; v < n ; v++)
       for (u = 0; u < n ; u++) {
           uv = (v << r) + u;  // uv = v * n  + u;
           //s = genrand_v32(s);
           D[uv] = bla;//s >> 16;   //16 bits significatifs
		 	bla += increment;
           }
     return;
};

// genere des variables reelles format flottant de valeurs de 16 bits significatifs
// a partir d'un generateur de variables 32 bits
void genere_ran_double_D (double D[], int r, int &s);
void genere_ran_double_D (double D[], int r, int &s)
{
     int n;
     int u, v, uv;
     n = (1 << r);
     for (v = 0; v < n ; v++)
       for (u = 0; u < n ; u++)
       {
           uv = (v << r) + u;  // uv = v * n  + u;
           s = genrand_v32(s);
           D[uv] = (double) (s >> 16); //16 bits significatifs
       }
     return;
};


void print_mat (const int mat[], const int n, const int m, const char legende[], const char format[]);
void print_mat (const int mat[], const int n, const int m, const char legende[], const char format[])
/* Impression d'une matrice sur console avec legende et format specifique    */
/* mat[], n, m,                                                              */
/*    matrice n lignes m colonnes memorisee en tableau tel  que              */
/*    ij = i * n + j;                                                        */
/*    le tableau doit etre constitué lignes par lignes                       */
/*    ur une ligne d'ince i, j est l'indice courant .....                    */
/* legende[] est une chaine qui sera ajoutee en titre                        */
/* format[]  specificateur de format pour printf a employer pour les valeurs */
/*                                                                           */
/*     ref jordan & smith 2002 (mathematical techniques)                     */
/*          et T. Budd 1994 (classic data structure)                         */
/*     L'essentiel du C++ Lippman et Lajoie 3ie   pp 106                     */
{
    printf ("\nMatrice %3.0d par %3.0d : %s \n", n, m, legende) ;
    // Affichage ligne par ligne
    for (int i = 0; i < n ; i++)
    {
        for (int j = 0; j < m ; j++) 
            printf ( format, (int) mat[i * m + j]); 
        printf ("\n") ;
    }
    return;
}


void print_matd (double mat[], const int n, const int m, const char legende[], const char format[]);
void print_matd (double mat[], const int n, const int m, const char legende[], const char format[])
/* version pour valeurs de type double ...                                  */
{
  printf ("\nMatrice Format double %3.0d par %3.0d : %s \n", n, m, legende) ;
  // Affichage ligne par ligne
  for (int i = 0; i < n ; i++)
     {
     for (int j = 0; j < m ; j++) 
         printf ( format, mat[i * m + j]); 
     printf ("\n") ;
     }
  return;
}


//-------------------------------------------------------------------------
// Calcul des coefficients de Walsh
// dimension n = 2**r
//-------------------------------------------------------------------------

int wscoeff (int u, int x, int r);
// calcul general des tables de noyau de Walsh, methode Pourlakis-Seely
// ordonnancement dit sequentiel
int wscoeff (int u, int x, int r)
{
    int ex, q;
    ex =  (u >> (r-1) ) & x & 1;
    for (int i = 1; i < r ; i++)
    {
        q = ((u >> (r-i)) & 1) +  ((u >> (r-i-1)) & 1);
        ex = ex + (q  & ( (x >> i)&1 ) );
    }
    if (ex & 1)
        return -1;
    else
        return 1;
}


//-------------------------------------------------------------------------
// Calcul des coefficients de Walsh (methode 2)
// dimension n = 2**r
//-------------------------------------------------------------------------

void cwh (int A[], int r);
// calcul general des tables de noyau de Walsh, methode recursive
// ordonnancement dit sequentiel
void cwh (int A[], int r)
{
	if(r == 0)
		A[0] = 1;
	else {
		int n = (1 << r);
		int* Atmp = new int[n*n/4];
		cwh(Atmp, r-1);
		for(int y = 0; y < n/2; y++) {
			for(int x = 0; x < n/2; x++) {
				A[y*n+x] = Atmp[y*n/2+x];
			}
		}
		for(int y = 0; y < n/2; y++) {
			for(int x = n/2; x < n; x++) {
				A[y*n+x] = Atmp[y*n/2+(x-n/2)];
			}
		}
		for(int y = n/2; y < n; y++) {
			for(int x = 0; x < n/2; x++) {
				A[y*n+x] = Atmp[(y-n/2)*n/2+x];
			}
		}
		for(int y = n/2; y < n; y++) {
			for(int x = n/2; x < n; x++) {
				A[y*n+x] = -Atmp[(y-n/2)*n/2+(x-n/2)];
			}
		}
		
		delete[] Atmp;
	}
}

int reverse_gray_to_binary(int x1, int r) {
    unsigned int x = x1;
    
    // flip
    x = ((x >> 1) & 0x55555555u) | ((x & 0x55555555u) << 1);
    x = ((x >> 2) & 0x33333333u) | ((x & 0x33333333u) << 2);
    x = ((x >> 4) & 0x0f0f0f0fu) | ((x & 0x0f0f0f0fu) << 4);
    x = ((x >> 8) & 0x00ff00ffu) | ((x & 0x00ff00ffu) << 8);
    x = ((x >> 16) & 0xffffu) | ((x & 0xffffu) << 16);
    x = x >> (32 - r);
    
	// gray_to_binary
    unsigned int gray = x;
    unsigned int result = gray & 64;
    result |= (gray ^ (result >> 1)) & 32;
    result |= (gray ^ (result >> 1)) & 16;
    result |= (gray ^ (result >> 1)) & 8;
    result |= (gray ^ (result >> 1)) & 4;
    result |= (gray ^ (result >> 1)) & 2;
    result |= (gray ^ (result >> 1)) & 1;
    
    return result;  
}

//-------------------------------------------------------------------------
// calcul transformee de Walsh bidimensionnelle, algorithme de base
// Tableau a l'entree: D, transformee: W, dimension n = 2 a la puisance r
//-------------------------------------------------------------------------
void Walsh_2D_base (int D[], int W[], int r);
void Walsh_2D_base (int D[], int W[], int r)
{
	int coeff[dim_max_2D];
	
    int n;
    int u, v, x, y, uv, xy;
    n = (1 << r);
	
	int coeff_hada[dim_max_2D];
	cwh(coeff_hada, r);
	
	int tmp = 0;
	
	for (int i = 0; i < n ; i++) {
		tmp = reverse_gray_to_binary(i, r);
    	for (int j = 0; j < n ; j++) {
			coeff[tmp*n+j] = coeff_hada[i*n+j];
		}
	}
	
    for (v = 0; v < n ; v++)
        for (u = 0; u < n ; u++)
        {
            uv = (v << r) + u;  // uv = v * n  + u;
            W[uv] = 0;
            for (y = 0; y < n ; y++)
                for (x = 0; x < n ; x++) {
                    xy = (y << r) + x;  // xy = y * n + x;
                    W[uv] = W[uv] + D[xy] * coeff[u * n + x] * coeff[v * n + y];
                }
        }
        return;
}


//-------------------------------------------------------------------------
// Calcul transformee DCT 2D avec des fonctions cosinus
// Tableau a l'entree: D, transformee: S, dimension n = 2 a la puisance r
//-------------------------------------------------------------------------
void DCT_2D_base (double D[], double S[], int r);
void DCT_2D_base (double D[], double S[], int r)
{
    int n;
    int x, y, xy, vu;
    double Cu, Cv;
    n = (1 << r);
    for (int v = 0; v < n ; v++)
        for (int u = 0; u < n ; u++) 
        {
            vu = v*n+u;
            S[vu] = 0;
            if (u == 0) Cu = sqrt(2.0)/2.0; else Cu = 1.0;
            if (v == 0) Cv = sqrt(2.0)/2.0; else Cv = 1.0;
            for (y = 0; y < n ; y++)
                for (x = 0; x < n ; x++)
                {
                    xy = y*n+x;  
                    S[vu] = S[vu] +
                      D[xy]
                      * cos ((double)(2*x+1) * pi * (double) u / (double) (2*n))
                      * cos ((double)(2*y+1) * pi * (double) v / (double) (2*n));
                }
                S[vu] = (double) (2.0 / n ) * Cu * Cv * S[vu];
        }
}

//-------------------------------------------------------------------------
// Calcul transformee DCT 2D inverse avec des fonctions cosinus
// Tableau a l'entree: S, transformee inv: Di, dim. n = 2 a la puisance r
//-------------------------------------------------------------------------
void DCT_2D_basei (double S[], double Di[], int r);
void DCT_2D_basei (double S[], double Di[], int r)
{
    int n;
    int u, v, x, y, xy, vu;
    double Cu, Cv;
    n = (1 << r);
    for (y = 0; y < n ; y++)
        for (x = 0; x < n ; x++)
        {
            xy = y*n+x;  
            Di[xy] = 0;
            for (v = 0; v < n ; v++)
                for (u = 0; u < n ; u++) 
                {
                    vu = v*n+u;
                    if (u == 0) Cu = sqrt(2.0)/2.0; else Cu = 1.0;
                    if (v == 0) Cv = sqrt(2.0)/2.0; else Cv = 1.0;
                    Di[xy] = Di[xy] +
                        Cu * Cv *  S[vu]
                    * cos ((double)(2*x+1) * pi * (double) u / (double) (2*n))
                        * cos ((double)(2*y+1) * pi * (double) v / (double) (2*n));
                }
                Di[xy] = (double) (2.0 / n ) * Di[xy];
        }
}



//-------------------------------------------------------------------------
// faire_un_test()
//-------------------------------------------------------------------------	
// execution unique des fonctions pour verification des algorithmes sans
// faire des mesures de temps d'execution
//-------------------------------------------------------------------------	

int faire_un_test();
int faire_un_test()
{
    int x, y, k, n, r;

    int    D[dim_max_2D];      // non initialisées
    int    W[dim_max_2D];      // espace pour transformee de Walsh direct 
    int    Wi[dim_max_2D];     // espace pour transformees inverses

    double Ddbl[dim_max_2D];
    double Didbl[dim_max_2D];
    double S[dim_max_2D];


    // -------------------------------------------------------------------------
    // donnee initiale, test du generateur aleatoire

    printf ("\n-----------------------------------------------------------------") ;
    printf ("\nfaire_un_test----------------------------------------------------") ;
    printf ("\n-------------test du generateur aleatoire------------------------\n") ;

    r = kr;
    n = (1 << r);
    // assert (n*n <= dim_max); 
    if (n*n > dim_max_2D) {
        printf("*** Erreur dimension n*n > dim_max ( r = %d, n = %d, dim_max = %d, n*n = %d ) \n", r, n, dim_max_2D, n*n);
        exit (EXIT_FAILURE);
    }

    int rv = 99; // variable aleatoire initialisee
    genere_ran_D(D, r, rv);

    print_mat (D, n, n, "\nTableau D pseudo aleatoire PREMIER TIRAGE :", "%11.0d, ") ;

    genere_ran_D(D, r, rv);

    print_mat (D, n, n, "\nTableau D pseudo aleatoire SECOND TIRAGE :", "%11.0d, ") ;

    // -------------------------------------------------------------------------
    // Transformee de Walsh 2D
    printf ("\n-----------------------------------------------------------------") ;
    printf ("\nfaire_un_test--Walsh 2D------------------------------------------") ;
    printf ("\n-------------Coefficients Walsh 2D par wscoeff(x,y,r)------------\n") ;
    for (y = 0; y < n ; y++)
    {
        for (x = 0; x < n ; x++)
            printf ("%2.1d ", wscoeff(x,y,r)) ;
        printf ("\n") ;
    }

    printf ("\n-----------------------------------------------------------------") ;
    printf ("\nfaire_un_test--Walsh 2D------------------------------------------") ;
    printf ("\n-------------Donnees initiales-----------------------------------\n") ;
    print_mat (D, n, n, "\nTableau D :", "%11.0d, ") ;


    printf("Calcul transformee Walsh 2D ...\n");
    Walsh_2D_base (D, W,  r);  
    Walsh_2D_base (W, Wi,  r);  
    // mise a l'echelle d l'amplitude de la transformee inverse
    for (k = 0; k < n*n ; k++)
        Wi[k] = Wi[k] >> (2*r) ;

    // affichage de la transformee Walsh 2D

    printf ("\n-----------------------------------------------------------------") ;
    printf ("\nfaire_un_test--Walsh 2D------------------------------------------") ;
    printf ("\n-------------Transformee W calculee avec Fonction Walsh (decimal)\n") ;
    print_mat (W, n, n, "\nTransformee W calculee avec Fonction Walsh (decimal):", "%11.0d, ") ;


    printf ("\n-----------------------------------------------------------------") ;
    printf ("\nfaire_un_test--Walsh 2D------------------------------------------") ;
    printf ("\n-------------Transformee W calculee avec Fonction Walsh (hexadecimal)\n") ;
    print_mat (W, n, n, "\nTransformee W calculee avec Fonction Walsh (decimal):", "0x%8.0X, ") ;


    printf ("\n-----------------------------------------------------------------") ;
    printf ("\nfaire_un_test--Walsh 2D------------------------------------------") ;
    printf ("\n-------------Transformee inverse Wi calculee avec Fonction Walsh (decimal)\n") ;
    print_mat (Wi, n, n, "\nTransformee inverse Wi calculee avec Fonction Walsh (decimal):", "%11.0d, ") ;

    // -------------------------------------------------------------------------
    // Transformee DCT 2D
    printf ("\n-----------------------------------------------------------------") ;
    printf ("\nfaire_un_test--DCT  2D------------------------------------------") ;
    //printf ("\n-------------Tableau initial D (format flottant double)-----------\n") ;

    // conversion double et affichage de la donnee initiale
    for (y = 0; y < n ; y++)
    {
        for (x = 0; x < n ; x++){
            Ddbl[ y*n+x] = (double) D[ y*n+x];
            //printf ("%+9.2f, ", Ddbl[ y*n+x]) ;
        }
        //printf ("\n") ;
    }
    print_matd (Ddbl, n, n, "\n-------------Tableau initial D (format flottant double)-----------", "%+9.2f, ") ;

    // calcul transformee  DCT_2D_base directe
    DCT_2D_base (Ddbl, S, r); 

    // calcul transformee  DCT_2D_base inverse
    DCT_2D_basei (S, Didbl, r);

    // affichage de la transformee DCT
    printf ("\n-----------------------------------------------------------------") ;
    printf ("\nfaire_un_test--DCT 2D------------------------------------------") ;
    print_matd (S, n, n, "\n-------------Transformee S calculee avec Fonction DCT (double)-----------", "%+9.2f, ") ;
 
    // affichage de la transformee DCT inverse
    printf ("\n-----------------------------------------------------------------") ;
    printf ("\nfaire_un_test--DCT 2D------------------------------------------") ;
    print_matd (Didbl, n, n, "\n-------------Transformee inverse Didbl calculee avec Fonction DCTi (double)---------", "%+9.2f, ") ;
 
   return 0;
}

//-------------------------------------------------------------------------
// faire_N_test()
//-------------------------------------------------------------------------	
// Execution multiple des fonctions pour mesures des temps d'execution
// Des donnees aleatoires sont generees a chaque repetition
//-------------------------------------------------------------------------	   
int faire_N_test  (ParamChrono& p);
int faire_N_test  (ParamChrono& p)
{
    int  k, n, r;
    double starttime;

    // donnes representant un bloc d'images 
    int D[dim_max_2D];  // non initialisées

    // espaces pour transformees
    int    W[dim_max_2D];
    int    Wi[dim_max_2D];
    double Ddbl[dim_max_2D];
    double Didbl[dim_max_2D];
    double S[dim_max_2D];

    // -------------------------------------------------------------------------
    // DEBUT DES TESTs ---------------------------------------------------------
    // affichage de la donnee initiale

    printf ("\n-----------------------------------------------------------------") ;
    printf ("\n Iteration de N tests     ---------------------------------------") ;
    printf ("\n-----------------------------------------------------------------\n") ;
    ladate_c(p.la_date);
    printf ("Date courante: %s", p.la_date);


    r = kr;
    n = (1 << r);
    // assert (n*n <= dim_max); 
    if (n*n > dim_max_2D) 
    {
        printf("*** Erreur dimension n*n > dim_max ( r = %d, n = %d, dim_max = %d, n*n = %d ) \n", r, n, dim_max_2D, n*n);
        exit (EXIT_FAILURE);
    }

    // -------------------------------------------------------------------------
    // donnee initiale, avec generateur aleatoire
    int rv = 99; // une variable aléatoire initialisee
    genere_ran_D(D, r, rv);

    //----------------------------------------------------------------
    // calcul transformee  Walsh 2D 
    //-----------------------------------------------------------------

    printf("\nEvaluation iteree Walsh_2D_base &  inverse ...\n");

    starttime = utime();
    p.runtimeWbase   = 0;
    // on fait N fois pour chronometrer ....
    for (int it = 0; (it <= NwalMax) && (p.runtimeWbase < Tmax) ; it++)
    {
        // Calcul transformee Walsh
        Walsh_2D_base (D, W,  r);  
        // Calcul transformee inverse
        Walsh_2D_base (W, Wi,  r);  
        // mise a l'echelle d l'amplitude de la transformee inverse
        for (k = 0; k < n*n ; k++)
            Wi[k] = Wi[k] >> (2*r) ;
        genere_ran_D(D, r, rv);  // nouvelle donnee pour le prochain test....
        p.runtimeWbase   = utime() - starttime;
        p.niterWbase   = it;
    }   

    //-----------------------------------------------------------------
    // calcul transformee  DCT 
    //-----------------------------------------------------------------

    // tirage aleatoire initial conversion double  ---------------------------------------------
    genere_ran_double_D(Ddbl, r, rv);

    printf("\nEvaluation iteree DCT 2D & DCT 2D inverse ...\n");
    starttime = utime();
    p.runtimeDCT   = 0;
    for (int it = 0; (it <= NdctMax) && ( p.runtimeDCT < Tmax) ; it++)
    {
        // Calcul transformee DCT
        DCT_2D_base (Ddbl, S, r); 
        // calcul transformee  DCT inverse
        DCT_2D_basei (S, Didbl, r);
        genere_ran_double_D(Ddbl, r, rv);  // nouvelle donnee pour le prochain test....
        p.runtimeDCT = utime() - starttime;
        p.niterDCT   = it;
    }
    return (0);
}



int affiche_resultats (ParamChrono& p);
int affiche_resultats (ParamChrono& p)
{
   printf("\n\n-----affiche_resultats-------Parametres--------------------------\n");
   printf  (" kr                = %8.2d\n",  kr);
   printf  (" (n = 1 << kr)     = %8.2d\n",  (1 << kr));
   printf  (" (nxn = 1 << 2*kr) = %8.2d\n",  (1 << 2*kr));
   printf  (" NwalMax           = %8.1d\n",  NwalMax);
   printf  (" NdctMax           = %8.1d\n",  NdctMax);
   printf  (" Tmax              = %8.3f\n",  Tmax);

   printf ("\n-----affiche_resultats---------Legendes--------------------------\n");
   printf ("Algorithme,    version:  \n");
   printf ("Walsh_2D : Walsh_2D_base  directe et inverse algorithme de base\n");
   printf ("DCT      : DCT_2D_base    directe et inverse algorithme de base\n");

   printf ("\n-----affiche_resultats---------Mesures----------------------------\n"); 
   printf ("Date du test: %s \n", p.la_date);
   printf ("Programme,    Algorithme,        version,   kr,    n,    N_iter,       T_N_iter,      N_iter/sec\n");
   printf ("perfWalshDCT,   Walsh_2D,  Walsh_2D_base, %4.0d, %4.0d,   %7.0d,   %12.6f, %12.3f \n", kr, (1 << kr), p.niterWbase, p.runtimeWbase, p.niterWbase/p.runtimeWbase );
   printf ("perfWalshDCT,     DCT_2D,    DCT_2D_base, %4.0d, %4.0d,   %7.0d,   %12.6f, %12.3f \n", kr, (1 << kr), p.niterDCT, p.runtimeDCT, p.niterDCT/p.runtimeDCT);
   return 0;
}


// -------------------------------------------------------------------------
// DEBUT DU CODE PRINCIPAL -------------------------------------------------
int main( int argc, char **argv, char **envp )

{
   ParamChrono mesures;   // structure de donnees qui contient les valeurs des mesures
    
   if (argc != 1)            // Analyse et validation de la ligne de commande
   {
      printf ("Ligne de commande non correcte (aucun argument requis) \n") ;
      return EXIT_FAILURE;
   }
   printf  ("programme        = %s \n", argv[0]);
   printf  ("environnement    = %s \n", *envp);
   printf  ("sizeof(int)      = %u\n", (unsigned int) sizeof(int) );
   printf  ("sizeof(long)     = %u\n", (unsigned int) sizeof(long) );
   printf  ("sizeof(double)   = %u\n", (unsigned int) sizeof(double) );

   faire_un_test ( );           // verifier les algorithmes

   faire_N_test(mesures);       // repeter plusieurs executions sur donnees pseudo-aleatoires

   affiche_resultats(mesures);  // afficher les resultats de temps d'execution

   return 0;

}
