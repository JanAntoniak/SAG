package pl.sag.microservice.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import pl.sag.microservice.{GetProductsRequest, ProductDTO, ProductsResponse}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val productsResponseFormatter: RootJsonFormat[ProductsResponse] = jsonFormat1(ProductsResponse)

  implicit val productDTOFormatter: RootJsonFormat[ProductDTO] = jsonFormat1(ProductDTO)
  implicit val getProductsRequestFormatter: RootJsonFormat[GetProductsRequest] = jsonFormat2(GetProductsRequest)
}
