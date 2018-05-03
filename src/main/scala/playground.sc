
class TSet[E] {
  private val elements = collection.mutable.HashMap[E, Long]()
  
  def add(elem: E, timestamp: Long): Long = { 
      if (!elements.contains(elem) || (elements.contains(elem) && elements(elem) < timestamp)) {
        elements.put(elem, timestamp)
        timestamp
      } else {
        elements(elem)
      }
  }
  
  def exists(elem: E): Boolean = elements.contains(elem)
  def get(elem: E): Option[Long] = {
    elements.get(elem)
  }
  
  def all(): Set[E] = elements.keySet.toSet
  
  def apply(elem: E): Long = elements(elem)
}

class LWWSet[E] {
  private val addSet = new TSet[E]()
  private val removeSet = new TSet[E]()
  
  def add(elem: E, timestamp: Long): Long = addSet.add(elem, timestamp)
  def remove(elem: E, timestamp: Long): Long = removeSet.add(elem, timestamp)
  def exists(elem: E): Boolean = {
    if (addSet.exists(elem)) {
      !removeSet.exists(elem) || addSet(elem) > removeSet(elem)
    } else false
  }
  
  def get(): Seq[E] = addSet.all().filter(exists).toSeq
}


val l1 = new LWWSet[String]()

l1.add("test", 1)
l1.add("hello", 2)
l1.add("test", 3)
l1.remove("hello", 1)
l1.add("hey", 1)
l1.remove("hey", 2)

l1.exists("test")
l1.exists("hello")
l1.exists("hey")
l1.remove("hello", 2)

l1.get()