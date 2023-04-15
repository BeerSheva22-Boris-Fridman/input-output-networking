package telran.view;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class InputOutputTest {

public static void main(String[] args) {
	InputOutput inputOutput = new StandardInputOutput();
	
	int response = inputOutput.readInt("enter a number and press enter button", "entered value is not a number");
	inputOutput.writeLine("number is: " + response);
	
	int response2 = inputOutput.readInt("enter number in the range from 2 to 10", "Imput error", 2, 10);
	inputOutput.writeLine("number from range is: " + response2);
	
	Predicate<String> predicate = s -> s.matches("^(.+)@(\\S+)$");
	String response3 = inputOutput.readStringPredicate("enter e-mail adress", "Wrong adress", predicate);
	inputOutput.writeLine("e-mail is: " + response3);
	
	Set<String> set = new HashSet<>();
	set.add("Volga");
	set.add("BMW");
	set.add("Ford");
	String response4 = inputOutput.readStringOptions("Choose between Volga, BMW and Ford", "Good choise, but not from given range", set);
	inputOutput.writeLine("You chose: " + response4);
	
	long response5 = inputOutput.readLong("enter number (Long) in the range from 2 to 100000", "Imput error", 2, 100000);
	inputOutput.writeLine("number is: " + response5);
	
	LocalDate response6 = inputOutput.readDateISO("enter date in YYYY-MM-DD format", "wrong format");
	inputOutput.writeLine("The date is: " + response6);
	
	LocalDate min = LocalDate.parse("2000-01-11");
	LocalDate max = LocalDate.parse("2020-01-11");
	String format = "YYYY-MM-dd";
	String prompt = String.format("enter date in %s format (get in the range)", format);
	LocalDate response7 = inputOutput.readDate(prompt, "wrong date", format, min, max);
	inputOutput.writeLine("the date is: " + response7);
	
	double response8 = inputOutput.readNumber("guess a number(D) in some range", "your number not in a range", 10.0, 20.0);
	inputOutput.writeLine("mecuian: " + response8);
}

}
