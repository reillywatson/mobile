import BeautifulSoup
import urllib2

def main():
	quotes = open("/Users/reilly/quotes.txt", "w")
	infile = urllib2.urlopen("file:///Users/reilly/hgrepo/incubator/simpsonswikiquote.htm")
	soup = BeautifulSoup.BeautifulSoup(infile)
	seasonName = ""
	eptitle = ""
	for tag in soup.body.contents:
		if not 'name' in dir(tag):
			pass
		elif tag.name == "h2":
			seasonName = tag.contents[2].contents[0].contents[0]
			print seasonName
			if 'text' in seasonName:
				seasonName = seasonName.text
			seasonName = seasonName + '\n'
		elif tag.name == "h3":
			eptitle = tag.contents[2].contents[0].contents[0]
			if 'title' in eptitle:
				eptitle = eptitle['title']
			if 'text' in dir(eptitle):
				eptitle = eptitle.text
			print eptitle
			eptitle = eptitle + '\n'
		elif tag.name == "dl":
			for sub in tag.findAll('a'):
				sub.replaceWith(sub.text)
			epquote = tag.prettify().replace("\n", "")
			try:
				quotes.write(seasonName + eptitle + epquote + '\n')
			except UnicodeDecodeError:
				pass
			except UnicodeEncodeError:
				pass


if __name__ == '__main__':
	main()