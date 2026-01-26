// script_simple.js
const services = {
  healthCheck: {
    name: "Health Check",
    url: "http://110.238.69.203:8080/healthCheck",
    method: "GET",
    statusElementId: "healthStatus",
    resultClass: "health"
  },
  dewInfo: {
    name: "DEW Info",
    url: "http://110.238.69.203:8080/dew-info",
    method: "GET",
    statusElementId: "dewStatus",
    resultClass: "dew"
  },
  clientes: {
    name: "Clientes API",
    url: "http://110.238.69.203:8080/clientes",
    method: "GET",
    statusElementId: "clientesStatus",
    resultClass: "clientes"
  }
};

let appState = { isChecking: false };

document.addEventListener('DOMContentLoaded', function() {
  setupEventListeners();
});

function setupEventListeners() {
  document.getElementById('healthCheckBtn')?.addEventListener('click', () => checkService('healthCheck'));
  document.getElementById('dewInfoBtn')?.addEventListener('click', () => checkService('dewInfo'));
  document.getElementById('clientesBtn')?.addEventListener('click', () => checkService('clientes'));
  document.getElementById('checkAllBtn')?.addEventListener('click', checkAllServices);
  document.getElementById('clearBtn')?.addEventListener('click', clearResults);
}

// FUNCIÓN PRINCIPAL MEJORADA - Usar proxy confiable
async function makeRequest(url, method = 'GET') {
  // Siempre usar proxy cuando estamos en local
  const proxyUrl = `https://api.allorigins.win/raw?url=${encodeURIComponent(url)}`;
  
  try {
    console.log(`Usando proxy: ${proxyUrl}`);
    const response = await fetch(proxyUrl, {
      method: method,
      headers: {
        'Accept': 'application/json, text/html, text/plain, */*'
      }
    });
    
    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`);
    }
    
    const data = await response.text();
    
    // Intentar detectar el tipo de contenido
    if (data.trim().startsWith('{') || data.trim().startsWith('[')) {
      try {
        return JSON.parse(data);
      } catch {
        return data;
      }
    }
    
    return data;
    
  } catch (error) {
    console.error('Error con proxy:', error);
  }
}

async function checkService(serviceKey) {
  if (appState.isChecking) return;
  
  const service = services[serviceKey];
  if (!service) return;
  
  appState.isChecking = true;
  showLoading();
  
  try {
    const statusElement = document.getElementById(service.statusElementId);
    if (statusElement) {
      statusElement.textContent = "Verificando...";
      statusElement.className = "status-badge checking";
    }
    
    const data = await makeRequest(service.url, service.method);
    
    if (statusElement) {
      statusElement.textContent = "Operativo";
      statusElement.className = "status-badge up";
    }
    
    displayResult(service, data);
    
  } catch (error) {
    console.error(`Error:`, error);
    
    const statusElement = document.getElementById(service.statusElementId);
    if (statusElement) {
      statusElement.textContent = "Error";
      statusElement.className = "status-badge down";
    }
    
    displayErrorResult(service, error.message);
  } finally {
    hideLoading();
    appState.isChecking = false;
  }
}

async function checkAllServices() {
  if (appState.isChecking) return;
  
  appState.isChecking = true;
  showLoading();
  clearResults();
  
  const resultsContent = document.getElementById('resultsContent');
  if (resultsContent) {
    resultsContent.innerHTML = `
      <div style="text-align: center; padding: 40px;">
        <i class="fas fa-sync-alt fa-spin" style="font-size: 3rem; color: #4a148c; margin-bottom: 20px;"></i>
        <h3>Verificando todos los servicios...</h3>
        <p>Esto tomará unos segundos</p>
      </div>
    `;
  }
  
  // Verificar cada servicio secuencialmente
  for (const serviceKey in services) {
    await checkService(serviceKey);
    await new Promise(resolve => setTimeout(resolve, 1000));
  }
  
  hideLoading();
  appState.isChecking = false;
  displaySummary();
}

function displayResult(service, data) {
  const resultsContent = document.getElementById('resultsContent');
  if (!resultsContent) return;
  
  resultsContent.innerHTML = '';
  
  const resultCard = document.createElement('div');
  resultCard.className = `result-card ${service.resultClass}`;
  resultCard.style.animation = 'fadeIn 0.5s ease-in';
  
  const title = document.createElement('h3');
  title.innerHTML = `<i class="fas fa-${getServiceIcon(service.name)}"></i> ${service.name}`;
  
  const content = document.createElement('div');
  content.className = 'result-content';
  
  // Formatear la respuesta
  if (service.name === 'Health Check') {
    content.textContent = data;
    content.style.color = data === 'up' ? '#2e7d32' : '#c62828';
    content.style.fontWeight = 'bold';
    content.style.fontSize = '1.2rem';
  } else if (service.name === 'DEW Info') {
    content.innerHTML = data || 'Sin datos';
  } else if (service.name === 'Clientes API') {
    try {
      const jsonData = typeof data === 'string' ? JSON.parse(data) : data;
      content.innerHTML = formatJSON(jsonData);
      content.className += ' json-container';
    } catch {
      content.textContent = data || 'Sin datos';
    }
  } else {
    content.textContent = data || 'Sin datos';
  }
  
  const info = document.createElement('div');
  info.className = 'endpoint-info';
  info.innerHTML = `
    <strong>URL:</strong> ${service.url}<br>
    <strong>Hora:</strong> ${new Date().toLocaleTimeString('es-ES')}<br>
    <strong>Estado:</strong> <span style="color: #2e7d32; font-weight: bold;">✓ Operativo</span>
  `;
  
  resultCard.appendChild(title);
  resultCard.appendChild(content);
  resultCard.appendChild(info);
  resultsContent.appendChild(resultCard);
}

function displayErrorResult(service, errorMessage) {
  const resultsContent = document.getElementById('resultsContent');
  if (!resultsContent) return;
  
  resultsContent.innerHTML = '';
  
  const resultCard = document.createElement('div');
  resultCard.className = 'result-card error';
  resultCard.style.borderLeftColor = '#ff6b6b';
  resultCard.style.animation = 'fadeIn 0.5s ease-in';
  
  const title = document.createElement('h3');
  title.innerHTML = `<i class="fas fa-exclamation-triangle"></i> Error de Conexión`;
  
  const content = document.createElement('div');
  content.className = 'result-content';
  content.style.padding = '20px';
  
  // Dividir el mensaje de error para mejor legibilidad
  const lines = errorMessage.split('\n').filter(line => line.trim());
  
  let formattedMessage = '';
  for (let line of lines) {
    if (line.includes('SOLUCIÓN') || line.includes('SOLUCION')) {
      formattedMessage += `<h4 style="color: #ffd93d; margin-top: 20px;">${line}</h4>`;
    } else if (line.includes('❌') || line.includes('🚀')) {
      formattedMessage += `<h3 style="color: #ff6b6b;">${line}</h3>`;
    } else if (line.trim().startsWith('-') || line.trim().startsWith('1.') || line.trim().startsWith('2.')) {
      formattedMessage += `<p style="margin-left: 20px;">${line}</p>`;
    } else if (line.includes('python') || line.includes('http-server')) {
      formattedMessage += `
        <div style="
          background: #1a1a1a;
          color: #00ff9d;
          padding: 10px;
          border-radius: 5px;
          font-family: monospace;
          margin: 10px 0;
          overflow-x: auto;
        ">
          ${line}
        </div>
      `;
    } else {
      formattedMessage += `<p>${line}</p>`;
    }
  }
   
  resultCard.appendChild(title);
  resultCard.appendChild(content);
  resultsContent.appendChild(resultCard);
}

// Funciones auxiliares
function getServiceIcon(name) {
  const icons = {
    'Health Check': 'heartbeat',
    'DEW Info': 'cloud',
    'Clientes API': 'users'
  };
  return icons[name] || 'server';
}

function formatJSON(data) {
  return JSON.stringify(data, null, 2)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, 
      function(match) {
        let cls = 'json-number';
        if (/^"/.test(match)) {
          if (/:$/.test(match)) {
            cls = 'json-key';
          } else {
            cls = 'json-string';
          }
        } else if (/true|false/.test(match)) {
          cls = 'json-boolean';
        } else if (/null/.test(match)) {
          cls = 'json-null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
      });
}

function displaySummary() {
  // Implementación simple de resumen
  const resultsContent = document.getElementById('resultsContent');
  if (!resultsContent) return;
  
  const successCount = Object.keys(services).filter(key => 
    document.getElementById(services[key].statusElementId)?.textContent === 'Operativo'
  ).length;
  
  const summary = document.createElement('div');
  summary.className = 'result-card summary';
  summary.innerHTML = `
    <h3><i class="fas fa-chart-bar"></i> Resumen</h3>
    <div class="result-content">
      <div style="text-align: center; padding: 20px;">
        <div style="font-size: 2rem; font-weight: bold; color: ${successCount === 3 ? '#2e7d32' : successCount > 0 ? '#ff9800' : '#c62828'};">
          ${successCount}/3
        </div>
        <p>servicios operativos</p>
      </div>
    </div>
  `;
  
  resultsContent.prepend(summary);
}

function showLoading() {
  const loading = document.getElementById('loading');
  if (loading) loading.classList.remove('hidden');
}

function hideLoading() {
  const loading = document.getElementById('loading');
  if (loading) loading.classList.add('hidden');
}

function clearResults() {
  const resultsContent = document.getElementById('resultsContent');
  if (resultsContent) {
    resultsContent.innerHTML = `
      <div class="empty-state">
        <i class="fas fa-mouse-pointer"></i>
        <h3>Haz clic en un servicio para comenzar</h3>
        <p>Selecciona uno de los botones para verificar su estado</p>
      </div>
    `;
  }
}