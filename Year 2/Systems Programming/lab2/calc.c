// include parts of the C standard library
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

// create a new empty stack
struct double_stack {
  double * items;
  int max_size;
  int top;
};

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
	this->items[this->top]=value;
	this->top++;
}

// pop a value from the stack
double double_stack_pop(struct double_stack * this) {
	double last = this->items[this->top-1];
	this->top--;
	return last;
}

int main(int argc, char ** argv) {
	struct double_stack * stack = double_stack_new(10);
	double_stack_push(stack, 5);
	printf("%f", stack->items[0]);
	printf("%d", stack->top);
	double_stack_pop(stack);
	printf("%d", stack->top);
}
// end of code
