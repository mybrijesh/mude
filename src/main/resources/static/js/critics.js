$(function () {
    $("#top-logo").attr("href", "/");

    $('.list').click(function () {
        $(this).addClass('active').siblings().removeClass('active');
        $(this).removeClass('bat').siblings().addClass('bat');
    });

    $('#nav-critics-home').click(function () {
        critic_spotlight()
        $('#critics-home').fadeIn().siblings().hide();
        $('#navbar-critics').show();
    });
    $('#nav-critics-list').click(function () {
        critic_list("A");
        $('#critics-list').fadeIn().siblings().hide();
        $('#navbar-critics').show();
    });
    $('#nav-publications-list').click(function () {
        $('#publications-list').fadeIn().siblings().hide();
        $('#navbar-critics').show();
    });
    $('#nav-critics-groups').click(function () {
        $('#critics-groups').fadeIn().siblings().hide();
        $('#navbar-critics').show();
    });
    $('#nav-latest-film-reviews').click(function () {
        latest_review(0);
        $('#latest-film-reviews').fadeIn().siblings().hide();
        $('#navbar-critics').show();
    });
    $('#nav-critics-criteria').click(function () {
        $('#critics-criteria').fadeIn().siblings().hide();
        $('#navbar-critics').show();
    });
    /*$('#nav-critic-application').click(function () {
        $('#critics-criteria').fadeIn().siblings().hide();
        $('#navbar-critics').show();
    });*/

    $('.critic-detail-button').click(function () {
        $('#critic-detail').fadeIn().siblings().hide();
        $("#nav-critics-list").addClass('active').siblings().removeClass('active');
        $("#nav-critics-list").removeClass('bat').siblings().addClass('bat');
        $('#navbar-critics').show();
        return false; //cancel href
    });

    $('.publication-detail-button').click(function () {
        $('#publication-detail').removeClass('d-none').siblings().addClass('d-none');
        $("#nav-publications-list").addClass('active').siblings().removeClass('active');
        $("#nav-publications-list").removeClass('bat').siblings().addClass('bat');
        $('#navbar-critics').show();
        return false; //cancel href
    });

    //CHECKING IF A-Z CLICKED UNDER CRITIC-LIST TAB
    $("#Listing_by_last_name .page-link").click(function (){
        critic_list($(this).text());
    });

});

function critic_spotlight(){
    $.ajax({
        url: "/topfivecritics",
        type: "POST",
        complete: function(data){
            var response = JSON.parse(data.responseText);
            if(response.success){
                console.log(response);
                var totalCount = response.top5.length;
                var top5 = response.top5;
                if(totalCount > 0){
                    if(top5.length > 0){
                        $("#critic_spotlight_parent .critic_spotlight_child").not(".critic_spotlight_child:eq(0)").remove();
                    }
                    for(i=0;i<top5.length && i < 2;i++){
                        var critic = top5[i];
                        $(".critic_spotlight_child:eq(0)").clone().appendTo("#critic_spotlight_parent").each(function(){
                            $(this).find(".critic_image_button").attr("href","/member?memberID="+critic.id);
                            $(this).find("img").attr("src","http://23.94.27.164/resource/members/"+critic.id+"/photos/0.jpg");
                            $(this).find("img").attr("onerror", "this.onerror=null; this.src='images/critics.gif';");
                            $(this).find(".critic_name_button").attr("href","/member?memberID="+critic.id);
                            $(this).find(".critic_name_button").html(critic.name.firstName+" "+critic.name.lastName);
                            if(critic.topCritic)
                                $(this).find("p").attr("style","display:block;");
                        });
                    }
                }
                else{
                    $("#critic_spotlight_parent .critic_spotlight_child").not(".critic_spotlight_child:eq(0)").remove();
                }

                $("#critic_spotlight_parent .critic_spotlight_child").show();
                $("#critic_spotlight_parent .critic_spotlight_child:eq(0)").hide();
            }
        }
    });
}

function critic_list(letter){
    $.ajax({
        url: "/criticsbyalphabet",
        type: "POST",
        data: "alphabet="+letter,
        complete: function(data){
            var response = JSON.parse(data.responseText);
            if(response.success){
//                    console.log(response);
                var totalCount = response.critics.length;
                var critics = response.critics;
                if(totalCount > 0){
                    if(critics.length > 0){
                        $("#alpha_critic_parent .alpha_critic_child").not(".alpha_critic_child:eq(0)").remove();
                    }
                    for(i=0;i<critics.length;i++){
                        var critic = critics[i];
                        $(".alpha_critic_child:eq(0)").clone().appendTo("#alpha_critic_parent").each(function(){
                            $(this).find("a").attr("href","/member?memberID="+critic.id);
                            $(this).find("a").html(critic.name.firstName+" "+critic.name.lastName);
                            if(critic.topCritic)
                                $(this).find("p").attr("style","display:block;");
                        });
                    }
                }
                else{
                    $("#alpha_critic_parent .alpha_critic_child").not(".alpha_critic_child:eq(0)").remove();
                }

                $("#alpha_critic_parent .alpha_critic_child").show();
                $("#alpha_critic_parent .alpha_critic_child:eq(0)").hide();
            }
        }
    });
}

function latest_review(pageNo){
    $.ajax({
        url: "/latestreviews",
        type: "POST",
        data: "pageNo="+pageNo,
        complete: function(data){
            var response = JSON.parse(data.responseText);
            if(response.success){
                console.log(response);
                var totalCount = response.rTotalCnt;
                var reviews = response.reviews;
                if(totalCount > 0){
                    if(reviews.length > 0){
                        $("#latest_review_parent .latest_review_child").not(".latest_review_child:eq(0)").remove();
                    }
                    for(i=0;i<reviews.length;i++){
                        var review = reviews[i];
                        $(".latest_review_child:eq(0)").clone().appendTo("#latest_review_parent").each(function(){
                            $(this).find(".mmeter").html(""+parseInt(review.media.mudemeter.criticMudemeter)+"%");
                            $(this).find(".text-danger-light").html(review.media.mediaName);
                            $(this).find(".latest_review_d").html(review.media.releaseDate.theatre.split("T")[0]);
                            $(this).find(".latest_review_child_review").html(review.comment);
                            $(this).find(".latest_review_child_score").html(review.score);
                            $(this).find(".latest_review_child_regDate").html(review.regDate.split("T")[0]);
                            $(this).find(".critic-detail-button").attr("href","/member?memberID="+review.member.id);
                            $(this).find("b").html(review.member.name.firstName+" "+review.member.name.lastName);
                            if(review.member.topCritic)
                                $(this).find("top-badge").attr("style","display:block;");
                        });
                    }
                }
                else{
                    $("#latest_review_parent .latest_review_child").not(".latest_review_child:eq(0)").remove();
                }

                $("#latest_review_parent .latest_review_child").show();
                $("#latest_review_parent .latest_review_child:eq(0)").hide();

                var numberOfPages = totalCount / 12;
                numberOfPages = parseInt(numberOfPages);
                if(response.totalCnt % 12 != 0)
                    numberOfPages++;

                $("#pagination").html("");
                for(i=0;i<numberOfPages;i++){
                    var pn = i + 1;
                    if(pn < pageNo - 3 || pn > pageNo + 3){
                        $("#pagination").append('<a class="mx-2" href="#" onclick="latest_review('+i+')" style="display:none;">'+pn+'</a>');
                    }
                    else{
                        $("#pagination").append('<a class="mx-2" href="#" onclick="latest_review('+i+')">'+pn+'</a>');
                    }
                }


            }
        }
    });
}

