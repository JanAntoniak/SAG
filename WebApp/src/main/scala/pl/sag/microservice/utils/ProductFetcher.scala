package pl.sag.microservice.utils

import _root_.pl.sag.microservice.{Product, Products}
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ProductFetcher {
  def fetchProducts(amount: Int): Future[Products]
}

class ProductFetcherImpl(dbName: String) extends ProductFetcher with Logger {

  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Product]), DEFAULT_CODEC_REGISTRY )
  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase(dbName).withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Product] = database.getCollection("products")

  def fetchProducts(amount: Int): Future[Products]= {
    collection.aggregate(List(sample(amount)))
      .toFuture
      .map(Products.apply)
      .recoverWith {
        case e: Throwable => error(e.toString)
                             Future.failed(e) }
  }
}

