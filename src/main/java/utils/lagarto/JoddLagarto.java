package utils.lagarto;

public class JoddLagarto {
	private static final JoddLagarto instance = new JoddLagarto();

	public static JoddLagarto defaults() {
		return instance;
	}
}