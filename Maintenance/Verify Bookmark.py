import ssl
from os import fsync
from re import search
from urllib import request

headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36'}
location = input('Type location: ').replace('"', '')

def print_and_write(message, file):
	print(message)
	file.write(str(message) + '\n')
	file.flush()
	fsync(file.fileno())

with open('Verify Bookmark.txt', 'w', encoding='utf-8', newline='\n') as file_log:
	with open(location, 'r', encoding='utf-8') as file_bookmark:
		lines_bookmark = [line_bookmark.lower() for line_bookmark in file_bookmark.read().split('\n') if ' href="' in line_bookmark.lower()]
	for line_bookmark in lines_bookmark:
		address_url = search(r' href=[\'"]?([^\'" >]+)', line_bookmark).group(1)
		try:
			with request.urlopen(request.Request(address_url, headers=headers), context=ssl._create_unverified_context(), timeout=16) as response:
				address_url_redirect = response.geturl()
				if address_url == address_url_redirect:
					print('OK - Address: {}'.format(address_url))
				else:
					print_and_write('Redirected - Address_Original: {}, Address_Final: {}'.format(address_url, address_url_redirect), file_log)
		except BaseException as ex:
			print_and_write('Error - Address: {}, Exception: {}'.format(address_url, ex), file_log)