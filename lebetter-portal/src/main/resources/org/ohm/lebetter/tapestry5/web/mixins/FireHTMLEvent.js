var ZoneUpdater = Class.create();
ZoneUpdater.prototype = {
	initialize: function(zoneElementId, listeningElement, event, link, zone, selector) {
		this.zoneElement = $(zoneElementId);
		this.event = event;
		this.link = link;
		this.listeningElement = listeningElement;
		this.selector = selector;
		$T(this.zoneElement).zoneId = zone;
		listeningElement.observe(this.event, this.updateZone.bindAsEventListener(this));
	},
	updateZone: function(event) {
        var zoneObject = Tapestry.findZoneManager(this.zoneElement);
        if ( !zoneObject ) return;
        var val=jQuery(this.selector).attr("value");
        if(val==''){
            val='$N';
        }
        var link=this.link.replace("/SELECTOR","/"+KS.encodeURIComponentForTapestry(val));
        zoneObject.updateFromURL(link);
    }

}