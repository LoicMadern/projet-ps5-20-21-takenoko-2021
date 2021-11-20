package fr.unice.polytech.startingpoint.type;

/**
 * Enumération contenant les types de robots disponibles
 * @author Manuel Enzo
 * @author Naud Eric
 * @author Madern Loic
 * @author Le Calloch Antoine
 * @version 2020.12.03
 */

public enum BotType {
    RANDOM {
        @Override
        public String toString() {
            return "Random Bot";
        }
    }
    ,
    PARCEL_BOT {
        @Override
        public String toString() {
            return "Parcel Bot";
        }
    }
    ,
    PANDA_BOT {
        @Override
        public String toString() {
            return "Panda Bot";
        }
    }
    ,
    PEASANT_BOT {
        @Override
        public String toString() {
            return "Peasant Bot";
        }
    }
    ,
    INTELLIGENT_BOT {
        @Override
        public String toString() {
            return "Intelligent Bot";
        }
    }
}
