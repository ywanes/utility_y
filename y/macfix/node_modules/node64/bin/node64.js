#!/usr/bin/env node
var program = require('commander'), fs = require('fs');
program.version('0.0.1')
		.usage('[options] file')
		.option('-o, --out [output]', 'Output to file (default is print to console)')
		.option('-d, --data', 'add Data URI scheme prefix (data: and ;base64,)')
		.option('-m, --mime [MIME-type]', 'add MIME-type prefix if -d flag is on (image/png)')
		.option('-c, --charset [encoding]', 'add charset prefix if -d flag is on (UTF-8)')
		.parse(process.argv);

if (program.args.length === 0) {
	program.help();
	return;
}

var filepath = program.args[0];
var data = fs.readFileSync(filepath);
var base64 = data.toString('base64');

if (program.data) {
	var prefix = 'data:';

	if (program.mime) {
		prefix += program.mime;
	}
	if (program.charset) {
		prefix += ';' + program.charset;
	}
	prefix += ';base64,';
	base64 = prefix + base64;
}

if (program.out) {
	fs.writeFileSync(program.out, base64);
} else {
	console.log(base64);
}
