package ch.fhnw.dist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This is a Java implementation of the BloomFilter, which was developed as a small project work during the course discrete stochastics.
 *
 * @author  Fernandez Sebastian
 * @version 1.0
 * @since   2020-12-02
 */
public class Main {

    public static void main(String[] args) {

        List<String> wordsList = ReadWordsList();
        int wordsCount = wordsList.size();
        double probabilityFalsePositive = 0.001;

        // Adding words to the BloomFilter
        int counterOfWordsAddedToBloomFilter = wordsCount / 3;
        BloomFilter bloomFilter = new BloomFilter(counterOfWordsAddedToBloomFilter, probabilityFalsePositive);
        List<String> wordsToAddToBloomFilter = IntStream.range(0, wordsCount)
                .filter(n -> n % 3 == 0)
                .mapToObj(wordsList::get)
                .collect(Collectors.toList());

        for (String value : wordsToAddToBloomFilter)
            bloomFilter.add(value);

        // Checking words whethere they are in the BloomFilter.
        List<String> wordsToCheckContaining = IntStream.range(0, wordsCount)
                .filter(n -> n % 3 != 0)
                .mapToObj(wordsList::get)
                .collect(Collectors.toList());

        int falsePositiveCounter = 0;
        for(String value : wordsToCheckContaining) {
            if(bloomFilter.contains(value)){
                falsePositiveCounter++;
            }
        }


        // Console output, of BloomFilter tests.
        System.out.println("False positive parameter: " + String.format("%.10f", probabilityFalsePositive));
        System.out.println("Size of Hashing Array: " + bloomFilter.getSizeOfHashingArray());
        System.out.println("Number of Hashing Functions: " + bloomFilter.getNumberOfHashFunctions());
        System.out.println("Result false positive from BloomFilter Test: " + String.format("%.10f", (double)falsePositiveCounter / (double)counterOfWordsAddedToBloomFilter));
    }

    /**
     * Read words from resource package, file: words.txt
     *
     * @return List of words as String.
     */
    private static List<String> ReadWordsList(){
        List<String> wordsList = new ArrayList<>();
        BufferedReader bufferedReader;
        try {
            URL wordsURL = Main.class.getClassLoader().getResource("words.txt");
            assert wordsURL != null;
            File wordsListFile = new File(wordsURL.toURI());
            FileReader fileReader = new FileReader(wordsListFile);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                wordsList.add(line);
            }
        }catch (IOException | URISyntaxException exception){
            exception.printStackTrace();
        }

        return wordsList;
    }
}