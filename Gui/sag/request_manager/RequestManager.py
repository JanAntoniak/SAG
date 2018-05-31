from http import HTTPStatus

from autologging import traced

from sag import logger, config
from sag.product_manager import get_list_of_products


@traced(logger)
class RequestManager:
    def __init__(self):
        self.__config = config.RequestManagerConfig()

    def get_params(self, session_request):
        query = self.__get_query(session_request)
        page = self.__get_page(session_request)
        num = self.__get_num(session_request)
        return query, page, num

    @staticmethod
    def __get_query(session_request):
        return session_request.args.get('q')

    def __get_page(self, session_request):
        page = self.__get_number_from_param(session_request.args, 'p')
        return page or self.__config.PAGE_DEFAULT

    def __get_num(self, session_request):
        num = self.__get_number_from_param(session_request.args, 'n', self.__config.MAX_NUM)
        num_cookie = self.get_num_from_cookie_or_default(session_request.cookies)
        return num or num_cookie

    def get_num_from_cookie_or_default(self, cookies):
        num_cookie = self.__get_number_from_param(cookies, 'num', self.__config.MAX_NUM)
        return num_cookie or self.__config.DEFAULT_NUM

    @staticmethod
    def __get_number_from_param(params, param_name, max_value=None):
        param = params.get(param_name, '')
        param_integer = int(param) if param.isdigit() else -1
        max_value = max_value or param_integer

        return param_integer if 0 < param_integer <= max_value else None

    @staticmethod
    def get_results(query, page, num):
        results_mongo = get_list_of_products(query, page, num)

        if results_mongo:
            status = HTTPStatus.OK.value
        else:
            status = HTTPStatus.NOT_FOUND.value

        return dict(success=True, status=status, pages_list=results_mongo)
