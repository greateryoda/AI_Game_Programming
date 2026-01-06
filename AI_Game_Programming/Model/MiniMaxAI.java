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
    public Joueur joueurIA;
    private int profondeurMax;
    private boolean solutionFound;
    private boolean useNewEval = false;

    public MiniMaxAI(Joueur joueurIA,int profondeurMax,boolean useNewEval){
        this.joueurIA = joueurIA;
        this.joueurIA = joueurIA;
        this.profondeurMax = Math.max(1,profondeurMax);
        this.solutionFound = false;
        this.useNewEval = useNewEval;
    }

    public void setJoueurIA(Joueur joueurIA){
        this.joueurIA = joueurIA;
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
                    ? minValue(state.ApplyMove(move),profondeurMax-1,min,max)
                    : maxValue(state.ApplyMove(move),profondeurMax-1,min,max);
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
        int nombre_alea = random.nextInt(bestMoves.size());

        meilleurCoup = bestMoves.get(nombre_alea);


        return meilleurCoup;

    }

    public Eval minValue(GameState etat,int profondeur,int alpha, int beta){
        if(profondeur==0 || etat.estTerminal()){
            return new Eval(etat.evaluate(useNewEval), profondeur);
        }
        int v = max;
        int bestDepth = 0;
        List<Move> coups = etat.getPossibleMoves();
        if (coups.isEmpty()){
            System.out.println("profondeur :"+profondeur);
            GameState starving = etat.starving();
            return new Eval(starving.evaluate(useNewEval), profondeur);
        }
        for(Move coup : coups){
            Eval res = maxValue(etat.ApplyMove(coup),profondeur-1,alpha,beta);
            if(res.value < v){
                v = res.value;
                bestDepth = res.depth;
            }
            if(v <=alpha){
                return new Eval(v, bestDepth);
            }
            beta = Math.min(beta,v);
        }
        return new Eval(v, bestDepth);
    }

    public Eval maxValue(GameState etat,int profondeur,int alpha,int beta){
        if(profondeur==0 || etat.estTerminal()){
            return new Eval(etat.evaluate(useNewEval), profondeur);
        }
        int v = min;
        int bestDepth = 0;
        List<Move> coups = etat.getPossibleMoves();
        if (coups.isEmpty()){
            GameState starving = etat.starving();
            return new Eval(starving.evaluate(useNewEval), profondeur);
        }
        for(Move coup : coups){
            Eval res = minValue(etat.ApplyMove(coup),profondeur-1,alpha,beta);
            if(res.value > v){
                v = res.value;
                bestDepth = res.depth;
            }
            if(v >=beta){
                return new Eval(v, bestDepth);
            }
            alpha = Math.min(alpha,v);
        }
        return new Eval(v, bestDepth);
    }
}

