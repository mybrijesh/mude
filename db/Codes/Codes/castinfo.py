from functions import *

def getcast():
    # list = [["", "emma_watson", ""]]
    artist = []
    row = ["artist_id","artist_name","birthdate","birthplace","artist_desc"]
    artist.append(row)
    appendtocsv("Artist.csv",artist)
    artist = []
    ls = readID("titleCrew.csv")
    c_id = []
    for l in ls:
        if len(l[2]) > 0 and l[2] != "artist_id":
            c_id.append(l[2])
    c_id = list(set(c_id))
    i = 1
    for cast in c_id:
        celebrity_id = cast
        url1 = "https://www.rottentomatoes.com/celebrity/"
        url = url1 + celebrity_id
        request = requests.get(url,'HEAD')
        if request.status_code == 200:
            page = urllib2.urlopen(url)
            soup = BeautifulSoup(page, "html5lib")
            print i, url
            i = i + 1
            main_container = soup.find(id="main_container")
            celeb_name = main_container.find(class_="celeb_name")
            celebrity_name = ""
            celeb_summary_bio = ""
            celebrity_bplace = ""
            celebrity_bday = ""
            if celeb_name is not None:
                celebrity_name = cleanup(celeb_name.get_text())
            celeb_bio = main_container.find(class_="celeb_bio")
            if celeb_bio is not None:
                #GET BDATE AND BPLACE
                celeb_bio_row = celeb_bio.find_all(class_="celeb_bio_row")
                for content in celeb_bio_row:
                    s = content.get_text()
                    if "Birthday:" in s:
                        celebrity_bday = content.find("time")
                        if celebrity_bday is not None:
                            celebrity_bday = celebrity_bday.get_text()
                    elif "Birthplace:" in s:
                        s = s.split("Birthplace:")
                        s = cleanup(s[1])
                        celebrity_bplace = s
                #GET BIO
                celeb_summary_bio = celeb_bio.find(class_="celeb_summary_bio")
                if celeb_summary_bio is not None:
                    celeb_summary_bio = cleanup(celeb_summary_bio.get_text())
                else:
                    celeb_summary_bio = ""
            #GET CELEBRITY PHOTO
            celebPhotoCol = main_container.find(class_="celebPhotoCol")
            if celebPhotoCol is not None:
                celebHeroImage = celebPhotoCol.find(class_="celebHeroImage")["style"]
                s = celebHeroImage.split("'")
                imgurl = s[1]
                downloadMoviePoster(imgurl,"celebrity",celebrity_id,celebrity_id)
            row = [celebrity_id,celebrity_name,celebrity_bday,celebrity_bplace,celeb_summary_bio]
            artist.append(row)
            appendtocsv("Artist.csv", artist)
            artist = []
        else:
            print "PAGE DOES NOT EXIST: ", celebrity_id
        time.sleep(1)