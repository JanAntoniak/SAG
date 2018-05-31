import logging
from threading import Thread

import requests as requests
from bs4 import BeautifulSoup

log_path = "products_urls"
logger = logging.getLogger('log')
logger.setLevel(logging.INFO)
ch = logging.FileHandler(log_path)
ch.setFormatter(logging.Formatter('%(message)s'))
logger.addHandler(ch)


def generate_all_products_links():
    categories_links = get_links(request_url="https://www.gearbest.com/", a_class="headerCate_itemTitle")

    for category_link in categories_links[:6]:
        generate_category_products_links(category_link)


def generate_category_products_links(url, pages=50, page_size=120):
    for page in range(1, pages + 1):
        thread = Thread(target=generate_page_category_products_links, args=(page, page_size, url))
        thread.start()


def generate_page_category_products_links(page, page_size, url):
    request_params = dict(page=page, page_size=page_size, odr="high2low")
    product_page_links = get_links(url, a_class="gbGoodsItem_title", request_params=request_params)

    if product_page_links:
        logger.info('\n'.join(product_page_links))


def get_links(request_url, a_class, request_params=None):
    request = requests.get(request_url, request_params)
    soup = BeautifulSoup(request.text, "html.parser")
    a_tags = soup.find_all("a", a_class, href=True)
    return [a.get('href') for a in a_tags]


if __name__ == "__main__":
    generate_all_products_links()
