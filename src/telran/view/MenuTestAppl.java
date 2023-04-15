package telran.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MenuTestAppl {

	static String mainName = "Calculator";
	static String numCalcName = "Numbers Calculator";
	static String dateCalcName = "Dates Calculator";

	static Item exit = Item.exit();
	static InputOutput io = new StandardInputOutput();

	public static void main(String[] args) {

		ArrayList<Item> itemsMath = itemsMathFill();
		ArrayList<Item> itemsDate = itemsDateFill();

		Item[] items = new Item[3];
		items[0] = new Menu(numCalcName, itemsMath);
		items[1] = new Menu(dateCalcName, itemsDate);
		items[2] = exit;

		Menu menu = new Menu(mainName, items);
		menu.perform(io);
	}

	private static ArrayList<Item> itemsDateFill() {
		ArrayList<Item> itemsDate = new ArrayList<>();
		Consumer<InputOutput> dateAdd = x -> {
			LocalDate date = getDate();
			int days = getNdays();
			io.writeLine("new date is " + date.plusDays(days));
		};
		Consumer<InputOutput> dateSub = x -> {
			LocalDate date = getDate();
			int days = getNdays();
			io.writeLine("new date is " + date.minusDays(days));
		};
		Item add = Item.of("Add days", dateAdd);
		Item sub = Item.of("Subtract days", dateSub);
		itemsDate.add(add);
		itemsDate.add(sub);
		itemsDate.add(exit);
		return itemsDate;
	}

	private static int getNdays() {
		return io.readInt("enter number of days", "error");
	}

	private static LocalDate getDate() {
		return io.readDateISO("enter date in format YYYY-MM-dd", "error");
	}

	private static ArrayList<Item> itemsMathFill() {
		ArrayList<Item> itemsMath = new ArrayList<>();

		Consumer<InputOutput> sum = x -> {
			double firstNum = getFirstNumber(io);
			double secondNum = getSecondNumber(io);
			String res = String.format("the sum of %s and %s is %s ", firstNum, secondNum, (firstNum + secondNum));
			io.writeLine(res);
		};

		Consumer<InputOutput> sub = x -> {
			double firstNum = getFirstNumber(io);
			double secondNum = getSecondNumber(io);
			String res = String.format("the subtraction of %s and %s is %s ", firstNum, secondNum,	(firstNum - secondNum));
			io.writeLine(res);
		};

		Consumer<InputOutput> mult = x -> {
			double firstNum = getFirstNumber(io);
			double secondNum = getSecondNumber(io);
			String res = String.format("the result of multiply of %s and %s is %s ", firstNum, secondNum, (firstNum * secondNum));
			io.writeLine(res);
		};

		Consumer<InputOutput> div = x -> {
			double firstNum = getFirstNumber(io);
			double secondNum = getSecondNumber(io);
			while (secondNum == 0) {
				io.writeLine("can't divide by zero, enter another number");
				secondNum = getSecondNumber(io);
			}
			String res = String.format("the result of divide of %s and %s is %s ", firstNum, secondNum,	(firstNum / secondNum));
			io.writeLine(res);
		};
		
//		Item add = Item.of("Add two numbers", x -> {
//			io.writeLine(getFirstNumber(io) + getSecondNumber(io));
//		});
		
		Item add = Item.of("Add two numbers", sum);
		Item substract = Item.of("Subtract two numbers", sub);
		Item multiply = Item.of("Multiply two numbers", mult);
		Item divide = Item.of("Divide two numbers", div);

		itemsMath.add(add);
		itemsMath.add(substract);
		itemsMath.add(multiply);
		itemsMath.add(divide);
		itemsMath.add(exit);

		return itemsMath;
	}

	private static double getSecondNumber(InputOutput io) {
		return io.readNumber("enter 2nd number", "error", -Double.MAX_VALUE, Double.MAX_VALUE);
	}

	private static double getFirstNumber(InputOutput io) {
		return io.readNumber("enter 1st number", "error", -Double.MAX_VALUE, Double.MAX_VALUE);
	}

}
