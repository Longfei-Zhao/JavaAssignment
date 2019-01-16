Homework Four
=============

### Learning Standard Class Design and Minimising Repetition

#### The Problem

1.  Construct and test a *Student* class which stores the following
    data:

    -   Name
    -   Address
    -   Degree name
    -   Department
    -   Year Commenced

2.  Write accessor/mutator -- `get()/set(x)` -- methods for each field.
    If you are using an IDE, find out (ask your tutor for help, if necessary),
	how these methods can be generated automatically.

3.  Override `toString()` method of the *Student* class to return "Name,
    Department, Year Commenced" information on the *Student* object.
	Do you understand how does this affect the result of printing an object
	state?

4.  Write **overloaded constructors** so that a student can be
    constructed in several different way:

          Student()
          Student(name)
          Student(name, address)
          Student(name, address, department)

    Think how to minimise code repetition in these constructors by
    calling other constructor using the `this(…)` syntax. 
	
	Programmers should always strive to avoid coding the same instruction
	more than once. This principle is known as **DRY** -- 
	"Don't Repeat Yourself!"

5.  Write a test class, `StudentTest`, to test this class by creating an
    array of three students. Print out the students data as it's done by
    `toString()` method (actually you don't need to call this method;
    just put your object into a print statement and it gets called
    automatically!).

6.  Add a student ID field, `studentID`, and another *static* field
    `nextID` for counting students. Write a private method to increment
    the last value. Modify every constructor to set the value of
    `studentID` to the current value of `nextID`, and increment `nextID`
    each time a student object is constructed. Initialise the `nextID`
    to a static constant, `STUDENT_ID_BASE=901000` (static variables are
    initialised prior to the first constructor call). Finally, modify
    `toString()` method to print the student information including the
    student id.

7.  *Optional.*

- Modify the above program to use an `ArrayList<Student>` instead of
  an array `Student[]`. Why the new version is better?

- Modify the *Student* class so that the student ID is assigned
  based on the year of commencement (to fit the ANU format where
  the first two digits are the last two digits of the commencement
  year, and the remaining five digits represent a unique number
  incremented by every new student enrolled *on the same year*;
  for example, the two IDs -- 9207147 and 0207147 are both count
  7,147th student enrolled correspondingly on 1992 and on 2002.
  You may assume that there was no enrolled students before the
  University was founded, in 1947).



#### Assessment

You will get up to two marks, if you present a solution to the Homework 
exercise during the next week lab, or submitted it by push you GitLab repository
as explained in the [Forking your GitLab repository](https://cs.anu.edu.au/courses/comp6700/labs/gitlab/) exercise (part 
of the Lab 3).
