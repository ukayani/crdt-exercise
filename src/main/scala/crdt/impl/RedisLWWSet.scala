package crdt.impl

import com.redis.RedisClient
import crdt.{ LWWSet, Marshaller, TimeSet }

/**
  * An implementation of LWWSet using Redis ZSETs
  * @param redis - redis client
  * @param key - key for the set structure in redis, will create two keys prefixed with this key
  * @tparam E
  */
class RedisLWWSet[E: Marshaller](redis: RedisClient, key: String) extends LWWSet[E] {

  /**
    * Returns a newly initialized TimeSet instance for the add set
    *
    * @return
    */
  override protected def createAddTimeSet: TimeSet[E] = RedisTimeSet[E](redis, s"$key:add")

  /**
    * Returns a newly initialized TimeSet instance for the remove set
    *
    * @return
    */
  override protected def createRemoveTimeSet: TimeSet[E] = RedisTimeSet[E](redis, s"$key:remove")
}
