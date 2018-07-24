import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;


public class HangmanGame {

	private final String wordToGuess;
	private char[] wordToGuessArr; //to be printed after each turn
	private HashSet<Character> alreadyGuessed;
	private HashMap<Character, LinkedList<Integer>> charToIndices; //list used to accommodate for characters that appear multiple times
	private int incorrectGuessesRemaining = 5;
	Scanner scanner;
	
	//sample word array used for testing purposes
	//TODO figure out how to pull words from external database
	String[] wordSamples = {"UNIVERSITY", "VIRGINIA", "CAVALIER", "BASKETBALL", "HAMBURGER", "DOG", "ZEBRA", "GIRAFFE", "MULTIPLE WORD TEST"};
	
	public HangmanGame() {
		this.wordToGuess = getWord().toUpperCase();
		this.wordToGuessArr = new char[wordToGuess.length()];
		this.charToIndices = new HashMap<Character, LinkedList<Integer>>();
		this.alreadyGuessed = new HashSet<Character>();
		fillIndexMapping(wordToGuess, charToIndices);
		fillWordArr(wordToGuessArr);
		this.scanner = new Scanner(System.in);
	}
	
	//TODO add more personality to the command line prompts, but focus on functionality for now
	public void start() {
		printInitialMessage();
		while (incorrectGuessesRemaining > 0 && !charToIndices.isEmpty()){
			char guess = getGuess();
			validateGuess(guess);
			printWordArr();
		}
		if (incorrectGuessesRemaining == 0) System.out.println("You lose, the correct answer was " + wordToGuess);
		else System.out.println("You win");
		scanner.close();
		
	}
	
	//returns random word from wordSamples array, to be updated to pull from database instead (once I learn how to do that...)
	private String getWord() {
		return wordSamples[(int)(Math.random() * wordSamples.length)];		
	}
	
	//fills hashmap to map each character to its indices
	private void fillIndexMapping(String wordToGuess, HashMap<Character, LinkedList<Integer>> charToIndices) {
		for (int index = 0; index < wordToGuess.length(); index++){
			char currentCharacter = wordToGuess.charAt(index);
			if (currentCharacter == ' ') continue; // don't put spaces in hashmap
			if (!charToIndices.containsKey(currentCharacter)){
				LinkedList<Integer> newList = new LinkedList<Integer>();
				newList.add(index);
				charToIndices.put(currentCharacter, newList);
			}
			else {
				charToIndices.get(currentCharacter).add(index);
			}
		}
	}
	
	//populates wordToGuessArr with underscores for each character
	private void fillWordArr(char[] wordToGuessArr) {
		for (int i = 0; i < wordToGuessArr.length; i++) {
			wordToGuessArr[i] = wordToGuess.charAt(i) == ' ' ? ' ' : '_';
		}
	}
	
	//prints initial message of game
	private void printInitialMessage(){
		//System.out.println("Welcome to hangman\n(" + wordToGuess + ")"); //This line would display the correct answer, used for debugging
		System.out.println("Welcome to hangman");
		printWordArr();		
	}
	
	//prints current state of wordToGuessArr
	private void printWordArr() {
		System.out.println("");
		System.out.println("Incorrect guesses remaining: " + incorrectGuessesRemaining);
		for (int i = 0; i < wordToGuessArr.length; i++){
			System.out.print(wordToGuessArr[i] + " ");
		}
		System.out.println("");
	}
	
	//prompts user until user provides a single alphabetic character
	private char getGuess() {
		boolean firstTime = true;
		String userInput;
		do {
			if (!firstTime) System.out.println("Invalid input, please enter a single character");
			firstTime = false;
			System.out.print("Guess: ");
			userInput = scanner.nextLine();
		} while(!isValidInput(userInput));
		
		return userInput.toUpperCase().charAt(0);
	}
	
	//tests to confirm that user entered a valid input (single alphabetical character)
	private boolean isValidInput(String userInput) {
		userInput = userInput.trim();
		if (userInput.length() != 1) return false;
		char guess = userInput.charAt(0);
		//could check ascii codes, but will use static Character method for simplicity
		return Character.isLetter(guess);
		
	}
	
	//process user's guess
	private void validateGuess(char guess) {
		
		//first check to see if user has already guessed this letter
		if (alreadyGuessed.contains(guess)){
			System.out.println("You have already guessed that letter");
		}
		
		//incorrect guess
		else if (!charToIndices.containsKey(guess)){
			incorrectGuessesRemaining--;
		}
		
		//correct guess
		else {
			//update wordToGuessArr (array that's printed after each turn) with correctly guessed characters
			for (int index : charToIndices.get(guess)){
				wordToGuessArr[index] = guess;
			}
			//remove character from hashmap
			charToIndices.remove(guess);
		}
		
		//add guess to alreadyGuessed
		alreadyGuessed.add(guess);		
	}
	
	
	
}
