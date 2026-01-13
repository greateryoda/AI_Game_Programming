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
        List<Move> possibleMoves = state.getPossibleMoves();
        if (possibleMoves.isEmpty()) return null;
        int nbMoves = possibleMoves.size();
        if(nbMoves >5){
            profondeurMax = 5;
        }
        else profondeurMax = 7;
        int meilleurScore = Integer.MIN_VALUE;
        int meilleurProfondeur = Integer.MAX_VALUE;
        List<Move> bestMoves = new ArrayList<>();

        for (Move move : possibleMoves) {
            Eval res = minValue(
                    state.ApplyMove(move),
                    profondeurMax - 1,
                    min,
                    max
            );

            if (res.value > meilleurScore ||
            (res.value == meilleurScore && res.depth < meilleurProfondeur)) {

                meilleurScore = res.value;
                meilleurProfondeur = res.depth;
                bestMoves.clear();
                bestMoves.add(move);
            }
            else if (res.value == meilleurScore &&
                    res.depth == meilleurProfondeur) {
                bestMoves.add(move);
            }
        }

        return bestMoves.get(new Random().nextInt(bestMoves.size()));
    }

    public Eval minValue(GameState etat,int profondeur,int alpha, int beta){
        if(profondeur==0 || etat.estTerminal()){
            return new Eval(etat.evaluate(useNewEval,joueurIA), profondeur);
        }
        int v = max;
        int bestDepth = 0;
        List<Move> coups = etat.getPossibleMoves();
        if (coups.isEmpty()){
            System.out.println("profondeur :"+profondeur);
            GameState starving = etat.starving();
            return new Eval(starving.evaluate(useNewEval,joueurIA), profondeur);
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
            return new Eval(etat.evaluate(useNewEval, joueurIA), profondeur);
        }
        int v = min;
        int bestDepth = 0;
        List<Move> coups = etat.getPossibleMoves();
        if (coups.isEmpty()){
            GameState starving = etat.starving();
            return new Eval(starving.evaluate(useNewEval, joueurIA), profondeur);
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
            alpha = Math.max(alpha,v);
        }
        return new Eval(v, bestDepth);
    }
}

