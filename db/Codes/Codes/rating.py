from functions import *

def converLetterGradeToNumberInHundred(s):
    if s == "A+":
        return 100
    elif s == "A":
        return 95
    elif s == "A-":
        return 90
    elif s == "B+":
        return 87
    elif s == "B":
        return 84
    elif s == "B-":
        return 80
    elif s == "C+":
        return 77
    elif s == "C":
        return 74
    elif s == "C-":
        return 70
    elif s == "D+":
        return 67
    elif s == "D":
        return 64
    elif s == "D-":
        return 60
    else:
        return 50

def HundredTo5(n):
    return (n/10.0)/2.0
def HundredTo10(n):
    return (n/10.0)

def convertToBase5(n,lower):
    return OneDecimalFormat(HundredTo5((n * 100)/lower))
def convertToBase10(n,lower):
    return OneDecimalFormat(HundredTo10((n * 100)/lower))

def letterTo5(s):
    return OneDecimalFormat(HundredTo5(converLetterGradeToNumberInHundred(s)))
def letterTo10(s):
    return OneDecimalFormat(HundredTo10(converLetterGradeToNumberInHundred(s)))

def OneDecimalFormat(x):
    return "{0:.1f}".format(x)

def normalizeRatingTo10(s):
    rating = s
    try:
        if "/" in rating:
            rating = rating.split("/")
            rating[0] = rating[0].replace("'","")
            rating[1] = rating[1].replace("'", "")
            upper = float(rating[0])
            lower = float(rating[1])
            nupper = convertToBase10(upper, lower)
            nlower = convertToBase10(lower, lower)
            return str(nupper)+"/"+str(int(float(nlower)))
        elif rating.isdigit():
            if len(rating) == 1:
                return OneDecimalFormat(HundredTo10(float(rating) * 10))+"/10"
            else:
                return OneDecimalFormat(HundredTo10(float(rating)))+"/10"
        else:
            return str(letterTo10(rating))+"/10"
    except Exception as inst:
        print "ERROR COUNLD NOT CONVERT RATING"
    # RETURN 0/0 ON FAIL CONVERSION CASE AND DON'T ADD IT TO .CSV
    return "0/0"

def normalizeRatingTo5(s):
    rating = s
    if "/" in rating:
        rating = rating.split("/")
        rating[0] = rating[0].replace("'", "")
        rating[1] = rating[1].replace("'", "")
        upper = float(rating[0])
        lower = float(rating[1])
        nupper = convertToBase5(upper,lower)
        nlower = convertToBase5(lower,lower)
        return str(nupper)+"/"+str(int(float(nlower)))
    elif rating.isdigit():
        if len(rating) == 1:
            return OneDecimalFormat(HundredTo5(float(rating) * 10))+"/5"
        else:
            return OneDecimalFormat(HundredTo5(float(rating)))+"/5"
    else:
        return str(letterTo5(rating))+"/5"

def getmovierating(title_id,turl):

    url3 = "/reviews"
    url4 = "/reviews/?type=user"
    #url = url1 + url2 + url3
    url = turl + url3
    titleRating = []
    #url = "https://www.rottentomatoes.com/m/harry_potter_and_the_sorcerers_stone/reviews/?page=4&sort="
    request = requests.get(url, 'HEAD')
    if request.status_code == 200:
        soup = readPageFromURL(url)
        if soup != "":
            print "Critic", url
            review = soup.find(id="reviews")
            content = review.find(class_="content")

            if content is not None:
                # GET NUMBER OF PAGES FOR REVIEW
                pageInfo = content.find("span", class_="pageInfo")
                review_table = content.find(class_="review_table")
                if pageInfo is not None:
                    pageInfo = cleanup(pageInfo.get_text())
                    pageInfo = pageInfo.split(" ")[3]
                    pageInfo = int(pageInfo)
                    if pageInfo > 5:
                        pageInfo = 5

                    for pageNumber in range(1,pageInfo+1):
                        url1 = "https://www.rottentomatoes.com/m/"
                        url2 = title_id
                        url3 = "/reviews/?page="
                        url4 = str(pageNumber)
                        url5 = "&sort="
                        url = turl + url3 + url4 + url5
                        request = requests.get(url, 'HEAD')
                        if request.status_code == 200:
                            soup = readPageFromURL(url)
                            if soup != "":
                                review = soup.find(id="reviews")
                                if review is not None:
                                    content = review.find(class_="content")
                                    if content is not None:
                                        review_table = content.find(class_="review_table")
                                        if review_table is not None:
                                            sub_review_table = review_table.find_all(class_="review_table_row")
                                            for review in sub_review_table:
                                                critic_name = review.find(class_="critic_name")
                                                member_id = ""
                                                publication_id = ""
                                                if critic_name is not None:
                                                    all_a = critic_name.find_all("a")
                                                    if len(all_a) == 2:
                                                        # GET RATER_ID
                                                        rater = all_a[0].get("href")
                                                        member_id = rater.split("/")[2]
                                                        #GET SOURCE_ID
                                                        source = all_a[1].get("href")
                                                        publication_id = source.split("/")[1]
                                                        review_container = review.find(class_="review_container")
                                                        review_date = review_container.find(class_="review_date subtle small")
                                                        title_review_date = ""
                                                        if review_date is not None:
                                                            title_review_date = cleanup(review_date.get_text())

                                                        review_area = review_container.find(class_="review_area")
                                                        the_review = review_area.find(class_="the_review")
                                                        title_review = ""
                                                        if the_review is not None:
                                                            title_review = cleanup(the_review.get_text())

                                                        title_rating = ""
                                                        full_review = ""
                                                        review_desc = review_area.find(class_="review_desc")
                                                        small = review_desc.find(class_="small subtle")
                                                        if small is not None:
                                                            ourl = small.find("a")
                                                            if ourl is not None:
                                                                full_review = ourl.get("href")
                                                            small = small.get_text()
                                                            small = small.split("Original Score: ")
                                                            if len(small) > 1:
                                                                title_rating = cleanup(small[1])

                                                        #print member_id, publication_id, title_rating, title_review_date, full_review, title_review

                                                        if len(title_rating) > 0:
                                                            # print title_rating, title_review_date
                                                            title_rating = normalizeRatingTo10(title_rating)
                                                            # print(title_id, member_id, "Critic", publication_id, title_rating, title_review_date, title_review, full_review)
                                                            row = [title_id, member_id, "Critic", publication_id, title_rating, title_review_date, title_review, full_review]
                                                            titleRating.append(row)
                                                            appendtocsv("titleRating.csv", titleRating)
                                                            titleRating = []
                                                            time.sleep(1)
                elif review_table is not None:
                    sub_review_table = review_table.find_all(class_="review_table_row")
                    for review in sub_review_table:
                        critic_name = review.find(class_="critic_name")
                        member_id = ""
                        publication_id = ""
                        if critic_name is not None:
                            all_a = critic_name.find_all("a")
                            if len(all_a) == 2:
                                # GET RATER_ID
                                rater = all_a[0].get("href")
                                member_id = rater.split("/")[2]
                                # GET SOURCE_ID
                                source = all_a[1].get("href")
                                publication_id = source.split("/")[1]
                                review_container = review.find(class_="review_container")
                                review_date = review_container.find(class_="review_date subtle small")
                                title_review_date = ""
                                if review_date is not None:
                                    title_review_date = cleanup(review_date.get_text())

                                review_area = review_container.find(class_="review_area")
                                the_review = review_area.find(class_="the_review")
                                title_review = ""
                                if the_review is not None:
                                    title_review = cleanup(the_review.get_text())

                                title_rating = ""
                                full_review = ""
                                review_desc = review_area.find(class_="review_desc")
                                small = review_desc.find(class_="small subtle")
                                if small is not None:
                                    ourl = small.find("a")
                                    if ourl is not None:
                                        full_review = ourl.get("href")
                                    small = small.get_text()
                                    small = small.split("Original Score: ")
                                    if len(small) > 1:
                                        title_rating = cleanup(small[1])

                                # print member_id, publication_id, title_rating, title_review_date, full_review, title_review

                                if len(title_rating) > 0 and titleRating != "0/0":
                                    # print title_rating, title_review_date
                                    title_rating = normalizeRatingTo10(title_rating)
                                    # print(title_id, member_id, "Critic", publication_id, title_rating, title_review_date, title_review, full_review)
                                    row = [title_id, member_id, "Critic", publication_id, title_rating, title_review_date,
                                           title_review, full_review]
                                    titleRating.append(row)
                                    appendtocsv("titleRating.csv", titleRating)
                                    titleRating = []

def getepisoderating(title_id,turl):

    url3 = "/reviews"
    #url = url1 + url2 + url3
    url = turl + url3
    titleRating = []
    #url = "https://www.rottentomatoes.com/m/harry_potter_and_the_sorcerers_stone/reviews/?page=4&sort="
    request = requests.get(url, 'HEAD')
    if request.status_code == 200:

        soup = readPageFromURL(url)
        if soup is not None:
            print url
            review = soup.find(id="reviews")
            #GET NUMBER OF PAGES FOR REVIEW
            if review is not None:
                pageInfo = review.find("span", class_="pageInfo")
                if pageInfo is not None:
                    pageInfo = cleanup(pageInfo.get_text())
                    pageInfo = pageInfo.split(" ")[3]
                    pageInfo = int(pageInfo)
                    if pageInfo > 5:
                        pageInfo = 5
                    for pageNumber in range(1,pageInfo+1):
                        url3 = "/reviews/?page="
                        url4 = str(pageNumber)
                        url5 = "&sort="
                        url = turl + url3 + url4 + url5
                        request = requests.get(url, 'HEAD')
                        if request.status_code == 200:
                            soup = readPageFromURL(url)
                            if soup != "":
                                print url
                                review = soup.find(id="reviews")
                                if review is not None:
                                    review_table = review.find("table")
                                    if review_table is not None:
                                        sub_review_table = review_table.find_all("tr")
                                        for review in sub_review_table:
                                            member_id = ""
                                            publication_id = ""
                                            all_td = review.find_all("td")
                                            if all_td is not None:
                                                all_a = all_td[1].find_all("a")
                                                if len(all_a) == 2:
                                                    # GET RATER_ID
                                                    rater = all_a[0].get("href")
                                                    member_id = rater.split("/")[2]
                                                    # GET SOURCE_ID
                                                    source = all_a[1].get("href")
                                                    publication_id = source.split("/")[1]
                                                    review_container = all_td[4]

                                                    review_date = review_container.find(class_="pull-right")
                                                    title_review_date = ""
                                                    if review_date is not None:
                                                        title_review_date = cleanup(review_date.get_text())

                                                    review_area = review_container.find("p")
                                                    the_review = review_area
                                                    title_review = ""
                                                    if the_review is not None:
                                                        title_review = cleanup(the_review.get_text())

                                                    title_rating = ""
                                                    full_review = ""
                                                    all_div = review_container.find_all("div")
                                                    if all_div[1] is not None:
                                                        small = all_div[1]
                                                        if small is not None:
                                                            ourl = small.find("a")
                                                            if ourl is not None:
                                                                full_review = ourl.get("href")
                                                            small = small.get_text()
                                                            small = small.split("Original Score: ")
                                                            if len(small) > 1:
                                                                title_rating = cleanup(small[1])

                                                    if len(title_rating) > 0:
                                                        title_rating = normalizeRatingTo10(title_rating)
                                                        #print(title_id, member_id, "Critic",publication_id, title_rating, title_review_date, title_review, full_review)
                                                        row = [title_id, member_id, "Critic",publication_id, title_rating, title_review_date, title_review, full_review]
                                                        titleRating.append(row)
                                                        appendtocsv("titleRating.csv", titleRating)
                                                        titleRating = []
                                                        time.sleep(1)
                        else:
                            print "INSDE PAGE IS NOT LOADED"
                else:
                    print "PAGE INFO IS NONE"
        else:
            print "REVIEW IS NONE"
    else:
        print "EPISODE IS NOT FOUND: ",url


def getmovieratingaudience(title_id,turl):

    url4 = "/reviews/?type=user"
    #url = url1 + url2 + url3
    url = turl + url4
    titleRating = []
    #url = "https://www.rottentomatoes.com/m/harry_potter_and_the_sorcerers_stone/reviews/?page=4&sort="
    request = requests.get(url, 'HEAD')
    if request.status_code == 200:
        soup = readPageFromURL(url)
        if soup != "":
            print "Audience", url
            review = soup.find(id="reviews")
            #GET NUMBER OF PAGES FOR REVIEW
            pageInfo = review.find("span", class_="pageInfo")
            review_table = review.find(class_="review_table")
            if pageInfo is not None:
                pageInfo = cleanup(pageInfo.get_text())
                pageInfo = pageInfo.split(" ")[3]
                pageInfo = int(pageInfo)
                if pageInfo > 5:
                    pageInfo = 5

                for pageNumber in range(1,pageInfo+1):
                    url3 = "/reviews/?page="
                    url4 = str(pageNumber)
                    url5 = "&type=user&sort="
                    url = turl + url3 + url4 + url5
                    request = requests.get(url, 'HEAD')
                    if request.status_code == 200:
                        soup = readPageFromURL(url)
                        if soup != "":
                            #print url
                            review = soup.find(id="reviews")

                            review_table = review.find(class_="review_table")
                            if review_table is not None:
                                sub_review_table = review_table.find_all(class_="review_table_row")
                                for review in sub_review_table:
                                    col_8 = review.find(class_="col-xs-8")
                                    name_link = col_8.find("a")
                                    member_id = name_link.get("href")
                                    member_id = member_id.split("/")
                                    member_id = member_id[3]

                                    col_16 = review.find(class_="col-xs-16")
                                    fl = col_16.find(class_="fl")
                                    if fl is not None:
                                        starts = fl.find_all("span",class_="glyphicon glyphicon-star")
                                        rating = len(starts)
                                    else:
                                        rating = 1

                                    date = col_16.find("span", class_="fr small subtle")
                                    if date is not None:
                                        date = cleanup(date.get_text())
                                    else:
                                        date = ""

                                    user_review = col_16.find(class_="user_review")
                                    if user_review is not None:
                                        review = cleanup(user_review.get_text())
                                    else:
                                        review = ""

                                    #print(member_id,"Audience",rating,date,review)
                                    if date != "":
                                        #print(title_id, member_id, "Audience", "", rating, date, review, "")
                                        row = [title_id, member_id, "Audience", "", rating, date, review, ""]
                                        titleRating.append(row)
                                        appendtocsv("titleRating.csv", titleRating)
                                        titleRating = []

            elif review_table is not None:
                sub_review_table = review_table.find_all(class_="review_table_row")
                for review in sub_review_table:
                    col_8 = review.find(class_="col-xs-8")
                    name_link = col_8.find("a")
                    member_id = name_link.get("href")
                    member_id = member_id.split("/")
                    member_id = member_id[3]

                    col_16 = review.find(class_="col-xs-16")
                    fl = col_16.find(class_="fl")
                    if fl is not None:
                        starts = fl.find_all("span", class_="glyphicon glyphicon-star")
                        rating = len(starts)
                    else:
                        rating = 1

                    date = col_16.find("span", class_="fr small subtle")
                    if date is not None:
                        date = cleanup(date.get_text())
                    else:
                        date = ""

                    user_review = col_16.find(class_="user_review")
                    if user_review is not None:
                        review = cleanup(user_review.get_text())
                    else:
                        review = ""

                    # print(member_id,"Audience",rating,date,review)
                    if date != "":
                        #print(title_id, member_id, "Audience", "", rating, date, review, "")
                        row = [title_id, member_id, "Audience", "", rating, date, review, ""]
                        titleRating.append(row)
                        appendtocsv("titleRating.csv", titleRating)
                        titleRating = []

        else:
            print "PAGE COULD NOT BE READ, ", url




# removeemptyrow("titleRating.csv")

titles = readID("RTTitle.csv")
print "DONE READING FILE"
showID = dict()
for t in titles:
    if t[3] == "Movie":
        getmovieratingaudience(t[0], "https://www.rottentomatoes.com/m/"+t[1])
    elif t[3] == "Season":
        sn = t[1].split("_s")
        sn = sn[len(sn)-1]
        if len(sn) == 1:
            sn = "0"+sn
        url = "https://www.rottentomatoes.com/tv/"+t[2]+"/s"+sn
        getmovierating(t[0], "https://www.rottentomatoes.com/tv/"+t[2]+"/s"+sn)
        getmovieratingaudience(t[0], "https://www.rottentomatoes.com/tv/" + t[2] + "/s" + sn)
    elif t[3] == "Episode":
        sn = t[1].split("_s")
        s = sn[len(sn)-1]
        s = s.split("_")
        s = s[0]
        if len(s) == 1:
            s = "0"+s
        url = "https://www.rottentomatoes.com/tv/"+showID[t[2]]+"/s"+s

        en = sn[len(sn)-1].split("_e")
        #print en
        en = en[len(en) - 1]
        if len(en) == 1:
            en = "0" + en
        url = url+"/e"+en
        #print url
        getepisoderating(t[0], url)
    #     getmovierating(t[0], "https://www.rottentomatoes.com/tv/"+t[2]+"/s"+sn)
    #     getmovieratingaudience(t[0], "https://www.rottentomatoes.com/tv/" + t[2] + "/s" + sn)
    # elif t[3] == "Episode":
    #     #getepisoderating(t[0], "https://www.rottentomatoes.com/tv/game_of_thrones/s07/e03")