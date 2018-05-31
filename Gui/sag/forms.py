from flask_wtf import FlaskForm
from wtforms import SubmitField, IntegerField
from wtforms.validators import DataRequired

from sag.config import RequestManagerConfig as Config


class Settings(FlaskForm):
    __SLIDER_LIST = list(range(Config.MIN_NUM, Config.MAX_NUM + 1, Config.MIN_NUM))

    num = IntegerField(
        label='Number of results per page', validators=[DataRequired()],
        render_kw={
            "data-slider-ticks": __SLIDER_LIST,
            "data-slider-ticks-labels": __SLIDER_LIST,
        }
    )
    save = SubmitField()
