from sag import sag, logger
from sag.gunicorn.GunicornServer import GunicornServer

if __name__ == "__main__":
    LOGO = r"""SAG"""
    logger.info(LOGO)

    gunicorn_app = GunicornServer(sag)
    gunicorn_app.run()
