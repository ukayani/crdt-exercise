package crdt

trait Marshaller[E] {
  def marshal(value: E): String
  def unmarshal(value: String): E
}

object Marshaller {
  implicit val stringMarshaller = new Marshaller[String] {
    override def marshal(value: String): String = value

    override def unmarshal(value: String): String = value
  }

  implicit val intMarshaller = new Marshaller[Int] {
    override def marshal(value: Int): String = value.toString

    override def unmarshal(value: String): Int = value.toInt
  }
}
