import BeautifulSoup
import urllib2
import os

def main():
	quotes = open("quotesout.txt", "w")
	os.system("wget http://en.wikiquote.org/wiki/Arrested_Development --output-document=wgetoutput")
	infile = open("wgetoutput", "r")
	instr = infile.read();
	infile.close();
	os.remove("wgetoutput")
	soup = BeautifulSoup.BeautifulSoup(instr)
	seasonName = ""
	eptitle = ""
	contents = soup.find(id="bodyContent").contents
	for tag in contents:
		if not 'name' in dir(tag):
			pass
		else:
			if tag.name == "h2":
				seasonName = tag.text.replace("[edit]", "")
				print seasonName
				if seasonName.lower().find('season') > -1:
					seasonName = seasonName + '\n'
				else:
					seasonName=""
			if seasonName != "":
				if tag.name == "h3":
					eptitle = tag.text.replace("[edit]", "")
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
