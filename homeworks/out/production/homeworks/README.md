Homework 6
==========

Collecting Data from Stream Pipelines
-------------------------------------

### Objectives

To practice the use of stream pipelines for creating a complex data structures, 
and define a necessary "building infrastructure" when not relying on the 
standard API container. The material needed for this homework has been 
discussed in **F4** and **F5** lectures. Also, consult the textbook, Java 
Tutorial or any other suitable sources. 


### Preparations

Read the discussion of stream pipeline processing as discussed in the lectures
[F4](https://cs.anu.edu.au/courses/comp6700/lectures/F4.pdf) and 
[F5](https://cs.anu.edu.au/courses/comp6700/lectures/F5.pdf), or alternatively, in the Textbook, ch. 8, or [Oracle's Java Tutorial on Aggregate Operations](https://docs.oracle.com/javase/tutorial/collections/streams/).

Make sure that you have forked the [GitLab repository](https://gitlab.cecs.anu.edu.au/comp6700/comp6700-2017) and cloned the forked one <span style="background-color:#A5DCB8">**your**</span>
``comp6700-2017`` Gitlab repository (or more likely, if you have cloned it 
already, run `git pull` command to update it, either on the command line, or 
from an IDE if your are using one).

### Preliminaries

Donald "You're Fired!" Trump is <strike>a frontrunner for the republican 
nomination as a candidate for 2016 presidential elections in</strike> President
of the US-of-A. One of his election promises was to build a wall along the 
US–Mexico border to prevent illegal immigration. Such construction is not an 
easy enterprise. We shall help him to do this.

The original code consists of four source files:

1. ``Ball.java`` --- class of objects (balls) from which the stream is made of originally; instances of *Ball* are created with a randomly chosen colour.
2. ``Brick.java`` --- class of objects into which balls are converted (mapped) during the stream operations; bricks are created from balls to the same "mass" (naturally).
3. ``Wall.java`` --- the class of objects being built, a stream of bricks are reduced to a wall, or collected into one.
4. ``DonaldTrump.java`` — the main ("presidential") class.


### Exercise One: Study the Code

Compile and run the code. If you're using the command line (UNIX/Linux):

```bash

% javac -d . *.java
% java -cp . trump.DonaldTrump 20 20 330
```

(if using an IDE, don't forget to pass 3 command-line arguments in the
execution setup).

If your Terminal supports UTF-8 encoding (all modern ones should, it's just the 
matter of setting the preferences; UTF-8 encoding is the default on our Linux 
computers in the labs), then the output should be a "real" bricky wall to get 
printed out. If it doesn't, replace the values of ``Wall.sideBrick`` and 
``Wall.innerBrick`` with ``" "`` and ``" * "``, correspondingly, to have a 
"mock" brick wall printed.

Study the main class, *DonaldTrump*. Replace the ``reduce`` terminal method on 
``forEach``. What should you pass to this method to achieve the effect of wall 
building? What other changes are necessary to make the code compile and run 
correctly? How this version is different from the original one with the 
``reduce`` method? Which is betters and why?


### Exercise Two: ``collect``, not ``reduce``

Retract the changes in Exercise One. There is another possibility to modify the 
pipeline, which also collects together the desired results into an aggregate 
object. This approach involves using the ``collect(..,..,..)``, a 3-argument 
method, and it is best optimised with respect of an optimal and safe use of 
mutability when pipeline can be parallelised (We do not consider parallel 
streams in this study, though!). The collect method requires three parameters 
--- functions (λ-expressions):

1. a *supplier* function to construct new instances of the result aggregate
2. an *accumulator* function to incorporate an input element into a result aggregate
3. a *combining* function to merge the contents of one result aggregate into another

which is similar to the reduction:

```java

R collect(Supplier supplier,
        BiConsumer accumulator,
        BiConsumer combiner);

```

To execute the ``collect`` terminal operation, unless we're using a standard 
API container class like *ArrayList*, we need to define two methods in the 
class which we use as an aggregate, in our case the Wall. These two methods 
are: 

- ``void accept()``
- ``void combine()``

Answer the question "What is the supplier" here? A supplier functional 
interface has the signature ``() -> T``; ``T`` is obviously the *Wall*, but it 
has only one 2-argument constructor --- how can we create a wall with specified 
width and height in such a situation?

Next, add ``accept`` and ``combine`` methods to the *Wall* class; you can make 
Wall implement ``java.util.function.BiConsumer`` and annotate the method accept 
with ``@Override``, but this is not obligatory --- if a right λ-expression or a 
method reference is passed, the compiler will make a correct type inference.

You can surely use methods which are already defined in ``Wall.java`` to 
implement accept and combine. If you do this exercise correctly (the amount of 
additional code you need to write is very small, most challenging part is to 
think this all over!), it would be sufficient to receive the full mark for the 
homework.

### Exercise Three (**optional**): *Collector* class

The streams API provides helper classes to make programming standard pipeline 
operation easier. The interface ``java.util.stream.Collector`` defines a static
default method ``of(..)`` with 3 arguments:

```java

java.util.stream.Collector.of(
          java.util.function.Supplier supplier,
          java.util.function.BiConsumer accumulator,
          java.util.function.BinaryOperator combiner
     );

```

it returns a *Collector* object which can be passed to a 1-argument 
``collect(Collector c)`` method to perform the same task. Add a declaration of 
a collector variable ``donaldCollector`` to the ``DonaldTrump.main`` body and 
call ``Collector.of()`` to assign it a value, and then replace the 3-argument 
``collect()`` which you introduced in the previous Exercise, with a 
single-argument collect to which ``donaldCollector`` is passed. The result 
should be the same wall object like it was in Ex. Two.

**Note One** Alongside with the 3-argument collect, the *Collector* interface 
also provides a 4-argument version in which the fourth term is called 
*finisher*, and is used when you need to make a transformation of the final 
built aggregate into something different. A typical example would be create a 
string representing all the (relevant) information which is incorporated into a 
container built by the ``collect`` (the example is from 
[Benjamin Winterberg's Java 8 Stream Tutorial](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples)):


```java

List family =
    Arrays.asList(
        new Person("Donald", 70),
        new Person("Melania", 47),
        new Person("Baron", 11),
        new Person("Eric", 43),
        new Person("Donald Jr.", 40)
        new Person("Ivanka", 35)); // present wives and some children
	   
Collector personNameCollector =
    Collector.of(
        () -> new StringJoiner(" | "),          // supplier
        (j, p) -> j.add(p.name.toUpperCase()),  // accumulator
        (j1, j2) -> j1.merge(j2),               // combiner
        StringJoiner::toString);                // finisher

String names = family
    .stream()
    .collect(personNameCollector);

System.out.println(names);

```

**Note Two** It would a gratuitous exercise to replace a 3-argument collect 
with a 1-argument one by using a *Collector* actual argument, because the main 
benefit of the latter approach is the ability to compose multiple collectors. 
You are encouraged to investigate this approach further if it interests you, in 
particular, look into the documentation of the ``java.util.stream.Collectors`` 
utility class.

### Exercise Four (**optional**): Breaking out of a Stream

Finally, think (and conduct a research if necessary) how to remove a constraint 
put in the middle of the stream pipeline (``DonaldTrump.main:24`` in the 
original version) to ensure that the stream will terminate when the number of 
supplied bricks (not balls!) has reached a limit. This limit is set by the user 
(with the 3d command-line argument), and the assert statement 
(``DonaldTrump.main:16``) guarantees that the stream pipeline will not go on 
forever (try running the program with the value of 3d argument exceeding the 
product of 1st and 2nd, without ``-ea`` option and with it; ``-ea`` enables 
assertion execution).

Such an a priori constraint is a blemish on our code --- we should not use it 
because we do not have information about the wall size before the run-time. In 
a loop-based approach, this is a no-brainer problem --- every time a brick is 
laid into the wall, check if the latter is complete, and then "hits the breaks" 
by calling break. With a stream pipeline, however, this is not so 
straightforward. Try to come up with a solution. (It's "OK" to look up and find 
out what other people think, on *Stackoverflow* or else where.) Feel free to 
discuss you findings on Piazza.

![Build it!](../images/trump-wall.jpg "Build it!")

### Assessment

You will get up to two marks (one for each task), if you submit your
work by pushing the local repository using `git push` command by Friday,
12 May 2017, in *your* GitLab repository following the instructions which
are provided in the [Git and GitLab](https://cs.anu.edu.au/courses/comp6700/labs/gitlab/). 
The code should be placed in
the `hw6` subdirectory of your locally cloned repository. You can optionally
(if the opportunity will exist) present your solution to tutor during the Week 
10 labs.


