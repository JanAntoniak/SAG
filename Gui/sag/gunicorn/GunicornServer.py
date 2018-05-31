from gunicorn.app.base import Application


class GunicornServer(Application):
    def __init__(self, app):
        self.application = app
        super().__init__()

    def init(self, parser, opts, args):
        return {
            'bind': self.application.config['GUNICORN_BIND'],
            'workers': self.application.config['GUNICORN_WORKERS'],
            'errorlog': self.application.config['GUNICORN_LOG_FILE']
        }

    def load(self):
        return self.application
