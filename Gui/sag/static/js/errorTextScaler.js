$(function () {
    $(window).resize(function (e) {
        errorTextScaler();
    });
    errorTextScaler();

});


function errorTextScaler() {
    var containerSize = parseInt($(".error_container").height());
    var fontSize = (containerSize / 3) + 'px';
    $(".error_text p").css('font-size', fontSize);
}
