package crdt

import com.redis.RedisClient
import crdt.impl.RedisTimeSet
import org.scalatest._
import org.scalatest.OptionValues._

class TimeSetSpec extends FunSpec with MustMatchers with BeforeAndAfterEach with BeforeAndAfterAll {

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

  describe(".add") {
    it("must add an element if it doesn't exist") {
      val ts = createTimeSet
      val key = "non-existent-key"
      val timestamp = 1L

      ts.add(key, timestamp) mustBe timestamp

      ts.get(key).value mustBe timestamp
    }

    it("must replace an element if it's timestamp is older than the one being added") {
      val key = "mykey"
      val timestamp = 1L
      val ts = createTimeSet(key -> timestamp)

      ts.get(key).value mustBe timestamp

      ts.add(key, timestamp + 1) mustBe (timestamp + 1)
      ts.get(key).value mustBe (timestamp + 1)
    }

    it("must ignore element if it's timestamp is not newer than existing element") {
      val key = "mykey"
      val timestamp = 1L
      val ts = createTimeSet(key -> timestamp)

      ts.add(key, timestamp - 1) mustBe timestamp

      ts.get(key).value mustBe timestamp

    }

  }

  describe(".exists") {
    it("must return true if an element with specified key exists in the set") {
      val key = "mykey"
      val ts = createTimeSet(key -> 1L)

      ts.exists(key) mustBe true
    }

    it("must return false if an element with specified key does not exist in the set") {
      val ts = createTimeSet

      ts.exists("non-existent-key") mustBe false
    }
  }
}
