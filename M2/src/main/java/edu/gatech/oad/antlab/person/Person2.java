package edu.gatech.oad.antlab.person;

/**
 *  A simple class for person 2
 *  returns their name and a
 *  modified string 
 *
 * @author Bob
 * @version 1.1
 */
public class Person2 {
    /** Holds the persons real name */
    private String name;
	 	/**
	 * The constructor, takes in the persons
	 * name
	 * @param pname the person's real name
	 */
	 public Person2(String pname) {
	   name = pname;
	 }
	/**
	 * This method should take the string
	 * input and return its characters in
	 * random order.
	 * given "gtg123b" it should return
	 * something like "g3tb1g2".
	 *
	 * @param input the string to be modified
	 * @return the modified string
	 */
	private String calc(String input) {
	  int length = input.length();
          int index;
          String mxdUp = "";
          for (int x = length; x > 0; x--) {
              StringBuilder bob = new StringBuilder(input);
              index = ((int) Math.ceil(Math.random() * 100))%x;  // Gives a random # from 1 to 100.
              mxdUp = mxdUp + input.charAt(index);               // Then mods it to get an index,
              bob.deleteCharAt(index);                           // deletes index from old string,
              input = bob.toString();                            // and adds it to the new one
          }
	  return mxdUp;
	}
	/**
	 * Return a string rep of this object
	 * that varies with an input string
	 *
	 * @param input the varying string
	 * @return the string representing the 
	 *         object
	 */
	public String toString(String input) {
	  return name + calc(input);
	}
}
