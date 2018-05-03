class TimeSet[E] {
  private val elements = collection.mutable.HashMap[E, Long]()

  def add(elem: E, timestamp: Long): Long = {
    if (!elements.contains(elem) || (elements.contains(elem) && elements(elem) < timestamp)) {
      elements.put(elem, timestamp)
      timestamp
    } else {
      elements(elem)
    }
  }

  def get(elem: E): Option[Long] = {
    elements.get(elem)
  }
  def all(): Set[E] = elements.keySet.toSet

  def apply(elem: E): Long = elements(elem)

  def exists(elem: E): Boolean = elements.contains(elem)
}
