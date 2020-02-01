"use strict";

import {LoggerService} from "./consoleLoggerService.js";

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

    /* properties = rich data */

    onlog ( message ){
        LoggerService.logMessage("suggestion-input: " + message);
    }

    onConsolelog ( message ){
        LoggerService.logMessage("suggestion-input: " + message);
    }

    /* Getter and Setter of primitive data = attributes */

    get suggesterurl() {
        return this.getAttribute("suggesterurl");
    }

    set suggesterurl( restapiurl) {
        this.setAttribute("suggesterurl", restapiurl );
    }

    get suggesterparametername() {
        return this.getAttribute("suggesterparametername");
    }

    set suggesterparametername( restapiparametername) {
        this.setAttribute("suggesterparametername", restapiparametername );
    }

    /* default lifecycle methods of customs elements */

    constructor() {
        super();  // immer zuerst aufrufen
        // for init attribut defaults
        // e.g. this.src = '';
        this.onConsolelog("constructor called");
    }

    connectedCallback() {
        this.onConsolelog("In Seite eingehängt");
        this.initialisiereAttributwerte();
        this.erzeugeShadowDOMIfNotExists();
        this.onConsolelog("ShadowDOM befüllt");
    }

    disconnectedCallback() {
        this.onConsolelog("element has been removed");
    }

    attributeChangedCallback(name, oldval, newval) {
        // do something every time the attribute changes
        this.onConsolelog(`the ${name} attribute has changed from ${oldval} to ${newval}!!`);
    }

    static get observedAttributes() {
     // Do not reflect rich data (properties)  to attributes in most cases.
     // https://developers.google.com/web/fundamentals/web-components/best-practices
     // Only refect primitive data to attributes
     return ["suggesterurl","suggesterparametername","onsubmit"];
    }


    /* methods of specific components logic */

    initialisiereAttributwerte(){
        this.suggesterurl = this.getAttribute("suggesterurl");
        this.suggesterparametername = this.getAttribute("suggesterparametername");
        this.onsubmit = this.getAttribute("onsubmit");
    }

    ersetzeVorschlagslisteMit ( content ){
        this.shadowRoot.getElementById("vorschlaege").innerHTML=`${content}`;
    }

    handleInput( srcValue, key ){
        var text = "";
        text +=  srcValue?  srcValue : "";
        text +=  key?  key : "";

        var apiurl = this.suggesterurl;
        var apiparameter = this.suggesterparametername;
        this.workerService.sendToWorker({"apiurl": apiurl, "apiparameter" : apiparameter, "msg": text});
    }

    erzeugeWebWorker(){
        var scriptURL = import.meta.url;
        var workerURL = scriptURL.replace("SuggestionModule.js", "webworkerScript.js");
        this.workerService = new WorkerService(workerURL, (event) => {
            var msgObject = event.data;
            if( msgObject.cmd === "log"){
                this.onlog(msgObject.msg);
            }else if( msgObject.cmd === "conlog"){
                this.onConsolelog(msgObject.msg)
            }else if( msgObject.cmd === "replace-taglist"){
                this.ersetzeVorschlagslisteMit(msgObject.data);
            }else{
                this.onConsolelog(msgObject);
            }
        }, (msg) => { this.onConsolelog(msg); });
    }

    erzeugeShadowDOMIfNotExists() {
        if (!this.shadowRoot) {
            this.onlog("creating shadow dom");
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
             var apiurl = this.suggesterurl;
             var apiparameter = this.suggesterparametername;
             if (text && text !== "") {
                this.workerService.sendToWorker({"apiurl": apiurl, "apiparameter" : apiparameter, "msg": text});
             } else {
                this.workerService.sendToWorker({"apiurl": apiurl});
             }
        });
    }


}

export {SuggestionInput};

window.customElements.define("suggestion-input", SuggestionInput);



