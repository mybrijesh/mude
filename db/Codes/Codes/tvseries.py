from functions import *

def gettvseries():
    series = []
    screw = []
    sgenre = []
    showSeasons = []

    slist = readID("showidfinal.csv")
    slist = [['game_of_thrones'], ["flash"]]

    url1 = "https://www.rottentomatoes.com/tv/"
    i = 0;
    for showid in slist:
        i = i + 1
        print i, showid

        show = showid[0]
        url = url1 + show
        request = requests.get(url, 'HEAD')
        if request.status_code == 200:
            page = urllib2.urlopen(url)
            soup = BeautifulSoup(page, "html5lib")
            # GET FRANCHISE_ID
            mainColumn = soup.find(id="main_container")
            h3 = mainColumn.find("h3", class_="bodyBlack hidden-xs")
            title_franchiseid = ""
            if h3 is not None:
                all_a = h3.find_all("a")
                a = all_a[1].get("href")
                a = a.split("/")
                a = a[2]
                a = cleanup(a)
                title_franchiseid = a
                # row = [show, a]
                # franchise.append(row)

            super_series_header = soup.find(id="super_series_header")
            super_series_header = removeSpace(super_series_header.get_text())
            showName = super_series_header.split("(")[0]
            # print showName

            # GET NUMBER OF SEASON
            seasonList = soup.find(id="seasonList")
            seasonList = seasonList.find_all(class_="seasonItem")
            numSeason = len(seasonList)
            row = [show, numSeason]
            showSeasons.append(row)
            # appendtocsv("series_info.csv",showSeasons)

            # SHOW BIO
            series_info = soup.find(id="series_info")
            movie_info = series_info.find(class_="movie_info")
            movieSynopsis = movie_info.find(id="movieSynopsis")
            bio = movieSynopsis.get_text()
            bio = cleanup(bio)

            # GET CREATOR AND STARRING
            subtle = movie_info.find_all("div", class_="")
            for sub in subtle:
                s = sub.get_text()
                if "Starring" in s:
                    all_a = sub.find_all("a")
                    for a in all_a:
                        link = a.get("href")
                        link = link.split("/")[2]
                        link = cleanup(link)
                        row = [show, link, "Starring", ""]
                        screw.append(row)
                        # row = [show, link]
                        # showStarring.append(row)
                        # print "STARRING: ",link
                elif "Creators" in s:
                    all_a = sub.find_all("a")
                    for a in all_a:
                        link = a.get("href")
                        link = link.split("/")[2]
                        link = cleanup(link)
                        row = [show, link, "Creator",""]
                        screw.append(row)
                        # print "CREATORS: ",link

            # SERIES DETAIL
            detail_panel = soup.find(id="detail_panel")
            table = detail_panel.find("table")
            all_tr = table.find_all("tr")
            tvnetwork = ""
            premieredate = ""
            genre = ""
            exeProducers = ""
            for tr in all_tr:
                s = tr.get_text()
                if "TV Network:" in s:
                    all_td = tr.find_all("td")
                    tvnetwork = all_td[1].get_text()
                elif "Premiere Date:" in s:
                    all_td = tr.find_all("td")
                    premieredate = all_td[1].get_text()
                elif "Genre:" in s:
                    all_td = tr.find_all("td")
                    genre = all_td[1].get_text()
                    genre = cleanup(genre)
                    row = [show, genre]
                    sgenre.append(row)
                elif "Executive Producers" in s:
                    all_td = tr.find_all("td")
                    all_a = all_td[1].find_all("a")
                    for a in all_a:
                        link = a.get("href")
                        link = link.split("/")[2]
                        link = cleanup(link)
                        row = [show, link, "Producer", ""]
                        screw.append(row)
                elif "Written" in s:
                    all_td = tr.find_all("td")
                    all_a = all_td[1].find_all("a")
                    for a in all_a:
                        link = a.get("href")
                        link = link.split("/")[2]
                        link = cleanup(link)
                        row = [show, link, "Writer", ""]
                        screw.append(row)
                elif "Directed" in s:
                    all_td = tr.find_all("td")
                    all_a = all_td[1].find_all("a")
                    for a in all_a:
                        link = a.get("href")
                        link = link.split("/")[2]
                        link = cleanup(link)
                        row = [show, link, "Director", ""]
                        screw.append(row)

            # cast
            fullCast = soup.find(id="fullCast")
            castSection = fullCast.find(class_="castSection")
            if castSection is not None:
                castitem = castSection.find_all(class_="cast-item")
                if castitem is not None:
                    for item in castitem:
                        link = item.find("a").get("href")
                        link = link.split("/")[2]
                        link = cleanup(link)
                        mediabody = item.find(class_="media-body")
                        span = mediabody.find("span", class_="characters subtle smaller")
                        role = ""
                        if span is not None:
                            role = span.get_text()
                            role = role.split("as ")[1]
                            role = cleanup(role)
                        row = [show, link, "Cast", role]
                        screw.append(row)

                # print link

            # row = [show,showName,"tvshow",tvnetwork,premieredate,numSeason,bio]
            row = [show, title_franchiseid, "TVShow", showName, bio, premieredate, "",
                   "", "", "", tvnetwork]
            series.append(row)

            appendtocsv("title.csv", series)
            appendtocsv("titleCrew.csv", screw)
            appendtocsv("titleGenre.csv", sgenre)
            appendtocsv("showSeasons.csv", showSeasons)

            showSeasons = []
            series = []
            screw = []
            sgenre = []

            # FINALLY OPTIONAL DOWNLOAD TV POSTER
            imgsec = soup.find(id="tv-image-section")
            if imgsec is not None:
                img = imgsec.find("img")
                if img is not None:
                    img = img.get("src")
                    downloadMoviePoster(img, "series", show, show)

        else:
            print "SHOW: ", show, " COULD NOT BE FOUND"
        time.sleep(1)


# series = []
# showSeasons = []
# franchise = []
# showCreator = []
# showGenre = []
# showProducers = []
# showStarring = []
# showCast = []
# writer = []
# director = []
#
# #slist = readID("showidfinal.csv")
# #slist = ['game_of_thrones','the_big_bang_theory','altered_carbon','flash','daredevil']
# slist = [['game_of_thrones'],["flash"]]
#
# url1 = "https://www.rottentomatoes.com/tv/"
# i = 0;
# for showid in slist:
#     i = i + 1
#     print i, showid
#     #CLEAR ALL LIST
#     series = []
#     showSeasons = []
#     showCreator = []
#     showGenre = []
#     showProducers = []
#     showStarring = []
#     showCast = []
#     franchise = []
#     writer = []
#     director = []
#
#     show = showid[0]
#     url = url1 + show
#     request = requests.get(url,'HEAD')
#     if request.status_code == 200:
#         page = urllib2.urlopen(url)
#         soup = BeautifulSoup(page, "html5lib")
#         #GET FRANCHISE_ID
#         mainColumn = soup.find(id="main_container")
#         h3 = mainColumn.find("h3", class_="bodyBlack hidden-xs")
#         title_franchiseid = ""
#         if h3 is not None:
#             all_a = h3.find_all("a")
#             a = all_a[1].get("href")
#             a = a.split("/")
#             a = a[2]
#             a = cleanup(a)
#             title_franchiseid = a
#             row = [show,a]
#             franchise.append(row)
#
#         super_series_header = soup.find(id="super_series_header")
#         super_series_header = removeSpace(super_series_header.get_text())
#         showName = super_series_header.split("(")[0]
#         #print showName
#
#         #GET NUMBER OF SEASON
#         seasonList = soup.find(id="seasonList")
#         seasonList = seasonList.find_all(class_="seasonItem")
#         numSeason = len(seasonList)
#         row = [show,numSeason]
#         showSeasons.append(row)
#         #appendtocsv("series_info.csv",showSeasons)
#
#         #SHOW BIO
#         series_info = soup.find(id="series_info")
#         movie_info = series_info.find(class_="movie_info")
#         movieSynopsis = movie_info.find(id="movieSynopsis")
#         bio = movieSynopsis.get_text()
#         bio = cleanup(bio)
#
#         #GET CREATOR AND STARRING
#         subtle = movie_info.find_all("div",class_="")
#         for sub in subtle:
#             s = sub.get_text()
#             if "Starring" in s:
#                 all_a = sub.find_all("a")
#                 for a in all_a:
#                     link = a.get("href")
#                     link = link.split("/")[2]
#                     link = cleanup(link)
#                     row = [show,link]
#                     showStarring.append(row)
#                     #print "STARRING: ",link
#             elif "Creators" in s:
#                 all_a = sub.find_all("a")
#                 for a in all_a:
#                     link = a.get("href")
#                     link = link.split("/")[2]
#                     link = cleanup(link)
#                     row = [show,link]
#                     showCreator.append(row)
#                     #print "CREATORS: ",link
#
#         #SERIES DETAIL
#         detail_panel = soup.find(id="detail_panel")
#         table = detail_panel.find("table")
#         all_tr = table.find_all("tr")
#         tvnetwork = ""
#         premieredate = ""
#         genre = ""
#         exeProducers = ""
#         for tr in all_tr:
#             s = tr.get_text()
#             if "TV Network:" in s:
#                 all_td = tr.find_all("td")
#                 tvnetwork = all_td[1].get_text()
#             elif "Premiere Date:" in s:
#                 all_td = tr.find_all("td")
#                 premieredate = all_td[1].get_text()
#             elif "Genre:" in s:
#                 all_td = tr.find_all("td")
#                 genre = all_td[1].get_text()
#                 genre = cleanup(genre)
#                 row = [show,genre]
#                 showGenre.append(row)
#             elif "Executive Producers" in s:
#                 all_td = tr.find_all("td")
#                 all_a = all_td[1].find_all("a")
#                 for a in all_a:
#                     link = a.get("href")
#                     link = link.split("/")[2]
#                     link = cleanup(link)
#                     row = [show,link]
#                     showProducers.append(row)
#             elif "Written" in s:
#                 all_td = tr.find_all("td")
#                 all_a = all_td[1].find_all("a")
#                 for a in all_a:
#                     link = a.get("href")
#                     link = link.split("/")[2]
#                     link = cleanup(link)
#                     row = [show, link]
#                     writer.append(row)
#             elif "Directed" in s:
#                 all_td = tr.find_all("td")
#                 all_a = all_td[1].find_all("a")
#                 for a in all_a:
#                     link = a.get("href")
#                     link = link.split("/")[2]
#                     link = cleanup(link)
#                     row = [show, link]
#                     director.append(row)
#
#
#         # cast
#         fullCast = soup.find(id="fullCast")
#         castSection = fullCast.find(class_="castSection")
#         if castSection is not None:
#             castitem = castSection.find_all(class_="cast-item")
#             if castitem is not None:
#                 for item in castitem:
#                     link = item.find("a").get("href")
#                     link = link.split("/")[2]
#                     link = cleanup(link)
#                     mediabody = item.find(class_="media-body")
#                     span = mediabody.find("span", class_="characters subtle smaller")
#                     role = ""
#                     if span is not None:
#                         role = span.get_text()
#                         role = role.split("as ")[1]
#                         role = cleanup(role)
#                     row = [show,link,role]
#                     showCast.append(row)
#
#             #print link
#
#
#         #row = [show,showName,"tvshow",tvnetwork,premieredate,numSeason,bio]
#         row = [show, title_franchiseid, "TVShow", showName, bio, premieredate, "",
#                "", "", "", tvnetwork]
#         series.append(row)
#
#         appendtocsv("title.csv",series)
#         appendtocsv("showSeasons.csv",showSeasons)
#         appendtocsv("titleProducers.csv",showProducers)
#         appendtocsv("titleGenre.csv",showGenre)
#         appendtocsv("titleCreator.csv",showCreator)
#         appendtocsv("titleStarring.csv",showStarring)
#         appendtocsv("titleCast.csv",showCast)
#         appendtocsv("titleWriter.csv",writer)
#         appendtocsv("titleDirector.csv",director)
#         appendtocsv("titlefranchise.csv", franchise)
#
#         # FINALLY OPTIONAL DOWNLOAD TV POSTER
#         imgsec = soup.find(id="tv-image-section")
#         if imgsec is not None:
#             img = imgsec.find("img")
#             if img is not None:
#                 img = img.get("src")
#                 downloadMoviePoster(img, "series", show, show)
#
#     else:
#         print "SHOW: ",show, " COULD NOT BE FOUND"
#     time.sleep(1)
# '''
# print series
# print showProducers
# print showGenre
# print showCreator
# print showStarring
# print showCast[1]'''
