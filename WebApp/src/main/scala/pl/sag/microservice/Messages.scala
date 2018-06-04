package pl.sag.microservice

import org.mongodb.scala.bson.ObjectId

sealed trait Request
sealed trait Response

case class GetProductsRequest(product: ProductDTO, resultAmount: Int) extends Request
case class ProductDTO(productSimplifiedDescription: String)

final case class DontUnderstand(obj: Any) extends Response
case class ProductsWithCosDist(products: List[ProductWithCosDist]) extends Response {
  def ++(other: ProductsWithCosDist) = ProductsWithCosDist(products ++ other.products)
}

case class ProductWithCosDist(_id: ObjectId, cosineDistance: Double)

object Product {
  def apply(name: String,
            simplifiedDescription: String,
            description: String,
            imageURL: String
           ): Product =
    new Product(
      new ObjectId(),
      simplifiedDescription)
}
object Products { def apply(products: Seq[Product]): Products = new Products(products.toList) }
case class Products(products: List[Product]) { def ++(other: Products) = Products(products ++ other.products) }
case class Product(_id: ObjectId, simplified_description: String)

final case class ProductsResponse(products: List[String]) extends Response {
  def ++(other: ProductsResponse) = ProductsResponse(products ++ other.products)
}