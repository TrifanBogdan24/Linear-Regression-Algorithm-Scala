import scala.annotation.tailrec

object Regression {

  def regression(dataset_file: String,
                attribute_columns: List[String],
                value_column: String,
                test_percentage: Double,
                alpha: Double,
                gradient_descent_steps: Int): (Matrix, Double) = {

    // 3.1 Incarcarea si selectia datelor
    val ds: Dataset = Dataset.apply(dataset_file)

    val selectedX: Dataset = ds.selectColumns(attribute_columns)
    val selectedY: Dataset = ds.selectColumn(value_column)

    val (trainX, evalX) = selectedX.split(test_percentage)
    val (trainY, evalY) = selectedY.split(test_percentage)


    // 3.2 Crearea matricii de antrenare X
    val matriceValoriAntrenare: Matrix = Matrix.apply(trainX)

    val (m: Int, n: Int) = matriceValoriAntrenare.getDimensions()

    val X: Matrix = matriceValoriAntrenare ++ 1    // m x (n + 1)





    // 3.3 Initializarea coeficientilor regresiei / ipotezei (W)
    val W_init: Matrix = Matrix(List.fill(n + 1)(List.fill(1)(0.0)))

    // matrice m * 1
    val Y_real_train: Matrix = Matrix(trainY)


    /**
     * 3.4 Gradient Descent
     */
    @tailrec
    def gradient_heleper(W_step: Matrix, current_step: Int): Matrix = {
      if (current_step <= 0) W_step
      else {
        // TODO: cerunta 4
        // X -> matrice m * (n + 1)
        // W_step -> matrice (n + 1) * 1

        // Y_estimat -> matrice m * 1
        val Y_estimat_train: Matrix = X * W_step


        // err -> matrice m * 1
        val err_vector: Matrix = Y_real_train - Y_estimat_train



        // gradient -> matrice (n + 1) * 1
        val gradient: Matrix = (X.transpose * err_vector).map(el => el / m.toDouble)

        val W_new: Matrix = W_step - gradient.map(el => el * alpha)

        gradient_heleper(W_new, current_step - 1)
      }

    }


    val W_final: Matrix = gradient_heleper(W_init, gradient_descent_steps)

    // 3.5 calculul errorii
    // vedem cat de bine trece dreapta prin punctele de evaluare
    val X_eval: Matrix = Matrix(evalX)
    val Y_real_eval: Matrix = Matrix(evalY)
    val Y_predictie: Matrix = X_eval * W_final

    val error_value: Double = {
      Regression.sumAbsDiff(Y_real_eval, Y_predictie) match {
        case None => 0
        case Some(err_val) => err_val
      }
    }


    (W_final, error_value)
  }




  def sumAbsDiff(Y_real: Matrix, Y_estimat: Matrix): Option[Double] = {
    (Y_real.data, Y_estimat.data) match {
      case (Some(data1), Some(data2)) if data1.size == data2.size && data1.headOption.exists(_.size == data2.headOption.fold(false)(_.size)) =>
        val differences = data1.zip(data2).flatMap { case (row1, row2) =>
          row1.zip(row2).map { case (elem1, elem2) =>
            Math.abs(elem1 - elem2)
          }
        }
        Some(differences.reduce(_ + _))
      case _ => None
    }
  }

  def main(args: Array[String]): Unit = {
    // Exemplu de utilizare
    print(regression("datasets/houseds.csv", List("GrLivArea", "YearBuilt"), "SalePrice", 0.1, 1e-7, 10000))
  }
}