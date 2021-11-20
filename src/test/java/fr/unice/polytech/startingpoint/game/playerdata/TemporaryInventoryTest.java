package fr.unice.polytech.startingpoint.game.playerdata;

import fr.unice.polytech.startingpoint.exception.OutOfResourcesException;
import fr.unice.polytech.startingpoint.game.board.Parcel;
import fr.unice.polytech.startingpoint.game.board.ParcelInformation;
import fr.unice.polytech.startingpoint.type.ActionType;
import fr.unice.polytech.startingpoint.type.WeatherType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TemporaryInventoryTest {
    private TemporaryInventory temporaryInventory;

    @BeforeEach
    void setUp(){
        temporaryInventory = new TemporaryInventory();
    }

    @Nested
    class staminaTest{

        @Test
        void staminaLoose() throws OutOfResourcesException {
            assertEquals(2,temporaryInventory.getStamina());

            temporaryInventory.looseStamina();

            assertEquals(1,temporaryInventory.getStamina());
        }

        @Test
        void outOfStamina() throws OutOfResourcesException {
            assertEquals(2,temporaryInventory.getStamina());

            temporaryInventory.looseStamina();
            temporaryInventory.looseStamina();

            assertEquals(0,temporaryInventory.getStamina());
            assertThrows(OutOfResourcesException.class, () -> temporaryInventory.looseStamina());
        }
        @Test
        void staminaReset() throws OutOfResourcesException {
            assertEquals(2,temporaryInventory.getStamina());

            temporaryInventory.looseStamina();

            assertEquals(1,temporaryInventory.getStamina());

            temporaryInventory.looseStamina();

            assertEquals(0,temporaryInventory.getStamina());

            temporaryInventory.reset();

            assertEquals(2,temporaryInventory.getStamina());
        }
    }

    @Nested
    class parcelListAndParcelTest{

        @Test
        void saveParcel() throws OutOfResourcesException {
            assertNull(temporaryInventory.getParcel());

            temporaryInventory.saveParcel(new Parcel());

            assertEquals(new ParcelInformation(),temporaryInventory.getParcel().getParcelInformation());
        }

        @Test
        void parcelReset() throws OutOfResourcesException {
            assertNull(temporaryInventory.getParcel());

            temporaryInventory.saveParcel(new Parcel());
            temporaryInventory.reset();

            assertNull(temporaryInventory.getParcel());
        }

        @Test
        void saveParcels(){
            assertTrue(temporaryInventory.getParcelsSaved().isEmpty());

            temporaryInventory.saveParcels(new ArrayList<>(Arrays.asList(new Parcel(),new Parcel(),new Parcel())));

            assertEquals(3,temporaryInventory.getParcelsSaved().size());
        }

        @Test
        void parcelsReset(){
            assertTrue(temporaryInventory.getParcelsSaved().isEmpty());

            temporaryInventory.saveParcels(new ArrayList<>(Arrays.asList(new Parcel(),new Parcel(),new Parcel())));
            temporaryInventory.reset();

            assertTrue(temporaryInventory.getParcelsSaved().isEmpty());
        }
    }

    @Nested
    class actionTypeListTest{

        @Test
        void addActionType(){
            assertTrue(temporaryInventory.getActionTypeList().isEmpty());

            temporaryInventory.add(ActionType.DRAW_PARCELS);

            assertEquals(1,temporaryInventory.getActionTypeList().size());
        }

        @Test
        void removeActionType(){
            assertTrue(temporaryInventory.getActionTypeList().isEmpty());

            temporaryInventory.add(ActionType.DRAW_PARCELS);
            temporaryInventory.remove(ActionType.DRAW_PARCELS);

            assertTrue(temporaryInventory.getActionTypeList().isEmpty());
        }

        @Test
        void actionTypeListReset(){
            assertTrue(temporaryInventory.getActionTypeList().isEmpty());

            temporaryInventory.add(ActionType.DRAW_PARCELS);
            temporaryInventory.reset();

            assertTrue(temporaryInventory.getActionTypeList().isEmpty());
        }

        @Test
        void hasPlayedCorrectlyTest(){
            assertDoesNotThrow(() -> temporaryInventory.hasPlayedCorrectly());

            temporaryInventory.add(ActionType.DRAW_PARCELS);

            assertThrows(NoSuchElementException.class,() -> temporaryInventory.hasPlayedCorrectly());

            temporaryInventory.add(ActionType.SELECT_PARCEL);

            assertThrows(NoSuchElementException.class,() -> temporaryInventory.hasPlayedCorrectly());

            temporaryInventory.add(ActionType.PLACE_PARCEL);

            assertDoesNotThrow(() -> temporaryInventory.hasPlayedCorrectly());
        }

        @Test
        void hasPlayedCorrectlyReset(){
            assertDoesNotThrow(() -> temporaryInventory.hasPlayedCorrectly());

            temporaryInventory.add(ActionType.DRAW_PARCELS);
            temporaryInventory.reset();

            assertDoesNotThrow(() -> temporaryInventory.hasPlayedCorrectly());
        }
    }
    
    @Nested
    class windTest{

        @Test void weatherDifferentFromSunSo2Stamina(){
            //de base no_weather
            assertEquals(2,temporaryInventory.getStamina());
            temporaryInventory.reset(WeatherType.NO_WEATHER);
            assertEquals(2,temporaryInventory.getStamina());
        }

        @Test void sunWeatherSo3Stamina(){
            temporaryInventory.reset(WeatherType.SUN);
            assertEquals(3,temporaryInventory.getStamina());
        }

        @Test void noWindSoNoDoubleAction(){
            assertFalse(temporaryInventory.isActionCouldBeDoneTwice());
            temporaryInventory.setWeatherType(WeatherType.SUN);
            assertFalse(temporaryInventory.isActionCouldBeDoneTwice());
        }

        @Test void windWeather(){
            temporaryInventory.reset(WeatherType.WIND);
            assertTrue(temporaryInventory.isActionCouldBeDoneTwice());
        }
    }
}