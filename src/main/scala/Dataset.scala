import scala.annotation.tailrec
import scala.io.Source    // ciitirea din fisier CSV

class Dataset(m: List[List[String]]) {
  val data: List[List[String]] = m
  override def toString: String = {
    val header = getHeader.mkString(",")
    val rows = getRows.map(_.mkString(",")).mkString("\n")
    s"$header\n$rows"
  }


  def selectColumn(col: String): Dataset =  {
    val columnIndex = getHeader.indexOf(col)
    if (columnIndex == -1) {
      throw new Exception(s"Column '$col' does not exist")
    } else {
      val selectedData = getRows.map(row => List(row(columnIndex)))
      new Dataset(selectedData)
    }
  }

  def selectColumns(cols: List[String]): Dataset = {
    val columnIndices = cols.map(col => getHeader.indexOf(col))
    if (columnIndices.exists(_ == -1)) {
      val invalidColumns = cols.filterNot(getHeader.contains)
      throw new Exception(s"Columns do not exist: ${invalidColumns.mkString(", ")}")
    } else {
      val selectedData = getRows.map(row => columnIndices.map(row))
      new Dataset(selectedData)
    }
  }


  def split(percentage: Double): (Dataset, Dataset) = {
    // TODO: split function nu trece testele
    if (percentage < 0 || percentage > 0.5) {
      throw new IllegalArgumentException("ce procent e asta?")
    }

    // sortam liniile matricii (mai putin prima)
    val sortedData: List[List[String]] = m.tail.sortBy(
      row => row.head      // crescator dupa prima coloana (strcmp)
    )



    val nrElementsSubSet: Int = (1.0.toDouble / percentage).ceil.toInt

    @tailrec
    def splitHelper(inputValues: List[List[String]],
               trainValues: List[List[String]],
               evalValues: List[List[String]],
               k: Int)
                : (List[List[String]], List[List[String]]) = {

      inputValues match {
        case Nil => (trainValues, evalValues)
        case x :: xs => {
          if (k == nrElementsSubSet)
            splitHelper(xs, trainValues, evalValues :+ x, 1)
          else
            splitHelper(xs, trainValues :+ x, evalValues, k + 1)
        }
      }

    }


    val (trainValues, evalValues) = splitHelper(sortedData, List(), List(), 1)
    val trainData = m.head :: trainValues
    val evalData = m.head :: evalValues

    (new Dataset(trainData), new Dataset(evalData))
  }

  def size: Int = m.length

  def getRows: List[List[String]] = m


  def getHeader: List[String] = m.headOption.getOrElse(Nil)
}

object Dataset {
  def apply(csvFilename: String): Dataset = {
    val source = Source.fromFile(csvFilename)
    try {
      val lines = source.getLines().toList
      val header = lines.head.split(",").toList
      val data = lines.tail.map(_.split(",").toList)
      new Dataset(data)
    } finally {
      source.close()
    }
  }
  def apply(ds: List[List[String]]): Dataset = {
    new Dataset(ds)
  }
}
