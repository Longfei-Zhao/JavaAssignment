Parsing ``JSON`` with *Jackson*
===============================

Assignment One requires you to read, process and analyse a set of data
in the JSON format, which simulate a real time stream of 
Twitter messages.

**J**ava**S**cript **O**bject **N**otation --- *JSON* --- is a widely used
format for inter-program data exchange. The format is defined in simple terms:

- each unit of data --- a JSON object --- is a comma-separated list of 
  *key-value pairs* inside a matching pair of curly braces, e.g.
  
   ``json
   {
	   key1:value1, 
	   key2:value2,
	   ...
   }
   ``
   
   where the white spaces do not matter.
   
-  a JSON key is a string (name), and the associated value is one of the JSON types:

   - ``string`` 
   - ``array``
   - ``number``
   - ``boolean`` 
   - ``object`` --- a JSON object, allows a composite object to be constructed from simpler JSON objects
   - ``null``.
   
   Some examples of JSON data are given in the file ``tweets.JSON``, which
   contains a collection of messages written by users of the *Twitter* network.
   Each JSON record is a single message, and messages are separated by the
   newline character.
   
Your program will convert Twitter data from the JSON representation to 
a collection of Java objects, following rules for mapping JSON types to
Java types.
The process of reading data in a text format like JSON and converting it into a
data structure inside a Java program is called *parsing*.
Parsing is a difficult task, so you will be allowed to use a special Application
Programming Interface (API) to perform it.

There are many JSON APIs available for Java, which can convert from JSON objects
to Java objects ("reading") or from Java objects to JSON objects ("writing").
The objects created by reading are usually so-called
*POJO*s --- **P**lain **O**ld **J**ava **O**bjects.
A POJO class defines only fields and accessor and mutator methods (also known
as getters and setters) for each field. An example of such as class is:

```java
    public class Planet {
       	
    	private String name;
    	private int catalogNumber;
    
    	public String getName(){ 
    		return this.name; 
    	}
    
    	public void setName(String n) {
    		this.name = n;
    	}
    	... // get- and set- methods for catalogNumber
    }
```

One popular JSON API for Java is called 
[Jackson](http://wiki.fasterxml.com/JacksonHome). It defines simple ways to
create a POJO from the corresponding JSON object (represented by a string
``jsonObject``) using an `ObjectMapper`:

```java
    ObjectMapper mapper = new ObjectMapper();
    String jsonObject = "{\"name\":\"Jupiter\",\"catalogNumber\":6}";
    Planet p = mapper.readValue(jsonObject, Planet.class); 
    System.out.println("Read and parsed Planet from JSON: " + p);
    Planet q = new Planet(1, "Sun");
    System.out.print("Planet object " + q + " as JSON = ");
    mapper.writeValue(System.out, p);
```

When we work with JSON data for which the complete specification is not
known, or not relevant, we may not have a POJO defined by a class. In
such a case, another, more flexible way of reading a JSON object can be convenient
--- we can convert it into a ``Map<String,Object>`` object (its keys are 
``String``s, and values are ``Object``s).

```java
    ObjectMapper mapper = new ObjectMapper();
    Map<String,Object> tweet = new HashMap<>();
    String record = ... // get a string representing a tweet in json
	tweet = mapper.readValue(line, Map.class); // the tweet info is encoded
	                                           // as Map object
```

The individual fields of the tweet can then be extracted from the ``Map``,
to allow the content of the tweet to be processed.

A good tutorial on how to use Jackson is 
[JacksonInFiveMinutes](http://wiki.fasterxml.com/JacksonInFiveMinutes).

We provide three small programs which read in the tweet
file, take its contents line-by-line, parse the lines and convert them into
*Map* objects.
Each program assumes that the tweets are presented as a single record per line,
with no empty lines.
Each program performs the parsing task as described above using a different API:

1. ``JacksonSampler.java``  --- uses ``Scanner`` and standard (external) iterators
2. ``JacksonSamplerNIO.java``  --- uses ``NIO`` for reading the tweets
3. ``JacksonSamplerStream.java``  --- uses streams to (internally) traverse
   the tweets.

To use Jackson in your program requires that the Jackson library is accessible
at compile and run-time.
Typically, a build manager like *Maven* would be used to fetch the correct
version of Jackson and put it in a standard location.
However, for this project we do not want you to use a build manager.
Instead, we have packaged a copy of the Jackson library for you inside this
repository, as three JAR files in the ``jackson`` directory.
These files were originally downloaded from the central Maven repository for
[Jackson release 2.8.7](http://repo1.maven.org/maven2/com/fasterxml/jackson/).

To compile and run the example programs (on UNIX/Linux), use the provided
script ``readTweets.sh``.
The script accepts one argument which is the name of the source file for the
example program.
For example:

```shell
% ./readTweets.sh JacksonSampler.java
```

A short video [How to use Jackson with IntelliJ IDEA](http://cs.anu.edu.au/courses/comp6700/v_media/3d-party-API-in-IDAE.mp4) demonstrates how the Jackson library can be used with a Java project in IntelliJ
IDEA.


