import java.io.*;
import java.util.*;

class Main {

	private static StringBuilder alphabet = new StringBuilder();
    private static StringBuilder cypherKey = new StringBuilder ("abcdefghijklmnopqrstuvwxyz");
    private static ArrayList<Character> charsOutput = new ArrayList<Character>();
    private static String method, cName1, pName1, cName2, outputName, iv1, iv2;

	public static void main(String[] args) {
		if (args.length > 0) {
			method = args[0]; // sets mode used
			if (args.length == 5) {
				cName1 = checkExtenssion(args[1]); //Load names from args and check for extenssion
				pName1 = checkExtenssion(args[2]); //Load names from args and check for extenssion
				cName2 = checkExtenssion(args[3]); //Load names from args and check for extenssion
    			outputName = checkExtenssion(args[4]); //Load output file name
				
				if (method.equals("ECB")) {
					System.out.println("******* Starting in ECB mode ******* \n");
        			setupAlphabet();
        			readChars(pName1, cName1, cName2); //start reading characthers from file
        			showKey();
        			System.out.println("******* Finished with ECB mode ******* \n");
        			makeNewFile(charsOutput);
				}
			}

			else if (args.length == 7){
				cName1 = checkExtenssion(args[1]); //Load names from args and check for extenssion
				pName1 = checkExtenssion(args[2]); //Load names from args and check for extenssion
				cName2 = checkExtenssion(args[3]); //Load names from args and check for extenssion
    			outputName = checkExtenssion(args[4]); //Load output file name
    			iv1 = args[5];
    			iv2 = args[6];
    			
				if (method.equals("CBC")) {
					System.out.println("******* Starting in CBC mode ******* \n");
					CBCCracker cbc = new CBCCracker();
					System.out.println("******* Finished with CBC mode ******* \n");
					//CBC mode here
				}
			}
			else{
				System.out.println("Wrong Number Arguments");
				System.out.println("java <mode> <c1> <p1> <c2> <output> <IV1> <IV2>");
			}
		}
		else {
            System.out.println("You must pass arguments");
            System.exit(0);
        }
	}

	private static void setupAlphabet(){
		Random rand = new Random();
		char character = '0';
		for (int i = 0; i < 26;i++ ) 
			alphabet.append(character = (char)(i + 'a'));
	}

	/**
     * Checks the userInput to see if it contains the correct extension
     * if it doesn't then the correct extension is added and returned
     * @param  userInput [contains the name of the file imputed by the user]
     * @return           [returns a String containing the correct extension ".txt"]
     */
    private static String checkExtenssion( String userInput) {
        String ext          = ".txt";
        String userExt      = userInput.substring(userInput.length() - 4);
        String newString    = null;

        if (ext.equals(userExt)) 
            return userInput;
        
        else 
            newString = userInput+=ext;
            return newString;
    }

    /**
     * Reads charecters from file passed
     * @param filename [is the name of the file to be loaded]
     */
    private static void readChars(String filename, String filename2, String filename3){
    	char  char1 = '0';
    	char  char2 = '0';
    	char  char3 = '0';
    	try{
    			File file1 = new File(filename);
    			File file2 = new File(filename2);
    			File file3 = new File(filename3);

    			/* Setting all reading objects to start reading from the file. */
				BufferedReader reader = new BufferedReader(
				    new InputStreamReader(
				        new FileInputStream(file1)));

				BufferedReader reader2 = new BufferedReader(
				    new InputStreamReader(
				        new FileInputStream(file2)));

	        	BufferedReader reader3 = new BufferedReader(
				    new InputStreamReader(
				        new FileInputStream(file3)));

				int c1 = 0;
				int c2 = 0;
				int c3 = 0;

				/**
				 * Reads from both files when letter at the time and calls the
				 * Mapping function to create the key.
				 */
				while((c1 = reader.read()) != -1) {
						c2 = reader2.read();
					char1 = (char) c1;
					char2 = (char) c2;
					mapLetters(char1, char2);
				}

				/**
				 * Reads c2 char by char and decodes each letter at the time
				 */
				while((c3 = reader3.read()) != -1){
					char3 = (char) c3;
					decode(char3);
				}
			}

		catch(IOException e){
			System.out.println(e);
		}
    }

    /**
     * Cycles through alphabet and when a letter is found,
     * it substitutes the letter in that position by the one in the file building a key
     * @param letter1 [letter in that position for plain text]
     * @param letter2 [letter in that position for coded text]
     */
    private static void mapLetters(char letter1, char letter2)
	{
        for(int i=0; i < alphabet.length(); i ++)
       		if (alphabet.charAt(i) == letter1)
       			cypherKey.setCharAt(i, letter2);
	}

	/**
	 * Prints the Key Mapping to the Console
	 */
	private static void showKey() {
		System.out.println("The Key is : ");
		for (int i=0; i < alphabet.length(); i++)
			System.out.println(alphabet.charAt(i) + " -> " + cypherKey.charAt(i));
	}

	/**
	 * Decodes c2 file using the key extracted from c1/p1
	 * It builds the new decoded message simultaneously
	 * @param letter [description]
	 */
	private static void decode(char letter) {
		for (int i=0; i < cypherKey.length(); i++ )
			if (cypherKey.charAt(i) == letter)
				charsOutput.add(alphabet.charAt(i));				
	}

	/**
	 * Makes new file containing the new decoded message from c2
	 * @param words [description]
	 */
	private static void makeNewFile(ArrayList<Character> words){
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(outputName));
			//loop through our ArrayList
            for (int i = 0; i < words.size(); i++)
                //Write the string to the file
                writer.write(words.get(i));
                //writer.flush ensures than anything in the buffer is written to the file. If you do nto call this you may find an empty file. To be safe, you can call this after every write.
                writer.flush();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.flush();
					writer.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("******* New File Created Succesfully ******* \n");
	}
}