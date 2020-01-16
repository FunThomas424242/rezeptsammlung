"use strict";

import {LoggerService} from "./loggerService.js";

// script of inline service worker
import "./webworkerScript.js";


class WorkerService {

    constructor( workerURL, handleMessageCallback) {
        var oReq = new XMLHttpRequest();
        oReq.addEventListener("load", (event) => {
            LoggerService.logMessage( event.target.responseText );
            // Worker erzeugen und starten
            var blob = new Blob([event.target.responseText]);
            var serviceWorkerBlobURL = window.URL.createObjectURL(blob);
            var worker = new Worker(serviceWorkerBlobURL);
            this.serviceWorker = worker;
            // onMessage definieren
            this.serviceWorker.onmessage = (e) => {
              handleMessageCallback(e);
            };
            // service worker startencon
            this.sendToWorker("");
        });
        oReq.open("GET", workerURL);
        oReq.send();
    }



    sendToWorker( message ){
        this.serviceWorker.postMessage(message);
    }

}

export {WorkerService};
