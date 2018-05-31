package pl.sag

import org.mongodb.scala.bson.ObjectId

sealed trait Request

sealed trait Response

sealed trait Error extends Response

case class GetProductsRequest(product: ProductDTO, resultAmount: Int) extends Request

case object KillAndDie extends Request

case class LogicError(msg: String) extends Error


case class ProductDTO(productSimplifiedDescription: String)

final case class ProductsResponse(products: List[ProductResponse]) extends Response {
  def ++(other: ProductsResponse) = ProductsResponse(products ++ other.products)
}
final case class ProductResponse(_id: String, url: String)



object Product {
  def apply(name: String,
            simplifiedDescription: String,
            description: String,
            imageURL: String
           ): Product =
    new Product(
      new ObjectId(),
      name,
      simplifiedDescription,
      description,
      imageURL)
}
object Products {
  def apply(products: Seq[Product]): Products = new Products(products.toList)
}
case class Products(products: List[Product]) {
  def ++(other: Products) = Products(products ++ other.products)
}
case class Product(_id: ObjectId, name: String, simplified_description: String, description: String, image_url: String)




case class ProductsWithCosDist(products: List[ProductWithCosDist]) {
  def ++(other: ProductsWithCosDist) = ProductsWithCosDist(products ++ other.products)
}
case class ProductWithCosDist(_id: ObjectId, imageURL: String, cosineDistance: Double)