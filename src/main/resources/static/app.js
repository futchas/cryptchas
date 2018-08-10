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
    socket.onclose = function(event) {
    alert("Server Disconnect You");
      console.log("WebSocket is closed now.");
    };
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/notifier/updates', function (updateMessage) {
            showCryptoUpdates(updateMessage.body);
        });
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
    stompClient.send("/app/top-volume-coins", {}, "1");
    stompClient.send("/app/volumes", {}, "");
}

function showCryptoUpdates(message) {
    $("#history").append("<tr><td>" + message + "</td></tr>");
    sendDesktopNotification("jdfkdfjk");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    requestDesktopNotificationPermission();
    connect();

    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });

    window.addEventListener("beforeunload", function (event) {
      disconnect();
      event.preventDefault();
    });
});

function requestDesktopNotificationPermission(){
 if(Notification && Notification.permission === 'default') {
   Notification.requestPermission(function (permission) {
      if(!('permission' in Notification)) {
        Notification.permission = permission;
      }
   });
 }
}

function sendDesktopNotification(text) {
    let notification = new Notification('Your Page Title', {
      body: "the body sdsdsdsds" + text,
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

