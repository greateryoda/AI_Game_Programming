import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
public class Game{
    ModeleAwale terrain;
    MiniMaxAI ai2;
    MiniMaxAI ai1;
    public Game(){
        terrain = new ModeleAwale();
        ai1 = new MiniMaxAI(Joueur.Joueur_1,6,true);
        ai2 = new MiniMaxAI(Joueur.Joueur_2,6,true);
    }

    public void jouerUnTour(boolean botvsHuman){
        //ModeleAwale terrain = new ModeleAwale();
        terrain.afficherPlateau();
        boolean isStarving = false;
        if(terrain.getPossibleMoves().isEmpty()){
            terrain.starving();
            isStarving = true;
        }
        Boolean asRed = null;

        Boolean move_is_true = false;

        if(botvsHuman){
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
                terrain.changeJoueur();
                terrain.afficherPlateau();


            }
        }
        else{
            Move aiMove = ai1.choisirCoup(terrain);
            if(aiMove == null){
                terrain.starving();
                isStarving = true;
            }
            else{
                terrain.deplacerGraine(aiMove.getGraine(),aiMove.getNumeroCase(),aiMove.getAsRed());
                terrain.changeJoueur();
            }

        }
        terrain.afficherPlateau();
        

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
            Move aiMove = ai2.choisirCoup(terrain);
            if(aiMove == null){
                terrain.starving();
                isStarving = true;
            }
            else{
                terrain.deplacerGraine(aiMove.getGraine(),aiMove.getNumeroCase(),aiMove.getAsRed());
                terrain.changeJoueur();
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
        }
    }
    public void jouerUnePartie(boolean botVsHuman){
        System.out.println("=== Jeu Commencé ===");
        int cpt= 0;
        while (!terrain.isJeuTermine()) {
            System.out.println("\n" + terrain.getJoueurActif() + " est en train de jouer:");
            jouerUnTour(botVsHuman);
            cpt++;
        }
        System.out.println("=== Jeu Terminé ===");
        System.out.println("nombre de tour :" + cpt);
    }

    public void jouerCompet(String args[]) throws Exception {
        String name = args[0];
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String etat;
        MiniMaxAI currentAI = null;
        
        // Déterminer quel joueur est ce processus
        if ("JoueurA".equals(name)) {
            currentAI = ai1;
        } else if ("JoueurB".equals(name)) {
            currentAI = ai2;
        }
        
        while((etat = in.readLine()) != null) {
            if ("END".equals(etat)){
                System.out.println(name + ": Partie terminée");
                break;
            }
            String coup;
            if ("START".equals(etat)){
                Move aiMove = currentAI.choisirCoup(terrain);
                terrain.deplacerGraine(aiMove.getGraine(),aiMove.getNumeroCase(),aiMove.getAsRed());
                coup = aiMove.getMove();
                terrain.changeJoueur();
            } else {
                terrain.deplacerGraineFromInput(etat);
                terrain.changeJoueur();
                Move aiMove = currentAI.choisirCoup(terrain);
                terrain.deplacerGraine(aiMove.getGraine(),aiMove.getNumeroCase(),aiMove.getAsRed());
                if(terrain.isJeuTermine()){
                    coup="RESULT " + aiMove.getMove() + " " + terrain.getScore(currentAI.joueurIA) + " " + terrain.getScore(currentAI.joueurIA.opposite());
                }
                else coup = aiMove.getMove();
                terrain.changeJoueur();
            }
            System.out.println(coup);
            System.out.flush();
        }

    }
}