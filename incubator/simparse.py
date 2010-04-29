import BeautifulSoup
import urllib
import urllib2
import sys

def responsestrwithfakemadness(url):
	user_agent = 'Mozilla/4.0 (compatible; MSIE 5.5; Windows NT)'
	values = {'name' : 'Michael Foord',
		  'location' : 'Northampton',
		  'language' : 'Python' }
	headers = { 'User-Agent' : user_agent }

	data = urllib.urlencode(values)
	req = urllib2.Request(url, data, headers)
	response = urllib2.urlopen(req)
	return response.read()

def striplinks(tag):
	for sub in tag.findAll('a'):
		sub.replaceWith(sub.text)

def parse(url):
	urlparts = url.split('/')
	outputfile = urlparts[len(urlparts) - 1].lower()
	quotes = open(outputfile + ".txt", "w")
	print "Retrieving data from URL: " + url
	instr = responsestrwithfakemadness(url)
	soup = BeautifulSoup.BeautifulSoup(instr)
	seasonName = ""
	eptitle = ""
	contents = soup.find(id="bodyContent").contents
	for tag in contents:
		if not 'name' in dir(tag):
			pass
		else:
			if tag.name == "h2":
				seasonName = tag.text.replace("[edit]", "").strip()
				print seasonName
				if seasonName.lower().startswith('season ') or seasonName.lower().startswith('series '):
					seasonName = seasonName + '\n'
				else:
					seasonName=""
			if seasonName != "":
				if tag.name == "h3":
					eptitle = tag.text.replace("[edit]", "").strip()
					# [TODO] html decoding?
					eptitle = eptitle.replace('&amp;','&').replace('&gt;','>').replace('&lt;','<')
					print eptitle
					eptitle = eptitle + '\n'
				elif tag.name == "dl":
					striplinks(tag)
					epquote = tag.prettify().replace("\n", "").replace('%','&#37;')
					try:
						quotes.write(seasonName + eptitle + epquote + '\n')
					except UnicodeDecodeError:
						pass
					except UnicodeEncodeError:
						pass


def main():
	if len(sys.argv) != 2:
		print "usage: simparse url"
		return
	url = sys.argv[1]
	parse(url)

if __name__ == '__main__':
	main()
