$J(document).ready(function(){
    //Установка начального значения выбранной темы в select
    function putCookieThemeToSelect(){
        var theme=$J.cookie("ui-theme")?$J.cookie("ui-theme"):"smoothness";
        $J("#ui-select-theme option").each(function(){
            if ($J(this).html()==theme){
                $J(this).attr("selected", "selected");
            }
        })
    }

    //Применение темы
    function applyCookieTheme(){
        if ($J.cookie("ui-theme")!=null){
            var what = "css/.*/jquery-ui-1.8.16.custom.css";
            var to = "css/"+$J.cookie("ui-theme")+"/jquery-ui-1.8.16.custom.css";
            $J("head link").each(function(){
                if ($J(this).attr("href").indexOf("jquery-ui-1.8.16.custom.css")>-1) {
                    $J(this).attr("href",$J(this).attr("href").replace(new RegExp (what, 'g'), to));
                }
            });
        }
    }
    
    applyCookieTheme();
    putCookieThemeToSelect();
    $J("#select-theme-button").click(function(){
        jQuery.cookie("ui-theme", jQuery("#ui-select-theme option:selected").text());
        applyCookieTheme();
    })
    $J("#ui-show-select-theme-dialog-button").click(function(){
        jQuery("#ui-select-theme-dialog").dialog()
    })
})
