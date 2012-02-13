
// ------------- show/hide grid properties block - begin -----------------------

// show properties editor
function show_grid_properties_editor(){
    $('grid_properties_editor').setStyle({
        display:'inline-block'
    });
}

// show properties editor
function hide_grid_properties_editor(){
    $('grid_properties_editor').setStyle({
        display:'none'
    });
}
Event.observe(window, 'load',hide_grid_properties_editor);

// ------------- show/hide grid properties block - end -------------------------

// open selected page, javascript paginig links
function goToPage(pgnum){
    $('currentPage').value=pgnum;
    $('grid_properties_form').submit();
}


// show button if the grid properties should be saved
function grid_changed(){
    $('savechanges').show();
}
document.observe("sortmodel:changed", grid_changed);
document.observe("viewmodel:changed", grid_changed);
document.observe("filtermodel:changed", grid_changed);


