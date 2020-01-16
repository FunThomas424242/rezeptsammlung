"use strict";

self.onmessage = function(e) {
  self.postMessage("msg from worker "+e.data);
  var jsonPromise;
  if( e.data ){
    jsonPromise = self.fetch("http:localhost:8080/rezeptsammlung/api/rezepte/tags?taglist="+e.data);
  }else{
    jsonPromise = self.fetch("http:localhost:8080/rezeptsammlung/api/rezepte/tags");
  }
  jsonPromise.then( (response)=> {
    var promise = response.json();
    promise.then( (jsonObject) => {
        var selectionContent = createSelectionContent(jsonObject);
        self.postMessage({"cmd":"replace-taglist", "data": selectionContent});
        var text = "Response:" + JSON.stringify( jsonObject );
        self.postMessage({"cmd":"log","msg": text});
    });
  });
};

function createSelectionContent( json ){
    var content = "";
    for (const key of Object.keys(json)) {
        content += "<option value=\""+key+"\">";
    }
   return content;
}
