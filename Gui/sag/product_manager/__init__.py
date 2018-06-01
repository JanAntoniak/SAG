import re

from bson import ObjectId
from pymongo import MongoClient
import requests
import json


def get_product(product_id):
    collection = __get_products_collection()
    return collection.find_one({"_id": ObjectId(product_id)})


def get_list_of_products(query, page, num):
    collection = __get_products_collection()

    regex = re.compile(rf".*{query}.*", re.IGNORECASE)
    cursor = collection.find({"name": regex}).skip((page - 1) * num).limit(num)
    return list(cursor)


def prepare_request_data(simplified_description):
    payload = {"product": {"productSimplifiedDescription": simplified_description}, "resultAmount": 5}
    return json.dumps(payload)


def get_offers(product):
    data = prepare_request_data(product['simplified_description'])
    url = "http://localhost:8080"
    headers = {'content-type': 'application/json'}
    result = requests.post(url, data, headers=headers)
    products_ids = result.json()['products']
    return list(map(get_product, products_ids))


def __get_products_collection():
    connection = MongoClient()
    db = connection.database
    return db.products
