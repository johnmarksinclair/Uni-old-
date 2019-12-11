#include "q1.h"
// create new empty stack
struct stack * new_stack(int _size) {
    struct stack *new = malloc(sizeof(struct stack));
    new->size = _size;
    new->top = 0;
    new->val = malloc(sizeof(float)*_size);
    return new;
}
// push a float to the stack
void stack_push(struct stack *this, float _val) {
    this->val[this->top] = _val;
    this->top++;
}
// pop a float off the stack
float stack_pop(struct stack *this) {
    this->top -= 1;
    float val = this->val[this->top];
    return val;
}
// peek at value from stack
float stack_peek(struct stack *this) {
    float val = this->val[this->top-1];
    return val;
}
// check if stack is empty
bool stack_empty(struct stack *this) {
    return (this->top == 0);
}
// evaluates a passed char
int evalSymb(char * x) {
	if (strcmp(x, "&") == 0) {
		return 1;
	} else if (strcmp(x, "|") == 0) {
		return 2;
	} else if (strcmp(x, "!") == 0) {
		return 3;
	} else {
		return 0;
	}
}
// evaluates a postfix fuzzy logic expression
float eval_postfix_fuzzy(char **terms, int nterms) {
    struct stack *stack = new_stack(50);
    for (int i = 0; i < nterms; i++) {
        if (evalSymb(terms[i]) != 0) {
            float a = stack_pop(stack);
            float b = stack_pop(stack);
            float ans;
            switch (evalSymb(terms[i])) {
                case 1:
                    ans = (a > b) ? b : a;
                    break;
                case 2:
                    ans = (a > b) ? a : b;
                    break;
                case 3:
                    ans = 1-a;
                    break;
            }
            stack_push(stack, ans);
        } else {
            float val = atof(terms[i]);
			stack_push(stack, val);
        }
    }
    return stack_pop(stack);
}
int main(int argc, char **argv) {
    if ( argc == 1 ) {
		// command line contains only the name of the program
		printf("Error: No command line parameters provided\n");
		printf("Usage: %s <expression>\n", argv[0]);
		exit(1);
	} else {
		// command line has enough parameters for an expression
		float result = eval_postfix_fuzzy(argv+1, argc-1);
        printf("Result is %f\n", result);
		return 0;
	}
}
