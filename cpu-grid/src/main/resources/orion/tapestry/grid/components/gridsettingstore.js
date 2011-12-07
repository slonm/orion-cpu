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
    if(data){
       //alert("Loading " + data);
       //console.log(data);
       //console.log(data.sortJSON);
       for(var i in data){
           $(i).value=data[i];
       }
       $('grid_properties_form').submit();
    }
}

function update_settings(){
        var newGridPropertiesValue={
            "gridFilterJSON":$("gridFilterJSON").value,
            "gridPropertyViewJSON":$("gridPropertyViewJSON").value,
            "gridSortJSON":$("gridSortJSON").value,
            "nRowsPerPage":$("nRowsPerPage").value};
        $('newGridPropertiesValue').value=Object.toJSON(newGridPropertiesValue);
}
Event.observe(window, 'load',function(){
    update_settings();
    Event.observe($('gridFilterJSON'),'change',update_settings);
    Event.observe($('gridPropertyViewJSON'),'change',update_settings);
    Event.observe($('gridSortJSON'),'change',update_settings);
});


// ======================= load/save grid settings = end =======================