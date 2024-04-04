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
    val header = getHeader
    val columnIdx = header.indexOf(col)
    if (columnIdx != -1) {
      val newM = m.map(row => List(row(columnIdx)))
      new Dataset(newM)
    } else
      throw new Exception(s"Coloana $col nu exista in setul de date.")
  }

  def selectColumns(cols: List[String]): Dataset = {
    val header = getHeader

    val columnIndices = cols.map(col => header.indexOf(col))
    if (columnIndices.contains(-1)) {
      throw new Exception(s"Err: Una sau mai multe coloane specificate nu exista în setul de date.")
    } else {
      val newDS = m.map(row => columnIndices.map(index => row(index)))
      new Dataset(newDS)
    }
  }


  /**
   *
   * @param percentage    `1 / percentage`` = numarul de elemente dintr-un subset
   *                      la fiecare `1 / percentage` elemente
   *                      primele `1 / percentage - 1` se duc in setul de antrenare
   *                      ia utlimul, al `1 / percentage`-lea element se duce in setul de evaluare
   * @return              trainDataset, evalDataset
   */
  def split(percentage: Double): (Dataset, Dataset) = {
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

  /**
   * numarul de linii al matricii
   */
  def size: Int = m.length

  /**
   * extrage toate liniile, mai putin prima
   * va extrage valorile efective din Dataset
   * */
  def getRows: List[List[String]] = m.tail


  /**
   * extrage prima linie, denumirile coloanelor
   */
  def getHeader: List[String] = m.head
}

object Dataset {
  def apply(csvFilename: String): Dataset = {
    val source = Source.fromFile(csvFilename)
    try {
      val lines: List[String] = source.getLines().toList
      val header: List[String] = lines.head.split(",").toList
      val data: List[List[String]] = lines.tail.map(_.split(",").toList)
      new Dataset(header :: data)
    } finally {
      source.close()
    }
  }
  def apply(ds: List[List[String]]): Dataset = {
    new Dataset(ds)
  }
}
