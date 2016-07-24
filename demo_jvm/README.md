# Demo server using sputter components
This contains a demo akka-http server that uses sputter components with 
mock datastores that just log messages and return static data.
 
## Running
Run this with `sbt "project demo_jvm" run` from the directory containing
`build.sbt`, or build a fat jar with `sbt "project demo_jvm" assembly`.

## Making requests
If using the default settings, you can make requests from the command 
line with:

```
curl -H "Content-Type: application/json" -X POST \
     -d '{"body":"test contact form body", "name": "Me", "email": "me@example.com"}' \
     http://localhost:8080/contact
```

Check the server's log messages and the curl response to see what's 
going on.
