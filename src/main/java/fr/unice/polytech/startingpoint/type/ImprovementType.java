package fr.unice.polytech.startingpoint.type;

public enum ImprovementType {
    ENCLOSURE {
        @Override
        public String toString() {
            return "Enclosure";
        }
    }
    ,
    FERTILIZER {
        @Override
        public String toString() {
            return "Fertilizer";
        }
    }
    ,
    WATERSHED {
        @Override
        public String toString() {
            return "Watershed";
        }
    }
    ,
    NOTHING {
        @Override
        public String toString() {
            return "Nothing";
        }
    }
    ,
    WHATEVER {
        @Override
        public String toString() {
            return "Whatever";
        }

    }
}
