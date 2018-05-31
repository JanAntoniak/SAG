package pl.sag

import breeze.linalg.DenseVector
import breeze.linalg.functions.cosineDistance

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait TextProcessingUtils {

  def findMostSimilarProducts(futureProducts: Future[Products], product: ProductDTO, amount: Int): Future[ProductsWithCosDist] =
    futureProducts.map { products =>
      ProductsWithCosDist(
        products.products.map(p => {
          val cosDistVal = countCosineDistance(p.simplified_description, product.productSimplifiedDescription)
          (ProductWithCosDist(p._id, p.image_url, cosDistVal), cosDistVal)
        }).sortBy(_._2)
          .take(amount)
          .map(_._1)
      )
    }


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
