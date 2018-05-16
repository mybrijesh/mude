from functions import *


def createMemberID():
    member = dict()
    print("x")
    f1 = readID("member.csv")
    for f in f1:
        if f[0] not in f1 and f[0] != "":
            member[f[0]] = f[1]

    with open("memberFinal.csv", 'a+') as resultFile:
        i = 1
        wr = csv.writer(resultFile, dialect='excel')
        for x in member:
            row = [i, x, member[x]]
            wr.writerow(row)
            i = i + 1


def getMemberCritic(memberID,RTmemberID):
    url = "https://www.rottentomatoes.com/critic/"+RTmemberID
    request = requests.get(url, 'HEAD')
    if request.status_code == 200:
        soup = readPageFromURL(url)
        if soup != "":
            criticPanel = soup.find(id="criticPanel")
            if criticPanel is not None:
                imgcontainer = criticPanel.find("img", class_="critic_thumb")
                imgurl = imgcontainer.get("src")
                if imgurl != "https://staticv2-4.rottentomatoes.com/static/images/redesign/actor.default.tmb.gif":
                    downloadMoviePoster(imgurl, "critic", memberID, memberID)

                first_name = ""
                last_name = ""
                title = criticPanel.find("h2", class_="title")
                if title is not None:
                    name = cleanup(title.get_text())
                    name = name.split(" ")
                    first_name = name[0]
                    if len(name) > 1:
                        last_name = name[1]
                    else:
                        last_name = ""
                print memberID, RTmemberID, first_name, last_name
                criticsSidebar_main = soup.find(id="criticsSidebar_main")
                table = criticsSidebar_main.find("table", class_="info_list")

                desc = ""
                if table is not None:
                    alltr = table.find_all("tr")
                    bio = ""
                    for tr in alltr:
                        td = tr.find_all("td")
                        s = cleanup(td[0].get_text())
                        if "Biography:" in s:
                            bio = cleanup(td[1].get_text())
                        elif "Publications:" in s:
                            alla = td[1].find_all("a")
                            for a in alla:
                                publication_id = cleanup(a.get("href"))
                                row = [memberID,"Critic",RTmemberID,publication_id]
                                ar = []
                                ar.append(row)
                                appendtocsv("MemberPublication.csv", ar)

                    desc = bio
                username = RTmemberID.replace("-","_")
                email = username + "@mude.com"
                row = [memberID,"Critic",email,1,first_name,last_name,username,"password","2018-04-01", desc]
                arr = []
                arr.append(row)
                appendtocsv("dbMember.csv",arr)

def getMemberAudience(memberID, RTmemberID):
    url = "https://www.rottentomatoes.com/user/id/"+RTmemberID
    request = requests.get(url, 'HEAD')
    if request.status_code == 200:
        soup = readPageFromURL(url)
        if soup != "":
            main_container = soup.find(id="main_container")
            if main_container is not None:
                col_left = main_container.find(class_="col-left")
                if col_left is not None:
                    imgcontainer = col_left.find("img")
                    imgurl = imgcontainer.get("src")
                    if imgurl != "https://d2a5cgar23scu2.cloudfront.net/static/images/redesign/actor.default.tmb.gif":
                        if "https:" in imgurl:
                            downloadMoviePoster(imgurl, "audience", memberID, memberID)
                        else:
                            downloadMoviePoster("https:"+imgurl, "audience", memberID, memberID)
                    title = col_left.find("h3")
                    first_name = ""
                    last_name = ""
                    if title is not None:
                        name = cleanup(title.get_text())
                        name = name.split(" ")
                        first_name = name[0]
                        if len(name) > 1:
                            last_name = name[1]
                        else:
                            last_name = ""
                    print memberID, RTmemberID, first_name, last_name
                    username = RTmemberID.replace("-", "_")
                    email = username + "@mude.com"
                    row = [memberID, "Audience", email, 1, first_name, last_name, username, "password", "2018-04-01", ""]
                    arr = []
                    arr.append(row)
                    appendtocsv("dbMember.csv", arr)


# getMemberCritic(str(1),"lisa-kennedy")
# getMemberCritic(str(2),"john-wenzel")
# getMemberCritic(str(3),"cole-smithey")
# getMemberAudience(str(4),"976957778")
# getMemberAudience(str(5),"909712698")
member = readID("memberFinal.csv")
print "FILE READ COMPLETE"
for m in member:
    if m[1] != "":
        if m[2].lower() == "critic":
            getMemberCritic(str(m[0]), m[1])
        else:
            getMemberAudience(str(m[0]), m[1])
        if int(m[0]) % 50 == 0:
            time.sleep(1)