var scenarios = [
	{
		name: "Tutorial",
		url: "scenarios/tutorial/",
		creator: "Alx Dark",
		description: "A short tutorial that demonstrates everything you need to know in order to play."
	},{
        name:"The Chalice of Goldcastle",
		url: "scenarios/goldcastle/",
        creator:"anna anthropy", 
        description:"It's the stuff of legends. But now, your pilgrimage complete, you may get the chance to touch with your own hands the holy relic itself: the Chalice of Goldcastle!"
    },{
		name: "The Tombs of Asciiroth",
		url: "scenarios/toa/",
		creator: "Alx Dark",
		description: "Escape from the isles of Asciiroth with some proof of your archaeological prowess. A larger and more involved scenario, to be sure."
	}
];

// Copyright 2007-2008 Adobe Systems Incorporated.
// See about dialog for full information 

var air;
if (window.runtime) 
{
    if (!air) air = {};
    // functions
    air.trace = window.runtime.trace;
    air.navigateToURL = window.runtime.flash.net.navigateToURL;
    air.sendToURL = window.runtime.flash.net.sendToURL;
    air.File = window.runtime.flash.filesystem.File;
    air.FileStream = window.runtime.flash.filesystem.FileStream;
    air.FileMode = window.runtime.flash.filesystem.FileMode;
    air.Event = window.runtime.flash.events.Event;
    air.FileFilter = window.runtime.flash.net.FileFilter;

    // data
    air.EncryptedLocalStore = window.runtime.flash.data.EncryptedLocalStore;
    air.SQLCollationType = window.runtime.flash.data.SQLCollationType;
    air.SQLColumnNameStyle = window.runtime.flash.data.SQLColumnNameStyle;
    air.SQLColumnSchema = window.runtime.flash.data.SQLColumnSchema;
    air.SQLConnection = window.runtime.flash.data.SQLConnection;
    air.SQLError = window.runtime.flash.errors.SQLError;
    air.SQLErrorEvent = window.runtime.flash.events.SQLErrorEvent;
    air.SQLErrorOperation = window.runtime.flash.errors.SQLErrorOperation;
    air.SQLEvent = window.runtime.flash.events.SQLEvent;
    air.SQLIndexSchema = window.runtime.flash.data.SQLIndexSchema;
    air.SQLMode = window.runtime.flash.data.SQLMode;
    air.SQLResult = window.runtime.flash.data.SQLResult;
    air.SQLSchema = window.runtime.flash.data.SQLSchema;
    air.SQLSchemaResult = window.runtime.flash.data.SQLSchemaResult;
    air.SQLStatement = window.runtime.flash.data.SQLStatement;
    air.SQLTableSchema = window.runtime.flash.data.SQLTableSchema;
    air.SQLTransactionLockType = window.runtime.flash.data.SQLTransactionLockType;
    air.SQLTriggerSchema = window.runtime.flash.data.SQLTriggerSchema;
    air.SQLUpdateEvent = window.runtime.flash.events.SQLUpdateEvent;
    air.SQLViewSchema = window.runtime.flash.data.SQLViewSchema;

    // service monitoring framework
    air.__defineGetter__("ServiceMonitor", function() { return window.runtime.air.net.ServiceMonitor; })
    air.__defineGetter__("SocketMonitor", function() { return window.runtime.air.net.SocketMonitor; })
    air.__defineGetter__("URLMonitor", function() { return window.runtime.air.net.URLMonitor; })
}

// Copyright 2007, Google Inc.
// See about dialog for full information
(function() {
  if (window.google && google.gears) {
    return;
  }
  var factory = null;
  if (typeof GearsFactory != 'undefined') {
    factory = new GearsFactory();
  } else {
    try {
      factory = new ActiveXObject('Gears.Factory');
      if (factory.getBuildInfo().indexOf('ie_mobile') != -1) {
        factory.privateSetGlobalObject(this);
      }
    } catch (e) {
      if ((typeof navigator.mimeTypes != 'undefined')
           && navigator.mimeTypes["application/x-googlegears"]) {
        factory = document.createElement("object");
        factory.style.display = "none";
        factory.width = 0;
        factory.height = 0;
        factory.type = "application/x-googlegears";
        document.documentElement.appendChild(factory);
      }
    }
  }
  if (!factory) {
    return;
  }
  if (!window.google) {
    google = {};
  }
  if (!google.gears) {
    google.gears = {factory: factory};
  }
})();
