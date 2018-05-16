$(function () {
    $('.details').owlCarousel({
        margin: 10,
        autoWidth: false,
        nav: false,
        items: 6,
        responsive: {
            0: {
                items: 2
            },
            500: {
                items: 4
            },
            1000: {
                items: 6
            }
        }
    });


    $(".users-tab button").click(function () {
        $(this).siblings().removeClass("active");
        if (!$(this).hasClass("active")) {
            $(this).addClass("active");
            buttonHandler($(this).attr("id"));
        }
    });

    var dynamicContent = getParameterByName('tab');
    if (dynamicContent == '0') {
        $("#wts-ni").siblings().removeClass("active");
        if (!$("#wts-ni").hasClass("active")) {
            $("#wts-ni").addClass("active");
            buttonHandler($("#wts-ni").attr("id"));
        }
    }
    if (dynamicContent == '1') {
        $("#movie-reviews").siblings().removeClass("active");
        if (!$("#movie-reviews").hasClass("active")) {
            $("#movie-reviews").addClass("active");
            buttonHandler($("#movie-reviews").attr("id"));
        }
    }
    if (dynamicContent == '2') {
        $("#wts-ni").siblings().removeClass("active");
        if (!$("#wts-ni").hasClass("active")) {
            $("#wts-ni").addClass("active");
            buttonHandler($("#wts-ni").attr("id"));
        }
    }
    if (dynamicContent == '3') {
        $("#wts-ni").siblings().removeClass("active");
        if (!$("#wts-ni").hasClass("active")) {
            $("#wts-ni").addClass("active");
            buttonHandler($("#wts-ni").attr("id"));
        }
    }

    $("#top-logo").attr("href", "/");

    $('#edit-media-search-bar-box').on({
    "shown.bs.dropdown": function () { $(this).attr('closable', false); },
    "hide.bs.dropdown": function () { return $(this).attr('closable') == 'true'; }
    });
    $('#edit-filmography-search-bar-box').on({
    "shown.bs.dropdown": function () { $(this).attr('closable', false); },
    "hide.bs.dropdown": function () { return $(this).attr('closable') == 'true'; }
    });
    $('#edit-filmography-search-bar-box').children().first().on({
    "focusout": function () {
      $(this).parent().attr('closable', true);
    }
    });
});

function searchmediatoedit(){
    var keyword = document.getElementById("edit-media-search-bar").value;
    $.ajax({
        url: "/allmedianame",
        type: 'GET',
        data: "keyword="+keyword,
        complete: function (data) {
            var response = JSON.parse(data.responseText);
            if(response.success){
                if(response.movie.length > 0){
                    var m = response.movie;
                    for(i =0 ;i < m.length;i++){
                        addtoeditsearchelement(m[i]);
                    }
                }
                if(response.tvseries.length > 0){
                    var m = response.tvseries;
                    for(i =0 ;i < m.length;i++){
                        addtoeditsearchelement(m[i]);
                    }
                }
                if(response.season.length > 0){
                    var m = response.season;
                    for(i =0 ;i < m.length;i++){
                        addtoeditsearchelement(m[i]);
                    }
                }
                if(response.episode.length > 0){
                    var m = response.episode;
                    for(i =0 ;i < m.length;i++){
                        addtoeditsearchelement(m[i]);
                    }
                }
            }
        }
    });
}
function searchforartists(){
    $("#edit-filmography-search-bar-dd").html("");
    var keyword = document.getElementById("edit-filmography-search-bar").value;
    $.ajax({
        url: "/artistbykeyword",
        type: 'GET',
        data: "keyword="+keyword,
        complete: function (data) {
            var response = JSON.parse(data.responseText);

            if(response.success){
                console.log("ADDING ARTIST TO CURRENT ARTIST LIST"+response);
                var artists = response.artist;
                if(artists.length > 0){
                    for(i = 0 ; i < artists.length; i++){
                        var artist = artists[i];
                        var dob = artist.birthdate.split("T")[0];
                        var name = artist.name.firstName+"_"+artist.name.lastName;
                        var text = name+"_"+dob;
                        $("#edit-filmography-search-bar-dd").append('<div class="w-100" onclick="addArtistToMediaInFilmography('+artist.id+')"><a  style="cursor:pointer">'+text+'</a></div>');
                    }
                }
            }
        }
    });
}

function changepassword(){
    var cpwd = document.getElementById("current_password").value;
    var npwd = document.getElementById("new_password").value;
    $.ajax({
        url: "/changepwd",
        type: "POST",
        data: "currentPwd="+cpwd+"&newPwd="+npwd,
        complete: function(data){
            var response = JSON.parse(data.responseText)
            $("#current_password").val("");
            $("#new_password").val("");
            if(response.success){
                $("#changepassword").modal("hide");
                $("#successModalCenter").modal("show");
            }
            else{
                $("#changepassword").modal("hide");
                $("#failModalCenter").modal("show");
            }
        }
    });
}
function getuserinfo(){
    $.ajax({
        url: "/myinfo",
        type: "GET",
        complete: function(data){
            var response = JSON.parse(data.responseText);
            if(response.success){
                console.log(response);
                var fname = response.member.name.firstName;
                var lname = response.member.name.lastName;
                var desc = response.member.desc;
                var email = response.member.email.address;

                $("#editinfo_Title").text(response.member.id);
                $("#editinfo_firstname").val(fname);
                $("#editinfo_lastname").val(lname);
                $("#editinfo_email").val(email);
                if(desc != null && desc != ""){
                    $("#editinfo_desc").val(desc);
                }
                if(response.member.private){
                    $("#editinfo_privacy").val("true");
                }
                else{
                    $("#editinfo_privacy").val("false");
                }
                $("#changepersonalinfo").modal("show");
            }
        }
    });
}
function submit_editinfo(){
    var ID = $("#editinfo_Title").text();
    var fname = document.getElementById("editinfo_firstname").value;
    var lname = document.getElementById("editinfo_lastname").value;
    var desc = document.getElementById("editinfo_desc").value;
    var email = document.getElementById("editinfo_email").value;
    var pwd = document.getElementById("editinfo_pwd").value;
    console.log(fname.length+":"+lname.length+":"+email.length);
    if(fname.length > 0 && lname.length > 0 && email.length > 0){
        console.log("we here and there");
        var p = document.getElementById("editinfo_privacy").value;
        console.log("privacy setting is: "+p);
        $.ajax({
            url: "/updatemyinfo",
            type: "POST",
            data: "firstName="+fname+"&lastName="+lname+"&email="+email+"&desc="+desc+"&password="+pwd+"&isPrivate="+p,
            complete: function(data){
                var response = JSON.parse(data.responseText);
                clearAllModal();
                if(response.success){
                    reloadDivByID("descDiv");
                    $("#changepersonalinfo").modal("hide");
                    $("#successModalCenter").modal("show");
                }
                else{
                    $("#changepersonalinfo").modal("hide");
                    $("#failModalCenter").modal("show");
                }
            }
        });
    }
    else{
        console.log("firstname lastname and email can't be empty")
    }
}

function getallreportedreview(){
 $("#reportedreviewBody").html("");
// console.log("Getting Reported Reviews");
 $.ajax({
    url: "/reportedreviews",
    type: 'GET',
    complete: function (data) {
        var response = JSON.parse(data.responseText);
        if(response.success){
            console.log(response);
            var reportedreview = response.reported;
            var innerBody = "";

            for(i=0;i<reportedreview.length;i++){
                var comment = reportedreview[i].comment;
                var mediaName = reportedreview[i].media.mediaName;
                var ratingType = reportedreview[i].ratingType;
                var reviewDate = reportedreview[i].regDate.split("T")[0];
                var score = reportedreview[i].score;

                innerBody += '<div class="row m-0 p-1 w-100 border">'
                innerBody += '<div class="col-2 w-100 m-0">'+mediaName+'</div>'
                innerBody += '<div class="col-5 w-100 m-0">'+comment+'</div>'
                innerBody += '<div class="col-2 w-100 m-0">'+reviewDate+'</div>'
                innerBody += '<div class="col-1 w-100 m-0">'+ratingType+'</div>'
                innerBody += '<div class="col-1 w-100 m-0">'+score+'</div>'
                innerBody += '<div class="col-1 w-100 m-0"><button onclick="removeReview('+reportedreview[i].id+')" type="button" class="close" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>'
                innerBody += '</div></div>'
            }
            $("#reportedreviewBody").html(innerBody);
            $("#reportedreview").modal("show");
        }
    }
 });
}
function refreshSession(){
    $.ajax({
        url: "refreshsession",
        get: "POST",
        complete: function(data){
            var response = JSON.parse(data.responseText);
            if(response.success){
                console.log("SESSION MEMBER REFRESHED");
            }

        }
    })
}
function getAllCriticRequests(){
    $("#criticrequestBody").html("");
    // console.log("Getting Reported Reviews");
    $.ajax({
        url: "/viewrequests",
        type: 'GET',
        complete: function (data) {
            var response = JSON.parse(data.responseText);
            if(response.success){
                console.log(response);
                var requests = response.reqs;
                var innerBody = "";

                for(i=0;i<requests.length;i++){
                    var ID = requests[i].id;
                    var memberID = requests[i].member.id;
                    var appliedDate = requests[i].regDate.split("T")[0];
                    var name = requests[i].member.name.firstName+" "+requests[i].member.name.lastName;
                    innerBody += '<div class="row m-0 p-1 w-100 border">'
                    innerBody += '<div class="col-4 w-100 m-0"><a href="member?memberID='+memberID+'" style="color:black;">'+name+'</a></div>'
                    innerBody += '<div class="col-4 w-100 m-0">'+appliedDate+'</div>'
                    innerBody += '<div class="col-2 w-100 m-0"><div class="mx-0 my-0 px-2 py-0 text-center"><a onclick="approveRequest('+memberID+')"><i class="fas fa-check"></i></a></div></div>'
                    innerBody += '<div class="col-2 w-100 m-0"><div class="mx-0 my-0 px-2 py-0 text-center"><a onclick="declineRequest('+memberID+')"><i class="fas fa-times"></i></a></div></div>'
                    innerBody += '</div>'
                }
                $("#criticrequestBody").html(innerBody);
                $("#criticrequest").modal("show");
            }
        }
    });
}
function approveRequest(requestID){
    $.ajax({
        url: "/acceptornot",
        type: "POST",
        data: "memberID="+requestID+"&accepted="+"true",
        complete: function(data){
            var response = JSON.parse(data.responseText);
            if(response.success){
                $("#criticrequest").modal("hide");
                $("#successModalCenter").modal("show");
            }
            else{
                $("#criticrequest").modal("hide");
                $("#failModalCenter").modal("show");
            }
        }
    });
}
function declineRequest(requestID){
    $.ajax({
        url: "/acceptornot",
        type: "POST",
        data: "memberID="+requestID+"&accepted="+"false",
        complete: function(data){
            var response = JSON.parse(data.responseText);
            if(response.success){
                $("#criticrequest").modal("hide");
                $("#successModalCenter").modal("show");
            }
            else{
                $("#criticrequest").modal("hide");
                $("#failModalCenter").modal("show");
            }
        }
    });
}
function removeReview(reviewID){
    console.log("Review Deleted: "+reviewID);
    $.ajax({
        url: "/removereview",
        type: 'POST',
        data: "reviewID="+reviewID,
        complete: function (data) {
            var response = JSON.parse(data.responseText);
            if(response.success){
                $("#reportedreview").modal("hide");
                $("#successModalCenter").modal("show");
            }
            else{
                $("#reportedreview").modal("hide");
                $("#failModalCenter").modal("show");
            }
        }
     });
}
function fukIT(){
    $.ajax({
        url: "/deletemyprofile",
        type: "POST",
        complete: function(data){
            var response = JSON.parse(data.responseText);
            if(response.success){
                $("#successModalCenter").modal("show");
                window.location.replace("http://localhost:8888/");
            }
            else{
                $("#failModalCenter").modal("show");
            }

        }
    });
}
function addArtistToMediaInFilmography(artistID){
    var mediaID = document.getElementById("mediaSelectedForFilmography").value;
    var artistType = document.getElementById("addFilmographySelectArtistType").value;
    var artistRole = document.getElementById("addFilmographySelectArtistRole").value;
    $.ajax({
        url: "/addfilmo",
        type: 'POST',
        data: "mediaID="+mediaID+"&artistID="+artistID+"&artistType="+artistType+"&artistRole="+artistRole,
        complete: function (data) {
            var response = JSON.parse(data.responseText);
            if(response.success){
                console.log("successfully removed artists from filmography");
                //UPDATE THE OPTION LIST WITH NEW FILMOGRAPHY
                addFilmographyCastToCurrentList(mediaID);
            }
        }
    });
}
function addtoeditsearchelement(media){
    //CHECK IF MODAL WAS REQUESTED FOR MEDIA OR FILMOGRAPHY
    $("#edit-media-search-bar-dd").append('<div class="w-100" onclick="loadSelectedMediaInformation('+media.id+')"><a  style="cursor:pointer">'+media.mediaName+'</a></div>');
}
function getMediaRelatedFilmography(mediaID){
    $("#mediaSelectedForFilmography").html("<option selected value="+mediaID+">"+mediaID+"</option>");
    addFilmographyCastToCurrentList(mediaID);
    $("#filmographymodal").modal('toggle');
}
function addFilmographyCastToCurrentList(mediaID){
    //CLEAR BEFORE ADDING
    $("#artistsToRemoveFromFilmography").html("");
    $.ajax({
        //GET LIST FROM FILMOGRAPHY BASED ON mediaID
        url: "/getfilmo",
        type: 'GET',
        data: "mediaID="+mediaID,
        complete: function (data) {
            var response = JSON.parse(data.responseText);
            if(response.success){
                var response = JSON.parse(data.responseText);
                if(response.success){
                    if(response.filmo.length > 0){
                        //ARTIST
                        var artists = response.filmo;
                        for(i = 0; i< artists.length; i++){
                            var artist = artists[i].artist;
                            var Name = artist.name;
                            var artistName = Name.firstName +"_"+Name.lastName;
                            var artistType = artists[i].artistType;
                            var artistRole = artists[i].artistRole;
                            var crew = artistName +"_"+artistType+"_"+artistRole
                            $("#artistsToRemoveFromFilmography").append("<option value="+artists[i].id+">"+crew+"</option>")
                        }
                    }
                }
            }
        }
    });
}
function removeFromFilmography(){
    var mediaID = document.getElementById("mediaSelectedForFilmography").value;
    var artists_selected = ""
    var i = 0;
    $("#artistsToRemoveFromFilmography option:selected").each(function(){
        artists_selected += (this.value +",");
        i = i + 1;
    });
    if(i > 0){
        $.ajax({
            url: "/deletefilmo",
            type: 'POST',
            data: "filmoIDs="+artists_selected,
            complete: function (data) {
                var response = JSON.parse(data.responseText);
                if(response.success){
//                    clearEditMediaModel();
                    //UPDATE THE OPTION LIST WITH NEW FILMOGRAPHY
                    addFilmographyCastToCurrentList(mediaID);
                }
            }
        });
    }
}
function removeMedia(mediaID){
    $.ajax({
        url: "/removemedia",
        type: "POST",
        data: "mediaID="+mediaID,
        complete:function(data){
            var response = JSON.parse(data.responseText);
            if(response.success){
                $("#edit-media-search-bar").val("");
                $("#selectmediatoedit").modal("hide");
                $("#successModalCenter").modal("show");
            }
            else{
                $("#edit-media-search-bar").val("");
                $("#selectmediatoedit").modal("hide");
                $("#failModalCenter").modal("show");
            }
        }
    });
}
function loadSelectedMediaInformation(mediaID){
    $("#edit-media-search-bar-dd").html("");
    $('#selectmediatoedit').modal('toggle');
    //CHECK IF MODAL WAS REQUESTED FOR MEDIA OR FILMOGRAPHY
    var editaction = document.getElementById("editaction").value;
    if(editaction == "filmography"){
        getMediaRelatedFilmography(mediaID);
    }
    else if(editaction == "removemedia"){
        removeMedia(mediaID);
    }
    else{
        $.ajax({
            url: "/mediainfo",
            type: 'GET',
            data: "mediaID="+mediaID,
            complete: function (data) {
                var response = JSON.parse(data.responseText);
                if(response.success){
                    $("#editmediamodalTitle").text(response.media.id);
    //                $("$editmediamodalTitle").attr("value",response.media.id);
                    if(response.media.mediaType == "MOVIE"){$("#editmediamdoal_mediatype").val("MOVIE");}
                    else if(response.media.mediaType == "TVSERIES"){$("#editmediamdoal_mediatype").val("TVSERIES");}
                    else if(response.media.mediaType == "SEASON"){$("#editmediamdoal_mediatype").val("SEASON");}
                    else if(response.media.mediaType == "EPISODE"){$("#editmediamdoal_mediatype").val("EPISODE");}
                    if(response.media.filmRating == "NA"){$("#editmediamdoal_mediarating").val("NA");}
                    else if(response.media.filmRating == "G"){$("#editmediamdoal_mediarating").val("G");}
                    else if(response.media.filmRating == "PG"){$("#editmediamdoal_mediarating").val("PG");}
                    else if(response.media.filmRating == "PG13"){$("#editmediamdoal_mediarating").val("PG13");}
                    else if(response.media.filmRating == "R"){$("#editmediamdoal_mediarating").val("R");}
                    else if(response.media.filmRating == "NC17"){$("#editmediamdoal_mediarating").val("NC17");}
                    $("#editmediamdoal_mediaName").val(response.media.mediaName);
                    if(response.parent != null)
                        $("#editmediamdoal_parentid").val(response.parent.id);
                    $("#editmediamdoal_description").val(response.media.desc);
                    $("#editmediamdoal_releasedate").val(response.media.releaseDate.theatre.split("T")[0]);
                    $("#editmediamdoal_dvdreleasedate").val(response.media.releaseDate.dvd.split("T")[0]);
                    $("#editmediamdoal_runtime").val(response.media.runtime);
                    $("#editmediamdoal_boxoffice").val(response.media.boxOffice.number);

                    $("#editmediamodal").modal("toggle");
                }
            }
        });
    }
}
function clearAllModal(){
    $("#edit-media-search-bar").val("");
    $("#edit-media-search-bar-dd").html("");
    $("#editmediamodalTitle").text("MEDIA");//THIS IS TO TELL IF IT'S NEW OR EDIT MOVIE
    $("#editmediamdoal_parentid").val(0);
    $("#editmediamdoal_mediaName").val("");
    $("#editmediamdoal_description").val("");
    $("#editmediamdoal_releasedate").val("1000-01-01");
    $("#editmediamdoal_dvdreleasedate").val("1000-01-01");
    $("#editmediamdoal_runtime").val(0);
    $("#editmediamdoal_boxoffice").val(0);
    $("#addFilmographySelectArtistRole").val("");
    $("#edit-filmography-search-bar").val("");

    $("#editinfo_Title").text("");
    $("#editinfo_firstname").val("");
    $("#editinfo_lastname").val("");
    $("#editinfo_email").val("");
    $("#editinfo_desc").val("");

    $("#deleteotheruser_memberID").val("");
}

function deleteotheruser(){
    var memberID = document.getElementById("deleteotheruser_memberID").value;
    $.ajax({
        url:"/removemember",
        type: "POST",
        data: "memberID="+memberID,
        complete: function(data){
            var response = JSON.parse(data.responseText);
            if(response.success){
                clearAllModal();
                $("#deleteotheruser").modal("hide");
                $("#successModalCenter").modal("show");
            }
            else{
                clearAllModal();
                $("#deleteotheruser").modal("hide");
                $("#failModalCenter").modal("show");
            }
        }
    });
}

function reloadDivByID(divId){
    //that space front of # matters...it's syntex
  $(" #"+divId).load(" #"+divId);
}

function submiteditmediainfo(){
    var title = $("#editmediamodalTitle").text();
    mediaName = document.getElementById("editmediamdoal_mediaName").value;
    parentID = document.getElementById("editmediamdoal_parentid").value;
    mediaType = document.getElementById("editmediamdoal_mediatype").value;
    mediaRating = document.getElementById("editmediamdoal_mediarating").value;
    desc = document.getElementById("editmediamdoal_description").value;
    rdate = document.getElementById("editmediamdoal_releasedate").value;
    ddate = document.getElementById("editmediamdoal_dvdreleasedate").value;
    runtime = document.getElementById("editmediamdoal_runtime").value;
    boxoffice = document.getElementById("editmediamdoal_boxoffice").value;
    var genre_selected = ""
    $("#editmediamdoal_genres option:selected").each(function(){
        genre_selected += (this.value +",");
    });
    if(title == "MEDIA"){
        console.log("ADDING NEW MEDIA");
        $.ajax({
            url: "/addmedia",
            type: 'POST',
            data: "mediaName="+mediaName+"&parentID="+parentID+"&mediaType="+mediaType+"&mediaRating="+mediaRating+"&releaseDate="+rdate+"&dvdreleaseDate="+ddate+"&runtime="+runtime+"&boxOffice="+boxoffice+"&desc="+desc+"&genres="+genre_selected,
            complete: function (data) {
                var response = JSON.parse(data.responseText);
                if(response.success){
                    clearAllModal();
                    $("#editmediamodal").modal("hide");
                    $("#successModalCenter").modal("show");
                }
                else{
                    clearAllModal();
                    $("#editmediamodal").modal("hide");
                    $("#failModalCenter").modal("show");
                }
            }
        });
    }
    else{
        ID = title;
        console.log("EXISTING MEDIA MODIFY");
        $.ajax({
            url: "/editmedia",
            type: 'POST',
            data: "id="+ID+"&mediaName="+mediaName+"&parentID="+parentID+"&mediaType="+mediaType+"&mediaRating="+mediaRating+"&releaseDate="+rdate+"&dvdreleaseDate="+ddate+"&runtime="+runtime+"&boxOffice="+boxoffice+"&desc="+desc+"&genres="+genre_selected,
            complete: function (data) {
                var response = JSON.parse(data.responseText);
                if(response.success){
                    clearAllModal();
                    $("#editmediamodal").modal("hide");
                    $("#successModalCenter").modal("show");
                }
                else{
                    clearAllModal();
                    $("#editmediamodal").modal("hide");
                    $("#failModalCenter").modal("show");
                }
            }
        });
    }
}

function buttonHandler(x) {
    switch (x) {
        case "overview":
            $("#wts-movie-box").hide();
            $("#wts-tv-box").hide();
            $("#review-history-box").hide();
            $("#wts-list").hide();
            $("#ni-list").hide();
            $("#my-review-box").hide();
            $.ajax({
                // url: url,
                type: 'GET',
                dataType: 'html',
                complete: function () {
                    $('.users-tab button').attr("disabled", "");
                    var d1 = $("#load-page-bounce-user").show(0).delay(500).fadeOut();
                    $.when(d1).done(function () {
                        $('.users-tab button').removeAttr("disabled");
                    });
                    // load Stuff
                    $("#review-history-box h5:eq(0)").text("Review History");
                    $("#review-box .tv-middle-box-row").show();
                    $("#review-box").show();
                    //$("#want-to-see-movie-box").show();
                    //$("#want-to-see-tv-box").show();
                    $("#wts-movie-box").show();
                    $("#wts-tv-box").show();
                    $("#wts-list").show();
                    $("#ni-list").show();
                    $("#review-history-box").show();
                }
            });
            break;
        case "movie-reviews":
            $("#wts-movie-box").hide();
            $("#wts-tv-box").hide();
            $("#review-history-box").hide();
            $("#my-review-box").hide();
            $.ajax({
                // url: url,
                type: 'GET',
                dataType: 'html',
                complete: function () {
                    $('.users-tab button').attr("disabled", "");
                    var d1 = $("#load-page-bounce-user").show(0).delay(500).fadeOut();
                    $.when(d1).done(function () {
                        $('.users-tab button').removeAttr("disabled");
                    });
                    // load Stuff
                    $("#review-box .tv-middle-box-row:eq(0)").show();
                    $("#review-box .tv-middle-box-row:eq(1)").hide();
                    $("#review-history-box h5:eq(0)").text("Movie Reviews");
                    $("#tv-reviews").hide( );
                    $("#movie-reviews").show( );
                    $("#review-box").show();
                    $("#review-history-box").show();
                }
            });
            break;
        case "tv-reviews":
            $("#wts-movie-box").hide();
            $("#wts-tv-box").hide();
            $("#review-history-box").hide();
            $("#wts-movie-box").hide();
            $("#wts-tv-box").hide()
            $("#my-review-box").hide();;
            $.ajax({
                // url: url,
                type: 'GET',
                dataType: 'html',
                complete: function () {
                    $('.users-tab button').attr("disabled", "");
                    var d1 = $("#load-page-bounce-user").show(0).delay(500).fadeOut();
                    $.when(d1).done(function () {
                        $('.users-tab button').removeAttr("disabled");
                    });
                    // load Stuff
                    $("#review-box .tv-middle-box-row:eq(0)").hide();
                    $("#review-box .tv-middle-box-row:eq(1)").show();
                    $("#review-history-box h5:eq(0)").text("TV Reviews");
                    $("#tv-reviews").show();
                    $("#movie-reviews").hide( );
                    $("#review-history-box").show();
                }
            });
            break;
        case "wts-ni":
            $("#wts-movie-box").hide();
            $("#wts-tv-box").hide();
            $("#review-history-box").hide();
            $.ajax({
                // url: url,
                type: 'GET',
                dataType: 'html',
                complete: function () {
                    $('.users-tab button').attr("disabled", "");
                    var d1 = $("#load-page-bounce-user").show(0).delay(800).fadeOut();
                    $.when(d1).done(function () {
                        $('.users-tab button').removeAttr("disabled");
                    });
                    $("#want-to-see-tv-box").hide();
                    $("#want-to-see-movie-box").hide();
                    // load Stuff
                    $("#wts-movie-box h5:eq(0)").text("Want to See List");
                    $("#wts-tv-box h5:eq(0)").text("Not Interested List");
                    $("#wts-list").show();
                    $("#ni-list").show();
                    $("#wts-movie-box").show();
                    $("#wts-tv-box").show();
                }
            });
            break;
        case "my-critics":
            $("#wts-movie-box").hide();
            $("#wts-tv-box").hide();
            $("#review-history-box").hide();
            $.ajax({
                // url: url,
                type: 'GET',
                dataType: 'html',
                complete: function () {
                    $('.users-tab button').attr("disabled", "");
                    var d1 = $("#load-page-bounce-user").show(0).delay(500).fadeOut();
                    $.when(d1).done(function () {
                        $('.users-tab button').removeAttr("disabled");
                    });
                    $("#review-box").hide();
                    $("#review-history-box h5:eq(0)").text("My Critics List");
                    // $("#my-review-box").show();
                    $("#review-history-box").hide();
                }
            });
            break;
        default:
            break;
    }
}
