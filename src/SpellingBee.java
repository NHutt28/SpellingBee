import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Neil Hutton
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // Calls recursive function that finds every combination
        gHelper("",letters);
    }

    // generate helper function
    public void gHelper(String s1, String s2) {
        // Base cases
        // Add the words using all letters
        if (s2.length() < 1) {
            words.add(s1);
            return;
        }
        // Add the word at every step
        if (!(s1.isEmpty())) {
            words.add(s1);
        }
        // Goes through each letter and tries every combo
        for (int i = 1; i < s2.length() + 1; i++)
        {
            // Go through each permutation
            {
                gHelper(s1 + s2.substring(i - 1, i), s2.substring(0,i-1) + s2.substring(i));
            }
    }
    }

    //  Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // Just sorts the main array
        words = mergeSort(words);
    }

    // merge sort method
    public ArrayList<String> mergeSort(ArrayList<String> copy)
    {
        // Base case it's already sorted
        if(copy.size() <= 1)
        {
            return copy;
        }
        // Middle of array
        int mid = (copy.size() / 2);
        // Initializes the two halves
        ArrayList<String> left = new ArrayList<String>(mid);
        ArrayList<String> right = new ArrayList<String>(mid);
        int count = 0;
        // Fills the left half with its values
        while (count < mid)
        {
            left.add(count, copy.get(count));
            count++;
        }
        count = 0;
        // Fills right half
        while (count + mid < copy.size())
        {
            right.add(count, copy.get(count + mid));
            count++;
        }
        // Sort each array through recursion
        left = mergeSort(left);
        right = mergeSort(right);

        // Merge the two sorted arrays at every stage
        return merge(new ArrayList<String>(left.size() + right.size()), left, right);
    }
    // Merges two array list of strings
    public ArrayList<String> merge(ArrayList<String> word, ArrayList<String> left, ArrayList<String> right){
        // Counters for each array
        int count1 = 0;
        int count2 = 0;
        while (count1 < left.size() && count2 < right.size())
        {
            // If the left string is worth less than right, add it first
            if (left.get(count1).compareTo(right.get(count2)) <= 0)
            {
                word.add(count1 + count2, left.get(count1));
                count1++;
            }
            // The same but if right is worth less than left
            else
            {
                word.add(count1 + count2, right.get(count2));
                count2++;
            }
        }
        // Adds the rest of the array if one is longer
        while (count1 < left.size())
        {
            word.add(count1 + count2, left.get(count1));
            count1++;
        }
        while (count2 < right.size())
        {
            word.add(count1 + count2, right.get(count2));
            count2++;
        }
        // Returns the final product
        return word;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    //  For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // Goes through every word
        for (int i = 0; i < words.size(); i++)
        {
            // If it isn't found in the dictionary, remove it.
            if(!found(words.get(i)))
            {
                words.remove(i);
                // Make sure we don't skip over
                i--;
            }
        }
    }
    // Checks if string is found in dictionary
    public boolean found(String s)
    {
        int comp;
        // First index
        int low = 0;
        // Last index
        int high = DICTIONARY_SIZE;
        // Initial midpoint
        int mid;
        while(true)
        {
            mid = low + ((high-low) / 2);
            // Compares the object at middle index with the string
             comp = DICTIONARY[mid].compareTo(s);
             if (comp > 0)
             {
                 // Looks to the left side
                  high = mid - 1;
             }
             else if (comp < 0)
             {
                 // Looks to the right side
                 low = mid + 1;
             }
             else
             {
                 // If they're equal you found it
                 return true;
             }
             if (high < low)
             {
                 // If you can't find the word
                 break;
             }
        }
        return false;
    }
    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
