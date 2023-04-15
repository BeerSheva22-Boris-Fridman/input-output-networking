package telran.sportsmen;

public class footballer implements Sportsman {
String team;
	@Override
	public void action() {
		System.out.printf("plays football %s\n", team != null ? "for team " + team : "alone");

	}

}
