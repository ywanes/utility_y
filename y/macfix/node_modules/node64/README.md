node64
======

Simple file to Base64 encoder

## How to

	npm install node64 -g

	Usage: node64.js [options] file

	Options:

	  -h, --help                output usage information
	  -V, --version             output the version number
	  -o, --out [output]        Output to file (default is print to console)
	  -d, --data                add Data URI scheme prefix (data: and ;base64,)
	  -m, --mime [MIME-type]    add MIME-type prefix if -d flag is on (image/png)
	  -c, --charset [encoding]  add charset prefix if -d flag is on (UTF-8)

### Example

Convert README.md to Base64 string and write it to output.txt

	node64 -o output.txt README.md

Convert /foo/bar/image.png to browser readable Data URI and write it to base64.txt

	node64 -d -m image/png -o base64.txt /foo/bar/image.png