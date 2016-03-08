package bbc.gameoflifestub;

import java.util.HashSet;
import org.junit.Test;
import static org.junit.Assert.*;
	
public class CellTest {
	@Test
	public void testEquality()
    {
        Cell c1 = new Cell(1, 2);
        Cell c2 = new Cell(1, 2);
		assertEquals(c1, c2);
	}
	
	@Test
	public void testInequality()
    {
        Cell c1 = new Cell(1, 2);
        Cell c2 = new Cell(1, 3);
		assertFalse(c1.equals(c2));

        c1 = new Cell(0, 2);
        c2 = new Cell(1, 2);
        assertFalse(c1.equals(c2));
	}
	
	@Test
    public void testHash()
    {
        assertEquals(new Cell(1, 1).hashCode(), new Cell(1, 1).hashCode());

        HashSet<Cell> set = new HashSet<Cell>();
        set.add(new Cell(1, 1));
        set.add(new Cell(1, 1));
        set.add(new Cell(2, 2));

        assertEquals(2, set.size());
    }

    @Test
    public void testSetsOfSameCellsAreEqual(){
        HashSet<Cell> setOfCells = new HashSet<Cell>();
        setOfCells.add(new Cell(1,1));

        HashSet<Cell> setOfCells2 = new HashSet<Cell>();
        setOfCells2.add(new Cell(1,1));

        assertEquals(setOfCells, setOfCells2);
    }

}
