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

    public String getMove(){
        StringBuilder sb = new StringBuilder();
        sb.append(numero_case + 1);
        switch (graine){
            case ROUGE:
                sb.append("R");
                break;
            case BLEU:
                sb.append("B");
                break;
            case TRANSPARENT:
                if(asRed != null && asRed){
                    sb.append("TR");
                }
                else{
                    sb.append("TB");
                }
                break;
        }
        return sb.toString();
    }

    public int getNumeroCase() { return numero_case; }
    public Graine getGraine() { return graine; }
    public Boolean getAsRed() { return asRed; }
    public Joueur getJoueur() { return joueur; }

}