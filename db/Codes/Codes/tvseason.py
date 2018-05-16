from functions import *

def gettvseason():
    showlist = readID("showSeasons.csv")

    season = []
    screw = []
    sgenre = []
    seasonEpisodes = []

    for show in showlist:
        seasonlink1 = "https://www.rottentomatoes.com/tv/"
        seasonlink2 = show[0]  # show_id
        seasonlink3 = "/s"
        numSeason = int(show[1])  # show_seasons
        if "title_seasons" != numSeason and numSeason > 0:
            print numSeason
            for x in range(1, numSeason + 1):
                url = seasonlink1 + seasonlink2 + seasonlink3 + str(x)
                request = requests.get(url, 'HEAD')
                if request.status_code == 200:
                    print url
                    page = urllib2.urlopen(url)
                    soup = BeautifulSoup(page, "html5lib")
                    tv_title_headers = soup.find(id="tv_title_headers")
                    # SAVE SEASON PARENT ID
                    season_parent_id = seasonlink2
                    # TITLE_TYPE
                    title_type = "season"
                    # create season_id
                    season_id = seasonlink2 + "_s" + str(x)
                    # season_name
                    season_name = tv_title_headers.find("h1", class_="movie_title")
                    season_name = season_name.get_text()
                    season_name = season_name.split("(")[0]
                    season_name = cleanup(season_name)
                    # season number
                    season_number = x
                    # bio
                    mainColumn = soup.find(id="mainColumn")
                    movie_info = mainColumn.find("div", class_="movie_info")
                    movieSynopsis = movie_info.find(id="movieSynopsis")
                    bio = movieSynopsis.get_text()
                    bio = unicodedata.normalize('NFKD', unicode(bio)).encode('ascii', 'ignore')
                    bio = removeSpace(bio)
                    # info
                    season_network = ""
                    season_premieredate = ""
                    ul = movie_info.find("ul")
                    if ul is not None:
                        all_li = ul.find_all("li")
                        for li in all_li:
                            s = li.get_text()
                            if "Network" in s:
                                season_network = li.find(class_="meta-value").get_text()
                                season_network = removeSpace(
                                    unicodedata.normalize('NFKD', unicode(season_network)).encode('ascii', 'ignore'))
                            elif "Premiere" in s:
                                premieredate = li.find(class_="meta-value").get_text()
                                season_premieredate = removeSpace(
                                    unicodedata.normalize('NFKD', unicode(premieredate)).encode('ascii', 'ignore'))
                            elif "Genre" in s:
                                g = li.find(class_="meta-value").get_text()
                                row = [season_id,cleanup(g)]
                                sgenre.append(row)
                            elif "Creators" in s:
                                c = li.find(class_="meta-value")
                                link = c.find_all("a")
                                for l in link:
                                    c = l.get("href")
                                    c = c.split("/")[2]
                                    row = [season_id, cleanup(c), "Creator", ""]
                                    screw.append(row)
                            elif "Producers" in s:
                                c = li.find(class_="meta-value")
                                link = c.find_all("a")
                                for l in link:
                                    c = l.get("href")
                                    c = c.split("/")[2]
                                    row = [season_id, cleanup(c), "Producer", ""]
                                    screw.append(row)
                            elif "Written" in s:
                                c = li.find(class_="meta-value")
                                link = c.find_all("a")
                                for l in link:
                                    c = l.get("href")
                                    c = c.split("/")[2]
                                    row = [season_id, cleanup(c), "Writer", ""]
                                    screw.append(row)
                    # GET CAST FOR THE SEASON
                    # cast
                    castSection = soup.find("div", class_="castSection")
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
                                row = [season_id, link, "Cast", role]
                                screw.append(row)
                    # GET NUMBER OF EPISODES
                    season_episodes = 0
                    episodelistroot = mainColumn.find(id="episode-list-root")
                    if episodelistroot is not None:
                        episodeItem = episodelistroot.find_all("a")
                        season_episodes = len(episodeItem)

                    row = [season_id, season_parent_id, season_episodes]
                    seasonEpisodes.append(row)

                    # row = [season_id,season_parent_id,season_name,title_type,season_number,season_episodes,season_network,season_premieredate,bio]
                    # season.append(row)

                    row = [season_id, season_parent_id, "Season", season_name, bio, season_premieredate, "",
                           "", "", "", season_network]

                    season.append(row)

                    appendtocsv("title.csv", season)
                    appendtocsv("seasonEpisodes.csv", seasonEpisodes)
                    appendtocsv("titleGenre.csv", sgenre)
                    appendtocsv("titleCrew.csv", screw)

                    # FINALLY OPTIONAL DOWNLOAD MOVIE POSTER
                    imgsec = soup.find(id="movie-image-section")
                    if imgsec is not None:
                        center = imgsec.find(id="tvPosterLink")
                        if center is not None:
                            img = center.find("img")
                            if img is not None:
                                img = img.get("src")
                                downloadMoviePoster(img, "season", season_id, season_id)

                    season = []
                    seasonEpisodes = []
                    screw = []
                    sgenre = []
                    time.sleep(1)


#
# showlist = readID("showSeasons.csv")
#
# #season = []
# #row = ["title_id","title_parent_id","title_name","title_type","title_season_number","title_episodes","title_network","title_premieredate","title_bio"]
# #season.append(row)
# #exporttocsv("season_info.csv",season)
# season = []
# seasonEpisodes = []
# genre = []
# creator = []
# producers = []
# showCast =[]
# writer = []
#
# for show in showlist:
#     seasonlink1 = "https://www.rottentomatoes.com/tv/"
#     seasonlink2 = show[0]#show_id
#     seasonlink3 = "/s"
#     numSeason = int(show[1])#show_seasons
#     if "title_seasons" != numSeason and numSeason > 0:
#         print numSeason
#         for x in range(1,numSeason+1):
#             url = seasonlink1 + seasonlink2 + seasonlink3 + str(x)
#             request = requests.get(url,'HEAD')
#             if request.status_code == 200:
#                 print url
#                 page = urllib2.urlopen(url)
#                 soup = BeautifulSoup(page, "html5lib")
#                 tv_title_headers = soup.find(id="tv_title_headers")
#                 #SAVE SEASON PARENT ID
#                 season_parent_id = seasonlink2
#                 #TITLE_TYPE
#                 title_type = "season"
#                 #create season_id
#                 season_id = seasonlink2+"_s"+str(x)
#                 #season_name
#                 season_name = tv_title_headers.find("h1", class_="movie_title")
#                 season_name = season_name.get_text()
#                 season_name = season_name.split("(")[0]
#                 season_name = removeSpace(unicodedata.normalize('NFKD',unicode(season_name)).encode('ascii','ignore'))
#                 #season number
#                 season_number = x
#                 #bio
#                 mainColumn = soup.find(id="mainColumn")
#                 movie_info = mainColumn.find("div", class_="movie_info")
#                 movieSynopsis = movie_info.find(id="movieSynopsis")
#                 bio = movieSynopsis.get_text()
#                 bio = unicodedata.normalize('NFKD',unicode(bio)).encode('ascii','ignore')
#                 bio = removeSpace(bio)
#                 #info
#                 season_network = ""
#                 season_premieredate = ""
#                 ul = movie_info.find("ul")
#                 if ul is not None:
#                     all_li = ul.find_all("li")
#                     for li in all_li:
#                         s = li.get_text()
#                         if "Network" in s:
#                             season_network = li.find(class_="meta-value").get_text()
#                             season_network = removeSpace(unicodedata.normalize('NFKD',unicode(season_network)).encode('ascii','ignore'))
#                         elif "Premiere" in s:
#                             premieredate = li.find(class_="meta-value").get_text()
#                             season_premieredate = removeSpace(unicodedata.normalize('NFKD',unicode(premieredate)).encode('ascii','ignore'))
#                         elif "Genre" in s:
#                             g = li.find(class_="meta-value").get_text()
#                             row = [season_id,removeSpace(unicodedata.normalize('NFKD',unicode(g)).encode('ascii','ignore'))]
#                             genre.append(row)
#                         elif "Creators" in s:
#                             c = li.find(class_="meta-value")
#                             link = c.find_all("a")
#                             for l in link:
#                                 c = l.get("href")
#                                 c = c.split("/")[2]
#                                 row = [season_id,removeSpace(unicodedata.normalize('NFKD',unicode(c)).encode('ascii','ignore'))]
#                                 creator.append(row)
#                         elif "Producers" in s:
#                             c = li.find(class_="meta-value")
#                             link = c.find_all("a")
#                             for l in link:
#                                 c = l.get("href")
#                                 c = c.split("/")[2]
#                                 row = [season_id,removeSpace(unicodedata.normalize('NFKD',unicode(c)).encode('ascii','ignore'))]
#                                 producers.append(row)
#                         elif "Written" in s:
#                             c = li.find(class_="meta-value")
#                             link = c.find_all("a")
#                             for l in link:
#                                 c = l.get("href")
#                                 c = c.split("/")[2]
#                                 row = [season_id,removeSpace(unicodedata.normalize('NFKD',unicode(c)).encode('ascii','ignore'))]
#                                 writer.append(row)
#                 #GET CAST FOR THE SEASON
#                 # cast
#                 castSection = soup.find("div",class_="castSection")
#                 if castSection is not None:
#                     castitem = castSection.find_all(class_="cast-item")
#                     if castitem is not None:
#                         for item in castitem:
#                             link = item.find("a").get("href")
#                             link = link.split("/")[2]
#                             link = removeSpace(unicodedata.normalize('NFKD',unicode(link)).encode('ascii','ignore'))
#                             mediabody = item.find(class_="media-body")
#                             span = mediabody.find("span", class_="characters subtle smaller")
#                             role = ""
#                             if span is not None:
#                                 role = span.get_text()
#                                 role = role.split("as ")[1]
#                                 role = removeSpace(unicodedata.normalize('NFKD',unicode(role)).encode('ascii','ignore'))
#                             row = [season_id,link,role]
#                             showCast.append(row)
#                 #GET NUMBER OF EPISODES
#                 season_episodes = 0
#                 episodelistroot = mainColumn.find(id="episode-list-root")
#                 if episodelistroot is not None:
#                     episodeItem = episodelistroot.find_all("a")
#                     season_episodes = len(episodeItem)
#
#                 row = [season_id,season_parent_id,season_episodes]
#                 seasonEpisodes.append(row)
#
#                 # row = [season_id,season_parent_id,season_name,title_type,season_number,season_episodes,season_network,season_premieredate,bio]
#                 # season.append(row)
#
#                 row = [season_id, season_parent_id, "Season", season_name, bio, season_premieredate, "",
#                        "", "", "", season_network]
#
#                 season.append(row)
#
#                 appendtocsv("title.csv",season)
#                 appendtocsv("seasonEpisodes.csv",seasonEpisodes)
#                 appendtocsv("titleGenre.csv",genre)
#                 appendtocsv("titleCreator.csv",creator)
#                 appendtocsv("titleProducers.csv",producers)
#                 appendtocsv("titleCast.csv",showCast)
#                 appendtocsv("titleWriter.csv",writer)
#
#                 # FINALLY OPTIONAL DOWNLOAD MOVIE POSTER
#                 imgsec = soup.find(id="movie-image-section")
#                 if imgsec is not None:
#                     center = imgsec.find(id="tvPosterLink")
#                     if center is not None:
#                         img = center.find("img")
#                         if img is not None:
#                             img = img.get("src")
#                             downloadMoviePoster(img, "season", season_id, season_id)
#
#                 season = []
#                 genre = []
#                 creator = []
#                 producers = []
#                 showCast = []
#                 writer = []
#                 seasonEpisodes = []
#                 time.sleep(1)
