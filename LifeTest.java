package bbc.gameoflifestub;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
	
public class LifeTest {

	protected Set<Cell> setOfCells;
	protected Life life;
	
	@Before
	public void setUp(){
    	setOfCells = new HashSet<Cell>();
    	setOfCells.add(new Cell(1,1));
    	setOfCells.add(new Cell(2,2));
        life = new Life(setOfCells);
    }
	
	@Test
	public void testInitialisation()
	{
		assertEquals(2, life.getLiveCells().size());
	}

    @Test
    public void testUnderpopulation()
    {
        assertFalse(life.cellShouldSurvive(0));
        assertFalse(life.cellShouldSurvive(1));
    }
    
    @Test
    public void testOvercrowding()
    {
        assertFalse(life.cellShouldSurvive(4));
        assertFalse(life.cellShouldSurvive(5));
        assertFalse(life.cellShouldSurvive(6));
        assertFalse(life.cellShouldSurvive(7));
        assertFalse(life.cellShouldSurvive(8));
    }
    
    @Test
    public void testSurvival()
    {
    	assertTrue(life.cellShouldSurvive(2));
        assertTrue(life.cellShouldSurvive(3));
    }
    
    @Test
    public void testCreationOfLife()
    {
    	assertFalse(life.cellShouldBeCreated(1));
    	assertFalse(life.cellShouldBeCreated(2));
    	assertTrue(life.cellShouldBeCreated(3));
    	assertFalse(life.cellShouldBeCreated(4));
    }
    
    @Test
    public void testGridWithNoliveCells()
    {
    	Set<Cell> setOfCells = new HashSet<Cell>();
    	Life life = new Life(setOfCells);
    	assertEquals(0, life.getLiveCells().size());
    	life.turn();
    	assertEquals(0, life.getLiveCells().size());
    }    
    
    @Test
    public void testSeededgGrid1()
    {
    	Set<Cell> setOfCells = new HashSet<Cell>();
    	setOfCells.add(new Cell(1,2));
    	setOfCells.add(new Cell(2,2));
    	setOfCells.add(new Cell(3,2));
    	Life life = new Life(setOfCells);
    	
    	assertEquals(3, life.getLiveCells().size());
    	life.turn();
    	assertEquals(3, life.getLiveCells().size());
    	assertTrue(life.getLiveCells().contains(new Cell(2,1)));
    	assertTrue(life.getLiveCells().contains(new Cell(2,2)));
    	assertTrue(life.getLiveCells().contains(new Cell(2,3)));    	
    }
    
    @Test
    public void testSeededgGrid2()
    {
    	Set<Cell> setOfCells = new HashSet<Cell>();
    	setOfCells.add(new Cell(1,2));
    	setOfCells.add(new Cell(2,2));
    	setOfCells.add(new Cell(3,2));
    	Life life = new Life(setOfCells);
    	
    	assertEquals(3, life.getLiveCells().size());
    	life.turn();
    	life.turn();
    	assertEquals(3, life.getLiveCells().size());
    	assertTrue(life.getLiveCells().contains(new Cell(1,2)));
    	assertTrue(life.getLiveCells().contains(new Cell(2,2)));
    	assertTrue(life.getLiveCells().contains(new Cell(3,2)));    	
    }
    
}
