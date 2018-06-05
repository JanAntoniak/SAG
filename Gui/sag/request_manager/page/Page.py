class Page:
    def __init__(self, search_engine, title, url, description):
        self.search_engine = search_engine
        self.title = title
        self.url = url
        self.description = description
        self.__generate_short_url()

    def __hash__(self):
        return hash(self.url)

    def __eq__(self, other):
        return self.url == other.url

    def __repr__(self):
        return f"<{self.__class__.__name__}> {str(self.__dict__)}"

    def dumps(self):
        return self.__dict__

    def __generate_short_url(self):
        max_size = 100
        self.short_url = (self.url[:max_size] + '...') if len(self.url) > max_size else self.url
