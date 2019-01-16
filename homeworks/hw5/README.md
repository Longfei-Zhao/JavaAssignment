Homework 5
==========

### Note

This homework comes after a long break. It warrants an extended scope, and 
therefore has two parts, each worth of 1 mark. 

Part One: Reading input and formatting output
---------------------------------------------

### Objectives

To practice formatting output and use of exceptions when working with external 
(user) data and defining a custom exception to match the domain semantics. 

### Problem

Write a Java program called *Percent* which repeatably prompts the user to 
input an integer, then (after the user enters an empty line, that is hits 
"return" key ↵ without entering anything) calculates the total sum and 
percentage which every input integers represents.

The program shall operate as follows: It shall prompt for a list of integers 
from the standard input, one per line, terminated by a blank line. It shall 
then calculate the total of those numbers and the percentage of that total that 
each represents. It shall present the output as a table, with two columns 
separated by exactly four spaces. The left column shall contain the original 
number, right-justified in a field of width four characters. The right column 
shall contain the percentages, followed by the % symbol, right justified in a 
field of width six characters (up to three digits before the decimal point, the 
decimal point, one figure after the decimal point, and the percent symbol). 
Percentages shall be rounded to one decimal place. (Did you use already 
``printf()`` or ``System.format()``? If not, this is the time!)

```bash
% java Percent
47
37
81
21
66
57

The numbers and percentage:
  47     15.2%
  37     12.0%
  81     26.2%
  21      6.8%
  66     21.4%
  57     18.4%
 309    100.0%

% java Percent
2
17
1
3

The numbers and percentage:
   2      8.7%
  17     73.9%
   1      4.3%
   3     13.0%
  23    100.0%

```

Notice that there is a blank line between the prompt for the last line of input 
and the start of the output. Notice also that the last line of output holds the 
total of all the inputs and "full percentage".

If there are no numbers input, or if their total is zero, the program should 
print an error message and stop (rather than trying to divide by zero). If one 
of the inputs is not an integer, it should print an error message and prompt 
for it again.

Make the program robust when the user enters a negative integer, or a 
non-integer, so the program does not crash, but corrects the user and offers 
another try:

```bash
% java Percent
2
-17
Non-negative integers only, try again:
seventeen
Non-negative integers only, try again:
17
1
3

The numbers and percentage:
   2      8.7%
  17     73.9%
   1      4.3%
   3     13.0%
  23    100.0%
```

You should check the validity of input with your own exception type, named, eg, 
*BadValueException*, and make your program to throw this exception if the 
entered input cannot be used to create a non-negative value of integer type, 
and to handle this exception to prevent the run-time error (and to achieve the 
behaviour which is exemplified above). Which standard exception should your 
*BadValueException* extend? Which part of your program should be programmed to 
throw this exception?

Part Two: Managing a bottle shop
--------------------------------

### Objectives

To practice the use of Java Foundation Classes API (generic container from
the ``java.util`` package), (anonymous) inner classes or lambda-expressions, 
and standard algorithms to manage large sets of data. The exercise will 
reinforce your experience obtained while working on Assignment One.

### Problem

Create the *Bottle* class with the field attributes: ``beer``, ``volume`` 
(double), ``alcoholContent`` (double), ``glassColour`` (``enum`` type), 
``price`` (this can be an object of a *Price* class, in which case define this 
as well; or it can be plain double), ``quantity`` (plain int will do unless you 
are a heavy drinker in which case use long type). The beer field should be an 
object of the *Beer* class which has fields --- ``brandName`` (String), 
``strength`` (double value for the amount of alcohol per unit volume), and 
perhaps few others (if you are a beer drinker you will think of something, 
surely), and also standard methods (``toString()`` etc).

Use the value of ``Beer.strength`` to determine the value of 
``bottle.alcoholContent`` field (also using the value ``bottle.volume``). The 
field ``alcoholContent`` is an example of the so called derived attribute -- it 
has no independently set value, rather values of other object fields determine 
it.

Then write the *BottleShop* class which allows to store a number of bottles of 
various kind. Include a number of methods needed to manage this inventory, like 
calculating the number of bottles of a given beer brand, price of the whole 
stock, etc. Also define some methods to sort and print the collection. Sorting 
can be various: by the beer brand name, by the alcohol strength or alcohol 
content of the single bottle (in some countries, the regulation limits the 
hours when strong alcoholic beverages can be sold), by the colour of the bottle 
glass (for recycling purposes), etc, etc, etc, let you practical fantasy flow...

Write a client program which uses the *BottleShop* collection (this can be done 
via inclusion the ``main()`` method in the ``BottleShop.java``). 

### Assessment

You will get up to two marks (one for each task; to receive 1 full
mark for Part One, your code must contain a custom exception class which the
main class uses, without such exception class your mark may not exceed 0.5), 
if you submit your
work by pushing the local repository using `git push` command by Friday,
5 May 2017, in *your* GitLab repository following the instructions which
are provided in the [Git and GitLab](https://cs.anu.edu.au/courses/comp6700/labs/gitlab/).

The code should be placed in
the `hw6` subdirectory of your locally cloned repository. You can optionally
(if the opportunity will exist) present your solution to tutor during the Week 
9 labs.


