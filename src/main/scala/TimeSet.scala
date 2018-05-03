import collection.mutable

class TimeSet[E] private (elements: mutable.HashMap[E, Long] = new mutable.HashMap[E, Long]()) {

  def add(elem: E, timestamp: Long): Long =
    // The existence check + put operation need to be done atomically to make this method thread safe
    this.synchronized {
      if (!elements.contains(elem) || (elements.contains(elem) && elements(elem) < timestamp)) {
        elements.put(elem, timestamp)
        timestamp
      } else {
        elements(elem)
      }
    }

  def get(elem: E): Option[Long] = elements.get(elem)

  def all(): Set[E] = elements.keySet.toSet

  def apply(elem: E): Long = elements(elem)

  def exists(elem: E): Boolean = elements.contains(elem)
}

object TimeSet {
  def apply[E]: TimeSet[E] = new TimeSet[E]()
  def apply[E](elements: (E, Long)*): TimeSet[E] = {

    val map = new mutable.HashMap[E, Long]()
    elements.foreach((map.put _).tupled)

    new TimeSet[E](map)
  }
}
