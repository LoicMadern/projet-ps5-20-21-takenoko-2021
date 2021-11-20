package fr.unice.polytech.startingpoint.type;

/**
 * Enumération contenant les types de missions disponibles
 * @author Manuel Enzo
 * @author Naud Eric
 * @author Madern Loic
 * @author Le Calloch Antoine
 * @version 2020.12.03
 */


public enum MissionType{
    PARCEL {
        @Override
        public String toString() {
            return "Parcel";
        }
    }
    ,
    PANDA {
        @Override
        public String toString() {
            return "Panda";
        }
    }
    ,
    PEASANT {
        @Override
        public String toString() {
            return "Peasant";
        }
    }
}
