from functions import *

def gettvepisode():
    showlist = readID("seasonEpisodes.csv")

    episode = []
    egenre = []
    ecrew = []

    for show in showlist:
        seasonlink1 = "https://www.rottentomatoes.com/tv/"
        seasonlink2 = show[1]  # show_id
        seasonlink3 = "/s"
        seasonlink4 = show[0].split(seasonlink2)  # season number
        seasonlink4 = seasonlink4[1].split("_s")
        seasonlink4 = seasonlink4[1]
        print seasonlink4
        seasonlink5 = "/e"

        numEpisode = int(show[2])  # number of episode in the season
        if "title_episodes" != numEpisode:
            for episodeNum in range(1, numEpisode + 1):
                url = seasonlink1 + seasonlink2 + seasonlink3 + seasonlink4 + seasonlink5 + str(episodeNum)
                print url
                page = urllib2.urlopen(url)
                soup = BeautifulSoup(page, "html5lib")
                # CREATE EPISODE ID
                title_id = seasonlink2 + "_s" + seasonlink4 + "_e" + str(episodeNum)
                # GET SEASON ID = PARENT
                title_parent_id = show[0]
                # GET SHOW ID
                title_show_id = seasonlink2
                # GET EPISODE NAME
                tv_title_headers = soup.find(id="tv_title_headers")
                movie_title = tv_title_headers.find("h1", class_="movie_title")
                title_name = movie_title.get_text()
                title_name = cleanup(title_name)
                # SET TITLE_TYPE
                title_type = "episode"
                # GET title_episode_number
                title_episode_number = episodeNum
                # BIO
                mainColumn = soup.find(id="mainColumn")
                movieSynopsis = mainColumn.find(id="movieSynopsis")
                bio = movieSynopsis.get_text()
                bio = cleanup(bio)
                # CONTENT-BODY

                title_network = ""
                title_airdate = ""
                ul = mainColumn.find("ul", class_="content-meta info")
                all_li = ul.find_all("li")
                for li in all_li:
                    s = li.get_text()
                    if "Network" in s:
                        season_network = li.find(class_="meta-value").get_text()
                        title_network = cleanup(season_network)
                    elif "Air Date" in s:
                        premieredate = li.find(class_="meta-value").get_text()
                        title_airdate = cleanup(premieredate)
                    elif "Genre" in s:
                        g = li.find(class_="meta-value").get_text()
                        row = [title_id, cleanup(g)]
                        egenre.append(row)
                    elif "Creator" in s:
                        c = li.find(class_="meta-value")
                        link = c.find_all("a")
                        for l in link:
                            c = l.get("href")
                            c = c.split("/")[2]
                            row = [title_id, cleanup(c), "Creator", ""]
                            ecrew.append(row)
                    elif "Producers" in s:
                        c = li.find(class_="meta-value")
                        link = c.find_all("a")
                        for l in link:
                            c = l.get("href")
                            c = c.split("/")[2]
                            row = [title_id, cleanup(c), "Producer", ""]
                            ecrew.append(row)
                    elif "Written" in s:
                        c = li.find(class_="meta-value")
                        link = c.find_all("a")
                        for l in link:
                            c = l.get("href")
                            c = c.split("/")[2]
                            row = [title_id, cleanup(c), "Writer", ""]
                            ecrew.append(row)
                    elif "Directed" in s:
                        c = li.find(class_="meta-value")
                        link = c.find_all("a")
                        for l in link:
                            c = l.get("href")
                            c = c.split("/")[2]
                            row = [title_id, cleanup(c), "Director", ""]
                            ecrew.append(row)
                # GET CAST FOR THE EPISODE
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
                            row = [title_id, link, "Cast", role]
                            ecrew.append(row)

                # row = [title_id, title_parent_id, title_show_id, title_name, title_type, title_episode_number, title_network, title_airdate, bio]
                row = [title_id, title_parent_id, "Episode", title_name, bio, title_airdate, "",
                       "", "", "", title_network]
                episode.append(row)
                appendtocsv("title.csv", episode)
                appendtocsv("titleGenre.csv", egenre)
                appendtocsv("titleCrew.csv", ecrew)

                # FINALLY OPTIONAL DOWNLOAD TV POSTER
                imgsec = soup.find(id="tv-image-section")
                if imgsec is not None:
                    img = imgsec.find("img")
                    if img is not None:
                        img = img.get("src")
                        downloadMoviePoster(img, "episode", title_id, title_id)
                print "done"
                episode = []
                egenre = []
                ecrew = []
                time.sleep(1)


# showlist = readID("seasonEpisodes.csv")
#
# # episode = []
# # row = ["title_id","title_parent_id","title_show_id","title_name","title_type","title_episode_number","title_network","title_airdate","title_bio"]
# # episode.append(row)
# # exporttocsv("episode_info.csv",episode)
#
# episode = []
# creator = []
# genre = []
# writer = []
# producers = []
# cast = []
# director = []
#
# for show in showlist:
#     seasonlink1 = "https://www.rottentomatoes.com/tv/"
#     seasonlink2 = show[1]  # show_id
#     seasonlink3 = "/s"
#     seasonlink4 = show[0].split(seasonlink2) # season number
#     seasonlink4 = seasonlink4[1].split("_s")
#     seasonlink4 = seasonlink4[1]
#     print seasonlink4
#     seasonlink5 = "/e"
#
#     numEpisode = int(show[2])  # number of episode in the season
#     if "title_episodes" != numEpisode:
#         for episodeNum in range(1,numEpisode+1):
#             url = seasonlink1 + seasonlink2 + seasonlink3 + seasonlink4 + seasonlink5 + str(episodeNum)
#             print url
#             page = urllib2.urlopen(url)
#             soup = BeautifulSoup(page, "html5lib")
#             # CREATE EPISODE ID
#             title_id = seasonlink2 + "_s" + seasonlink4 + "_e" + str(episodeNum)
#             # GET SEASON ID = PARENT
#             title_parent_id = show[0]
#             # GET SHOW ID
#             title_show_id = seasonlink2
#             # GET EPISODE NAME
#             tv_title_headers = soup.find(id="tv_title_headers")
#             movie_title = tv_title_headers.find("h1", class_="movie_title")
#             title_name = movie_title.get_text()
#             title_name = cleanup(title_name)
#             # SET TITLE_TYPE
#             title_type = "episode"
#             # GET title_episode_number
#             title_episode_number = episodeNum
#             # BIO
#             mainColumn = soup.find(id="mainColumn")
#             movieSynopsis = mainColumn.find(id="movieSynopsis")
#             bio = movieSynopsis.get_text()
#             bio = cleanup(bio)
#             # CONTENT-BODY
#
#             title_network = ""
#             title_airdate = ""
#             ul = mainColumn.find("ul", class_="content-meta info")
#             all_li = ul.find_all("li")
#             for li in all_li:
#                 s = li.get_text()
#                 if "Network" in s:
#                     season_network = li.find(class_="meta-value").get_text()
#                     title_network = cleanup(season_network)
#                 elif "Air Date" in s:
#                     premieredate = li.find(class_="meta-value").get_text()
#                     title_airdate = cleanup(premieredate)
#                 elif "Genre" in s:
#                     g = li.find(class_="meta-value").get_text()
#                     row = [title_id, cleanup(g)]
#                     genre.append(row)
#                 elif "Creator" in s:
#                     c = li.find(class_="meta-value")
#                     link = c.find_all("a")
#                     for l in link:
#                         c = l.get("href")
#                         c = c.split("/")[2]
#                         row = [title_id, cleanup(c)]
#                         creator.append(row)
#                 elif "Producers" in s:
#                     c = li.find(class_="meta-value")
#                     link = c.find_all("a")
#                     for l in link:
#                         c = l.get("href")
#                         c = c.split("/")[2]
#                         row = [title_id, cleanup(c)]
#                         producers.append(row)
#                 elif "Written" in s:
#                     c = li.find(class_="meta-value")
#                     link = c.find_all("a")
#                     for l in link:
#                         c = l.get("href")
#                         c = c.split("/")[2]
#                         row = [title_id, cleanup(c)]
#                         writer.append(row)
#                 elif "Directed" in s:
#                     c = li.find(class_="meta-value")
#                     link = c.find_all("a")
#                     for l in link:
#                         c = l.get("href")
#                         c = c.split("/")[2]
#                         row = [title_id, cleanup(c)]
#                         director.append(row)
#             # GET CAST FOR THE EPISODE
#             castSection = soup.find("div", class_="castSection")
#             if castSection is not None:
#                 castitem = castSection.find_all(class_="cast-item")
#                 if castitem is not None:
#                     for item in castitem:
#                         link = item.find("a").get("href")
#                         link = link.split("/")[2]
#                         link = cleanup(link)
#                         mediabody = item.find(class_="media-body")
#                         span = mediabody.find("span", class_="characters subtle smaller")
#                         role = ""
#                         if span is not None:
#                             role = span.get_text()
#                             role = role.split("as ")[1]
#                             role = cleanup(role)
#                         row = [title_id, link, role]
#                         cast.append(row)
#
#             #row = [title_id, title_parent_id, title_show_id, title_name, title_type, title_episode_number, title_network, title_airdate, bio]
#             row = [title_id, title_parent_id, "Episode", title_name, bio, title_airdate, "",
#                    "", "", "", title_network]
#             episode.append(row)
#             appendtocsv("title.csv", episode)
#             appendtocsv("titleGenre.csv", genre)
#             appendtocsv("titleCreator.csv", creator)
#             appendtocsv("titleProducers.csv", producers)
#             appendtocsv("titleCast.csv", cast)
#             appendtocsv("titleWriter.csv", writer)
#             appendtocsv("titleDirector.csv", director)
#
#             # FINALLY OPTIONAL DOWNLOAD TV POSTER
#             imgsec = soup.find(id="tv-image-section")
#             if imgsec is not None:
#                 img = imgsec.find("img")
#                 if img is not None:
#                     img = img.get("src")
#                     downloadMoviePoster(img, "episode", title_id, title_id)
#             print "done"
#             episode = []
#             genre = []
#             creator = []
#             producers = []
#             cast = []
#             writer = []
#             director = []
#             time.sleep(1)
