/*
 * Links a FormFragment to a trigger (a select), such that
 * changing the trigger will hide or show the FormFragment. Care should be
 * taken to render the page with the select and the FormFragment's
 * visibility in agreement.
 */
Tapestry.Initializer.linkSelectTriggerToFormFragment = function(spec) {
    var trigger = $(spec.triggerId);

    var update = function() {
        var checked = trigger.checked;
        var makeVisible = checked == !spec.invert;

        $(spec.fragmentId).fire(Tapestry.CHANGE_VISIBILITY_EVENT, {
            visible : makeVisible
        }, true);
    }

    
    /* Let the event bubble up to the form level. */
    /*if (trigger.type == "radio") {
        $(trigger.form).observe("click", update);
        return;
    }*/

    /* Normal trigger is a select; listen just to it. */
    trigger.observe("change", update);

};
