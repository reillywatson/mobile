import difflib
import BeautifulSoup

def main():
	file = open('quotes1.txt').read()
	soup = BeautifulSoup.BeautifulStoneSoup(file)
	quotes = [str(x.text) for x in soup.findAll('text')]
	suspiciousQuotes = []
	for quote in quotes:
		matches = difflib.get_close_matches(quote, quotes, 3, 0.8)
		if len(matches) > 1:
			print 'hey these are awfully similar:'
			print matches

if __name__ == '__main__':
	main()

