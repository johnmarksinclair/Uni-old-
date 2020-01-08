package bonusAssignment;

import java.util.Stack;

//-------------------------------------------------------------------------
/**
 * Utility class containing validation/evaluation/conversion operations for
 * prefix and postfix arithmetic expressions.
 *
 * @author John Sinclair
 * @version 1/12/15 13:03:48
 */

public class Arith {

//~ Validation methods ..........................................................

	/**
	 * Validation method for prefix notation.
	 *
	 * @param prefixLiterals : an array containing the string literals hopefully in
	 *                       prefix order. The method assumes that each of these
	 *                       literals can be one of: - "+", "-", "*", or "/" - or a
	 *                       valid string representation of an integer.
	 *
	 * @return true if the parameter is indeed in prefix notation, and false
	 *         otherwise.
	 **/
	public static boolean validatePrefixOrder(String prefixLiterals[]) {
		int counter = 1;
		for (int i = 0; i < prefixLiterals.length; i++) {
			if (evalSymb(prefixLiterals[i]) > 0)
				counter++;
			else
				counter--;
			if (counter <= 0 && i != prefixLiterals.length - 1)
				return false;
		}
		return counter == 0;
	}

	/**
	 * Validation method for postfix notation.
	 *
	 * @param postfixLiterals : an array containing the string literals hopefully in
	 *                        postfix order. The method assumes that each of these
	 *                        literals can be one of: - "+", "-", "*", or "/" - or a
	 *                        valid string representation of an integer.
	 *
	 * @return true if the parameter is indeed in postfix notation, and false
	 *         otherwise.
	 **/
	public static boolean validatePostfixOrder(String postfixLiterals[]) {
		int counter = 1;
		for (int i = 0; i < postfixLiterals.length; i++) {
			if (evalSymb(postfixLiterals[i]) > 0)
				counter++;
			else
				counter--;
			if (counter > 0 && i != postfixLiterals.length - 1 && i != 0)
				return false;
		}
		return counter == 0;
	}

//~ Evaluation  methods ..........................................................

	/**
	 * Evaluation method for prefix notation.
	 *
	 * @param prefixLiterals : an array containing the string literals in prefix
	 *                       order. The method assumes that each of these literals
	 *                       can be one of: - "+", "-", "*", or "/" - or a valid
	 *                       string representation of an integer.
	 *
	 * @return the integer result of evaluating the expression
	 **/
	public static int evaluatePrefixOrder(String prefixLiterals[]) {
		return evaluatePostfixOrder(convertPrefixToPostfix(prefixLiterals));
	}

	/**
	 * Evaluation method for postfix notation.
	 *
	 * @param postfixLiterals : an array containing the string literals in postfix
	 *                        order. The method assumes that each of these literals
	 *                        can be one of: - "+", "-", "*", or "/" - or a valid
	 *                        string representation of an integer.
	 *
	 * @return the integer result of evaluating the expression
	 **/
	public static int evaluatePostfixOrder(String postfixLiterals[]) {
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = 0; i < postfixLiterals.length; i++) {
			if (evalSymb(postfixLiterals[i]) == 0) {
				stack.push(Integer.parseInt(postfixLiterals[i]));
			} else {
				int a = stack.pop();
				int b = stack.pop();
				int ans = performOp(a, b, evalSymb(postfixLiterals[i]));
				stack.push(ans);
			}
		}
		return stack.peek();
	}
//~ Conversion  methods ..........................................................

	/**
	 * Converts prefix to postfix.
	 *
	 * @param prefixLiterals : an array containing the string literals in prefix
	 *                       order. The method assumes that each of these literals
	 *                       can be one of: - "+", "-", "*", or "/" - or a valid
	 *                       string representation of an integer.
	 *
	 * @return the expression in postfix order.
	 **/
	public static String[] convertPrefixToPostfix(String prefixLiterals[]) {
		String[] postfixLiterals = new String[prefixLiterals.length];
		Stack<String> stack = new Stack<String>();
		for (int i = prefixLiterals.length-1; i >= 0; i--) {
			if (evalSymb(prefixLiterals[i]) != 0) {
				String a = stack.pop();
				String b = stack.pop();
				String ans = a + b + prefixLiterals[i];
				stack.push(ans);
			} else {
				stack.push(prefixLiterals[i]);
			}
		}
		String finished = stack.pop();
		for (int i = 0; i < finished.length(); i++) {
			postfixLiterals[i] = finished.charAt(i) + "";
		}
		return postfixLiterals;
	}

	/**
	 * Converts postfix to prefix.
	 *
	 * @param prefixLiterals : an array containing the string literals in postfix
	 *                       order. The method assumes that each of these literals
	 *                       can be one of: - "+", "-", "*", or "/" - or a valid
	 *                       string representation of an integer.
	 *
	 * @return the expression in prefix order.
	 **/
	public static String[] convertPostfixToPrefix(String postfixLiterals[]) {
		String[] prefixLiterals = new String[postfixLiterals.length];
		Stack<String> stack = new Stack<String>();
		for (int i = 0; i < postfixLiterals.length; i++) {
			if (evalSymb(postfixLiterals[i]) != 0) {
				String a = stack.pop();
				String b = stack.pop();
				String ans = postfixLiterals[i] + a + b;
				stack.push(ans);
			} else {
				stack.push(postfixLiterals[i]);
			}
		}
		String finished = stack.pop();
		for (int i = 0; i < finished.length(); i++) {
			prefixLiterals[i] = finished.charAt(i) + "";
		}
		return prefixLiterals;
	}

	/**
	 * Converts prefix to infix.
	 *
	 * @param infixLiterals : an array containing the string literals in prefix
	 *                      order. The method assumes that each of these literals
	 *                      can be one of: - "+", "-", "*", or "/" - or a valid
	 *                      string representation of an integer.
	 *
	 * @return the expression in infix order.
	 **/
	public static String[] convertPrefixToInfix(String prefixLiterals[]) {
		return convertPostfixToInfix(convertPrefixToPostfix(prefixLiterals));
	}

	/**
	 * Converts postfix to infix.
	 *
	 * @param infixLiterals : an array containing the string literals in postfix
	 *                      order. The method assumes that each of these literals
	 *                      can be one of: - "+", "-", "*", or "/" - or a valid
	 *                      string representation of an integer.
	 *
	 * @return the expression in infix order.
	 **/
	public static String[] convertPostfixToInfix(String postfixLiterals[]) {
		String[] infixLiterals = new String[postfixLiterals.length];
		Stack<String> stack = new Stack<String>();
		for (int i = 0; i < postfixLiterals.length; i++) {
			if (evalSymb(postfixLiterals[i]) != 0) {
				String a = stack.pop();
				String b = stack.pop();
				String ans = b + postfixLiterals[i] + a;
				stack.push(ans);
			} else {
				stack.push(postfixLiterals[i]);
			}
		}
		String finished = stack.pop();
		for (int i = 0; i < finished.length(); i++) {
			infixLiterals[i] = finished.charAt(i) + "";
		}
		return infixLiterals;
	}

	public static int evalSymb(String x) {
		if (x.contentEquals("+")) {
			return 1;
		} else if (x.contentEquals("-")) {
			return 2;
		} else if (x.contentEquals("*")) {
			return 3;
		} else if (x.contentEquals("/")) {
			return 4;
		}
		return 0;
	}

	public static int performOp(int a, int b, int op) {
		switch (op) {
		case 1:
			return b + a;
		case 2:
			return b - a;
		case 3:
			return b * a;
		case 4:
			return b / a;
		}
		return -1;
	}
}
