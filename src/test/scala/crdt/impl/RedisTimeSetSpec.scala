package crdt.impl

import com.redis.RedisClient
import crdt.{ TimeSet, TimeSetSpecBase }
import org.scalatest.{ BeforeAndAfterAll, BeforeAndAfterEach }

class RedisTimeSetSpec extends TimeSetSpecBase with BeforeAndAfterEach with BeforeAndAfterAll {
  val client = new RedisClient("localhost", 6379)

  override def afterEach =
    // clear stored data before next test runs for test isolation
    client.flushdb

  override def afterAll =
    client.disconnect

  def createTimeSet: TimeSet[String] = RedisTimeSet[String](client, "test:set")

  def createTimeSet(members: (String, Long)*): TimeSet[String] = {
    val ts = createTimeSet
    members.foreach((ts.add _).tupled)
    ts
  }
}
