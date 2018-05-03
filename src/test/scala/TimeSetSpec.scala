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
  
  describe(".exists") {
    it("must return true if an element with specified key exists in the set") {
      val ts = new TimeSet[String]()
      val key = "mykey"
      
      ts.add(key, 1)
      
      ts.exists(key) mustBe true
    }
    
    it("must return false if an element with specified key does not exist in the set") {
      val ts = new TimeSet[String]()
      
      ts.exists("non-existent-key") mustBe false
    }
  }
}
