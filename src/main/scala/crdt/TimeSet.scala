package crdt

/**
  * A set structure which accepts elements of type E with an associated timestamp value (Long)
  *
  * This set's add operation will replace existing elements only if the timestamp is newer than what is currently
  * in the set.
  *
  * @tparam E
  */
trait TimeSet[E] {

  /**
    * Adds element to set if it doesn't exist. If element already exists, the element will be updated only if
    * the provided timestamp is newer (larger) than the existing element's timestamp
    *
    * @param elem
    * @param timestamp - A long value representing a time value, bigger means newer
    * @return The newest timestamp between the element being added and the element which exists (if it exists)
    */
  def add(elem: E, timestamp: Long): Long

  /**
    * Returns the timestamp of the element if it exists, otherwise returns None.
    * @param elem
    * @return
    */
  def get(elem: E): Option[Long]

  /**
    * Returns all elements as a regular set
    * @return
    */
  def all(): Set[E]

  /**
    * Returns the timestamp of the element if it exists, otherwise throws an exception.
    * @param elem
    * @return
    */
  def apply(elem: E): Long

  /**
    * Returns true if element exits, false otherwise.
    * @param elem
    * @return
    */
  def exists(elem: E): Boolean
}
