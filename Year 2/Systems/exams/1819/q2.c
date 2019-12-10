#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "q2.h"
// declarations
struct node * new_node(int value);
struct deque * new_deque();
void push_front_deque(struct deque * this, int value);
int pop_front_deque(struct deque * this);
void push_back_deque(struct deque * this, int value);
int pop_back_deque(struct deque * this);
void free_deque(struct deque * this);
void free_deque(struct deque * this);
// create a new node
struct node * new_node(int value) {
    struct node * new = malloc(sizeof(struct node));
    new->next = NULL;
    new->prev = NULL;
    new->val = value;
    return new;
}
// create a new empty deque
struct deque * new_deque() {
    struct deque * q = malloc(sizeof(struct deque));
    q->head = NULL;
    q->tail = NULL;
    return q;
}
// push a value to the front of the deque
void push_front_deque(struct deque * this, int value) {
    struct node * new = new_node(value);
    if (this->head == NULL) {
        this->head = new;
        this->tail = new;
    } else {
        new->next = this->head;
        this->head->prev  = new;
        this->head = new;
    }
}
// pop a value from the front of the deque
int pop_front_deque(struct deque * this) {
    int value = this->head->val;
    this->head = this->head->next;
    this->head->prev = NULL;
    return value;
}
// push a value to the back of the deque
void push_back_deque(struct deque * this, int value) {

}
// pop a value from the back of the deque
int pop_back_deque(struct deque * this) {

}
// free the memory used by the deque
void free_deque(struct deque * this) {

}
int main() {
    return 0;
}