package crdt.impl

import crdt.{ TimeSet, TimeSetSpecBase }

class InMemTimeSetSpec extends TimeSetSpecBase {
  override def createTimeSet: TimeSet[String] = InMemTimeSet[String]

  override def createTimeSet(members: (String, Long)*): TimeSet[String] = InMemTimeSet[String](members: _*)
}
