package pl.sag

import org.jsoup.Jsoup

import scala.io.Source

/**
 * This is example class fetching ebay product description for product with given link.
  * Because of specific way of information extraction from html source it can be used only
  * for fetching descriptions from Ebay service.
 */
class EbayFetcher {

  def fetchDescription(offerURL: String): ProductDescription = {
    val offerEbayURL = Source.fromURL(offerURL).mkString
    val linkToDescription = Jsoup.parse(offerEbayURL).getElementById("desc_ifr").attr("src")
    val offerDescriptionHTML = Source.fromURL(linkToDescription).mkString
    ProductDescription(Jsoup.parse(offerDescriptionHTML).text())
  }
}

case class ProductDescription(description: String)

object test {
  def main(args: Array[String]): Unit = {
    val fetcher = new EbayFetcher
    val url = "https://www.ebay.com/itm/Wireless-Bluetooth-Earphone-Headphones-Headset-Sports-Stereo-For-iPhone-Samsung/162646121926?hash=item25de76ddc6:m:m3r5AAYp_tH8FsJjChQ944w"
    println(fetcher.fetchDescription(url).description)
  }
}