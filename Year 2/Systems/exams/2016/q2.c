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

struct node * create_first_node(char * passed) {
    struct node * new = malloc(sizeof(struct node));
    new->address = passed;
    new->next = NULL;
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
    new->prev = NULL;
    this->head = new;
}

void remove_string(struct set * this, char * address) {
    while(this->head != NULL) {
        if (strcmp(this->head->address, address) == 0) {
            this->head = this->head->next;
            break;
        }
        this->head = this->head->next;
    }
}

// returns 1 if true, 0 if false
int contains(struct set * this, char * address) {
    while(this->head != NULL) {
        if (strcmp(this->head->address, address) == 0) {
            return 1;
        }
        this->head = this->head->next;
    }
    return 0;
}

int main() {
    struct set * temp = new_set();
    add_string(temp, "one");
    add_string(temp, "two");
    add_string(temp, "three");
    add_string(temp, "four");
    //remove_string(temp, "two");
    //printf("%d\n", contains(temp, "three"));
    // while(temp->head != NULL) {
    //     printf("%s\n", temp->head->address);
    //     temp->head = temp->head->next;
    // }
    // temp->head = temp->start;
    // while(temp->head != NULL) {
    //     printf("%s\n", temp->head->address);
    //     temp->head = temp->head->next;
    // }
    printf("%s\n", temp->head->address);
    return 0;
}