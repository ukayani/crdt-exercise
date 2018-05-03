/**
  * Last Write Wins Element Set
  *
  * This set stores only one instance of each element in the set, and associates it with a timestamp.
  *
  * Intuitively, for a LWW Element Set:
  *  - An element is in the set if its most-recent operation was an add.
  *  - An element is not in the set if its most-recent operation was a remove.
  *
  * @tparam E
  */
class LWWSet[E] {
  private val addSet = TimeSet[E]
  private val removeSet = TimeSet[E]

  /**
    * Adds an element to the set if the element does not exist or it exists with an older timestamp
    *
    * @param elem
    * @param timestamp A long representing a time value. Bigger is newer
    * @return
    *         Returns the latest timestamp the element was added to the set
    */
  def add(elem: E, timestamp: Long): Long = addSet.add(elem, timestamp)

  /**
    * Removes an element from the set if the element has not been removed before or if it was removed with an older
    * timestamp
    *
    * @param elem
    * @param timestamp
    * @return
    *         Returns the latest timestamp that the element was removed from the set
    */
  def remove(elem: E, timestamp: Long): Long = removeSet.add(elem, timestamp)

}

object LWWSet {
  def apply[E]: LWWSet[E] = new LWWSet[E]()
}
