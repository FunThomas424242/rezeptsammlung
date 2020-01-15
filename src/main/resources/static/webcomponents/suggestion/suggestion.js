"use strict";

import {Logger} from "./logger.js";

// script of inline service worker
import "./worker.js";

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

//    <script id="worker" type="text/javascript">
//        if ('serviceWorker' in navigator) {
//          window.addEventListener('load', function() {
//            navigator.serviceWorker.register('./worker.js').then(function(registration) {
//              // Registration was successful
//              console.log('ServiceWorker registration successful with scope: ', registration.scope);
//            }, function(err) {
//              // registration failed :(
//              console.log('ServiceWorker registration failed: ', err);
//            });
//          });
//    </script>

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
        this.shadowRoot.getElementById("vorschlaege").innerHTML=`${content}`;
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

    handleInput( srcValue, key ){
        var text = "";
         text +=  srcValue?  srcValue : "";
         text +=  key?  key : "";
        this.serviceWorker.postMessage(text);
    }

//    reqListener( callback ){
//      console.log( this.responseText );
//
//      // Worker erzeugen und starten
//      var blob = new Blob([this.responseText]);
//      var serviceWorkerBlobURL = window.URL.createObjectURL(blob);
//      var worker = new Worker(serviceWorkerBlobURL);
//      callback( worker );
//
//    }

    erzeugeShadowDOMIfNotExists() {
        if (!this.shadowRoot) {
            Logger.logMessage("creating shadow dom");
            this.attachShadow({mode: "open"});
        }
        this.shadowRoot.appendChild(template.content.cloneNode(true));


//        if ('serviceWorker' in navigator) {
//          window.addEventListener('load', function() {
//            navigator.serviceWorker.register('./worker.js').then(function(registration) {
//              // Registration was successful
//              console.log('ServiceWorker registration successful with scope: ', registration.scope);
//            }, function(err) {
//              // registration failed :(
//              console.log('ServiceWorker registration failed: ', err);
//            });
//          });
//        };


        var scriptURL = import.meta.url;
        var workerURL = scriptURL.replace("suggestion.js", "worker.js");
        var oReq = new XMLHttpRequest();
        oReq.addEventListener("load", (event) =>{

            console.log( event.target.responseText );

            // Worker erzeugen und starten
            var blob = new Blob([event.target.responseText]);
            var serviceWorkerBlobURL = window.URL.createObjectURL(blob);
            var worker = new Worker(serviceWorkerBlobURL);
            this.serviceWorker = worker;
            // onMessage definieren
            this.serviceWorker.onmessage = (e) => {
              this.handleMessage(e);
            };
            // service worker starten
            this.serviceWorker.postMessage("");
        });
        oReq.open("GET", workerURL);
        oReq.send();




        this.filterPattern = this.shadowRoot.getElementById("eingabe");
// Bei erkanntem Bedarf nutzen
//        this.filterPattern.onkeypress = (event) => {
//            this.handleInput( worker, event.srcElement.value, event.key );
//        }
//        this.filterPattern.onKeyUp = (event) => {
//            this.handleInput( worker, event.srcElement.value, event.key );
//        }
//        this.filterPattern.onchange = (event) => {
//            this.handleInput( worker, event.srcElement.value, event.key );
//        }
//        this.filterPattern.onpaste = (event) => {
//            this.handleInput( worker, event.srcElement.value, event.key );
//        }
        this.filterPattern.oninput = (event) => {
            this.handleInput( event.srcElement.value, event.key );
        }

        // onClick auf Vorschlagen Button definieren
        this.suggestButton = this.shadowRoot.getElementById("vorschlagen-button");
        this.suggestButton.addEventListener("click", () => {
             var text = this.shadowRoot.getElementById("eingabe").value;
             this.serviceWorker.postMessage(text);
        });
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



