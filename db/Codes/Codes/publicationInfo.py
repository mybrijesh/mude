from functions import *

publicationIDs = readID("RTPublicationID.csv")
print "File Read Complete"

RTPublicationIDMapperToDB = []
dbPublication = []
i = 1
for pid in publicationIDs:
    url = "https://www.rottentomatoes.com/" + pid[0]
    if checkForPageHeader(url):
        soup = readPageFromURL(url)
        if soup != "":
            main_container = soup.find(id="main_container")
            if main_container is not None:
                left_col = main_container.find("div", class_="col-center-right")
                if left_col is not None:
                    section = left_col.find("section")
                    if section is not None:
                        h1tag = section.find("h1")
                        if h1tag is not None:
                            publicationName = cleanup(h1tag.get_text())
                            row = [i, publicationName]
                            dbPublication.append(row)

                            row = [i, pid[0]]
                            RTPublicationIDMapperToDB.append(row)

                            print publicationName, i
                            i = i +1

    exporttocsv("dbPublication.csv", dbPublication)
    exporttocsv("RTPublicationIDMapperToDB.csv", RTPublicationIDMapperToDB)

