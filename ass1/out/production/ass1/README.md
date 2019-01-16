Analysing data feeds
====================

<span style="background-color:#E193CF">**Draft**, may undergo minor editing and
clarifications and/or alternations.</span>

<span style="color:red**">*Due on 18.00 pm EST Friday, April 7 2017*</span>

The [UN Global Pulse](http://unglobalpulse.org/) uses big data, often 
crowdsourced from social media, to understand global trends and crises.  For 
example, they conducted a [study which showed that Twitter messages (tweets) 
about ‘haze’ are correlated with forest fires in 
Indonesia](http://unglobalpulse.org/projects/forest-and-peat-fire-management-social-media).

In this assignment, you will build an alert system that will determine the 
probable location of disasters such as fires and floods by searching a stream 
of simulated tweets.

The simulated tweets are provided in a file [tweets.json](../tweets.json), which
contains data in [Twitter's JSON tweet format](https://dev.twitter.com/overview/api/tweets) 
(which is a version of the popular [JSON format](http://json.org/) used to 
exchange data between programs).
Each JSON data block (one per line) represents a single tweet.
They are intended to simulate data pulled from the [Twitter public streaming API, also known as the 'Firehose'](https://dev.twitter.com/streaming/public).
Reading the contents of the file will simulate the real-time stream of data from Twitter.
Your program will process each data block and extract relevant information,
such as:

1. time of origin
2. place of origin
3. keywords

Your program will filter and aggregate the data according to certain rules, and 
generate alerts based on the aggregated data e.g. 'Possible fire at Queanbeyan'.

Your program will perform a cumulative assessment: 

1. How many data blocks contain keywords which indicate a particular event, e.g 
   "fire"?
   
2. What is the extent (both temporal and spatial) of the event?
   For example, in the case of fire natural disaster, when did the fire 
   emergency start and end, and what is the size of the affected area?

The results of these event-analyses will be neatly formatted and printed or 
saved in a separate file.

The total size of your program (excluding comments) would be around 500 lines 
of code (LOC), depending on style of your code (high level or not) and choice 
of standard API. Use of good modularisation will be crucial for success.
The time necessary to study the necessary material (JSON data 
format and parsing) is around 15 hours.

Problem Description
-------------------

Each data block (aka, a *tweet*) is a short JSON (**J**ava**S**cript **O**bject 
**N**otation) record which contains meta-data (time stamp, geolocation, topic)
and a short body of text (a "message"). Each JSON record
is a comma-separated list of *key-value pairs* inside a matching pair of
curly braces, ``{key1:value1, key2:value2,...}``.
A JSON key is a string (name), and its value is
one of the JSON types: ``string, array, object, number, boolean`` or ``null``.

An example of a tweet as a JSON record may look like this:

```json
{
  "text": "Fire Alarm. Shoalhaven (75 Owen St, Huskisson,  2540) at 3 Mar 2017 16:10 https://t.co/eZ1btQ7mTC #NSWRFS #FireAlarm",
  "user": {
    "id": "4721717942",
    "name": "NSW Fire Updates"
  },
  "lang": "en",
  "coordinates": {
    "coordinates": [
      150.672193,
      -35.038762
    ],
    "type": "Point"
  },
  "created_at": "Fri Mar 03 05:44:33 +0000 2017"
}
```

(The JSON record above is shown in a pretty-printed form, to expose its
structure; however, the whitespace (including line breaks) has no significance.)
The actual content of the tweet is a string which is a value for the key `text`.
The other fields are metadata describing the context of the tweet. 
For some tweets, some of the metadata fields shown in the example may be missing
or empty; there may also be additional metadata that are irrelevant to your task.

A program which reads a JSON record and identifies its components is called a
(JSON) parser. Usually, the job of parsing is performed by specialised 
libraries (JSON API), but it may be done from scratch (for example, for 
learning reasons). Since json-parsing can be a rather difficult to do from 
scratch, we are allowing to use a third-party JSON API called *Jackson*.
Instructions and examples of how it can be use in the context of this assignment
is provided as well as the API itself (three jar-files).

### Topic matching

Once parsing is performed, one can analyse the contents and establish if it
contains keywords from a topic set. For example, in a tweet which reports on 
a fire emergency, the value corresponding to the `"text"` key *may* contain 
the word ``"fire"`` and one or several other keywords which belong to the 
*Topic 1* ("natural disasters") from the table below: 

**Topic 1**|**Topic 2**|**Topic 3**|**...** 
---|---|---|----
fire      |fire        |fire|...
smoke     |song        |Trump|...
burning   |gig         |Putin|...
flame     |performance |Hanson|...
haze      |crowd       |democracy|...
emergency |cheering    |free|...
disaster  |fun         |...|...

The easiest way to look for tweets which report on the topic of interest is
to match the text against a keyword. But a keyword may be present in multiple
topic sets (like "fire" in the topic sets above). We need to search for 
*meaning, not keywords*. Therefore, the program will be given the topic set
to carry out the search. The text of a tweet will be matched against 
*each keyword* from the set. If the text is found to contain the keywords, and
some of those keywords are also present in other topic sets, the text is also
matched against all keywords which are present in those topic sets. For example,
if a tweet contains the keyword "fire", it should be matched against all 
keywords in the Topic 1, **but also** against all keywords from Topic 2 and 
Topic 3, since finding "fire" in the tweet may, in fact, be indicative of the
topic "pop-music-event" (represented by the Topic 2 set), or of the topic
"political-debate" (represented by the Topic 3 set). In a general (but, perhaps, not too 
common) situation, we may find keywords (apart from the shared "fire") which
belong to several topic sets, and some of them are shared. To determine the
likelihood that the tweet does report on the topic of interest, we will consider
all found keywords (using the rule described above) and:

1. *tentatively*
   accept a tweet as reporting the topic of interest if it contains only one
   keyword which is present in the corresponding topic set and also in one or
   more other sets (e.g. "fire" which is present in "natural_disaster", 
   "pop-music-event" and "political-debate" topic sets);

2. otherwise, we will
   remove all keywords which are shared by the topic of interest and any other 
   topic set (e.g. "fire" will be removed since it is shared between 
   "natural_disaster", "pop-music-event" and "political-debate"), and compute
   a difference between the number of remaining keyword which belong to the
   chosen set and all other keywords from different sets; 
   - if the difference is positive, accept the tweet as reporting, 
   - if the difference is negative, reject the tweet, 
   - if the difference is zero, accept the tweet *tentatively*
   
   Tentatively accepted tweets will be marked by a boolean marker, and will be
   included in reporting of an event only if there are also definitively
   accepted tweets about the same event.


The program will then print the following information extracted from the tweets 
which report about fire emergency: 

- user id and name
- geo-location coordinates
- timestamp 

The subsequent analysis will process the fire-reporting tweets and compute
how many of them originated from the same region (they cluster geographically),
and were posted about the same time (they cluster temporally).
Any tweets with the same topic that are separated by an interval of less than
one hour and less than one degree of both latitude and longitude are assumed
to refer to the same event.
(useful hints to approach this task --- *if deemed necessary* --- may be 
provided later).


Example of program operation
----------------------------

The program data will contain the following data sets:

1. the tweet file [`twitterfeed.json`](twitterfeed.json) (aka, the feed stream, since we simulate a 
   real feed)
2. several keyword sets representing *concepts*; some sets may intersect, *ie*
   contain same keywords (like "fire" in sets representing "fire natural 
   disaster" and "human relationships"); 
3. a property file (called "concepts.property"), which maps a concept name to
   a file containing relevant keywords (like those shown as columns in the
   table above); a contents of the property files may look as follows:
   
   ```java
   natural_disaster=file01.txt
   pop_music=file02.txt
   political_debate=file03.txt
   ... ...
   ```
   
The execution environment is defined by the following command-line options:

```
% java TwitterAnalyser twitterfeed.json
```

(this is the command line execution instruction; an IDE execution environment
should be set accordingly). At the start, the program reads in the property file
``topics.property``, and based on its contents (``key=value`` pairs, one per 
line), displays available choice of search concepts for the user to peek:

```
The following concepts can be used in search:

1. natural_disaster
2. human_relationships
3. political-debate
...

Choose by number (empty to quit):  
```

If the user makes a good choice (a number-like input when the number is one from
the displayed above), the program performs the search and reports its results
(see below for the output format specification). If the choice is incorrect
(the number is out of range, or is not a number), the program will report an
error and terminate. If the user enters an empty string, the program terminates
without an error.

An example of an output when a correct choice of the concept is made is as 
follows (assuming that the search concept was "natural_disaster" with the 
intent to search for fire-related events):

```
From 10,000 tweets between 00:00 and 23:59 of March 3, 2017, it is found:

- 341 tweets report on natural disaster in the region ... // provide coordinates here
- 255 of those were definitive, the rest were tentative

// more report on different natural disaster events if they are found
... ...
```

While you are provided with an example tweet file and topic configurations,
your code should work with any tweet or configuration files that are in the
same format.
For marking purposes, your code may be tested with different tweets and
configurations, which will not be available to you before submission.

Advice on how to approach the project
-------------------------------------

The best way to process and analyse the tweets data is to adopt the *pipeline
approach*:

- break the "continuous" input stream into
  separate tweets, and consider one tweet at a time;
- define the *Tweet* class, including its constructor which accepts a *String*,
  parses it, extracts relevant values and uses them to initialise corresponding
  fields;
- formulate (as a method) the criterion which can be used to accept 
  (tentatively, or definitively) a tweet as reporting on the topic of interest;
  what parameters such method should be defined with?
- use the above method to select the tweets;
- extract spatial and temporal values from selected tweets to determine the
  extent of the event in both time and space.
  (At the moment, we do not provide clues how to deal with this problem).
  
You are free to use a different approach if the above one looks not very 
convincing, but do attempt a design before plunging into coding.

Marking guide
-------------

The marks will be awarded in accordance with the following scheme:

- Creating a *program skeleton*: opening and reading a tweet file, loading
  properties which determine topic sets of keywords (these are files
  whose name are given by the property values); loading property and
  and opening other files must be done with appropriate exception handling
  to ensure robust program behaviour --- **4** points

- *Parsing the tweet file*, using the help of Jackson API (allowed and 
  encouraged!) to create *Tweet* objects which should only contain
  text, coordinates and timestamp (you should define an appropriate
  POJO class for this) --- **3** points

- Applying the rules of topic matching described above --- **3** points

- Group tweets together according to location and time, counting the number of 
  tweets related to each event --- **2** points

- *Reporting* in a nicely formatted manner --- **1.5** point

- The program *style* and quality of *comments* --- **1.5** points

Submission protocol
-------------------

The submission will be done by merging the final source code to your GitLab 
repository. The necessary instructions are provided in the 
[Forking your GitLab repository](https://cs.anu.edu.au/courses/comp6700/labs/gitlab/) --- a set of instructions
which is a part of Week Four exercises. All the source code for Assignment One
has to be inside the directory ``comp6700-2017/ass1`` (of your forked 
repository), and the last commit prior or on the due date will be regarded as
your submission. We do not prescribe the use of any particular IDE or
other tools (*tools agnostic* specifications), but you are free to include a
project file which an IDE creates to configure your project, but do 
**refrain from committing the byte code**, please.

In the GitLab repository which you will fork, we will provide additional data
(tweets and topic sets), and sample programs which may assist you with solving
some of the problems (like parsing tweet files).