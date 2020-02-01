"use strict";

self.onmessage = function(e) {
  self.postMessage("msg from worker "+JSON.stringify(e.data));
  var jsonPromise;
  if( e.data.msg ){
    jsonPromise = self.fetch(e.data.apiurl+"?"+e.data.apiparameter+"="+e.data.msg);
  }else if( e.data.apiurl ) {
    jsonPromise = self.fetch(e.data.apiurl);
  }else{
    jsonPromise = null;
    // empty start message received
  };
  if(jsonPromise){
    jsonPromise.then( (response)=> {
      var promise = response.json();
      promise.then( (jsonObject) => {
          var selectionContent = createSelectionContent(jsonObject);
          self.postMessage({"cmd":"replace-taglist", "data": selectionContent});
          var text = "Response:" + JSON.stringify( jsonObject );
          self.postMessage({"cmd":"log","msg": text});
      });
    });
  }
};

function createSelectionContent( json ){
    var content = "";
    for (const key of Object.keys(json)) {
        content += "<option value=\""+key+"\">";
    }
   return content;
}
