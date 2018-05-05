package crdt

/**
  * A type which allows for marshalling from type E to string and back
  *
  * @tparam E
  */
trait Marshaller[E] {

  /**
    * Converts a value of type E to it's string representation
    * @param value
    * @return
    */
  def marshal(value: E): String

  /**
    * Converts a string to it's representation as type E
    * @param value
    * @return
    */
  def unmarshal(value: String): E
}

object Marshaller {

  /**
    * Define some simple marshallers for string and int type elements
    */
  implicit val stringMarshaller = new Marshaller[String] {
    override def marshal(value: String): String = value

    override def unmarshal(value: String): String = value
  }

  implicit val intMarshaller = new Marshaller[Int] {
    override def marshal(value: Int): String = value.toString

    override def unmarshal(value: String): Int = value.toInt
  }
}
