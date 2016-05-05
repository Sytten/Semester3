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
Walsh_2D_base:
	#Enregistrer parametre
	move $s0, $a0 #n
	move $s1, $a1 #tableau original
	
	addi $sp, $sp, -4
	sw $ra, 0($sp)

	# Allocation memoire sur le heap
	ori $v0, $0, 9
	mul $a0, $a0, $a0
	mul $a0, $a0, 4
	syscall
	
	# Copier adresse memoire de retour (tableau)
	move $s2, $v0 #tableau des coeff
	
	# Calculer taille bytes tableau
	mul $s3, $s0, $s0
	mul $s3, $s3, 4
	
	#Populer tableau de coefficients
	ori $s4, $0, 0
	lw $a2, r($0)
bcle_coeff_walsh:
	beq $s4,$s3,fbcle_coeff_walsh
	
	div $a0, $s4, 4
	rem $a1, $a0, $s0
	div $a0, $a0, $s0
	
	jal wscoeff
	
	addu $t0, $s4, $s2
	sw $v0, 0($t0)
	addiu $s4, $s4, 4
	j bcle_coeff_walsh
fbcle_coeff_walsh:
	lw $ra, 0($sp)
	addi $sp, $sp, 4
	jr $ra	
	
Exit:
	ori $v0, $0, 0xA
	syscall
	

