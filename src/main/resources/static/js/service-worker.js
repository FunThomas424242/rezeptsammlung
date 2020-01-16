"use strict";

var CACHE_NAME = "rezeptsammlung-cache-v1";
// This script won't be parsed by JS engines
// because its type is javascript/worker.


function sendRequest ( request ){
    return fetch( request ).then(
        function(response) {
        // Check if we received a valid response
        if(!response || response.status !== 200 || response.type !== "basic") {
          return response;
        }

        // IMPORTANT: Clone the response. A response is a stream
        // and because we want the browser to consume the response
        // as well as the cache consuming the response, we need
        // to clone it so we have two streams.
        var responseToCache = response.clone();

        caches.open(CACHE_NAME)
          .then(function(cache) {
            cache.put( request, responseToCache);
          });

        return response;
        }
    );
}



self.addEventListener("fetch", function(event) {
  event.respondWith(
    caches.match(event.request)
      .then(function(response) {
        // Cache hit - return response
        if (response) {
          return response;
        }
        return sendRequest(event.request);
      })
    );
});



