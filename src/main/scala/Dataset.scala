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
  @tailrec
  private def splitHelper(remaining: List[List[String]], targetSize: Int, accTrain: List[List[String]], accTest: List[List[String]]): (Dataset, Dataset) = {
    remaining match {
      case Nil => (Dataset(accTrain.reverse), Dataset(accTest.reverse))
      case head :: tail =>
        if (accTest.length < targetSize) {
          splitHelper(tail, targetSize, accTrain, head :: accTest)
        } else {
          splitHelper(tail, targetSize, head :: accTrain, accTest)
        }
    }
  }

  def split(percentage: Double): (Dataset, Dataset) = {
    require(percentage > 0 && percentage <= 0.5, "Percentage must be between 0 and 0.5")

    val sortedData = m.sortBy(_.head) // Sortăm după prima coloană
    val dataSize = sortedData.length
    val testSize = (dataSize * percentage).toInt
    splitHelper(sortedData, testSize, Nil, Nil)
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
      val header = lines.head.split(";,").toList
      val data = lines.tail.map(_.split(";,").toList)
      new Dataset(data)
    } finally {
      source.close()
    }
  }
  def apply(ds: List[List[String]]): Dataset = {
    new Dataset(ds)
  }
}
