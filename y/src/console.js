// fetch('https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/console.js?t='+Date.now()).then(r=>r.text()).then(t=>(0,eval)(t))
// acima esta para https, caso precise de http use o comando abaixo no dominio 203
// fetch('http://203/console.js?t='+Date.now()).then(r=>r.text()).then(t=>(0,eval)(t))
// gmail exige essa permissao antes
// trustedTypes.createPolicy('default', { createScript: s => s });
(function(){
  var version='0.120';
  var build=new Date().toISOString().slice(0,16).replace('T',' ');

  try{
    if(window.trustedTypes&&window.trustedTypes.createPolicy&&!window.trustedTypes.defaultPolicy){
      window.trustedTypes.createPolicy('default',{createScript:function(s){return s;},createHTML:function(s){return s;},createScriptURL:function(s){return s;}});
    }
  }catch(_){}

  var w=window.open('about:blank','_blank','width=900,height=600');
  if(!w){alert('Popup bloqueado');return;}

  try{
    if(w.trustedTypes&&w.trustedTypes.createPolicy&&!w.trustedTypes.defaultPolicy){
      w.trustedTypes.createPolicy('default',{createScript:function(s){return s;},createHTML:function(s){return s;},createScriptURL:function(s){return s;}});
    }
  }catch(_){}

  var d=w.document;
  d.head.textContent='';d.body.textContent='';
  d.title='DevConsole v'+version;

  var css='body{margin:0;font:12px/1.4 Menlo,Monaco,Consolas,monospace;background:#1e1e1e;color:#e0e0e0;display:flex;flex-direction:column;height:100vh}'
   +'#__dc_tabs{display:flex;background:#252526;border-bottom:1px solid #444;align-items:stretch}'
   +'.tab{padding:8px 16px;cursor:pointer;color:#888;border-right:1px solid #333;user-select:none}'
   +'.tab.active{background:#1e1e1e;color:#fff}.tab:hover{color:#ccc}'
   +'.spacer{flex:1}'
   +'.tbtn{padding:8px 12px;cursor:pointer;color:#888;user-select:none}.tbtn:hover{color:#fff}'
   +'.ver{padding:8px 10px;color:#666;font-size:11px;cursor:default;user-select:none}'
   +'.pane{flex:1;overflow-y:auto;display:none}.pane.active{display:block}'
   +'#__dc_log{padding:6px 10px}'
   +'.entry{padding:3px 0;white-space:pre-wrap;word-break:break-word;border-bottom:1px solid #2a2a2a}'
   +'.inp{color:#9cdcfe}.inp::before{content:"\u276F  ";color:#666}'
   +'.out{color:#d4d4d4}.out::before{content:"\u27F5  ";color:#666}'
   +'.err{color:#f48771}.err::before{content:"\u2715  ";color:#f48771}'
   +'.warn{color:#dcdcaa}'
   +'.ok{color:#6a9955}.ok::before{content:"\u2713  ";color:#6a9955}'
   +'.nrow{display:grid;grid-template-columns:55px 1fr 60px 70px 75px 70px;gap:8px;padding:4px 8px;border-bottom:1px solid #2a2a2a;cursor:pointer;align-items:center}'
   +'.nrow:hover,.nrow.exp{background:#2a2a2a}'
   +'.nhead{color:#888;font-weight:bold;border-bottom:1px solid #555;background:#252526;cursor:default}'
   +'.nm{color:#9cdcfe;font-weight:bold}'
   +'.nu{overflow:hidden;text-overflow:ellipsis;white-space:nowrap}'
   +'.ns.s2{color:#6a9955}.ns.s3{color:#dcdcaa}.ns.s4,.ns.s5{color:#f48771}.ns.sp{color:#888}'
   +'.ndet{padding:12px;background:#252526;border-bottom:1px solid #2a2a2a;font-size:11px}'
   +'.ndet-sec{margin-bottom:12px}.ndet-sec:last-child{margin-bottom:0}'
   +'.ndet-sh{color:#888;font-weight:bold;text-transform:uppercase;font-size:10px;letter-spacing:.05em;margin-bottom:6px;border-bottom:1px solid #333;padding-bottom:3px}'
   +'.ndet-kv{display:grid;grid-template-columns:200px 1fr;gap:4px 12px}'
   +'.ndet-k{color:#9cdcfe;font-weight:bold;word-break:break-all}'
   +'.ndet-v{color:#d4d4d4;word-break:break-all;white-space:pre-wrap}'
   +'.ndet-code{background:#1a1a1a;color:#d4d4d4;padding:8px 10px;border-radius:3px;max-height:300px;overflow:auto;white-space:pre-wrap;word-break:break-all;margin:0;font:11px/1.4 Menlo,Monaco,Consolas,monospace}'
   +'.ndet-act{display:flex;gap:6px;flex-wrap:wrap;margin-top:8px;padding-top:8px;border-top:1px solid #333}'
   +'.ndet-btn{background:#3c3c3c;color:#d4d4d4;border:1px solid #555;padding:5px 12px;cursor:pointer;font:11px Menlo,monospace;border-radius:2px}'
   +'.ndet-btn:hover{background:#555;color:#fff}'
   +'.ndet-btn.replay{background:#0e639c;border-color:#1177bb;color:#fff}.ndet-btn.replay:hover{background:#1177bb}'
   +'.ndet-err{color:#f48771;padding:4px 0}'
   +'.nrow.evt{grid-template-columns:40px 1fr 80px;cursor:default;background:#202020;font-style:italic;border-top:1px solid #333;border-bottom:1px solid #333}'
   +'.nrow.evt:hover{background:#202020}'
   +'.nrow.evt.evt-warn{color:#dcdcaa}.nrow.evt.evt-err{color:#f48771}.nrow.evt.evt-ok{color:#6a9955}'
   +'.evt-ic{text-align:center;font-weight:bold;font-style:normal}'
   +'.evt-tm{color:#666;font-size:11px;text-align:right;font-style:normal}'
   +'#__dc_bar{display:flex;border-top:1px solid #444;background:#252526}'
   +'#__dc_cmd{flex:1;background:transparent;color:#e0e0e0;border:0;padding:8px 10px;font:inherit;resize:none;outline:none;min-height:38px}'
   +'#__dc_status{flex:1;padding:10px;color:#f48771;text-align:center;font-style:italic}'
   +'button{background:#0e639c;color:#fff;border:0;padding:0 14px;cursor:pointer;font:inherit}button:hover{background:#1177bb}';
  var st=d.createElement('style');st.textContent=css;d.head.appendChild(st);

  function el(t,p){var e=d.createElement(t);if(p)Object.assign(e,p);return e;}

  var tabsBar=el('div',{id:'__dc_tabs'});
  tabsBar.append(
    el('div',{id:'__dc_tC',className:'tab active',textContent:'Console'}),
    el('div',{id:'__dc_tN',className:'tab',textContent:'Network'}),
    el('div',{className:'spacer'}),
    el('div',{className:'ver',textContent:'v'+version+'  ['+build+']'}),
    el('div',{id:'__dc_clear',className:'tbtn',textContent:'\u232B Clear'})
  );
  var paneC=el('div',{id:'__dc_paneC',className:'pane active'});
  paneC.appendChild(el('div',{id:'__dc_log'}));
  var paneN=el('div',{id:'__dc_paneN',className:'pane'});
  var nHead=el('div',{className:'nrow nhead'});
  ['Method','URL','Status','Type','Size','Time'].forEach(function(t){nHead.appendChild(el('div',{textContent:t}));});
  paneN.append(nHead,el('div',{id:'__dc_netList'}));
  var bar=el('div',{id:'__dc_bar'});
  bar.append(
    el('textarea',{id:'__dc_cmd',rows:2,placeholder:'Enter executa  \u2022  Shift+Enter nova linha  \u2022  Ctrl+\u2191/\u2193 hist\u00F3rico'}),
    el('button',{id:'__dc_run',textContent:'Run'})
  );
  d.body.append(tabsBar,paneC,paneN,bar);

  function popupSetup(popup){
    var window=popup;
    var document=popup.document;
    var d=document,dc=window.__dc;
    var MAX_BODY=100000;

    function fmt(v){
      if(v===undefined)return'undefined';
      if(v===null)return'null';
      if(typeof v==='function')return v.toString();
      if(typeof v==='object'){
        try{return JSON.stringify(v,function(k,val){if(typeof val==='function')return'[Function]';if(val&&val.nodeType)return'[DOM '+val.nodeName+']';return val;},2);}catch(e){return String(v);}
      }
      return String(v);
    }
    function fSize(b){if(b==null||isNaN(b)||b===0)return'\u2014';if(b<1024)return b+' B';if(b<1048576)return(b/1024).toFixed(1)+' K';return(b/1048576).toFixed(2)+' M';}
    function fTime(ms){if(ms==null||isNaN(ms))return'\u2014';if(ms<1000)return Math.round(ms)+' ms';return(ms/1000).toFixed(2)+' s';}
    function sCls(s){if(s==='pending')return'sp';var n=parseInt(s,10);if(n>=200&&n<300)return's2';if(n>=300&&n<400)return's3';if(n>=400&&n<500)return's4';if(n>=500)return's5';return'';}
    function shortUrl(u){try{var x=new URL(u);return x.pathname+(x.search||'')+'  ('+x.hostname+')';}catch(_){return u;}}
    function formatBody(b){if(!b)return'';var t=b.trim?b.trim():'';if(t.charAt(0)==='{'||t.charAt(0)==='['){try{return JSON.stringify(JSON.parse(b),null,2);}catch(_){}}return b;}
    function parseQS(url){var out={};try{var u=new URL(url,window.opener?window.opener.location.href:'http://x/');u.searchParams.forEach(function(v,k){out[k]=v;});}catch(_){}return out;}
    function normHeaders(h){
      if(!h)return{};var out={};
      try{
        if(typeof h.forEach==='function'&&typeof h.get==='function'){h.forEach(function(v,k){out[k]=v;});}
        else if(Array.isArray(h)){h.forEach(function(p){out[p[0]]=p[1];});}
        else if(typeof h==='object'){Object.keys(h).forEach(function(k){out[k]=h[k];});}
      }catch(_){}
      return out;
    }
    function bodyToString(b){
      if(b==null)return undefined;
      if(typeof b==='string')return b.slice(0,MAX_BODY);
      var name=b.constructor&&b.constructor.name;
      try{
        if(name==='URLSearchParams')return b.toString().slice(0,MAX_BODY);
        if(name==='FormData'){
          var parts=[];b.forEach(function(v,k){parts.push(encodeURIComponent(k)+'='+(typeof v==='string'?encodeURIComponent(v):'['+(v.constructor&&v.constructor.name||'binary')+']'));});
          return '[FormData] '+parts.join('&');
        }
        if(name==='Blob')return '[Blob '+b.size+' bytes type='+b.type+']';
        if(name==='ArrayBuffer')return '[ArrayBuffer '+b.byteLength+' bytes]';
      }catch(_){}
      return '['+(name||'unknown')+']';
    }
    dc.fmt=fmt;

    dc.addLog=function(text,cls){
      var x=d.createElement('div');x.className='entry '+cls;x.textContent=text;
      var log=d.getElementById('__dc_log');log.appendChild(x);
      if(d.getElementById('__dc_paneC').classList.contains('active'))log.scrollTop=log.scrollHeight;
    };
    dc.addNet=function(data){
      var id=String(++dc.counter);
      dc.entries[id]={method:data.method,url:data.url,status:data.status,type:data.type,size:data.size,duration:data.duration,time:data.time,reqHeaders:data.reqHeaders,reqBody:data.reqBody,resHeaders:data.resHeaders,resBody:data.resBody,error:data.error};
      var row=d.createElement('div');row.className='nrow';row.dataset.entryId=id;
      var c=function(cls,txt,t){var x=d.createElement('div');if(cls)x.className=cls;x.textContent=txt;if(t)x.title=t;return x;};
      row.append(c('nm',data.method||'\u2014'),c('nu',shortUrl(data.url),data.url),c('ns '+sCls(data.status),data.status||'\u2014'),c('',data.type||'\u2014'),c('',fSize(data.size)),c('',fTime(data.duration)));
      d.getElementById('__dc_netList').appendChild(row);
      var pN=d.getElementById('__dc_paneN');if(pN.classList.contains('active'))pN.scrollTop=pN.scrollHeight;
      return id;
    };
    dc.updateNet=function(id,partial){
      var e=dc.entries[id];if(!e)return;
      Object.assign(e,partial);
      var row=d.querySelector('[data-entry-id="'+id+'"]');if(!row)return;
      var c=row.children;
      if('method' in partial)c[0].textContent=partial.method||'\u2014';
      if('status' in partial){c[2].textContent=partial.status==null?'\u2014':partial.status;c[2].className='ns '+sCls(partial.status);}
      if('size' in partial)c[4].textContent=fSize(partial.size);
      if('duration' in partial)c[5].textContent=fTime(partial.duration);
    };
    dc.addEvent=function(text,type){
      var row=d.createElement('div');row.className='nrow evt evt-'+(type||'warn');
      var ic=type==='err'?'\u2715':(type==='ok'?'\u2713':'\u26A0');
      var c=function(txt,cls){var x=d.createElement('div');if(cls)x.className=cls;x.textContent=txt;return x;};
      row.append(c(ic,'evt-ic'),c(text),c(new Date().toLocaleTimeString(),'evt-tm'));
      d.getElementById('__dc_netList').appendChild(row);
      var pN=d.getElementById('__dc_paneN');if(pN.classList.contains('active'))pN.scrollTop=pN.scrollHeight;
    };

    function mkSec(title){var s=d.createElement('div');s.className='ndet-sec';var h=d.createElement('div');h.className='ndet-sh';h.textContent=title;s.appendChild(h);return s;}
    function mkKV(obj){var kv=d.createElement('div');kv.className='ndet-kv';Object.keys(obj).forEach(function(k){var kd=d.createElement('div');kd.className='ndet-k';kd.textContent=k;var vd=d.createElement('div');vd.className='ndet-v';vd.textContent=obj[k];kv.append(kd,vd);});return kv;}
    function mkCode(text){var pre=d.createElement('pre');pre.className='ndet-code';pre.textContent=text;return pre;}

    dc.buildDetail=function(e,entryId){
      var box=d.createElement('div');box.className='ndet';
      var gen=mkSec('General');
      gen.appendChild(mkKV({'URL':e.url,'Method':e.method||'\u2014','Status':e.status==null?'\u2014':String(e.status),'Type':e.type||'\u2014','Size':fSize(e.size),'Duration':fTime(e.duration),'Time':e.time?new Date(e.time).toLocaleTimeString():'\u2014'}));
      box.appendChild(gen);
      var qs=parseQS(e.url);
      if(qs&&Object.keys(qs).length){var qsSec=mkSec('Query String');qsSec.appendChild(mkKV(qs));box.appendChild(qsSec);}
      if(e.reqHeaders&&Object.keys(e.reqHeaders).length){var rh=mkSec('Request Headers');rh.appendChild(mkKV(e.reqHeaders));box.appendChild(rh);}
      if(e.reqBody){var rp=mkSec('Request Payload');rp.appendChild(mkCode(formatBody(e.reqBody)));box.appendChild(rp);}
      if(e.resHeaders&&Object.keys(e.resHeaders).length){var sh=mkSec('Response Headers');sh.appendChild(mkKV(e.resHeaders));box.appendChild(sh);}
      if(e.resBody){var rs=mkSec('Response Body');rs.appendChild(mkCode(formatBody(e.resBody)));box.appendChild(rs);}
      if(e.error){var er=mkSec('Error');var p=d.createElement('div');p.className='ndet-err';p.textContent=e.error;er.appendChild(p);box.appendChild(er);}
      var act=d.createElement('div');act.className='ndet-act';
      function btn(label,cls,fn){var b=d.createElement('button');b.className='ndet-btn'+(cls?' '+cls:'');b.textContent=label;b.addEventListener('click',fn);return b;}
      act.append(
        btn('\u25B6 Replay','replay',function(){dc.replay(entryId);}),
        btn('Copy fetch','',function(){dc.copy(dc.toFetch(entryId),'fetch');}),
        btn('Copy curl (bash)','',function(){dc.copy(dc.toCurlBash(entryId),'curl bash');}),
        btn('Copy curl (cmd)','',function(){dc.copy(dc.toCurlCmd(entryId),'curl cmd');}),
        btn('Copy URL','',function(){dc.copy(e.url,'URL');})
      );
      box.appendChild(act);
      return box;
    };

    dc.replay=function(id){
      var e=dc.entries[id];if(!e)return;
      var t=window.opener;
      if(!t||t.closed){dc.addLog('Replay falhou: sem target ativo.','err');return;}
      try{
        var opts={method:e.method||'GET',headers:e.reqHeaders||{},credentials:'include'};
        if(e.reqBody&&opts.method!=='GET'&&opts.method!=='HEAD')opts.body=e.reqBody;
        dc.addEvent('Replay: '+e.method+' '+e.url,'warn');
        t.fetch(e.url,opts).catch(function(err){dc.addLog('Replay falhou: '+err.message,'err');});
      }catch(err){dc.addLog('Replay erro: '+err.message,'err');}
    };
    dc.toFetch=function(id){
      var e=dc.entries[id];if(!e)return'';
      var opts={method:e.method||'GET'};
      if(e.reqHeaders&&Object.keys(e.reqHeaders).length)opts.headers=e.reqHeaders;
      if(e.reqBody)opts.body=e.reqBody;
      opts.credentials='include';
      return 'fetch('+JSON.stringify(e.url)+', '+JSON.stringify(opts,null,2)+')';
    };
    dc.toCurlBash=function(id){
      var e=dc.entries[id];if(!e)return'';
      function esc(s){return "'"+String(s).replace(/'/g,"'\\''")+"'";}
      var parts=['curl '+esc(e.url)];
      if(e.method&&e.method!=='GET')parts.push('-X '+esc(e.method));
      if(e.reqHeaders)Object.keys(e.reqHeaders).forEach(function(k){parts.push('-H '+esc(k+': '+e.reqHeaders[k]));});
      if(e.reqBody)parts.push('--data-raw '+esc(e.reqBody));
      parts.push('--compressed');
      return parts.join(' \\\n  ');
    };
    dc.toCurlCmd=function(id){
      var e=dc.entries[id];if(!e)return'';
      function esc(s){return '"'+String(s).replace(/\\/g,'\\\\').replace(/"/g,'\\"').replace(/%/g,'%%').replace(/\^/g,'^^')+'"';}
      var parts=['curl '+esc(e.url)];
      if(e.method&&e.method!=='GET')parts.push('-X '+esc(e.method));
      if(e.reqHeaders)Object.keys(e.reqHeaders).forEach(function(k){parts.push('-H '+esc(k+': '+e.reqHeaders[k]));});
      if(e.reqBody)parts.push('--data-raw '+esc(e.reqBody));
      parts.push('--compressed');
      return parts.join(' ^\n  ');
    };
    dc.copy=function(text,label){
      var ok=function(){dc.addEvent('Copiado: '+(label||'')+' ('+text.length+' chars)','ok');};
      var fail=function(m){dc.addLog('Falha ao copiar: '+m,'err');};
      try{
        if(navigator.clipboard&&navigator.clipboard.writeText){
          navigator.clipboard.writeText(text).then(ok,function(err){
            try{var ta=d.createElement('textarea');ta.value=text;ta.style.position='fixed';ta.style.opacity='0';d.body.appendChild(ta);ta.select();var ok2=d.execCommand('copy');ta.remove();if(ok2)ok();else fail(err.message);}catch(e2){fail(e2.message);}
          });
        }else{
          var ta=d.createElement('textarea');ta.value=text;ta.style.position='fixed';ta.style.opacity='0';d.body.appendChild(ta);ta.select();var ok2=d.execCommand('copy');ta.remove();if(ok2)ok();else fail('execCommand falhou');
        }
      }catch(err){fail(err.message);}
    };

    dc.activate=function(which){
      d.getElementById('__dc_tC').classList.toggle('active',which==='c');
      d.getElementById('__dc_tN').classList.toggle('active',which==='n');
      d.getElementById('__dc_paneC').classList.toggle('active',which==='c');
      d.getElementById('__dc_paneN').classList.toggle('active',which==='n');
      d.getElementById('__dc_bar').style.display=which==='c'?'flex':'none';
      if(which==='c')d.getElementById('__dc_cmd').focus();
    };
    dc.disableInput=function(msg){
      var b=d.getElementById('__dc_bar');if(!b)return;
      d.getElementById('__dc_cmd').style.display='none';
      d.getElementById('__dc_run').style.display='none';
      var s=d.getElementById('__dc_status');
      if(!s){s=d.createElement('div');s.id='__dc_status';b.appendChild(s);}
      s.textContent=msg||'Comunica\u00E7\u00E3o perdida';s.style.display='block';
    };
    dc.enableInput=function(){
      var s=d.getElementById('__dc_status');if(s)s.style.display='none';
      d.getElementById('__dc_cmd').style.display='';
      d.getElementById('__dc_run').style.display='';
    };

    dc.hist=[];dc.histIdx=-1;
    dc.runCommand=function(){
      var ta=d.getElementById('__dc_cmd');var code=ta.value;
      if(!code.trim())return;
      dc.addLog(code,'inp');dc.hist.push(code);dc.histIdx=dc.hist.length;
      try{
        var t=window.opener;
        if(!t||t.closed){dc.addLog('Sem target ativo.','err');}
        else{
          var r=new t.Function('return ('+code+');')();
          dc.addLog(fmt(r),'out');
        }
      }catch(e){
        try{var t2=window.opener;new t2.Function(code)();dc.addLog('undefined','out');}
        catch(e2){dc.addLog(e2.message,'err');}
      }
      ta.value='';ta.focus();
    };

    // ====== NEW: instala wraps direto via property assignment (sem eval/Function) ======
    dc.installOnTarget=function(target){
      if(target.__dcInstalled)return true;
      target.__dcInstalled=true;

      // fetch
      try{
        var oF=target.fetch.bind(target);
        target.fetch=function(input,init){
          var url=typeof input==='string'?input:(input&&input.url)||'';
          var method=(init&&init.method)||(typeof input==='object'&&input&&input.method)||'GET';
          var hdrs=normHeaders((init&&init.headers)||(typeof input==='object'&&input&&input.headers));
          var data={method:method.toUpperCase(),url:url,type:'fetch',status:'pending',time:Date.now(),reqHeaders:hdrs};
          try{if(init&&init.body)data.reqBody=bodyToString(init.body);}catch(_){}
          var id=dc.addNet(data);
          var t0=target.performance.now();
          return oF(input,init).then(function(res){
            var rh={};try{res.headers.forEach(function(v,k){rh[k]=v;});}catch(_){}
            dc.updateNet(id,{status:res.status,duration:target.performance.now()-t0,resHeaders:rh});
            try{res.clone().text().then(function(txt){dc.updateNet(id,{resBody:txt.slice(0,MAX_BODY),size:txt.length});}).catch(function(){});}catch(_){}
            return res;
          }).catch(function(err){
            dc.updateNet(id,{status:'ERR',error:err.message,duration:target.performance.now()-t0});throw err;
          });
        };
      }catch(e){dc.addLog('Falha ao wrappar fetch: '+e.message,'err');}

      // XHR
      try{
        var OO=target.XMLHttpRequest.prototype.open,OS=target.XMLHttpRequest.prototype.send,ORS=target.XMLHttpRequest.prototype.setRequestHeader;
        target.XMLHttpRequest.prototype.open=function(m,u){this.__dcInfo={method:(m||'').toUpperCase(),url:u,type:'xhr',reqHeaders:{}};return OO.apply(this,arguments);};
        target.XMLHttpRequest.prototype.setRequestHeader=function(n,v){if(this.__dcInfo)this.__dcInfo.reqHeaders[n]=v;return ORS.apply(this,arguments);};
        target.XMLHttpRequest.prototype.send=function(body){
          var info=this.__dcInfo;
          if(info){
            info.status='pending';info.time=Date.now();
            try{if(body)info.reqBody=bodyToString(body);}catch(_){}
            var id=dc.addNet(info),t0=target.performance.now(),self=this;
            this.addEventListener('loadend',function(){
              var upd={status:self.status||'ERR',duration:target.performance.now()-t0};
              try{
                var rh={};var raw=self.getAllResponseHeaders()||'';
                raw.split(/\r?\n/).forEach(function(ln){var i=ln.indexOf(':');if(i>0)rh[ln.slice(0,i).trim()]=ln.slice(i+1).trim();});
                upd.resHeaders=rh;
              }catch(_){}
              try{
                if(self.responseType===''||self.responseType==='text'){var txt=self.responseText||'';upd.size=txt.length;upd.resBody=txt.slice(0,MAX_BODY);}
                else if(self.response){upd.size=self.response.byteLength||self.response.size||0;upd.resBody='['+self.responseType+']';}
              }catch(_){}
              dc.updateNet(id,upd);
            });
          }
          return OS.apply(this,arguments);
        };
      }catch(e){dc.addLog('Falha ao wrappar XHR: '+e.message,'err');}

      // console
      ['log','info','warn','error'].forEach(function(m){
        try{
          var o=target.console[m];
          target.console[m]=function(){
            var cls=m==='error'?'err':(m==='warn'?'warn':'out');
            try{dc.addLog(Array.prototype.map.call(arguments,function(a){return fmt(a);}).join(' '),cls);}catch(_){}
            o.apply(target.console,arguments);
          };
        }catch(_){}
      });

      // PerformanceObserver
      try{
        var seen={};
        var po=new target.PerformanceObserver(function(list){
          list.getEntries().forEach(function(pe){
            if(pe.initiatorType==='fetch'||pe.initiatorType==='xmlhttprequest')return;
            var k=pe.name+'|'+pe.startTime;if(seen[k])return;seen[k]=1;
            dc.addNet({method:'GET',url:pe.name,type:pe.initiatorType||'other',status:pe.responseStatus||200,duration:pe.duration,size:pe.transferSize||pe.encodedBodySize||0,time:Date.now()});
          });
        });
        po.observe({type:'resource',buffered:true});
      }catch(e){dc.addLog('Falha ao observar performance: '+e.message,'err');}

      // beforeunload
      try{
        target.addEventListener('beforeunload',function(){
          var url='';try{url=target.location.href;}catch(_){}
          try{dc.onTargetUnload(url);}catch(_){}
        });
      }catch(_){}

      var here='';try{here=target.location.href;}catch(_){}
      try{dc.onTargetReady(here);}catch(_){}
      return true;
    };

    dc.onTargetReady=function(url){
      dc.addLog('Conectado em: '+url,'ok');
      dc.addEvent('Conectado em '+url,'ok');
      dc.enableInput();
      if(dc.pollIv){window.clearInterval(dc.pollIv);dc.pollIv=null;}
    };
    dc.onTargetUnload=function(url){
      dc.addLog('\u26A0 Postback detectado em '+url+'. Aguardando nova p\u00E1gina...','warn');
      dc.addEvent('Postback em '+url,'warn');
      dc.startPolling();
    };
    dc.commLost=function(reason){
      if(dc.pollIv){window.clearInterval(dc.pollIv);dc.pollIv=null;}
      dc.addLog('\u2715 Comunica\u00E7\u00E3o perdida: '+reason,'err');
      dc.addEvent('Comunica\u00E7\u00E3o perdida: '+reason,'err');
      dc.disableInput('\u2715 '+reason);
    };
    dc.pollIv=null;
    dc.startPolling=function(){
      if(dc.pollIv)return;
      dc.pollIv=window.setInterval(function(){
        if(window.closed){window.clearInterval(dc.pollIv);dc.pollIv=null;return;}
        var nw;try{nw=window.opener;}catch(_){nw=null;}
        if(!nw||nw.closed){dc.commLost('janela original fechada. Reclique o bookmarklet em outra aba.');return;}
        var doc;try{doc=nw.document;}catch(_){dc.commLost('nova p\u00E1gina cross-origin. Reclique o bookmarklet l\u00E1.');return;}
        if(doc.readyState!=='complete')return;
        var url;try{url=nw.location.href;}catch(_){dc.commLost('nova p\u00E1gina cross-origin. Reclique o bookmarklet l\u00E1.');return;}
        if(nw.__dcInstalled)return;
        window.clearInterval(dc.pollIv);dc.pollIv=null;
        dc.bootstrap(nw);
      },200);
    };

    dc.bootstrap=function(target){
      try{
        dc.installOnTarget(target);
      }catch(e){
        dc.addLog('\u2715 Falha geral em installOnTarget: '+e.message,'err');
        dc.addEvent('Falha: '+e.message,'err');
        dc.disableInput('Inje\u00e7\u00e3o falhou');
      }
    };

    d.getElementById('__dc_tC').addEventListener('click',function(){dc.activate('c');});
    d.getElementById('__dc_tN').addEventListener('click',function(){dc.activate('n');});
    d.getElementById('__dc_clear').addEventListener('click',function(){
      if(d.getElementById('__dc_paneC').classList.contains('active'))d.getElementById('__dc_log').textContent='';
      else d.getElementById('__dc_netList').textContent='';
    });
    d.getElementById('__dc_netList').addEventListener('click',function(ev){
      if(ev.target.closest('.ndet'))return;
      var row=ev.target.closest&&ev.target.closest('.nrow');
      if(!row||!row.dataset.entryId)return;
      var next=row.nextElementSibling;
      if(next&&next.classList.contains('ndet')){next.remove();row.classList.remove('exp');return;}
      var e=dc.entries[row.dataset.entryId];if(!e)return;
      row.classList.add('exp');
      var det=dc.buildDetail(e,row.dataset.entryId);
      row.parentNode.insertBefore(det,row.nextSibling);
    });
    d.getElementById('__dc_run').addEventListener('click',function(){dc.runCommand();});
    d.getElementById('__dc_cmd').addEventListener('keydown',function(e){
      if(e.key==='Enter'&&!e.shiftKey){e.preventDefault();dc.runCommand();}
      else if(e.key==='ArrowUp'&&(e.target.value===''||e.ctrlKey)){
        if(dc.histIdx>0){dc.histIdx--;e.target.value=dc.hist[dc.histIdx];}e.preventDefault();
      }else if(e.key==='ArrowDown'&&e.ctrlKey){
        if(dc.histIdx<dc.hist.length-1){dc.histIdx++;e.target.value=dc.hist[dc.histIdx];}
        else{dc.histIdx=dc.hist.length;e.target.value='';}
        e.preventDefault();
      }
    });
    d.getElementById('__dc_cmd').focus();
  }

  w.__dc={entries:{},counter:0};

  var setupMode='eval';
  try{
    w.eval('('+popupSetup.toString()+')(window)');
  }catch(e){
    setupMode='direct';
    try{
      popupSetup(w);
    }catch(e2){
      alert('Falha total ao instalar popup-side: '+e2.message+' (eval: '+e.message+')');
      return;
    }
  }
  w.__dc.addLog('DevConsole v'+version+' carregado em '+build+' (modo: '+setupMode+').','warn');
  w.__dc.addEvent('DevConsole v'+version+' iniciado [modo '+setupMode+']','warn');
  if(setupMode==='direct'){
    w.__dc.addLog('\u26A0 Modo degradado: handlers no realm do opener. Postback vai quebrar comunica\u00e7\u00e3o.','warn');
    w.__dc.addEvent('\u26A0 Modo degradado: postback quebra console','warn');
  }
  w.__dc.bootstrap(window);
})();