#ifndef Q2_H
#define Q2_h

struct node {
    struct node * next;
    char * address;
};

struct set {
    struct node * head;
};

struct set * new_set();
void add_string(struct set * this, char * address);
void remove_string(struct set * this, char * address);
int contains(struct set * this, char * address);

#endif