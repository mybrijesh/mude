from functions import *


def getmovieinformation():
    rtmovieid = readID("movieid.csv")
    # print rtmovieid[0]
    #rtmovieid = [['harry_potter_and_the_sorcerers_stone'], ['thor'], ['rundskop_2012'], ["pk"]]

    mmovie = []
    mcrew = []
    mgenre = []

    url1 = "https://www.rottentomatoes.com/m/"
    i = 0
    for movie in rtmovieid:
        i = i + 1
        url = url1 + movie[0]
        # CHECK IF SITE EXIST
        request = requests.get(url, 'HEAD')
        if request.status_code == 200:
            page = urllib2.urlopen(url)
            soup = BeautifulSoup(page, "html5lib")
            if soup is not None:
	            print i, url
	            mainColumn = soup.find(id="mainColumn")
	            # GETTING NAME
	            # heroImageContainer = mainColumn.find(id="movie-title")
	            movieName = mainColumn.find('h1', id="movie-title")
	            movieName = movieName.get_text().replace("\n", "")
	            movieName = movieName.split("(")
	            movieName = movieName[0]
	            title_id = movie[0]
	            title_name = cleanup(movieName)
	            '''mname.append(removeSpace(movieName[0]))'''

	            # GET FRANCHISE NAME
	            title_parent_id = ""
	            franchiseLink = mainColumn.find(class_="franchiseLink")
	            if franchiseLink is not None:
	                flink = franchiseLink.find('a')
	                flink = flink.get('href').split("/")[2]
	                flink = cleanup(flink)
	                # row = [movie[0], flink]
	                # mfranchise.append(row)
	                title_parent_id = flink

	                # franchiseID.append(flink)
	                # franchiseID.append("")


	            title_bio = ""
	            movieInfoSec = mainColumn.find(class_="panel panel-rt panel-box movie_info media")
	            if movieInfoSec is not None:
	                # GET MOVIE BIO
	                b = ""
	                bio = movieInfoSec.find(id="movieSynopsis")
	                if bio is not None:
	                    b = bio.get_text()
	                bio = b
	                title_bio = cleanup(bio)

	                # GET OTHER MOVIE INFORMATION
	                ul = movieInfoSec.find("ul", class_="content-meta info")
	                all_li = ul.find_all("li")
	                title_rating = ""
	                genre = ""
	                directors = ""
	                writters = ""
	                title_releasedate = ""
	                title_dvddate = ""
	                title_runtime = ""
	                title_studio = ""
	                title_boxoffice = ""
	                for li in all_li:
	                    s = li.get_text()
	                    metavalue = li.find(class_="meta-value")
	                    if "Rating: " in s:
	                        metavalue = metavalue.get_text()
	                        metavalue = metavalue.split(" ")
	                        rating = metavalue[0]
	                        title_rating = cleanup(rating)
	                    elif "Genre:" in s:
	                        genre = metavalue.get_text()
	                        genre = genre.split(",")
	                        for g in genre:
	                            row = [title_id, cleanup(g)]
	                            mgenre.append(row)

	                    elif "Directed By:" in s:
	                        s = ""
	                        all_dire = metavalue.find_all('a')
	                        for dire in all_dire:
	                            d = dire.get('href').split("/")[2]
	                            row = [title_id, cleanup(d),"Director",""]
	                            mcrew.append(row)
	                    elif "Produce" in s:
	                        s = ""
	                        all_dire = metavalue.find_all('a')
	                        for dire in all_dire:
	                            d = dire.get('href').split("/")[2]
	                            row = [title_id, cleanup(d),"Producer",""]
	                            mcrew.append(row)
	                    elif "Starring" in s:
	                        s = ""
	                        all_dire = metavalue.find_all('a')
	                        for dire in all_dire:
	                            d = dire.get('href').split("/")[2]
	                            row = [title_id, cleanup(d), "Starring", ""]
	                            mcrew.append(row)
	                    elif "Written By:" in s:
	                        s = ""
	                        all_dire = metavalue.find_all('a')
	                        for dire in all_dire:
	                            s = dire.get('href').split("/")[2]
	                            row = [title_id, cleanup(s), "Writer", ""]
	                            mcrew.append(row)
	                    elif "In Theaters" in s:
	                        rdate = cleanup(metavalue.get_text())
	                        # rdate = removeSpace(metavalue.get_text())
	                        # rdate = rdate.replace(u'\xa0', u' ')
	                        rdate = rdate.replace("limited", "")
	                        rdate = rdate.split("wide")
	                        rdate = rdate[0]
	                        title_releasedate = cleanup(rdate)
	                    elif "On Disc/Streaming" in s:
	                        Online = metavalue.get_text()
	                        title_dvddate = cleanup(Online)
	                    elif "Box Office" in s:
	                        boxoffice = metavalue.get_text()
	                        title_boxoffice = cleanup(boxoffice)
	                    elif "Runtime" in s:
	                        runtime = metavalue.get_text()
	                        title_runtime = cleanup(runtime)
	                    elif "Studio" in s:
	                        studio = metavalue.get_text()
	                        title_studio = cleanup(studio)

	            # GET CAST
	            castSection = mainColumn.find(class_="castSection")
	            if castSection is not None:
	                eachSec = castSection.find_all(class_="cast-item")
	                casts = ""
	                for esec in eachSec:
	                    plink = esec.find('a')
	                    plink = plink.get('href').split("/")[2]
	                    plink = cleanup(plink)
	                    # get role
	                    mediabody = esec.find(class_="media-body")
	                    rolespan = mediabody.find('span', class_="characters")
	                    if rolespan is not None:
	                        role = rolespan.get('title')
	                        # role = role.replace(u'\xf1','n')
	                    else:
	                        role = ""

	                    role = cleanup(role)
	                    row = [title_id, plink, "Cast", role]
	                    mcrew.append(row)

	            row = [title_id, title_parent_id, "Movie", title_name, title_bio, title_releasedate, title_dvddate,
	                   title_rating, title_runtime, title_boxoffice, title_studio]
	            mmovie.append(row)

	            appendtocsv("title.csv", mmovie)
	            appendtocsv("titleCrew.csv", mcrew)
	            appendtocsv("titleGenre.csv", mgenre)

	            mmovie = []
	            mcrew = []
	            mgenre = []

	            # FINALLY OPTIONAL DOWNLOAD MOVIE POSTER
	            imgsec = soup.find(id="movie-image-section")
	            if imgsec is not None:
	                center = imgsec.find(class_="center")
	                if center is not None:
	                    img = center.find("img")
	                    if img is not None:
	                        img = img.get("src")
	                        downloadMoviePoster(img, "movie", title_id, title_id)

        # CLEAR THE LIST
        mmovie = []
        mcrew = []
        mgenre = []
        # WAIT 2 SECONDS BEFORE REQUESTING NEXT PAGE
        time.sleep(2)




# rtmovieid = readID("movieid.csv")
# #print rtmovieid[0]
# rtmovieid = [['harry_potter_and_the_sorcerers_stone'],['thor'],['rundskop_2012'],["pk"]]
#
# mmovie = []
# mfranchise = []
# mdirectors = []
# mwritters = []
# mcast = []
# #title_id	parent_id	title_type	title_name	title_desc	release_date	film_grade	runtime	box_office	agency
# header = ['title_id','parent_id','title_type','title_name','title_desc','release_date','dvd_release_date','title_grade','runtime','box_office','agency']
# mmovie.append(header)
#
# header = ['title_id','franchise_id']
# mfranchise.append(header)
#
# header = ['title_id','title_castID','role']
# mcast.append(header)
#
# header = ['title_id','director_id']
# mdirectors.append(header)
#
# header = ['title_id','writter_id']
# mwritters.append(header)
#
# mstarring = []
# row = ["title_id","title_starring"]
# mstarring.append(row)
#
# mgenre = []
# row = ["title_id","title_genre"]
# mgenre.append(row)
# mproducers = []
# row = ["title_id","title_producers"]
# mproducers.append(row)
# showCreator = []
# row = ["title_id","title_creator"]
# showCreator.append(row)
#
# exporttocsv("title.csv",mmovie)
# exporttocsv("titleCast.csv",mcast)
# exporttocsv("titleDirector.csv",mdirectors)
# exporttocsv("titlefranchise.csv",mfranchise)
# exporttocsv("titleProducers.csv",mproducers)
# exporttocsv("titleWriter.csv",mwritters)
# exporttocsv("titleGenre.csv",mgenre)
# exporttocsv("titleStarring.csv",mstarring)
# exporttocsv("titleCreator.csv",showCreator)
#
# mmovie = []
# mcast = []
# mfranchise = []
# mdirectors = []
# mwritters = []
# mstarring = []
# mproducers = []
# mgenre = []
#
#
# url1 = "https://www.rottentomatoes.com/m/"
# i = 0
# for movie in rtmovieid:
#     i = i + 1
#     url = url1 + movie[0]
#     #CHECK IF SITE EXIST
#     request = requests.get(url,'HEAD')
#     if request.status_code == 200:
#         page = urllib2.urlopen(url)
#         soup = BeautifulSoup(page, "html5lib")
#         print i, url
#         #mmovieid.append(movie)
#         mainColumn = soup.find(id="mainColumn")
#         #GETTING NAME
#         #heroImageContainer = mainColumn.find(id="movie-title")
#         movieName = mainColumn.find('h1', id="movie-title")
#         movieName = movieName.get_text().replace("\n","")
#         movieName = movieName.split("(")
#         movieName = movieName[0]
#         title_id = movie[0]
#         title_name = cleanup(movieName)
#         '''mname.append(removeSpace(movieName[0]))'''
#
#         #GET FRANCHISE NAME
#         title_parent_id = ""
#         franchiseLink = mainColumn.find(class_="franchiseLink")
#         if franchiseLink is not None:
#             flink = franchiseLink.find('a')
#             flink = flink.get('href').split("/")[2]
#             flink = cleanup(flink)
#             row = [movie[0],flink]
#             mfranchise.append(row)
#             title_parent_id = flink
#             #franchiseID.append(flink)
#             #franchiseID.append("")
#
#         #GET CAST
#         castSection = mainColumn.find(class_="castSection")
#         if castSection is not None:
#             eachSec = castSection.find_all(class_="cast-item")
#             casts = ""
#             for esec in eachSec:
#                 plink = esec.find('a')
#                 plink = plink.get('href').split("/")[2]
#                 plink = cleanup(plink)
#                 # get role
#                 mediabody = esec.find(class_="media-body")
#                 rolespan = mediabody.find('span', class_="characters")
#                 if rolespan is not None:
#                     role = rolespan.get('title')
#                     #role = role.replace(u'\xf1','n')
#                 else:
#                     role = ""
#
#                 role = cleanup(role)
#                 row = [title_id, plink, role]
#                 mcast.append(row)
#
#         title_bio = ""
#         movieInfoSec = mainColumn.find(class_="panel panel-rt panel-box movie_info media")
#         if movieInfoSec is not None:
#             #GET MOVIE BIO
#             b = ""
#             bio = movieInfoSec.find(id="movieSynopsis")
#             if bio is not None:
#                 b = bio.get_text()
#             bio = b
#             title_bio = cleanup(bio)
#
#             #GET OTHER MOVIE INFORMATION
#             ul = movieInfoSec.find("ul", class_="content-meta info")
#             all_li = ul.find_all("li")
#             title_rating = ""
#             genre = ""
#             directors = ""
#             writters = ""
#             title_releasedate = ""
#             title_dvddate = ""
#             title_runtime = ""
#             title_studio = ""
#             title_boxoffice = ""
#             for li in all_li:
#                 s = li.get_text()
#                 metavalue = li.find(class_="meta-value")
#                 if "Rating: " in s:
#                     metavalue = metavalue.get_text()
#                     metavalue = metavalue.split(" ")
#                     rating = metavalue[0]
#                     title_rating = cleanup(rating)
#                 elif "Genre:" in s:
#                     genre = metavalue.get_text()
#                     genre = genre.split(",")
#                     for g in genre:
#                         row = [title_id,cleanup(g)]
#                         mgenre.append(row)
#
#                 elif "Directed By:" in s:
#                     s = ""
#                     all_dire = metavalue.find_all('a')
#                     for dire in all_dire:
#                         d = dire.get('href').split("/")[2]
#                         row = [title_id,cleanup(d)]
#                         mdirectors.append(row)
#                 elif "Produce" in s:
#                     s = ""
#                     all_dire = metavalue.find_all('a')
#                     for dire in all_dire:
#                         d = dire.get('href').split("/")[2]
#                         row = [title_id,cleanup(d)]
#                         mproducers.append(row)
#                 elif "Starring" in s:
#                     s = ""
#                     all_dire = metavalue.find_all('a')
#                     for dire in all_dire:
#                         d = dire.get('href').split("/")[2]
#                         row = [title_id,cleanup(d)]
#                         mstarring.append(row)
#                 elif "Written By:" in s:
#                     s = ""
#                     all_dire = metavalue.find_all('a')
#                     for dire in all_dire:
#                         s = dire.get('href').split("/")[2]
#                         row = [title_id,cleanup(s)]
#                         mwritters.append(row)
#                 elif "In Theaters" in s:
#                     rdate = cleanup(metavalue.get_text())
#                     # rdate = removeSpace(metavalue.get_text())
#                     # rdate = rdate.replace(u'\xa0', u' ')
#                     rdate = rdate.replace("limited", "")
#                     rdate = rdate.split("wide")
#                     rdate = rdate[0]
#                     title_releasedate = cleanup(rdate)
#                 elif "On Disc/Streaming" in s:
#                     Online = metavalue.get_text()
#                     title_dvddate = cleanup(Online)
#                 elif "Box Office" in s:
#                     boxoffice = metavalue.get_text()
#                     title_boxoffice = cleanup(boxoffice)
#                 elif "Runtime" in s:
#                     runtime = metavalue.get_text()
#                     title_runtime = cleanup(runtime)
#                 elif "Studio" in s:
#                     studio = metavalue.get_text()
#                     title_studio = cleanup(studio)
#
#         row = [title_id, title_parent_id, "Movie", title_name, title_bio, title_releasedate, title_dvddate, title_rating, title_runtime, title_boxoffice, title_studio]
#         mmovie.append(row)
#
#         appendtocsv("title.csv", mmovie)
#         appendtocsv("titleCast.csv", mcast)
#         appendtocsv("titleDirector.csv", mdirectors)
#         appendtocsv("titlefranchise.csv", mfranchise)
#         appendtocsv("titleProducers.csv", mproducers)
#         appendtocsv("titleWriter.csv", mwritters)
#         appendtocsv("titleGenre.csv", mgenre)
#         appendtocsv("titleStarring.csv", mstarring)
#
#         #FINALLY OPTIONAL DOWNLOAD MOVIE POSTER
#         imgsec = soup.find(id="movie-image-section")
#         if imgsec is not None:
#             center = imgsec.find(class_="center")
#             if center is not None:
#                 img = center.find("img")
#                 if img is not None:
#                     img = img.get("src")
#                     downloadMoviePoster(img, "movie", title_id, title_id)
#
#     # CLEAR THE LIST
#     mmovie = []
#     mcast = []
#     mfranchise = []
#     mdirectors = []
#     mwritters = []
#     mstarring = []
#     mproducers = []
#     mgenre = []
#     #WAIT 2 SECONDS BEFORE REQUESTING NEXT PAGE
#     time.sleep(2)

