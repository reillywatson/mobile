import cookielib
import urllib
import BeautifulSoup
import codecs
import re
import time
import shutil

import socket
socket.setdefaulttimeout(10)

import sys
sys.setrecursionlimit(3000)

def sqlite2cookie(filename):
	from cStringIO import StringIO
	from pysqlite2 import dbapi2 as sqlite
	shutil.copy(filename, 'test.sqlite')
	con = sqlite.connect('test.sqlite')
	cur = con.cursor()
	cur.execute("select host, path, isSecure, expiry, name, value from moz_cookies")
	dict = {}
	for item in cur.fetchall():
		if item[0].find('en.thinkexist.com') > -1:
			dict[item[4]] = item[5]
	return dict

def fetch(url, data = None, cookies = None):
	print url
	opener = urllib.FancyURLopener()
	if isinstance(data, dict): data = urllib.urlencode(data)
	if isinstance(cookies, dict):
		# TODO: find a better way to do this
		cookies = "; ".join([str(key) + "=" + str(cookies[key]) for key in cookies])
		opener.addheader("Cookie", cookies)
	obj = opener.open(url, data)
	result = obj.read()
	obj.close()
	return result


cookies = sqlite2cookie('/home/reilly/.mozilla/firefox/k8v5btho.default/cookies.sqlite')
def getQuotePage(url):
	data = {
	'Host':'en.thinkexist.com',
	'User-Agent':'Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.1.9) Gecko/20100401 Ubuntu/9.10 (karmic) Firefox/3.5.9'
	}
	response = None
	tries = 0
	while response == None:
		time.sleep(1) # not too fast, now...
		try:
			response = fetch(url, data,cookies)
		except:
			tries = tries + 1
			if tries == 3:
				return ''
	return response

def parseQuotePage(html):
	soup = BeautifulSoup.BeautifulSoup(html)
	quotes = soup.findAll(attrs={'class' : 'sqq'})
	author = re.sub(' quotes', '', soup.title.text)
	resultQuotes = []
	for quote in quotes:
		categories = []
		text = re.sub('&rdquo;$', '', re.sub('^&ldquo;', '', quote.text))
		categoryTags = quote.parent.parent.parent.findAll(attrs={'class':'sqll'})
		for c in categoryTags:
			category = str(c.text).replace('&nbsp;quotes', '')
			if category != 'Similar&nbsp;Quotes' and category != 'T-Shirt':
				categories.append(category)
		resultQuotes.append([text, author, categories])
	if len(resultQuotes) == 0:
		print 'no results! HTML: ' + html
	nextLinkTag = [x for x in soup.findAll('a', attrs={'class':'mf'}) if x.next.attrMap.has_key('src') and x.next.attrMap['src']=='/images/paging/next.gif']
	if len(nextLinkTag) > 0:
		nextLink = 'http://en.thinkexist.com' + nextLinkTag[0]['href']
		nexthtml = getQuotePage(nextLink)
		if nexthtml == None or nexthtml == '':
			return None
		print 'link: ' + nextLink
		resultQuotes.extend(parseQuotePage(nexthtml))
	return resultQuotes

#authorsfile = codecs.open('thinkexistauthors.txt', 'a', 'utf-8')
def parseAuthorPages(url):
	soup = BeautifulSoup.BeautifulSoup(fetch(url))
	links = soup.findAll('a', attrs={'class':'ma'})
	for l in links:
		if l.parent.img['alt'].find('popularity 0/10') == -1:
			print l['href']
			author = l['href']
			if not author.startswith('http://'):
				author = u'http://en.thinkexist.com' + author
			authorsfile.write(author + u'\n')
	next = soup.findAll('a',attrs={'class':'mf'})
	if len(next) > 0:
		time.sleep(1)
		url = str(next.pop()['href'])
		print 'url: ' + url
		if not url.startswith('http://'):
			url = 'http://en.thinkexist.com' + url
		parseAuthorPages(url)

def writeQuotes(file, quotes):
	for quote in quotes:
		file.write('<quote>')
		file.write('<text>')
		file.write(quote[0])
		file.write('</text>')
		file.write('<author>')
		file.write(quote[1])
		file.write('</author>')
		if len(quote[2]) > 0:
			file.write('<categories>')
			for cat in quote[2]:
				file.write('<cat>')
				file.write(cat)
				file.write('</cat>')
			file.write('</categories>')
		file.write('</quote>\n')

#def main():
#	parseAuthorPages('http://en.thinkexist.com/quotesbyletter/heo-hor/')

def main():
	authors = open('thinkexistauthors.txt')
	parsedfile = open('thinkexistauthorsparsed.txt')
	parsedAuthors = []
	for a in parsedfile:
		parsedAuthors.append(a)
	parsedfile.close()
	parsedfile = codecs.open('thinkexistauthorsparsed.txt', 'a', 'utf-8')	
	output = codecs.open('thinkexist.txt', 'a', 'utf-8')
	for author in authors:
		if not author in parsedAuthors:
			try:
				html = getQuotePage(author)
				if html != None and html != '':
					results = parseQuotePage(html)
					if results != None:
						print results
						writeQuotes(output, results)
						parsedfile.write(author)
						parsedAuthors.append(author)
			except:
				pass
	output.close()
	parsedfile.close()

if __name__ == '__main__':
	main()
