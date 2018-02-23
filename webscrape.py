# python for website scraping
import urllib2

page = urllib2.urlopen('https://www.infoplease.com/homework-help/history/collected-state-union-addresses-us-presidents')
for line in page:
    if 'Collected State of the Union Addresses of U.S. Presidents' in line:
        a = [line]
        print(len(a))
        print(a[0])