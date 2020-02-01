"use strict";

import {LoggerService} from "./consoleLoggerService.js";

// script of inline web worker
import "./webworkerScript.js";


class WorkerService {

    constructor( workerURL, handleMessageCallback, onlogCallback) {
        var oReq = new XMLHttpRequest();
        oReq.addEventListener("load", (event) => {
            onlogCallback( event.target.responseText );
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
