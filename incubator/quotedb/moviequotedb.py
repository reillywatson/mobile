import urllib
import urllib2
import BeautifulSoup
import time
import codecs

quotesfile = codecs.open('quotes.txt', 'w', 'utf-8')

def responsestrwithfakemadness(url):
	response = None
	tries = 0
	while response == None:
		try:
			print 'requesting url: ' + url
			user_agent = 'Mozilla/4.0 (compatible; MSIE 5.5; Windows NT)'
			values = {'name' : 'Michael Foord', 'location' : 'Northampton', 'language' : 'Python' }
			headers = { 'User-Agent' : user_agent }
			data = urllib.urlencode(values)
			req = urllib2.Request(url, data, headers)
			response = urllib2.urlopen(req)
		except:
			tries = tries + 1
			if tries == 3:
				return ''
	return response.read()

def createQuote(html, title):
	try:
		quotesfile.write('<quote><film><title>' + title + '</title></film><text>' + html + '</text></quote>\n')
	except: # Amelie doesn't work, I suspect because it's got non-ASCII characters
		pass

def parseMovieQuotePage(url):
	print url
	soup = BeautifulSoup.BeautifulSoup(responsestrwithfakemadness(url))
	divs = soup.findAll(id=True)
	divs = [x for x in divs if x['id'].startswith('quote_')]
	for div in divs:
		createQuote(div.renderContents(), soup.title.getText().split('quotes')[0].strip())
	next = [x for x in soup.findAll('a') if x.getText()=='Next Page&raquo;']
	if len(next) == 1:
		parseMovieQuotePage('http://www.moviequotedb.com' + next[0]['href'])

def moviesForSite(site):
	movies = []
	response = responsestrwithfakemadness(site)
	soup = BeautifulSoup.BeautifulSoup(response)
	links = soup.findAll('a')
	for link in links:
		print link['href']
		if link['href'] != None and link['href'].startswith('/movies'):
			movies.append('http://www.moviequotedb.com' + link['href'])
	return movies

def generateMovieQuotesPages():
	sites = ['http://www.moviequotedb.com/browse/' + chr(x+96) + '.html' for x in range(1,27)]
	sites.append('http://www.moviequotedb.com/browse/num.html');
	file = open('moviepages.txt', 'w')
	for site in sites:
		movies = moviesForSite(site)
		for movie in movies:
			file.write(movie + '\n')

def readMovieQuotesPagesFromFile():
	file = open('moviepages.txt', 'r')
	for url in file:
		parseMovieQuotePage(url)
		time.sleep(3) # let's not hammer this poor site

def main():
	quotePages = readMovieQuotesPagesFromFile()

if __name__ == '__main__':
	main()
