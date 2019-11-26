// code for a huffman coder


#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <ctype.h>
#include <string.h>
#include "huff.h"
#include "bitfile.h"


// create a new huffcoder structure
struct huffcoder *  huffcoder_new()
{
    struct huffcoder * new = malloc(sizeof(struct huffcoder));
    memset(new,0,sizeof(struct huffcoder));
    new->tree = malloc(sizeof(struct huffchar)*NUM_CHARS);
    return new;
}

// count the frequency of characters in a file; set chars with zero
// frequency to one
void huffcoder_count(struct huffcoder * this, char * filename)
{
    FILE * file = fopen(filename,"r");
    unsigned char content;
    assert( file != NULL );
    while( !feof(file) ) {
        content = fgetc(file);
        this->freqs[content]+=1;
    }
    fclose(file);
    for (int n=0;n<NUM_CHARS;n++){
        if (this->freqs[n]==0)
            this->freqs[n]=1;
    }

}

void insertion_sort(int a[NUM_CHARS][2]){
    for ( int j = 1; j<NUM_CHARS; j++)
    {
        int i = j - 1;
        while(i>=0 && a[i][0]>a[i+1][0])
        {
          int temp = a[i][0];
          int temp2 = a[i][1];
          a[i][0] = a[i+1][0];
          a[i][1] = a[i+1][1];
          a[i+1][1] = temp2;
          a[i+1][0] = temp;
          i--;
        }
    }
}

struct huffchar * find_and_remove_smallest_node(struct huffchar ** tempArray, int size) {
  int smallest =0;
  for (int i = 0; i < size; i++) {
    if (tempArray[i]->freq<tempArray[smallest]->freq)
        smallest=i;
    else if ((tempArray[i]->freq==tempArray[smallest]->freq)&&
             (tempArray[i]->seqno<tempArray[smallest]->seqno))
        smallest=i;
  }
  struct huffchar * returningNode = tempArray[smallest];
  for (int n=smallest;n<size;n++){
    tempArray[n] = tempArray[n+1];
  }
  return returningNode;
}

void compoundWork(struct huffcoder * this, struct huffchar ** tempArray){
    int nchars = NUM_CHARS;
    int n = 0;
    while (nchars > 1){
        struct huffchar * smallest = find_and_remove_smallest_node(tempArray, nchars--);
        struct huffchar * smallest2 = find_and_remove_smallest_node(tempArray, nchars);
        struct huffchar * compound = malloc(sizeof(struct huffchar));
        compound->freq = smallest->freq + smallest2->freq;
        compound->seqno = NUM_CHARS + n++;
        compound->u.compound.left = smallest;
        compound->u.compound.right = smallest2;
        compound->is_compound = 1;
        tempArray[nchars-1]=compound;
    }
    this->tree = tempArray[0];
}


// using the character frequencies build the tree of compound
// and simple characters that are used to compute the Huffman codes
void huffcoder_build_tree(struct huffcoder * this)
{
    struct huffchar * tempArray[NUM_CHARS];
    for (int n=0;n<NUM_CHARS;n++){
        tempArray[n] = malloc(sizeof(struct huffchar *));
        tempArray[n]->freq = this->freqs[n];
        tempArray[n]->u.c =  n;
        tempArray[n]->seqno = n;
        tempArray[n]->is_compound=0;
    }

    compoundWork(this, tempArray);
}

void recursively_buildcodes(struct huffchar * someChar, int codelen, unsigned long long code, struct huffcoder * this){
    if (!someChar->is_compound){
        this->code_lengths[someChar->u.c] = codelen;
        this->codes[someChar->u.c] = code;
     }else {
        recursively_buildcodes(someChar->u.compound.left,codelen+1,code,this);
        code = code|((unsigned long long)1<<(codelen));
        recursively_buildcodes(someChar->u.compound.right,codelen+1,code,this);
    }
}


// using the Huffman tree, build a table of the Huffman codes
// with the huffcoder object
void huffcoder_tree2table(struct huffcoder * this)
{
    recursively_buildcodes(this->tree,0,0,this);
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
    printf("char: %d, freq: %d, code: %s\n", i, this->freqs[i], buffer);
  }
}



// encode the input file and write the encoding to the output file
void huffcoder_encode(struct huffcoder * this, char * input_filename,
		      char * output_filename)
{
    struct bitfile * input = malloc(sizeof(struct bitfile));
    struct bitfile * output = malloc(sizeof(struct bitfile));
    input = bitfile_open(input_filename,"r");
    output = bitfile_open(output_filename,"w");
    while (!bitfile_end_of_file(input)){
        int readbit = bitfile_read_bit(input);
        if (readbit==-1)
            break;
        unsigned long long bit = this->codes[readbit];
        int codelen = this->code_lengths[readbit]-1;
        while(codelen>=0){
            if (bit%2==1){
                bitfile_write_bit(output,1);
            }else{
                bitfile_write_bit(output,0);
            }
            bit = bit>>1;
            codelen--;
        }
    }
    unsigned long long bit = this->codes[4];
    int codelen = this->code_lengths[4]-1;
    while (codelen>=0){
        if (bit%2==1){
            bitfile_write_bit(output,1);
        }else{
            bitfile_write_bit(output,0);
        }
        bit = bit>>1;
        codelen--;
    }
//     if (output->index>=0){
//             codelen=output->index;
//     }
//     while (codelen>=0){
//         bitfile_write_bit(output,0);
//         codelen--;
//     }

    bitfile_close(input);
    bitfile_close(output);
}

// decode the input file and write the decoding to the output file
void huffcoder_decode(struct huffcoder * this, char * input_filename,
		      char * output_filename)
{
    struct bitfile * input = malloc(sizeof(struct bitfile));
    struct bitfile * output = malloc(sizeof(struct bitfile));
    input = bitfile_open(input_filename,"r");
    output = bitfile_open(output_filename,"w");
    int is_finished = 0;
    unsigned long long bits =0;
    struct huffchar * root = this->tree;
    struct huffchar * node = root;
    while (!is_finished&&bits!=-1){
        bits = bitfile_read_bit(input);
        for (int i=0;i<8;i++){
            unsigned long long bit_value = (bits >> i)&(unsigned long long)1;
            if (bit_value){
                 node = node->u.compound.right;
            }else{
                 node = node->u.compound.left;
            }
            if (!node->is_compound){
                if (node->u.c==4){
                    is_finished=1;
                    break;
                }
                unsigned long long character = node->u.c;
                for (int x=0;x<8;x++){
                    if (character%2==1)
                        bitfile_write_bit(output,1);
                    else
                        bitfile_write_bit(output,0);
                    character >>=1;
                }
                node = root;
            }
        }
    }
    bitfile_close(input);
    bitfile_close(output);
}
