# How to create strategies

There are two files:
1) *._matrix.txt
2) *._pattern.txt

## Pattern file

The pattern file will be used to match the game field.
When we got a match the matrix file will be applied.

Matching rules:
1 = me
2 = other player
' ' = empty cell
. = don't care

The pattern file will be mirrored and rotated so you only 
need to define it once and not 8 times. 

## Matrix file

The matrix file will overwrite the original game matrix
with new values.

. = do not overwrite
A-Z = new positive value
a-z = new negative value
 