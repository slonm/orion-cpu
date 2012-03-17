var crudPopupWindow;

Event.observe(window, 'load',function(){

    $$('a.crud-popup-edit').each(function(element){
        //element.writeAttribute({href:'javascript:void(0)'});
        Event.observe(element, 'click',crudLinkClicked);
    });
    $$('a.crud-popup-view').each(function(element){
        //element.writeAttribute({href:'javascript:void(0)'});
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
            crudPopupWindow.setZIndex(6);
            WindowCloseKey.init();
            
            // скрываются меню.
            firstClick=false;
            $$("span.menuitems").each(Element.hide);
            menuIsOpen=false; 

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
    crudPopupWindow.setZIndex(6);
    WindowCloseKey.init();
    
    // скрываются меню.
    firstClick=false;
    $$("span.menuitems").each(Element.hide);
    menuIsOpen=false; 
    
    Event.stop(event);
}

function onEditWindowClosed(msg){
    // alert(msg);
    if(crudPopupWindow) {
        crudPopupWindow.hide();
    }
    setTimeout(window.location.reload(),3000);
}