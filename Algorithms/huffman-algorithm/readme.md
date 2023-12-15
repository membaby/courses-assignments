# Huffman Coding

This Java program implements the Huffman coding algorithm to compress and decompress files. Huffman coding is a widely used algorithm for lossless data compression. It assigns variable-length codes to input characters, with shorter codes assigned to more frequent characters.

## Progress Update
### General
The program parses arguments from the command, and works according to the user specified input. After compressing or decompressing the target file, the program prints the time taken to complete the process and compression or decompression ratio.
### Compression
The program reads the file and builds a Frequency Map of each chunk of characters (determined by the specified bytes). Based on the Frequency Map, the program constructs a Huffman tree, where characters with higher frequencies have shorter codes.
### Decompression
Implemention for this part is not ready yet.

## TODO List
### Compression
- Assign codes to characters
- Encode the input text
- If length of bit streaps is not multiple of 8, add some padding to the text.
- Store padding information at the head
- Write the result to an output binary file

### Decompression
- Read binary file
- Read padding information and remove the padded bits.
- Decode the bits
- Save the decoded file into output file

## Usage
`java Huffman <method> <file> <bytes>`
- `Method`: Specify c for Compression or d for Decompression.
- `File`: Provide the absolute path to the input file.
- `Bytes`: Number of bytes considered together (required for compression only).

