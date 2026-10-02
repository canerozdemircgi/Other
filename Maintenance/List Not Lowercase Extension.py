from os import fsync
from pathlib import Path

location = input('Type location: ').replace('"', '')

def print_and_write(message, file):
	print(message)
	file.write(str(message) + '\n')
	file.flush()
	fsync(file.fileno())

with open('List Not Lowercase Extension.txt', 'w', encoding='utf-8', newline='\n') as file_log:
	for file_path in Path(location).rglob('*.*'):
		if not file_path.is_dir():
			file_suffix = file_path.suffix
			if file_suffix == '':
				file_suffix = file_path.name
			if not file_suffix[1:].islower():
				print_and_write(file_path, file_log)