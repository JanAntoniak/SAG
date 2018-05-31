from http import HTTPStatus

from bson.errors import BSONError
from flask import render_template, request, redirect, url_for, flash
from werkzeug.exceptions import NotFound

from sag import sag, request_manager, forms, logger
from sag.product_manager import get_product, get_offers


@sag.after_request
def after_request(response):
    request_info = " ".join([request.remote_addr, request.method, request.scheme, request.full_path, response.status])
    sag.logger.info(request_info)
    return response


@sag.errorhandler(Exception)
def exceptions(error):
    request_info = " ".join([request.remote_addr, request.method, request.scheme, request.full_path])
    logger.error(f"{request_info} INTERNAL SERVER ERROR", exc_info=True)
    sag.logger.error(f"{request_info} INTERNAL SERVER ERROR", exc_info=True)

    return error_response(request.path, HTTPStatus.INTERNAL_SERVER_ERROR)


@sag.errorhandler(HTTPStatus.NOT_FOUND)
def page_not_found(error):
    sag.logger.error(f"Page not found: {request.full_path}")
    return render_template('error.html', error=HTTPStatus.NOT_FOUND)


@sag.errorhandler(HTTPStatus.BAD_REQUEST)
def bad_request(error):
    sag.logger.error(f"Bad request: {request.full_path}")
    return error_response(request.path, HTTPStatus.BAD_REQUEST)


def error_response(path, error):
    return render_template('error.html', error=error)


def is_api_request(path):
    first_part_of_url_path = path.split('/', 1)[1]
    return first_part_of_url_path == 'api'


@sag.route("/")
def index():
    return render_template('index.html')


@sag.route("/search", methods=['GET', 'POST'])
def search():
    query, page, num = request_manager.get_params(request)
    if query:
        results = request_manager.get_results(query, page, num)
        return render_template('search.html', results=results, page=page, query=query)
    else:
        return redirect(url_for('index'))


@sag.route("/settings", methods=['GET', 'POST'])
def settings():
    num = request_manager.get_num_from_cookie_or_default(request.cookies)

    form = forms.Settings()
    response = render_template('settings.html', form=form, num=num)

    if form.validate_on_submit():
        response = sag.make_response(redirect(url_for('settings')))
        response.set_cookie('num', value=str(form.num.data))

        flash('Parameters have been set.', 'success')

    return response


@sag.route("/product/<product_id>", methods=['GET', 'POST'])
def product(product_id):
    try:
        product = get_product(product_id)
        offers = get_offers(product)
        return render_template('product.html', product=product, offers=offers)
    except BSONError:
        raise NotFound()


def url_for_other_page(page):
    args = request.args.copy()
    args['p'] = page
    return url_for(request.endpoint, **args)


sag.jinja_env.globals['url_for_other_page'] = url_for_other_page
