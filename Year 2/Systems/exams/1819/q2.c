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
    if (this->head == NULL) {
        return -1;
    } else {
        int value = this->head->val;
        this->head = this->head->next;
        this->head->prev = NULL;
        return value;
    }
}
// push a value to the back of the deque
void push_back_deque(struct deque * this, int value) {
    struct node * new = new_node(value);
    if (this->head == NULL) {
        this->head = new;
        this->tail = new;
    } else {
        new->prev = this->tail;
        this->tail->next = new;
        this->tail = new;
    }
}
// pop a value from the back of the deque
int pop_back_deque(struct deque * this) {
    if (this->head == NULL) {
        return -1;
    } else {
        int value = this->tail->val;
        this->tail = this->tail->prev;
        this->head->next = NULL;
        return value;
    }
}
// free the memory used by the deque
void free_deque(struct deque * this) {

}
int main() {
    struct deque * q = new_deque();
    push_front_deque(q, 2);
    push_back_deque(q, 3);
    push_front_deque(q, 1);
    struct node * temp = q->head;
    printf("%d\n", temp->val);
    temp = q->tail;
    printf("%d\n", temp->val);
    pop_front_deque(q);
    temp = q->head;
    printf("%d\n", temp->val);
    return 0;
}