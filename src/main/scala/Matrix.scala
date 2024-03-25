import scala.util.Try

type Mat = List[List[Double]]

class Matrix(m: Option[List[List[Double]]]) {

  def transpose: Matrix = {
    m match {
      case Some(data) => Matrix(Some(data.transpose))
      case None => Matrix(None)
    }
  }

  def map(f: Double => Double): Matrix = {
    m match {
      case Some(data) => Matrix(Some(data.map(_.map(f))))
      case None => Matrix(None)
    }
  }

  def *(other: Matrix): Matrix = {
    m match {
      case Some(data1) =>
        other.data match {
          case Some(data2) if data1.headOption.forall(_.length == data2.length) =>
            val result = data1.map { row1 =>
              data2.transpose.map { col2 =>
                row1.zip(col2).map { case (a, b) => a * b }.sum
              }
            }
            Matrix(Some(result))
          case _ => Matrix(None)
        }
      case None => Matrix(None)
    }
  }

  def ++(x: Double): Matrix = {
    m match {
      case Some(data) =>
        val newData = data.map(row => row :+ x)
        Matrix(Some(newData))
      case None => Matrix(None)
    }
  }

  def -(other: Matrix): Matrix = {
    m match {
      case Some(mat1) =>
        other.data match {
          case Some(mat2) if mat1.size == mat2.size && mat1.headOption.exists(_.size == mat2.headOption.fold(false)(_.size)) =>
            val result = mat1.zip(mat2).map { case (row1, row2) =>
              row1.zip(row2).map { case (elem1, elem2) =>
                elem1 - elem2
              }
            }
            Matrix(Some(result))
          case _ => Matrix(None)
        }
      case None => Matrix(None)
    }
  }



  def data: Option[Mat] = m
  def height: Option[Int] = m.map(_.length)
  def width: Option[Int] = m.flatMap(_.headOption.map(_.length))
  override def toString: String = m match {
    case Some(data) => data.map(_.mkString(" ")).mkString("\n")
    case None => "Matrix(None)"
  }
}

object Matrix {
  def apply(data: Mat): Matrix = new Matrix(Some(data))
  def apply(data: Option[Mat]): Matrix = new Matrix(data)
  def apply(dataset: Dataset): Matrix = {
    val data = dataset.data.tail.map(_.map(_.toDouble)).toList
    Matrix(Some(data))
  }
}
