Launch redis on local using : 
```redis-cli```

Launch the application on local


```
let ws = new WebSocket('ws://localhost:8080/user/channelId');
message => {
console.log(message.data);
}
ws.send("Hi");
```


