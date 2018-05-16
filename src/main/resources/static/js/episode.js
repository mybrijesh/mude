$(function () {
    $("#trailer-info-box p").text(trimString($("#trailer-info-box p").text(), 150));
    $(".list").removeClass("active");
    $("#stat-tv").addClass("active");
    statTvShows();
});