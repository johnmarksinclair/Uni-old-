#include "q2.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct node * create_node(char * passed);

struct set * new_set() {
    struct set * new = malloc(sizeof(struct set));
    new->head = NULL;
    return new;
}

struct node * create_node(char * passed) {
    struct node * new = malloc(sizeof(struct node));
    new->address = passed;
    new->next = NULL;
    return new;
}

void add_string(struct set * this, char * address) {
    struct node * new = create_node(address);
    new->next = this->head;
    this->head = new;
}

void remove_string(struct set * this, char * address) {
    if (contains(this, address) == 1) {
        struct node * node  = this->head;
        while(node != NULL) {
            if (strcmp(node->address, address) == 0) {
                this->head = this->head->next;
                break;
            }
        node = node->next;
        }
    }
}
// returns 1 if true, 0 if false
int contains(struct set * this, char * address) {
    struct node *node  = this->head;
    while(node != NULL) {
        if (strcmp(node->address, address) == 0) {
            return 1;
        }
        node = node->next;
    }
    return 0;
}

int main() {
    struct set * temp = new_set();
    add_string(temp, "one");
    add_string(temp, "two");
    add_string(temp, "three");
    add_string(temp, "four");
    int cont = contains(temp, "three");
    printf("%s\n", (cont == 1) ? "true" : "false");
    struct node * node = temp -> head;
    while(node != NULL) {
        printf("%s\n", node -> address);
        node = node -> next;
    }
    remove_string(temp, "three");
    node = temp -> head;
    while(node != NULL) {
        printf("%s\n", node -> address);
        node = node -> next;
    }
    return 0;
}