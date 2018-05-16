$(function () {
    if ($(".cReviewBox").length)
        updateCriticReview();
    if ($(".aReviewBox").length)    
        updateAudienceReview();
    if ($(".lp-row-box-container").length)
        statOverview();
    if ($("#review-box-big").length)    
        myReviewAndWTS();
    $('.list').click(function () {
        $(this).addClass('active').siblings().removeClass('active');
        $(this).removeClass('bat').siblings().addClass('bat');
    });

    $('.details').owlCarousel({
        margin: 10,
        autoWidth: false,
        nav: true,
        items: 4,
        responsive: {
            0: {
                items: 1
            },
            500: {
                items: 2
            },
            1000: {
                items: 4
            }
        }
    });

    $('.art-highest').owlCarousel({
        margin: 40,
        autoWidth: false,
        nav: true,
        items: 4,
        responsive: {
            0: {
                items: 1
            },
            500: {
                items: 2
            },
            1000: {
                items: 4
            }
        }
    });

    $("#stat-overview").click(function () {
        statOverview();
    });
    $("#stat-movie").click(function () {
        statMovies();
    }); 
    $("#stat-tv").click(function () {
        statTvShows();
    });

    $("#top-logo").attr("href", "/");

    if ($("#consensus-big span").length)
        $("#consensus-big span").text(trimString($("#consensus-big span").text(), 70));
    if ($("#consensus-small span").length)
        $("#consensus-small span").text(trimString($("#consensus-small span").text(), 70));

    if ($(".first-name:eq(0)").text() === "No-User") {
        $(".review-box-big").css("opacity", "0.3");
        $(".review-box-small").css("opacity", "0.3");
        $(".review-cover").show();
    }

    if ($(".wts-button").length) {
        $(".wts-button").click(function () {
            wantToSee();
        });
    }
    if ($(".ni-button").length) {
        $(".ni-button").click(function () {
            notInterested();
        });
    }

    var $imgSrc;
    $('.img-btn').click(function () {
        $imgSrc = $(this).data("src");
    });
    $('#photoModal').on('shown.bs.modal', function (e) {
        $('.container-fluid').addClass('light-off');
        $("#image").attr('src', $imgSrc);
    });
    $('#photoModal').on('hide.bs.modal', function (e) {
        $('.container-fluid').removeClass('light-off');
        $("#image").attr('src', $imgSrc);
    }); 
});

function showEpisodes() {
    if ($("#episode-box").is(":hidden")){
        $("#episode-box").slideDown(300);
    } else {
        $("#episode-box").slideUp(300);
    }
}

function reviewTrigger() {
    $(".add-box").click(function () {
        if ($("#review-slide").is(":hidden")) {
            $("#review-slide").slideDown(300);
        } else {
            $("#review-slide").slideUp(300);
        }
    });

    $(window).on('resize', function () {
        if (window.innerWidth > 768)
            $("#review-slide").hide();
    });

    $("#post-button").click(function () {
        postReview();
    });

    $("#edit-button").click(function () {
        editReview();
    });

    $("#delete-button").click(function () {
        deleteReview();
    });

    $("#post-button-small").click(function () {
        // $("#post-button-small").addClass("disabled");
        // $("#post-button-small").attr("aria-disabled", "true");
        $(".review-box-small").css("opacity", "0.3");
        var p2 = $("#load-page-bounce-review-small").show(0).delay(2000).hide(0);
        $.when(p2).done(function () {
            $(".review-box-small").css("opacity", "1");
            var ps2 = $("#post-button-small-alert").show(0).delay(5000).hide(0);
            $.when(ps2).done(function () {
                $("#post-button-small").removeClass("disabled");
                $("#post-button-small").attr("aria-disabled", "false");
            });
        });
        return false;
    });

}

function postReview() {
    var mediaID = getParameterByName("mediaID");
    var score;
    var comment;
    score = $(".starRadio:checked").val();
    comment = $("#review-comment").val();
    if (!score) {
        $("#failModalCenter").modal("show");
        return;
    }
    if (!comment || comment == "Add a Review") {
        $("#failModalCenter").modal("show");
        return;
    }
    // $("#post-button").addClass("disabled");
    // $("#post-button").attr("aria-disabled", "true");
    $(".review-box-big").css("opacity", "0.3");
    $("#load-page-bounce-review").show();
    $.ajax({
        url: "/postreview",
        type: "POST",
        data: "mediaID=" + mediaID + "&score=" + score + "&comment=" + comment,
        complete: function (xhr, status) {
            $("#load-page-bounce-review").hide();
            if (xhr.responseText) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    $(".review-box-big").css("opacity", "1");
                    $("#post-button-alert p").text("POSTED!!");
                    var ps1 = $("#post-button-alert").show(0).delay(5000).hide(0);
                    $.when(ps1).done(function () {
                        $("#post-button").removeClass("disabled");
                        $("#post-button").attr("aria-disabled", "false");
                    });
                    //Enable Edit + Delete
                    myReviewAndWTS();
                    //Update userMudeMeter
                    if (response.ratings) {
                        if (response.ratings.audienceMudemeter != null && response.ratings.audienceMudemeter != 0)
                            $(".a-mudemeter").text(Math.round(response.ratings.audienceMudemeter) + "%");
                        if (response.ratings.audienceAvgScore != null && response.ratings.audienceAvgScore != 0)
                            $(".a-score").text(Math.round(response.ratings.audienceAvgScore * 10) / 10.0 + "  /  5");
                        if (response.ratings.criticMudemeter != null && response.ratings.criticMudemeter != 0)
                            $(".c-mudemeter").text(Math.round(response.ratings.criticMudemeter) + "%");
                        if (response.ratings.criticAvgScore != null && response.ratings.criticAvgScore != 0)
                            $(".c-score").text(Math.round(response.ratings.criticAvgScore * 10) / 10.0 + "  /  5");
                    }
                    updateCriticReview();
                    updateAudienceReview();
                    return true;
                }
            }
            $("#post-button").removeClass("disabled");
            $("#post-button").attr("aria-disabled", "false");
            $("#failModalCenter").modal("show");
            return false;
        }
    });
}

function wantToSee() {
    var mediaID = getParameterByName("mediaID");
    var isWantToSee = true; 

    if ($(".wts-button").hasClass("active"))
        return false;

    $.ajax({
        url: "/wanttosee",
        type: "POST",
        data: "mediaID=" + mediaID + "&isWantToSee=" + isWantToSee,
        complete: function (xhr, status) {
            if (xhr.responseText) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    if (!$(".wts-button").hasClass("active")) {
                        $(".wts-button").addClass("active");
                        $(".ni-button").removeClass("active");
                    }
                    $("#successModalCenter").modal("show");    
                    return true;
                }
            }
            $("#failModalCenter").modal("show");
            return false;
        }
    });
}

function notInterested() {
    var mediaID = getParameterByName("mediaID");
    var isWantToSee = false;

    if ($(".ni-button").hasClass("active"))
        return false;

    $.ajax({
        url: "/wanttosee",
        type: "POST",
        data: "mediaID=" + mediaID + "&isWantToSee=" + isWantToSee,
        complete: function (xhr, status) {
            if (xhr.responseText) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    if (!$(".ni-button").hasClass("active")) {
                        $(".wts-button").removeClass("active");
                        $(".ni-button").addClass("active");
                    }
                    $("#successModalCenter").modal("show");
                    return true;
                }
            }
            $("#failModalCenter").modal("show");
            return false;
        }
    });
}

function updateCriticReview() {
    var mediaID = getParameterByName("mediaID");
    $.ajax({
        url: "/refeshcriticreviews",
        type: "GET",
        data: "mediaID=" + mediaID,
        complete: function (xhr, status) {
            if (xhr.responseText) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    var count = 0;
                    $(".crb-badge").hide();
                    for (var review in response.criticReviews) {
                        var score = response.criticReviews[review].score;
                        var whole = Math.trunc(score);
                        var half = (score * 10) % 10;
                        if (count === 0) {
                            $(".cReviewBox").show();
                            $(".cReviewBox").not(".cReviewBox:eq(0)").remove();
                            $(".cReviewBox:eq(0)").find("img").attr("src","http://23.94.27.164/resource/members/"+response.criticReviews[review].member.id+"/photos/0.jpg");
                            $(".cReviewBox:eq(0)").find("img").attr("onerror", "this.onerror=null; this.src='images/critics.gif';");
                            $(".cReviewBox:eq(0)").find(".crb-name").attr("href", "/member?memberID=" + response.criticReviews[review].member.id);
                            $(".cReviewBox:eq(0)").find("img").attr("style","width:80px;height:80px;");

                            if(response.criticReviews[review].reported){
                                $(".cReviewBox:eq(0)").find(".report-button").html("reported");
                                $(".cReviewBox:eq(0)").find(".report-button").attr("onclick","");
                                $(".cReviewBox:eq(0)").find(".report-button").attr("style","color:#6c757d !important;");
                            }
                            else{
                                $(".cReviewBox:eq(0)").find(".report-button").html("report");
                                $(".cReviewBox:eq(0)").find(".report-button").attr("onclick","reportReview("+response.criticReviews[review].id+");");
                                $(".cReviewBox:eq(0)").find(".report-button").attr("style","#ff8a80 !important");
                            }

                            $(".cReviewBox:eq(0)").find(".crb-name").text(response.criticReviews[review].member.name.firstName + " " + response.criticReviews[review].member.name.lastName);
                            $(".cReviewBox:eq(0)").find(".crb-reg").text(response.criticReviews[review].regDate.split("T")[0]);
                            $(".cReviewBox:eq(0)").find(".crb-comment").text(response.criticReviews[review].comment);
                            $(".cReviewBox:eq(0)").find(".crb-comment").text(trimString($(".cReviewBox:eq(0)").find(".crb-comment").text(), 170));
                            $(".cReviewBox:eq(0)").find(".crb-star").not($(".cReviewBox:eq(0)").find(".crb-star:eq(0)")).remove();
                            if (response.criticReviews[review].member.topCritic)
                                $(".cReviewBox:eq(0)").find(".crb-badge").show();
                            for (i = 0; i < whole - 1; i++) {
                                $(".cReviewBox:eq(0)").find(".crb-star").show();
                                $(".cReviewBox:eq(0)").find(".crb-score").addClass("ml-1");
                                $(".cReviewBox:eq(0)").find(".crb-star:eq(0)").clone().appendTo($(".cReviewBox:eq(0)").find(".crb-star-box")).each(function () {
                                    $(this).show();
                                });
                            }
                            if (whole == 0) {
                                $(".cReviewBox:eq(0)").find(".crb-star").hide();
                                $(".cReviewBox:eq(0)").find(".crb-score").removeClass("ml-1");
                            }
                            if (whole == 1) {
                                $(".cReviewBox:eq(0)").find(".crb-star").show();
                            }
                            if (half != 0) {
                                $(".cReviewBox:eq(0)").find(".crb-score").text(whole + ".5  /  5");
                                $(".cReviewBox:eq(0)").find(".crb-star-half").show();
                                $(".cReviewBox:eq(0)").find(".crb-star-half:eq(0)").clone().appendTo($(".cReviewBox:eq(0)").find(".crb-star-box"));
                                $(".cReviewBox:eq(0)").find(".crb-star-half:eq(0)").remove();
                            }
                            else {
                                $(".cReviewBox:eq(0)").find(".crb-star-half").hide();
                                $(".cReviewBox:eq(0)").find(".crb-score").text(whole + "  /  5");
                            }
                        }
                        else {
                            $(".cReviewBox:eq(0)").clone().appendTo(".cReviewBox-div").each(function () {
                                $(this).find("img").attr("src","http://23.94.27.164/resource/members/"+response.criticReviews[review].member.id+"/photos/0.jpg");
                                $(this).find("img").attr("onerror", "this.onerror=null; this.src='images/critics.gif';");
                                $(this).find("img").attr("style","width:80px;height:80px;");
                                $(this).find(".crb-name").attr("href", "/member?memberID=" + response.criticReviews[review].member.id);

                                if(response.criticReviews[review].reported){
                                    $(this).find(".report-button").html("reported");
                                    $(this).find(".report-button").attr("onclick","");
                                    $(this).find(".report-button").attr("style","color:#6c757d !important;");
                                }
                                else{
                                    $(this).find(".report-button").html("report");
                                    $(this).find(".report-button").attr("onclick","reportReview("+response.criticReviews[review].id+");");
                                    $(this).find(".report-button").attr("style","#ff8a80 !important");
                                }


                                $(this).find(".crb-name").text(response.criticReviews[review].member.name.firstName + " " + response.criticReviews[review].member.name.lastName);
                                $(this).find(".crb-reg").text(response.criticReviews[review].regDate.split("T")[0]);
                                $(this).find(".crb-comment").text(response.criticReviews[review].comment);
                                $(this).find(".crb-comment").text(trimString($(this).find(".crb-comment").text(), 170));
                                $(this).find(".crb-star").not($(this).find(".crb-star:eq(0)")).remove();
                                if (response.criticReviews[review].member.topCritic)
                                    $(this).find(".crb-badge").show();
                                for (i = 0; i < whole - 1; i++) {
                                    $(this).find(".crb-star").show();
                                    $(this).find(".crb-score").addClass("ml-1");
                                    $(this).find(".crb-star:eq(0)").clone().appendTo($(this).find(".crb-star-box")).each(function () {
                                        $(this).show();
                                    });
                                }
                                if (whole == 0) {
                                    $(this).find(".crb-star").hide();
                                    $(this).find(".crb-score").removeClass("ml-1");
                                }
                                if (whole == 1) {
                                    $(this).find(".crb-star").show();
                                }
                                if (half != 0) {
                                    $(this).find(".crb-score").text(whole + ".5  /  5");
                                    $(this).find(".crb-star-half").show();
                                    $(this).find(".crb-star-half:eq(0)").clone().appendTo($(this).find(".crb-star-box"));
                                    $(this).find(".crb-star-half:eq(0)").remove();
                                }
                                else {
                                    $(this).find(".crb-star-half").hide();
                                    $(this).find(".crb-score").text(whole + "  /  5");
                                }
                            });
                        }
                        count++;
                        if (count == 6) {
                            $(".view-all-c").show();
                            break;
                        }
                    }
                    if (count === 0) {
                        var str = "<h4 class='mx-auto pt-5 pb-3'>Not Yet Available!</h4>";
                        var html = $.parseHTML(str);
                        $(".view-all-c").hide();
                        $(".cReviewBox:eq(0)").hide();
                        $(".cReviewBox-div").children().hide();
                        $(".cReviewBox-div").append(html);
                        return false;
                    }
                    return true;
                }
            }
            $("#failModalCenter").modal("show");
            return false;
        }
    });
}

function updateAudienceReview() {
    var mediaID = getParameterByName("mediaID");
    $.ajax({
        url: "/refeshaudiencereviews",
        type: "GET",
        data: "mediaID=" + mediaID,
        complete: function (xhr, status) {
            if (xhr.responseText) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    var count = 0;
                    for (var review in response.audienceReviews) {
                        var score = response.audienceReviews[review].score;
                        var whole = Math.trunc(score);
                        var half = (score * 10) % 10;
                        if (count === 0) {
                            $(".aReviewBox-div h4").hide();
                            $(".aReviewBox").show();
                            $(".aReviewBox").not(".aReviewBox:eq(0)").remove();
                            $(".aReviewBox:eq(0)").find("img").attr("src","http://23.94.27.164/resource/members/"+response.audienceReviews[review].member.id+"/photos/0.jpg");
                            $(".aReviewBox:eq(0)").find("img").attr("onerror", "this.onerror=null; this.src='images/critics.gif';");
                            $(".aReviewBox:eq(0)").find("img").attr("style","width:80px;height:80px;");

                            if(response.audienceReviews[review].reported){
                                $(".aReviewBox:eq(0)").find(".report-button").html("reported");
                                $(".aReviewBox:eq(0)").find(".report-button").attr("onclick","");
                                $(".aReviewBox:eq(0)").find(".report-button").attr("style","color:#6c757d !important;");
                            }
                            else{
                                $(".aReviewBox:eq(0)").find(".report-button").html("report");
                                $(".aReviewBox:eq(0)").find(".report-button").attr("onclick","reportReview("+response.audienceReviews[review].id+");");
                                $(".aReviewBox :eq(0)").find(".report-button").attr("style","#ff8a80 !important");
                            }


                            $(".aReviewBox:eq(0)").find(".arb-name").attr("href", "/member?memberID=" + response.audienceReviews[review].member.id);
                            $(".aReviewBox:eq(0)").find(".arb-name").text(response.audienceReviews[review].member.name.firstName + " " + response.audienceReviews[review].member.name.lastName);
                            $(".aReviewBox:eq(0)").find(".arb-reg").text(response.audienceReviews[review].regDate.split("T")[0]);
                            $(".aReviewBox:eq(0)").find(".arb-comment").text(response.audienceReviews[review].comment);
                            $(".aReviewBox:eq(0)").find(".arb-comment").text(trimString($(".aReviewBox:eq(0)").find(".arb-comment").text(), 170));
                            $(".aReviewBox:eq(0)").find(".arb-star").not($(".aReviewBox:eq(0)").find(".arb-star:eq(0)")).remove();
                            for (i = 0; i < whole - 1; i++) {
                                $(".aReviewBox:eq(0)").find(".arb-star").show();
                                $(".aReviewBox:eq(0)").find(".arb-score").addClass("ml-1");
                                $(".aReviewBox:eq(0)").find(".arb-star:eq(0)").clone().appendTo($(".aReviewBox:eq(0)").find(".arb-star-box")).each(function () {
                                    $(this).show();
                                });
                            }
                            if (whole == 0) {
                                $(".aReviewBox:eq(0)").find(".arb-star").hide();
                                $(".aReviewBox:eq(0)").find(".arb-score").removeClass("ml-1");
                            }
                            if (whole == 1) {
                                $(".aReviewBox:eq(0)").find(".arb-star").show();
                            }
                            if (half != 0) {
                                $(".aReviewBox:eq(0)").find(".arb-score").text(whole + ".5  /  5");
                                $(".aReviewBox:eq(0)").find(".arb-star-half").show();
                                $(".aReviewBox:eq(0)").find(".arb-star-half:eq(0)").clone().appendTo($(".aReviewBox:eq(0)").find(".arb-star-box"));
                                $(".aReviewBox:eq(0)").find(".arb-star-half:eq(0)").remove();
                            }
                            else {
                                $(".aReviewBox:eq(0)").find(".arb-star-half").hide();
                                $(".aReviewBox:eq(0)").find(".arb-score").text(whole + "  /  5");
                            }
                        }
                        else {
                            $(".aReviewBox:eq(0)").clone().appendTo(".aReviewBox-div").each(function () {
                                $(this).find("img").attr("src","http://23.94.27.164/resource/members/"+response.audienceReviews[review].member.id+"/photos/0.jpg");
                                $(this).find("img").attr("onerror", "this.onerror=null; this.src='images/critics.gif';");
                                $(this).find("img").attr("style","width:80px;height:80px;");

                                if(response.audienceReviews[review].reported){
                                    $(this).find(".report-button").html("reported");
                                    $(this).find(".report-button").attr("onclick","");
                                    $(this).find(".report-button").attr("style","color:#6c757d !important;");
                                }
//
                                else{
                                    $(this).find(".report-button").html("report");
                                    $(this).find(".report-button").attr("onclick","reportReview("+response.audienceReviews[review].id+");");
                                    $(this).find(".report-button").attr("style","#ff8a80 !important");
                                }

                                $(this).find(".arb-name").attr("href", "/member?memberID=" + response.audienceReviews[review].member.id);
                                $(this).find(".arb-name").text(response.audienceReviews[review].member.name.firstName + " " + response.audienceReviews[review].member.name.lastName);
                                $(this).find(".arb-reg").text(response.audienceReviews[review].regDate.split("T")[0]);
                                $(this).find(".arb-comment").text(response.audienceReviews[review].comment);
                                $(this).find(".arb-comment").text(trimString($(this).find(".arb-comment").text(), 170));
                                $(this).find(".arb-star").not($(this).find(".arb-star:eq(0)")).remove();
                                for (i = 0; i < whole - 1; i++) {
                                    $(this).find(".arb-star").show();
                                    $(this).find(".arb-score").addClass("ml-1");
                                    $(this).find(".arb-star:eq(0)").clone().appendTo($(this).find(".arb-star-box")).each(function () {
                                        $(this).show();
                                    });
                                }
                                if (whole == 0) {
                                    $(this).find(".arb-star").hide();
                                    $(this).find(".arb-score").removeClass("ml-1");
                                }
                                if (whole == 1) {
                                    $(this).find(".arb-star").show();
                                }
                                if (half != 0) {
                                    $(this).find(".arb-score").text(whole + ".5  /  5");
                                    $(this).find(".arb-star-half").show();
                                    $(this).find(".arb-star-half:eq(0)").clone().appendTo($(this).find(".arb-star-box"));
                                    $(this).find(".arb-star-half:eq(0)").remove();
                                }
                                else {
                                    $(this).find(".arb-star-half").hide();
                                    $(this).find(".arb-score").text(whole + "  /  5");
                                }
                            });
                        }
                        count++;
                        if (count == 6) {
                            $(".view-all-a").show();
                            break;
                        }
                    }
                    if (count === 0) {
                        var str = "<h4 class='mx-auto'>Not Yet Available!</h4>";
                        var html = $.parseHTML(str);
                        $(".view-all-a").hide();
                        $(".aReviewBox:eq(0)").hide();
                        $(".aReviewBox-div").children().hide();
                        $(".aReviewBox-div").append(html);
                        return false;
                    }
                    return true;
                }
            }
            $("#failModalCenter").modal("show");
            return false;
        }
    });
}


function reportReview(reviewID){
    console.log("Reporting Review: "+reviewID);
    $.ajax({
        url: "/reportreview",
        type: "POST",
        data: "reviewID="+reviewID,
        complete: function(data){
            var response = JSON.parse(data.responseText);
            if(response.success){
                $("#successModalCenter").modal("show");
                updateCriticReview();
                updateAudienceReview();
            }
            else{
                $("#failModalCenter").modal("show");
            }
//            console.log(response);
        }
    });
}

function statOverview() {
    $("#load-page-bounce-stat").css("height", "93%");
    $("#load-page-bounce-stat").show();
    $.ajax({
        url: "/statoverview",
        type: 'GET',
        complete: function (xhr, status) {
            if (xhr.responseText) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    $(".lp-row-box-container").show();
                    if (response.comingSoon) {
                        $(".lp-header:eq(0) b").text("COMING SOON");
                        $(".lp-row").not(".lp-row:eq(0)").remove();
                        var count = 0;
                        var cs = response.comingSoon;
                        for (var index in cs) {
                            if (count == 0) {
                                if (cs[index].mudemeter.criticMudemeter >= 70 && cs[index].mudemeter.criticMudemeter <= 100) {
                                    $(".lp-row:eq(0)").find(".lp-mude").text("M");
                                    $(".lp-row:eq(0)").find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row:eq(0)").find(".lp-mude").addClass("gold");
                                }
                                if (cs[index].mudemeter.criticMudemeter >= 60 && cs[index].mudemeter.criticMudemeter < 70) {
                                    $(".lp-row:eq(0)").find(".lp-mude").text("M");
                                    $(".lp-row:eq(0)").find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row:eq(0)").find(".lp-mude").addClass("silver");
                                }
                                if (cs[index].mudemeter.criticMudemeter >= 50 && cs[index].mudemeter.criticMudemeter < 60) {
                                    $(".lp-row:eq(0)").find(".lp-mude").text("M");
                                    $(".lp-row:eq(0)").find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row:eq(0)").find(".lp-mude").addClass("bronze");
                                }
                                if (cs[index].mudemeter.criticMudemeter > 0 && cs[index].mudemeter.criticMudemeter < 50) {
                                    $(".lp-row:eq(0)").find(".lp-mude").text("M");
                                    $(".lp-row:eq(0)").find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row:eq(0)").find(".lp-mude").addClass("black");
                                }
                                if (cs[index].mudemeter.criticMudemeter == 0) {
                                    $(".lp-row:eq(0)").find(".lp-mude").text("—");
                                    $(".lp-row:eq(0)").find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row:eq(0)").find(".lp-mude").addClass("not-rated");
                                }
                                if (cs[index].mudemeter.criticMudemeter != 0) 
                                    $(".lp-row:eq(0)").find(".lp-mudemeter").text(Math.round(cs[index].mudemeter.criticMudemeter) + "%");
                                else
                                    $(".lp-row:eq(0)").find(".lp-mudemeter").text("");
                                $(".lp-row:eq(0)").find(".lp-medianame").text(cs[index].mediaName);
                                $(".lp-row:eq(0)").find(".lp-releasedate").text(dateToMonthDay(cs[index].releaseDate.theatre));
                                $(".lp-row:eq(0)").attr("href", "/movie?mediaID=" + cs[index].id);
                            }
                            else {
                                $(".lp-row:eq(0)").clone().appendTo(".lp-row-box").each(function () {
                                    if (cs[index].mudemeter.criticMudemeter >= 70 && cs[index].mudemeter.criticMudemeter <= 100) {
                                        $(this).find(".lp-mude").text("M");
                                        $(this).find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude").addClass("gold");
                                    }
                                    if (cs[index].mudemeter.criticMudemeter >= 60 && cs[index].mudemeter.criticMudemeter < 70) {
                                        $(this).find(".lp-mude").text("M");
                                        $(this).find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude").addClass("silver");
                                    }
                                    if (cs[index].mudemeter.criticMudemeter >= 50 && cs[index].mudemeter.criticMudemeter < 60) {
                                        $(this).find(".lp-mude").text("M");
                                        $(this).find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude").addClass("bronze");
                                    }
                                    if (cs[index].mudemeter.criticMudemeter > 0 && cs[index].mudemeter.criticMudemeter < 50) {
                                        $(this).find(".lp-mude").text("M");
                                        $(this).find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude").addClass("black");
                                    }
                                    if (cs[index].mudemeter.criticMudemeter == 0) {
                                        $(this).find(".lp-mude").text("—");
                                        $(this).find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude").addClass("not-rated");
                                    }
                                    if (cs[index].mudemeter.criticMudemeter !== 0)
                                        $(this).find(".lp-mudemeter").text(Math.round(cs[index].mudemeter.criticMudemeter) + "%");
                                    else    
                                        $(this).find(".lp-mudemeter").text("");
                                    $(this).find(".lp-medianame").text(cs[index].mediaName);
                                    $(this).find(".lp-releasedate").text(dateToMonthDay(cs[index].releaseDate.theatre));
                                    $(this).attr("href", "/movie?mediaID=" + cs[index].id);
                                });
                            }
                            count++;
                            if (count == 5)
                                break;
                        }
                    }
                    if (response.openThisWeek) {
                        $(".lp-header-otw:eq(0) b").text("OPENING THIS WEEK");
                        $(".lp-row-otw").not(".lp-row-otw:eq(0)").remove();
                        var count2 = 0;
                        var otw = response.openThisWeek;
                        for (var index2 in otw) {
                            if (count2 == 0) {
                                if (otw[index2].mudemeter.criticMudemeter >= 70 && otw[index2].mudemeter.criticMudemeter <= 100) {
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").text("M");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").addClass("gold");
                                }
                                if (otw[index2].mudemeter.criticMudemeter >= 60 && otw[index2].mudemeter.criticMudemeter < 70) {
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").text("M");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").addClass("silver");
                                }
                                if (otw[index2].mudemeter.criticMudemeter >= 50 && otw[index2].mudemeter.criticMudemeter < 60) {
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").text("M");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").addClass("bronze");
                                }
                                if (otw[index2].mudemeter.criticMudemeter > 0 && otw[index2].mudemeter.criticMudemeter < 50) {
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").text("M");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").addClass("black");
                                }
                                if (otw[index2].mudemeter.criticMudemeter == 0) {
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").text("—");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").addClass("not-rated");
                                }
                                if (otw[index2].mudemeter.criticMudemeter != 0)
                                    $(".lp-row-otw:eq(0)").find(".lp-mudemeter-otw").text(Math.round(otw[index2].mudemeter.criticMudemeter) + "%");
                                else
                                    $(".lp-row-otw:eq(0)").find(".lp-mudemeter-otw").text("");
                                $(".lp-row-otw:eq(0)").find(".lp-medianame-otw").text(otw[index2].mediaName);
                                $(".lp-row-otw:eq(0)").find(".lp-releasedate-otw").text(dateToMonthDay(otw[index2].releaseDate.theatre));
                                $(".lp-row-otw:eq(0)").attr("href", "/movie?mediaID=" + otw[index2].id);
                            }
                            else {
                                $(".lp-row-otw:eq(0)").clone().appendTo(".lp-row-box-otw").each(function () {
                                    if (otw[index2].mudemeter.criticMudemeter >= 70 && otw[index2].mudemeter.criticMudemeter <= 100) {
                                        $(this).find(".lp-mude-otw").text("M");
                                        $(this).find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-otw").addClass("gold");
                                    }
                                    if (otw[index2].mudemeter.criticMudemeter >= 60 && otw[index2].mudemeter.criticMudemeter < 70) {
                                        $(this).find(".lp-mude-otw").text("M");
                                        $(this).find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-otw").addClass("silver");
                                    }
                                    if (otw[index2].mudemeter.criticMudemeter >= 50 && otw[index2].mudemeter.criticMudemeter < 60) {
                                        $(this).find(".lp-mude-otw").text("M");
                                        $(this).find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-otw").addClass("bronze");
                                    }
                                    if (otw[index2].mudemeter.criticMudemeter > 0 && otw[index2].mudemeter.criticMudemeter < 50) {
                                        $(this).find(".lp-mude-otw").text("M");
                                        $(this).find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-otw").addClass("black");
                                    }
                                    if (otw[index2].mudemeter.criticMudemeter == 0) {
                                        $(this).find(".lp-mude-otw").text("—");
                                        $(this).find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-otw").addClass("not-rated");
                                    }
                                    if (otw[index2].mudemeter.criticMudemeter !== 0)
                                        $(this).find(".lp-mudemeter-otw").text(Math.round(otw[index2].mudemeter.criticMudemeter) + "%");
                                    else
                                        $(this).find(".lp-mudemeter-otw").text("");
                                    $(this).find(".lp-medianame-otw").text(otw[index2].mediaName);
                                    $(this).find(".lp-releasedate-otw").text(dateToMonthDay(otw[index2].releaseDate.theatre));
                                    $(this).attr("href", "/movie?mediaID=" + otw[index2].id);
                                });
                            }
                            count2++;
                            if (count2 == 5)
                                break;
                        }
                    }
                    if (response.topBoxOffice) {
                        $(".lp-header-tbo:eq(0) b").text("TOP BOX OFFICE");
                        $(".lp-row-tbo").not(".lp-row-tbo:eq(0)").remove();
                        var count3 = 0;
                        var tbo = response.topBoxOffice;
                        for (var index3 in tbo) {
                            if (count3 == 0) {
                                $("#load-page-bounce-stat").css("height", "87%");
                                if (tbo[index3].mudemeter.criticMudemeter >= 70 && tbo[index3].mudemeter.criticMudemeter <= 100) {
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").text("M");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").addClass("gold");
                                }
                                if (tbo[index3].mudemeter.criticMudemeter >= 60 && tbo[index3].mudemeter.criticMudemeter < 70) {
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").text("M");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").addClass("silver");
                                }
                                if (tbo[index3].mudemeter.criticMudemeter >= 50 && tbo[index3].mudemeter.criticMudemeter < 60) {
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").text("M");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").addClass("bronze");
                                }
                                if (tbo[index3].mudemeter.criticMudemeter > 0 && tbo[index3].mudemeter.criticMudemeter < 50) {
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").text("M");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").addClass("black");
                                }
                                if (tbo[index3].mudemeter.criticMudemeter == 0) {
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").text("—");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").addClass("not-rated");
                                }
                                if (tbo[index3].mudemeter.criticMudemeter != 0)
                                    $(".lp-row-tbo:eq(0)").find(".lp-mudemeter-tbo").text(Math.round(tbo[index3].mudemeter.criticMudemeter) + "%");
                                else
                                    $(".lp-row-tbo:eq(0)").find(".lp-mudemeter-tbo").text("");
                                $(".lp-row-tbo:eq(0)").find(".lp-medianame-tbo").text(tbo[index3].mediaName);
                                $(".lp-row-tbo:eq(0)").find(".lp-releasedate-tbo").text(dateToMonthDay(tbo[index3].releaseDate.theatre));
                                $(".lp-row-tbo:eq(0)").attr("href", "/movie?mediaID=" + tbo[index3].id);
                            }
                            else {
                                if (count3 == 1) {
                                    $("#load-page-bounce-stat").css("height", "93%");
                                }
                                $(".lp-row-tbo:eq(0)").clone().appendTo(".lp-row-box-tbo").each(function () {
                                    if (tbo[index3].mudemeter.criticMudemeter > 70 && tbo[index3].mudemeter.criticMudemeter <= 100) {
                                        $(this).find(".lp-mude-tbo").text("M");
                                        $(this).find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-tbo").addClass("gold");
                                    }
                                    if (tbo[index3].mudemeter.criticMudemeter >= 60 && tbo[index3].mudemeter.criticMudemeter < 70) {
                                        $(this).find(".lp-mude-tbo").text("M");
                                        $(this).find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-tbo").addClass("silver");
                                    }
                                    if (tbo[index3].mudemeter.criticMudemeter >= 50 && tbo[index3].mudemeter.criticMudemeter < 60) {
                                        $(this).find(".lp-mude-tbo").text("M");
                                        $(this).find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-tbo").addClass("bronze");
                                    }
                                    if (tbo[index3].mudemeter.criticMudemeter > 0 && tbo[index3].mudemeter.criticMudemeter < 50) {
                                        $(this).find(".lp-mude-tbo").text("M");
                                        $(this).find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-tbo").addClass("black");
                                    }
                                    if (tbo[index3].mudemeter.criticMudemeter == 0) {
                                        $(this).find(".lp-mude-tbo").text("—");
                                        $(this).find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-tbo").addClass("not-rated");
                                    }
                                    if (tbo[index3].mudemeter.criticMudemeter !== 0)
                                        $(this).find(".lp-mudemeter-tbo").text(Math.round(tbo[index3].mudemeter.criticMudemeter) + "%");
                                    else
                                        $(this).find(".lp-mudemeter-tbo").text("");
                                    $(this).find(".lp-medianame-tbo").text(tbo[index3].mediaName);
                                    $(this).find(".lp-releasedate-tbo").text(dateToMonthDay(tbo[index3].releaseDate.theatre));
                                    $(this).attr("href", "/movie?mediaID=" + tbo[index3].id);
                                });
                            }
                            count3++;
                            if (count3 == 5)
                                break;
                        }
                    }
                    $("#load-page-bounce-stat").fadeOut();
                    return true;
                }
            }
            $("#load-page-bounce-stat").fadeOut();
            return false;
        }
    });
}

function statMovies() {
    $("#load-page-bounce-stat").show();
    $.ajax({
        url: "/statmovies",
        type: 'GET',
        complete: function (xhr, status) {
            if (xhr.responseText) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    $(".lp-row-box-container").hide();
                    if (response.topMovie) {
                        $("#load-page-bounce-stat").css("height", "90%");
                        $(".lp-header-otw:eq(0) b").text("TOP MOVIE");
                        $(".lp-row-otw").not(".lp-row-otw:eq(0)").remove();
                        var count2 = 0;
                        var otw = response.topMovie;
                        for (var index2 in otw) {
                            if (count2 == 0) {
                                if (otw[index2].mudemeter.criticMudemeter >= 70 && otw[index2].mudemeter.criticMudemeter <= 100) {
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").text("M");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").addClass("gold");
                                }
                                if (otw[index2].mudemeter.criticMudemeter >= 60 && otw[index2].mudemeter.criticMudemeter < 70) {
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").text("M");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").addClass("silver");
                                }
                                if (otw[index2].mudemeter.criticMudemeter >= 50 && otw[index2].mudemeter.criticMudemeter < 60) {
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").text("M");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").addClass("bronze");
                                }
                                if (otw[index2].mudemeter.criticMudemeter > 0 && otw[index2].mudemeter.criticMudemeter < 50) {
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").text("M");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").addClass("black");
                                }
                                if (otw[index2].mudemeter.criticMudemeter == 0) {
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").text("—");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").addClass("not-rated");
                                }
                                if (otw[index2].mudemeter.criticMudemeter != 0)
                                    $(".lp-row-otw:eq(0)").find(".lp-mudemeter-otw").text(Math.round(otw[index2].mudemeter.criticMudemeter) + "%");
                                else
                                    $(".lp-row-otw:eq(0)").find(".lp-mudemeter-otw").text("");
                                $(".lp-row-otw:eq(0)").find(".lp-medianame-otw").text(otw[index2].mediaName);
                                $(".lp-row-otw:eq(0)").find(".lp-releasedate-otw").text(dateToMonthDay(otw[index2].releaseDate.theatre));
                                $(".lp-row-otw:eq(0)").attr("href", "/movie?mediaID=" + otw[index2].id);
                            }
                            else {
                                $(".lp-row-otw:eq(0)").clone().appendTo(".lp-row-box-otw").each(function () {
                                    if (otw[index2].mudemeter.criticMudemeter >= 70 && otw[index2].mudemeter.criticMudemeter < 100) {
                                        $(this).find(".lp-mude-otw").text("M");
                                        $(this).find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-otw").addClass("gold");
                                    }
                                    if (otw[index2].mudemeter.criticMudemeter >= 60 && otw[index2].mudemeter.criticMudemeter < 70) {
                                        $(this).find(".lp-mude-otw").text("M");
                                        $(this).find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-otw").addClass("silver");
                                    }
                                    if (otw[index2].mudemeter.criticMudemeter >= 50 && otw[index2].mudemeter.criticMudemeter < 60) {
                                        $(this).find(".lp-mude-otw").text("M");
                                        $(this).find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-otw").addClass("bronze");
                                    }
                                    if (otw[index2].mudemeter.criticMudemeter > 0 && otw[index2].mudemeter.criticMudemeter < 50) {
                                        $(this).find(".lp-mude-otw").text("M");
                                        $(this).find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-otw").addClass("black");
                                    }
                                    if (otw[index2].mudemeter.criticMudemeter == 0) {
                                        $(this).find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-otw").text("—");
                                        $(this).find(".lp-mude-otw").addClass("not-rated");
                                    }
                                    if (otw[index2].mudemeter.criticMudemeter !== 0)
                                        $(this).find(".lp-mudemeter-otw").text(Math.round(otw[index2].mudemeter.criticMudemeter) + "%");
                                    else
                                        $(this).find(".lp-mudemeter-otw").text("");
                                    $(this).find(".lp-medianame-otw").text(otw[index2].mediaName);
                                    $(this).find(".lp-releasedate-otw").text(dateToMonthDay(otw[index2].releaseDate.theatre));
                                    $(this).attr("href", "/movie?mediaID=" + otw[index2].id);
                                });
                            }
                            count2++;
                            if (count2 == 5)
                                break;
                        }
                    }
                    if (response.certified) {
                        $(".lp-header-tbo:eq(0) b").text("CERTIFIED FRESH MOVIE");
                        $(".lp-row-tbo").not(".lp-row-tbo:eq(0)").remove();
                        var count3 = 0;
                        var tbo = response.certified;
                        for (var index3 in tbo) {
                            if (count3 == 0) {
                                if (tbo[index3].mudemeter.criticMudemeter >= 70 && tbo[index3].mudemeter.criticMudemeter <= 100) {
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").text("M");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").addClass("gold");
                                }
                                if (tbo[index3].mudemeter.criticMudemeter >= 60 && tbo[index3].mudemeter.criticMudemeter < 70) {
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").text("M");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").addClass("silver");
                                }
                                if (tbo[index3].mudemeter.criticMudemeter >= 50 && tbo[index3].mudemeter.criticMudemeter < 60) {
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").text("M");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").addClass("bronze");
                                }
                                if (tbo[index3].mudemeter.criticMudemeter > 0 && tbo[index3].mudemeter.criticMudemeter < 50) {
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").text("M");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").addClass("black");
                                }
                                if (tbo[index3].mudemeter.criticMudemeter == 0) {
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").text("—");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").addClass("not-rated");
                                }
                                if (tbo[index3].mudemeter.criticMudemeter != 0)
                                    $(".lp-row-tbo:eq(0)").find(".lp-mudemeter-tbo").text(Math.round(tbo[index3].mudemeter.criticMudemeter) + "%");
                                else
                                    $(".lp-row-tbo:eq(0)").find(".lp-mudemeter-tbo").text("");
                                $(".lp-row-tbo:eq(0)").find(".lp-medianame-tbo").text(tbo[index3].mediaName);
                                $(".lp-row-tbo:eq(0)").find(".lp-releasedate-tbo").text(dateToMonthDay(tbo[index3].releaseDate.theatre));
                                $(".lp-row-tbo:eq(0)").attr("href", "/movie?mediaID=" + tbo[index3].id);
                            }
                            else {
                                $(".lp-row-tbo:eq(0)").clone().appendTo(".lp-row-box-tbo").each(function () {
                                    if (tbo[index3].mudemeter.criticMudemeter >= 70 && tbo[index3].mudemeter.criticMudemeter <= 100) {
                                        $(this).find(".lp-mude-tbo").text("M");
                                        $(this).find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-tbo").addClass("gold");
                                    }
                                    if (tbo[index3].mudemeter.criticMudemeter >= 60 && tbo[index3].mudemeter.criticMudemeter < 70) {
                                        $(this).find(".lp-mude-tbo").text("M");
                                        $(this).find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-tbo").addClass("silver");
                                    }
                                    if (tbo[index3].mudemeter.criticMudemeter >= 50 && tbo[index3].mudemeter.criticMudemeter < 60) {
                                        $(this).find(".lp-mude-tbo").text("M");
                                        $(this).find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-tbo").addClass("bronze");
                                    }
                                    if (tbo[index3].mudemeter.criticMudemeter > 0 && tbo[index3].mudemeter.criticMudemeter < 50) {
                                        $(this).find(".lp-mude-tbo").text("M");
                                        $(this).find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-tbo").addClass("black");
                                    }
                                    if (tbo[index3].mudemeter.criticMudemeter == 0) {
                                        $(this).find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-tbo").text("—");
                                        $(this).find(".lp-mude-tbo").addClass("not-rated");
                                    }
                                    if (tbo[index3].mudemeter.criticMudemeter !== 0)
                                        $(this).find(".lp-mudemeter-tbo").text(Math.round(tbo[index3].mudemeter.criticMudemeter) + "%");
                                    else
                                        $(this).find(".lp-mudemeter-tbo").text("");
                                    $(this).find(".lp-medianame-tbo").text(tbo[index3].mediaName);
                                    $(this).find(".lp-releasedate-tbo").text(dateToMonthDay(tbo[index3].releaseDate.theatre));
                                    $(this).attr("href", "/movie?mediaID=" + tbo[index3].id);
                                });
                            }
                            count3++;
                            if (count3 == 5)
                                break;
                        }
                    }
                    $("#load-page-bounce-stat").fadeOut();
                    return true;
                }
            }
            $("#load-page-bounce-stat").fadeOut();
            return false;
        }
    });
}

function statTvShows() {
    $("#load-page-bounce-stat").show();
    $.ajax({
        url: "/stattvshows",
        type: 'GET',
        complete: function (xhr, status) {
            if (xhr.responseText) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    $(".lp-row-box-container").show();
                    if (response.currentTopTVs) {
                        $(".lp-header:eq(0) b").text("CURRENT TOP TV");
                        $(".lp-row").not(".lp-row:eq(0)").remove();
                        var count = 0;
                        var cs = response.currentTopTVs;
                        for (var index in cs) {
                            if (count == 0) {
                                if (cs[index].mudemeter.criticMudemeter >= 70 && cs[index].mudemeter.criticMudemeter <= 100) {
                                    $(".lp-row:eq(0)").find(".lp-mude").text("M");
                                    $(".lp-row:eq(0)").find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row:eq(0)").find(".lp-mude").addClass("gold");
                                }
                                if (cs[index].mudemeter.criticMudemeter >= 60 && cs[index].mudemeter.criticMudemeter < 70) {
                                    $(".lp-row:eq(0)").find(".lp-mude").text("M");
                                    $(".lp-row:eq(0)").find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row:eq(0)").find(".lp-mude").addClass("silver");
                                }
                                if (cs[index].mudemeter.criticMudemeter >= 50 && cs[index].mudemeter.criticMudemeter < 60) {
                                    $(".lp-row:eq(0)").find(".lp-mude").text("M");
                                    $(".lp-row:eq(0)").find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row:eq(0)").find(".lp-mude").addClass("bronze");
                                }
                                if (cs[index].mudemeter.criticMudemeter > 0 && cs[index].mudemeter.criticMudemeter < 50) {
                                    $(".lp-row:eq(0)").find(".lp-mude").text("M");
                                    $(".lp-row:eq(0)").find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row:eq(0)").find(".lp-mude").addClass("black");
                                }
                                if (cs[index].mudemeter.criticMudemeter == 0) {
                                    $(".lp-row:eq(0)").find(".lp-mude").text("—");
                                    $(".lp-row:eq(0)").find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row:eq(0)").find(".lp-mude").addClass("not-rated");
                                }
                                if (cs[index].mudemeter.criticMudemeter != 0)
                                    $(".lp-row:eq(0)").find(".lp-mudemeter").text(Math.round(cs[index].mudemeter.criticMudemeter) + "%");
                                else
                                    $(".lp-row:eq(0)").find(".lp-mudemeter").text("");
                                $(".lp-row:eq(0)").find(".lp-medianame").text(cs[index].mediaName);
                                $(".lp-row:eq(0)").find(".lp-releasedate").text(dateToMonthDay(cs[index].releaseDate.theatre));
                                $(".lp-row:eq(0)").attr("href", "/season?mediaID=" + cs[index].id);
                            }
                            else {
                                $(".lp-row:eq(0)").clone().appendTo(".lp-row-box").each(function () {
                                    if (cs[index].mudemeter.criticMudemeter >= 70 && cs[index].mudemeter.criticMudemeter < 100) {
                                        $(this).find(".lp-mude").text("M");
                                        $(this).find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude").addClass("gold");
                                    }
                                    if (cs[index].mudemeter.criticMudemeter >= 60 && cs[index].mudemeter.criticMudemeter < 70) {
                                        $(this).find(".lp-mude").text("M");
                                        $(this).find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude").addClass("silver");
                                    }
                                    if (cs[index].mudemeter.criticMudemeter >= 50 && cs[index].mudemeter.criticMudemeter < 60) {
                                        $(this).find(".lp-mude").text("M");
                                        $(this).find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude").addClass("bronze");
                                    }
                                    if (cs[index].mudemeter.criticMudemeter > 0 && cs[index].mudemeter.criticMudemeter < 50) {
                                        $(this).find(".lp-mude").text("M");
                                        $(this).find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude").addClass("black");
                                    }
                                    if (cs[index].mudemeter.criticMudemeter == 0) {
                                        $(this).find(".lp-mude").text("—");
                                        $(this).find(".lp-mude").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude").addClass("not-rated");
                                    }
                                    if (cs[index].mudemeter.criticMudemeter !== 0)
                                        $(this).find(".lp-mudemeter").text(Math.round(cs[index].mudemeter.criticMudemeter) + "%");
                                    else
                                        $(this).find(".lp-mudemeter").text("");
                                    $(this).find(".lp-medianame").text(cs[index].mediaName);
                                    $(this).find(".lp-releasedate").text(dateToMonthDay(cs[index].releaseDate.theatre));
                                    $(this).attr("href", "/season?mediaID=" + cs[index].id);
                                });
                            }
                            count++;
                            if (count == 5)
                                break;
                        }
                    }
                    if (response.certifiedTVs) {
                        $(".lp-header-otw:eq(0) b").text("CERTIFIED FRESH TV");
                        $(".lp-row-otw").not(".lp-row-otw:eq(0)").remove();
                        var count2 = 0;
                        var otw = response.certifiedTVs;
                        for (var index2 in otw) {
                            if (count2 == 0) {
                                if (otw[index2].mudemeter.criticMudemeter >= 70 && otw[index2].mudemeter.criticMudemeter <= 100) {
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").text("M");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").addClass("gold");
                                }
                                if (otw[index2].mudemeter.criticMudemeter >= 60 && otw[index2].mudemeter.criticMudemeter < 70) {
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").text("M");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").addClass("silver");
                                }
                                if (otw[index2].mudemeter.criticMudemeter >= 50 && otw[index2].mudemeter.criticMudemeter < 60) {
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").text("M");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").addClass("bronze");
                                }
                                if (otw[index2].mudemeter.criticMudemeter > 0 && otw[index2].mudemeter.criticMudemeter < 50) {
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").text("M");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").addClass("black");
                                }
                                if (otw[index2].mudemeter.criticMudemeter == 0) {
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").text("—");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-otw:eq(0)").find(".lp-mude-otw").addClass("not-rated");
                                }
                                if (otw[index2].mudemeter.criticMudemeter != 0)
                                    $(".lp-row-otw:eq(0)").find(".lp-mudemeter-otw").text(Math.round(otw[index2].mudemeter.criticMudemeter) + "%");
                                else
                                    $(".lp-row-otw:eq(0)").find(".lp-mudemeter-otw").text("");
                                $(".lp-row-otw:eq(0)").find(".lp-medianame-otw").text(otw[index2].mediaName);
                                $(".lp-row-otw:eq(0)").find(".lp-releasedate-otw").text(dateToMonthDay(otw[index2].releaseDate.theatre));
                                $(".lp-row-otw:eq(0)").attr("href", "/season?mediaID=" + otw[index2].id);
                            }
                            else {
                                $(".lp-row-otw:eq(0)").clone().appendTo(".lp-row-box-otw").each(function () {
                                    if (otw[index2].mudemeter.criticMudemeter >= 70 && otw[index2].mudemeter.criticMudemeter <= 100) {
                                        $(this).find(".lp-mude-otw").text("M");
                                        $(this).find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-otw").addClass("gold");
                                    }
                                    if (otw[index2].mudemeter.criticMudemeter >= 60 && otw[index2].mudemeter.criticMudemeter < 70) {
                                        $(this).find(".lp-mude-otw").text("M");
                                        $(this).find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-otw").addClass("silver");
                                    }
                                    if (otw[index2].mudemeter.criticMudemeter >= 50 && otw[index2].mudemeter.criticMudemeter < 60) {
                                        $(this).find(".lp-mude-otw").text("M");
                                        $(this).find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-otw").addClass("bronze");
                                    }
                                    if (otw[index2].mudemeter.criticMudemeter > 0 && otw[index2].mudemeter.criticMudemeter < 50) {
                                        $(this).find(".lp-mude-otw").text("M");
                                        $(this).find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-otw").addClass("black");
                                    }
                                    if (otw[index2].mudemeter.criticMudemeter == 0) {
                                        $(this).find(".lp-mude-otw").text("—");
                                        $(this).find(".lp-mude-otw").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-otw").addClass("not-rated");
                                    }
                                    if (otw[index2].mudemeter.criticMudemeter !== 0)
                                        $(this).find(".lp-mudemeter-otw").text(Math.round(otw[index2].mudemeter.criticMudemeter) + "%");
                                    else
                                        $(this).find(".lp-mudemeter-otw").text("");
                                    $(this).find(".lp-medianame-otw").text(otw[index2].mediaName);
                                    $(this).find(".lp-releasedate-otw").text(dateToMonthDay(otw[index2].releaseDate.theatre));
                                    $(this).attr("href", "/season?mediaID=" + otw[index2].id);
                                });
                            }
                            count2++;
                            if (count2 == 5)
                                break;
                        }
                    }
                    if (response.popularTVs) {
                        $(".lp-header-tbo:eq(0) b").text("MOST POPULAR TV");
                        $(".lp-row-tbo").not(".lp-row-tbo:eq(0)").remove();
                        var count3 = 0;
                        var tbo = response.popularTVs;
                        for (var index3 in tbo) {
                            if (count3 == 0) {
                                $("#load-page-bounce-stat").css("height", "87%");
                                if (tbo[index3].mudemeter.criticMudemeter >= 70 && tbo[index3].mudemeter.criticMudemeter <= 100) {
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").text("M");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").addClass("gold");
                                }
                                if (tbo[index3].mudemeter.criticMudemeter >= 60 && tbo[index3].mudemeter.criticMudemeter < 70) {
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").text("M");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").addClass("silver");
                                }
                                if (tbo[index3].mudemeter.criticMudemeter >= 50 && tbo[index3].mudemeter.criticMudemeter < 60) {
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").text("M");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").addClass("bronze");
                                }
                                if (tbo[index3].mudemeter.criticMudemeter > 0 && tbo[index3].mudemeter.criticMudemeter < 50) {
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").text("M");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").addClass("black");
                                }
                                if (tbo[index3].mudemeter.criticMudemeter == 0) {
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").text("—");
                                    $(".lp-row-tbo:eq(0)").find(".lp-mude-tbo").addClass("not-rated");
                                }
                                if (tbo[index3].mudemeter.criticMudemeter != 0)
                                    $(".lp-row-tbo:eq(0)").find(".lp-mudemeter-tbo").text(Math.round(tbo[index3].mudemeter.criticMudemeter) + "%");
                                else
                                    $(".lp-row-tbo:eq(0)").find(".lp-mudemeter-tbo").text("");
                                $(".lp-row-tbo:eq(0)").find(".lp-medianame-tbo").text(tbo[index3].mediaName);
                                $(".lp-row-tbo:eq(0)").find(".lp-releasedate-tbo").text(dateToMonthDay(tbo[index3].releaseDate.theatre));
                                $(".lp-row-tbo:eq(0)").attr("href", "/season?mediaID=" + tbo[index3].id);
                            }
                            else {
                                if (count3 == 1) {
                                    $("#load-page-bounce-stat").css("height", "93%");
                                }
                                $(".lp-row-tbo:eq(0)").clone().appendTo(".lp-row-box-tbo").each(function () {
                                    if (tbo[index3].mudemeter.criticMudemeter > 70 && tbo[index3].mudemeter.criticMudemeter <= 100) {
                                        $(this).find(".lp-mude-tbo").text("M");
                                        $(this).find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-tbo").addClass("gold");
                                    }
                                    if (tbo[index3].mudemeter.criticMudemeter >= 60 && tbo[index3].mudemeter.criticMudemeter < 70) {
                                        $(this).find(".lp-mude-tbo").text("M");
                                        $(this).find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-tbo").addClass("silver");
                                    }
                                    if (tbo[index3].mudemeter.criticMudemeter >= 50 && tbo[index3].mudemeter.criticMudemeter < 60) {
                                        $(this).find(".lp-mude-tbo").text("M");
                                        $(this).find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-tbo").addClass("bronze");
                                    }
                                    if (tbo[index3].mudemeter.criticMudemeter > 0 && tbo[index3].mudemeter.criticMudemeter < 50) {
                                        $(this).find(".lp-mude-tbo").text("M");
                                        $(this).find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-tbo").addClass("black");
                                    }
                                    if (tbo[index3].mudemeter.criticMudemeter == 0) {
                                        $(this).find(".lp-mude-tbo").text("—");
                                        $(this).find(".lp-mude-tbo").removeClass("gold silver bronze black not-rated");
                                        $(this).find(".lp-mude-tbo").addClass("not-rated");
                                    }
                                    if (tbo[index3].mudemeter.criticMudemeter !== 0)
                                        $(this).find(".lp-mudemeter-tbo").text(Math.round(tbo[index3].mudemeter.criticMudemeter) + "%");
                                    else
                                        $(this).find(".lp-mudemeter-tbo").text("");
                                    $(this).find(".lp-medianame-tbo").text(tbo[index3].mediaName);
                                    $(this).find(".lp-releasedate-tbo").text(dateToMonthDay(tbo[index3].releaseDate.theatre));
                                    $(this).attr("href", "/season?mediaID=" + tbo[index3].id);
                                });
                            }
                            count3++;
                            if (count3 == 5)
                                break;
                        }
                    }
                    $("#load-page-bounce-stat").fadeOut();
                    return true;
                }
            }
            $("#load-page-bounce-stat").fadeOut();
            return false;
        }
    });
}

function myReviewAndWTS() {
    var mediaID = getParameterByName("mediaID");
    var stop = false;
    if ($(".first-name:eq(0)").text() !== "No-User") {
        // $("#post-button").addClass("disabled");
        // $("#post-button").attr("aria-disabled", "true");
        $(".review-box-big").css("opacity", "0.3");
        $("#load-page-bounce-review").show();
        $.ajax({
            url: "/myreviewandwts",
            type: "POST",
            data: "mediaID=" + mediaID,
            complete: function (xhr, status) {
                $("#load-page-bounce-review").hide();
                $(".review-box-big").css("opacity", "1");
                $("#post-button").removeClass("disabled");
                $("#post-button").attr("aria-disabled", "false");
                if (xhr.responseText) {
                    var response = JSON.parse(xhr.responseText);
                    if (response.success) {
                        if (response.myReview) {
                            $("#post-button").hide();
                            $("#edit-button").show();
                            $("#delete-button").show();
                            $(".starRadio").each(function () {
                                if (response.myReview.score >= parseFloat($(this).val()) && !stop) {
                                    $(this).prop('checked', true);
                                    stop = true;
                                }
                            });
                            $("#review-comment").val(response.myReview.comment);
                        }
                        if (response.myWantToSee) {
                            if (response.myWantToSee.isWantToSee)
                                $(".wts-button").addClass("active");
                            else
                                $(".ni-button").addClass("active");      
                        }
                    }
                }
            }
        });
    }
}

function deleteReview() {
    var mediaID = getParameterByName("mediaID");
    // $("#delete-button").addClass("disabled");
    // $("#delete-button").attr("aria-disabled", "true");
    // $("#edit-button").addClass("disabled");
    // $("#edit-button").attr("aria-disabled", "true");
    $(".review-box-big").css("opacity", "0.3");
    $("#load-page-bounce-review").show();
    $.ajax({
        url: "/deletereview",
        type: "POST",
        data: "mediaID=" + mediaID,
        complete: function (xhr, status) {
            $("#load-page-bounce-review").hide();
            if (xhr.responseText) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    $(".starRadio:checked").prop('checked', false);
                    $("#review-comment").val("");
                    $(".review-box-big").css("opacity", "1");
                    $("#post-button-alert p").text("DELETED!!");
                    var ps1 = $("#post-button-alert").show(0).delay(5000).hide(0);
                    $.when(ps1).done(function () {
                        $("#delete-button").removeClass("disabled");
                        $("#delete-button").attr("aria-disabled", "false");
                        $("#edit-button").removeClass("disabled");
                        $("#edit-button").attr("aria-disabled", "false");
                    });
                    myReviewAndWTS();
                    //Update userMudeMeter
                    if (response.ratings) {
                        if (response.ratings.audienceMudemeter != null && response.ratings.audienceMudemeter != 0)
                            $(".a-mudemeter").text(Math.round(response.ratings.audienceMudemeter) + "%");
                        if (response.ratings.audienceAvgScore != null && response.ratings.audienceAvgScore != 0)
                            $(".a-score").text(Math.round(response.ratings.audienceAvgScore * 10) / 10.0 + "  /  5");
                        if (response.ratings.criticMudemeter != null && response.ratings.criticMudemeter != 0)
                            $(".c-mudemeter").text(Math.round(response.ratings.criticMudemeter) + "%");
                        if (response.ratings.criticAvgScore != null && response.ratings.criticAvgScore != 0)
                            $(".c-score").text(Math.round(response.ratings.criticAvgScore * 10) / 10.0 + "  /  5");
                    }
                    $("#edit-button").hide();
                    $("#delete-button").hide();
                    $("#post-button").show();    
                    updateCriticReview();
                    updateAudienceReview();
                    return true;
                }
            }
            $("#post-button").removeClass("disabled");
            $("#post-button").attr("aria-disabled", "false");
            $("#failModalCenter").modal("show");
            return false;
        }
    });
}

function editReview() {
    var mediaID = getParameterByName("mediaID");
    var score;
    var comment;
    score = $(".starRadio:checked").val();
    comment = $("#review-comment").val();
    if (!score) {
        $("#failModalCenter").modal("show");
        return;
    }
    if (!comment || comment == "Add a Review") {
        $("#failModalCenter").modal("show");
        return;
    }
    // $("#delete-button").addClass("disabled");
    // $("#delete-button").attr("aria-disabled", "true");
    // $("#edit-button").addClass("disabled");
    // $("#edit-button").attr("aria-disabled", "true");
    $(".review-box-big").css("opacity", "0.3");
    $("#load-page-bounce-review").show();
    $.ajax({
        url: "/editreview",
        type: "POST",
        data: "mediaID=" + mediaID + "&score=" + score + "&comment=" + comment,
        complete: function (xhr, status) {
            $("#load-page-bounce-review").hide();
            if (xhr.responseText) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    $(".review-box-big").css("opacity", "1");
                    $("#post-button-alert p").text("EDITED!!");
                    var ps1 = $("#post-button-alert").show(0).delay(5000).hide(0);
                    $.when(ps1).done(function () {
                        $("#delete-button").removeClass("disabled");
                        $("#delete-button").attr("aria-disabled", "false");
                        $("#edit-button").removeClass("disabled");
                        $("#edit-button").attr("aria-disabled", "false");
                    });
                    myReviewAndWTS();
                    //Update userMudeMeter
                    if (response.ratings) {
                        if (response.ratings.audienceMudemeter != null && response.ratings.audienceMudemeter != 0)
                            $(".a-mudemeter").text(Math.round(response.ratings.audienceMudemeter) + "%");
                        if (response.ratings.audienceAvgScore != null && response.ratings.audienceAvgScore != 0)
                            $(".a-score").text(Math.round(response.ratings.audienceAvgScore * 10) / 10.0 + "  /  5");
                        if (response.ratings.criticMudemeter != null && response.ratings.criticMudemeter != 0)
                            $(".c-mudemeter").text(Math.round(response.ratings.criticMudemeter) + "%");
                        if (response.ratings.criticAvgScore != null && response.ratings.criticAvgScore != 0)
                            $(".c-score").text(Math.round(response.ratings.criticAvgScore * 10) / 10.0 + "  /  5");
                    }
                    updateCriticReview();
                    updateAudienceReview();
                    return true;
                }
            }
            $("#post-button").removeClass("disabled");
            $("#post-button").attr("aria-disabled", "false");
            $("#failModalCenter").modal("show");
            return false;
        }
    });
}

function generateThumbnail() {
    var _VIDEO = document.querySelector("#video-element"),
        _CANVAS = document.querySelector("#canvas-element"),
        _CANVAS_CTX = _CANVAS.getContext("2d");
    
    _VIDEO.addEventListener('loadedmetadata', function () {
        _CANVAS.width = _VIDEO.videoWidth;
        _CANVAS.height = _VIDEO.videoHeight;
    });

    _CANVAS_CTX.drawImage(_VIDEO, 0, 0, _VIDEO.videoWidth, _VIDEO.videoHeight);
    var dataURL = _CANVAS.toDataURL();

    var img = document.createElement('img');
    img.setAttribute('src', dataURL);
    $("#movie-video .item:eq(0) img").replaceWith(img);
    $("#movie-video .item:eq(0) img").addClass("card-img-top rounded-0");
}