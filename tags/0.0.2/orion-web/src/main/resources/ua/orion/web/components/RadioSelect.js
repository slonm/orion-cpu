/**
 * Converts a link into an Ajax update of a Zone. The url includes the
 * information to reconnect with the server-side Form.
 * 
 * @param spec.selectId
 *            id or instance of <select>
 * @param spec.zoneId
 *            id of element to update when select is changed
 * @param spec.url
 *            component event request URL
 */
Tapestry.Initializer.linkRadioSelectToZone = function(spec) {
    var element = $(spec.selectId);
    if(element == null) return;
    Tapestry.Initializer.updateZoneOnEvent("change", spec.selectId,
        spec.zoneId, spec.url);
    element.stopObserving(Tapestry.TRIGGER_ZONE_UPDATE_EVENT);
    element.observe(Tapestry.TRIGGER_ZONE_UPDATE_EVENT, function() {

        var zoneObject = Tapestry.findZoneManager(element);

        if (!zoneObject)
            return;

            /*
             * A hack related to allowing a RadioSelect to perform an Ajax update of
             * the page.
             */

        var parameters = {};

        if (element.value) {
            parameters["t:radioselectvalue"] = element.value;
        }

        zoneObject.updateFromURL(spec.url, parameters);
    });
}