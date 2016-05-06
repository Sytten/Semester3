# wscoeff.s 
# APP # 1 S3 G. informatique
# D. Dalle aout 2015
# calcul general des tables de noyau de Walsh, methode Pourlakis-Seely
# ordonnancement dit sequentiel
#------------------------------------------------------------------
.data

r:	.word 3  

  
.text
main:
	ori $t0, $0, 1
	lw $s0, r($0)
	sllv $s0, $t0, $s0
	
	# Allocation memoire sur le heap
	ori $v0, $0, 9
	mul $a0, $s0, $s0
	mul $a0, $a0, 4
	syscall
	
	# Copier adresse memoire de retour (tableau)
	move $s1, $v0
	
	# Population du teableau
	ori $t0, $0, 0
	ori $t1, $0, 1
bcle:
	beq $t0,$a0,fbcle
	addu $t2, $t0, $s1
	sw $t1, 0($t2)
	addiu $t1, $t1, 1
	addiu $t0, $t0, 4
	j bcle
fbcle:	
	move $a0, $s0 # Parametre n
	move $a1, $s1 # Parametre Tableau
	
	jal Walsh_2D_base
	j Exit

# FONCTION DE CALCUL DES COEFF #
################################
wscoeff:
          # allocation des registres (aucune sauvegarde)
          # $a0    u
          # $a1    x
          # $a2    r
          # $t0    travail
          # $t1    constante 1
          # $t2    indice i
          # $t3    constante r-1
          # $t4    r-i
          # $t5    registre tmp
          # $t6    q
          # $v0    ex, retour wscoeff
                          
# ex =  (u>>(r-1) ) & x & 1;
        addi    $t1, $0,  1
        add     $t3, $0,  $a2
        addi    $t3, $t3, -1    #(r-1)
        srlv    $v0, $a0, $t3   # u >> (r-1)
        and     $v0, $v0, $a1   # & x
        and     $v0, $v0, $t1   # & 1
                                # ex           
        addi    $t2, $0,  1     # i
                                # for (i = 1; i < r ; i++)                                
bfor:                           # {
        slt     $t0, $t2, $a2
        beq     $t0, $0, ffor
        
                                # q = ((u >> (r-i)) & 1) + ((u >> (r-i-1)) & 1);
        subu    $t4, $a2, $t2   # r-i
        srlv    $t5, $a0, $t4   # u >>(r-i)
        and     $t5, $t5, $t1   # & 1

        subu    $t4, $t4, $t1   # r-i -1
        srlv    $t6, $a0, $t4   # u >>(r-i-1)
        and     $t6, $t6, $t1   # & 1
        add     $t6, $t6, $t5   # q
		
                                # ex = ex + (q & ((x >> i)&1));             
        srlv    $t5, $a1, $t2   # x >>(i)
        and     $t5, $t5, $t1   # & 1
        and     $t5, $t5, $t6   # & q
        add     $v0, $v0, $t5   # ex
                
        addi    $t2, $t2, 1     # i++
        j       bfor
ffor:                           # }

                                # if (ex & 1)  return -1; else  return 1; 
        and     $v0, $v0, $t1   # & 1
        beq     $v0, $0,  rp    #
        addi    $v0, $0, -1     # return -1
        jr      $ra     
rp:     addi    $v0, $0, 1      # return 1                                          
        jr      $ra
  
              
# FONCTIONDE CALCUL DE WALSH #
##############################
# S0: n
# S1: Tableau entree
# S2: Tableau coeff
# S3: Tableau resultant
# S4: n*n*4
Walsh_2D_base:
	#Enregistrer parametre
	move $s0, $a0 #n
	move $s1, $a1 #tableau original
	
	addi $sp, $sp, -4
	sw $ra, 0($sp)
	
	# Calculer taille bytes tableau
	mul $s4, $s0, $s0
	mul $s4, $s4, 4
	
	# Allocation memoire sur le heap pour resultant
	ori $v0, $0, 9
	move $a0, $s4
	syscall
	
	# Copier adresse memoire de retour (tableau)
	move $s3, $v0 #tableau resultant

	# Allocation memoire sur le heap pour coeff
	ori $v0, $0, 9
	move $a0, $s4
	syscall
	
	# Copier adresse memoire de retour (tableau)
	move $s2, $v0 #tableau des coeff
	
	#Populer tableau de coefficients
	ori $s5, $0, 0
	lw $a2, r($0)
bcle_coeff_walsh:
	beq $s5,$s4,fbcle_coeff_walsh
	
	div $a0, $s5, 4
	rem $a1, $a0, $s0 	# Calcul colonne (i %n)
	div $a0, $a0, $s0 	# Calcul range (i / n)
	
	jal wscoeff
	
	addu $t0, $s5, $s2
	sw $v0, 0($t0)
	addiu $s5, $s5, 4
	j bcle_coeff_walsh
fbcle_coeff_walsh:
	
	ori $t0, $0, 0 		# t0 <- 0 (compteur externe)
bcle_externe_walsh:
	beq $t0, $s4, fbcle_externe_walsh
	ori $t1, $0, 0 		# t1 <- 0 (compteur interne)
	ori $t2, $0, 0 		# t2 <- 0 (valeur calculee)
	
bcle_interne_walsh:
	beq $t1, $s4, fbcle_interne_walsh
	
	div $t4, $t0, 4
	rem $t7, $t4, $s0	# Calcul colonne boucle externe (i % n) -> u
	div $t6, $t4, $s0	# Calcul range boucle externe (i / n) -> v
	
	div $t4, $t1, 4
	rem $t5, $t4, $s0	# Calcul colonne boucle interne (i % n) -> x
	div $t4, $t4, $s0	# Calcul range boucle interne (i / n) -> y

	addu $t3, $t1, $s1	# Calcul adresse xy
	lw $s5, 0($t3)		# Lecture de D[xy]
	
	mul $t3, $t7, $s0	# u * n
	addu $t3, $t3, $t5	# (u * n) + x
	mul $t3, $t3, 4		# Bon nombre bytes
	addu $t3, $t3, $s2	# calcul adresse coefficient (u * n) + x
	lw $s6, 0($t3)		# Lecture du coefficient (u * n) + x
	
	mul $s5, $s5, $s6	# Multiplication D[xy] * coeff[(u * n) + x]
	
	mul $t3, $t6, $s0	# v * n
	addu $t3, $t3, $t4	# (v * n) + y
	mul $t3, $t3, 4		# Bon nombre bytes
	addu $t3, $t3, $s2	# calcul adresse coefficient (v * n) + y
	lw $s6, 0($t3)		# Lecture du coefficient (v * n) + y
	
	mul $s5, $s5, $s6	# Multiplication D[xy] * coeff[(u * n) + x] * coeff[(v * n) + y]
	
	add $t2, $t2, $s5	# W[uv] + D[xy] * coeff[(u * n) + x] * coeff[(v * n) + y]
	
	addiu $t1, $t1, 4 	# Incrementer compteur interne
	j bcle_interne_walsh
fbcle_interne_walsh:
	addu $t3, $t0, $s3 	# Calculer adresse enregistrement tableau sortie
	sw $t2, 0($t3)		# Enregistrer valeur calculee
	addiu $t0, $t0, 4	# Incrementer compteur externe
	j bcle_externe_walsh
	
fbcle_externe_walsh:
	move $v0, $s3
	lw $ra, 0($sp)
	addi $sp, $sp, 4
	jr $ra	
	
Exit:
	ori $v0, $0, 0xA
	syscall
	

