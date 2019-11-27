// C code file for  a file ADT where we can read a single bit at a
// time, or write a single bit at a time

#include <stdlib.h>
#include <assert.h>
#include <string.h>
#include <stdio.h>

#include "bitfile.h"

// open a bit file in "r" (read) mode or "w" (write) mode
struct bitfile * bitfile_open(char * filename, char * mode)
{
    struct bitfile * opened = malloc(sizeof(struct bitfile));
    opened -> buffer = 0;
    if (strcmp(mode, "r") == 0) {
        opened -> file = fopen(filename, "r");
        opened -> is_read_mode = 1;
    } else if (strcmp(mode, "w") == 0) {
        opened -> file = fopen(filename, "w");
        opened -> is_read_mode = 0;
    }
    return opened;
}

// write a bit to a file; the file must have been opened in write mode
void bitfile_write_bit(struct bitfile * this, int bit)
{
    if (!this -> is_read_mode) {
        this -> index++;
        this -> buffer = this -> buffer | bit;
        if (this -> index == 8) {
            int temp = 0;
            for (int i = 0; i < 8; i++) {
                temp = temp | (((this -> buffer >> i) & 1) << (7 - i));
            }
            fputc(temp, this -> file);
            this -> index = 0;
            this -> buffer = 0;
        }
        this -> buffer = (this -> buffer) << 1;
    }
}

// read a bit from a file; the file must have been opened in read mode
int bitfile_read_bit(struct bitfile * this)
{
    if (!bitfile_end_of_file(this) && this -> is_read_mode) {
        return fgetc(this -> file);
    } else {
        return -1; // indicates EOF
    }
}

// close a bitfile; flush any partially-filled buffer if file is open
// in write mode
void bitfile_close(struct bitfile * this) {
    if (!this -> is_read_mode) {
        if (this -> index != 0) {
            while (this -> index != 7) {
                bitfile_write_bit(this, 0);
            }
            bitfile_write_bit(this, 0);
        }
        this -> is_EOF = 1;
    }
    fclose(this -> file);
}

// check for end of file
int bitfile_end_of_file(struct bitfile * this)
{
    return feof(this -> file);
}
