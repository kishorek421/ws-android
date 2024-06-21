const socket = new WebSocket("ws://106.51.106.43/ws");

socket.onopen = function(event) {
    console.log("Connected to the WebSocket server.");
};

socket.onmessage = function(event) {
    const chat = document.getElementById("chat");
    const newMessage = document.createElement("div");
    newMessage.textContent = event.data;
    chat.appendChild(newMessage);
};

socket.onclose = function(event) {
    console.log("Disconnected from the WebSocket server.");
};

socket.onerror = function(error) {
    console.error("WebSocket Error: ", error);
};

document.getElementById("sendButton").addEventListener("click", function() {
    const messageInput = document.getElementById("messageInput");
    const message = messageInput.value;
    if (message) {
        socket.send(message);
        messageInput.value = "";
    }
});

function makeid(length) {
    let result = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    const charactersLength = characters.length;
    let counter = 0;
    while (counter < length) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
      counter += 1;
    }
    return result;
}

document.getElementById("sendRandomButton").addEventListener("click", function() {
    setInterval(() => {
        console.log("Random Message")
        const message = makeid(10);
        if (message) {
            socket.send(message);
            messageInput.value = "";
        }
        document.getElementById("chat").scroll(0,document.documentElement.scrollHeight + 10)
    }, 10)
});
