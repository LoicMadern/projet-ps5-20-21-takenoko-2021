package fr.unice.polytech.startingpoint.bot;

import fr.unice.polytech.startingpoint.game.Game;
import fr.unice.polytech.startingpoint.game.board.*;
import fr.unice.polytech.startingpoint.type.ColorType;
import fr.unice.polytech.startingpoint.type.ImprovementType;
import fr.unice.polytech.startingpoint.type.WeatherType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

class RandomBotTest {
    private Game game;
    private RandomBot rdmBot1;
    private Board board;
    private Resource resource;
    private BoardRules boardRules;

    @BeforeEach
    void setUp() {

        game = new Game();
        boardRules = game.getRules();
        board = game.getBoard();
        rdmBot1 = (RandomBot) game.getPlayerData().getBot();
        resource = game.getResource();
    }

    @Test
    void drawMissionParcel() {
        Random mockRand = mock(Random.class);
        Random mockRand2 = mock(Random.class);
        Mockito.when(mockRand.nextInt(5)).thenReturn(0);//donne une val au random pour piocher une mission
        Mockito.when(mockRand2.nextInt(3)).thenReturn(0);//donne une val au random pour choisir la mission
        rdmBot1.setRand(mockRand, mockRand2);//set les Random mock

        assertEquals(1, game.getGameInteraction().getInventoryParcelMissions().size());
        rdmBot1.botPlay(WeatherType.NO_WEATHER);
        assertEquals(2, game.getGameInteraction().getInventoryParcelMissions().size());
    }

    @Test
    void drawMissionPanda() {
        Random mockRand = mock(Random.class);
        Random mockRand2 = mock(Random.class);
        Mockito.when(mockRand.nextInt(5)).thenReturn(0);//donne une val au random pour piocher une mission
        Mockito.when(mockRand2.nextInt(3)).thenReturn(1);//donne une val au random pour choisir la mission
        rdmBot1.setRand(mockRand, mockRand2);//set les Random mock

        assertEquals(1, game.getGameInteraction().getInventoryPandaMissions().size());
        rdmBot1.botPlay(WeatherType.NO_WEATHER);

        assertEquals(2, game.getGameInteraction().getInventoryPandaMissions().size());
    }

    @Test
    void drawMissionPeasant() {
        Random mockRand = mock(Random.class);
        Random mockRand2 = mock(Random.class);
        Mockito.when(mockRand.nextInt(5)).thenReturn(0);//donne une val au random pour piocher une mission
        Mockito.when(mockRand2.nextInt(3)).thenReturn(2);//donne une val au random pour choisir la mission
        rdmBot1.setRand(mockRand, mockRand2);//set les Random mock

        assertEquals(1, game.getGameInteraction().getInventoryPeasantMissions().size());
        rdmBot1.botPlay(WeatherType.NO_WEATHER);

        assertEquals(2, game.getGameInteraction().getInventoryPeasantMissions().size());
    }

    @Test
    void putCanal(){
        Random mockRand = mock(Random.class);
        board.placeParcel(new Parcel(ColorType.NO_COLOR, ImprovementType.NOTHING), new Coordinate(1, -1, 0));//ajoute une pièce ou mettre le canal
        board.placeParcel(new Parcel(ColorType.NO_COLOR,ImprovementType.NOTHING), new Coordinate(0, -1, 1));//ajoute une pièce ou mettre le canal
        Mockito.when(mockRand.nextInt(5)).thenReturn(1);//donne une val au random pour piocher une mission
        rdmBot1.setRand(mockRand, new Random());//set les Random mock

        assertEquals(0, board.getPlacedCanals().size());
        rdmBot1.botPlay(WeatherType.NO_WEATHER);
        assertEquals(1, board.getPlacedCanals().size());
    }

    @Test
    void putParcel(){
        Random mockRand = mock(Random.class);
        Mockito.when(mockRand.nextInt(5)).thenReturn(2);//donne une val au random pour piocher une mission
        rdmBot1.setRand(mockRand,new Random());//set les Random mock

        assertEquals(1,board.getPlacedParcels().size());//1 parcel (central)
        rdmBot1.botPlay(WeatherType.NO_WEATHER);
        assertEquals(2,board.getPlacedParcels().size());//2 parcels (central + la parcel posée)
    }

    @Test
    void movePanda(){
        Random mockRand = mock(Random.class);
        Mockito.when(mockRand.nextInt(5)).thenReturn(3);//donne une val au random pour piocher une mission
        Coordinate central = new Coordinate(0,0,0);
        board.placeParcel(new Parcel(), new Coordinate(1,-1,0));//ajoute une pièce ou mettre le panda
        rdmBot1.setRand(mockRand,new Random());//set les Random mock

        assertEquals(central,board.getPandaCoordinate());//Le Panda est au centre
        rdmBot1.botPlay(WeatherType.NO_WEATHER);
        assertNotEquals(central,board.getPandaCoordinate());//Le Panda n'est plus au centre

    }

    @Test
    void movePeasant(){
        Random mockRand = mock(Random.class);
        Mockito.when(mockRand.nextInt(5)).thenReturn(4);//donne une val au random pour piocher une mission
        Coordinate central = new Coordinate(0,0,0);
        Parcel parcel1Bamboo = new Parcel();//parcel qui aura plus d'un bamboo pour recevoir le paysan
        parcel1Bamboo.addBamboo();//ajout d'un bamboo
        board.placeParcel(new Parcel(), new Coordinate(1,-1,0));//ajoute une pièce ou mettre le paysan
        rdmBot1.setRand(mockRand,new Random());//set les Random mock
        assertEquals(central,board.getPeasantCoordinate());//Le Paesant est au centre
        rdmBot1.botPlay(WeatherType.NO_WEATHER);
        assertNotEquals(central,board.getPeasantCoordinate());//Le Paesant n'est plus au centre
    }
}
