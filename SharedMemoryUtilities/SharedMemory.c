/** 
 * Code par D. Palau  
 * Modifie par C. Drolet
 * Modifie par S. Cherkaoui
 * Version inclue:
 *   - Lecture avec des caracteres en trop
 *   - Depassement des limites si la String ecrite est trop longue   
 *   - Liberation de memoire
**/
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/wait.h>


#include <jni.h>
#include "SharedMemory.h"


/** 
 * Permet de reserver une zone de memoire partagee divisee en strings
 * @param    size        le nombre de positions (strings)a reserver, qui
 *                        doit etre  inferieur a 128
 * @param    maxString   la taille maximale de chaque string, , qui
 *                        doit etre  inferieur a 128 
 * @return   l'adresse de la memoire partagee reservee   
 */
JNIEXPORT jint JNICALL Java_SharedMemoryUtilities_SharedMemory_alloc
    (JNIEnv * env, jclass classe, jint size, jint maxsize)
{
    /* La reservation de la memoire */
    int shmid;
    int totalSize = (int)size*(int)maxsize + 2;
    
    if ((shmid = shmget(IPC_PRIVATE, totalSize, IPC_CREAT | 0660)) < 0) {
       perror ("Error - Java_SharedMemory_alloc");
       return -1;
    }
    else {
        printf("%d",shmid);
    }
    
    /* La memoire a ete reservee on attache la memoire partagee a      
       l’espace memoire du processus */
    char *shm;
    char *s;
    if ((shm = (char *)shmat(shmid,0,0)) == (char *) -1){
       perror ("Error - Java_SharedMemory_alloc 2");
    }
    s = shm;

    /* On procede a la initialization. Toutes les cellules de la memoire contiennent une valeur de \0 au depart */
    int i;    
    for ( i=0; i<totalSize; i++ )
    {
        s[i]='\0';
    }

    /* Pour pouvoir controller les access futurs, on sauvegarde 
     * dans la position 0 le nombre maximal de strings a stocker 
     * et dans la position 1 la taille maximale de ces strings
     */
    s[0] = size;
    s[1] = maxsize;
    
    /* on detattache la memoire de l’espace memoire du processus.*/
    shmdt(shm);
    
    return shmid;
}

/* Permet d'ecrire une string dans la zone de memoire partagee
 * @param    handle      l'adresse de la memoire partagee 
 * @param    pos         la position dans la memoire partage ou ecrire
 * @param    data        le string a ecrire
 */
JNIEXPORT void JNICALL Java_SharedMemoryUtilities_SharedMemory_write
    (JNIEnv * env, jclass classe, jint handle, jint pos, jstring data)
{
    /* On attache la memoire partagee a l’espace memoire du processus */

    char *shm;
    char *s;
    if ((shm = (char *)shmat(handle,0,0)) == (char *) -1){
        perror ("Error - Java_SharedMemory_write");
    }

    s = shm;
    /* On dois savoir le nombre de strings, et la taille maximale de ces strings */
    int size = s[0];
    int maxsize = s[1];

    /* Conversion de format imposee par JNI */
    const char *str;
    str = (*env)->GetStringUTFChars(env,data,NULL);
    if (str==NULL)
    {
        perror ("Error - Java_SharedMemory_write - GetStringUTFChars");
    }
   
    /* La taille du string lu */
    int stringSize = (*env)->GetStringUTFLength(env, data);

    /* On calcule la position pour le stocker */
    int index = 2+pos*maxsize;

    /* On doit copier caractere par caractere le string dans la zone de memoire partagee */
    int j;
    
    /* Condition: j < maxsize
     * Si la taille de la String recue est trop grande,
     * on doit couper au maximum (maxsize) pour ne pas
     * depasser l'espace allouee pour notre bloc de
     * memoire partagee.
     */
    for (j=0; j < stringSize && j < maxsize; j++)
    {
        s[index+j] = str[j];
    }
   
    /* Afin que la String stockee soit identique lorsqu'elle sera
     * lue, on met un \0 a la fin si elle n'arrive pas a la longueur
     * maximale (maxsize). Sinon, on pourrait lire le contenu d'une
     * String stockee avant.
     */
    if (j < maxsize)
        s[index+j] = '\0';
    
    /* on detattache la memoire de l’espace memoire du processus.*/    

    shmdt(shm);
    
    (*env)->ReleaseStringUTFChars (env, data, str);
}

/* Permet de lire une string dans la zone de memoire partagee
 * @param    handle      l'adresse de la memoire partagee 
 * @param    pos         la position dans la memoire partagee ou ecrire? (lire)
 * @return   data        le string lu
 */
JNIEXPORT jstring JNICALL Java_SharedMemoryUtilities_SharedMemory_read
    (JNIEnv * env, jclass classe, jint handle, jint pos)
{
    char *shm;
    char *s;

    /* On attache la memoire partagee a l’espace memoire du processus */

    if ((shm = (char *)shmat(handle,0,0)) == (char *) -1){
        perror ("Error - Java_SharedMemory_read");
    }

    int p;
    p = (int)pos;
    s = shm;

    /* On dois savoir le nombre de strings, et la taille maximale de ces strings */
    int size = s[0];
    int maxsize = s[1];
    
    char str[256];
    int ipos = (int) pos;

    /* Je calcule la position de debut du string demande */
    int index;
    index = ipos*maxsize+2;

    int j;

    /* caractere par caractere on construit le string de retour */
    
    /* Condition ajoutee: s[index+j] != '\0'
     * Il est inutile de continuer a lire apres la fin de la String.
     */
    for (j=0; j < maxsize && s[index+j] != '\0'; j++)
    {
        str[j] = s[index+j];
    }
    str[j] = '\0'; 

    /* on detattache la memoire de l’espace memoire du processus*/
    shmdt(shm);
    
    return (*env)->NewStringUTF(env,str);
}

/* Permet de liberer la memoire partagee
 * @param    handle      l'adresse de la memoire partagee 
 */
JNIEXPORT void JNICALL Java_SharedMemoryUtilities_SharedMemory_free
    (JNIEnv * env, jclass classe, jint handle)
{
    /* shmctl : la fonction qui sert a supprimer
     * la memoire partagee.
     */
    if (shmctl(handle, IPC_RMID, NULL)) {
        perror ("Error - Java_SharedMemory_free");
    }
}

/* Permet d'afficher le contenu de la memoire partagee
 * @param    handle      l'adresse de la memoire partagee 
 */
JNIEXPORT void JNICALL Java_SharedMemoryUtilities_SharedMemory_displaySharedMemory
    (JNIEnv * env, jclass classe, jint handle)
{
    char *shm;
    char *s;
    
    /* On attache la memoire partagee a l’espace memoire du processus */
    if ((shm = (char *)shmat(handle,0,0)) == (char *) -1){
        perror ("Error - Java_SharedMemory_displaySharedMemory");
    }

    s = shm;
    int size = s[0];
    int maxsize = s[1];

    printf("%s %d %s %d %s %d\n", 
        "Java_SharedMemory_displaySharedMemory --- Handle =",
        (int)handle, 
        " size: ", size,
        " maxsize: ",maxsize);
   
    int j;
    int k;
    for (j=0;j<size ; j++)
    {
        printf("%s %d %s", "position : ", j," = ");
        for (k=0; k<maxsize; k++)
        {
            // printf("%s", s[2+(j*maxsize)+k]);
            printf("%d", s[2+(j*maxsize)+k]);
        }
        printf("\n");
    }
    printf("==================================================\n");
    
    /* on detattache la memoire de l’espace memoire du processus.*/    
    shmdt(shm);
}
