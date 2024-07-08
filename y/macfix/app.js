
var express = require('express');
var bodyParser = require("body-parser");
var https = require('https');
var pem = require('https-pem');
var fs = require('fs');
var cors = require('cors');
var iconv = require('iconv-lite');
var os = require('os');
var xml2js = require('xml2js');
var crypto = require('crypto');
var exec = require('child_process').exec;
var v8 = require('v8');
var execSync = require('child_process').execSync;
var path = require("path");

app = express();
app.use(cors());
app.use(bodyParser.json({limit: '50mb'}));
app.use(bodyParser.urlencoded({limit: '50mb', extended: true}));
app.disable('x-powered-by');

var port=7070;

var txt404 = '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><head><meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/><title>404 - File or directory not found.</title><style type="text/css"><!--body{margin:0;font-size:.7em;font-family:Verdana, Arial, Helvetica, sans-serif;background:#EEEEEE;}fieldset{padding:0 15px 10px 15px;} h1{font-size:2.4em;margin:0;color:#FFF;}h2{font-size:1.7em;margin:0;color:#CC0000;} h3{font-size:1.2em;margin:10px 0 0 0;color:#000000;} #header{width:96%;margin:0 0 0 0;padding:6px 2% 6px 2%;font-family:"trebuchet MS", Verdana, sans-serif;color:#FFF;background-color:#555555;}#content{margin:0 0 0 2%;position:relative;}.content-container{background:#FFF;width:96%;margin-top:8px;padding:10px;position:relative;}--></style></head><body><div id="header"><h1>Server Error</h1></div><div id="content"> <div class="content-container"><fieldset>  <h2>404 - File or directory not found.</h2>  <h3>The resource you are looking for might have been removed, had its name changed, or is temporarily unavailable.</h3> </fieldset></div></div></body></html>';
var txtTokenInvalido = '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><head><meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/><title>MacFix - Ordem de serviço</title><style type="text/css"><!--body{margin:0;font-size:.7em;font-family:Verdana, Arial, Helvetica, sans-serif;background:#EEEEEE;}fieldset{padding:0 15px 10px 15px;} h1{font-size:2.4em;margin:0;color:#FFF;}h2{font-size:1.7em;margin:0;color:#CC0000;} h3{font-size:1.2em;margin:10px 0 0 0;color:#000000;} #header{width:96%;margin:0 0 0 0;padding:6px 2% 6px 2%;font-family:"trebuchet MS", Verdana, sans-serif;color:#FFF;background-color:#555555;}#content{margin:0 0 0 2%;position:relative;}.content-container{background:#FFF;width:96%;margin-top:8px;padding:10px;position:relative;}--></style></head><body><div id="header"><h1>Server Error</h1></div><div id="content"> <div class="content-container"><fieldset>  <h2>Token inválido</h2>   </fieldset></div></div></body></html>';
var token_requerido=null;
var formulario=null;

function write(res, status, txt, type){
	res.writeHead(status, {'Content-Type': type
		,'X-XSS-Protection': '1; mode=block'
		,'X-Content-Type-Options': 'nosniff'
		,'Strict-Transport-Security': 'max-age=15552000; includeSubDomains'
		,'X-Frame-Options': 'deny'
	});
	res.end(txt);
}

function tokenErrado(token){
	if (token_requerido == null)
		return true;
	if (token_requerido != token)
		return true;
	return false;
}

var paramsValidos=[];
function paramsValidoFile(p){
	if ( p == null )
		return false;
	for ( var i=0;i<paramsValidos.length; i++ )
		if ( paramsValidos[i] == p )
			return true;
	return false;
}

function resolve_dir(a){
	if ( ! fs.existsSync(a) )
		fs.mkdirSync(a);	
}
//fs.existsSync

function resolve_dir_by_os(a){
	resolve_dir(dir+'/'+a[0]+a[1]);
	resolve_dir(dir+'/'+a[0]+a[1]+'/'+a[2]+a[3]);
	return dir+'/'+a[0]+a[1]+'/'+a[2]+a[3]+'/'+a;
}

function getMine(p){
	if ( p.endsWith('.html') || p.endsWith('.htm') )
		return 'text/html; charset=utf-8';
	if ( p.endsWith('.js') )
		return 'text/javascript';
	if ( p.endsWith('.css') )
		return 'text/css';
	if ( p.endsWith('.png') || p.endsWith('.ico') || p.endsWith('.jpg') )
		return 'image/png';
	return 'application/octet-stream';
}
var f_get = function(req,res){
	//console.log(req);
	if ( paramsValidoFile(req.params['0']) ){		
		write(res, 200, fs.readFileSync(req.params['0']), getMine(req.params['0']));
		return;
	}
	if ( tokenErrado(req.query.token) ){
		write(res, 401, txtTokenInvalido, 'text/html; charset=utf-8');
		return;			
	}
    if ( req._parsedUrl.pathname == '/' ){
	    write(res, 200, fs.readFileSync(formulario), 'text/html; charset=utf-8');		
		return;
	}
	write(res, 404, txt404, 'text/html; charset=utf-8');
	return;
};

var f_post = function(req,res){
	//console.log(req);
	if ( tokenErrado(req.query.token) ){
		write(res, 401, JSON.stringify({"msg": "token inválido"}), 'application/json');
		return;			
	}	
    if ( req._parsedUrl.pathname == '/cadastrar' ){		
		data=req.body; // ja esta json
		if ( data == null || data['1001']['id'] != 'os' || data['1001']['value'] == null || data['1001']['value'].trim() == '' ){
			write(res, 401, JSON.stringify({"msg": "Ordem de serviço não preenchido!"}), 'application/json');
			return;						
		}		
		data['1001']['value'] = data['1001']['value'].padStart(6, '0');
		os=data['1001']['value'];
		dir_os=resolve_dir_by_os(os);
		if ( fs.existsSync(dir_os) ){
			write(res, 401, JSON.stringify({"msg": "Ordem de serviço " + os  + " já existe!"}), 'application/json');
			return;						
		}
		fs.writeFileSync(dir_os, JSON.stringify(data, null, "\t"), "UTF-8");
	    write(res, 200, JSON.stringify({"msg": "cadastrado!"}), 'application/json');		
		return;
	}
    if ( req._parsedUrl.pathname == '/buscar' ){		
		data=req.body; // ja esta json
		if ( data == null || data['os'] == null || data['os'].trim() == '' ){
			write(res, 401, JSON.stringify({"msg": "Comando inválido, \"os\" não informada!"}), 'application/json');
			return;						
		}		
		os=data['os'].trim().padStart(6, '0');
		dir_os=resolve_dir_by_os(os);
		if ( !fs.existsSync(dir_os) ){
			write(res, 200, JSON.stringify({"msg": "Ordem de serviço " + os  + " não existe!"}), 'application/json');
			return;						
		}
	    write(res, 200, JSON.stringify({"msg": "encontrou", "data": JSON.parse(fs.readFileSync(dir_os), 'utf8') }), 'application/json');		
		return;
	}
    if ( req._parsedUrl.pathname == '/atualizar' ){		
		data=req.body; // ja esta json
		if ( data == null || data['1001']['id'] != 'os' || data['1001']['value'] == null || data['1001']['value'].trim() == '' ){
			write(res, 401, JSON.stringify({"msg": "Ordem de serviço não preenchido!"}), 'application/json');
			return;						
		}		
		data['1001']['value'] = data['1001']['value'].padStart(6, '0');
		os=data['1001']['value'];
		dir_os=resolve_dir_by_os(os);
		if ( !fs.existsSync(dir_os) ){
			write(res, 401, JSON.stringify({"msg": "Ordem de serviço " + os  + " não encontrada!"}), 'application/json');
			return;						
		}
		fs.writeFileSync(dir_os, JSON.stringify(data, null, "\t"), "UTF-8");
	    write(res, 200, JSON.stringify({"msg": "atualizado!"}), 'application/json');		
		return;
	}
    if ( req._parsedUrl.pathname == '/excluir' ){		
		data=req.body; // ja esta json
		if ( data == null || data['os'] == null || data['os'].trim() == '' ){
			write(res, 401, JSON.stringify({"msg": "Comando inválido, \"os\" não informada!"}), 'application/json');
			return;						
		}		
		if ( data['os'].trim().split(' ').length != 2 || data['os'].trim().split(' ')[1] != 'excluir' ){
			write(res, 401, JSON.stringify({"msg": "Por questão de segurança, preencha: \"" + data['os'].trim().split(' ')[0] + " excluir\" no campo buscar e click novamente em excluir"}), 'application/json');
			return;						
		}		
		os=data['os'].trim().split(' ')[0].padStart(6, '0');
		dir_os=resolve_dir_by_os(os);
		if ( !fs.existsSync(dir_os) ){
			write(res, 401, JSON.stringify({"msg": "Ordem de serviço " + os  + " não existe para excluir!"}), 'application/json');
			return;						
		}
		!fs.rmSync(dir_os);
		if ( fs.existsSync(dir_os) ){
			write(res, 401, JSON.stringify({"msg": "Não foi possivel excluir a os:" + os + " !"}), 'application/json');
			return;						
		}
	    write(res, 200, JSON.stringify({"msg": "os: " + os + " excluida com sucesso!"}), 'application/json');		
		return;
	}
	write(res, 404, JSON.stringify({"msg": "pagina não encontrada!"}), 'application/json');
	return;
}

config = JSON.parse(fs.readFileSync('config.js'), 'utf8');
token_requerido=config['token'];
paramsValidos=config['paramsValidos'];
formulario=config['formulario'];
dir=config['dir'].replaceAll('\\', '/');

console.log('Server ' + config['logo'] + ': https://[' + config['ip'] + ']:' + config['port'] + '/?token=' + config['token']);

app.get('/*', f_get);
app.post('/*', f_post);

var server = https.createServer(pem, app);

server.listen(config['port'], config['ip']).on('error', function(err){
	if ( String(err).indexOf('Error: listen EADDRINUSE: address already in use') >= 0 )
		console.log('\nErro! - Porta ' + config['port'] + ' ja em uso!');
	else	
		console.log(err);
});



