import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameState {
    private List<Graine>[] plateau;
    private Joueur joueurActif;
    private int scoreJ1;
    private int scoreJ2;

    public GameState(ModeleAwale model) {
        this.plateau = model.getPlateauCopie();
        this.joueurActif = model.getJoueurActif();
    }

    public GameState(GameState gameState) {
        this.plateau = gameState.plateau;
        this.joueurActif = gameState.joueurActif;
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

    public GameState ApplyMove(Move move) {
        if(move.graine != Graine.TRANSPARENT) move.asRed = null;
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
    //eval()
}