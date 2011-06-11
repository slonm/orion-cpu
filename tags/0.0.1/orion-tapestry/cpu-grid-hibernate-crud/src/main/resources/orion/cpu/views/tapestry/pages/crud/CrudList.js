Event.observe(window, 'load',function(){
    $$('a.grid-page-link-edit').each(function(element){
        Event.observe(element, 'click',editCrudLinkClicked);
    });
});

var crudEditWindow;
function editCrudLinkClicked(event){
    if(crudEditWindow) crudEditWindow.destroy();
    crudEditWindow = new Window({className: "spread", title: "Editing record",
                          top:70, left:100, width:500, height:300,
                          url: $(this).readAttribute('href'), showEffectOptions: {duration:1.5}})

    crudEditWindow.setCloseCallback(function(){window.location.reload();});
    crudEditWindow.show();
    WindowCloseKey.init();
    Event.stop(event);
}


