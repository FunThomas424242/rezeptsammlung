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
        if( ! this.disabled ){
            this.schreibeLogEintrag( message );
        }
    }

    /* Getter and Setter of primitive data = attributes */

    get listeners() {
        return this.getAttribute("listeners");
    }

    set listeners( listeners) {
        this.setAttribute("listeners", listeners );
    }

    get disabled() {
        return this.hasAttribute('disabled');
    }

    set disabled( disabledValue) {
        const isDisabled = Boolean(disabledValue);
        if (isDisabled){
            this.setAttribute('disabled', '');
        } else {
            this.removeAttribute('disabled');
        }
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
        return ["listeners", "disabled"];
    }

    /* methods of specific components logic */

    initialisiereAttributwerte(){
         this.listeners = this.getAttribute("listeners");
         this.disabled = this.getAttribute("disabled");
    }

    onConsolelog ( message ){
        LoggerService.logMessage("logger-component: " + message);
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
        const listeners = this.getAttribute("listeners");
        const items = listeners.split(',');
        items.forEach( item =>{
            const listener = item.split(':');
            document.getElementById(listener[0])[listener[1]] = (msg) => this.onlog(msg);
            this.onConsolelog("Logger für "+ listener[0] + " an "+ listener[1] + " installiert.");
        } );
    }

}

export {LoggerComponent};

window.customElements.define("logger-component", LoggerComponent);



