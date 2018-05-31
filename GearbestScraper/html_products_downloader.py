import re
from multiprocessing.pool import Pool

import requests as requests


def generate_product_file(product_link):
    html = get_product_html(product_link)

    file_name = re.sub('[^a-zA-Z0-9 ]', '', product_link)
    with open(f"html/{file_name}", "w") as f:
        f.writelines(html)


def get_product_html(url):
    try:
        return requests.get(url).text
    except:
        return ''


if __name__ == "__main__":
    all_products_links = [line.rstrip('\n') for line in open('products_urls')]
    all_products_links = set(all_products_links)

    p = Pool(8)
    p.map(generate_product_file, all_products_links)
