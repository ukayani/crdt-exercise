import com.redis._

import scala.collection.mutable

val r = new RedisClient("localhost", 6379)
import serialization.Parse.Implicits.parseLong
val luaCode = """
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
r.evalBulk[Long](luaCode, List("myset"), List("hey", "2"))
r.evalBulk[Long](luaCode, List("myset"), List("hey", "1"))

r.zscore("myset", "hey")

r.zrange[String]("myset")
r.zrange[String]("my")


val tst: Option[String] = Some("hello")
tst.exists(_ => true)