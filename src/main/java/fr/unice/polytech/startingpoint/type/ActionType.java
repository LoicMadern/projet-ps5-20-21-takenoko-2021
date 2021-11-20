package fr.unice.polytech.startingpoint.type;

import fr.unice.polytech.startingpoint.exception.IllegalTypeException;

import java.util.Set;

public enum ActionType {
    DRAW_CANAL {
        @Override
        public String toString() {
            return "DrawCanal";
        }
    }
    ,
    DRAW_MISSION {
        @Override
        public String toString() {
            return "DrawMission";
        }
    }
    ,
    DRAW_PARCELS {
        @Override
        public String toString() {
            return "DrawParcels";
        }
    }
    ,
    SELECT_PARCEL {
        @Override
        public String toString() {
            return "SelectParcel";
        }
    }
    ,
    PLACE_PARCEL {
        @Override
        public String toString() {
            return "PlaceParcel";
        }
    }
    ,
    PLACE_IMPROVEMENT {
        @Override
        public String toString() {
            return "PlaceImprovement";
        }
    }
    ,
    PLACE_CANAL {
        @Override
        public String toString() {
            return "PlaceCanal";
        }
    }
    ,
    WEATHER{
        @Override
        public String toString() {
            return "Utilise la météo";
        }
    }
    ,
    SUN{
        @Override
        public String toString() {
            return "Sun";
        }
    }
    ,
    WIND{
        @Override
        public String toString() {
            return "Wind";
        }
    }
    ,
    MOVE_PEASANT {
        @Override
        public String toString() {
            return "MovePeasant";
        }
    }
    ,
    MOVE_PANDA {
        @Override
        public String toString() {
            return "MovePanda";
        }
    };

    public static ActionType getCharacterAction(CharacterType characterType){
        switch (characterType){
            case PANDA:
                return MOVE_PANDA;
            case PEASANT:
                return MOVE_PEASANT;
            default:
                throw new IllegalTypeException("Wrong CharacterType to move.");
        }
    }

    public static boolean isParcelAction(ActionType actionType){
        return  actionType.equals(DRAW_PARCELS) ||
                actionType.equals(SELECT_PARCEL) ||
                actionType.equals(PLACE_PARCEL);
    }

    public static int containParcelAction(Set<ActionType> actionTypeList){
        return  (actionTypeList.contains(DRAW_PARCELS) ? 1 : 0) +
                (actionTypeList.contains(SELECT_PARCEL) ? 1 : 0) +
                (actionTypeList.contains(PLACE_PARCEL) ? 1 : 0);
    }

}
