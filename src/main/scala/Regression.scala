import scala.annotation.tailrec

object Regression {

  def regression(dataset_file: String,
                 attribute_columns: List[String],
                 value_column: String,
                 test_percentage: Double,
                 alpha: Double,
                 gradient_descent_steps: Int): (Matrix, Double) = {

    // 3.1 incarcarea si selectia datelor
    val dataset = Dataset(dataset_file)

    // musai split-ul inainte de selectia coloanelor
    val (train_set, eval_set) = dataset.split(test_percentage)


    val W: Matrix = calculate_regression_equation(
      train_set, attribute_columns, value_column,
      alpha, gradient_descent_steps
    )


    val err_val: Double = calculate_avg_err(W, eval_set, attribute_columns, value_column)
    (W, err_val)
  }


  /**
   * va returna `W` = coeficientii lui `X` din ecuatia dreptei de regresie
   */
  private def calculate_regression_equation(train_set: Dataset,
                                            attribute_columns: List[String], value_column: String,
                                            alpha: Double, gradient_descent_steps: Int): Matrix = {

    val train_X_set: Dataset = train_set.selectColumns(attribute_columns)
    val train_Y_set: Dataset = train_set.selectColumn(value_column)
    val X: Matrix = Matrix.apply(train_X_set) ++ 1
    val Y: Matrix = Matrix.apply(train_Y_set)

    // numarul de linii si numarul de coloane a matricii X
    val (m: Int, n: Int) = get_X_dimensions(X)


    // Initializam vectorul de parametri W cu 0
    val W_init = Matrix(List.fill(n)(List.fill(1)(0.0)))


    @tailrec
    def gradient_helper(W: Matrix, step: Int): Matrix = {
      if (step == 0) W
      else {
        val Y_estimat = X * W                                           // 3.4.a calculul estimarilor
        val err_vec = (Y_estimat - Y)                                   // 3.4.b calculul erororii
        val grad = (X.transpose * err_vec).map(el => el / m.toDouble)   // 3.4.c calculul gradientului
        val new_W = W - grad.map(el => el * alpha)                      // 3.4.d actualizarea ipotezei W (coef. lui X)

        gradient_helper(new_W, step - 1)
      }
    }

    val W = gradient_helper(W_init, gradient_descent_steps)
    W
  }


  /**
   * va returna eroarea medie pentru toate
   * diferentele in modul dintre valorile reale si cele estimate
   */
  private def calculate_avg_err(W: Matrix, eval_set: Dataset,
                                attribute_columns: List[String], value_column: String): Double = {

    val eval_X_set: Dataset = eval_set.selectColumns(attribute_columns)
    val eval_Y_set: Dataset = eval_set.selectColumn(value_column)
    val X: Matrix = Matrix.apply(eval_X_set) ++ 1
    val Y: Matrix = Matrix.apply(eval_Y_set)

    val Y_estimat: Matrix = X * W

    // numarul de linii si numarul de coloane a matricii X
    val (m: Int, n: Int) = get_X_dimensions(X)

    // diferentele in modul dintre valoarea reala si ce estimata
    val Y_abs_diff: Matrix = (Y_estimat - Y).map(el => el.abs)

    // valoarea medie a tuturor elementelor matricii
    val err: Double = Y_abs_diff.data.get.flatten.sum / m.toDouble
    err
  }


  /**
   *
   * @param X     matricea X
   * @return      un tuplu continand numarul de linii si numarul de coloane a lui X
   */
  private def get_X_dimensions(X: Matrix): (Int, Int) = {

    val (m: Int, n: Int) = X.getDimensions()

    if (m <= 1 || n <= 0) {
      throw new Exception("Dimensiunea invalida a matricilor.")
    }

    (m, n)
  }




  def main(args: Array[String]): Unit = {
    // Exemplu de utilizare
    println(regression("datasets/houseds.csv", List("GrLivArea", "YearBuilt"), "SalePrice", 0.1, 1e-7, 10000))
  }
}
