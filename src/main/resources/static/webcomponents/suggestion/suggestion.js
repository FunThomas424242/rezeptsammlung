"use strict";

import {Logger} from "./logger.js";

const template = document.createElement("template");
template.innerHTML = `
    <style>

    </style>


    <label>
        <input id='eingabe' type="text" list="vorschlaege">
    </label>
    <button id="vorschlagen-button" type="submit">Vorschläge ermitteln</button>

    <datalist id="vorschlaege">
        <option value="Geben Sie eine Liste zu suchender Tags ein.">
    </datalist>

    <script id="worker1" type="javascript/worker">
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
      </script>

`;


class SuggestionInput extends HTMLElement {

    constructor() {
        super();  // immer zuerst aufrufen
        // for init attribut defaults
        // e.g. this.src = '';
        Logger.logMessage("constructor called");
    }

    connectedCallback() {
        Logger.logMessage("custom element in Seite eingehängt");
        this.erzeugeShadowDOMIfNotExists();
        Logger.logMessage("ShadowDom befüllt");
    }

    disconnectedCallback() {
        Logger.logMessage("element has been removed");
    }

    attributeChangedCallback(name, oldval, newval) {
        // do something every time the attribute changes
        Logger.logMessage(`the ${name} attribute has changed from ${oldval} to ${newval}!!`);
    }

    ersetzeVorschlagslisteMit ( content ){
        this.shadowRoot.getElementById("vorschlaege").innerHTML=content;
    }

    schreibeLogEintrag( text ){
        // Use a fragment: browser will only render/reflow once.
        var fragment = document.createDocumentFragment();
        fragment.appendChild(document.createTextNode(text));
        fragment.appendChild(document.createElement("br"));
        document.querySelector("#log").appendChild(fragment);
    }

    handleMessage(e){
        var msgObject = e.data;
        if( msgObject.cmd === "log"){
            this.schreibeLogEintrag(msgObject.msg);
        }else if( msgObject.cmd === "replace-taglist"){
            this.ersetzeVorschlagslisteMit(msgObject.data);
        }else{
            this.schreibeLogEintrag(msgObject);
        }
    }


    erzeugeShadowDOMIfNotExists() {
        if (!this.shadowRoot) {
            Logger.logMessage("creating shadow dom");
            this.attachShadow({mode: "open"});
        }
        this.shadowRoot.appendChild(template.content.cloneNode(true));

        // Worker starten
        var blob = new Blob([this.shadowRoot.getElementById("worker1").textContent]);
        var worker = new Worker(window.URL.createObjectURL(blob));

        worker.onmessage = (e) => {
            this.handleMessage(e);
        };



        // onClick auf Vorschlagen Button definieren
        this.suggestButton = this.shadowRoot.getElementById("vorschlagen-button");
        this.suggestButton.addEventListener("click", () => {
             var text = this.shadowRoot.getElementById("eingabe").value;
             worker.postMessage(text);
        });

        worker.postMessage(""); // Start the worker.
    }


// static get observedAttributes() {
//     return ['toggled'];
// }


// get toggled() {
//     return this.getAttribute('toggled') === 'true';
// }
//
// // the second argument for setAttribute is mandatory, so we’ll use an empty string
// set toggled(val) {
//     if (val) {
//         this.setAttribute('toggled', true);
//     } else {
//         this.setAttribute('toggled', false);
//     }
// }


}

export {SuggestionInput};

window.customElements.define("suggestion-input", SuggestionInput);



