$(function () {
  inTheaters();
  searchEvent();

  $(".main").owlCarousel({
    center: true,
    items: 2,
    loop: true,
    margin: 80,
    autoWidth: true,
    autoplay: true,
    autoplayTimeout: 4000,
    autoplayHoverPause: true,
    responsive: {
      1000: {
        items: 4
      }
    }
  });

    $( "#userProfileOptions" ).hover(function() {
        console.log("test");
        $.ajax({
            url: "/mouseoverprofile",
            type: "GET",
            complete: function(data){
                var response = JSON.parse(data.responseText)
                if(response.success){
                    console.log(response);
                    $("#nav-wts-num").text(response.wtsCnt);
                    $("#nav-notinterested-num").text(response.niCnt);
                    $("#nav-reviewcount-num").text(response.rCnt);
                }
            }
        });
    });

  $(".main").on('mouseleave', function () {
    $(".main").trigger('stop.owl.autoplay');
    $(".main").trigger('play.owl.autoplay', [4000]);
  });

  $(window).on('resize', function () {
    if (window.innerWidth > 768) $('.collapse').collapse('hide');
  });

  $('.navbar-toggler').focusout(function () {
    $('.collapse').collapse('hide');
  });

  $(window).scroll(function () {
    if ($(this).width() < 768) {
      if ($(this).scrollTop() > 15) {
        $('#navbar-second nav').removeClass('bg-dark');
        $('#navbar-second nav').css('background-color', 'black');
        $('#navbar-second nav').css('opacity', '0.8');
        $('#navbar-second .collapse').css('opacity', '0.8');
      }
      if ($(this).scrollTop() < 15) {
        $('#navbar-second nav').addClass('bg-dark');
        $('#navbar-second nav').css('opacity', '1');
        $('#navbar-second .collapse').css('opacity', '1');
      }
    }
    else {
      $('#navbar-second nav').addClass('bg-dark');
      $('#navbar-second nav').css('opacity', '1');
      $('#navbar-second .collapse').css('opacity', '1');
    }
  });

  $('#meterModalCenter').on('show.bs.modal', function () {
    $('.container-fluid').addClass('fixShaking');
    $('.container-fluid').addClass('blur');
  });

  $('#meterModalCenter').on('hide.bs.modal', function () {
    $('.container-fluid').removeClass('blur');
    $('.container-fluid').removeClass('fixShaking');
  });

  $('#logInModalCenter').on('show.bs.modal', function () {
    $('.container-fluid').addClass('fixShaking');
    $('.container-fluid').addClass('blur');
  });

  $('#logInModalCenter').on('hide.bs.modal', function () {
    $(".needs-validation").removeClass('was-validated');
    $(this).removeClass('failed');
    $('.container-fluid').removeClass('blur');
    $('.container-fluid').removeClass('fixShaking');
  });

  $('#signUpModalCenter').on('show.bs.modal', function () {
    $('.container-fluid').addClass('fixShaking');
    $('.container-fluid').addClass('blur');
  });

  $('#signUpModalCenter').on('hide.bs.modal', function () {
    $(".needs-validation").removeClass('was-validated');
    $(this).removeClass('failed');
    $('.container-fluid').removeClass('blur');
    $('.container-fluid').removeClass('fixShaking');
  });

  $('#forgotModalCenter').on('show.bs.modal', function () {
    $('.container-fluid').addClass('fixShaking');
    $('.container-fluid').addClass('blur');
  });

  $('#forgotModalCenter').on('hide.bs.modal', function () {
    $(".needs-validation").removeClass('was-validated');
    $(this).removeClass('failed');
    $('.container-fluid').removeClass('blur');
    $('.container-fluid').removeClass('fixShaking');
  });

  $("#reSendModalCenter").on('show.bs.modal', function () {
    $('.container-fluid').addClass('fixShaking');
    $('.container-fluid').addClass('blur');
  });

  $("#reSendModalCenter").on('hide.bs.modal', function () {
    $(".needs-validation").removeClass('was-validated');
    $(this).removeClass('failed');
    $('.container-fluid').removeClass('blur');
    $('.container-fluid').removeClass('fixShaking');
  });

  $('#successModalCenter').on('show.bs.modal', function () {
    $('.container-fluid').addClass('fixShaking');
    $('.container-fluid').addClass('blur');
  });

  $('#successModalCenter').on('hide.bs.modal', function () {
    $(".needs-validation").removeClass('was-validated');
    $('.container-fluid').removeClass('blur');
    $('.container-fluid').removeClass('fixShaking');
  });

  $('[data-toggle="tooltip"]').tooltip();

  var images3 = ['ad-3.png', 'ad-3-1.png', 'ad-3-2.png', 'ad-3-3.png'];
  $("#ad3").attr('src', 'images/' + images3[Math.floor(Math.random() * images3.length)]);
  $("#ad5").attr('src', 'images/' + images3[Math.floor(Math.random() * images3.length)]);

  var images4 = ['ad-1.png', 'ad-1-1.png', 'ad-1-2.png'];
  $("#ad4").attr('src', 'images/' + images4[Math.floor(Math.random() * images4.length)]);

  $("#top-search-bar").val("");

  $("#top-search-bar").focusout(function () {
    if (!$(this).val) {
      inTheaters();
      $("#movie-heading").hide();
      $("#artist-heading").hide();
      $(".dropdown-item").hide();
      $("#top-movie-heading").show();
      $(".dropdown-item.top-item").show();
    }
  });

  $("#signUpModalCenter").on("keypress", function (e) {
    if (e.which == 13) {
      if (!validate("#signUpModalCenter")) {
        $(this).removeClass("failed");
        $(this).addClass("failed");
        return;
      }
      signup();
      $(this).removeClass("failed");
      $('#signUpModalCenter').modal('toggle');
    }
  });
  
  $("#logInModalCenter").on("keypress", function (e) {
    if (e.which == 13) {
      if (!validate("#logInModalCenter")) {
        $(this).removeClass("failed");
        $(this).addClass("failed");
        return;
      }
      login();
      $(this).removeClass("failed");
      $('#logInModalCenter').modal('toggle');
    }
  });

  $('#top-search-bar-box').on({
    "shown.bs.dropdown": function () { $(this).attr('closable', false); },
    "hide.bs.dropdown": function () { return $(this).attr('closable') == 'true'; }
  });

  $('#top-search-bar-box').children().first().on({
    "focusout": function () {
      $(this).parent().attr('closable', true);
    }
  });

  if ($(".first-name:eq(0)").text() !== "No-User") {
    $(".signIn-label").addClass("d-none");
    $(".signUp-label").addClass("d-none");
    $(".user-tab").removeClass("d-none");
  }

  if ($('#carouselExampleControls').length) {
    var $videoSrc;
    var $videoIndex;
    var $videoIndexMax = $('.video-btn.active').length;

    if ($('.video-btn.active').length <= 1)
      $('#carouselExampleControls a').hide();

    $('.video-btn').click(function () {
      $videoSrc = $(this).data("src");
      $videoIndex = $(this).data("index");
    });

    $('.carousel').on('slid.bs.carousel	', function () {
      $('.carousel').carousel('pause');
      for (i = 0; i < parseInt($videoIndexMax); i++) {
        $('.carousel-item:eq(' + i + ')').find('iframe').attr('src', $('.video-btn.active:eq(' + i + ')').data("src"));
      }
    });

    $('#myModal').on('shown.bs.modal', function (e) {
      $('.container-fluid').addClass('light-off');
      $('.carousel').carousel(parseInt($videoIndex));
    });

    $('#myModal').on('hide.bs.modal', function (e) {
      $('.container-fluid').removeClass('light-off');
      for (i = 0; i < parseInt($videoIndexMax); i++) {
        $('.carousel-item:eq(' + i + ')').find('iframe').attr('src', $('.video-btn.active:eq(' + i + ')').data("src"));
      }
    });

    initCarousel($videoIndexMax);
  }
  $('section').removeClass('d-none');
  $('footer').removeClass('d-none');
});

function validate(id) {
  var forms = $(id + " .needs-validation");
  var b;
  var validation = Array.prototype.filter.call(forms, function (form) {
    if (form.checkValidity() === false) {
      event.preventDefault();
      event.stopPropagation();
      form.classList.add('was-validated');
      b = false;
    } else {
      b = true;
    }
  });
  return b;
}

function login() {
  if (!validate("#logInModalCenter")) {
    $("#logInModalCenter").removeClass("failed");
    $("#logInModalCenter").addClass("failed");
    return false;
  }
  $("#logInModalCenter").removeClass("failed");
  $("#cover-modal").modal('show');
  $.ajax({
    url: "/signin",
    type: "POST",
    data: "email=" + $("#logInEmail").val() + "&password=" + $("#logInPassword").val(),
    complete: function (xhr, status) {
      if (xhr.responseText) {
        var response = JSON.parse(xhr.responseText);
        if (response.result) {
          var member = response.member;
          $(".first-name").text(member.name.firstName);
          $(".last-name").text(member.name.lastName);
          $("#logInEmail").val("");
          $("#logInPassword").val("");
          $(".signIn-label").addClass("d-none");
          $(".signUp-label").addClass("d-none");
          $(".nav-id").attr("href", "/member?memberID=" + member.id);
          $("#nav-wts").attr("href", "/member?memberID=" + member.id + "&tab=" + 0);
          $("#nav-mReview").attr("href", "/member?memberID=" + member.id + "&tab=" + 1);
          $("#nav-tvReview").attr("href", "/member?memberID=" + member.id + "&tab=" + 2);
          $("#nav-setting").attr("href", "/member?memberID=" + member.id + "&tab=" + 3);
          $(".user-tab").removeClass("d-none");    
          enableReview();
          $("#cover-modal").modal('hide');
          $("#successModalCenter").modal("show");
          window.location.reload(true);
          return;
        }
      }
      $("#cover-modal").modal('hide');
      $("#failModalCenter").modal("show");
    }
  });
}

function reSend() {
  if (!validate("#reSendModalCenter")) {
    $("#reSendModalCenter").removeClass("failed");
    $("#reSendModalCenter").addClass("failed");
    return false;
  }
  $("#reSendModalCenter").removeClass("failed");
  $("#cover-modal").modal('show');
  $.ajax({
    url: "/resendverification",
    type: "POST",
    data: "&email=" + $("#reSendEmail").val(),
    complete: function (xhr, status) {
      if (xhr.responseText) {
        var response = JSON.parse(xhr.responseText);
        if (response.result) {
          $("#cover-modal").modal('hide');
          $("#reSendEmail").val("");
          $("#successModalCenter").modal("show");
          return;
        }
      }
      $("#cover-modal").modal('hide');
      $("#failModalCenter").modal("show");
    }
  });
}

function forgot() {
  if (!validate("#forgotModalCenter")) {
    $("#forgotModalCenter").removeClass("failed");
    $("#forgotModalCenter").addClass("failed");
    return false;
  }
  $("#forgotModalCenter").removeClass("failed");
  $("#cover-modal").modal('show');
  $.ajax({
    url: "/forgotpwd",
    type: "POST",
    data: "&email=" + $("#forgotEmail").val(),
    complete: function (xhr, status) {
      if (xhr.responseText) {
        var response = JSON.parse(xhr.responseText);
        if (response.success) {
          $("#cover-modal").modal('hide');
          $("#forgotEmail").val("");
          $("#successModalCenter").modal("show");
          return;
        }
      }
      $("#cover-modal").modal('hide');
      $("#failModalCenter").modal("show");
    }
  });
}

function signup() {
  if (!validate("#signUpModalCenter")) {
    $("#signUpModalCenter").removeClass("failed");
    $("#signUpModalCenter").addClass("failed");
    return false;
  }
  $("#signUpModalCenter").removeClass("failed");
  $("#cover-modal").modal('show');
  $.ajax({
    url: "/signup",
    type: "POST",
    data: "firstName=" + $("#signUpFirst").val() + "&lastName=" + 
    $("#signUpLast").val() + "&email=" + $("#signUpEmail").val() + 
    "&password=" + $("#signUpPassword").val(),
    complete: function (xhr, status) {
      if (xhr.responseText) {
        var response = JSON.parse(xhr.responseText);
        if (response.result) {
          $("#cover-modal").modal('hide');
          $("#signUpFirst").val("");
          $("#signUpLast").val("");
          $("#signUpEmail").val("");
          $("#signUpPassword").val("");
          $("#successModalCenter").modal("show");
          return;
        }
      }
      $("#cover-modal").modal('hide');
      $("#failModalCenter").modal("show");
    }
  });
}

function logout() {
  $.ajax({
    url: "/signout",
    type: "POST",
    complete: function (xhr, status) {
      window.location.href = "/"; //redirect to index.html
      $(".signIn-label").removeClass("d-none");
      $(".signUp-label").removeClass("d-none");
      $(".user-tab").addClass("d-none");
      $(".first-name").text("No-User");
      $(".last-name").text("No-User");
    }
  });
}

function inTheaters() {
  $.ajax({
    url: "/intheatrenow",
    type: "GET",
    complete: function (xhr, status) {
      if (xhr.responseText) {
        var response = JSON.parse(xhr.responseText);
        if (response.success) {
          var count = 0;
          for (var review in response.inTheatreNow) {
            if (count === 0) {
              $(".dropdown-item.top-item").not(".dropdown-item.top-item:eq(0)").remove();
              $(".dropdown-item.top-item:eq(0)").attr("href", "/movie?mediaID=" + 
              response.inTheatreNow[review].id);
              $(".dropdown-item.top-item:eq(0) img").attr("src", "http://23.94.27.164/" + response.inTheatreNow[review].resourcePath + "/photos/0.jpg");
              $(".dropdown-item.top-item:eq(0) img").attr("onerror", "this.onerror=null; this.src='images/poster.png';");
              $(".dropdown-item.top-item:eq(0) h6 b").text(response.inTheatreNow[review].mediaName);
              $(".dropdown-item.top-item:eq(0) h6 span").text("(" + 
              response.inTheatreNow[review].releaseDate.theatre.substring(0, 4) + ")");
            }
            else {
              $(".dropdown-item.top-item:eq(0)").clone().appendTo(".top-box").each(function () {
                $(this).attr("href", "/movie?mediaID=" +
                  response.inTheatreNow[review].id);
                $(this).find("img").attr("src", "http://23.94.27.164/" + response.inTheatreNow[review].resourcePath + "/photos/0.jpg");
                $(this).find("img").attr("onerror", "this.onerror=null; this.src='images/poster.png';");
                $(this).find("b").text(response.inTheatreNow[review].mediaName);
                $(this).find("span").text("(" +
                  response.inTheatreNow[review].releaseDate.theatre.substring(0, 4) + ")");
              });
            }
            count++;
            if (count >= 5)
              break;
          }
        }
      }
    }
  });
}

function searchEvent() {
  $("#top-search-bar").keyup(function () {
    function response() {
      if (!valThis) {
        inTheaters();
        $("#top-movie-heading").show();
        $(".dropdown-item.top-item").show();
        $("#tv-heading").hide();
        $("#movie-heading").hide();
        $("#artist-heading").hide();
        $(".dropdown-item.tv-item").hide();
        $(".dropdown-item.movie-item").hide();
        $(".dropdown-item.artist-item").hide();
        $("#load-page-bounce-search").fadeOut();
        return true;
      }

      $.ajax({
        url: "/searchbytyping",
        type: "GET",
        data: "keyword=" + valThis,
        complete: function (xhr, status) {
          if (xhr.responseText) {
            var response = JSON.parse(xhr.responseText);
            if (response.success) {
              var t = response.seasons;
              var m = response.movies;
              var a = response.artists;
              var f_count = 0;
              var m_count = 0;
              var t_count = 0;
              $(".dropdown-item.movie-item").not(".dropdown-item.movie-item:eq(0)").remove();
              $(".dropdown-item.tv-item").not(".dropdown-item.tv-item:eq(0)").remove();
              $("#tv-heading").hide();
              $("#movie-heading").hide();
              $("#artist-heading").hide();
              $(".dropdown-item.movie-item").hide();
              $(".dropdown-item.tv-item").hide();
              if (m) {
                for (i = 0; i < m.length; i++) {
                  $("#movie-heading").show();
                  $(".dropdown-item.movie-item").show().css("display", "flex");
                  if (m_count == 0) {
                    $(".dropdown-item.movie-item").attr("href", "/movie?mediaID=" + m[i].id);
                    $(".dropdown-item.movie-item h6 b").text(m[i].mediaName);
                    $(".dropdown-item.movie-item h6 span").text("(" +
                      m[i].releaseDate.theatre.substring(0, 4) + ")");
                    $(".dropdown-item.movie-item").find("img").attr("src", "http://23.94.27.164/" + m[i].resourcePath + "/photos/0.jpg");
                    $(".dropdown-item.movie-item").find("img").attr("onerror", "this.onerror=null; this.src='images/poster.png';");
                    m_count++;
                  }
                  else {
                    $(".dropdown-item.movie-item" + ":eq(0)").clone().appendTo(".movie-box").each(function () {
                      $(this).attr("href", "/movie?mediaID=" + m[i].id);
                      $(this).find("b").text(m[i].mediaName);
                      $(this).find("span").text("(" +
                        m[i].releaseDate.theatre.substring(0, 4) + ")");
                      $(this).find("img").attr("src", "http://23.94.27.164/" + m[i].resourcePath + "/photos/0.jpg");
                      $(this).find("img").attr("onerror", "this.onerror=null; this.src='images/poster.png';");
                      m_count++;
                    });
                  }  
                }
              }
              if (t) {
                for (k = 0; k < t.length; k++) {
                  $("#tv-heading").show();
                  $(".dropdown-item.tv-item").show().css("display", "flex");
                  if (t_count == 0) {
                    $(".dropdown-item.tv-item").attr("href", "/season?mediaID=" + t[k].id);
                    $(".dropdown-item.tv-item h6 b").text(t[k].mediaName);
                    $(".dropdown-item.tv-item h6 span").text("(" +
                      t[k].releaseDate.theatre.substring(0, 4) + ")");
                    $(".dropdown-item.tv-item").find("img").attr("src", "http://23.94.27.164/" + t[k].resourcePath + "/photos/0.jpg");
                    $(".dropdown-item.tv-item").find("img").attr("onerror", "this.onerror=null; this.src='images/poster.png';");
                    t_count++;
                  }
                  else {
                    $(".dropdown-item.tv-item" + ":eq(0)").clone().appendTo(".tv-box").each(function () {
                      $(this).attr("href", "/season?mediaID=" + t[k].id);
                      $(this).find("b").text(t[k].mediaName);
                      $(this).find("span").text("(" +
                        t[k].releaseDate.theatre.substring(0, 4) + ")");
                      $(this).find("img").attr("src", "http://23.94.27.164/" + t[k].resourcePath + "/photos/0.jpg");
                      $(this).find("img").attr("onerror", "this.onerror=null; this.src='images/poster.png';");
                      t_count++;
                    });
                  }
                }
              }
              if (a) {
                $("#artist-heading").show();
                $(".dropdown-item.artist-item").not(".dropdown-item.artist-item:eq(0)").remove();
                $(".dropdown-item.artist-item").show().css("display", "flex");
                for (j = 0; j < a.length; j++) {
                  if (j == 0) {
                    $(".dropdown-item.artist-item").attr("href", "/artist?artistID=" + a[j].id);
                    $(".dropdown-item.artist-item h6 b").text(a[j].name.firstName + " " + a[j].name.lastName);
                    $(".dropdown-item.artist-item").find("img").attr("src", "http://23.94.27.164/" + a[j].resourcePath + "/photos/0.jpg");
                    $(".dropdown-item.artist-item").find("img").attr("onerror", "this.onerror=null; this.src='images/critics.gif';");
                  }
                  else {
                    $(".dropdown-item.artist-item" + ":eq(0)").clone().appendTo(".artist-box").each(function () {
                      $(this).attr("href", "/artist?artistID=" + a[j].id);
                      $(this).find("b").text(a[j].name.firstName + " " + a[j].name.lastName);
                      $(this).find("img").attr("src", "images/c" + (j + 1) + ".jpg");
                      $(this).find("img").attr("src", "http://23.94.27.164/" + a[j].resourcePath + "/photos/0.jpg");
                      $(this).find("img").attr("onerror", "this.onerror=null; this.src='images/critics.gif';");
                    });
                  }
                }
              }
              $("#top-movie-heading").hide();
              $(".dropdown-item.top-item").hide();
              $("#load-page-bounce-search").fadeOut();
            }
          }
        }
      });
    }
    var valThis = $(this).val();
    $("#load-page-bounce-search").show();
    if (typeof searchTimeout === 'undefined') {
      searchTimeout = setTimeout(response, 500);
    } else {
      clearTimeout(searchTimeout);
      searchTimeout = setTimeout(response, 500);
    }
  });
}

function trimString(x, y) {
  var string = x;
  var length = y;
  var cut = string.indexOf(' ', length);
  if (string.length <= length)
    return string;
  if (cut == -1) {
    if (string[string.length - 1] == ',')
      return string.slice(0, -1) + "...";  
    if (string[string.length - 1] == '.')
      return string;  
    return string + "...";  
  }
  cut = string.substring(0, cut);
  if (cut[cut.length - 1] == ',')
    return cut.slice(0, -1) + "...";
  if (cut[cut.length - 1] == '.')
    return cut;  
  return cut + "...";
}

function getParameterByName(name, url) {
  if (!url) url = window.location.href;
  name = name.replace(/[\[\]]/g, "\\$&");
  var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
    results = regex.exec(url);
  if (!results) return null;
  if (!results[2]) return '';
  return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function enableReview() {
  if ($(".review-box-big"))
    $(".review-box-big").css("opacity", "1");
  if ($(".review-box-small"))
    $(".review-box-small").css("opacity", "1");
  if ($(".first-name:eq(0)").text() !== "No-User" && getParameterByName("mediaID") !== null)  
    myReviewAndWTS();
  $(".review-cover").hide();  
}

function dateToMonthDay(date) {
  const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
  ];

  var d = new Date(date);
  return monthNames[d.getMonth()] + " " + d.getDate();
}

function initCarousel(max) {
  $('.carousel-item:eq(0)').find('iframe').attr('src', $('.video-btn.active:eq(' + 0 + ')').data("src"));
  for (i = 1; i < max; i++) {
    $('.carousel-item:eq(0)').clone().appendTo('.carousel-inner').each(function () {
      $(this).removeClass('active');
      $(this).find('iframe').attr('src', $('.video-btn.active:eq(' + i + ')').data("src"));
    });
  }

  $('.carousel').carousel();
  $('.carousel').carousel('pause');
}