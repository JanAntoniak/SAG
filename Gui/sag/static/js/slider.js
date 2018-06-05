$(document).ready(function () {
    $('.slider').slider({
        formatter: function (value) {
            return 'Current value: ' + value;
        }
    });
});