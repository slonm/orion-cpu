var counter;
Event.observe(window, 'load',function(){
        Sortable.create('orderview',{tag:'div',
					 overlap:'vertical',
					 constraint:'vertical',
					 starteffect: myStartEffect
				    ,onUpdate: function(){ renumerate('div#orderview input[type="text"]'); return true;}
       				}
        );
	renumerate('div#orderview input[type="text"]');

        Sortable.create('ordersort',{tag:'div',
					 overlap:'vertical',
					 constraint:'vertical',
					 starteffect: myStartEffect
				    ,onUpdate: function(){ renumerate('div#ordersort input[type="text"]'); return true;}
       				}
        );
	renumerate('div#ordersort input[type="text"]');

});

function renumerate(selector){
	counter=1;
    $$(selector).each(function(element){ element.value=counter++; });
}
// start effect for Sortable elements
function myStartEffect(element) {
     element._opacity = Element.getOpacity(element);
     new Effect.Opacity(element, {duration:0.2, from:element._opacity, to:0.7});
     new Effect.Highlight(element, {});
}
