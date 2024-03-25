type Mat = List[List[Double]]

class Matrix(m: Option[List[List[Double]]]) {

  def transpose: Matrix = {
    m match {
      case Some(mat) => Matrix(Some(mat.transpose))
      case None => Matrix(None)
    }
  }

  def map(f: Double => Double): Matrix = {
    m match {
      case Some(mat) => Matrix(Some(mat.map(_.map(f))))
      case None => Matrix(None)
    }
  }

  def *(other: Matrix): Matrix = ???


  def ++(x: Double): Matrix = {
    m match {
      case Some(mat) => Matrix(Some(mat.map(row => row :+ x)))
      case None => Matrix(None)
    }
  }


  def -(other: Matrix): Matrix = ???

  def data: Option[Mat] = {
    m
  }
  def height: Option[Int] = {
    m.map(_.size)
  }
  def width: Option[Int] = {
    m.flatMap(_.headOption.map(_.size))
  }

  override def toString: String = {
    m match {
      case Some(mat) => mat.map(_.mkString(" ")).mkString("\n")
      case None => "Matrix(None)"
    }
  }
}

object Matrix {
  def apply(data: Mat): Matrix = ???
  def apply(data: Option[Mat]): Matrix = ???
  def apply(dataset: Dataset): Matrix = ???
}
