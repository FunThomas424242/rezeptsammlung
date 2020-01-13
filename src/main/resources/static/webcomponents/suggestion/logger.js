"use strict";

class Logger {

    static logMessage(message) {
        // eslint-disable-next-line no-console
        console.log(message);
    }

    static debugMessage(message) {
        // eslint-disable-next-line no-console
        console.debug(message);
    }

    static errorMessage(message) {
        // eslint-disable-next-line no-console
        console.error(message);
    }

    static infoMessage(message) {
        // eslint-disable-next-line no-console
        console.info(message);
    }

}

export {Logger};
