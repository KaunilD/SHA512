# SHA512
Calculate SHA512 hash of a String.

Since SHA512 uses a 64 bit key for calculating hash of input blocks along with the message block itself as a key I have used java.math.BigInteger to accomodate the strings.
Everything else is just the implementation of the algorithm. The program displays all 80 iterations of registers [a..h] and the message digest of the input String.

Compilation :
<code>javac HashValues.java SHA512Utils.java Main.java</code>

Execution : 
<code>java Main the path of K80 file</code>
e.g in my case :
<code>java Main /Users/kaunildhruv/Development/java/SHA512/src/K80</code>


