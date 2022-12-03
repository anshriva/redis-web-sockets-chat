Launch redis on local using : 
```redis-cli```

make sure your redis runs on localhost:6379 OR you can change the redis connection settings in Publisher.java and Subscriber.java

Launch the application on local


```
let ws = new WebSocket('ws://localhost:8080/user/channelId');
message => {
console.log(message.data);
}
ws.send("reciverId->Hi");
```


