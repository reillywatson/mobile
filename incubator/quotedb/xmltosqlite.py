from pysqlite2 import dbapi2 as sqlite
import codecs

def main():
	con = sqlite.connect('quotes.sqlite')
	file = codecs.open('test2.txt', 'r', 'utf-8')
	lineNo = 0
	for line in file:
		lineNo = lineNo + 1
		if lineNo % 100 == 0:
			print 'processing line ' + str(lineNo)
		groups = line.split('<')
		author = [x.lstrip('author>') for x in groups if x.startswith('author>')]
		if len(author) == 1:
			author = author[0]
			quoteText = [x.lstrip('text>') for x in groups if x.startswith('text>')][0]
			cats = [x.lstrip('cat>') for x in groups if x.startswith('cat>')]
			cats = ','.join(cats)
			cur = con.cursor()
			values = {'author':author, 'quote':quoteText}
			if cats != '':
				values['categories'] = cats
				cur.execute('insert into Quotes(Author, Quote, Categories) values (:author, :quote, :categories)', values)
			else:
				cur.execute('insert into Quotes(Author, Quote) values (:author, :quote)', values)
	con.commit()

if __name__ == '__main__':
	main()

