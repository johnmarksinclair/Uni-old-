// code for a huffman coder


#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <ctype.h>

#include "huff.h"
#include "bitfile.h"

// create a new huffcoder structure
struct huffcoder *  huffcoder_new()
{
  struct huffcoder * new;
  new = malloc(sizeof(struct huffcoder));
  new -> tree = malloc(sizeof(struct huffchar)*NUM_CHARS);
  return new;
}

// count the frequency of characters in a file; set chars with zero
// frequency to one
void huffcoder_count(struct huffcoder * this, char * filename)
{
  unsigned char c;  // we need the character to be
                    // unsigned to use it as an index
  FILE * file;

  file = fopen(filename, "r");
  assert( file != NULL );
  while( !feof(file) ) {
    //printf("%c", c);
    c = fgetc(file);
    this -> freqs[c] += 1;
  }
  for (int i = 0; i < NUM_CHARS; i++) {
    if (this -> freqs[i] == 0) this -> freqs[i] = 1;
  }
  fclose(file);
}

// using the character frequencies build the tree of compound
// and simple characters that are used to compute the Huffman codes
void huffcoder_build_tree(struct huffcoder * this)
{
  struct huffchar * nodes[NUM_CHARS];
  for (int i = 0; i < NUM_CHARS; i++) {
    nodes[i] = malloc(sizeof(struct huffchar *));
    nodes[i] -> freq = this -> freqs[i];
    nodes[i] -> u.c = i;
    nodes[i] -> seqno = i;
    nodes[i] -> is_compound = 0;
  }
  finishTree(nodes, this);
}

// completes the huffman tree
void finishTree(struct huffchar ** nodes, struct huffcoder * this) 
{
  int space = NUM_CHARS;
  int n = 0;
  while (space > 1) {
    struct huffchar * small1 = getSmallest(nodes, space);
    space--;
    struct huffchar * small2 = getSmallest(nodes, space);
    struct huffchar * combined = malloc(sizeof(struct huffchar));
    combined -> freq = (small1 -> freq) + (small2 -> freq);
    combined -> seqno = NUM_CHARS + n;
    combined -> u.compound.left = small1;
    combined -> u.compound.right = small2;
    combined -> is_compound = 1;
    nodes[space-1] = combined;
    n++;
  }
  this -> tree = nodes[0];
}

// finds, removes and returns the least frequent node
struct huffchar * getSmallest(struct huffchar ** nodes, int size) {
  int smallest = 0;
  for (int i = 0; i < size; i++) {
    if (nodes[i] -> freq < nodes[smallest] -> freq) { // if no smaller, lowest index
      smallest = i;
    }
  }
  struct huffchar * to_return = nodes[smallest];
  // removes node
  for (int index = smallest; index < size; index++) {
    nodes[index] = nodes[index+1];
  }
  return to_return;
}

// using the Huffman tree, build a table of the Huffman codes
// with the huffcoder object
void huffcoder_tree2table(struct huffcoder * this)
{

}

// print the Huffman codes for each character in order
void huffcoder_print_codes(struct huffcoder * this)
{
  int i, j;
  char buffer[NUM_CHARS];

  for ( i = 0; i < NUM_CHARS; i++ ) {
    // put the code into a string
    assert(this->code_lengths[i] < NUM_CHARS);
    for ( j = this->code_lengths[i]-1; j >= 0; j--) {
      buffer[j] = ((this->codes[i] >> j) & 1) + '0';
    }
    // don't forget to add a zero to end of string
    buffer[this->code_lengths[i]] = '\0';

    // print the code
    printf("char: %d, freq: %d, code: %s\n", i, this->freqs[i], buffer);;
  }
}

// encode the input file and write the encoding to the output file
void huffcoder_encode(struct huffcoder * this, char * input_filename, char * output_filename)
{

}

// decode the input file and write the decoding to the output file
void huffcoder_decode(struct huffcoder * this, char * input_filename, char * output_filename)
{

}
