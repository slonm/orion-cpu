// ======================= load/save grid settings = begin =====================
function draw_load_setting(data){
     var options="<option value=\"0\"></option>";
     data.each(function(el){options+="<option value=\""+el.id+"\">"+el.label+"</option>";});
     var block=$('block-saved-properties');
     var selector=new Element('select',{'id':"block-saved-properties-selector",'name':"block-saved-properties-selector"});
     selector.update(options);
     $('block-saved-properties').insert(selector);
}
function load_setting(data){
   //alert("Loading " + data);
}

function update_settings(){
        var newGridPropertiesValue={"sortJSON":$("sortJSON").value,"widthJSON":$("widthJSON").value,"viewJSON":$("viewJSON").value,"filterJSON":$("filterJSON").value};
        $('newGridPropertiesValue').value=Object.toJSON(newGridPropertiesValue);
}
Event.observe(window, 'load',function(){
    update_settings();
    Event.observe($('sortJSON'),'change',update_settings);
    Event.observe($('widthJSON'),'change',update_settings);
    Event.observe($('viewJSON'),'change',update_settings);
    Event.observe($('filterJSON'),'change',update_settings);
});


// ======================= load/save grid settings = end =======================