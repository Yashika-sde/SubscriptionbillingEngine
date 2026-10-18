function getCookie(name) {
  const m = document.cookie.match(new RegExp("(?:^|; )" + name + "=([^;]*)"));
  return m ? decodeURIComponent(m[1]) : null;
}
function esc(s) {
  return String(s ?? "").replace(/[&<>"']/g, c =>
    ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
}


async function api(path, options = {}) {
  const method = (options.method || "GET").toUpperCase();
  const headers = { "Content-Type": "application/json", ...(options.headers || {}) };
  if (method !== "GET") {
    const token = getCookie("XSRF-TOKEN");
    if (token) headers["X-XSRF-TOKEN"] = token;
  }
  const res = await fetch(path, { ...options, method, headers, credentials: "same-origin" });

  if (res.status === 401 || (res.redirected && res.url.includes("/login.html"))) {
    window.location.href = "/login.html";
    throw new Error("Not logged in");
  }
  const data = await res.json().catch(() => null);
  if (!res.ok) throw new Error((data && data.message) || "Request failed (" + res.status + ")");
  return data;
}

function money(value) {
  return "₹" + Number(value).toLocaleString("en-IN", { minimumFractionDigits: 2 });
}

async function logout() {
  const token = getCookie("XSRF-TOKEN");
  await fetch("/logout", { method: "POST", headers: { "X-XSRF-TOKEN": token || "" } });
  window.location.href = "/login.html?logout";
}

function renderNav(active) {
  const links = [
    ["dashboard.html", "Dashboard"], ["customers.html", "Customers"],
    ["plans.html", "Plans"], ["subscriptions.html", "Subscriptions"],
    ["invoices.html", "Invoices"]
  ];
  document.getElementById("nav").innerHTML =
    '<span class="brand">Billing Engine</span>' +
    links.map(([href, label]) =>
      '<a href="/' + href + '"' + (label === active ? ' class="active"' : "") + ">" + label + "</a>"
    ).join("") +
    '<button class="link-btn" onclick="logout()">Logout</button>';
}