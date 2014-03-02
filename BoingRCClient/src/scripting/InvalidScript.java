package scripting;

public class InvalidScript implements Script {

	private final String name;
	
	public InvalidScript(String name) {
		this.name = name;
	}

	public final String getName() {
		return name;
	}
}
