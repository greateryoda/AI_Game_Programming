import java.io.*;
import java.util.concurrent.*;

public class Arbitre {
    private static final int TIMEOUT_SECONDS = 15;

    public static void main(String[] args) throws Exception {
        //Process A = Runtime.getRuntime().exec("C:\\Users\\mleve\\Desktop\\Cours\\2025\\2026\\AI_Game_Programming\\AI_Game_Programming\\external_player.exe JoueurA");
        Process A = Runtime.getRuntime().exec("java -jar C:\\Users\\mleve\\Desktop\\Cours\\2025\\2026\\AI_Game_Programming\\AI_Game_Programming\\external_player.jar JoueurA");
        //        Process A = new ProcessBuilder("./A").start();
        // Pour lancer un code java COMPILE : voir https://www.baeldung.com/java-process-api
        // process = Runtime.getRuntime().exec("java -cp src/main/java com.baeldung.java9.process.OutputStreamExample");
        // sinon il faut lancer la compil avant (puis lancer le code compilé):
//        Process process = Runtime.getRuntime().exec("javac -cp src src\\main\\java\\com\\baeldung\\java9\\process\\OutputStreamExample.java");
        Process B = Runtime.getRuntime().exec("java -cp C:\\Users\\mleve\\Desktop\\Cours\\2025\\2026\\AI_Game_Programming\\AI_Game_Programming\\bin principale JoueurB");
        //Process B = new ProcessBuilder("./B").start();

        Joueur joueurA = new Joueur("A", A);
        Joueur joueurB = new Joueur("B", B);

        Joueur courant = joueurA;
        Joueur autre = joueurB;

        String coup = "START";
        int nbCoups = 0;
        while (true) {
            try {
                // Reception du coup de l'adversaire
                courant.receive(coup);
                // reponse avec TIMEOUT
                coup = courant.response(TIMEOUT_SECONDS);
                if (coup == null) {
                    disqualifier(courant, "timeout");
                    break;
                }
                nbCoups++;
                if (nbCoups == 400) {
                    System.out.println("RESULT LIMIT");
                }
                // Validation du coup
//            if (!coupValide(reponse)) {
//                disqualifier(courant, "coup invalide : " + reponse);
//                break;
//            }

                System.out.println(courant.nom + " -> " + coup);
                // Fin de partie
                if (coup.contains("RESULT")) {
                    System.out.println(coup);
                    break;
                }
                // Changement de joueur
                Joueur tmp = courant;
                courant = autre;
                autre = tmp;
            } catch (IOException e) {
                disqualifier(courant, "erreur de communication: " + e.getMessage());
                break;
            }
        }
        joueurA.destroy();
        joueurB.destroy();
        System.out.println("Fin.");
    }

    // ===============================
    // Validation du coup (À ADAPTER)
    // ===============================
    private static boolean coupValide(String coup) {
        try {
            int c = Integer.parseInt(coup);
            return c >= 0 && c <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void disqualifier(Joueur j, String raison) {
        System.out.println("RESULT Joueur " + j.nom + " disqualifié (" + raison + ")");
    }

    // ===============================
    // Classe Joueur
    // ===============================
    static class Joueur {
        String nom;
        Process process;
        BufferedWriter in;
        BufferedReader out;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        private boolean processAlive = true;

        Joueur(String nom, Process p) {
            this.nom = nom;
            this.process = p;
            this.in = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            this.out = new BufferedReader(new InputStreamReader(p.getInputStream()));
        }

        void receive(String msg) throws IOException {
            if (!processAlive || !process.isAlive()) {
                processAlive = false;
                throw new IOException("Le processus " + nom + " n'est pas actif");
            }
            try {
                in.write(msg);
                in.newLine();
                in.flush();
            } catch (IOException e) {
                processAlive = false;
                throw e;
            }
        }
        String response(int timeoutSeconds) throws IOException {
            if (!processAlive || !process.isAlive()) {
                processAlive = false;
                throw new IOException("Le processus " + nom + " n'est pas actif");
            }
            Future<String> future = executor.submit(() -> out.readLine());
            try {
                return future.get(timeoutSeconds, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                future.cancel(true);
                return null;
            } catch (Exception e) {
                processAlive = false;
                return null;
            }
        }

        void destroy() {
            processAlive = false;
            executor.shutdownNow();
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                // Ignore
            }
            process.destroy();
        }
    }
}
