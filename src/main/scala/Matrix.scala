type Mat = List[List[Double]]

class Matrix(m: Option[List[List[Double]]]) {

  def transpose: Matrix = ???
  def map(f: Double => Double): Matrix = ???
  def *(other: Matrix): Matrix = ???
  def ++(x: Double): Matrix = ???
  def -(other: Matrix): Matrix = ??? 

  def data: Option[Mat] = ???
  def height: Option[Int] = ???
  def width: Option[Int] = ???
  override def toString: String = ???
}

object Matrix {
  def apply(data: Mat): Matrix = ???
  def apply(data: Option[Mat]): Matrix = ???
  def apply(dataset: Dataset): Matrix = ???
}
