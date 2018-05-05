package crdt

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
trait LWWSet[E] {

  private val addSet: TimeSet[E] = createAddTimeSet
  private val removeSet: TimeSet[E] = createRemoveTimeSet

  /**
    * Returns a newly initialized TimeSet instance for the add set
    *
    * @return
    */
  protected def createAddTimeSet: TimeSet[E]

  /**
    * Returns a newly initialized TimeSet instance for the add set
    *
    * @return
    */
  protected def createRemoveTimeSet: TimeSet[E]

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

  /**
    * Returns true if element exists in the set and false otherwise.
    * An element exists in the set if the most recent operation was an add
    *
    * Note: If an element was added and removed with the same timestamp, it is not considered a member of the set
    * @param elem - Element to check existence of
    * @return
    */
  def exists(elem: E): Boolean =
    if (addSet.exists(elem)) {
      !removeSet.exists(elem) || addSet(elem) > removeSet(elem)
    } else false

  /**
    * Returns all elements which are members of the set.
    * An element is a member of the set if exists(elem) == true
    * @return
    */
  def get(): Seq[E] = addSet.all().filter(exists).toSeq
}
