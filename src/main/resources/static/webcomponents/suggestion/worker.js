"use strict"

var CACHE_NAME = 'my-site-cache-v1';
// This script won't be parsed by JS engines
// because its type is javascript/worker.
self.onmessage = function(e) {
  self.postMessage('msg from worker '+e.data);
  var jsonPromise;
  if( e.data ){
    jsonPromise = self.sendRequest('http:localhost:8080/api/rezepte/tags?taglist='+e.data);
  }else{
    jsonPromise = self.sendRequest('http:localhost:8080/api/rezepte/tags');
  }
  jsonPromise.then( (response)=>{
    var promise = response.json();
    promise.then( (jsonObject) =>{
        var selectionContent = createSelectionContent(jsonObject);
        self.postMessage({'cmd':'replace-taglist', 'data': selectionContent});
        var text = 'Response:' + JSON.stringify( jsonObject );
        self.postMessage({'cmd':'log','msg': text});
    });
  });
};
self.addEventListener('fetch', function(event) {
  event.respondWith(
    caches.match(event.request)
      .then(function(response) {
        // Cache hit - return response
        if (response) {
          return response;
        }
        return getRequest(request);
      })
    );
});

function createSelectionContent( json ){
    var content = '';
    for (const key of Object.keys(json)) {
        content += '<option value="'+key+'">';
    }
   return content;
}

function sendRequest ( request ){
    return fetch( request ).then(
        function(response) {
        // Check if we received a valid response
        if(!response || response.status !== 200 || response.type !== 'basic') {
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
