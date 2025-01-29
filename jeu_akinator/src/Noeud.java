import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Noeud {
    /**
     * pour avec des id de noeuds dans graphviz
     */
    public static int ID_NOEUD = 0;
    String id = "";

    /**
     * la liste des data du noeud
     */
    List<Data> data;

    /**
     * le nom du critère a predire
     */
    String sortie;

    /**
     * le critere conserve au niveau de ce noeud
     */
    String critereTest = null;

    /**
     * gestion des fils => structure d'arbre
     */
    Map<String, Noeud> fils;

    /**
     * constructeur simple
     *
     * @param data   donnees associes au noeud
     * @param sortie critere de sortie
     */
    public Noeud(List<Data> data, String sortie) {
        this.data = data;
        this.fils = new HashMap<>();

        this.sortie = sortie;

        // id du noeud pour graphviz
        this.id = "N" + ID_NOEUD;
        ID_NOEUD++;
    }


    /**
     * methode recursif en passant les criteres consideres pour ce noeud
     *
     * @param entrees liste des criteres utiles pour ce noeud.
     */
    public void creationArbre(String[] entrees) {
        // selection du meilleur critere pour separer les données entrees
        Analyse analyse = new Analyse();
        String critere = analyse.getMeilleurCritere(data, entrees, sortie);
        critereTest = critere;

        // vérification de l'entropie (si pas nul on continue de separer les données)
        double entropie = analyse.entropie(data, sortie);
        if (entropie != 0 && !Objects.equals(critere, "")) {

            // séparation des données en sous groupes pour chaque resultats possible
            Map<String, List<Data>> donneesSeparees = analyse.separer(data, critere);

            // on enleve le critere de la liste pour les prochains noeuds
            String[] nouveauxCriteres = retirerCritere(entrees, critere);

            if (nouveauxCriteres.length != 0) {
                // creation recursive des noeuds fils (1 pour chaque valeur du critère séléctionné)
                for (String valeur : donneesSeparees.keySet()) {
                    List<Data> donneesNoeud = donneesSeparees.get(valeur);
                    if (!donneesNoeud.isEmpty()) {
                        Noeud n = new Noeud(donneesNoeud, sortie);
                        n.creationArbre(nouveauxCriteres);
                        this.fils.put(valeur, n);
                    }
                }
            }
        }


    }
