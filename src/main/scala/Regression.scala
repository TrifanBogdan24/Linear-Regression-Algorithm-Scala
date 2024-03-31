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
    val X_eval_expended = fillX(X_eval, W_final)
    val Y_real_eval: Matrix = Matrix(evalY)


    val Y_predictie: Matrix = X_eval_expended * W_final

    val error_value: Double = Regression.sumAbsDiff(Y_real_eval, Y_predictie)


    (W_final, error_value)
  }



  def fillX(X: Matrix, W: Matrix): Matrix = {
    val XData = X.data.getOrElse(List.empty)
    val WData = W.data.getOrElse(List.empty)

    val l: Int = XData.length
    val c: Int = X.width.getOrElse(0)
    val m: Int = WData.length

    if (c >= m) {
      X
    } else {
      // umplem coloanele din dreapta ca sa puten inmulti X cu W
      val filledColumns = List.fill(l)(List.fill(m - c)(0.0))
      val newData = XData.zip(filledColumns).map { case (rowX, rowZero) => rowX ++ rowZero }
      Matrix(Some(newData))
    }
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