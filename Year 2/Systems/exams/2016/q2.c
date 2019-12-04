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

int main() {
    struct set * temp = new_set();
    add_string(temp, "one");
    add_string(temp, "two");
    add_string(temp, "three");
    add_string(temp, "four");
    while(temp->head != NULL) {
        printf("%s\n", temp->head->address);
        temp->head = temp->head->next;
    }
    return 0;
}