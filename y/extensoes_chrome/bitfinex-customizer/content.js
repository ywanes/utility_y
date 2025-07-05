// https://trading.bitfinex.com/t/BTC:USD?type=exchange

// Função principal atualizada
function customizeBitfinex() {
  try {
    // 1. Remove a sidebar
    const sidebar = document.querySelector('.sidebar.trading-app.slideout');
    if (sidebar) {
      console.log('Removendo sidebar...');
      sidebar.remove();
    }

    // 2. Remove o footer
    const footer = document.getElementById('footer');
    if (footer) {
      console.log('Removendo footer...');
      footer.remove();
    }

    // 3. Remove elementos antes do depth-chart-header
    const depthChartHeader = document.getElementById('depth-chart-header');
    if (depthChartHeader) {
      console.log('Removendo elementos anteriores ao depth-chart-header...');
      while (depthChartHeader.previousSibling !== null) {
        depthChartHeader.previousSibling.remove();
      }
    }

    // 4. NOVO: Remove o header wrapper
    const headerWrapper = document.querySelector('.header__wrapper');
    if (headerWrapper) {
      console.log('Removendo header wrapper...');
      headerWrapper.remove();
    }

    // 5. NOVO: Ajusta o margin do ticker-side
    const tickerSide = document.querySelector('.ticker-side.ticker-side.default-theme.dark-mode.sidebar-left');
    if (tickerSide) {
      console.log('Ajustando margin do ticker-side...');
      const currentStyle = tickerSide.getAttribute('style') || '';
      tickerSide.setAttribute('style', currentStyle.replace('margin: 0px;', 'margin-top: -50px;'));
    }

    // Ajustes adicionais de layout
    const mainContent = document.querySelector('.main-content');
    if (mainContent) {
      mainContent.style.marginLeft = '0';
      mainContent.style.width = '100%';
    }

  } catch (error) {
    console.error('Erro durante a customização:', error);
  }
}

// Estratégia de espera para páginas dinâmicas
function waitForPageLoad() {
  // Tentativa imediata
  customizeBitfinex();

  // Observador de mutação para elementos dinâmicos
  const observer = new MutationObserver((mutations) => {
    let shouldRetry = false;
    
    mutations.forEach((mutation) => {
      if (!shouldRetry) {
        shouldRetry = mutation.addedNodes.length > 0;
      }
    });

    if (shouldRetry) {
      customizeBitfinex();
    }
  });

  // Observa o corpo do documento
  observer.observe(document.body, {
    childList: true,
    subtree: true
  });

  // Timeout de segurança
  setTimeout(() => {
    observer.disconnect();
    customizeBitfinex(); // Última tentativa
  }, 15000); // 15 segundos como limite máximo
}

// Inicia o processo
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', waitForPageLoad);
} else {
  waitForPageLoad();
}