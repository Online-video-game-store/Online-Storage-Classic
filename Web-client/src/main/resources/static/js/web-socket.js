document.addEventListener("DOMContentLoaded", function () {
    const protocol = window.location.protocol === "https:" ? "wss" : "ws";
    const host = window.location.hostname;
    console.log("host = " + host);
    const socket = new WebSocket(`${protocol}://${host}:9000/ws/notifications`);
    // const socket = new WebSocket("ws://localhost:9000/ws/notifications");
    socket.onopen = () => console.log("✔ WS OPEN");
    socket.onerror = (e) => console.error("❌ WS Error", e);

    socket.onopen = function () {
        console.log("WebSocket connection established.");
    };

    socket.onmessage = function (event) {
        console.log("Notification received: " + event.data);
        alert("Уведомление: " + event.data);
        window.location.reload();
    };

    socket.onerror = function(error) {
        console.error("WebSocket Error: ", error);
    };

    socket.onclose = function(event) {
        console.log("WebSocket connection closed.", event);
        if (event.wasClean) {
            console.log(`Closed cleanly, code=<span class="math-inline">\{event\.code\} reason\=</span>{event.reason}`);
        } else {
            console.error('Connection died');
        }
    };

});
