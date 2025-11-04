

public enum Joueur {
    Joueur_1, 
    Joueur_2;

    public Joueur opposite() {
        return this == Joueur_1 ? Joueur_2 : Joueur_1;
    }
    
}
