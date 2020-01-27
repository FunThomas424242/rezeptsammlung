"use strict";

import {LoggerService} from "./consoleLoggerService.js";

const template = document.createElement("template");
template.innerHTML = `
    <style>

    </style>

    <div id="log"></div>
`;


class LoggerComponent extends HTMLElement {

    /* properties = rich data */

    onlog( message ){
         this.schreibeLogEintrag( message );
    }

    /* Getter and Setter of primitive data = attributes */

    get listeners() {
        return this.getAttribute("listeners");
    }

    set listeners( listeners) {
        this.setAttribute("listeners", listeners );
    }

    /* default lifecycle methods of customs elements */

    constructor() {
        super();  // immer zuerst aufrufen
        // for init attribut defaults
        // e.g. this.src = '';
        this.onConsolelog = (msg) => LoggerService.logMessage("logger-component: " + msg);
        this.onConsolelog("constructor called");
    }

    connectedCallback() {
        this.onConsolelog("In Seite eingehängt");
        this.initialisiereAttributwerte();
        this.erzeugeShadowDOMIfNotExists();
        this.establishLogger();
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
        return [];
        // return ["suggesterurl","suggesterparametername","onlog","onsubmit"];
    }

    /* methods of specific components logic */

    initialisiereAttributwerte(){
        // this.suggesterurl = this.getAttribute("suggesterurl");
    }

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

    establishLogger(){
        const componentId = "suggestion1";
        const onLogFunctionName = "onlog";
        document.getElementById(componentId)[onLogFunctionName] = (msg) => this.onlog(msg);
        this.onConsolelog("Logger für "+ componentId + " an "+ onLogFunctionName + " installiert.");
    }

}

export {LoggerComponent};

window.customElements.define("logger-component", LoggerComponent);



