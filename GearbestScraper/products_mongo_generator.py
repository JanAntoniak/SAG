import os
import re
from multiprocessing.pool import Pool

import nltk
from bs4 import BeautifulSoup
from nltk.corpus import stopwords
from nltk.stem import PorterStemmer
from nltk.tokenize import word_tokenize
from pymongo import MongoClient

nltk.download('punkt')
nltk.download('stopwords')


def generate_product_file(filename):
    html = get_product_html(filename)

    product_description = get_product_description(html)
    product_name = get_product_name(html)
    product_image_url = get_product_image_url(html)

    if product_description and product_name and product_image_url:
        product = dict(
            name=product_name,
            description=product_description,
            simplified_description=get_simplified_description(product_description),
            image_url=product_image_url
        )

        connection = MongoClient()
        db = connection.database
        collection = db.products
        collection.insert_one(product)


def get_product_html(filename):
    result = ""
    try:
        with open(f'html/{filename}', 'r') as f:
            result = f.read()
    except:
        pass
    return BeautifulSoup(result, "html.parser")


def get_product_name(html):
    title = html.find("h1", "goodsIntro_title")
    return title.get_text().strip() if title else ""


def get_product_description(html):
    desc = html.find("div", "product_pz_info mainfeatures")
    return desc.get_text().strip() if desc else ""


def get_product_image_url(html):
    image = html.find("img", "goodsIntro_largeImg")
    return image.get("src", default="") if image else ""


def get_simplified_description(description):
    ps = PorterStemmer()
    words = word_tokenize(description)

    filtered_words = [word for word in words if word not in stopwords.words('english') and re.search('[a-zA-Z]', word)]

    return " ".join([ps.stem(word) for word in filtered_words])


if __name__ == "__main__":
    file_names = os.listdir('html')
    p = Pool(8)
    p.map(generate_product_file, file_names)
