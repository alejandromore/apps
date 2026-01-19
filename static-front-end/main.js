// Cambia estas URLs por tus endpoints reales de APIG
const SERVICE_1_URL = "https://<tu-api-gateway>/servicio1";
const SERVICE_2_URL = "https://<tu-api-gateway>/servicio2";

async function callService1() {
  const responseEl = document.getElementById("response1");
  try {
    const res = await fetch(SERVICE_1_URL);
    const data = await res.json();
    responseEl.textContent = JSON.stringify(data, null, 2);
  } catch (err) {
    responseEl.textContent = "Error: " + err.message;
  }
}

async function callService2() {
  const responseEl = document.getElementById("response2");
  try {
    const res = await fetch(SERVICE_2_URL);
    const data = await res.json();
    responseEl.textContent = JSON.stringify(data, null, 2);
  } catch (err) {
    responseEl.textContent = "Error: " + err.message;
  }
}
