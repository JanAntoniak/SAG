from flask import Flask

from sag import config
from logger import prepare_logger

sag = Flask(__name__)
sag.config.from_object(config.ProductionConfig)
prepare_logger(sag.logger.name)

logger = prepare_logger('backend')

from sag.request_manager.RequestManager import RequestManager

request_manager = RequestManager()

from sag import views
