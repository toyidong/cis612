import bs4
import requests
import unicodecsv as csv
import os 

"""global variables, start_url for start webpage, domain_url for adding domain name to the link on start webpage
   filename_index increment everytime after saving one webpage into .txt file
"""
dir_path = os.path.dirname(os.path.realpath(__file__))
# print(dir_path)


start_url = 'https://www.infoplease.com/homework-help/history/collected-state-union-addresses-us-presidents'
domain_url = 'https://www.infoplease.com/homework-help/us-documents/state-union-address'
filename_index = 1

"""function to generate linked webpage address
   replace character and add domain_url, 
   return as a string
"""
def parse_URL(string):
	if u'.' in string:
		string = string.replace(u'.', u'')
	# string.replace(u' ', u'-')
	string = string.replace(u',', u'')
	string = string.replace(u'(', u'')
	string = string.replace(u')', u'')
	string = string.replace(u' ', u'-')
	return domain_url + "-" + string

"""function to generate file_name
   concatenate with global variable filename_index, then increment filename_index, 
   return as a string
"""
def get_file_name():
	global filename_index
	s = "InfoUnionAddress_" + str(filename_index) + ".txt"
	filename_index += 1
	return s

"""function to check the linked webpage address
   for convenience, I searched on google to get each webpage address, then hardcode  
   return as a string
"""
def check_URL(link_to_address):
	if u'1805' in link_to_address:
		link_to_address = u'https://www.infoplease.com/homework-help/us-documents/thomas-jefferson-december-3-1805'
	if u'1922' in link_to_address:
		link_to_address = u'https://www.infoplease.com/homework-help/us-documents/warren-harding-december-8-1922' 
	if u'Chester-A-Arthur' in link_to_address:
		link_to_address = link_to_address.replace(u'-A-', u'-')
	if u'1940' in link_to_address:
		link_to_address = u'https://www.infoplease.com/homework-help/us-documents/franklin-d-roosevelt-january-3-1940'
	if u'1941' in link_to_address:
		link_to_address = u'https://www.infoplease.com/homework-help/us-documents/franklin-d-roosevelt-january-6-1941'
	if u'1958' in link_to_address:
		link_to_address = u'https://www.infoplease.com/homework-help/us-documents/dwight-d-eisenhower-january-9-1958'
	if u'1959' in link_to_address:
		link_to_address = u'https://www.infoplease.com/homework-help/us-documents/dwight-d-eisenhower-january-9-1959'
	if u'1960' in link_to_address:
		link_to_address = u'https://www.infoplease.com/homework-help/us-documents/dwight-d-eisenhower-january-7-1960'
	if u'12-1961' in link_to_address:
		link_to_address = u'https://www.infoplease.com/homework-help/us-documents/dwight-d-eisenhower-january-12-1961'
	if u'30-1961' in link_to_address:
		link_to_address = u'https://www.infoplease.com/homework-help/us-documents/john-f-kennedy-january-30-1961'
	if u'1962' in link_to_address:
		link_to_address = u'https://www.infoplease.com/homework-help/us-documents/john-f-kennedy-january-11-1962'
	if u'1963' in link_to_address:
		link_to_address = u'https://www.infoplease.com/homework-help/us-documents/john-f-kennedy-january-14-1963'
	if u'1964' in link_to_address:
		link_to_address = u'https://www.infoplease.com/homework-help/us-documents/lyndon-b-johnson-january-8-1964'
	if u'1965' in link_to_address:
		link_to_address = u'https://www.infoplease.com/homework-help/us-documents/lyndon-b-johnson-january-4-1965'
	if u'1981' in link_to_address:
		link_to_address = u'https://www.infoplease.com/homework-help/us-documents/jimmy-carter-january-16-1981'
	if u'1982' in link_to_address:
		link_to_address = u'https://www.infoplease.com/homework-help/us-documents/ronald-reagan-january-26-1982'
	return link_to_address




def get_link():
	r = requests.get(start_url)
	soup = bs4.BeautifulSoup(r.text, 'lxml')
	
	all_links = soup.find_all('a')
	text_list = []
	link_list = []
	for link in all_links:
		if '(' in link.text:
			text_list.append(link.text)
			link_to_address = (parse_URL(link.text))
			tokens = link.text.split('(')
			president_name = tokens[0]
			date_of_union_address = tokens[1].replace(u')', u'')

			file_name = get_file_name()
			filename_address = dir_path + file_name
			link_list.append(link_to_address)
			link_to_address = check_URL(link_to_address)
			if link_to_address is not None:
				text_of_address = get_text_of_Union_Address(link_to_address).replace(u'\n', u' ')

			text_file = open(file_name, 'wb')
			big_file = open('big_file.txt', 'a')
			csv_file = open('table.csv', 'a')
			text_file.write((link.text+text_of_address).encode('ascii', 'ignore'))
			big_file.write((link.text+text_of_address+'\n\n').encode('ascii', 'ignore'))

			csv_output = president_name+"\t"+date_of_union_address+"\t"+link_to_address+"\t"+filename_address+"\t"+text_of_address+"\n"
			csv_file.write(csv_output.encode('ascii', 'ignore'))
			text_file.close()
			big_file.close()


def get_text_of_Union_Address(parsed_URL):
	global filename_index
	try:
		s = ""
		r1 = requests.get(parsed_URL)
		soup = bs4.BeautifulSoup(r1.text, 'lxml')
	
		article = soup.findChild('article')

		div1 = article.find('div')
		div2 = div1.find_all('p')
		# print(parsed_URL,div2.text, filename_index)
		for para in div2:
			if para.text is not None:
				s += para.text
		return s
				

	except Exception as e:
		print("err link", parsed_URL)
		pass
	else:
		pass
	finally:
		pass

"""entry point for .py file, first create a table.csv file, use delimiter as "\t",
   write the column name
   then call get_link() function to start
   after all job done, print out output file saving path
"""
if __name__ == '__main__':
		
	with open('table.csv', 'wb') as csvfile:
		writer = csv.writer(csvfile, delimiter='\t')
		writer.writerow(['Name of President', 'Date of Union Address', 'Link to Address', 'Filename of Address', 'Text of Address'])

	get_link()
	
	print("\noutput files are saved in: " + dir_path +"\n")

