Homework Three
==============

Prime Factorisation
-------------------

#### The Problem

It is a fact that any integer not equal to zero has a unique 
factorisation as product of primes. But finding this unique factorisation can 
take a very long time for large numbers. This principle is behind some 
important cryptographic algorithms such as the RSA algorithm. It is worthwhile 
to take a look at the following site to learn about prime factorisation. In 
particular, examples 1 and 2 of this site 
[http://www.mathsisfun.com/prime-factorization.html](http://www.mathsisfun.com/prime-factorization.html) demonstrate an algorithm 
which you might like to use in coding this homework. 

Your task for this homework exercise is to write a Java program that takes an 
integer as its command-line argument and prints out the complete prime 
factorisation of that integer, with the factors in ascending order. Your class 
must have at least one other method in addition to your main method. Your 
output needs to look similar to the example below. 

You do not have to do any other data checking. In other words, you may assume 
that the user will type in a non-negative integer, and that it's OK if your 
program crashes if they type in something else. You also do not have to worry 
about your program working for very big integers; just use long variables 
rather than the *BigInteger* class as in Homework 2.

Here is a sample run of such a program. Try to format your output as much like 
this as possible: 

```
% java Factors 15
15 = 1 * 3 * 5

% java Factors 2008
2008 = 1 * 2 * 2 * 2 * 251

% java Factors 1999817
1999817 = 1 * 1999817
(that's a prime number)
```

When you will have completed your Java program, test it on a few numbers until 
you are sure of its correctness. (Once again, don't worry if your program 
gives bad results for very large integers.) 

There are some excellent web sites which discuss cryptography and explain how 
prime factorisation is used. The "Art of the Problem" cryptography series looks 
pretty good: [https://www.youtube.com/watch?v=lICOtR078Gw](https://www.youtube.com/watch?v=lICOtR078Gw).

Further Programming Ideas
-------------------------

#### Fermat Little Theorem, Primality Tests and Carmichael Numbers

The primality test for an integer number *N* based on the factorisation is too 
inefficient for practice. Its complexity is *O(2<sup>(log N)/2</sup>)*, and if 
*N* is large (like in RSA algorithms --- 200-digit or more), the time for 
completing the algorithm like you tried above is ridiculously long.

One of the methods used in practical applications is based of the so-called
*Little Fermat Theorem* (LFT):

- If *p* is prime, then *a<sup>p-1</sup> - 1* is divisible by *p* for
  any *0 < a < p*.
  
In an equivalent form:

- If *p* is prime, then *a<sup>p-1</sup> = 1 mod p* is divisible by *p* for
  any *0 < a < p*.

The number *a* is called a *witness*. Write a method 
``fermatTest(long N, long a)`` which will test the equality featured in the
(second) form of the LFT. If you implement the operation of raising a number to 
a power (which is less then this number) using the Egyptian multiplication 
algorithm (it has different names, look up the details online) with the 
efficiency of O(log N), the efficiency of the primality test will be fast
(give the big-O expression!). 

It's all good, but the trouble is that some non-prime numbers also pass the test
--- it's "if" but not "iff". These are called Carmichael numbers. Their 
definition is:

- A *composite* number *N > 1* is called Carmichael number iff (if and only if)
  for every *b > 1*, which is coprime to *N* (*b* and *N* have no common prime
  factors) it follows that *b<sup>N-1</sup> = 1* mod *N*
  
For example, ``1722081 = 7 * 13 * 31 * 61`` is a Carmichael number.

1. Write a method ``boolean isCarmichael(long N)``.
2. Find the first seven Carmichael numbers using the above method.

*Final Note* The LFT and Carmichael tests are still rather inefficient for 
testing primality. In real application, a random test, like *Miller-Rabin*, is
used; it gives the answer with non-absolute but very high probability that a 
number is prime, and it gives it very fast.

You do not have to complete this part to get the maximum mark for this homework.

#### Assessment

You will get up to two marks, if you present a solution to the Homework 
exercise during the next week lab, or submitted it by push you GitLab repository
as explained in the [Forking your GitLab repository](https://cs.anu.edu.au/courses/comp6700/labs/gitlab/) exercise (part 
of the Lab 4).