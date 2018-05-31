package pl.sag

trait ProductFetcher {
  def fetchProducts(amount: Int): Products = Products(List(Product("name", ProductId(12), "description")))
}
