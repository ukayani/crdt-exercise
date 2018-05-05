package crdt

import org.scalatest._

trait LWWSetSpecBase extends FunSpec with MustMatchers {

  def createLWWSet: LWWSet[String]

  describe(".add") {
    it("must return specified timestamp if element does not exist") {
      val lww = createLWWSet

      lww.add("test", 5) mustBe 5
    }

    it("must return newest timestamp if element already exist") {
      val lww = createLWWSet

      lww.add("test", 5) mustBe 5
      lww.add("test", 4) mustBe 5
      lww.add("test", 6) mustBe 6
    }
  }

  describe(".remove") {
    it("must return specified timestamp if element does not exist") {
      val lww = createLWWSet

      lww.remove("test", 5) mustBe 5
    }

    it("must return newest timestamp if element already exist") {
      val lww = createLWWSet

      lww.remove("test", 5) mustBe 5
      lww.remove("test", 4) mustBe 5
      lww.remove("test", 6) mustBe 6
    }
  }

  describe(".exists") {
    it("must return true for an element that was added but never removed") {
      val lww = createLWWSet

      lww.add("test", 1)

      lww.exists("test") mustBe true
    }
    it("must return false for an element that was added and subsequently removed with a newer timestamp") {
      val lww = createLWWSet

      lww.add("test", 1)
      lww.remove("test", 2)

      lww.exists("test") mustBe false
    }
    it(
      "must return true for an element that was added, removed, and then added again with monotonically increasing timestamps"
    ) {
      val lww = createLWWSet

      lww.add("test", 1)
      lww.remove("test", 2)
      lww.add("test", 3)

      lww.exists("test") mustBe true
    }

    it("must return true for an element that was added and then removed with an older timestamp") {
      val lww = createLWWSet

      lww.add("test", 2)
      lww.remove("test", 1)

      lww.exists("test") mustBe true
    }

    it("must return false for an element that was never added") {
      val lww = createLWWSet
      lww.exists("test") mustBe false
    }
    it("must return false for an element that was only ever removed, never added") {
      val lww = createLWWSet

      lww.remove("test", 1)
      lww.exists("test") mustBe false
    }
    it("must return false for an element that was added and removed with identical timestamp values") {
      val lww = createLWWSet
      lww.add("test", 1)
      lww.remove("test", 1)

      lww.exists("test") mustBe false

      lww.remove("test2", 1)
      lww.add("tes2", 1)
      lww.exists("test2") mustBe false
    }
  }

  describe(".get") {
    it("must return an empty list if no elements are added") {
      val lww = createLWWSet
      lww.get() mustBe Seq.empty[String]
    }

    it("must return a list containing all elements which were added but not removed") {
      val lww = createLWWSet
      lww.add("test", 1)
      lww.add("hello", 1)
      lww.add("world", 1)
      lww.get() must contain theSameElementsAs Seq("hello", "world", "test")
    }

    it("must not return the same element more than once") {
      val lww = createLWWSet

      lww.add("test", 1)
      lww.add("test", 2)

      lww.get() must contain theSameElementsAs Seq("test")
    }

    it("must not return elements which are removed with a later timestamp than they were added") {
      val lww = createLWWSet

      lww.add("test", 1)
      lww.remove("test", 2)
      lww.add("hello", 1)
      lww.remove("hello", 3)
      lww.add("world", 3)
      lww.remove("world", 2)

      lww.get() must contain theSameElementsAs Seq("world")
    }

  }
}
