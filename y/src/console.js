// fetch('https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/console.js?t='+Date.now()).then(r=>r.text()).then(t=>(0,eval)(t))
(function(){
  var version='0.112';
  var build=new Date().toISOString().slice(0,16).replace('T',' ');

  var w=window.open('about:blank','_blank','width=900,height=600');
  if(!w){alert('Popup bloqueado');return;}
  var d=w.document;
  d.head.innerHTML='';d.body.innerHTML='';
  d.title='DevConsole v'+version;

  var css='body{margin:0;font:12px/1.4 Menlo,Monaco,Consolas,monospace;background:#1e1e1e;color:#e0e0e0;display:flex;flex-direction:column;height:100vh}'
   +'#tabs{display:flex;background:#252526;border-bottom:1px solid #444;align-items:stretch}'
   +'.tab{padding:8px 16px;cursor:pointer;color:#888;border-right:1px solid #333;user-select:none}'
   +'.tab.active{background:#1e1e1e;color:#fff}'
   +'.tab:hover{color:#ccc}'
   +'.spacer{flex:1}'
   +'.tbtn{padding:8px 12px;cursor:pointer;color:#888;user-select:none}.tbtn:hover{color:#fff}'
   +'.ver{padding:8px 10px;color:#666;font-size:11px;cursor:default;user-select:none}'
   +'.pane{flex:1;overflow-y:auto;display:none}'
   +'.pane.active{display:block}'
   +'#log{padding:6px 10px}'
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
   +'#bar{display:flex;border-top:1px solid #444;background:#252526}'
   +'#cmd{flex:1;background:transparent;color:#e0e0e0;border:0;padding:8px 10px;font:inherit;resize:none;outline:none;min-height:38px}'
   +'button{background:#0e639c;color:#fff;border:0;padding:0 14px;cursor:pointer;font:inherit}button:hover{background:#1177bb}';
  var st=d.createElement('style');st.textContent=css;d.head.appendChild(st);

  function el(t,p){var e=d.createElement(t);if(p)Object.assign(e,p);return e;}

  var tabsBar=el('div',{id:'tabs'});
  var tC=el('div',{className:'tab active',textContent:'Console'});
  var tN=el('div',{className:'tab',textContent:'Network'});
  var sp=el('div',{className:'spacer'});
  var ver=el('div',{className:'ver',textContent:'v'+version+'  ['+build+']',title:'Vers\u00E3o do c\u00F3digo carregado'});
  var bClr=el('div',{className:'tbtn',textContent:'\u232B Clear'});
  tabsBar.append(tC,tN,sp,ver,bClr);

  var paneC=el('div',{className:'pane active'});
  var log=el('div',{id:'log'});paneC.appendChild(log);

  var paneN=el('div',{className:'pane'});
  var nHead=el('div',{className:'nrow nhead'});
  ['Method','URL','Status','Type','Size','Time'].forEach(function(t){nHead.appendChild(el('div',{textContent:t}));});
  var netList=el('div');
  paneN.append(nHead,netList);

  var bar=el('div',{id:'bar'});
  var cmd=el('textarea',{id:'cmd',rows:2,placeholder:'Enter executa  \u2022  Shift+Enter nova linha  \u2022  Ctrl+\u2191/\u2193 hist\u00F3rico'});
  var runBtn=el('button',{textContent:'Run'});
  bar.append(cmd,runBtn);

  d.body.append(tabsBar,paneC,paneN,bar);

  function activate(which){
    tC.classList.toggle('active',which==='c');
    tN.classList.toggle('active',which==='n');
    paneC.classList.toggle('active',which==='c');
    paneN.classList.toggle('active',which==='n');
    bar.style.display=which==='c'?'flex':'none';
    if(which==='c')cmd.focus();
  }
  tC.addEventListener('click',function(){activate('c');});
  tN.addEventListener('click',function(){activate('n');});
  bClr.addEventListener('click',function(){
    if(paneC.classList.contains('active'))log.innerHTML='';
    else netList.innerHTML='';
  });

  // ===== Console helpers =====
  var hist=[],hi=-1;
  function fmt(v){
    if(v===undefined)return'undefined';
    if(v===null)return'null';
    if(typeof v==='function')return v.toString();
    if(typeof v==='object'){
      try{return JSON.stringify(v,function(k,val){
        if(typeof val==='function')return'[Function]';
        if(val instanceof Node)return'[DOM '+val.nodeName+']';
        return val;
      },2);}catch(e){return String(v);}
    }
    return String(v);
  }
  function logAdd(text,cls){
    var x=el('div');x.className='entry '+cls;x.textContent=text;
    log.appendChild(x);
    if(paneC.classList.contains('active'))log.scrollTop=log.scrollHeight;
  }
  var gEval=(0,eval);
  function run(){
    var code=cmd.value;
    if(!code.trim())return;
    logAdd(code,'inp');
    hist.push(code);hi=hist.length;
    try{
      // sempre roda no escopo global do target atual, se ainda houver
      var t=currentTarget||window;
      var r=(t===window)?gEval(code):t.eval(code);
      logAdd(fmt(r),'out');
    }catch(e){logAdd(e.message,'err');}
    cmd.value='';cmd.focus();
  }
  runBtn.addEventListener('click',run);
  cmd.addEventListener('keydown',function(e){
    if(e.key==='Enter'&&!e.shiftKey){e.preventDefault();run();}
    else if(e.key==='ArrowUp'&&(cmd.value===''||e.ctrlKey)){
      if(hi>0){hi--;cmd.value=hist[hi];}e.preventDefault();
    }else if(e.key==='ArrowDown'&&e.ctrlKey){
      if(hi<hist.length-1){hi++;cmd.value=hist[hi];}
      else{hi=hist.length;cmd.value='';}
      e.preventDefault();
    }
  });
  cmd.focus();

  // ===== Network helpers =====
  function fSize(b){if(b==null||isNaN(b)||b===0)return'\u2014';if(b<1024)return b+' B';if(b<1048576)return(b/1024).toFixed(1)+' K';return(b/1048576).toFixed(2)+' M';}
  function fTime(ms){if(ms==null||isNaN(ms))return'\u2014';if(ms<1000)return Math.round(ms)+' ms';return(ms/1000).toFixed(2)+' s';}
  function sCls(s){if(s==='pending')return'sp';var n=parseInt(s,10);if(n>=200&&n<300)return's2';if(n>=300&&n<400)return's3';if(n>=400&&n<500)return's4';if(n>=500)return's5';return'';}
  function shortUrl(u){try{var x=new URL(u);return x.pathname+(x.search||'')+'  ('+x.hostname+')';}catch(_){return u;}}

  function addNet(e){
    var row=el('div',{className:'nrow'});
    var m=el('div',{className:'nm',textContent:e.method||'\u2014'});
    var u=el('div',{className:'nu',textContent:shortUrl(e.url),title:e.url});
    var s=el('div',{className:'ns '+sCls(e.status),textContent:e.status||'\u2014'});
    var t=el('div',{textContent:e.type||'\u2014'});
    var sz=el('div',{textContent:fSize(e.size)});
    var dr=el('div',{textContent:fTime(e.duration)});
    row.append(m,u,s,t,sz,dr);

    var detail=null;
    row.addEventListener('click',function(){
      if(detail){detail.remove();detail=null;row.classList.remove('exp');return;}
      row.classList.add('exp');
      var lines=['URL: '+e.url,'Method: '+(e.method||'\u2014'),'Status: '+(e.status||'\u2014'),'Type: '+(e.type||'\u2014'),'Size: '+fSize(e.size),'Duration: '+fTime(e.duration),'Time: '+(e.time?e.time.toLocaleTimeString():'\u2014')];
      if(e.reqBody)lines.push('','Request body:',e.reqBody);
      if(e.resPreview)lines.push('','Response preview:',e.resPreview);
      if(e.error)lines.push('','Error: '+e.error);
      detail=el('div',{className:'ndet',textContent:lines.join('\n')});
      row.parentNode.insertBefore(detail,row.nextSibling);
    });

    netList.appendChild(row);
    e._upd=function(){
      m.textContent=e.method||'\u2014';
      s.textContent=e.status||'\u2014';s.className='ns '+sCls(e.status);
      sz.textContent=fSize(e.size);dr.textContent=fTime(e.duration);
    };
    if(paneN.classList.contains('active'))paneN.scrollTop=paneN.scrollHeight;
  }

  // ===== Install: aplica os wraps numa janela alvo =====
  var currentTarget=null;
  function install(target){
    if(currentTarget===target)return;
    currentTarget=target;

    // fetch
    try{
      var oF=target.fetch.bind(target);
      target.fetch=function(input,init){
        var url=typeof input==='string'?input:(input&&input.url)||'';
        var method=(init&&init.method)||(typeof input==='object'&&input&&input.method)||'GET';
        var e={method:method.toUpperCase(),url:url,type:'fetch',status:'pending',time:new Date()};
        var t0=performance.now();
        try{if(init&&init.body)e.reqBody=typeof init.body==='string'?init.body.slice(0,1000):'[non-string body]';}catch(_){}
        addNet(e);
        return oF(input,init).then(function(res){
          e.status=res.status;e.duration=performance.now()-t0;
          try{
            var cl=res.clone();
            cl.text().then(function(txt){e.resPreview=txt.slice(0,1000);e.size=txt.length;e._upd();}).catch(function(){});
          }catch(_){}
          e._upd();
          return res;
        }).catch(function(err){
          e.status='ERR';e.error=err.message;e.duration=performance.now()-t0;e._upd();throw err;
        });
      };
    }catch(_){}

    // XHR
    try{
      var OO=target.XMLHttpRequest.prototype.open;
      var OS=target.XMLHttpRequest.prototype.send;
      target.XMLHttpRequest.prototype.open=function(m,u){this._n={method:(m||'').toUpperCase(),url:u,type:'xhr'};return OO.apply(this,arguments);};
      target.XMLHttpRequest.prototype.send=function(body){
        var e=this._n;
        if(e){
          e.status='pending';e.time=new Date();
          try{if(body)e.reqBody=typeof body==='string'?body.slice(0,1000):'[non-string body]';}catch(_){}
          var t0=performance.now(),self=this;
          addNet(e);
          this.addEventListener('loadend',function(){
            e.status=self.status||'ERR';e.duration=performance.now()-t0;
            try{
              if(self.responseType===''||self.responseType==='text'){
                var txt=self.responseText||'';
                e.size=txt.length;e.resPreview=txt.slice(0,1000);
              }else if(self.response){
                e.size=self.response.byteLength||self.response.size||0;
              }
            }catch(_){}
            e._upd();
          });
        }
        return OS.apply(this,arguments);
      };
    }catch(_){}

    // Console
    ['log','info','warn','error'].forEach(function(m){
      try{
        var o=target.console[m];
        target.console[m]=function(){
          logAdd(Array.prototype.map.call(arguments,fmt).join(' '),m==='error'?'err':(m==='warn'?'warn':'out'));
          o.apply(target.console,arguments);
        };
      }catch(_){}
    });

    // PerformanceObserver pra recursos passivos
    try{
      var seen=new Set();
      var po=new target.PerformanceObserver(function(list){
        list.getEntries().forEach(function(pe){
          if(pe.initiatorType==='fetch'||pe.initiatorType==='xmlhttprequest')return;
          var k=pe.name+'|'+pe.startTime;if(seen.has(k))return;seen.add(k);
          addNet({method:'GET',url:pe.name,type:pe.initiatorType||'other',status:pe.responseStatus||200,duration:pe.duration,size:pe.transferSize||pe.encodedBodySize||0,time:new Date()});
        });
      });
      po.observe({type:'resource',buffered:true});
    }catch(_){}
  }

  // ===== Lifecycle: detecta postback, reinjeta, anuncia status =====
  function attachLifecycle(target){
    var prevDoc=target.document;
    var prevUrl='';
    try{prevUrl=target.location.href;}catch(_){prevUrl='(desconhecida)';}

    try{
      target.addEventListener('beforeunload',function(){
        if(w.closed)return;
        logAdd('\u26A0 Postback detectado em '+prevUrl+'. Aguardando nova p\u00E1gina...','warn');
        // usa setInterval da POPUP pra sobreviver \u00e0 morte do opener
        var iv=w.setInterval(function(){
          if(w.closed){w.clearInterval(iv);return;}
          var nw;
          try{nw=w.opener;}catch(_){nw=null;}
          if(!nw||nw.closed){
            w.clearInterval(iv);
            currentTarget=null;
            logAdd('\u2715 Comunica\u00E7\u00E3o perdida totalmente: janela original foi fechada. Reclique o bookmarklet em outra aba pra reabrir.','err');
            return;
          }
          var newDoc;
          try{newDoc=nw.document;}
          catch(_){
            w.clearInterval(iv);
            currentTarget=null;
            logAdd('\u2715 Comunica\u00E7\u00E3o perdida totalmente: nova p\u00E1gina em origem diferente (cross-origin). Reclique o bookmarklet l\u00E1 pra reconectar.','err');
            return;
          }
          if(newDoc===prevDoc)return;
          if(newDoc.readyState!=='complete')return;
          var newUrl;
          try{newUrl=nw.location.href;}
          catch(_){
            w.clearInterval(iv);
            currentTarget=null;
            logAdd('\u2715 Comunica\u00E7\u00E3o perdida totalmente: nova p\u00E1gina em origem diferente (cross-origin). Reclique o bookmarklet l\u00E1 pra reconectar.','err');
            return;
          }
          w.clearInterval(iv);
          try{
            currentTarget=null; // permite reinstalar
            install(nw);
            attachLifecycle(nw);
            logAdd('Reconectado em: '+newUrl,'ok');
          }catch(err){
            currentTarget=null;
            logAdd('\u2715 Comunica\u00E7\u00E3o perdida: falha ao reinjetar ('+err.message+').','err');
          }
        },200);
      });
    }catch(err){
      logAdd('N\u00E3o consegui anexar lifecycle: '+err.message,'err');
    }
  }

  // ===== Bootstrap =====
  install(window);
  attachLifecycle(window);

  logAdd('DevConsole v'+version+' carregado em '+build+'. S\u00F3 captura requisi\u00E7\u00F5es feitas a partir de agora.','warn');
})();