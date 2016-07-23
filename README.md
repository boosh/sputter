# Akka HTTP Components
This project contains a collection of akka-http components commonly 
needed by web apps and sites so you can get up and running on a new 
project quickly. 

Functionality is non-blocking where possible, implemented either as 
classes that return Futures or as actors. Database access is coded to 
interfaces with a default layer based on doobie provided. If you want 
to use a different storage engine, just implement your own classes and 
update the DI config.

REST endpoints are provided for some packages. Simply add the routes 
to your project to expose them. However, since these just delegate to 
actors and other classes, it's simple to use these components using 
other transports (e.g. gRPC, relay, etc.).

The following components are included:

* Admin web interface - View and reply to contact messages, view user 
  stats, usage, etc.
* Auth - Log users in and out. Supports authentication via several 
  providers including Facebook, as well as just via a site-specific 
  username and password. A JSON Web Token (JWT) is created upon 
  successfully logging in, and an akka-http directive is provided to 
  revalidate it.
* Contact form - Lets users submit questions & feedback, and optionally
  notifies you when they do.
* Image scaler - create thumbnails of local or remote images
* Messaging - Send realtime system and/or user-to-user chat messages or 
  contact users in different ways
* Third party integration - simplify copying files to S3, etc.
* User profiles - Create and update a JSON profile for users.
* Various akka-http directives, including:
    - CorsSupport - Enable support for CORS in your akka-http server
* Misc utilities including: 
    - Sanitise user input to prevent XSS and page hijacking, etc.
    - Simple validation library that allows you to validate submitted 
      data against a set of constraints, and return error messages
      for all failures instead of just the first.

Another package contains ScalaJS web widgets built on react for:

* Contact form
* Log in
* Registration (super simple)

Logging is also configured with Log4J.

To do: 
* Certain errors shouldn't be returned to clients for security. Make sure 
  only whitelisted ones are. This should really be handled by a directive.
  See the `handleExceptions` directive which might already do this.
  