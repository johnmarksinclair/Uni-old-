package bonusAssignment;
import static org.junit.Assert.*;

import org.junit.Test;

public class ArithTest {

	@Test
	public void testEvalSymb() {
		assertEquals(1, Arith.evalSymb("+"));
		assertEquals(0, Arith.evalSymb("3"));
	}
}
