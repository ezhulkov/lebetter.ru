var OnEvent = Class.create({
    initialize: function(elementId, event, url, callback)
    {
        if (!$(elementId))
            throw(elementId + " doesn't exist!");
        this.element = $(elementId);
        this.callback = callback;
        this.url = url;
        this.element.observe(event, this.eventHandler.bindAsEventListener(this));
    },
    eventHandler: function(event)
    {
        new Ajax.Request(this.url, {
            method: 'get',
			onFailure: function(t)
            {
                alert('Error communication with the server: ' + t.responseText.stripTags());
            },
            onException: function(t, exception)
            {
                alert('Error communication with the server: ' + exception.message);
            },
            onSuccess: function(t)
            {
                if (this.callback)
				{
					var funcToEval = this.callback + "(" + t.responseText + ")";
					eval(funcToEval);
				}
			}.bind(this)
        });
    }
});