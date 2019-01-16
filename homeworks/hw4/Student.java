import java.util.*;

public class Student {

	private String name;
	private String address;
	private String degreeName;
	private String department;
	private int yearCommenced;

	private int studentID;
	private static int nextID;
	private final static int STUDENT_ID_BASE = 901000;

	static {
		nextID = STUDENT_ID_BASE;
	}

	/**
	* Returns value of name
	* @return name
	*/
	public String getName() {
		return name;
	}

	/**
	* Sets new value of name
	* @name -> this.name
	*/
	public void setName(String name) {
		this.name = name;
	}

	/**
	* Returns value of address
	* @return address
	*/
	public String getAddress() {
		return address;
	}

	/**
	* Sets new value of address
	* @address -> this.address
	*/
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	* Returns value of degreeName
	* @return degressName
	*/
	public String getDegreeName() {
		return degreeName;
	}

	/**
	* Sets new value of degreeName
	* @degreeName -> this.degreeName
	*/
	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	/**
	* Returns value of department
	* @return department
	*/
	public String getDepartment() {
		return department;
	}

	/**
	* Sets new value of department
	* @department -> this.department
	*/
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	* Returns value of yearCommenced
	* @return yearCommenced
	*/
	public int getYearCommenced() {
		return yearCommenced;
	}

	/**
	* Sets new value of yearCommenced
	* @yearCommenced -> this. yearCommenced
	*/
	public void setYearCommenced(int yearCommenced) {
		this.yearCommenced = yearCommenced;
	}

	/**
	* Student constructors
	* use this() to call other constructor
	*/
	public Student() {
		this(null);
	}

	public Student(String name) {
		this(name, null);
	}

	public Student(String name, String address) {
		this(name, address, null);
	}

	public Student(String name, String address, String department) {
		this.name = name;
		this.address = address;
		this.department = department;
		this.studentID = nextID;
		nextID++;
	}

	/**
	* Create string representation of Student for printing
	* @return name, department, yearCommenced, studentID
	*/
	@Override
	public String toString() {
		return "studentID:" + this.studentID + "\nname:" + this.name + "\ndepartment:" + this.department + "\nyear commenced:" + this.yearCommenced + "\n";
	}

	public static void main( String[] args ) {
		StudentTest.test();
	}
}

class StudentTest {
	static List<Student> studentList;

	static void test() {
		List<Student> studentList = new ArrayList<Student>();
		Student stu1 = new Student("Logan", "45 West Row", "CS");
		Student stu2 = new Student("Jessie", "Canberra", "Computer Science");
		Student stu3 = new Student("Somebody", "SomePlace", "SomeDeparment");
		studentList.add(stu1);
		studentList.add(stu2);
		studentList.add(stu3);
		for(Student student : studentList) {
			System.out.println(student);
		}
	}
}
