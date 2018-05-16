from bs4 import BeautifulSoup
import csv
import urllib
import urllib2
import time
import unicodedata
import requests
from urlparse import *
import os
import httplib
import mysql.connector
from mysql.connector import errorcode
from mysql.connector import (connection)
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
def getPictureFromCuroselOneFullRestThumbnail(main_container, photopath):
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
                if i == 0:
                    downloadImage(var_d_value[i].get("urls").get("fullscreen"),getPathToSpecificFolder(photopath + "0_heroImage.jpg"))

                downloadImage(var_d_value[i].get("urls").get("thumbnail"), getPathToSpecificFolder(photopath + str(i + 1) + ".jpg"))
                i = i + 1

def checkForPageHeader(url):
    request = requests.get(url, 'HEAD')
    if request.status_code == 200:
        return True
    else:
        return False

def getArtistType(s):
    if "Other" == s:
        return 1
    if "Cast" == s:
        return 2
    if "Director" == s:
        return 3
    if "Writer" == s:
        return 4
    if "Producer" == s:
        return 5
    if "Creator" == s:
        return 6
    return 1

def getGenre(s):
    if s in "Action" or s.replace(" ","") in "Action":
        return 2
    if s in "Animation" or s.replace(" ","") in "Animation":
        return 3
    if s in "Kids&Family" or s.replace(" ","") in "Kids&Family":
        return 10
    if s in "Art&Foreign" or s.replace(" ","") in "Art&Foreign":
        return 4
    if s in "Classics" or s.replace(" ","") in "Classics":
        return 5
    if s in "Comedy" or s.replace(" ","") in "Comedy":
        return 6
    if s in "Documentary" or s.replace(" ","") in "Documentary":
        return 7
    if s in "Drama" or s.replace(" ","") in "Drama":
        return 8
    if s in "Horror" or s.replace(" ","") in "Horror":
        return 9
    if s in "Mystery" or s.replace(" ","") in "Mystery":
        return 11
    if s in "Romance" or s.replace(" ","") in "Romance":
        return 12
    if s in "Sci-Fi&Fantasy" or s.replace(" ","") in "Sci-Fi&Fantasy":
        return 13
    return 1

def getUserType(s):
    if "Audience" == s:
        return 1
    if "Critic" == s:
        return 2
    if "Admin" == s:
        return 3

def getMonthInNumber(s):
    if "Jan" == s:
        return 1
    if "Feb" == s:
        return 2
    if "Mar" == s:
        return 3
    if "Apr" == s:
        return 4
    if "May" == s:
        return 5
    if "Jun" == s:
        return 6
    if "Jul" == s:
        return 7
    if "Aug" == s:
        return 8
    if "Sep" == s:
        return 9
    if "Oct" == s:
        return 10
    if "Nov" == s:
        return 11
    if "Dec" == s:
        return 12

def get_media_type(s):
    if s == "Franchise":
        return 1
    if s == "Movie":
        return 2
    if s == "TVShow":
        return 3
    if s == "Season":
        return 4
    if s == "Episode":
        return 5

def get_media_rating(s):
    if s == "NA":
        return 1
    if s == "G":
        return 2
    if s == "PG":
        return 3
    if s == "PG-13":
        return 4
    if s == "R":
        return 5
    if s == "NC-17":
        return 6
    return 1

def get_in_int(s):
    if len(s) >= 1:
        return int(s)
    else:
        return 0

def connectToDB():
    try:
        cnx = connection.MySQLConnection(user='pine', password='mude2018!',
                                         host='mysql3.cs.stonybrook.edu',
                                         database='pine', port="3306")
        # cnx = connection.MySQLConnection(user='root', password='password',
        #                                  host='127.0.0.1',
        #                                  database='pine', port="3306")
        print "CONNECTED TO DATABASE"
        return cnx
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("Database does not exist")
        else:
            print(err)

def closeConnection(cnx):
    cnx.cursor().close()
    cnx.close()

def readPageFromURL(url):
    incomplete = True
    while incomplete:
        try:
            page = urllib2.urlopen(url)
            soup = BeautifulSoup(page, "html5lib")
            return soup
        except httplib.IncompleteRead:
            incomplete = True
        except Exception as inst:
            incomplete = True
    return ""

def removeemptyrow(filename):
    f = readID(filename)
    exporttocsv(filename, f)

def peebreak(i):
    if i % 100 == 0:
        print "pee break"
        time.sleep(30)

def fileExists(fname):
    return os.path.exists(os.getcwd()+fname)


def readID(filename):
    with open(filename, 'rb') as f:
        reader = csv.reader(f)
        ml = list(reader)
    return ml

def removeSpace(s):
    s = s.replace("\n", "")
    s = s.split(" ")
    s = filter(None, s)
    s = " ".join(s)
    return s

#USE THIS FOR SINGLE COLUMN EXPORT
def exporttocsvsinglecolumn(filename,ls):
    with open(filename,'wb') as resultFile:
        wr = csv.writer(resultFile, dialect='excel')
        for x in ls:
            wr.writerow([x])
def appendtocsvsinglecolumn(filename,ls):
    with open(filename,'a+') as resultFile:
        wr = csv.writer(resultFile, dialect='excel')
        for x in ls:
            wr.writerow([x])
def exportDictionaryToCSV(filename,ls):
    with open(filename, 'wb') as resultFile:
        wr = csv.writer(resultFile, dialect='excel')
        # wr.writerow(ls)
        for x in ls:
            row = [x,ls[x]]
            wr.writerow(row)

def exporttocsv(filename, ls):
    with open(filename, 'wb') as resultFile:
        wr = csv.writer(resultFile, dialect='excel')
        # wr.writerow(ls)
        for x in ls:
            wr.writerow(x)


def appendtocsv(filename, ls):
    with open(filename, 'a+') as resultFile:
        wr = csv.writer(resultFile, dialect='excel')
        # wr.writerow(ls)
        for x in ls:
            wr.writerow(x)

def cleanup(s):
    return removeSpace(unicodedata.normalize('NFKD', unicode(s)).encode('ascii', 'ignore'))

def createImageFolderAtCurrentDirectory(foldername):
    # CREATE IMAGES FOLDER
    current_directory = os.getcwd()
    final_directory = os.path.join(current_directory, r''+foldername)
    if not os.path.exists(final_directory):
        os.makedirs(final_directory)

def getcurrentfolderpath():
    return os.getcwd()

def getPathToSpecificFolder(folderpath):
    if "C:" in os.getcwd():
        folderpath = folderpath.replace("/", "\\")
    # CREATE IMAGES FOLDER
    folder_directory = os.getcwd() + folderpath
    return folder_directory

def createFolder(foldername, folderpath):

    # CHECK TO SEE IF IT'S WINDOWS
    # IF WINDOWS CONVERT / TO \
    if "C:" in os.getcwd():
        folderpath = folderpath.replace("/", "\\")

    # CREATE IMAGES FOLDER
    current_directory = os.getcwd() + folderpath
    final_directory = os.path.join(current_directory, r''+foldername)
    if not os.path.exists(final_directory):
        os.makedirs(final_directory)

def getEpisodeNumberFromString(t):
    en = t.split("_e")
    en = en[len(en) - 1]
    if len(en) == 1:
        en = "0" + en
    return en

def getSeasonNumberFromString(t):
    sn = t.split("_s")
    s = sn[len(sn) - 1]
    s = s.split("_")
    s = s[0]
    if len(s) == 1:
        s = "0" + s
    return s

def downloadImage(imageurl, imagenamewithpath):
    #urllib.urlretrieve(imgurl, person_folder + imageName + ".jpg")
    # CHECK TO SEE IF IT'S WINDOWS
    # IF WINDOWS CONVERT / TO \
    if "C:" in os.getcwd():
        imagenamewithpath = imagenamewithpath.replace("/", "\\")
    if checkForPageHeader(imageurl):
        urllib.urlretrieve(imageurl, imagenamewithpath)
    else:
        "page does not exists ",imageurl

def getImageJPGDestinationPath(foldername, titleid, imgname):
    # FOR WINDOWS
    if "C:" in os.getcwd():
        return foldername + "\\" + titleid + "\\" + imgname +".jpg"
    else:
        return foldername + "/" + titleid + "/" + imgname + ".jpg"

def downloadMoviePoster(imgurl, foldername,titleid,imgname):
    # CHECK IF URL IS VALID
    request = requests.get(imgurl, 'HEAD')
    if request.status_code == 200 and "poster_default.gif" not in imgurl:
        createImageFolderAtCurrentDirectory(foldername)
        createFolder(titleid, "/"+foldername)
        #JUST MAKE SURE IMAGE NAME IS NOT BIG

        if len(imgname) > 20:
            imgname = imgname[0:20]
        p = getImageJPGDestinationPath(foldername, titleid, imgname)
        downloadImage(imgurl, p)
    else:
        "IMAGE DOES NOT EXISTS"

def appendListToList(filename,ls):

    f1 = readID(filename)

    for f in f1:
        if f[0] != "":
            ls.append(f)

    return ls