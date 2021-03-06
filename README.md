date-math  [![Build Status](https://travis-ci.org/GBSA/date-math.png?branch=master)](https://travis-ci.org/GBSA/date-math)
========

A library for date calculations of fixed income securities, written in Scala, originally based on [jFin](http://jfin.org/wp/).




Why date-math
-------
*jFin* obtained a certain popularity in the financial software environment, as it provided ready-to-use implementations for day count convention, payment date scheduling and so on. We have been using jFin for quite a long time, and it t simply did what we wanted: compute the dates correctly. However, it suffered from major drawbacks:


1. The original code we downloaded was based on `java.util.Calendar` and `java.util.Date`, mutable data structures. Our code was polluted by `clone` to protect against undesired changes in the parameters provided to the function.
2. The original code was not designed according to the `SOLID` principles, making it hard to extend and to debug
3. When we tried to retrieve the last version from the svn trunk, there was a mix of the old source code, in package `org.jfin` and new source code in package `com.mbc.jfin` and the code didn't compile


As a consequence, we decided to try to rewrite it as a simple exercise. We then decided to publish the result
[@edmondo1984](https://github.com/edmondo1984) and [@jnguyenxuan](https://github.com/jnguyenx)

Philisophy
-----------
The `date-math` didn't want to implement all the features in `jFin`, but just the subset we were using. It has a very simple, yet powerful design.

1.  It provides a single entry point, `DateMath.scala` inside `com.gottexbrokers.datemath`. This was originally contained in `com.gottexbrokers.datemath` package object, but has to be moved out to guarantee **Java Interoperability**
2.  It relies on the largely used, de-facto standard library for date manipulation in Java: `JodaTime` 
3.  It is a functional library: the functions are pure, independent and composable. There is no mutable state. 
4.  It is coded against `JodaTime` interfaces, so that it does not impose restrictions on which concrete class you will be using
5.  It is largely tested using `ScalaCheck` to validate that same abstractions from the two libraries yield the same result



Credits
----------

To:

1. Morgan Brown Consultancy LTD for providing the original implementation of the manipulation functions
2. Kenji Yoshida (@xuwei-k) , author of [nscala-time](https://github.com/nscala-time/nscala-time)  , A new Scala wrapper for Joda Time based on scala-time, whose ideas inspired a part of the code 
 
