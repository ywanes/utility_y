// fetch('https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/console.js?t='+Date.now()).then(r=>r.text()).then(t=>(0,eval)(t))
(function(){
  var version='0.114';
  var build=new Date().toISOString().slice(0,16).replace('T',' ');

  var w=window.open('about:blank','_blank','width=900,height=600');
  if(!w){alert('Popup bloqueado');return;}
  var d=w.document;
  d.head.innerHTML='';d.body.innerHTML='';
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
   +'.ndet{padding:8px 12px 12px 12px;background:#252526;border-bottom:1px solid #2a2a2a;font-size:11px;white-space:pre-wrap;word-break:break-all}'
   +'#__dc_bar{display:flex;border-top:1px solid #444;background:#252526}'
   +'#__dc_cmd{flex:1;background:transparent;color:#e0e0e0;border:0;padding:8px 10px;font:inherit;resize:none;outline:none;min-height:38px}'
   +'#__dc_status{flex:1;padding:10px;color:#f48771;text-align:center;font-style:italic}'
   +'button{background:#0e639c;color:#fff;border:0;padding:0 14px;cursor:pointer;font:inherit}button:hover{background:#1177bb}';
  var st=d.createElement('style');st.textContent=css;d.head.appendChild(st);

  function el(t,p){var e=d.createElement(t);if(p)Object.assign(e,p);return e;}

  // DOM com IDs estáveis
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

  // ===========================================================
  // targetInstaller: roda DENTRO de cada opener target via eval.
  // Self-contained: só conhece `window.__devconsole` que aponta pra popup.
  // ===========================================================
  function targetInstaller(){
    if(window.__dcInstalled)return;
    window.__dcInstalled=true;
    var popup=window.__devconsole;
    if(!popup||popup.closed)return;
    var dc=popup.__dc;

    try{
      var oF=window.fetch.bind(window);
      window.fetch=function(input,init){
        var url=typeof input==='string'?input:(input&&input.url)||'';
        var method=(init&&init.method)||(typeof input==='object'&&input&&input.method)||'GET';
        var data={method:method.toUpperCase(),url:url,type:'fetch',status:'pending',time:Date.now()};
        try{if(init&&init.body)data.reqBody=typeof init.body==='string'?init.body.slice(0,1000):'[non-string body]';}catch(_){}
        var id=dc.addNet(data);
        var t0=performance.now();
        return oF(input,init).then(function(res){
          dc.updateNet(id,{status:res.status,duration:performance.now()-t0});
          try{res.clone().text().then(function(txt){dc.updateNet(id,{resPreview:txt.slice(0,1000),size:txt.length});}).catch(function(){});}catch(_){}
          return res;
        }).catch(function(err){
          dc.updateNet(id,{status:'ERR',error:err.message,duration:performance.now()-t0});throw err;
        });
      };
    }catch(_){}

    try{
      var OO=XMLHttpRequest.prototype.open,OS=XMLHttpRequest.prototype.send;
      XMLHttpRequest.prototype.open=function(m,u){this.__dcInfo={method:(m||'').toUpperCase(),url:u,type:'xhr'};return OO.apply(this,arguments);};
      XMLHttpRequest.prototype.send=function(body){
        var info=this.__dcInfo;
        if(info){
          info.status='pending';info.time=Date.now();
          try{if(body)info.reqBody=typeof body==='string'?body.slice(0,1000):'[non-string body]';}catch(_){}
          var id=dc.addNet(info),t0=performance.now(),self=this;
          this.addEventListener('loadend',function(){
            var upd={status:self.status||'ERR',duration:performance.now()-t0};
            try{
              if(self.responseType===''||self.responseType==='text'){var txt=self.responseText||'';upd.size=txt.length;upd.resPreview=txt.slice(0,1000);}
              else if(self.response){upd.size=self.response.byteLength||self.response.size||0;}
            }catch(_){}
            dc.updateNet(id,upd);
          });
        }
        return OS.apply(this,arguments);
      };
    }catch(_){}

    ['log','info','warn','error'].forEach(function(m){
      try{
        var o=console[m];
        console[m]=function(){
          var cls=m==='error'?'err':(m==='warn'?'warn':'out');
          try{dc.addLog(Array.prototype.map.call(arguments,function(a){return dc.fmt(a);}).join(' '),cls);}catch(_){}
          o.apply(console,arguments);
        };
      }catch(_){}
    });

    try{
      var seen={};
      var po=new PerformanceObserver(function(list){
        list.getEntries().forEach(function(pe){
          if(pe.initiatorType==='fetch'||pe.initiatorType==='xmlhttprequest')return;
          var k=pe.name+'|'+pe.startTime;if(seen[k])return;seen[k]=1;
          dc.addNet({method:'GET',url:pe.name,type:pe.initiatorType||'other',status:pe.responseStatus||200,duration:pe.duration,size:pe.transferSize||pe.encodedBodySize||0,time:Date.now()});
        });
      });
      po.observe({type:'resource',buffered:true});
    }catch(_){}

    window.addEventListener('beforeunload',function(){
      var url='';try{url=location.href;}catch(_){}
      try{dc.onTargetUnload(url);}catch(_){}
    });

    var here='';try{here=location.href;}catch(_){}
    try{dc.onTargetReady(here);}catch(_){}
  }

  // ===========================================================
  // popupSetup: roda DENTRO da popup via w.eval. Define todos os
  // métodos de bridge, event handlers, e a lógica de polling.
  // ===========================================================
  function popupSetup(){
    var d=document,dc=window.__dc;

    function fmt(v){
      if(v===undefined)return'undefined';
      if(v===null)return'null';
      if(typeof v==='function')return v.toString();
      if(typeof v==='object'){
        try{return JSON.stringify(v,function(k,val){
          if(typeof val==='function')return'[Function]';
          if(val&&val.nodeType)return'[DOM '+val.nodeName+']';
          return val;
        },2);}catch(e){return String(v);}
      }
      return String(v);
    }
    function fSize(b){if(b==null||isNaN(b)||b===0)return'\u2014';if(b<1024)return b+' B';if(b<1048576)return(b/1024).toFixed(1)+' K';return(b/1048576).toFixed(2)+' M';}
    function fTime(ms){if(ms==null||isNaN(ms))return'\u2014';if(ms<1000)return Math.round(ms)+' ms';return(ms/1000).toFixed(2)+' s';}
    function sCls(s){if(s==='pending')return'sp';var n=parseInt(s,10);if(n>=200&&n<300)return's2';if(n>=300&&n<400)return's3';if(n>=400&&n<500)return's4';if(n>=500)return's5';return'';}
    function shortUrl(u){try{var x=new URL(u);return x.pathname+(x.search||'')+'  ('+x.hostname+')';}catch(_){return u;}}
    dc.fmt=fmt;

    dc.addLog=function(text,cls){
      var x=d.createElement('div');x.className='entry '+cls;x.textContent=text;
      var log=d.getElementById('__dc_log');log.appendChild(x);
      if(d.getElementById('__dc_paneC').classList.contains('active'))log.scrollTop=log.scrollHeight;
    };
    dc.addNet=function(data){
      var id=String(++dc.counter);
      dc.entries[id]={method:data.method,url:data.url,status:data.status,type:data.type,size:data.size,duration:data.duration,time:data.time,reqBody:data.reqBody,resPreview:data.resPreview,error:data.error};
      var row=d.createElement('div');row.className='nrow';row.dataset.entryId=id;
      var c=function(cls,txt,t){var x=d.createElement('div');if(cls)x.className=cls;x.textContent=txt;if(t)x.title=t;return x;};
      row.append(
        c('nm',data.method||'\u2014'),
        c('nu',shortUrl(data.url),data.url),
        c('ns '+sCls(data.status),data.status||'\u2014'),
        c('',data.type||'\u2014'),
        c('',fSize(data.size)),
        c('',fTime(data.duration))
      );
      d.getElementById('__dc_netList').appendChild(row);
      var pN=d.getElementById('__dc_paneN');
      if(pN.classList.contains('active'))pN.scrollTop=pN.scrollHeight;
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
        else{dc.addLog(fmt(t.eval(code)),'out');}
      }catch(e){dc.addLog(e.message,'err');}
      ta.value='';ta.focus();
    };

    dc.onTargetReady=function(url){
      dc.addLog('Conectado em: '+url,'ok');
      dc.enableInput();
      if(dc.pollIv){window.clearInterval(dc.pollIv);dc.pollIv=null;}
    };
    dc.onTargetUnload=function(url){
      dc.addLog('\u26A0 Postback detectado em '+url+'. Aguardando nova p\u00E1gina...','warn');
      dc.startPolling();
    };
    dc.commLost=function(reason){
      if(dc.pollIv){window.clearInterval(dc.pollIv);dc.pollIv=null;}
      dc.addLog('\u2715 Comunica\u00E7\u00E3o perdida: '+reason,'err');
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
        if(nw.__dcInstalled)return; // ainda é a página antiga
        window.clearInterval(dc.pollIv);dc.pollIv=null;
        dc.bootstrap(nw);
      },200);
    };
    dc.bootstrap=function(target){
      try{
        target.__devconsole=window;
        target.eval(dc.installerSrc);
      }catch(e){
        dc.addLog('\u2715 Falha ao injetar (CSP unsafe-eval?): '+e.message,'err');
        dc.disableInput('CSP bloqueou eval');
      }
    };

    // Event handlers (registrados no realm da popup — sobrevivem postback)
    d.getElementById('__dc_tC').addEventListener('click',function(){dc.activate('c');});
    d.getElementById('__dc_tN').addEventListener('click',function(){dc.activate('n');});
    d.getElementById('__dc_clear').addEventListener('click',function(){
      if(d.getElementById('__dc_paneC').classList.contains('active'))d.getElementById('__dc_log').innerHTML='';
      else d.getElementById('__dc_netList').innerHTML='';
    });
    d.getElementById('__dc_netList').addEventListener('click',function(ev){
      var row=ev.target.closest&&ev.target.closest('.nrow');
      if(!row||!row.dataset.entryId)return;
      var next=row.nextElementSibling;
      if(next&&next.classList.contains('ndet')){next.remove();row.classList.remove('exp');return;}
      var e=dc.entries[row.dataset.entryId];if(!e)return;
      row.classList.add('exp');
      var t=e.time?new Date(e.time).toLocaleTimeString():'\u2014';
      var lines=['URL: '+e.url,'Method: '+(e.method||'\u2014'),'Status: '+(e.status==null?'\u2014':e.status),'Type: '+(e.type||'\u2014'),'Size: '+fSize(e.size),'Duration: '+fTime(e.duration),'Time: '+t];
      if(e.reqBody)lines.push('','Request body:',e.reqBody);
      if(e.resPreview)lines.push('','Response preview:',e.resPreview);
      if(e.error)lines.push('','Error: '+e.error);
      var det=d.createElement('div');det.className='ndet';det.textContent=lines.join('\n');
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

  // ===========================================================
  // Bootstrap
  // ===========================================================
  w.__dc={installerSrc:'('+targetInstaller.toString()+')()',entries:{},counter:0};
  try{
    w.eval('('+popupSetup.toString()+')()');
  }catch(e){
    alert('Falha ao instalar popup-side: '+e.message);return;
  }
  w.__dc.addLog('DevConsole v'+version+' carregado em '+build+'.','warn');
  w.__dc.bootstrap(w.opener===null?window:window);
})();