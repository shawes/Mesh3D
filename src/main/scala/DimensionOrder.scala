
class DimensionOrder(val order: String) {
  val X = 0
  val Y = 1
  val Z = 2

  def getFirst: Int = getValue(order.charAt(0))

  def getSecond: Int = getValue(order.charAt(1))

  private def getValue(c: Char): Int = {
    if (c == 'X') X
    else if (c == 'Y') Y
    else Z
  }

  def getThird: Int = getValue(order.charAt(2))

}
