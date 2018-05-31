package pl.sag

sealed trait Request
sealed trait Response
sealed trait Error extends Response

case class GetProductsRequest(product: ProductDTO, resultAmount: Int) extends Request
case object KillAndDie extends Request

case class ProductDTO(productDescription: String)
case class Products(products: List[Product]) extends Response {
  def ++(other: Products) = Products(products ++ other.products)
}
case class ProductId(id: Long) extends AnyVal
case class Product(productName: String, productId: ProductId, productDescription: String)

case class LogicError(msg: String) extends Error
