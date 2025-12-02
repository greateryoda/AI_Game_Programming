import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Eval{
    final int value;
    final int depth;

    Eval(int value, int depth){
        this.value = value;
        this.depth = depth;
    }
}

public class MiniMaxAI {
    private int max = Integer.MAX_VALUE;
    private int min = Integer.MIN_VALUE;
    private Joueur joueurIA;
    private int profondeurMax;

    public MiniMaxAI(Joueur joueurIA,int profondeurMax) {
        this.joueurIA = joueurIA;
        this.profondeurMax = Math.max(1,profondeurMax);
    }

    public Move choisirCoup(ModeleAwale gameState){
        GameState state = new GameState(gameState);
        List<Move> possibleMove = state.getPossibleMoves();
        List<Move> bestMoves = new ArrayList<Move>();
        if(possibleMove.isEmpty()) return null;

        int meilleurScore = (joueurIA == Joueur.Joueur_1) ? min : max;
        int meilleurProfondeur = 0;
        Move meilleurCoup = null;
        bestMoves.clear();

        for(Move move : possibleMove){
            Eval res = (joueurIA == Joueur.Joueur_1)
                    ? minValue(state.ApplyMove(move),profondeurMax-1)
                    : maxValue(state.ApplyMove(move),profondeurMax-1);
            if ((joueurIA == Joueur.Joueur_1 && res.value > meilleurScore) || (joueurIA == Joueur.Joueur_2 && res.value < meilleurScore)) {
                meilleurScore = res.value;
                meilleurProfondeur = res.depth;
                bestMoves.clear();
                bestMoves.add(move);
            }
            else if(res.value == meilleurScore){
                if(res.depth<meilleurProfondeur){
                    meilleurProfondeur = res.depth;
                    bestMoves.clear();
                    bestMoves.add(move);
                }
                else if(res.depth == meilleurProfondeur){
                    bestMoves.add(move);
                }
            }
        }

        Random random = new Random();
        System.out.println("taille bestMoves :" + bestMoves.size());
        int nombre_alea = random.nextInt(bestMoves.size());

        meilleurCoup = bestMoves.get(nombre_alea);

        return meilleurCoup;

    }

    public Eval minValue(GameState etat,int profondeur){
        if(profondeur==0 || etat.estTerminal()){
            return new Eval(etat.evaluate(), profondeur);
        }
        int v = max;
        int bestDepth = 0;
        List<Move> coups = etat.getPossibleMoves();
        if (coups.isEmpty()){
            System.out.println("profondeur :"+profondeur);
            etat.afficherPlateau();
            GameState starving = etat.starving();
            return new Eval(starving.evaluate(), profondeur);
        }
        for(Move coup : coups){
            Eval res = maxValue(etat.ApplyMove(coup),profondeur--);
            if(res.value < v){
                v = res.value;
                bestDepth = res.depth;
            }
        }
        return new Eval(v, bestDepth);
    }

    public Eval maxValue(GameState etat,int profondeur){
        if(profondeur==0 || etat.estTerminal()){
            return new Eval(etat.evaluate(), profondeur);
        }
        int v = min;
        int bestDepth = 0;
        List<Move> coups = etat.getPossibleMoves();
        if (coups.isEmpty()){
            GameState starving = etat.starving();
            return new Eval(starving.evaluate(), profondeur);
        }
        for(Move coup : coups){
            Eval res = minValue(etat.ApplyMove(coup),profondeur--);
            if(res.value > v){
                v = res.value;
                bestDepth = res.depth;
            }
        }
        return new Eval(v, bestDepth);
    }
}

