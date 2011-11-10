firstClick=false;
menuIsOpen=false;
Event.observe(window, 'load', initApp, false);
var allmenus;
var menuopeners;

// ------------------------ init menu application - begin ----------------------
function initApp()
{
    // get all menus
    allmenus=$$('span.menuitems');

    // get all menu openers
    menuopeners=$$('span.menuarrow');

    // set event handlers
    menuopeners.each(
        function(e){
            e.observe('click',
                function(event){
                    // alert(Event.element(event).innerHTML);
                    // hide all other menus
                    allmenus.each(hm);

                    // show current menu
                    var a=Event.element(event);
                    var b=a.ancestors();
                    var c=b[0].adjacent('span.menuitems');
                    c.each(sm);

                    firstClick=true;
                    menuIsOpen=true;
                }
                );
            e.observe('mouseover',
                function (event){
                    if(!menuIsOpen) return true;
                    // hide other menus
                       allmenus.each(hm);

                    // show current menu
                    var a=Event.element(event);
                    var b=a.ancestors();
                    var c=b[0].adjacent('span.menuitems');
                    c.each(sm);

                    return true;
                }
            );

        }
        );


    Event.observe(document, 'click', function(){
        if(firstClick) firstClick=false;
        else
        {
            allmenus.each(hm);
            menuIsOpen=false;
        }
    });

}
function showMenus(e){
  //e.adjacent('span.menuitems').each(
    e.getOffsetParent().getElementsByClassName('menuitems').each(
    function(e){
        e.setStyle({'display': 'inline-block'});
    });
}
function hideMenus(e){
  //e.adjacent('span.menuitems').each(
    e.getOffsetParent().getElementsByClassName('menuitems').each(
    function(e){
        e.setStyle({'display': 'none'});
    });
}

function hm(e){
        e.setStyle({'display': 'none'});
}
function sm(e){
        e.setStyle({'display': 'inline-block'});
}
// ------------------------ init menu application - end ------------------------
