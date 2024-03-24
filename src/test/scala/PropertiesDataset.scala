import org.scalacheck.Prop.forAll
import org.scalacheck.Properties
import org.scalacheck.Gen
import org.scalacheck.Arbitrary
import org.scalacheck.Test

import scala.collection.immutable.ListMap
import Dataset._

import java.io.FileWriter
import scala.util.Random

object PropertiesDataset {

  class DatasetGenProperty(msg: String) extends Properties(msg) {
    val genDataset: Gen[Dataset] = for {
      n <- Gen.choose(2, 100)
      m <- Gen.choose(2, 100)
      data <- Gen.listOfN(n + 1, Gen.listOfN(m, Gen.double).map(_.map(_.toString)))
      // ^^ first line has column names :)
    } yield Dataset.apply(data)

    implicit val arbitraryDataset: Arbitrary[Dataset] = Arbitrary(genDataset)
  }

  object PropertiesReadCSV extends DatasetGenProperty("ReadCSV") {

    /* Aceasta proprietate citeste datseturile existente din folderul datasets.
      * Pentru fiecare dataset, se verifica daca:
      * - datasetul citit contine cel putin o virgula si un newline
      * - datasetul citit este acelasi cu cel citit din fisier
      *     -> verificarea se face prin scrierea datasetului citit
      *        intr-un fisier temporar si recitirea lui
      * - headerul datasetului citit este acelasi cu cel citit din fisier
      * - numarul de linii citite este acelasi cu cel citit din fisier
      * NOTA: Acesta nu e cod functional - NU faceti asta in tema :P */
    property("readCSV") = {
      var ok = true
      val files = new java.io.File("datasets").listFiles.filter(_.isFile).map(_.getPath)
      for f <- files do {
        val ds = Dataset.apply(f)
        val csvDS = ds.toString
        if !csvDS.contains(",") || !csvDS.contains("\n") then
          ok = false
        else {
          val fileName = "TEMPORARY.csv"
          val fw = new FileWriter(fileName)
          fw.write(csvDS)
          fw.close()
          val ds2 = Dataset.apply(fileName)
          val file = new java.io.File(fileName)
          file.delete()
          if ds2.toString != csvDS || ds.getHeader != ds2.getHeader || ds2.size != ds.size then
            ok = false
        }
      }
      ok
    }
  }

  object PropertiesSelectColumns extends DatasetGenProperty("SelectColumns") {

    /* Aceasta proprietate testeaza metoda selectColumn.
      * Se alege o coloana aleator din dataset.
      * Se creeaza un dataset nou cu acea coloana.
      * Se verifica daca selectColumn returneaza acelasi
      * dataset ca si selectColumns, cand aceasta din urma e
      * apelata cu o lista ce contine doar acea coloana.
     */
    property("selectColumn") = forAll { (ds: Dataset) =>
      val header = ds.getHeader
      val column = header(Random.nextInt(header.length))

      ds.selectColumn(column).getHeader == List(column) &&
        ds.selectColumn(column).data.zip(
          ds.selectColumns(List(column)).data).map((a, b) => a.sameElements(b)
        ).reduce(_ && _)
    }

    /* Aceasta proprietate testeaza metodele selectColumn si selectColumns.
      * Se alege un numar n aleator intre 1 si jumatatea dinnumarul de coloane din dataset.
      * Se alege cate o coloana la n pasi, incepand de la prima coloana.
      * Se creeaza un dataset nou cu acele coloane.
      * Se verifica daca datasetul nou are aceleasi coloane ca si datasetul initial,
      * comparandu-le reprezentarile sub forma de string. */
    property("selectColumns") = forAll { (ds: Dataset) =>
      val n = Random.nextInt(ds.data.head.length / 2) + 1
      val refCols = ds.data.map(_.zipWithIndex.collect { case (col, i) if i % n == 0 => col })
      val colNames = refCols.head
      val ref = Dataset(refCols).toString
      val rez = ds.selectColumns(colNames).toString
      ref == rez
    }
  }

  object PropertiesSplit extends DatasetGenProperty("Split") {

    /* Aceasta proprietate testeaza metoda split.
      * Se alege un factor aleator nenul intre 0 si 1.
      * Se imparte datasetul in doua dataseturi, ds1 si ds2, cu factorul ales.
      * Se verifica daca ds1 si ds2 au toate proprietatile urmatoare:
      * - suma dimensiunilor ds1 si ds2 este egala cu dimensiunea datasetului initial
      * - dimensiunea ds2 este egala cu dimensiunea datasetului initial inmultita cu factorul ales
      * - ds1 si ds2 contin linii care se regasesc in datasetul initial
      * - concatenand liniile din ds1 si ds2, se obtine datasetul initial, ordonat dupa prima coloana
      * - ds1 si ds2 au acelasi header ca si datasetul initial
     */
    property("split") = forAll { (ds: Dataset) =>
      var factor = 0.0
      while factor == 0 do factor = Random.nextDouble() / 2
      val (ds1, ds2) = ds.split(factor)
      ds1.size + ds2.size == ds.size && ds2.size == (ds.size * factor).toInt &&
        ds1.data.forall(row => ds.data.exists(_.sameElements(row))) &&
        ds2.data.forall(row => ds.data.exists(_.sameElements(row))) &&
        ds1.getHeader == ds.getHeader && ds2.getHeader == ds.getHeader &&
        ds1.data.tail ++ ds2.data.tail == ds.data.tail.sortBy(_.head) // tail ca sa scapam de numele coloanelor
    }
  }

  def runProperties(properties: Properties): Boolean = {
    var ok = true
    properties.properties.foreach { case (name, prop) =>
      println(s"Running property: $name")
      val result = Test.check(Test.Parameters.default, prop)
      println(result.status)
      ok &&= result.passed
    }
    ok
  }

  def main(args: Array[String]): Unit = {
    val points = List(
      if runProperties(PropertiesReadCSV) then 15 else 0,
      if runProperties(PropertiesSelectColumns) then 15 else 0,
      if runProperties(PropertiesSplit) then 10 else 0
    )
    print("Punctaj: " + points.sum + " / 40")
  }


}