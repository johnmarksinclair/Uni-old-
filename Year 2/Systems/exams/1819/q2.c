#include "q2.h"
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
    struct node * temp;
    while(this->head != NULL) {
        temp = this->head->next;
        free(this->head);
        this->head = temp;
    }
    free(this);
}
int main() {
    struct deque * q = new_deque();
    push_front_deque(q, 2);
    push_front_deque(q, 1);
    push_back_deque(q, 3);
    struct node * temp = q->head;
    while(temp != NULL) {
        printf("%d\n", temp->val);
        temp = temp->next;
    }
    printf("\n");
    pop_front_deque(q);
    temp = q->head;
    while(temp != NULL) {
        printf("%d\n", temp->val);
        temp = temp->next;
    }
    printf("\n");
    pop_back_deque(q);
    temp = q->head;
    while(temp != NULL) {
        printf("%d\n", temp->val);
        temp = temp->next;
    }
    printf("\n");
    free_deque(q);
    temp = q->head;
    while(temp != NULL) {
        printf("%d\n", temp->val);
        temp = temp->next;
    }
    return 0;
}
