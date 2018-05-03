import collection.mutable

/**
  * A set structure which accepts elements of type E with an associated timestamp value (Long)
  *
  * This set's add operation will replace existing elements only if the timestamp is newer than what is currently
  * in the set.
  *
  * @param elements initialize set with an existing hashmap
  * @tparam E
  */
class TimeSet[E] private (elements: mutable.HashMap[E, Long] = new mutable.HashMap[E, Long]()) {

  /**
    * Adds element to set if it doesn't exist. If element already exists, the element will be updated only if
    * the provided timestamp is newer (larger) than the existing element's timestamp
    *
    * @param elem
    * @param timestamp - A long value representing a time value, bigger means newer
    * @return The newest timestamp between the element being added and the element which exists (if it exists)
    */
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

  /**
    * Returns the timestamp of the element if it exists, otherwise returns None.
    * @param elem
    * @return
    */
  def get(elem: E): Option[Long] = elements.get(elem)

  /**
    * Returns all elements as a regular set
    * @return
    */
  def all(): Set[E] = elements.keySet.toSet

  /**
    * Returns the timestamp of the element if it exists, otherwise throws an exception.
    * @param elem
    * @return
    */
  def apply(elem: E): Long = elements(elem)

  /**
    * Returns true if element exits, false otherwise.
    * @param elem
    * @return
    */
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
