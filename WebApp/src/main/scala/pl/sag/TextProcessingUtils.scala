package pl.sag

import breeze.linalg.DenseVector
import breeze.linalg.functions.cosineDistance

trait TextProcessingUtils {

  def findMostSimilarProducts(products: Products, product: ProductDTO, amount: Int): Products =
    Products(
      products.products.map(product =>
        (product, countCosineDistance(product.productDescription, product.productDescription))
      ).sortBy(_._2)
        .take(amount)
        .map(_._1)
    )

  private def countCosineDistance(text1: String, text2: String): Double = {
    val (arr1, arr2) = createWordsIncidenceVector(text1, text2)
    cosineDistance(DenseVector(arr1), DenseVector(arr2))
  }

  private def createWordsIncidenceVector(text1: String, text2: String): (Array[Double], Array[Double]) = {
    def countWords(ws: Array[String]) =
      ws.toSet.map((word: String) => (word, ws.count(_ == word).toDouble)).toMap

    val allWords = s"$text1 $text2".split(" ").toSet.toArray

    def createArray(text: String) = allWords.map(countWords(text.split(" ")).getOrElse(_, 0.0))

    (createArray(text1), createArray(text2))
  }

}
