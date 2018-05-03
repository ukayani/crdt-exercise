import org.scalatest._
import org.scalatest.OptionValues._

class TimeSetSpec extends FunSpec with MustMatchers {
  describe(".add") {
    it("must add an element if it doesn't exist") {
      val ts = new TimeSet[String]()
      val key = "non-existent-key"
      val timestamp = 1L
      
      ts.add(key, timestamp)
      
      ts.get(key).value mustBe timestamp
    }
    
    it("must replace an element if it's timestamp is older than the one being added") {
      val ts = new TimeSet[String]()
      val key = "mykey"
      val timestamp = 1L
      
      ts.add(key, timestamp)
      
      ts.get(key).value mustBe timestamp
      
      ts.add(key, timestamp + 1)
      ts.get(key).value mustBe (timestamp + 1)
    }
    
    it("must ignore element if it's timestamp is not newer than existing element") {
      val ts = new TimeSet[String]()
      val key = "mykey"
      val timestamp = 1L
      
      ts.add(key, timestamp)
      ts.add(key, timestamp - 1)
      
      ts.get(key).value mustBe timestamp
      
    }
  }
}
