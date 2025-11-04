public class Move{
    int numero_case;
    Joueur joueur;
    Graine graine;
    Boolean asRed;

    public Move(int numeroCase, Graine graine, Boolean asRed, Joueur joueur){
        this.numero_case = numeroCase;
        this.graine = graine;
        this.asRed = asRed;
        this.joueur = joueur;
    }

    public int getNumeroCase() { return numero_case; }
    public Graine getGraine() { return graine; }
    public Boolean getAsRed() { return asRed; }
    public Joueur getJoueur() { return joueur; }
}