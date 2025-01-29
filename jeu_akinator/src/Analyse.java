

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analyse {

    /**
     * methode qui separe les donnees selon un critere
     *
     * @param donnees  la liste des donnees a separer
     * @param critere critere de separation
     * @return la liste des donnees reparties en sous groupe dans une map : valeur du critere -> donnees correspondantes
     */
    public Map<String, List<Data>> separer(List<Data> donnees, String critere) {
        Map<String, List<Data>> res = new HashMap<String, List<Data>>();

        for (Data d : donnees) {
            String resCritere = d.getValeur(critere);

            if (res.containsKey(resCritere)) {
                res.get(resCritere).add(d);
            } else {
                List<Data> l = new ArrayList<Data>();
                l.add(d);
                res.put(resCritere, l);
            }
        }
        return res;
    }

    /**
     * retourne les probabilites d'etre dans les groupes (calculees à partir des frequences)
     *
     * @param groupes la map qui associe groupe a chaque valeur
     * @return Table : chaque valeur -> proba d'etre dans le groupe
     */
    public static Map<String, Double> calculerDistribution (Map<String, List<Data>> groupes) {
        Map<String, Double> res = new HashMap<String, Double>();

        int nombreTotal = 0;
        for (List<Data> liste : groupes.values()) {
            nombreTotal += liste.size();
        }

        for (String key : groupes.keySet()) {
            double d = groupes.get(key).size() * 1.0 / nombreTotal;
            res.put(key, (double) (d));
        }
        return res;
    }

    /**
     * calcule l'entropie d'un groupe en fonction de la sortie predite
     *
     * @param groupe groupe dont on veut l'entropie
     * @param sortie nom du critère de sortie à predire
     * @return l'entropie selon la sortie
     */
    public static double entropie(List<Data> groupe, String sortie) {
        Map<String, Integer> compteur = new HashMap<String, Integer>();
        for (Data d : groupe) {
            compteur.put(d.getValeur(sortie), compteur.getOrDefault(d.getValeur(sortie), 0) + 1);
        }

        double entropie = 0;

        for (Integer i : compteur.values()) {
            double proba = i*1.00/groupe.size();
            entropie += -(proba * Math.log(proba));
        }
        return entropie;
    }

    /**
     * retourne l'entropie moyenne associée à une séparation en sous groupe
     *
     * @param donnees groupe de depart
     * @param nomCritere le critere utilise pour séparer les données
     * @param sortie le critère final à prédire (pour calcul d'entropie)
     * @return l'entropie moyenne correspondant à séparer le groupe en sous-groupes.
     */
    public static double entropieMoyenne (List<Data> donnees, String nomCritere, String sortie) {
        Map<String, List<Data>> donneesSeparees = separer(donnees, nomCritere);
        Map<String, Double> probaSousGroupes = calculerDistribution(donneesSeparees);

        Double entropieMoy = 0.0;

        for (String key : donneesSeparees.keySet()) {
            entropieMoy += probaSousGroupes.get(key) * entropie(donneesSeparees.get(key), sortie);
        }

        return entropieMoy;
    }

    /**
     * cherche le critere qui ameliore le mieux le gain d'entropie
     *
     * @param donnees liste des critères
     * @param sortie  sortie a predire
     * @return meilleur critere ou null si aucun critere n'améliore
     */
    public static String getMeilleurCritere (List<Data> donnees, String[] criteres, String sortie) {
        String meilleurCritere = "";
        double meilleurGain = 0;

        double entropieCourante = entropie(donnees, sortie);

        for (String critere : criteres) {
            double gain = entropieCourante - entropieMoyenne(donnees, critere, sortie);
            if (gain > meilleurGain) {
                meilleurCritere = critere;
                meilleurGain = gain;
            }
        }
        return meilleurCritere;
    }
}