"use strict";

import {LoggerService} from "./loggerService.js";

const template = document.createElement("template");
template.innerHTML = `
    <style>

    </style>

    <div id="log"></div>
`;


class LoggerComponent extends HTMLElement {

    schreibeLogEintrag( text ){
        // Use a fragment: browser will only render/reflow once.
        var fragment = document.createDocumentFragment();
        fragment.appendChild(document.createTextNode(text));
        fragment.appendChild(document.createElement("br"));
        document.querySelector("#log").appendChild(fragment);
    }

    erzeugeShadowDOMIfNotExists() {
        if (!this.shadowRoot) {
            this.onlog("creating shadow dom");
            this.attachShadow({mode: "open"});
        }
        this.shadowRoot.appendChild(template.content.cloneNode(true));

        this.loggerDiv = this.shadowRoot.getElementById("log");

    }


    constructor() {
        super();  // immer zuerst aufrufen
        // for init attribut defaults
        // e.g. this.src = '';
        this.onlog = (msg) => LoggerService.logMessage(msg);
        this.onlog("constructor called");

    }

    initialisiereAttributwerte(){
//        this.suggesterurl = this.getAttribute("suggesterurl");
//        this.suggesterparametername = this.getAttribute("suggesterparametername");
////        this.onlog = this.getAttribute("onlog");
//        this.onsubmit = this.getAttribute("onsubmit");
    }

    static get observedAttributes() {
     return [];
//     return ["suggesterurl","suggesterparametername","onlog","onsubmit"];
    }

//    get suggesterurl() {
//        return this.getAttribute("suggesterurl");
//    }
//
//    set suggesterurl( restapiurl) {
//        this.setAttribute("suggesterurl", restapiurl );
//    }
//
//    get suggesterparametername() {
//        return this.getAttribute("suggesterparametername");
//    }
//
//    set suggesterparametername( restapiparametername) {
//        this.setAttribute("suggesterparametername", restapiparametername );
//    }



    connectedCallback() {
        this.onlog("custom element in Seite eingehängt");
        this.initialisiereAttributwerte();
        this.erzeugeShadowDOMIfNotExists();
        this.onlog("ShadowDOM befüllt");
    }

    disconnectedCallback() {
        this.onlog("element has been removed");
    }

    attributeChangedCallback(name, oldval, newval) {
        // do something every time the attribute changes
        this.onlog(`the ${name} attribute has changed from ${oldval} to ${newval}!!`);
    }


}

export {LoggerComponent};

window.customElements.define("logger-component", LoggerComponent);



