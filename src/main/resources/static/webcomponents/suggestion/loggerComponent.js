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
        this.shadowRoot.getElementById("log").appendChild(fragment);
    }

    erzeugeShadowDOMIfNotExists() {
        if (!this.shadowRoot) {
            this.onConsolelog("creating shadow dom");
            this.attachShadow({mode: "open"});
        }
        this.shadowRoot.appendChild(template.content.cloneNode(true));
        this.loggerDiv = this.shadowRoot.getElementById("log");
    }


    constructor() {
        super();  // immer zuerst aufrufen
        // for init attribut defaults
        // e.g. this.src = '';
        this.onConsolelog = (msg) => LoggerService.logMessage(msg);
        this.onlog = (msg) => this.schreibeLogEintrag(msg);
        this.onConsolelog("constructor called");
    }

    initialisiereAttributwerte(){
        // this.suggesterurl = this.getAttribute("suggesterurl");
    }

    static get observedAttributes() {
        return [];
        // return ["suggesterurl","suggesterparametername","onlog","onsubmit"];
    }

    connectedCallback() {
        this.onConsolelog("custom element in Seite eingehängt");
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


}

export {LoggerComponent};

window.customElements.define("logger-component", LoggerComponent);



