import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameState {
    private List<Graine>[] plateau;
    private Joueur joueurActif;
    private int scoreJ1;
    private int scoreJ2;

    public GameState(ModeleAwale model) {
        plateau = new List[model.plateau.length];
        for(int i=0;i<model.plateau.length;i++){
            if(model.plateau[i]!=null){
                plateau[i] = new ArrayList<>(model.plateau[i]);
            }
        }
        this.joueurActif = model.getJoueurActif();
    }

    public GameState(){
        this.plateau = new ArrayList[16];
        for(int i = 0; i < plateau.length; i++){
            plateau[i] = new ArrayList<>();
        }
    }

    public GameState(GameState gameState) {
        plateau = new List[gameState.plateau.length];
        for(int i=0;i<gameState.plateau.length;i++){
            if(gameState.plateau[i]!=null){
                plateau[i] = new ArrayList<>(gameState.plateau[i]);
            }
        }
        this.joueurActif = gameState.joueurActif;
        this.scoreJ1 = gameState.scoreJ1;
        this.scoreJ2 = gameState.scoreJ2;
    }

    public int compterGrainesRestante(){
        int cpt = 0;
        for(int i=0;i< plateau.length;i++){
            cpt += compterTousGraines(i);
        }
        return cpt;
    }

    public int getScore(){
        return (joueurActif == Joueur.Joueur_1 ? scoreJ1 : scoreJ2);
    }

    public boolean estTerminal(){
        if (getScore() >= 49) {
            return true;
        }
        if(scoreJ1==40 && scoreJ2==40){
            return true;
        }
        if(compterGrainesRestante()<10){
            return true;
        }
        return false;
    }

    public List<Move> getPossibleMoves() {
        List<Move> moves = new ArrayList<>();
        if(joueurActif == Joueur.Joueur_2){
            for (int i = 1; i < 16; i+=2) {

                for (Graine g : Graine.values()) {
                    if (compterGraineCase(i, g) > 0) {
                        if (g == Graine.TRANSPARENT) {
                            moves.add(new Move(i, g, true, joueurActif));
                            moves.add(new Move(i, g, false, joueurActif));
                        } else {
                            moves.add(new Move(i, g, false, joueurActif));
                        }
                    }
                }
            }
        }
        if(joueurActif == Joueur.Joueur_1){
            for (int i = 0; i < 16; i+=2) {

                for (Graine g : Graine.values()) {
                    if (compterGraineCase(i, g) > 0) {
                        if (g == Graine.TRANSPARENT) {
                            moves.add(new Move(i, g, true, joueurActif));
                            moves.add(new Move(i, g, false, joueurActif));
                        } else {
                            moves.add(new Move(i, g, false, joueurActif));
                        }
                    }
                }
            }
        }
        return moves;
    }

    private int compterGraineCase(int i, Graine gr) {
        int count = 0;
        for(Graine g : plateau[i]){
            if(g == gr) count++;
        }
        return count;
    }

    private int compterTousGraines(int i) {
        return plateau[i].size();
    }

    public GameState starving(){
        System.out.println("starving!!!!!!");
        GameState copie = new GameState();
        switch(joueurActif){
            case Joueur_1:
                copie.scoreJ2 = this.scoreJ2+compterGrainesRestante();
                copie.scoreJ1 = this.scoreJ1;
                break;
            case Joueur_2:
                copie.scoreJ1 = this.scoreJ1+compterGrainesRestante();
                copie.scoreJ2 = this.scoreJ2;
                break;
        }
        copie.joueurActif = (joueurActif == Joueur.Joueur_1 ? Joueur.Joueur_2 : Joueur.Joueur_1);
        return copie;
    }

    public GameState ApplyMove(Move move) {
        if(move.graine != Graine.TRANSPARENT) move.asRed = false;
        GameState copie = new GameState(this);
        copie.plateau[move.numero_case].removeAll(Collections.singleton(move.graine));
        int i = move.numero_case;
        if(move.graine==Graine.TRANSPARENT){
            int quantite_graine = compterGraineCase(move.numero_case, move.graine);
            if(move.asRed){
                while (quantite_graine > 0) {
                    i = (i + 1) % 16;
                    copie.plateau[i].add(Graine.TRANSPARENT);
                    quantite_graine--;
                }
            }
            else{
                while (quantite_graine > 0) {
                    i = (i + 1) % 16;
                    if (joueurActif == Joueur.Joueur_1 && (i % 2 == 1)) {
                        copie.plateau[i].add(Graine.BLEU);
                        quantite_graine--;
                    } else if (joueurActif == Joueur.Joueur_2 && (i % 2 == 0)) {
                        copie.plateau[i].add(Graine.BLEU);
                        quantite_graine--;
                    }
                }
            }
        }
        if(move.graine==Graine.ROUGE || move.asRed){
            int quantite_graine = compterGraineCase(move.numero_case, Graine.ROUGE);
            while (quantite_graine > 0) {
                i = (i + 1) % 16;
                copie.plateau[i].add(Graine.ROUGE);
                quantite_graine--;
            }
        }
        if(move.graine==Graine.BLEU || !move.asRed){
            int quantite_graine = compterGraineCase(move.numero_case, Graine.BLEU);
            while (quantite_graine > 0) {
                i = (i + 1) % 16;
                if (joueurActif == Joueur.Joueur_1 && (i % 2 == 1)) {
                    copie.plateau[i].add(Graine.BLEU);
                    quantite_graine--;
                } else if (joueurActif == Joueur.Joueur_2 && (i % 2 == 0)) {
                    copie.plateau[i].add(Graine.BLEU);
                    quantite_graine--;
                }
            }
        }
        int number = move.numero_case;
        while(compterTousGraines(number)==2|| compterTousGraines(number)==3){
            if(copie.plateau[move.numero_case].isEmpty()) break;
            if (copie.joueurActif == Joueur.Joueur_1) {
                copie.scoreJ1 += copie.plateau[move.numero_case].size();
            }
            else {
                copie.scoreJ2 += copie.plateau[move.numero_case].size();
            }
            copie.plateau[move.numero_case].clear();
            number = (number - 1 + 16) % 16;
        }

        copie.joueurActif = (copie.joueurActif == Joueur.Joueur_1 ? Joueur.Joueur_2 : Joueur.Joueur_1);
        return copie;
    }

    public int evaluate(){
        return (joueurActif == Joueur.Joueur_1 ? scoreJ1 : scoreJ2);
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
    }

}