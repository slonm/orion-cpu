Tapestry.Initializer.oriTabControl = function(spec){
    var tabs = $J("#"+spec.id).find("li");
    var links = $J("#"+spec.id).find("a");
    //Обработчик нажатия на вкладку
    links.click(function(){
        tabs.removeClass("ui-state-active");
        tabs.removeClass("ui-tabs-selected");
        $J(this).parent().addClass("ui-state-active");
        $J(this).parent().addClass("ui-tabs-selected");
    })
    //Обработчик наведения мыши на вкладку
    tabs.hover(
        function () {
            $J(this).addClass("ui-state-hover");
        }, 
        function () {
            $J(this).removeClass("ui-state-hover");
        });
        
}