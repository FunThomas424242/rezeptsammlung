"use strict";

import {LoggerService} from "./loggerService.js";

// script of inline service worker
import {WorkerService} from "./workerService.js";

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

`;


class SuggestionInput extends HTMLElement {

    constructor() {
        super();  // immer zuerst aufrufen
        // for init attribut defaults
        // e.g. this.src = '';
        LoggerService.logMessage("constructor called");

    }

    connectedCallback() {
        LoggerService.logMessage("custom element in Seite eingehängt");
        this.erzeugeShadowDOMIfNotExists();
        LoggerService.logMessage("ShadowDom befüllt");
    }

    disconnectedCallback() {
        LoggerService.logMessage("element has been removed");
    }

    attributeChangedCallback(name, oldval, newval) {
        // do something every time the attribute changes
        LoggerService.logMessage(`the ${name} attribute has changed from ${oldval} to ${newval}!!`);
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



    handleInput( srcValue, key ){
        var text = "";
        text +=  srcValue?  srcValue : "";
        text +=  key?  key : "";
        this.workerService.sendToWorker(text);
    }

    erzeugeWebWorker(){
        var scriptURL = import.meta.url;
        var workerURL = scriptURL.replace("suggestion.js", "worker.js");
        this.workerService = new WorkerService(workerURL, (event) => {
            var msgObject = event.data;
            if( msgObject.cmd === "log"){
                this.schreibeLogEintrag(msgObject.msg);
            }else if( msgObject.cmd === "replace-taglist"){
                this.ersetzeVorschlagslisteMit(msgObject.data);
            }else{
                this.schreibeLogEintrag(msgObject);
            }
        });
    }


    erzeugeShadowDOMIfNotExists() {
        if (!this.shadowRoot) {
            LoggerService.logMessage("creating shadow dom");
            this.attachShadow({mode: "open"});
        }
        this.shadowRoot.appendChild(template.content.cloneNode(true));

        this.erzeugeWebWorker();

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
             this.workerService.sendToWorker(text);
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



