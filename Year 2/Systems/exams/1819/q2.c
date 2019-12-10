#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct node {
    struct node * next;
    struct node * prev;
    int val;
};

struct queue {
    struct node * head;
    struct node * tail;
};

struct queue * new_queue() {
    struct queue * q = malloc(sizeof(struct queue));
    q->head = NULL;
    q->tail = NULL;
    return q;
}

struct node * new_node(int _val) {
    struct node * new = malloc(sizeof(struct node));
    new->next = NULL;
    new->prev = NULL;
    new->val = _val;
}

int main() {
    return 0;
}