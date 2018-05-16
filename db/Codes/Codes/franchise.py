from functions import *



def getfranhise():
    lists = readID("title.csv")
    f_id = []
    for l in lists:
        if l[3] == "Movie" or l[3] == "TVShow":
            if len(l[2]) > 1:
                f_id.append(l[2])

    franchise = []
    row = ["franchise_id","franchise_name","franchise_desc"]
    franchise.append(row)
    appendtocsv("Franchise.csv",franchise)
    franchise = []


    #lists = [["harry_potter"]]
    url1 = "https://www.rottentomatoes.com/franchise/"

    for f in f_id:
        franchise_id = f
        url = url1 + franchise_id
        request = requests.get(url, 'HEAD')
        if request.status_code == 200:
            page = urllib2.urlopen(url)
            soup = BeautifulSoup(page, "html5lib")
            main_container = soup.find(id="main_container")

            franchise_name = ""
            franchise_desc = ""

            FranchiseTopBanner = main_container.find(id="FranchiseTopBanner")
            if FranchiseTopBanner is not None:
                bannerImage = FranchiseTopBanner.find(class_="bannerImage")["style"]
                if bannerImage is not None:
                    bannerImage = bannerImage.split("'")
                    bannerImage = bannerImage[1]
                    downloadMoviePoster(bannerImage,"franchise",franchise_id,franchise_id+"_top_banner")

                #get title
                title = main_container.find(class_="title")
                franchise_name = cleanup(title.get_text())

            FranchiseTopSubBanner = main_container.find(id="FranchiseTopSubBanner")
            if FranchiseTopSubBanner is not None:
                banner = FranchiseTopSubBanner.find(class_="banner")["style"]
                if banner is not None:
                    bannerImage = banner.split("'")
                    bannerImage = bannerImage[1]
                    downloadMoviePoster(bannerImage,"franchise",franchise_id,franchise_id+"_sub_banner")

                description = FranchiseTopSubBanner.find(class_="description")
                if description is not None:
                    description = description.get_text()
                    franchise_desc = cleanup(description)
                    # print description

            row = [franchise_id, franchise_name, franchise_desc]
            franchise.append(row)
            appendtocsv("Franchise.csv", franchise)
            franchise = []
            print url
            time.sleep(1)
        else:
            print "Franchise is not found: ", franchise
