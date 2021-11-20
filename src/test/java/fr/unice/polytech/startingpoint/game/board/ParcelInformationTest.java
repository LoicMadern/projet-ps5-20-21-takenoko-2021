package fr.unice.polytech.startingpoint.game.board;

import fr.unice.polytech.startingpoint.type.ColorType;
import fr.unice.polytech.startingpoint.type.ImprovementType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParcelInformationTest {
    private ParcelInformation parcelInformation;

    @BeforeEach
    void setUp(){
        parcelInformation = new ParcelInformation();
    }

    @Test
    void parcelInformationEquality(){
        assertEquals(parcelInformation,parcelInformation);
        assertEquals(new ParcelInformation(),new ParcelInformation(ColorType.NO_COLOR));
        assertEquals(new ParcelInformation(),new ParcelInformation(ImprovementType.NOTHING));
        assertEquals(new ParcelInformation(),new ParcelInformation(ColorType.NO_COLOR,ImprovementType.NOTHING));

        parcelInformation.setImprovementType(ImprovementType.WATERSHED);
        assertEquals(new ParcelInformation(ImprovementType.WATERSHED),parcelInformation);
    }
}