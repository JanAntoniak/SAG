package pl.sag

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val productsResponse = jsonFormat1(ProductsResponse)

  implicit val productDTO = jsonFormat1(ProductDTO)
  implicit val getProductsRequest = jsonFormat2(GetProductsRequest)
}
