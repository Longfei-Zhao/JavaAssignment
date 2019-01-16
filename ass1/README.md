Analysing data feeds
====================

<span style="color:red**">*Due on 18.00 pm AEST Friday, April 14 2017*</span>

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
generate alerts based on the aggregated data.
Your program will perform a cumulative assessment: 

1. How many data blocks contain keywords which indicate a particular event, e.g.
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
is provided as well as the API itself (three JAR files).

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

A tweet matches against a keyword if the keyword is found anywhere in the text
of the tweet.
A keyword may match part of a word, e.g. the keyword "fire" matches the text
"Bought some fireworks".
Keyword matching is case-insenstitive e.g. the keyword "fire" **does** match
the text "OMG FIRE!".

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

   When computing the difference, a keyword that is not present in the topic of
   interest but is present in multiple other topic sets is only counted once.
   
   Tentatively accepted tweets will be marked by a boolean marker, and will be
   included in reporting of an event only if there are also definitively
   accepted tweets about the same event.


The program will then print the following information extracted from the tweets 
which report about fire emergency: 

- user id and name
- geo-location coordinates
- timestamp 

### Grouping Tweets

The subsequent analysis will process the fire-reporting tweets and compute
how many of them originated from the same region (they cluster geographically),
and were posted about the same time (they cluster temporally).
Any tweets with the same topic that are separated by an interval of less than
six hours and less than half a degree of both latitude and longitude are assumed
to refer to the same event.

One way to compute the groups is to maintain a *bounding box* for each group of
points.
The bounding box is defined by the maximum and minimum values in each dimension
i.e. (minTime, maxTime, minLatitude, maxLatitude, minLongitude, maxLongitude).
If a point is less than six hours and less than half a degree of both latitude and longitude
distant from the bounding box, the point is added to the group and the bounding box is
enlarged accordingly.
After assigning each point to a group, if the bounding boxes of two groups are
closer than six hours and half a degree of latitude and longitude, the two
groups should be combined into a single group.

Example of program operation
----------------------------

The program data will contain the following data sets:

1. the tweet file [`tweets.json`](tweets.json) (aka, the feed stream, since we simulate a 
   real feed)
2. several keyword sets representing *topics*; some sets may intersect, *ie*
   contain same keywords (like "fire" in sets representing "fire natural 
   disaster" and "human relationships"); 
3. a property file (called "topics.properties"), which maps a topic name to
   a file containing relevant keywords (like those shown as columns in the
   table above); the content of the property file will look something like the
   following:
   
   ```java
   natural_disaster=file01.txt
   pop_music=file02.txt
   political_debate=file03.txt
   ... ...
   ```
   
The execution environment is defined by the following command-line options:

```
% java TwitterAnalyser tweets.json
```

(this is the command line execution instruction; an IDE execution environment
should be set accordingly). At the start, the program reads in the property file
``topics.properties``, and based on its contents (``key=value`` pairs, one per 
line), displays available choice of search topics for the user to peek:

```
The following topics can be used in search:

0. all topics
1. natural_disaster
2. human_relationships
3. political-debate
...

Choose by number (empty to quit):  
```

If the user makes a good choice (a number-like input when the number is one from
the displayed list), the program performs the search and reports its results
(see below for the output format specification). If the choice is incorrect
(the number is out of range, or is not a number), the program will report an
error and terminate. If the user enters an empty string, the program terminates
without an error.

The program should create an output which combines the search results in the 
form of JSON objects whose number is determined by the number of identified 
topics (zero if none was found). A topic should be first created as a POJO 
object and then serialised (i.e. converted into a JSON object) similar to how 
it's done in the sample program ``ReadWriteJackson.java``. The ``Topic`` class 
must contain the following fields:

- ``topicType`` --- type can be ``String`` or ``enum`` (``enum`` types are discussed in **O6**)

- ``numberOfDefinitives``: ``int``

- ``numberOfTentatives``: ``int``

- ``bbox`` --- an instance of the inner class ``BoundingBox``, whose definition is
  explained below (one can define ``BoundingBox`` as a "normal" outer class, too, 
  but the inner class option is more appropriate).

- two time-related fields of the type ``java.time.ZonedDateTime``, which
  correspond to the earliest and to the latest timestamps in all the tweets
  found to report on the topic.
 
A ``BoundingBox`` object should contain information about the distribution of 
tweet coordinates --- the coordinates of the top-left corner (given by 
*(min(x<sub>i</sub>), max(y<sub>i</sub>))* where *i* is an index which marks 
the relevant tweets) and the bottom-right corner
(given by *(max(x<sub>i</sub>), min(y<sub>i</sub>))*). If a tweet which has been
found to report on the topic of interests does not contain geolocation data,
it should be ignored when calculating the bounding box values.

A sample output for one topic JSON object is as follows:

```
{"topicType":"NaturalDisaster", "numberOfDefinitives":341, "numberOfTentatives":152, "cluster":{"found":true, "top-left":[150.69, -34.92], "bottom-right":[152.18, -36.41]}, "begin":"Tue Mar 07 10:04:46 +0000 2017", "end": "Tue Mar 07 14:17:21 +0000 2017"}
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
- formulate a method which can be used to identify 
  (tentatively, or definitively) a tweet as reporting on the topic of interest;
- use the above method to select the tweets;
- extract spatial and temporal values from selected tweets to determine the
  extent of the event in both time and space.
  
You are free to use a different approach if you prefer,
but do attempt a design before plunging into coding.

Two videos
----------

We provide two short videos which demonstrate how to add a 3rd party API
like Jackson to a project which is developed by IntelliJ IDEA, and how to
create a new project by cloning a GitLab repository (if that repository already
contains a JAR file with a 3rd party API, it will be included into the new 
project settings automatically):

- [How to add 3rd party API to IntelliJ's project setting](http://cs.anu.edu.au/courses/comp6700/v_media/3d-party-API-in-IDAE.mp4)

- [How to create a new project by cloning a GitLab repository](http://cs.anu.edu.au/courses/comp6700/v_media/new-project-by-cloning.mp4)

Marking guide
-------------

The marks will be awarded in accordance with the following scheme:

- Creating a *program skeleton*: opening and reading a tweet file, loading
  properties which determine topic sets of keywords (these are files
  whose name are given by the property values); loading property and
  and opening other files must be done with appropriate exception handling
  to ensure robust program behaviour --- **4** points

- *Parsing the tweet file*, using the help of Jackson API (allowed and 
  encouraged!) to create *Tweet* objects which should contain user id, 
  user name, text, coordinates and timestamp (you should define an appropriate
  POJO class for this) --- **3** points

- Applying the rules of topic matching described above --- **3** points

- Group tweets together according to location and time, counting the number of 
  tweets related to each event --- **2** points

- *Reporting* in a nicely formatted manner --- **1** point

- The program *style* and quality of *comments* --- **2** points

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
project file which an IDE creates to configure your project.
Please **do not commit Java byte code** i.e. the `.class` files.

In the GitLab repository (which you will fork) we provide additional data
(tweets and topic sets), and sample programs which may assist you with solving
some of the problems (like parsing tweet files).