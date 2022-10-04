from lxml import etree
from glob import glob
import bz2
import codecs
import csv
import time
import os

PATH_WIKI_XML = "/Users/light/Documents/Wikipedia/"
FILENAME_ARTICLES = "articles.csv"


def hms_string(sec_elapsed):
    h = int(sec_elapsed / (60 * 60))
    m = int((sec_elapsed % (60 * 60)) / 60)
    s = sec_elapsed % 60
    return "{}:{:>02}:{:>05.2f}".format(h, m, s)


def get_parser(filename):
    ns_token = "{http://www.mediawiki.org/xml/export-0.10/}ns"
    title_token = "{http://www.mediawiki.org/xml/export-0.10/}title"
    revision_token = "{http://www.mediawiki.org/xml/export-0.10/}revision"
    text_token = "{http://www.mediawiki.org/xml/export-0.10/}text"

    with bz2.BZ2File(filename, "rb") as bz2_file:
        for event, element in etree.iterparse(bz2_file, events=("end",)):
            if element.tag.endswith("page"):
                namespace_tag = element.find(ns_token)

                if namespace_tag.text == "0":
                    title_tag = element.find(title_token)
                    text_tag = element.find(revision_token).find(text_token)
                    yield title_tag.text, text_tag.text

                element.clear()


def pluck_wikipedia_titles_text(
    out_file, pattern="enwiki-*-pages-articles-multistream*.xml-*.bz2"
):
    totalCount = 0
    longestTitle = 0
    longestText = 0
    with codecs.open(out_file, "a+b", "utf8") as out_file:
        writer = csv.writer(out_file)
        for bz2_filename in sorted(
            glob(pattern),
            key=lambda a: int(a.split("articles-multistream")[1].split(".")[0]),
        ):
            print(bz2_filename)
            parser = get_parser(bz2_filename)
            for title, text in parser:
                if not (text.startswith("#REDIRECT") or text.startswith("#redirect")):
                    totalCount += 1
                    writer.writerow([title, text])
                    longestTitle = (
                        len(title) if len(title) > longestTitle else longestTitle
                    )
                    longestText = len(text) if len(text) > longestText else longestText
                    if totalCount > 1 and (totalCount % 100000) == 0:
                        print("{:,}".format(totalCount))
                    if totalCount >= 1000:
                        break
    print(
        f"Total Count: {totalCount}\nLongest Title: {longestTitle}\nLongest Text: {longestText}"
    )


pathArticles = os.path.join(PATH_WIKI_XML, FILENAME_ARTICLES)

start_time = time.time()

pluck_wikipedia_titles_text(pathArticles)

time_took = time.time() - start_time
print(f"Total runtime: {hms_string(time_took)}")
