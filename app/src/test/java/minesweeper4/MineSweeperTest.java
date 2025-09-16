package minesweeper4;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.*;

public class MineSweeperTest {
	@Test
	public void testJunit(){
		assertTrue(true);
	}
	@Test
	public void testInitializeGridSetsUpCells() throws Exception {
		// Create instance with small grid size
		Constructor<MineSweeperWithFlags1> ctor = MineSweeperWithFlags1.class.getDeclaredConstructor(int.class);
		ctor.setAccessible(true);
		MineSweeperWithFlags1 ms = ctor.newInstance(3); // 3x3 grid

		// Access private cells field
		Field cellsField = MineSweeperWithFlags1.class.getDeclaredField("cells");
		cellsField.setAccessible(true);
		Object cellsObj = cellsField.get(ms);
		assertNotNull(cellsObj);

		Object[][] cells = (Object[][]) cellsObj;
		assertEquals(3, cells.length);
		for (int row = 0; row < 3; row++) {
			assertEquals(3, cells[row].length);
			for (int col = 0; col < 3; col++) {
				assertNotNull(cells[row][col], "Cell at (" + row + "," + col + ") should not be null");
				// Optionally, check type
				Class<?> cellClass = MineSweeperWithFlags1.class.getDeclaredClasses()[0];
				assertTrue(cellClass.isInstance(cells[row][col]));
			}
		}
	}
}
