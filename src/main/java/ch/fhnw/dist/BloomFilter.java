package ch.fhnw.dist;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.util.Arrays;

public class BloomFilter implements iFilter{

    private int numberOfHashFunctions; // number of hash functions.
    private int sizeOfHashingArray; // size of the Bit-Array used for a single filter value.
    private int numberOfFilterElements; // number of filter values.
    private double probabilityFalsePositive; // probability of false-positives

    private int[] bloomHashingArray;

    public BloomFilter(int numberOfFilterElements, double probabilityFalsePositive){
        this.probabilityFalsePositive = probabilityFalsePositive;
        this.numberOfFilterElements = numberOfFilterElements;

        this.sizeOfHashingArray = Math.round((float)-(numberOfFilterElements * Math.log(this.probabilityFalsePositive) / Math.pow(Math.log(2), 2)));
        this.numberOfHashFunctions = Math.round((float)-(Math.log(this.probabilityFalsePositive) / Math.log(2)));
        this.bloomHashingArray = new int[sizeOfHashingArray];
        Arrays.stream(this.bloomHashingArray).forEach(m -> m = 0);
    }

    protected int getSizeOfHashingArray(){
        return this.sizeOfHashingArray;
    }

    protected int getNumberOfHashFunctions(){
        return this.numberOfHashFunctions;
    }

    /**
     * method to add a value to datastructure.
     *
     * @param value, to be added to the Array.
     */
    @Override
    public void add(String value) {
        for(HashFunction hashFunction : CreateMurmurHashArray())
            this.bloomHashingArray[getPositionFromHashedValueInFilterArray(value, hashFunction)] = 1;

    }

    /**
     * method to check whether datastructure has value.
     *
     * @param value, to be check if probably exists in Array.
     * @return boolean
     */
    @Override
    public boolean contains(String value) {
        boolean guard;
        for(HashFunction hashFunction : CreateMurmurHashArray()){
            guard = this.bloomHashingArray[getPositionFromHashedValueInFilterArray(value, hashFunction)] == 0 ? false : true;
            if(!guard) return false;
        }

        return true;
    }

    /**
     * method to calculate the position of the element in Array by given Hash Function.
     *
     * @param hashFunction, the murmur3_128 Bit Hash function.
     * @param value, value that shall be hashed by the given function.
     * @return int, position in the BloomFilter - Array, calculated with modulo based on the size of the array.
     */
    private int getPositionFromHashedValueInFilterArray(String value, HashFunction hashFunction){
        return Math.abs(hashFunction.hashString(value, Charset.defaultCharset()).asInt()) % this.sizeOfHashingArray;
    }

    /**
     * method to create Hashing Array of Murmur3_128 Functions
     *
     * @return HashFunction Array, generated with different seeds.
     */
    private HashFunction[] CreateMurmurHashArray(){
        var murmur128HashFunctions = new HashFunction[this.numberOfHashFunctions];
        for(var seed=0; seed<this.numberOfHashFunctions; seed++)
            murmur128HashFunctions[seed] = Hashing.murmur3_128(seed);

        return murmur128HashFunctions;
    }
}