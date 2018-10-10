var stompClient = null;
var socket = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#stream").show();
    }
//    else {
//        $("#stream").hide();
//    }
//    $("#history").html("");
}

function connect() {
    socket = new SockJS('/cryptchas-websocket');
    socket.onclose = (event) => {
    alert("Server Disconnect You");
      console.log("WebSocket is closed now.");
    };
    stompClient = Stomp.over(socket);
    stompClient.connect({}, (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/notifier/updates', (updateMessage) => showCryptoUpdates(updateMessage.body));

        stompClient.subscribe('/notifier/info', (marketInfo) => {
            console.log("Market cap: " + marketInfo.body)
            let responseBody = JSON.parse(marketInfo.body)
            console.log("Market cap: " + responseBody.totalMarketCap)
            $("#totalMarketCap").text("Market cap: " + responseBody.totalMarketCap);
            $("#totalVolume24h").text("24h Vol: " + responseBody.totalVolume24h);
            $("#btcDominance").text("BTC Dominance: " + responseBody.btcDominance);
            $("#amountCryptoCurrencies").text("Cryptos: " + responseBody.amountCryptoCurrencies);
        });

        setInterval(() => stompClient.send("/app/global-market-info", {}, ""), 5000);
        requestServerForUpdates();
        sendDesktopNotification();
    });
}

function disconnect() {
    stompClient.send("/app/disconnect", {}, "");
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function requestServerForUpdates() {
//    stompClient.send("/app/top-volume-coins", {}, "1");
//    stompClient.send("/app/volumes", {}, "");
    stompClient.send("/app/global-market-info", {}, "");
    stompClient.send("/app/global-market-cap", {}, "");
    stompClient.send("/app/prices", {}, "");
}

function showCryptoUpdates(message) {
    $("#history").append("<tr><td>" + message + "</td></tr>");
    sendDesktopNotification(message);
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());

    requestDesktopNotificationPermission();
    connect();

    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());

    window.addEventListener("beforeunload", (event) => {
      disconnect();
      event.preventDefault();
    });
});

function requestDesktopNotificationPermission(){
 if(Notification && Notification.permission === 'default') {
   Notification.requestPermission((permission) => {
      if(!('permission' in Notification)) {
        Notification.permission = permission;
      }
   });
 }
}

function sendDesktopNotification(text) {
    let notification = new Notification('News', {
      body: text,
      tag: 'soManyNotification'
    });
    //’tag’ handles muti tab scenario i.e when multiple tabs are
    // open then only one notification is sent

    //3. handle notification events and set timeout
    notification.onclick = function() {
      parent.focus();
      window.focus(); //just in case, older browsers
      this.close();
    };
    setTimeout(notification.close.bind(notification), 2000);
  }

