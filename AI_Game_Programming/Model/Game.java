import java.util.*;
public class Game{
    ModeleAwale terrain;
    public Game(){
        terrain = new ModeleAwale();
    }

    public void jouerUnTour(){
        //ModeleAwale terrain = new ModeleAwale();
        terrain.afficherPlateau();
        boolean isStarving = false;
        if(terrain.getPossibleMoves().isEmpty()){
            terrain.starving();
            isStarving = true;
        }
        Boolean asRed = null;

        Boolean move_is_true = false;
        while(!move_is_true && !isStarving){
            
            Scanner sc = new Scanner(System.in);
            System.out.print("Mettez votre coup: ");
            String line = sc.nextLine().trim().toUpperCase();
            String[] parts = line.split("\\s+");
            int numero_case = Integer.parseInt(parts[0]);
            String couleurPart = parts[1];
            Graine gr;
            switch (couleurPart) {
                case "R":
                    gr = Graine.ROUGE;
                    break;
                case "B":
                    gr = Graine.BLEU;
                    break;
                case "TB":
                    asRed= false;//Transparent Bleu
                    gr = Graine.TRANSPARENT;
                    break;
                case "TR":  //Transparent Rouge
                    asRed = true;
                    gr = Graine.TRANSPARENT;
                    break;
                default:
                    throw new AssertionError();
            }

            move_is_true = terrain.deplacerGraine(gr,numero_case -1 ,asRed);

        }
        

        if(terrain.isJeuTermine()){
            if(terrain.getDraw()){
                System.out.println("Le jeu est termine. Egualité");
            }
            else{
                Joueur gagnant = terrain.getGagnant();
                int score = terrain.getScore(gagnant);
                System.out.println("Jeu est termine. Le gagnant : " + gagnant + " | Score: " + score);
            }
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