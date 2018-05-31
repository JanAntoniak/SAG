class BaseConfig:
    DEBUG = False
    WTF_CSRF_ENABLED = True
    SECRET_KEY = 'BASE'

    GUNICORN_BIND = '0.0.0.0:8000'
    GUNICORN_WORKERS = 4
    GUNICORN_LOG_FILE = 'logs/gunicorn.log'


class DevelopmentConfig(BaseConfig):
    DEBUG = True
    SECRET_KEY = 'DEVELOP'
    GUNICORN_WORKERS = 1


class ProductionConfig(BaseConfig):
    SECRET_KEY = 'PRODUCTION'


class RequestManagerConfig:
    DEFAULT_NUM = 10
    MIN_NUM = 5
    MAX_NUM = 30
    PAGE_DEFAULT = 1
