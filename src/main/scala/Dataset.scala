class Dataset(m: List[List[String]]) {
  val data: List[List[String]] = ???
  override def toString: String = ???

  def selectColumn(col: String): Dataset = ???
  def selectColumns(cols: List[String]): Dataset = ???
  def split(percentage: Double): (Dataset, Dataset) = ???

  def size: Int = ???
  def getRows: List[List[String]] = ???
  def getHeader: List[String] = ???
}

object Dataset {
  def apply(csv_filename: String): Dataset = ???
  def apply(ds: List[List[String]]): Dataset = ???
}
