Tapestry.Initializer.initSpoilerLink = function(opt){
    jQuery("#"+opt).click(function(){
        var spoiler=jQuery(this).parent()
        if (spoiler.hasClass("ori_spoiler-visible")){
            spoiler.removeClass("ori_spoiler-visible").find(".ori_spoiler-body").hide("slow");
        } else{
            spoiler.addClass("ori_spoiler-visible").find(".ori_spoiler-body").show("slow");
        }
    })    
}