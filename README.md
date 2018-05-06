# LWW Element Set Exercise

This project is a set of iterations to build a LWW Element Set CRDT in Scala:

- An implementation of a LWW Element Set using standard Scala data types 
- An implementation of a LWW Element Set built using Redis primitives 

# Requirements

This project assumes the following is installed:

- SBT 1.x+
- JDK 8 +
- Docker

# Running Tests

To run tests, in the project root run the following command:

```bash
$ sbt test
```

This will launch redis locally and run through both In-Memory and Redis implementations of the components below:


# Interfaces

## TimeSet

A set structure which accepts elements with an associated timestamp value (Long)

This set's add operation will replace existing elements only if the timestamp is newer than what is currently in 
the set.

This project contains two implementations of this type: 
- In Memory 
- Redis

Please see associated tests in `src/main/test/crdt/TimeSetSpecBase`

## LWWSet

This set stores only one instance of each element in the set, and associates it with a timestamp.

Intuitively, for a LWW Element Set:
- An element is in the set if its most-recent operation was an add.
- An element is not in the set if its most-recent operation was a remove.

This project contains two implementations of this type:
- In Memory
- Redis

Please see associated tests in `src/main/test/crdt/LWWSetSpecBase`

## Marshaller

A simple marshaller type which marshalls/unmarshals from a type to string and vice versa

This type is used by Redis implementations to serialize set elements to string and back


# Implementation Notes

### LWWSet
LWWSet relies on the functionality of two TimeSet for add and remove

Implementations only need to provide creation methods for the two sets, the rest of the
operations come with the LWWSet trait

### Serialization

Elements can be of any type as long as an associated Marshaller[E] is in scope.

An implementation for string and int based elements is already provided


# Improvements

### Non Blocking implementation for Redis

Currently the library being used for Redis does blocking calls. 
We can switch out the client with a non blocking one or wrap the blocking calls in Futures as
suggested by the library creator.

We can then use a higher kinded type parameter for `LWWSet` and `TimeSet` which can either be
Future[_] or Id[_] for non async implementations.

### Redis Pipelining

Currently the calls for exist on RedisLWWSet do two calls to Redis.
This could be further improved by utilizing pipelining on the connection to Redis

### Error Handling

Currently operations on Redis implementations can throw exceptions due to connection errors.

We could change the return type of the methods to better model error handling in scala.

For example in a Future based implementation we could return a failed Future.

Another assumption made is for serialization/deserialization errors. Currently it is assumed that
these operations do not fail and if they do, they must throw exceptions.

## Flexibility for timestamp type

Currently the timestamp type is fixed to Long. We could easily make this parameterizable and 
include a Marshaller for this type to Long.

This would require improving the Marshaller type to have `In` and `Out` type parameters.