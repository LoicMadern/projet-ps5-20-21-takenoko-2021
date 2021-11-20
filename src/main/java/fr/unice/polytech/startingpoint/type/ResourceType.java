package fr.unice.polytech.startingpoint.type;

import fr.unice.polytech.startingpoint.exception.IllegalTypeException;

public enum ResourceType {
    PARCEL_MISSION {
        @Override
        public String toString() {
            return "ParcelMission";
        }
    }
    ,
    PANDA_MISSION {
        @Override
        public String toString() {
            return "PandaMission";
        }
    }
    ,
    PEASANT_MISSION {
        @Override
        public String toString() {
            return "PeasantMission";
        }
    }
    ,
    ALL_MISSION {
        @Override
        public String toString() {
            return "AllMission";
        }
    }
    ,
    CANAL {
        @Override
        public String toString() {
            return "Canal";
        }
    }
    ,
    PARCEL {
        @Override
        public String toString() {
            return "Parcel";
        }
    }
    ,
    ALL_IMPROVEMENT {
        @Override
        public String toString() {
            return "Improvement";
        }
    }
    ,
    WATERSHED_IMPROVEMENT {
        @Override
        public String toString() {
            return "Watershed Improvement";
        }
    }
    ,
    ENCLOSURE_IMPROVEMENT {
        @Override
        public String toString() {
            return "Enclosure Improvement";
        }
    }
    ,
    FERTILIZER_IMPROVEMENT {
        @Override
        public String toString() {
            return "Fertilizer Improvement";
        }
    };

    public static ResourceType get(MissionType missionType){
        switch (missionType){
            case PANDA:
                return PANDA_MISSION;
            case PEASANT:
                return PEASANT_MISSION;
            case PARCEL:
                return PARCEL_MISSION;
            default:
                throw new IllegalTypeException("Wrong CharacterType to move.");
        }
    }
}
