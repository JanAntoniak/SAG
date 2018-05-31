import re

from bson import ObjectId
from pymongo import MongoClient


def get_product(product_id):
    collection = __get_products_collection()
    return collection.find_one({"_id": ObjectId(product_id)})


def get_list_of_products(query, page, num):
    collection = __get_products_collection()

    regex = re.compile(rf".*{query}.*", re.IGNORECASE)
    cursor = collection.find({"name": regex}).skip((page - 1) * num).limit(num)
    return list(cursor)


def get_offers(product):
    return [product, product, product, product, product]


def __get_products_collection():
    connection = MongoClient()
    db = connection.database
    return db.products
