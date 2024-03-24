import Dataset._
import Regression._

import munit.FunSuite

class TestRegression extends FunSuite {

    val eps = 0.1
    val alpha = 0.001
    val split = 0.1

    /* 
    Testele verifica daca functia de regresie intoarce un vector de ponderi
    care sa aproximeze cat mai bine datele de intrare.

    Se verifica daca raportul dintre fiecare element al vectorului de ponderi
    si valoarea asteptata este aproape de 1, cu o marja de eroare de 0.1.

    Valorile numerice prezente in testele de mai jos sunt calculate cu ajutorul
    solutiei de referinta a temei.
    */
    test("tinyds.csv step=1") {
        val file = "datasets/tinyds.csv"
        val (w, loss) = Regression.regression(
            file, List("feature1"), "to_predict", split, alpha, 1)
        println(file + " step=1")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x " +
            "+ " + w.transpose.data.get(0)(1))
        val ok = w.data.get(0)
            .zip(List(23001.25, 100.875))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

    test("smallds_1.csv step=1") {
        val file = "datasets/smallds_1.csv"
        val (w, loss) = Regression.regression(
            file, List("feature1"), "to_predict", split, alpha, 1)
        println(file + " step=1")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x " +
            "+ " + w.transpose.data.get(0)(1))
        val ok = w.data.get(0)
            .zip(List(0.036445524811290334, -0.0420485742041271))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

    test("mediumds_1.csv step=1") {
        val file = "datasets/mediumds_1.csv"
        val (w, loss) = Regression.regression(
            file, List("feature1"), "to_predict", split, alpha, 1)
        println(file + " step=1")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x " +
            "+ " + w.transpose.data.get(0)(1))
        val ok = w.data.get(0)
            .zip(List(0.043976888634503636, -0.0035001802264028616))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

    test("bigds_1.csv step=1") {
        val file = "datasets/bigds_1.csv"
        val (w, loss) = Regression.regression(
            file, List("feature1"), "to_predict", split, alpha, 1)
        println(file + " step=1")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x " +
            "+ " + w.transpose.data.get(0)(1))
        val ok = w.data.get(0)
            .zip(List(0.0962221231877494, -0.0014708808601868574))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

    test("smallds_2.csv step=1") {
        val file = "datasets/smallds_2.csv"
        val (w, loss) = Regression.regression(
            file, List("feature1", "feature2"), "to_predict", split, alpha, 1)
        println(file + " step=1")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x1 " +
            "+ " + w.transpose.data.get(0)(1) + "x2 " +
            "+ " + w.transpose.data.get(0)(2))
        val ok = w.data.get(0)
            .zip(List(-0.007767069309725289, 0.044354738817640287, -0.007105983644519704))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

    test("bigds_2.csv step=1") {
        val file = "datasets/bigds_2.csv"
        val (w, loss) = Regression.regression(
            file, List("feature1", "feature2"), "to_predict", split, alpha, 1)
        println(file + " step=1")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x1 " +
            "+ " + w.transpose.data.get(0)(1) + "x2 " +
            "+ " + w.transpose.data.get(0)(2))
        val ok = w.data.get(0)
            .zip(List(0.022343737236188176, 0.06833183024313456, 0.0010098473867630171))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

    test("houseds.csv step=1 with GrLivArea and YearBuilt") {
        val file = "datasets/houseds.csv"
        val (w, loss) = Regression.regression(
            file, List("GrLivArea", "YearBuilt"), "SalePrice", split, alpha, 1)
        println(file + " step=1")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x1 " +
            "+ " + w.transpose.data.get(0)(1) + "x2 " +
            "+ " + w.transpose.data.get(0)(2))
        val ok = w.data.get(0)
            .zip(List(303741.1098753425, 357897.90681849315, 180.92119589041096))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

    test("houseds.csv step=1 with GrLivArea") {
        val file = "datasets/houseds.csv"
        val (w, loss) = Regression.regression(
            file, List("GrLivArea"), "SalePrice", split, alpha, 1)
        println(file + " step=1")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x1 " +
            "+ " + w.transpose.data.get(0)(1))
        val ok = w.data.get(0)
            .zip(List(303741.1098753425, 180.92119589041096))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

    test("tinyds.csv step=5") {
        val file = "datasets/tinyds.csv"
        val (w, loss) = Regression.regression(
            file, List("feature1"), "to_predict", split, alpha, 5)
        println(file + " step=5")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x " +
            "+ " + w.transpose.data.get(0)(1))
        val ok = w.data.get(0)
            .zip(List(9.248276076618884E10, 4.069050157501158E8))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

    test("smallds_1.csv step=5") {
        val file = "datasets/smallds_1.csv"
        val (w, loss) = Regression.regression(
            file, List("feature1"), "to_predict", split, alpha, 5)
        println(file + " step=5")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x " +
            "+ " + w.transpose.data.get(0)(1))
        val ok = w.data.get(0)
            .zip(List(0.18181900628912676, -0.20962075270330127))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

    test("mediumds_1.csv step=5") {
        val file = "datasets/mediumds_1.csv"
        val (w, loss) = Regression.regression(
            file, List("feature1"), "to_predict", split, alpha, 5)
        println(file + " step=5")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x " +
            "+ " + w.transpose.data.get(0)(1))
        val ok = w.data.get(0)
            .zip(List(0.21952134575434645, -0.017437203844587176))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

    test("bigds_1.csv step=5") {
        val file = "datasets/bigds_1.csv"
        val (w, loss) = Regression.regression(
            file, List("feature1"), "to_predict", split, alpha, 5)
        println(file + " step=5")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x " +
            "+ " + w.transpose.data.get(0)(1))
        val ok = w.data.get(0)
            .zip(List(0.48010741757111064, -0.007324563060477065))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

    test("smallds_2.csv step=5") {
        val file = "datasets/smallds_2.csv"
        val (w, loss) = Regression.regression(
            file, List("feature1", "feature2"), "to_predict", split, alpha, 5)
        println(file + " step=5")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x1 " +
            "+ " + w.transpose.data.get(0)(1) + "x2 " +
            "+ " + w.transpose.data.get(0)(2))
        val ok = w.data.get(0)
            .zip(List(-0.038660581672683304, 0.2212459245084685, -0.03549561678688793))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

    test("bigds_2.csv step=5") {
        val file = "datasets/bigds_2.csv"
        val (w, loss) = Regression.regression(
            file, List("feature1", "feature2"), "to_predict", split, alpha, 5)
        println(file + " step=5")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x1 " +
            "+ " + w.transpose.data.get(0)(1) + "x2 " +
            "+ " + w.transpose.data.get(0)(2))
        val ok = w.data.get(0)
            .zip(List(0.11139388190801659, 0.3408834511298815, 0.005029797194996237))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

    test("houseds.csv step=5 with GrLivArea and YearBuilt") {
        val file = "datasets/houseds.csv"
        val (w, loss) = Regression.regression(
            file, List("GrLivArea", "YearBuilt"), "SalePrice", split, alpha, 5)
        println(file + " step=5")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x1 " +
            "+ " + w.transpose.data.get(0)(1) + "x2 " +
            "+ " + w.transpose.data.get(0)(2))
        val ok = w.data.get(0)
            .zip(List(4.6038739702229965E20, 5.725326965599028E20, 2.9027957264979232E17))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

    test("houseds.csv step=5 with GrLivArea") {
        val file = "datasets/houseds.csv"
        val (w, loss) = Regression.regression(
            file, List("GrLivArea"), "SalePrice", split, alpha, 5)
        println(file + " step=5")
        println("\t" + "y = " 
            + w.transpose.data.get(0)(0) + "x1 " +
            "+ " + w.transpose.data.get(0)(1))
        val ok = w.data.get(0)
            .zip(List(1.3277894777983732E19, 7.821813476701869E15))
            .map((a, b) => a / b < 1 + eps && a / b > 1 - eps)
        assert(ok.reduce(_ && _))
    }

}