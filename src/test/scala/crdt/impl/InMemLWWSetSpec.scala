package crdt.impl

import crdt.{ LWWSet, LWWSetSpecBase }

class InMemLWWSetSpec extends LWWSetSpecBase {
  override def createLWWSet: LWWSet[String] = InMemLWWSet[String]
}
