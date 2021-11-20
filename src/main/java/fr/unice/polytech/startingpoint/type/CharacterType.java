package fr.unice.polytech.startingpoint.type;

/**
 * Enumération contenant les types de personnages disponibles
 * @author Manuel Enzo
 * @author Naud Eric
 * @author Madern Loic
 * @author Le Calloch Antoine
 * @version 2020.12.03
 */

public enum CharacterType {
    PEASANT {
        @Override
        public String toString() {
            return "Peasant";
        }
    }
    ,
    PANDA {
        @Override
        public String toString() {
            return "Panda";
        }
    }
}
