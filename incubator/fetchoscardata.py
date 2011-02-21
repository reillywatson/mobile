from pysqlite2 import dbapi2 as sqlite
import BeautifulSoup
import urllib
import urllib2
import codecs

def fetch(url):
	print url
	opener = urllib.FancyURLopener()
	obj = opener.open(url)
	result = obj.read()
	obj.close()
	return result

def quotes():
	con = sqlite.connect('oscars.sqlite')
	print 'Parsing file'
	soup = BeautifulSoup.BeautifulStoneSoup(codecs.open('quotedb/moviequotes.txt', 'r', 'utf-8').read())
	print 'File parsed'
	quotes = soup.findAll('quote')
	for quote in quotes:
		print quote.title.text
		filmTitle = quote.title.text
		try:
			cur = con.cursor()
			x = cur.execute("select count(*) from Oscar where Film = :Film", {"Film":filmTitle})
			count = x.next()[0]
			if count > 0:
				quoteText = quote.textTag.prettify().replace('<text>','').replace('</text>','')
				cur = con.cursor()
				cur.execute('insert into Quote(Film, Quote) Values (:Film, :Quote)', { 'Film':filmTitle, 'Quote':quoteText })
				con.commit()
		except sqlite.InterfaceError:
			print 


def pic():
	con = sqlite.connect('oscars.sqlite')
	soup = BeautifulSoup.BeautifulSoup(open('/Users/reilly/out.html').read())
	pictures = soup.findAll('i')
	for p in pictures:
		values = {'AwardName':'Best Picture'}
		big = None
		x = p
		while big == None and x != None:
			big = x.big
			x = x.parent
		values['Year'] = big.text[:4]
		values['Film'] = p.text
		values['IsWinner'] = p.parent.parent.has_key('style') and p.parent.parent['style'] == u'background:#FAEB86'
		values['Actors'] = p.parent.parent.nextSibling.nextSibling.text
		print values
		cur = con.cursor()
		cur.execute('insert into Oscar(AwardName,Year,Film,Actors,IsWinner) Values (:AwardName, :Year, :Film, :Actors, :IsWinner);', values)
		con.commit()



def director():
	con = sqlite.connect('oscars.sqlite')
	values = {'IsWinner':False, 'AwardName':'Best Director'}
	elements = codecs.open('actors.txt', 'r', 'utf-8').read().split('\t')
	for a in elements:
		for b in a.split('\n'):
			for x in b.split('-'):
				x = x.strip()
				if x == '':
					continue
				if x.isdigit():
					values['Year'] = int(x)
					values['IsWinner'] = True
				elif not values.has_key('Nominee'):
					values['Nominee'] = x
				elif not values.has_key('Film'):
					values['Film'] = x
					cur = con.cursor()
					print values
					cur.execute('insert into Oscar(AwardName,Year,Film,Nominee,IsWinner) Values (:AwardName, :Year, :Film, :Nominee, :IsWinner);', values)
					values = {'AwardName':'Best Director', 'IsWinner':False, 'Year':values['Year']}
	con.commit()

def go():
	con = sqlite.connect('oscars.sqlite')
	year = 0
	for line in codecs.open('actors.txt', 'r', 'utf-8'):
		if len(line.split(' ')) == 1:
			continue
		winner = False
		firstword = line.split(' ')[0]
		if firstword.isdigit():
			year = int(firstword)
			line = line.replace(firstword, '').strip()
			winner = True
		values = {'AwardName':'Best Actress', 'Year':year, 'IsWinner':winner}
		
		actor = line.split(' - ')[0].strip()
		values['Nominee'] = actor
		restofline = line[len(actor)+3:]
		if len(restofline.split(' as ')) > 2:
			print 'OH NOOOOO' + line
		film = restofline.split(' as ')[0].strip()
		values['Film'] = film
		role = restofline.split(' as ')[1].strip()
		values['Role'] = role
		cur = con.cursor()
		print values
		cur.execute('insert into Oscar(AwardName,Year,Film,Nominee,Role,IsWinner) Values (:AwardName, :Year, :Film, :Nominee, :Role, :IsWinner);', values)
		con.commit()
