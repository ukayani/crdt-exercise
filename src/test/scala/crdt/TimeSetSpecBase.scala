package crdt

import org.scalatest.{ FunSpec, MustMatchers }
import org.scalatest.OptionValues._

trait TimeSetSpecBase extends FunSpec with MustMatchers {
  def createTimeSet: TimeSet[String]
  def createTimeSet(members: (String, Long)*): TimeSet[String]

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
