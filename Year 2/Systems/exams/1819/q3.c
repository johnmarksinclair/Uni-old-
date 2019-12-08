#include <stdio.h>
#include <stdlib.h>
#include <string.h>

unsigned char *to_unary(unsigned int *data, int num_of_elements) {
    //get length of unary expression
    int length = 0;
    int num;
    for (int i = 0; i < num_of_elements; i++) {
        num = data[i];
        length += num;
        length++;
    }
    //make unary representation - array of bits (ints) 
    int *bits = malloc(sizeof(int)*length);
    int i = 0;
    int dataIndex = 0;
    while (i < length) {
        int temp = data[dataIndex];
        for (int x = 0; x < temp; x++) {
            bits[i+x] = 1;
        }
        bits[i+temp] = 0;
        i += temp+1;
        dataIndex++;
    }
    //turn into bytes for char array
    int length_bytes = (length/8) + 1;
    int total_length = length_bytes*8;
    unsigned char *unary = malloc(sizeof(char)*length_bytes);
    // int byte_no = 0;
    // for (int i = 0; i < total_length; i++) {
    //     char byte = 0x00000000;
    //     //byte  = byte >> 1;
    //     for (int x = 0; x < 8; x++) {
    //         byte = byte << x;
    //         byte += bits[i+x];
    //     }
    //     unary[byte_no] = byte;
    //     byte_no++;
    //     i+=8;
    // }
    
    //print out int array representation
    for (int i = 0; i < length; i++) {
        printf("%d", bits[i]);
    }
    printf("%c\n", ' ');
    //print out unary
    for (int i = 0; i < total_length; i++) {
        printf("%c", unary[i]);
    }
    printf("%c\n", ' ');
    return unary;
}

char *to_byte(int *arr) {
    int length = 8;
    char *temp = 0x00;
    for (int i = 0; i < length; i++) {
    }
    return temp;
}

unsigned int *from_unary(unsigned char *data, int num_of_elements) {

}

int main() {
    int num_of_elements = 4;
    int *data = malloc(sizeof(int)*num_of_elements); // allocating memory for the int array 
    data[0] = 2;
    data[1] = 3;
    data[2] = 4;
    data[3] = 5;
    //free(data); // will free up the memory used by the object data
    unsigned char *unary = to_unary(data, num_of_elements);
    return 0;
}