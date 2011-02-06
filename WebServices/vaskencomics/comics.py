import cgi
import datetime
import wsgiref.handlers
import random

from google.appengine.ext import db
from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.db import stats

class Comic(db.Model):
	name = db.StringProperty()
	json = db.StringProperty(multiline=True)
	platform = db.StringProperty()

class VersionInfo(db.Model):
	latest = db.IntegerProperty()
	platform = db.StringProperty()

class AddComic(webapp.RequestHandler):
	def get(self):
		self.post()
	def post(self):
		name = self.request.get('name')
		json = self.request.get('json')
		platform = self.request.get('platform')
		if name == '' or json == '' or platform == '':
			self.response.out.write('required: name, json, platform')
			return
		latestVersion = db.GqlQuery("SELECT * FROM VersionInfo WHERE platform = :1", platform)
		versionInfos = []
		for v in latestVersion:
			v.latest = v.latest + 1
			versionInfos.append(v)
		if len(versionInfos) == 0:
			v = VersionInfo()
			v.platform = platform
			v.latest = 1
			versionInfos.append(v)
		for v in versionInfos:
			v.put()
		comics = []
		results = db.GqlQuery("SELECT * FROM Comic WHERE name = :1 AND platform = :2", name, platform)
		for r in results:
			comics.append(r)
		if len(comics) == 0:
			c = Comic()
			c.name = name
			c.platform = platform
			comics.append(c)
		for comic in comics:
			comic.json = json
			comic.put()
		self.response.out.write('Success!')

class GetJSON(webapp.RequestHandler):

	def checkVersion(self):
		version = self.request.get("Version")
		platform = self.request.get("Platform")
		if version == '':
			self.response.out.write("VERSION AND PLATFORM REQUIRED")
			return True
		versionResults = db.GqlQuery("SELECT * FROM VersionInfo WHERE platform = :1", platform)
		latestVersion = 1
		for v in versionResults:
			latestVersion = v.latest
		if latestVersion <= int(version):
			self.response.out.write("Up to date")
			return True
		return False

	def get(self):
		if self.checkVersion():
			return
		platform = self.request.get("Platform")		
		results = db.GqlQuery("SELECT * FROM Comic WHERE platform = :1", platform)
		
		self.response.out.write("[")
		numresults = results.count()
		i = 0
		for comic in results:
			self.response.out.write(comic.json)
			
			if i < numresults - 1:
				self.response.out.write(",")
			i = i + 1
		self.response.out.write("]")


application = webapp.WSGIApplication([
  ('/', GetJSON),
  ('/add', AddComic),
], debug=True)


def main():
  wsgiref.handlers.CGIHandler().run(application)


if __name__ == '__main__':
  main()

