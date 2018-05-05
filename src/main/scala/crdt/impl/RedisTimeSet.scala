package crdt.impl

import com.redis._
import crdt.{ Marshaller, TimeSet }

class RedisTimeSet[E: Marshaller] private (redis: RedisClient, key: String) extends TimeSet[E] {
  import RedisTimeSet._
  import serialization.Parse.Implicits.parseLong

  private val marshaller = implicitly[Marshaller[E]]

  /**
    * Adds element to set if it doesn't exist. If element already exists, the element will be updated only if
    * the provided timestamp is newer (larger) than the existing element's timestamp
    *
    * @param elem
    * @param timestamp - A long value representing a time value, bigger means newer
    * @return The newest timestamp between the element being added and the element which exists (if it exists)
    */
  override def add(elem: E, timestamp: Long): Long =
    redis.evalBulk[Long](addScript, List(key), List(marshaller.marshal(elem), timestamp.toString)).get

  /**
    * Returns the timestamp of the element if it exists, otherwise returns None.
    *
    * @param elem
    * @return
    */
  override def get(elem: E): Option[Long] = redis.zscore(key, marshaller.marshal(elem)).map(_.toLong)

  /**
    * Returns all elements as a regular set
    *
    * @return
    */
  override def all(): Set[E] = redis.zrange[String](key).get.map(marshaller.unmarshal).toSet

  /**
    * Returns the timestamp of the element if it exists, otherwise throws an exception.
    *
    * @param elem
    * @return
    */
  override def apply(elem: E): Long = redis.zscore(key, marshaller.marshal(elem)).map(_.toLong).get

  /**
    * Returns true if element exits, false otherwise.
    *
    * @param elem
    * @return
    */
  override def exists(elem: E): Boolean = redis.zscore(key, marshaller.marshal(elem)).exists(_ => true)
}

object RedisTimeSet {

  /**
    * This Lua script will only add the given element if it's timestamp/score is greater than the one currently
    * in the set for that element. If the element does not exist in the set, it will add it with the given timestamp
    *
    * Returns the latest timestamp/score for the added key
    *
    * Note: We could have simply called ZSCORE and ZADD in separate api calls, however this would result in a race condition.
    *
    * Putting both the retrieval of the score and add operation in a script ensures that they are done atomically, to avoid
    * race conditions

    */
  private val addScript = """
          local key = KEYS[1]
          local elem = ARGV[1]
          local newScore = ARGV[2]       
	        local existingScore = tonumber(redis.call('ZSCORE', key, elem))
	        if not existingScore or existingScore < tonumber(newScore) then
	          redis.call('ZADD', key, newScore, elem)
	          return newScore
          else
            return redis.call('ZSCORE', key, elem)
          end
	        """

  /**
    * Convenience constructor
    * @param redis
    * @param key
    * @tparam E
    * @return
    */
  def apply[E: Marshaller](redis: RedisClient, key: String): RedisTimeSet[E] = new RedisTimeSet[E](redis, key)
}
