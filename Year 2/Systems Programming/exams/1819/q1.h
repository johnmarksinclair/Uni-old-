#ifndef Q1_H
#define Q1_H
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>
#include <math.h>
#include <stdbool.h>
// stact struct
struct stack {
    float *val;
    int size;
    int top;
};
// declare stack functions
struct stack *new_stack(int _size);
void stack_push(struct stack *this, float _val);
float stack_pop(struct stack *this);
float stack_peek(struct stack *this);
bool stack_empty(struct stack *this);
float min(int a, int b);
float max(int a, int b);
int evalSymb(char * x);
float eval_postfix_fuzzy(char **terms, int nterms);
#endif
