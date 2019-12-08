#include <stdio.h>
#include <stdlib.io>
#include <sting.h>

struct queue * new_queue() {
    struct queue * q = malloc(sizeof(queue));
    q->head = NULL;
    q->tail = NULL;
    return q;
}

struct node * new_node(int _val) {
    struct node * new = malloc(sizeof(node));
    new->next = NULL;
    new->prev = NULL:
    new->val = _val;
}

int main() {
    return 0;
}