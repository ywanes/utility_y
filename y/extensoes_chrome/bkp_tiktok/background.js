chrome.runtime.onInstalled.addListener(async () => {
  try {
    const rules = [
      {
        id: 1,
        priority: 1,
        action: {
          type: "redirect",
          redirect: {
            regexSubstitution: "http://meusite.com/?\\0"
			/*
				esse é um bkp, o original esta aqui => D:\ProgramFiles\extensoes_chrome\tiktok
				redirecionando para o site que faz redirect
				function tryRedirectTikTok(a){ // http://meusite.com/?https://vm.tiktok.com/ZMAmGMPjm/
					if ( a.indexOf('.tiktok.com') > -1 ){
						const xhr = new XMLHttpRequest();
						xhr.open("POST", "https://www.tikwm.com/api/", true); // URL de destino
						xhr.setRequestHeader("Content-Type", "application/json");
						xhr.onreadystatechange = () => {
						  if (xhr.readyState === 4) { // requisição completa
							if (xhr.status >= 200 && xhr.status < 300) {
							  window.location.href=JSON.parse(xhr.responseText)['data']['play'];
							} else {
							  console.error("Erro HTTP:", xhr.status);
							  alert('nao foi possivel decodificar a url!');
							}
						  }
						};
						xhr.send(JSON.stringify({ "url": a }));
					}
				}			
			*/
          }
        },
        condition: {
          regexFilter: "^(https://vm\\.tiktok\\.com/.*)",
          resourceTypes: ["main_frame"]
        }
      }
    ];

    // Remove regras existentes e adiciona novas
    const existingRules = await chrome.declarativeNetRequest.getDynamicRules();
    const existingRuleIds = existingRules.map(rule => rule.id);
    
    await chrome.declarativeNetRequest.updateDynamicRules({
      removeRuleIds: existingRuleIds,
      addRules: rules
    });

    console.log('Regras de redirecionamento configuradas com sucesso!');
  } catch (error) {
    console.error('Erro ao configurar regras:', error);
  }
});