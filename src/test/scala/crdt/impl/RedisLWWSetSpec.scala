package crdt.impl

import com.redis.RedisClient
import crdt.{ LWWSet, LWWSetSpecBase }
import org.scalatest.{ BeforeAndAfterAll, BeforeAndAfterEach }

class RedisLWWSetSpec extends LWWSetSpecBase with BeforeAndAfterEach with BeforeAndAfterAll {
  val client = new RedisClient("localhost", 6379)

  override def afterEach =
    // clear stored data before next test runs for test isolation
    client.flushdb

  override def afterAll =
    client.disconnect

  def createLWWSet: LWWSet[String] = new RedisLWWSet[String](client, "testset")

}
