

import java.io.IOException;
import java.util.List;

public class MainArbre {

    public static void main(String[] args) throws IOException {

        // TODO nom du fichier des donnees

        String nomDonnees = ".../.../....csv";

        // TODO chargement des donnees
        Chargement ch = new Chargement(nomDonnees);
        List<Data> personnages = ch.chargerFichier();
        String[] criteres = { "Magique", "Humain", "Super-héros", "Animal"};



        // TODO creation de l'arbre a et lancement des calculs
        Arbre akinator = new Arbre(personnages, criteres, "nom");
        akinator.creerArbre();

        // affichage de l'arbre
        ...

    }
}
