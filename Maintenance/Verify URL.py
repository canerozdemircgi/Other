import ssl
from os import fsync
from pathlib import Path
from urllib import request

headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36'}
location = input('Type location: ').replace('"', '')

def print_and_write(message, file):
	print(message)
	file.write(str(message) + '\n')
	file.flush()
	fsync(file.fileno())

with open('Verify URL.txt', 'w', encoding='utf-8', newline='\n') as file_log:
	for file_url_path in Path(location).rglob('*.url'):
		with open(file_url_path, 'r', encoding='utf-8') as file_url:
			data_url = file_url.read().split('\n')
			if len(data_url) > 2:
				print_and_write('Corrupted - File: {}'.format(file_url_path), file_log)
				continue
			address_url = data_url[1].replace('URL=', '')
		try:
			with request.urlopen(request.Request(address_url, headers=headers), context=ssl._create_unverified_context(), timeout=16) as response:
				address_url_redirect = response.geturl()
				if address_url == address_url_redirect:
					print('OK - File: {}, Address: {}'.format(file_url_path, address_url))
				else:
					print_and_write('Redirected - File: {}, Address_Original: {}, Address_Final: {}'.format(file_url_path, address_url, address_url_redirect), file_log)
		except BaseException as ex:
			print_and_write('Error - File: {}, Address: {}, Exception: {}'.format(file_url_path, address_url, ex), file_log)