Homework 7
==========

Programming with *JavaFX*: Events and Effects
---------------------------------------------

### Objectives

To start writing simple graphics and GUI programs to learn the basic of
event-driven programming, and have a a first experience of JavaFX RIA
framework.

### Note 

Define all programs be part of the package `hw7`.

### Preparations

Review the lectures on JavaFX and event-driven programming using either  [**R1**](https://cs.anu.edu.au/courses/comp6700/lectures/R1.pdf),
[**R2**](https://cs.anu.edu.au/courses/comp6700/lectures/R2.pdf),
[**R3**](https://cs.anu.edu.au/courses/comp6700/lectures/R3.pdf),
[**R4**](https://cs.anu.edu.au/courses/comp6700/lectures/R4.pdf),
lectures slides, or [Oracle's JavaFX Tutorials](http://docs.oracle.com/javase/8/javase-clienttechnologies.htm).

Make sure that you have forked the [GitLab repository](https://gitlab.cecs.anu.edu.au/comp6700/comp6700-2017) and cloned the forked one <span style="background-color:#A5DCB8">**your**</span>
``comp6700-2017`` Gitlab repository (or more likely, if you have cloned it 
already, run `git pull` command to update it, either on the command line, or 
from an IDE if your are using one).

### Exercise One: a simple JavaFX program with shapes

Using the *IntelliJ IDEA* IDE (or *Netbeans*, or *Eclipse*), create a
*JavaFX* program called `ColouredShapes.java`, which displays a few
simple shapes like square, triangle, circle and so on, all having
different colours.

Then (after succeeding in the previous step) attach an event handler to
each of the shape to detect events when the mouse moves into the region
occupied by the shape. By using the original Assignment Two code as
example, you will have to set `onMouseMovedProperty` of a shape object
to an `EventHandler⟨MouseEvent⟩` and implement the
`handle(MouseEvent e)` method to generate a desired effect when the
mouse enters the shape region. Consider the following effects:

1.  printing mouse coordinates to `stdout`
2.  changing the shape colour while the mouse is in and changing the
    colour back when it's out

### Exercise Two: adding transition effects

Create another program, called `SimpleTransitions.java`. which displays
a simple shape with colour and then does one of the following when an
event is generated:

1.  moves to another location (choose yourself which direction and how
    far) and stay there when you perform a mouse click inside it
2.  rotates by 45° clockwise when you press the key "UP", and by 45°
    counterclockwise when you press "DOWN"
3.  (**optional**) morphs into a different shape object when click the
    mouse outside the original shape. Make the shape changing lasts 3 or
    5 seconds.

When (if) all of the above transition effects work as described, try to
combine them so a transition involves movement (shift and rotation),
shape and colour changes. I leave it to you to choose the actual
combination and the event which triggers the transition: It can be a
mouse event, or a keyboard event, or you can add a button which will
trigger the transition when clicked.

### Exercise Three (**optional**): *Model* and *View*

Create two classes, `View.java` (which is a
`javafx.application.Application` class, like the above it contains the
`main-launch-start` methods) and `Model.java` class (which is an
ordinary Java class defining the data). Choose Model to represent a
collection of circles of different radii, locations and colours. These
must be logical shapes (like abstract geometrical objects), not
graphical primitives which JavaFX displays (examples of which we've seen
in Ex. 2). Then make the View to create the Model object (called
`model`), read its data which describe the shapes and **then** generate
graphical shapes according to their description in the `model` object.
Finally, extend the Model to allow the colour to take one of the several
values (use `private` array field `Color[] colours` to choose from) when
`model` object is created. Then, when the `model` is displayed in the
View, mouse clicks inside any of the circles result in the colour change
such that a sequence of successive clicks allow to change the circle
colour several times utilising the full gamut.

### Assessment

You will get up to two marks (one for each task), if you submit your
work by pushing the local repository using `git push` command by Friday,
19 May 2017, in *your* GitLab repository following the instructions which
are provided in the [Git and GitLab](https://cs.anu.edu.au/courses/comp6700/labs/gitlab/). 
The code should be placed in
the `hw7` subdirectory of your locally cloned repository. You can optionally
(if the opportunity will exist) present your solution to tutor during the Week 
11 labs.


