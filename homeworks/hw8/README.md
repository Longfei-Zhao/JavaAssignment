Homework 8
==========

Functional Interfaces, Laziness and JavaFX
------------------------------------------

### Objectives

To use functional composition technique and lazy evaluation to create an
image processing utility.

**Note:** this homework is based on exercises from Chapter 3 of "Java SE
8 for the Really Impatient" book by Cay Hortsmann which were modified a
little.

### Preparations

Review the notion of λ-expressions and functional interfaces using
either [**F2**](https://cs.anu.edu.au/courses/comp6700/lectures/F2.pdf) 
lectures slides, or [Oracle's Lambda Expressions
tutorial](http://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html),
or one of the resources listed in **F2**, last slide (or anything useful
you can find online, just don't spread too wide and thing).

Make sure that you have forked the [GitLab repository](https://gitlab.cecs.anu.edu.au/comp6700/comp6700-2017) and cloned the forked one <span style="background-color:#A5DCB8">**your**</span>
``comp6700-2017`` Gitlab repository (or more likely, if you have cloned it 
already, run `git pull` command to update it, either on the command line, or 
from an IDE if your are using one). Change into `hw8` directory and find
three Java source files and a JPEG image file --- these will be the
resource which you shall use in this homework:

-   `ColourFilter.java`
-   `ImageDemo.java`
-   `LatentImage.java`
-   `amazing-trees.jpg`

Study the code of the `ColourFilter.java` program (including the
commented out "alternative" lines 29,30 which can be executed instead of
the "composed" transform, line 32), understand the meaning and the
effect of the `ColourFilter.compose(op1, op2)` which returns a
composition of two `java.util.function.UnaryOperator` objects, similar
to two functions composition in Mathematics:

        y = f(x), z = g(y) → h = g o f,   h(x) = g(f(x))

The *UnaryOperator* interface consists of the abstract method
`T apply(T t)`.

One obvious benefit of the image transformation composition is avoidance
of intermediate image object(s). Multiple transformations can be first
composed into one transformation, and *then* the original image is
"broken into pixels", and each is transformed in the final value, the
pixels combined back into the transformed image and returned. Such "only
once transforming" approach is known as lazy computation (a computation
is defined and ready to be used, but its actual use is postponed until
it is absolutely necessary).

Next, study an alternative image processing which allows to combine
multiple transformations. The client class
`ImageDemo.java` makes use of the class
*LatentImage* in the `LatentImage.java` file which contains a list of
unary operator objects — pending operations — and the terminal operation
(a method) `toImage()` which returns the result (an original image
transformed in sequence by a number of colour operations).

### Part One

Consider a `transform(Image in, UnaryOperator f)` method from the class
*ColourFilter*, and add the *ColourTransformer* functional interface:

    @FunctionalInterface
    interface ColourTransformer {
       Color apply(int x, int y, Color colorAtXY);
    }

to the class ColourFilter, and add another (overload name) static method
`Image transform(Image in, ColorTransformer f)` which performs an image
transforming operation similar to the method
`Image transform(Image in, UnaryOperator f)` where the
*ColourTransformer* interface object is used instead.

Make use of the new method to add a 10 pixel wide gray frame replacing
the pixels on the border of an image by calling it with the appropriate
λ-expression in place of *ColourTransformer* interface object. Observe
the effect of closure (what are the captured variables?)

Ponder why did we need the *distributed* (which depends of pixel
coordinate) colour filter to solve a problem like this.

### Part Two

Using the insight from the Part One, enhance (add new methods) the
*LatentImage* class to allow it perform `blur` operation in which every
pixel colour value of the original is replaced by the average of itself
and its eight neighbour pixels. Note, that such operations as blurring
require the knowledge of pixels from the previous stages (which precedes
blurring) and therefore cannot be done lazily.

### Assessment

You will get up to two marks (one for each task), if you submit your
work by pushing the local repository using `git push` command by 6pm Friday,
26 May 2017, in *your* GitLab repository following the instructions to
be provided in the [Git and GitLab](https://cs.anu.edu.au/courses/comp6700/labs/gitlab/). 
The code should be placed in
the same `hw8` directory where the original source and image files where
located when your cloned your local repository for the first time. You can 
optionally (if the opportunity will exist) present your solution to tutor during the Week 12 labs.

