#ifndef Q2_H
#define Q2_H
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
// structs
struct node {
    struct node * next;
    struct node * prev;
    int val;
};
struct deque {
    struct node * head;
    struct node * tail;
};
// declarations
struct node * new_node(int value);
struct deque * new_deque();
void push_front_deque(struct deque * this, int value);
int pop_front_deque(struct deque * this);
void push_back_deque(struct deque * this, int value);
int pop_back_deque(struct deque * this);
void free_deque(struct deque * this);
#endif
