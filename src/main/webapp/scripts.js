var stompClient = null;
var notificationCount = 0;

$(document).ready(function() {
  console.log("Index page is ready");
  connect();

  $("#send").click(function() {
    sendMessage();
  });

  $("#send-private").click(function() {
    sendPrivateMessage();
  });

  $("#notifications").click(function() {
    resetNotificationCount();
  });
});

function connect() {
  var socket = new SockJS('http://localhost:8080/our-websocket');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    updateNotificationDisplay();
    stompClient.subscribe('http://localhost:8080/topic/messages', function (message) {
      showMessage(JSON.parse(message.body).content);
    });

    stompClient.subscribe('http://localhost:8080/user/topic/private-messages', function (message) {
      showMessage(JSON.parse(message.body).content);
    });

    stompClient.subscribe('http://localhost:8080/topic/global-notifications', function (message) {
      notificationCount = notificationCount + 1;
      updateNotificationDisplay();
    });

    stompClient.subscribe('http://localhost:8080/user/topic/private-notifications', function (message) {
      notificationCount = notificationCount + 1;
      updateNotificationDisplay();
    });
  });
}

function showMessage(message) {
  $("#messages").append("<tr><td>" + message + "</td></tr>");
}

function sendMessage() {
  console.log("sending message");
  stompClient.send("http://localhost:8080/ws/message", {}, JSON.stringify({'messageContent': $("#message").val()}));
}

function sendPrivateMessage() {
  console.log("sending private message");
  stompClient.send("http://localhost:8080/ws/private-message", {}, JSON.stringify({'messageContent': $("#private-message").val()}));
}

function updateNotificationDisplay() {
  if (notificationCount == 0) {
    $('#notifications').hide();
  } else {
    $('#notifications').show();
    $('#notifications').text(notificationCount);
  }
}

function resetNotificationCount() {
  notificationCount = 0;
  updateNotificationDisplay();
}
