package Game;

import static org.junit.Assert.*;

import org.junit.Test;

import Game.Enum.GameType;

public class GameTest {
    Game g ;
    Player p1 = new Player(20, null, g);
    Player p2 = new Player(20, null, g);



	@Test
	public void testRun() {
		GamePreferences prefs;
		try {
			prefs = new GamePreferences(GameType.NO_LIMIT, 0, 1000, 100, 2, 8, true,false,0);
		}
		catch(Exception e) {
			return;
		}
		g = new Game(p1, prefs, 123);
		g.joinGame(p2);
		Thread th = new Thread (g);
	    th.start();
	    int i=100;
	    while(p1.getCards()[0] == null || p1.getCards()[1] == null || p2.getCards()[0] == null || p2.getCards()[1] == null) i--;
	    assertNotEquals(p1.getCards()[0],null);
	    assertNotEquals(p1.getCards()[1],null);
	    assertNotEquals(p2.getCards()[0],null);
	    assertNotEquals(p2.getCards()[1],null);
	    
	    assertTrue((p1.getCards()[0].getNumber() != p1.getCards()[1].getNumber()) || (p1.getCards()[0].getType() != p1.getCards()[1].getType()));
	    assertTrue((p2.getCards()[0].getNumber() != p2.getCards()[1].getNumber()) || (p2.getCards()[0].getType() != p2.getCards()[1].getType()));
	    
	    
	    assertTrue((p1.getCards()[0].getNumber() != p2.getCards()[0].getNumber()) || (p1.getCards()[0].getType() != p2.getCards()[0].getType()));
	    assertTrue((p2.getCards()[1].getNumber() != p1.getCards()[1].getNumber()) || (p2.getCards()[1].getType() != p1.getCards()[1].getType()));
	    
	}

}
