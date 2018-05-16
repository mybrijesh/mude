$(function () {
    if ((document.referrer.indexOf("localhost") > -1)) {
        $("#load-page-text").hide(0);
        $("#load-page").modal('show');
        var d1 = $("#load-page-bounce .row").show(0).delay(2000).hide(0);
        $.when(d1).done(function () {
            $("#load-page").modal('hide');
        });
    }

    else {
        $("#load-page").modal('show');
        var d2 = $("#load-page-text").delay(1000).hide(0);
        var d3 = $("#load-page-bounce .row").delay(1000).show(0).delay(2000).hide(0);
        $.when(d2, d3).done(function () {
            $("#load-page").modal('hide');
        });
    }
});