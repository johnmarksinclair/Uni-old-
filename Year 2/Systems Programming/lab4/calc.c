// include parts of the C standard library
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <math.h>
#include <stdbool.h>
#include <string.h>

// create a new empty stack
struct double_stack {
  double * items;
  int max_size;
  int top;
};

// declare functions
struct double_stack * double_stack_new(int max_size);
void double_stack_push(struct double_stack * this, double value);
double double_stack_pop(struct double_stack * this);
double double_stack_peek(struct double_stack *this);
bool stack_empty(struct double_stack * this);
double evaluate_postfix_expression(char **arr, int length);
int evalSymb(char * x);
double evaluate_infix_expression(char **arr, int length);

// create new empty stack 
struct double_stack * double_stack_new(int max_size) {
	struct double_stack * result;
	// allocate space for the stack header
	result = malloc(sizeof(struct double_stack));
	result->max_size = max_size;
	result->top = 0;
	// allocate space for the data stored in the stack
	result->items = malloc(sizeof(double)*max_size);
	// return a pointer to the newly-allocated stack
	return result;
}

// push a value onto the stack
void double_stack_push(struct double_stack * this, double value) {
	this -> items[this -> top] = value;
	this -> top++;
}

// pop a value from the stack
double double_stack_pop(struct double_stack * this) {
	this -> top -= 1;
	double val = this -> items[this -> top];
	return val;
}

double double_stack_peek(struct double_stack * this) {
	double val = this -> items[this -> top-1];
	return val;
}

double evaluate_postfix_expression(char **arr, int length) {
	struct double_stack * stack = double_stack_new(50);
	for (int i = 0; i < length; i++) {
		if (evalSymb(arr[i]) != 0) {
			double a = double_stack_pop(stack);
			double b = double_stack_pop(stack);
			double ans;
			switch (evalSymb(arr[i])) {
				case 1: 
					ans = b + a;
					break;
				case 2: 
					ans = b - a;
					break;
				case 3: 
					ans = b * a;
					break;
				case 4: 
					ans = b / a;
					break;
				case 5:
					ans = pow(b, a);
					break;
			}
			double_stack_push(stack, ans);
		}
		else {
			double val;
			sscanf(arr[i], "%lf", &val);
			double_stack_push(stack, val);
		}
	}
	return double_stack_pop(stack);
}

double evaluate_infix_expression(char ** arr, int length) {
	struct double_stack * stack = double_stack_new(50);
	char ** expression = malloc(sizeof(char*)*length);
	int index = 0;
	for (int i = 0; i < length; i++) {
		char * symb = arr[i];
		switch(evalSymb(symb)) {
			case 0:
				expression[index] = symb;
				index++;
				break;
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				if (!stack_empty(stack)) {
					char * top_of_stack = arr[(int)double_stack_peek(stack)];
					int a = evalSymb(symb);
					int b = evalSymb(top_of_stack);
					while(b >= a && !stack_empty(stack) && b < 6) {
						expression[index] = top_of_stack;
						double_stack_pop(stack);
						index++;
						top_of_stack = arr[(int)double_stack_peek(stack)];
						b = evalSymb(top_of_stack);
					}
				}
				double_stack_push(stack, i);
				break;
			case 6:
				double_stack_push(stack, i);
				break;
			case 7:
				while (strcmp(arr[(int)double_stack_peek(stack)], "(") != 0) {
					double index1 = double_stack_pop(stack);
					expression[index] = arr[(int)index1];
					index++;
				}
				double_stack_pop(stack);
				break;
		}
	}
	while(!stack_empty(stack)) {
		double temp = double_stack_pop(stack);
		expression[index] = arr[(int)temp];
		index++;
	}
	return evaluate_postfix_expression(expression, index);
}

bool stack_empty(struct double_stack * this) {
	return (this -> top == 0);
}

int evalSymb(char * x) {
	if (strcmp(x, "+") == 0) {
		return 1;
	}
	else if (strcmp(x, "-") == 0) {
		return 2;
	}
	else if (strcmp(x, "X") == 0) {
		return 3;
	}
	else if (strcmp(x, "/") == 0) {
		return 4;
	}
	else if (strcmp(x, "^") == 0) {
		return 5;
	}
	else if (strcmp(x, "(") == 0) {
		return 6;
	}
	else if (strcmp(x, ")") == 0) {
		return 7;
	}
	else {
		return 0;
	}
}

// main function for a simple bench calculator with command line inputs
int main(int argc, char ** argv) {
	if ( argc == 1 ) {
		// command line contains only the name of the program
		printf("Error: No command line parameters provided\n");
		printf("Usage: %s postfix|infix <expression>\n", argv[0]);
		exit(1);
	}
	else if ( argc == 2 ) {
		// command line contains name of prog and one other parameter
		printf("Error: No expression to evaluate provided\n");
		printf("Usage: %s postfix|infix <expression>\n", argv[0]);
		exit(1);
	}
	else {
		// command line has enough parameters for an expression
		double result;
		if ( strcmp(argv[1], "postfix") == 0 ) {
			// pass the command line parameters, but with the first two removed
			result = evaluate_postfix_expression(argv+2, argc-2);
			printf("Result is %lf\n", result);
		}
		else if ( strcmp(argv[1], "infix") == 0 ) {
			// pass the command line parameters, but with the first two removed
			result = evaluate_infix_expression(argv+2, argc-2);
			printf("Result is %lf\n", result);
		}
		else {
			printf("Error: You must specify whether the expression is infix or postfix\n");
			printf("Usage: %s postfix|infix <expression>\n", argv[0]);
			exit(1);
		}
		return 0;
	}
}