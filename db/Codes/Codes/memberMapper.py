from functions import *

def memberMapper():

    members = readID("members.csv")
    dbMember = []

    i = 1
    for m in members:
        if m[0] != "":
            m[0] = i
            dbMember.append(m)
            i = i + 1

    exporttocsv("dbMember.csv",dbMember)

# memberMapper()
def makeRatingDBCompatible():

    members = readID("dbMember.csv")
    memberID = dict()
    for m in members:
        m[6] = m[6].replace("_","-")
        if m[6] in memberID:
            print m[6]
        else:
            memberID[m[6]] = m[0]
    print "DICTIONARY CREATED"

    allRatings = []
    print len(allRatings)
    allRatings = appendListToList("movieRating0.csv",allRatings)
    print len(allRatings)
    allRatings = appendListToList("movieRating1.csv", allRatings)
    print len(allRatings)
    allRatings = appendListToList("movieRating2.csv", allRatings)
    print len(allRatings)
    allRatings = appendListToList("movieRating3.csv", allRatings)
    print len(allRatings)
    allRatings = appendListToList("movieRating4.csv", allRatings)
    print len(allRatings)
    allRatings = appendListToList("tvRating.csv", allRatings)
    print "total unfiltered reviews", len(allRatings)
    # print allRatings[len(allRatings)-1]
    finalReviews = []
    publicationID = dict()
    i = 1
    j = 0
    for review in allRatings:
        if review[1] in memberID:
            review[1] = memberID[review[1]]
            finalReviews.append(review)
            i = i + 1

        if review[2] == "Critic" and review[3] != "":

            if review[3] not in publicationID:
                publicationID[review[3]] = review[3]

    # exporttocsv("dbReviews" + str(j) + ".csv", finalReviews)
    exporttocsv("dbReviews.csv",finalReviews)
    exportDictionaryToCSV("RTPublicationID.csv",publicationID)

makeRatingDBCompatible()

def mapPublicationIntoReview():

    publictions = readID("RTPublicationIDMapperToDB.csv")
    pid = dict()
    for p in publictions:
        if p[0] != "" and p[1] not in pid:
            pid[p[1]] = p[0]

    Reviews = readID("dbReviews.csv")
    dbReviews = []
    for rev in Reviews:
        if rev[0] != "":
            if rev[2] == "Critic" and rev[3] in pid:
                rev[3] = pid[rev[3]]
                dbReviews.append(rev)
            elif rev[2] == "Audience":
                dbReviews.append(rev)

    reviewFinal = []
    for r in dbReviews:
        if len(r[0]) > 0:
            r[2] = getUserType(r[2])
            if rev[2] == 1:
                r[3] = 0

            reviewFinal.append(r)

    print len(reviewFinal)
    exporttocsv("dbReviewFinal.csv", reviewFinal)

mapPublicationIntoReview()