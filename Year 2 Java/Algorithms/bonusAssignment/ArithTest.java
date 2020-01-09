package bonusAssignment;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

//-------------------------------------------------------------------------
/**
 *  Test class for Arith
 *
 *  @version 3.1 09/01/20 11:32:15
 *
 *  @author  John Sinclair - 16325734
 */

@RunWith(JUnit4.class)
public class ArithTest {
	
	@Test
	public void testValidatePrefixOrder() {
		String[] prefixLiterals = {};
		assertFalse(Arith.validatePrefixOrder(prefixLiterals));
	}
	
	@Test
	public void testValidatePostfixOrder() {
		
	}
	
	@Test
	public void evaluatePrefixOrder() {
		
	}
	
	@Test
	public void evaluatePostfixOrder() {
		
	}

	@Test
	public void convertPrefixToPostfix() {
		
	}

	@Test
	public void convertPostfixToPrefix() {
		
	}

	@Test
	public void convertPrefixToInfix() {
		
	}

	@Test
	public void convertPostfixToInfix() {
		
	}

	@Test
	public void evalSymb() {
		
	}

	@Test
	public void performOp() {
		
	}
}
