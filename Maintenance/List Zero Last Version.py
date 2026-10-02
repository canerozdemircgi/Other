from os import fsync
from pathlib import Path

location = input('Type location: ').replace('"', '')

def print_and_write(message, file):
	print(message)
	file.write(str(message) + '\n')
	file.flush()
	fsync(file.fileno())

with open('List Zero Last Version.txt', 'w', encoding='utf-8', newline='\n') as file_log:
	for file_path in Path(location).rglob('*.*'):
		file_name_parts = file_path.name.split('.')
		if len(file_name_parts) > 1 and file_name_parts[-2] and all(file_name_last_version_char == '0' for file_name_last_version_char in file_name_parts[-2]):
			print_and_write(file_path, file_log)