$(function () {
    $(".season-item").hide();
    for (i = 0; i < 6; i++) {
        $(".season-item:eq(" + i + ")").addClass("active");
        $(".season-item:eq(" + i + ")").show();
    }
    $(".season-button").click(function () {
        showSeasons();
    });
});

function showSeasons() {
    if ($(".season-item").is(":hidden")) {
        $(".season-item").slideDown(500);
    } else {
        $(".season-item").not(".active").slideUp(500);
    }
}