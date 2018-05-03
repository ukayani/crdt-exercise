import org.scalatest._

class LWWSetSpec extends FunSpec with MustMatchers {
  describe(".add") {
    it("must return specified timestamp if element does not exist") {
      val lww = LWWSet[String]

      lww.add("test", 5) mustBe 5
    }

    it("must return newest timestamp if element already exist") {
      val lww = LWWSet[String]

      lww.add("test", 5) mustBe 5
      lww.add("test", 4) mustBe 5
      lww.add("test", 6) mustBe 6
    }
  }

  describe(".remove") {
    it("must return specified timestamp if element does not exist") {
      val lww = LWWSet[String]

      lww.remove("test", 5) mustBe 5
    }

    it("must return newest timestamp if element already exist") {
      val lww = LWWSet[String]

      lww.remove("test", 5) mustBe 5
      lww.remove("test", 4) mustBe 5
      lww.remove("test", 6) mustBe 6
    }
  }
}
