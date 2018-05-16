$(function () {
    $("#trailer-info-box p").text(trimString($("#trailer-info-box p").text(), 190));
    reviewTrigger();
    $(".list").removeClass("active");
    $("#stat-tv").addClass("active");
    statTvShows();
});