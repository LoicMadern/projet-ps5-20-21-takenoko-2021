package fr.unice.polytech.startingpoint.type;

public enum WeatherType {
    SUN{
        @Override
        public String toString(){return "sun";}
    }
    ,
    WIND{
        @Override
        public String toString(){return "wind";}
    }
    ,
    RAIN{
        @Override
        public String toString(){return "wind";}
    }
    ,
    THUNDERSTORM{
        @Override
        public String toString(){return "thunderstorm";}
    }
    ,
    CLOUD{
        @Override
        public String toString(){return "thunderstorm";}
    }
    ,
    QUESTION_MARK{
        @Override
        public String toString(){return "question mark";}
    }
    ,
    NO_WEATHER{
        @Override
        public String toString(){return "no weather";}
    }
}
