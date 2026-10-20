function getCookie(name) {
  const m = document.cookie.match(new RegExp("(?:^|; )" + name + "=([^;]*)"));
  return m ? decodeURIComponent(m[1]) : null;
}

function esc(s) {
  return String(s ?? "").replace(/[&<>"']/g, c =>
    ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
}

function animateNumber(el, to, format = String, ms = 900) {
  const start = performance.now();
  const target = Number(to);
  function tick(now) {
    const t = Math.min((now - start) / ms, 1);
    el.textContent = format(target * (1 - Math.pow(1 - t, 3)));
    if (t < 1) requestAnimationFrame(tick);
  }
  requestAnimationFrame(tick);
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
  renderHero(active);
}

function renderHero(active) {
  const info = {
    "Dashboard": ["📊", "Revenue and billing at a glance"],
    "Customers": ["👥", "People who subscribe to your plans"],
    "Plans": ["🏷️", "Pricing tiers and billing cycles"],
    "Subscriptions": ["🔁", "Manage, upgrade and cancel"],
    "Invoices": ["🧾", "Every bill, its payment status and retries"]
  }[active] || ["✨", ""];
  const main = document.querySelector("main");
  const h1 = main && main.querySelector("h1");
  if (!h1) return;
  const hero = document.createElement("header");
  hero.className = "hero";
  hero.innerHTML =
    '<div class="blob b1"></div><div class="blob b2"></div>' +
    '<div class="hero-inner' + (main.classList.contains("wide") ? " wide" : "") + '">' +
    '<span class="hero-icon">' + info[0] + "</span>" +
    "<div><h1>" + esc(h1.textContent) + "</h1><p>" + esc(info[1]) + "</p></div></div>";
  document.getElementById("nav").after(hero);
  h1.remove();
}