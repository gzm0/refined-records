package records.test

import utest._

// This is for 2.10.x compatibility!
import scala.language.reflectiveCalls

object VariousTests extends TestSuite {

  def defRecord(age: Int = 2) = records.R(
    "age" -> age,
    "name" -> "David")

  "allow to read the value directly"-{
    val record = defRecord()

    assert(record.age == 2)
  }

  "be created with a special constructor"-{
    val row = records.R("foo" -> 1, ("bar", 2.3), Tuple2("baz", 1.7))

    assert(row.foo == 1)
    assert(row.bar == 2.3)
    assert(row.baz == 1.7)
  }

  "allow renaming in imports"-{
    import records.{ R => X }
    val row = X("foo" -> 1)

    assert(row.foo == 1)
  }

  "allow aliases"-{
    val X = records.R
    val row = X("foo" -> 1)

    assert(row.foo == 1)
  }

  "be hygienic"-{
    object records {
      val R = Predef
    }
    assert(defRecord(3).age == 3)
  }

  import records.R
  "allow strange field names"-{
    val record = R(
      "type" -> "R",
      "blank space" -> "blank space",
      "1" -> 1,
      "1>2" -> "1>2",
      "豆贝尔维" -> "dòu bèi ěr wéi"
    )

    assert(record.`type` == "R")
    assert(record.`blank space` == "blank space")
    assert(record.`1` == 1)
    assert(record.`1>2` == "1>2")
    assert(record.`豆贝尔维` == "dòu bèi ěr wéi")
  }

  "allow to read the value in a closure"-{
    val record = defRecord()

    val f = () => record.name

    assert(f() == "David")
  }

  it should "allow Rows as a result in a method type" in {

    def query = defRecord()

    query.age should be (2)

  }

  it should "allow rows in generics" in {
    import language.existentials

    class Box[T](val x: T) {
      def get = x
    }

    val x = new Box(defRecord())

    x.get.age should be (2)

  }

  it should "allow to fill lists" in {
    val x = List.fill(1)(defRecord())

    x.head.age should be (2)
  }

  it should "LUB properly if both records are the same" in {
    val x = List(defRecord(), defRecord(3))

    x.head.age should be (2)
    x.last.age should be (3)

    val r = if (true) defRecord() else defRecord(3)
    val r1 = true match {
      case true => defRecord(3)
      case false => defRecord()
    }
    r.age should be (2)
    r1.age should be (3)
  }

  // Records have a curse that they can never be seen so
  // explicit mentions of records must be defined as case classes.
  case class AgeName(age: Int, name: String)

  it should "allow different valued rows in ascribed lists" in {
    val x = List[AgeName](defRecord().to[AgeName], defRecord(3).to[AgeName])
    x.head.age should be (2)
    x.last.age should be (3)
  }

  it should "allow to ascribe a result type" in {

    def query: AgeName =
      defRecord().to[AgeName]

    query.age should be (2)
  }

  it should "allow tuples to construct literal rows" in {

    val row = R(("foo", 1), ("bar", 2.3), Tuple2("baz", 1.7))

    row.foo should be (1)
    row.bar should be (2.3)
    row.baz should be (1.7)
  }
*/
}
