import cgi
import datetime
import wsgiref.handlers
import random

from google.appengine.ext import db
from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.db import stats

class HotItem(db.Model):
	imageUrl = db.StringProperty()
	avgRating = db.StringProperty()
	numRatings = db.StringProperty()
	rateID = db.StringProperty()
	rand = db.FloatProperty()


class GetItems(webapp.RequestHandler):
	def get(self):
		hotItems = []
		numResults = self.request.get('NumResults')
		if numResults == '':
			numResults = 1
		else:
			numResults = int(numResults)
		# I guess there's a way to do this to get all the items at once, but I can't figure it out...
		for i in range(numResults):
			rand = random.random()
			results = db.GqlQuery("SELECT * FROM HotItem WHERE rand > :1 LIMIT 1",rand)
			for result in results:
				hotItems.append(result)
		# we need to guarantee there's an item in there with a rand value of 1 otherwise our select will sometimes return nothing
		if len(hotItems) == 0:
			hotItem = getInitialItem()
			hotItems.append(hotItem)
			hotItem.put()
		self.response.out.write('<?xml version="1.0" encoding="utf-8"?>')
		self.response.out.write("<HotItems>")
		for item in hotItems:
			self.response.out.write("<HotItem>")
			self.response.out.write("<RatingID>" + item.rateID + "</RatingID>")
			self.response.out.write("<ImageURL>" + item.imageUrl + "</ImageURL>")
			self.response.out.write("<AverageRating>" + item.avgRating + "</AverageRating>")
			self.response.out.write("<NumRatings>" + item.numRatings + "</NumRatings>")
			self.response.out.write("</HotItem>")
		self.response.out.write("</HotItems>")



def getInitialItem():
	hotItem = HotItem()
	hotItem.imageUrl = 'http://p1.hotornot.com/pics/H8/HZ/KL/NY/RUHLAZOUQRTX.jpg'
	hotItem.avgRating = '9.9'
	hotItem.numRatings = '5467'
	hotItem.rateId = '8604794'
	return hotItem

class SubmitRating(webapp.RequestHandler):
	def post(self):
		hotItem = HotItem()
		hotItem.rand = random.random()
		if self.request.get('ImageURL') != None and self.request.get('ImageURL') != '':
			existingResult = db.GqlQuery("SELECT * FROM HotItem WHERE ImageURL = :1 LIMIT 1",self.request.get('ImageURL'))
			for result in existingResult:
				hotItem = result
			hotItem.imageUrl = self.request.get('ImageURL')
			hotItem.avgRating = self.request.get('AverageRating')
			hotItem.numRatings = self.request.get('NumRatings')
			hotItem.rateID = self.request.get('RatingID')
			hotItem.put()
			self.response.out.write('SUCCESS')
		else:
			self.response.out.write('Please specify ImageURL, AverageRating, NumRatings, and RatingID as POST params')

	def get(self):
		self.response.out.write('Please specify ImageURL, AverageRating, NumRatings, and RatingID as POST params')

application = webapp.WSGIApplication([
  ('/', GetItems),
  ('/submit', SubmitRating)
], debug=True)


def main():
  wsgiref.handlers.CGIHandler().run(application)


if __name__ == '__main__':
  main()

