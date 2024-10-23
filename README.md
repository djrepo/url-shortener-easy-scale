# url-shortener
Highly scale-able, URL shortener MVP using Zookeeper, Cassandra, Redis as a cache, Spring-Boot, and Kotlin

<!DOCTYPE html>
<html>


<body class="stackedit">
  <div class="stackedit__html"><h1 id="matching-prefixes">URL Shortener</h1>
<p>Url Shortener is the services that convert long urls (which can be over a hundred character long) to short urls. </p>
<h1 id="solution">Instruction</h1>
<p>For the MVP I only care that I provide a proper REST API that will enable other users/ clients to send a URL and will receive some kind of identifier/ hash to which this URL corresponds in other system. And the other way around of course, meaning that a user should be able to resolve the full URL.</p>
<h1 id="solution">Solution</h1>
<p>Main approach is that we can have a distributed counter service which can provide unique counter to node whenever required.Every counter service node will have a predefined range of counters from which it can give the counter to application server. Whenever any application node gets request to convert long url to short url, it asks counter service for the counter and after receiving that counter it converts it to short url after encoding.
<br>
<h1 id="structure-and-configuration">Tech Structure and Configuration</h1>
The MVP was implemented by Kotlin and uses Zookeeper to assign the id and counter range to the application server. It will mark that counter range as “used” after assigning it to the server. It won’t give the same range to any other application server ever again.TinyURL is registered in Cassandra and Redis for cashing and database scalability. There are two API for generating the short URLs and resolving them by redirect response.
<br>
Accessing the services can use by the **"docker compose up"** command and running the build.
<br>
The solution would be a** MVP** that implements the basic functionality and covers with some basic tests too.
</div>
</body>

</html>
