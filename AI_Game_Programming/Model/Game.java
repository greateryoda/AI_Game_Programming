import java.util.*;
public class Game{
    ModeleAwale terrain;
    public Game(){
        terrain = new ModeleAwale();
    }
    public static Graine decomposerGraine(String couleurPart,Boolean asRed) {
        switch (couleurPart) {
            case "R":
                return Graine.ROUGE;
            case "B":
                return Graine.BLEU;
            case "TB":  //Transparent Bleu
                return Graine.TRANSPARENT;
            case "TR":  //Transparent Rouge
                asRed = true;
                return Graine.TRANSPARENT;
            default:
                throw new AssertionError();
        }
    }   
    public void jouerUnTour(){
        //ModeleAwale terrain = new ModeleAwale();
        terrain.afficherPlateau();
        Boolean asRed = null;

        Boolean move_is_true = false;
        while(!move_is_true){
            
            Scanner sc = new Scanner(System.in);
            System.out.print("Mettez votre coup: ");
            String line = sc.nextLine().trim().toUpperCase();
            String[] parts = line.split("\\s+");
            int numero_case = Integer.parseInt(parts[0]);
            String couleurPart = parts[1];
            Graine gr = decomposerGraine(couleurPart, asRed);

            move_is_true = terrain.deplacerGraine(gr,numero_case -1 ,asRed);

        }
        

        if(terrain.isJeuTermine()){
            Joueur gagnant = terrain.getGagnant();
            int score = terrain.getScore(gagnant);
            System.out.println("Jeu est termine. Le gagnant : " + gagnant + " | Score: " + score);
        }
        else{
            terrain.changeJoueur();
        }
    }
    public void jouerUnePartie(){
        System.out.println("=== Jeu Commencé ===");
        while (!terrain.isJeuTermine()) {
            System.out.println("\n" + terrain.getJoueurActif() + " est en train de jouer:");
            jouerUnTour();
        }
        System.out.println("=== Jeu Terminé ===");
    }
}