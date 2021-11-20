package fr.unice.polytech.startingpoint.game.playerdata;

import fr.unice.polytech.startingpoint.exception.OutOfResourcesException;
import fr.unice.polytech.startingpoint.game.board.Canal;
import fr.unice.polytech.startingpoint.type.ColorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryTest {
    private Inventory inventory;

    @BeforeEach
    void setUp(){
        inventory = new Inventory();
    }

    @Test
    void inventoryCanal() throws OutOfResourcesException {
        inventory.addCanal(new Canal());
        inventory.pickCanal();
        assertThrows(OutOfResourcesException.class, () -> inventory.pickCanal());
    }

    @Test
    void addBambooInventory() {
        inventory.addBamboo(ColorType.GREEN);
        assertEquals(1, inventory.getInventoryBamboo()[ColorType.GREEN.ordinal()]);
        assertEquals(0, inventory.getInventoryBamboo()[ColorType.YELLOW.ordinal()]);
        assertEquals(0, inventory.getInventoryBamboo()[ColorType.RED.ordinal()]);
    }

    @Test
    void deleteTwoBambooInventory() {
        inventory.addBamboo(ColorType.GREEN);
        inventory.addBamboo(ColorType.GREEN);
        inventory.subTwoBamboos(ColorType.GREEN);
        assertEquals(0, inventory.getInventoryBamboo()[ColorType.GREEN.ordinal()]);
    }

    @Test
    void deleteOneBambooPerColoInventory() {
        inventory.addBamboo(ColorType.GREEN);
        inventory.addBamboo(ColorType.YELLOW);
        inventory.addBamboo(ColorType.RED);
        inventory.subOneBambooPerColor();
        assertEquals(0, inventory.getInventoryBamboo()[ColorType.GREEN.ordinal()]);
        assertEquals(0, inventory.getInventoryBamboo()[ColorType.YELLOW.ordinal()]);
        assertEquals(0, inventory.getInventoryBamboo()[ColorType.RED.ordinal()]);
    }

    @Test
    void deleteOneBambooPerColoInventoryNotEnoughBamboo() {
        inventory.addBamboo(ColorType.GREEN);
        inventory.addBamboo(ColorType.YELLOW);
        inventory.subOneBambooPerColor();
        assertEquals(1, inventory.getInventoryBamboo()[ColorType.GREEN.ordinal()]);
        assertEquals(1, inventory.getInventoryBamboo()[ColorType.YELLOW.ordinal()]);
        assertEquals(0, inventory.getInventoryBamboo()[ColorType.RED.ordinal()]);
    }

    @Test
    void deleteTwoBambooInventoryNotEnoughBamboo() {
        inventory.addBamboo(ColorType.GREEN);
        inventory.subTwoBamboos(ColorType.GREEN);
        assertEquals(1, inventory.getInventoryBamboo()[ColorType.GREEN.ordinal()]);
    }
}