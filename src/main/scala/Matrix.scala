import scala.annotation.tailrec
import scala.util.Try

type Mat = List[List[Double]]

class Matrix(m: Option[List[List[Double]]]) {

  def transpose: Matrix = {
    m match {
      case Some(data) => {
        if (data.isEmpty || data.head.isEmpty) {
          Matrix(None)
        } else {

          val tranposedData = data.head.indices.map { j =>
            data.map(row => row(j))
          }.toList

          Matrix(Some(tranposedData))
        }
      }
      case None => Matrix(None)
    }
  }

  def map(f: Double => Double): Matrix = {
    m match {
      case Some(data) => {
        // `.map(el => f(el))` <=> `.map(f)`
        val newMat: List[List[Double]] = data.map(row => row.map(el => f(el)))
        Matrix(Some(newMat))
      }

      case None => Matrix(None)
    }
  }

  /**
   * operatia de inmultie a doua matrici
   */
  def *(other: Matrix): Matrix = {
    m match {
      case None => Matrix(None)

      case Some(mat1) => {

        other.data match {
          case Some(mat2) => {

            val widthOfMat1: Int = mat1.head.length
            val heightOfMat2: Int = mat2.length

            if (widthOfMat1 != heightOfMat2) Matrix(None)   // dimenensiuni invalide
            else {

              // <=> `case Some(mat2) if (mat1.head.length != mat2.length)`
              val res = mat1.map(row1 =>
                mat2.transpose.map(col2 =>
                  row1.zip(col2).map { case (a, b) => a * b }.sum
                )
              )
              Matrix(res)
            }
          }

          case None => Matrix(None)

        }

      }

    }
  }

  /**
   * adaugarea unei coloane la sfarsitul matricii
   * coloana va contine o constanta (un scalar)
   */
  def ++(x: Double): Matrix = {
    m match {
      case None => Matrix(None)
      case Some(data) => {
        // `list :+ el` adauga o lista la finalul listei
        val newMat: List[List[Double]] = data.map(row => row :+ x)
        Matrix(Some(newMat))
      }
    }
  }

  /**
   * scade doua matrici (operatia se face element cu element)
   */
  def -(other: Matrix): Matrix = {
    m match {
      case None => Matrix(None)

      case Some(mat1) => {
        other.data match {

          case Some(mat2) => {

            val (height1: Int, width1: Int) = (mat1.length, mat1.head.length)
            val (height2: Int, width2: Int) = (mat2.length, mat2.head.length)

            if (height1 != height2 || width1 != width2) Matrix(None)  // dimensiune invalida
            else {

              val result = mat1.zip(mat2).map {case (row1, row2) =>
                row1.zip(row2).map { case (elem1, elem2) => elem1 - elem2
                }
              }

              Matrix(Some(result))
            }
          }

          case None => Matrix(None)
        }
      }
    }
  }



  def data: Option[Mat] = m

  def height: Option[Int] = {
    // metoda simpla: m.map(_.length)

    m match {
      case Some(matrix) => Some(matrix.length)
      case None => None
    }
  }

  def width: Option[Int] = {
    // metoda simpla: m.flatMap(_.headOption.map(_.length))

    m match {
      case Some(matrix) => Some(matrix.head.length)   // `.head` extrage primul element al unei liste
      case None => None
    }
  }

  override def toString: String = m match {
    case None => "Matrix(None)"
    case Some(mat) => mat.map(row => row.mkString(" ")).mkString("\n") 
  }


  /**
   *
   * @return  un tuplu ce contine
   *          inaltimea (numarul de linii)
   *          latimea (numarul de coloane)
   */
  def getDimensions(): (Int, Int) = (height.getOrElse(0), width.getOrElse(0))

  def getElementsSum(): Double = {

    @tailrec
    def sumOfLine(line: List[Double], sumAcc: Double): Double = {
      line match {
        case Nil => sumAcc
        case x :: xs => sumOfLine(xs, sumAcc + x)
      }
    }


    @tailrec
    def helper(matrix: List[List[Double]], totalSum: Double): Double = {
      matrix match
        case Nil => totalSum
        case x :: xs => helper(xs, totalSum + sumOfLine(x, 0.0))
    }


    m match {
      case None => 0.0
      case Some(matrix) => helper(matrix, 0.0)
    }

  }
  
}

object Matrix {
  def apply(data: Mat): Matrix = new Matrix(Some(data))

  def apply(data: Option[Mat]): Matrix = new Matrix(data)

  def apply(dataset: Dataset): Matrix = {
    val data = dataset.data.tail.map(row => row.map(el => el.toDouble)).toList
    Matrix(Some(data))
  }
}

