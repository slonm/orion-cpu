/**
 * Редактор условия сортировки
 */
// здесь хранятся свойства сортировки
var gridSortProperties={};


/*
 * Инициализация формы - редактора условий сортировки
 */
function gridSortInit() {

    // достаём описание существующих условий сортировки, которые получены от сервера
    var value=$("gridSortJSON").value;
    if(!value) return false;

    // превращаем строку в объект
    eval("gridSortProperties="+value+";");
    if(!gridSortProperties) return false;

    // рисуем элементы формы
    drawSortEditorForm();

    // выстраиваем элементы формы в правильном порядке
    reorderGridSortBlocks();

    // включаем возможность перетаскивания блоков
    Sortable.create($('gridSortEditor').identify(),{
        tag:'div',
        overlap:'vertical',
        constraint:'vertical',
        starteffect: function (element) {
            element._opacity = Element.getOpacity(element);
            new Effect.Opacity(element, {duration:0.2, from:element._opacity, to:0.7});
            new Effect.Highlight(element, {});
        },
        onChange: function(){
            $('gridSortEditor').fire("sortmodel:changed");
        }
    } );

    // сигнал всем остальным слушателям
    $('gridSortEditor').fire("sortmodel:setup");
}

// =============================================================================
// после загрузки окна выполняем инициализацию
// формы редактирования условий сортировки
Event.observe(window, 'load',gridSortInit);


/*
 * Создание формы - редактора условий сортировки
 */
function drawSortEditorForm(){
    // иконка "соррировка по возрастанию"
    var asc ="<span class='lblArr'>&darr;</span><span class='lblLet' style=''>a<br/>z</span>&nbsp;";
    // иконка "соррировка по убыванию"
    var desc="<span class='lblArr'>&darr;</span><span class='lblLet' style=''>z<br/>a</span>&nbsp";

    // контейнер для редактора
    var parentId='gridSortEditor';
    var parent=$(parentId);


    // sort values
    Object.values(gridSortProperties).each(function(element){
        // console.log(element);
        var containerId=element.id+'SortContainer';
        var container=new Element('div',{
            'id':containerId,
            'class':'sortElementContainer'
        });

        var label;
        var config;

        // create "sort type" radiobutton
        label=new Element('label',{});
        container.insert(label);
        config={
            'type':'radio',
            'name':element.id+'SortTypeRadio',
            'id':element.id+'SortTypeRadioUNSORTED',
            'value':'UNSORTED'
        };
        if(element.columnsort=='UNSORTED'){
            config.checked='true';
        }
        var sortTypeUNSORTED=new Element('input',config);
        sortTypeUNSORTED.observe('change',    fireSortModelChanged);
        label.insert(sortTypeUNSORTED);
        label.insert('no&nbsp;');



        label=new Element('label',{});
        container.insert(label);
        config={
            'type':'radio',
            'name':element.id+'SortTypeRadio',
            'id':element.id+'SortTypeRadioASCENDING',
            'value':'ASCENDING'
        };
        if(element.columnsort=='ASCENDING'){
            config.checked='true';
        }
        var sortTypeASCENDING=new Element('input',config);
        sortTypeASCENDING.observe('change',fireSortModelChanged);
        label.insert(sortTypeASCENDING);
        label.insert(asc);


        label=new Element('label',{});
        container.insert(label);
        config={
            'type':'radio',
            'name':element.id+'SortTypeRadio',
            'id':element.id+'SortTypeRadioDESCENDING',
            'value':'DESCENDING'
        };
        if(element.columnsort=='DESCENDING'){
            config.checked='true';
        }
        var sortTypeDESCENDING=new Element('input',config);
        sortTypeDESCENDING.observe('change',fireSortModelChanged);
        label.insert(sortTypeDESCENDING);
        label.insert(desc);

        container.insert(element.label);
        parent.insert(container);
    });

}


function fireSortModelChanged(){
    $('gridSortEditor').fire("sortmodel:changed");
}


/*
 * Экспортирует условие упорядочения как строку в формате JSON
 */
function exportGridSortJSON(){
    var priority=0;
    $$('div.sortElementContainer').each(function(el){
        var id=el.identify().replace(/SortContainer$/, '');
        var columnsort='UNSORTED';
        if($(id+'SortTypeRadioASCENDING').checked){
            columnsort='ASCENDING';
        }else if($(id+'SortTypeRadioDESCENDING').checked){
            columnsort='DESCENDING';
        }
        gridSortProperties[id].columnsort=columnsort;
        gridSortProperties[id].priority=priority++;
    });
    // console.log(Object.toJSON(gridSortProperties));
    $("gridSortJSON").value=Object.toJSON(gridSortProperties);
}
/*
 *  если в форме что-то изменилось, надо экспортировать JSON заново
 */
document.observe("sortmodel:changed", exportGridSortJSON);


/*
 * Выстраивает список блоков в форме управления сортировкой
 * в зависимости от значения атрибута priority
 */
function reorderGridSortBlocks(){
    var parentId='gridSortEditor';
    var parent=$(parentId);

    var element;

    var gridSortModel=gridSortProperties;
    var maxPriority=Object.keys(gridSortModel).length
    var minPriority,
        minPriorityLimit=-1,
        minPriorityPosition;

    do{
        minPriority=maxPriority;
        minPriorityPosition=false;
        // find minimal priority
        for(var i in gridSortModel){
            if(gridSortModel[i].priority>minPriorityLimit && gridSortModel[i].priority<minPriority){
                minPriority=gridSortModel[i].priority;
                minPriorityPosition=i;
            }
        }
        if(minPriorityPosition){
          // cut
          element=$(minPriorityPosition+'SortContainer').remove();
          // paste at end of container
          parent.insert(element);

          minPriorityLimit=minPriority;
        }
    }while(minPriorityPosition)

}

/*
 * Функция для упорядочения правил сортировки
 */
function sortModelComparator(x, y) {
    if (x.priority < y.priority) return -1;
    else if (x.priority == y.priority) return 0;
    else    return 1;
}
/*
 * рисование элементов управления в заголовках колонок
 */
function drawSortTableHeaders(){
    var npp=0;
    Object.values(gridSortProperties).sort(sortModelComparator).each(function(element){
        var header=$(element.id+'ColumnHeader');
        if(header){
            var link=new Element('a',{
            'id':element.id+'ColumnHeaderLink',
            'class':'sortColumnHeaderLink',
            'href':'javascript:void(0)'
            });
            link.observe('click',sortTableHeaderClicked);
            header.insert(link);
        }
    });
}
document.observe("sortmodel:setup", drawSortTableHeaders);

/*
 * Расстановка стрелочек в заголовках колонок
 */
function updateTableHeaders(){
    var npp=0;
    Object.values(gridSortProperties).sort(sortModelComparator).each(function(element){
        var header=$(element.id+'ColumnHeader');
        if(header){
            var link=$(element.id+'ColumnHeaderLink');
            // show indicator
            if(element.columnsort=='DESCENDING'){
                link.update(new Element('span',{'class':'uparrow'}));
                link.insert( (++npp) );
            }else if(element.columnsort=='ASCENDING'){
                link.update(new Element('span',{'class':'downarrow'}));
                link.insert( (++npp)  );
            }else{
                link.update('&nbsp;');
            }
            header.insert(link);
        }
    });
}
document.observe("sortmodel:changed", updateTableHeaders);
document.observe("sortmodel:setup", updateTableHeaders); // этот обработчик надо подключать строго после drawSortTableHeaders

/*
 * Обрабатываем нажатие на сортировочную ссылку в заголовке таблицы
 */
function sortTableHeaderClicked(event){
    var element = Event.element(event);
    var tmp=element.identify();
    var id;
    if(!tmp.match(/ColumnHeaderLink$/)){
        tmp=element.ancestors().first().identify();
    }
    id=tmp.replace(/ColumnHeaderLink$/,'');

    // console.log('sort link click:'+id);
    
    if(event.ctrlKey){
       // если был нажат Ctrl, то
       var values=Object.values(gridSortProperties).sort(sortModelComparator);
       // если сортировка по текущей колонке уже была, то надо изменить текущую колонку
       if(gridSortProperties[id].columnsort!='UNSORTED'){
           gridSortProperties[id].columnsort=cycleColumnSort(gridSortProperties[id].columnsort);
           var npp=0;
           values.each(function(element){
               if(element.columnsort!='UNSORTED'){
                   gridSortProperties[element.id].priority=npp++;
               }
           });
           values.each(function(element){
               if(element.columnsort=='UNSORTED'){
                   gridSortProperties[element.id].priority=npp++;
               }
           });
       }else{
           // пересчитать приоритеты
           // 1) сосчитать, по скольким колонкам выполняется сортировка
           var npp=0;
           values.each(function(element){
               if(element.columnsort!='UNSORTED'){
                   gridSortProperties[element.id].priority=npp++;
               }
           });
           gridSortProperties[id].columnsort=cycleColumnSort(gridSortProperties[id].columnsort);
           gridSortProperties[id].priority=npp++;
           values.each(function(element){
               if(element.columnsort=='UNSORTED'){
                   gridSortProperties[element.id].priority=npp++;
               }
           });
       }
    }else{
       // если не была нажата клавиша Ctrl
       // удалить старое правило сортировки и создать новое
       var npp=1;
       for(var i in gridSortProperties){
           // console.log(i);
           if(i==id){
               gridSortProperties[i].columnsort=cycleColumnSort(gridSortProperties[i].columnsort);
               gridSortProperties[i].priority=0;
           }else{
               gridSortProperties[i].columnsort='UNSORTED';
               gridSortProperties[i].priority=npp++;
           }
       }
    }

    for(var j in gridSortProperties){
        $(j+'SortTypeRadioUNSORTED').checked=(gridSortProperties[j].columnsort=='UNSORTED');
        $(j+'SortTypeRadioASCENDING').checked=(gridSortProperties[j].columnsort=='ASCENDING');
        $(j+'SortTypeRadioDESCENDING').checked=(gridSortProperties[j].columnsort=='DESCENDING');
    }
    reorderGridSortBlocks();
    fireSortModelChanged();
    Event.stop(event);    
}

function cycleColumnSort(val){
    if(val=='UNSORTED'){
        return 'ASCENDING';
    }
    if(val=='ASCENDING'){
        return 'DESCENDING';
    }
    if(val=='DESCENDING'){
        return 'UNSORTED';
    }
}