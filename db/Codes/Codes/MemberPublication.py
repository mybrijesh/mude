from functions import *

membersID = readID("dbMember.csv")
publicationID = readID("RTPublicationIDMapperToDB.csv")

memDict = dict()
pubDict = dict()

for mem in membersID:
    mem[6] = mem[6].replace("_","-")
    memDict[mem[6]] = mem[0]
print len(membersID), " - ", len(memDict)
for mem in publicationID:
    if mem[1] not in pubDict:
        pubDict[mem[1]] = mem[0]
print len(publicationID), " - ", len(pubDict)

MPRelation = readID("MemberPublication.csv")

dbMemberPublicationRelation = []

for mp in MPRelation:
    if mp[0] != "":
        mp[3] = mp[3].replace("/","")
        if mp[3] in pubDict:
            row = [memDict[mp[2]],pubDict[mp[3]]]
            dbMemberPublicationRelation.append(row)

exporttocsv("dbMemberPublicationRelation.csv",dbMemberPublicationRelation)

