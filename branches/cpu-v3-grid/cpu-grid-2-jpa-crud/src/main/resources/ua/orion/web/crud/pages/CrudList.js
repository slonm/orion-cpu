var crudPopupWindow;

Event.observe(window, 'load',function(){

    $$('a.crud-page-link-edit').each(function(element){
        Event.observe(element, 'click',crudLinkClicked);
    });
    $$('a.crud-page-link-delete').each(function(element){
        Event.observe(element, 'click',crudLinkClicked);
    });
    $$('a.crud-page-link-view').each(function(element){
        
        Event.observe(element, 'click',function (event){
            var title=$(this).readAttribute('title');
            var url=$(this).readAttribute('href')
    
            if(crudPopupWindow) crudPopupWindow.destroy();
            crudPopupWindow = new Window({
                'className': "spread", 
                'title': title,
                'top':70, 
                'left':100, 
                'width':500, 
                'height':300,
                'url': url, 
                showEffectOptions: {
                    duration:1.5
                }
            })
            crudPopupWindow.show();
            WindowCloseKey.init();
            Event.stop(event);
        });
    });
    
    var template=$('menu').childElements().last();
    var cloned=template.clone();
    $('menu').insert(cloned);
    cloned.insert(template.innerHTML);
    var link=cloned.select('a').first();
    link.replace($('crud_page_link_add'));
    Event.observe($('crud_page_link_add'), 'click',crudLinkClicked);
});


function crudLinkClicked(event){
    var title=$(this).readAttribute('title');
    var url=$(this).readAttribute('href')
    
    if(crudPopupWindow) crudPopupWindow.destroy();
    crudPopupWindow = new Window({
        'className': "spread", 
        'title': title,
        'top':70, 
        'left':100, 
        'width':500, 
        'height':300,
        'url': url, 
        showEffectOptions: {
            duration:1.5
        }
    })

    crudPopupWindow.setCloseCallback(function(){
        window.location.reload();
    });
    crudPopupWindow.show();
    WindowCloseKey.init();
    Event.stop(event);
}

function onEditWindowClosed(msg){
    // alert(msg);
    if(crudPopupWindow) {
        crudPopupWindow.hide();
    }
    setTimeout(window.location.reload(),3000);
}