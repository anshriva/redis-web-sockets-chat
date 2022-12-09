Launch redis on local using : 
```redis-server```

make sure your redis runs on localhost:6379 OR you can change the redis connection settings in Publisher.java and Subscriber.java

Launch the application on local.

Open the browser console and start chat as below: 
```
let ws = new WebSocket('ws://localhost:8080/user/channelId');
ws.onmessage = message => {
console.log(message.data);
}
ws.send("reciverId->Hi");
```


