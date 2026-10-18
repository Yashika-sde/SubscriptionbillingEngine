const params = new URLSearchParams(window.location.search);
const msg = document.getElementById("msg");
if (params.has("error")) {
  msg.textContent = "Invalid username or password";
  msg.hidden = false;
} else if (params.has("logout")) {
  msg.textContent = "You have been logged out";
  msg.hidden = false;
}