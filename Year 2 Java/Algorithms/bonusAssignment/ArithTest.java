package bonusAssignment;

import static org.junit.Assert.*;

import java.util.Arrays;

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
		String[] prefixLiterals1 = { "-", "+", "2", "4" };
		assertFalse(Arith.validatePrefixOrder(prefixLiterals1));
		String[] prefixLiterals2 = { "+", "2", "4" };
		assertTrue(Arith.validatePrefixOrder(prefixLiterals2));
	}
	
	@Test
	public void testValidatePostfixOrder() {
		String[] postfixLiterals = {};
		assertFalse(Arith.validatePostfixOrder(postfixLiterals));
		String[] postfixLiterals1 = { "2", "4", "-", "+" };
		assertFalse(Arith.validatePostfixOrder(postfixLiterals1));
		String[] postfixLiterals2 = { "2", "4", "+" };
		assertTrue(Arith.validatePostfixOrder(postfixLiterals2));
	}
	
	@Test
	public void testEvaluatePrefixOrder() {
		String[] prefixLiterals = {};
		assertEquals(Arith.evaluatePrefixOrder(prefixLiterals), 0);
		String[] prefixLiterals1 = { "-", "+", "2", "4" };
		assertEquals(Arith.evaluatePrefixOrder(prefixLiterals1), 0);
		String[] prefixLiterals2 = { "+", "2", "4" };
		assertEquals(Arith.evaluatePrefixOrder(prefixLiterals2), 6);
	}
	
	@Test
	public void testEvaluatePostfixOrder() {
		String[] postfixLiterals = {};
		assertEquals(Arith.evaluatePostfixOrder(postfixLiterals), 0);
		String[] postfixLiterals1 = { "2", "4", "-", "+" };
		assertEquals(Arith.evaluatePostfixOrder(postfixLiterals1), 0);
		String[] postfixLiterals2 = { "2", "4", "+" };
		assertEquals(Arith.evaluatePostfixOrder(postfixLiterals2), 6);
	}

	@Test
	public void testConvertPrefixToPostfix() {
		String[] prefixLiterals = {};
		assertNull(Arith.convertPrefixToPostfix(prefixLiterals));
		String[] prefixLiterals1 = { "-", "+", "2", "4" };
		assertNull(Arith.convertPrefixToPostfix(prefixLiterals1));
		String[] prefixLiterals2 = { "+", "2", "4" };
		String[] postfixLiterals2 = { "2", "4", "+" };
		assertTrue(Arrays.equals(Arith.convertPrefixToPostfix(prefixLiterals2), postfixLiterals2));
	}

	@Test
	public void testConvertPostfixToPrefix() {
		String[] postfixLiterals = {};
		assertNull(Arith.convertPostfixToPrefix(postfixLiterals));
		String[] postfixLiterals1 = { "2", "4", "-", "+" };
		assertNull(Arith.convertPostfixToPrefix(postfixLiterals1));
		String[] postfixLiterals2 = { "2", "4", "+" };
		String[] prefixLiterals2 = { "+", "4", "2" };
		assertTrue(Arrays.equals(Arith.convertPostfixToPrefix(postfixLiterals2), prefixLiterals2));
	}

	@Test
	public void testConvertPrefixToInfix() {
		String[] prefixLiterals = {};
		assertNull(Arith.convertPrefixToInfix(prefixLiterals));
		String[] prefixLiterals1 = { "-", "+", "2", "4" };
		assertNull(Arith.convertPrefixToInfix(prefixLiterals1));
		String[] prefixLiterals2 = { "+", "2", "4" };
		String[] infixLiterals2 = { "2", "+", "4" };
		assertTrue(Arrays.equals(Arith.convertPrefixToInfix(prefixLiterals2), infixLiterals2));
	}

	@Test
	public void testConvertPostfixToInfix() {
		String[] postfixLiterals = {};
		assertNull(Arith.convertPostfixToInfix(postfixLiterals));
		String[] postfixLiterals1 = { "2", "4", "-", "+" };
		assertNull(Arith.convertPostfixToInfix(postfixLiterals1));
		String[] postfixLiterals2 = { "2", "4", "+" };
		String[] infixLiterals2 = { "2", "+", "4" };
		assertTrue(Arrays.equals(Arith.convertPostfixToInfix(postfixLiterals2), infixLiterals2));
	}

	@Test
	public void testEvalSymb() {
		assertEquals(Arith.evalSymb(""), 0);
		assertEquals(Arith.evalSymb("+"), 1);
		assertEquals(Arith.evalSymb("-"), 2);
		assertEquals(Arith.evalSymb("*"), 3);
		assertEquals(Arith.evalSymb("/"), 4);
	}

	@Test
	public void testPerformOp() {
		assertEquals(Arith.performOp(1, 2, 0), 0);
		assertEquals(Arith.performOp(1, 2, 1), 3);
		assertEquals(Arith.performOp(1, 2, 2), 1);
		assertEquals(Arith.performOp(1, 2, 3), 2);
		assertEquals(Arith.performOp(1, 2, 4), 2);
	}
}
