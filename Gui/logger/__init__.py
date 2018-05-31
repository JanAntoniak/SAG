import logging

from autologging import TRACE


def prepare_logger(logger_name, level=TRACE):
    logger = logging.getLogger(logger_name)
    filename = f'logs/{logger_name}/{logger_name}.log'

    logger_format = "[%(asctime)s] <%(levelname)s> {%(filename)s:%(lineno)d - %(funcName)s()} %(message)s"
    formatter = logging.Formatter(logger_format)

    file_handler = logging.FileHandler(filename)
    file_handler.setFormatter(formatter)

    logger.setLevel(level)

    logger.handlers = list()
    logger.addHandler(file_handler)

    return logger
