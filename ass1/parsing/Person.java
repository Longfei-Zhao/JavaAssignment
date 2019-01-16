public class Person {

	private int id;
	private String firstName;
	private String lastName;

	// need default constrcutor cause POJO requires it
	public Person() {

	}

	// need this one because the client (ReadWriteJackson)
	// calls it
	public Person(String fn, String ln) {
		this.firstName = fn;
		this.lastName = ln;
	}

	public int getId() {
		return this.id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String n) {
		this.firstName = n;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String n) {
		this.lastName = n;
	}

	@Override
	public String toString() {
			return String.format("Person %s %s (id %d)", this.firstName,
							this.lastName, this.id);
	}

}
