import difflib
import BeautifulSoup
import sys

def main():
	sys.stderr.write('reading file\n')
	file = open('moviequotes.txt').read()
	sys.stderr.write('beautiful souping\n')
	soup = BeautifulSoup.BeautifulStoneSoup(file)
	sys.stderr.write('splitting\n')
	quotes = soup.findAll('quote')
	quoteTexts = [x.textTag.text for x in quotes]
	authors = set([x.text for x in soup.findAll('title')])
	quotesByAuthor = {}
	for quote in quotes:
		author = quote.film.title.text
		if not quotesByAuthor.has_key(author):
			quotesByAuthor[author] = []
		quotesByAuthor[author].append(quote)
	authorNo = 0
	numAuthors = len(authors)
	for author in authors:
		authorNo = authorNo + 1
		try:
			sys.stderr.write(str(authorNo) + ' / ' + str(numAuthors) + ' - ' + author + '(' + str(len(quotesByAuthor[author])) + ')\n')
		except:
			pass
		quoteTextsByAuthor = [x.textTag.text for x in quotesByAuthor[author]]
		for quote in quoteTextsByAuthor:
			matches = difflib.get_close_matches(quote, quoteTextsByAuthor, 3, 0.8)
			if len(matches) > 1:
				try:
					sys.stderr.write('found match')
					sys.stderr.write(matches[0])
					sys.stderr.write(matches[1] + '\n')
				except:
					pass
				matchingTags = [x for x in quotesByAuthor[author] if x.textTag.text in matches]
				for i in range(1, len(matchingTags)):
					matchingTags[i].extract()
	print soup

if __name__ == '__main__':
	main()

