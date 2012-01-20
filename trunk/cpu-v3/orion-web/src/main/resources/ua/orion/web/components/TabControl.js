/**
 * Declare our own name space
 */
if (window.Ori == undefined) Ori = {};
Ori.TabControl = Class.create();
Ori.TabControl.prototype = {

	initialize: function(tabPanelId, activeId)
	{
		this.tabPanelId = tabPanelId;
		this.activeId = activeId;
		this.panelLinks = $(this.tabPanelId).select('a');

		this.removeActiveStyle();

		this.panelLinks.each(function (e)
		{
			Event.observe(e, 'click', this.__clicked.bind(this))

			if (e.up().id == activeId)
				this.addActiveStyle(e.up());

		}.bind(this));
	},
	removeActiveStyle: function()
	{
		this.panelLinks.each(function (element)
		{
			element.up().removeClassName('active');
		})
	},
	addActiveStyle: function(element)
	{
		element.addClassName('active');
	},
	__clicked:function(event)
	{
		this.removeActiveStyle();

		var clickedLink = Event.element(event)
		clickedLink.up().addClassName('active');
	}
}