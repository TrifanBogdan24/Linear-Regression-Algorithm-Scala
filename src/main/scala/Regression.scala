import scala.annotation.tailrec

object Regression {

  def regression(dataset_file: String,
                attribute_columns: List[String],
                value_column: String,
                test_percentage: Double,
                alpha: Double,
                gradient_descent_steps: Int): (Matrix, Double) = {

    // 3.1 Incarcarea si selectia datelor
    val ds = Dataset.apply(dataset_file)

    val datasetX = ds.selectColumns(attribute_columns)
    val datasetY = ds.selectColumn(value_column)

    val (trainDS_X, evalDS_X) = datasetX.split(test_percentage)
    val (trainDS_Y, evalDS_Y) = datasetY.split(test_percentage)

    // 3.2 Crearea matricii de antrenare X
    // le vom folosi pentru calculul vectorului `W`
    // coeficientii lui `x` ai dreptei
    val X_train: Matrix = Matrix.apply(trainDS_X)
    val Y_train: Matrix = Matrix.apply(trainDS_Y)

    val (m: Int, n: Int) = X_train.getDimensions()

    val X_train_mat: Matrix = X_train ++ 1

    // 3.3 Initializarea coeficientilor regresiei / ipotezei (W)
    val W_init: Matrix = Matrix(List.fill(n + 1)(List.fill(1)(0.0)))

    /**
     * 3.4 Gradient Descent
     */
    @tailrec
    def gradient_descent_helper(W_step: Matrix, step: Int) : Matrix = {
      if (step <= 0) W_step
      else {
        // TODO: cerinta 4
        // X -> matrice m * (n + 1)
        // W_step -> matrice (n + 1) * 1

        // Y_estimat -> matrice m * 1
        val Y_estimat: Matrix = X_train_mat * W_step

        // err -> matrice m * 1
        val err_vec: Matrix = (Y_estimat - Y_train).map(el => el.abs)

        // gradient -> matrice (n + 1) * 1
        val gradient: Matrix = (X_train_mat.transpose * err_vec).map(el => el / m.toDouble)

        val W_new: Matrix = W_step - gradient.map(el => el * alpha)

        gradient_descent_helper(W_new, step - 1)
      }
    }


    val W_final: Matrix = gradient_descent_helper(W_init, gradient_descent_steps)

    // 3.5 calculul errorii
    // vedem cat de bine trece dreapta prin punctele de evaluare

    // le vom folosi pentru calculul erorii
    val X_eval: Matrix = Matrix.apply(evalDS_X)
    val Y_eval: Matrix = Matrix.apply(evalDS_Y)

    val X_eval_mat: Matrix = X_eval ++ 1
    val Y_predicite: Matrix = X_eval_mat * W_final

     // media aritmetica intre modulul diferentei dintre pretul prezis si cel real
    val err_val: Double = sumAbsDiff(Y_predicite, Y_eval) / m.toDouble

    (W_final, err_val)
  }





  def sumAbsDiff(A_matrix: Matrix, B_matrix: Matrix): Double = {

    @tailrec
    def helper(A: List[List[Double]], B: List[List[Double]], sum: Double): Double = {
      (A, B) match {
        case (Nil, Nil) => sum

        case (lin1 :: linesA, lin2 :: linesB) => {
          val sumOfLines: Double = helper_for_line(lin1, lin2, 0.0)
          helper(linesA, linesB, sum + sumOfLines)
        }

        case _ => -1.0
      }
    }



    @tailrec
    def helper_for_line(lineA: List[Double], lineB: List[Double], sum: Double): Double = {
      (lineA, lineB) match {
        case (Nil, Nil) => sum
        case (a :: xa, b :: xb) => helper_for_line(xa, xb, sum + Math.abs(a - b))
        case _ => -1.0
      }
    }


    // conversia la o matrice de `Double`
    val A_mat: List[List[Double]] = A_matrix.data.getOrElse(List.empty)
    val B_mat: List[List[Double]] = B_matrix.data.getOrElse(List.empty)



    helper(A_mat, B_mat, 0.0)
  }

  def main(args: Array[String]): Unit = {
    // Exemplu de utilizare
    print(regression("datasets/houseds.csv", List("GrLivArea", "YearBuilt"), "SalePrice", 0.1, 1e-7, 10000))
  }
}