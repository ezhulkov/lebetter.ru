var ZoneUpdater = Class.create();
ZoneUpdater.prototype = {
	initialize: function(zoneElementId, listeningElement, event, link, zone, placeholder) {
		this.zoneElement = $(zoneElementId);
		this.event = event;
		this.link = link;
		this.placeholder = placeholder;
		this.listeningElement = listeningElement;
		$T(this.zoneElement).zoneId = zone;
		listeningElement.observe(this.event, this.updateZone.bindAsEventListener(this));
	},
	updateZone: function(event) {

        if(jQuery(this.listeningElement).attr("inside")==null){         //added for BigListInfoBox Ajax support
            jQuery("div.any_item").removeAttr("inside");                //added for BigListInfoBox Ajax support
            jQuery(this.listeningElement).attr("inside","true");        //added for BigListInfoBox Ajax support
            var zoneObject = Tapestry.findZoneManager(this.zoneElement);
            if ( !zoneObject ) return;
            var param;
            if (this.zoneElement.value) {
                param = this.zoneElement.value;
            }
            if (!param) param = ' ';
            param = this.encodeForUrl(param);
            var updatedLink = this.link.gsub(this.placeholder, param);
            zoneObject.updateFromURL(updatedLink);
        }                                                               //added for BigListInfoBox Ajax support
	    
    },
	encodeForUrl: function(string) {
		/**
		 * See equanda.js for updated version of this
		 */
		string = string.replace(/\r\n/g,"\n");
	    var res = "";
	    for (var n = 0; n < string.length; n++)
	    {
	        var c = string.charCodeAt( n );
	        if ( '$' == string.charAt( n ) )
	        {
	            res += '$$';
	        }
	        else if ( this.inRange( c, "AZ" ) || this.inRange( c, "az" ) || this.inRange( c, "09" ) || this.inRange( c, ".." ) )
	        {
	            res += string.charAt( n )
	        }
	        else
	        {
	            var tmp = c.toString(16);
	            while ( tmp.length < 4 ) tmp = "0" + tmp;
	            res += '$' + tmp;
	        }
	    }
	    return res;
	},
	inRange: function(code, range) {
		return code >= range.charCodeAt( 0 ) &&  code <= range.charCodeAt( 1 );
	}
}