from functions import *
import json

def getPictureFromCuroselThumbnail(main_container, photopath):
    movie_photo_panel = main_container.find(id="movie-photos-panel")
    if movie_photo_panel is not None:
        ss = movie_photo_panel.find("script")
        if ss is not None:
            ss = ss.get_text()
            var_d_source = ss.split('var photos =', 1)[-1].rsplit(';', 1)[0].strip()
            var_d_source = var_d_source.split("]")[0]
            var_d_value = json.loads(var_d_source + "]")
            n = len(var_d_value)
            i = 0
            while i < n and i < 6:
                downloadImage(var_d_value[i].get("urls").get("thumbnail"), getPathToSpecificFolder(photopath + str(i + 1) + ".jpg"))
                i = i + 1

def getFranchise(media,photopath):
    url1 = "https://www.rottentomatoes.com/franchise/"
    url = url1 + media[1]
    if checkForPageHeader(url):
        soup = readPageFromURL(url)
        if soup != "":
            main_container = soup.find(id="main_container")
            if main_container is not None:
                FranchiseTopBanner = main_container.find(id="FranchiseTopBanner")
                if FranchiseTopBanner is not None:
                    bannerImage = FranchiseTopBanner.find(class_="bannerImage")["style"]
                    if bannerImage is not None:
                        bannerImage = bannerImage.split("'")
                        bannerImage = bannerImage[1]
                        downloadImage(bannerImage, getPathToSpecificFolder(photopath+"0_top_banner.jpg"))

            FranchiseTopSubBanner = main_container.find(id="FranchiseTopSubBanner")
            if FranchiseTopSubBanner is not None:
                banner = FranchiseTopSubBanner.find(class_="banner")["style"]
                if banner is not None:
                    bannerImage = banner.split("'")
                    bannerImage = bannerImage[1]
                    downloadImage(bannerImage, getPathToSpecificFolder(photopath+"0_sub_banner.jpg"))

            movie_photo_panel = main_container.find(id="movie-photos-panel")
            if movie_photo_panel is not None:
                ss = movie_photo_panel.find("script")
                if ss is not None:
                    ss = ss.get_text()
                    var_d_source = ss.split('var photos =', 1)[-1].rsplit(';', 1)[0].strip()
                    var_d_source = var_d_source.split("]")[0]
                    var_d_value = json.loads(var_d_source + "]")
                    n = len(var_d_value)
                    i = 0
                    while i < n and i < 6:
                        downloadImage(var_d_value[i].get("urls").get("fullscreen"),getPathToSpecificFolder(photopath + str(i + 1) + ".jpg"))
                        i = i + 1

def getMovie(media,photopath):
    url1 = "https://www.rottentomatoes.com/m/"
    url = url1 + media[1]
    if checkForPageHeader(url):
        soup = readPageFromURL(url)
        if soup != "":
            mainColumn = soup.find(id="mainColumn")
            if mainColumn is not None:
                heroImageContainer = mainColumn.find(id="heroImageContainer")
                if heroImageContainer is not None:
                    heroImage = heroImageContainer.find(class_="heroImage")
                    if heroImage is not None:
                        heroImage = heroImage["style"]
                        s = heroImage.split("'")
                        imgurl = s[1]
                        downloadImage(imgurl, getPathToSpecificFolder(photopath + "0_heroImage.jpg"))

                poster_link = mainColumn.find(id="movie-image-section")
                if poster_link is not None:
                    img = poster_link.find("img", class_="posterImage")
                    if img is not None:
                        imgurl = img.get("src")
                        downloadImage(imgurl, getPathToSpecificFolder(photopath + "0.jpg"))

                getPictureFromCuroselThumbnail(mainColumn, photopath)

def getTVShow(media,photopath):
    url1 = "https://www.rottentomatoes.com/tv/"
    url = url1 + media[1]
    if checkForPageHeader(url):
        soup = readPageFromURL(url)
        if soup != "":
            mainColumn = soup.find(id="main_container")
            if mainColumn is not None:
                heroImageContainer = mainColumn.find(id="heroImageContainer")
                if heroImageContainer is not None:
                    heroImage = heroImageContainer.find(class_="heroImage")
                    if heroImage is not None:
                        heroImage = heroImage["style"]
                        s = heroImage.split("'")
                        imgurl = s[1]
                        downloadImage(imgurl, getPathToSpecificFolder(photopath + "0_heroImage.jpg"))

                poster_link = mainColumn.find(id="tv-image-section")
                if poster_link is not None:
                    img = poster_link.find("img", class_="posterImage")
                    if img is not None:
                        imgurl = img.get("src")
                        downloadImage(imgurl, getPathToSpecificFolder(photopath + "0.jpg"))
                getPictureFromCuroselThumbnail(mainColumn, photopath)

def getSeason(photopath,url):
    if checkForPageHeader(url):
        soup = readPageFromURL(url)
        if soup != "":
            mainColumn = soup.find(id="mainColumn")
            if mainColumn is not None:

                poster_link = mainColumn.find(id="movie-image-section")
                if poster_link is not None:
                    img = poster_link.find("img", class_="posterImage")
                    if img is not None:
                        imgurl = img.get("src")
                        downloadImage(imgurl, getPathToSpecificFolder(photopath + "0.jpg"))

                getPictureFromCuroselThumbnail(mainColumn, photopath)


def getEpisode(photopath,url):
    if checkForPageHeader(url):
        soup = readPageFromURL(url)
        if soup != "":
            mainColumn = soup.find(id="mainColumn")
            if mainColumn is not None:

                poster_link = mainColumn.find(id="tv-image-section")
                if poster_link is not None:
                    img = poster_link.find("img", class_="posterImage")
                    if img is not None:
                        imgurl = img.get("src")
                        downloadImage(imgurl, getPathToSpecificFolder(photopath + "0.jpg"))

                getPictureFromCuroselThumbnail(mainColumn, photopath)

mediaID = readID("RTTitle.csv")
print "Finish Reading csv"

# FIRST CREATE MEDIA FOLDER
createImageFolderAtCurrentDirectory("media")
mpath = "/media"
# SECOND CREATE FOLDER FOR MEDIA_ID
# PHOTO AND VIDEO
showID = dict()
for media in mediaID:
    if media[0] != "":

        #if media[3] == "Episode":
        print media[0]
        # CREATE FOLDER FOR MEDIA
        createFolder(str(media[0]), "/media")
        mpath = "/media/" + str(media[0])
        # CREATE PHOTOS FOLDER
        createFolder("photos", "/media/" + str(media[0]))
        photopath = mpath + "/photos/"
        # create video folder
        createFolder("videos", "/media/" + str(media[0]))


        if media[3] == "Franchise":
            getFranchise(media,photopath)
        elif media[3] == "Movie":
            getMovie(media,photopath)
        elif media[3] == "TVShow":
            getTVShow(media,photopath)
        elif media[3] == "Season":
            showID[media[1]] = media[2]
            url = "https://www.rottentomatoes.com/tv/"+media[2]+"/s"+str(getSeasonNumberFromString(media[1]))
            getSeason(photopath,url)
        elif media[3] == "Episode":
            url = "https://www.rottentomatoes.com/tv/"+showID[media[2]]+"/s"+str(getSeasonNumberFromString(media[2]))+"/e" + str(getEpisodeNumberFromString(media[1]))
            getEpisode(photopath,url)

        if int(media[0]) % 100 == 0:
            time.sleep(2)