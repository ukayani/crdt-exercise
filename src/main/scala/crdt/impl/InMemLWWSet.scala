package crdt.impl

import crdt.{ LWWSet, TimeSet }

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
class InMemLWWSet[E] extends LWWSet[E] {

  /**
    * Returns a newly initialized TimeSet instance
    *
    * @tparam E
    * @return
    */
  override protected def createTimeSet[E]: TimeSet[E] = InMemTimeSet[E]
}

object InMemLWWSet {
  def apply[E]: InMemLWWSet[E] = new InMemLWWSet[E]()
}
