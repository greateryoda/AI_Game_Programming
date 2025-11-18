import java.util.*;
public class ModeleAwale{
    int TAILLE_PLATEAU = 16;
    private List<Graine>[] plateau;
    private Joueur joueurActif;
    private boolean jeuTermine;
    private boolean draw;
    private Joueur gagnant;
    int scoreJoueur1;
    int scoreJoueur2;
    public ModeleAwale() {
        //plateau = new ArrayList[TAILLE_PLATEAU];
        plateau = (ArrayList<Graine>[]) new ArrayList<?>[TAILLE_PLATEAU];
        joueurActif = Joueur.Joueur_1;
        jeuTermine = false;
        gagnant = null;
        draw = false;
        scoreJoueur1 = 0;
        scoreJoueur2 = 0;
        initialiserPlateau();
    }

    public List<Graine>[] getPlateauCopie(){
        List<Graine>[] copie = new List[TAILLE_PLATEAU];
        for (int i = 0; i < TAILLE_PLATEAU; i++){
            copie[i] = plateau[i];
        }
        return copie;
    }

    private void initialiserPlateau() {
        for(int i = 0; i< TAILLE_PLATEAU; i++){
            plateau[i] = new ArrayList<>();
            for(int j = 0; j<2; j++){
                plateau[i].add(Graine.ROUGE);
                plateau[i].add(Graine.TRANSPARENT); 
                plateau[i].add(Graine.BLEU); 
            }
        }
    }



    public Joueur getJoueurActif() {
        return joueurActif;
    }

    public void changeJoueur(){
        joueurActif = joueurActif.opposite();
    }

    public boolean getDraw(){
        return draw;
    }

    public boolean isJeuTermine() {
        return jeuTermine;
    }

    public Joueur getGagnant() {
        return gagnant;
    }

    public Joueur computeGagnant(){
        if(scoreJoueur2>scoreJoueur1){
            return Joueur.Joueur_2;
        }
        else return Joueur.Joueur_1;
    }

    public int compterGraineCase(int numero_case, Graine gr){
        int count = 0;
        for(Graine g : plateau[numero_case]){
                if(g == gr) count++;
        }
        return count;
    }

    public int compterTousGraines(int numero_case) {
        return plateau[numero_case].size();
    }

    public int getScore(Joueur j) {
        if (j == Joueur.Joueur_1)
            return scoreJoueur1;
        else
            return scoreJoueur2;
    }

    public int compterGrainesRestante(){
        int cpt = 0;
        for(int i=0;i<TAILLE_PLATEAU;i++){
            cpt += compterTousGraines(i);
        }
        return cpt;
    }

    public void starving(){
        switch(joueurActif){
            case Joueur.Joueur_1:
                scoreJoueur2 += compterGrainesRestante();
                if(getScore(Joueur.Joueur_1)==40 && getScore(Joueur.Joueur_2)==40){
                    jeuTermine = true;
                    draw = true;
                }
                else{
                    jeuTermine = true;
                    gagnant = computeGagnant();
                }
                break;
            case Joueur.Joueur_2:
                scoreJoueur1 += compterGrainesRestante();
                if(getScore(Joueur.Joueur_1)==40 && getScore(Joueur.Joueur_2)==40){
                    jeuTermine = true;
                    draw = true;
                }
                else{
                    jeuTermine = true;
                    gagnant = computeGagnant();
                }
                break;
        }
    }

    public List<Move> getPossibleMoves() {
        List<Move> moves = new ArrayList<>();
        if(joueurActif == Joueur.Joueur_1){
            for (int i = 1; i < 16; i+=2) {

                for (Graine g : Graine.values()) {
                    if (compterGraineCase(i, g) > 0) {
                        if (g == Graine.TRANSPARENT) {
                            moves.add(new Move(i, g, true, joueurActif));
                            moves.add(new Move(i, g, false, joueurActif));
                        } else {
                            moves.add(new Move(i, g, null, joueurActif));
                        }
                    }
                }
            }
        }
        if(joueurActif == Joueur.Joueur_2){
            for (int i = 0; i < 16; i+=2) {

                for (Graine g : Graine.values()) {
                    if (compterGraineCase(i, g) > 0) {
                        if (g == Graine.TRANSPARENT) {
                            moves.add(new Move(i, g, true, joueurActif));
                            moves.add(new Move(i, g, false, joueurActif));
                        } else {
                            moves.add(new Move(i, g, null, joueurActif));
                        }
                    }
                }
            }
        }
        return moves;
    }

    public boolean isCaseDuJoueur(int idx, Joueur j) {
        int holeNum = idx + 1;                
        return (j == Joueur.Joueur_1)
                ? (holeNum % 2 == 1)
                : (holeNum % 2 == 0);
    }

    public boolean hasSeedsOfColor(int idx, Graine g) {
        return compterGraineCase(idx, g) > 0;
    }




    public boolean  deplacerGraine(Graine gr,int numero_case,Boolean asRed){
    System.out.println(gr);System.out.println(asRed);
    if (!isCaseDuJoueur(numero_case, joueurActif)) {
        System.out.println("Ce case ne t'appartient pas");
        return false;
    }
    if (!hasSeedsOfColor(numero_case, gr)) {
        System.out.println("Il n'y a aucune graine dans ce cas");
        return false;
    }
       
       int quantite_graine = compterGraineCase(numero_case, gr);
       plateau[numero_case].removeAll(Collections.singleton(gr));
       int i = numero_case;

        switch (gr) {

            case BLEU -> {
                while (quantite_graine > 0) {
                    i = (i + 1) % 16;
                    if (joueurActif == Joueur.Joueur_1 && (i % 2 == 1)) {
                        plateau[i].add(Graine.BLEU);
                        quantite_graine--;
                    } else if (joueurActif == Joueur.Joueur_2 && (i % 2 == 0)) {
                        plateau[i].add(Graine.BLEU);
                        quantite_graine--;
                    }
                }
            }

            
            case ROUGE -> {
                while (quantite_graine > 0) {
                    i = (i + 1) % 16;
                    plateau[i].add(Graine.ROUGE);
                    quantite_graine--;
                }
            }

            
            case TRANSPARENT -> {
                if (asRed == null) break; 

                if (asRed) {
                    while (quantite_graine > 0) {
                        i = (i + 1) % 16;
                        plateau[i].add(Graine.TRANSPARENT);
                        quantite_graine--;
                    }
                    if(compterGraineCase(numero_case,Graine.ROUGE)>0)
                        deplacerGraine(Graine.ROUGE, numero_case, null);
                } else {
                    
                    while (quantite_graine > 0) {
                        i = (i + 1) % 16;
                        if (joueurActif == Joueur.Joueur_1 && (i % 2 == 1)) {
                            plateau[i].add(Graine.TRANSPARENT);
                            quantite_graine--;
                        } else if (joueurActif == Joueur.Joueur_2 && (i % 2 == 0)) {
                            plateau[i].add(Graine.TRANSPARENT);
                            quantite_graine--;
                        }
                    }
                    if(compterGraineCase(numero_case,Graine.BLEU)>0)
                        deplacerGraine(Graine.BLEU, numero_case, null);
                }
            }
        }
        capture(i);

        return true;
    }

    void capture(int numero_case){
        while(compterTousGraines(numero_case) == 2 || compterTousGraines(numero_case) == 3){
            if (plateau[numero_case].isEmpty()) break;
            if (joueurActif == Joueur.Joueur_1)
                scoreJoueur1 += plateau[numero_case].size();
            else
                scoreJoueur2 += plateau[numero_case].size();
            plateau[numero_case].clear();
            numero_case = (numero_case - 1 + 16) % 16;
        }
        if (getScore(joueurActif) >= 49) {
            jeuTermine = true;
            gagnant = joueurActif;
        }
        if(getScore(Joueur.Joueur_1)==40 && getScore(Joueur.Joueur_2)==40){
            jeuTermine = true;
            draw = true;
        }
        if(compterGrainesRestante()<10){
            jeuTermine = true;
            gagnant = computeGagnant();
        }
    }


    private String afficherCase(int i) {
        StringBuilder sb = new StringBuilder("(");
        for (Graine g : plateau[i]) {
            if (g == Graine.ROUGE) sb.append("R");
            else if (g == Graine.BLEU) sb.append("B");
            else sb.append("T");
        }
        sb.append(")");
        return sb.toString();
    }


    public void afficherPlateau() {
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        // Üst sıra (1 → 8)
        for (int i = 0; i < 8; i++) {
            System.out.printf("%2d: %-10s ", (i + 1), afficherCase(i));
        }
        System.out.println();

        // Alt sıra (16 → 9) tersten
        for (int i = 15; i >= 8; i--) {
            System.out.printf("%2d: %-10s ", (i + 1), afficherCase(i));
        }
        System.out.println("\n");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Score: J1 = " + getScore(Joueur.Joueur_1) + 
                        " | J2 = " + getScore(Joueur.Joueur_2));
        System.out.println("Joueur Actif: " + joueurActif);
    }

}