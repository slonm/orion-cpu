// здесь хранятся свойства отображения
var gridViewProperties={};
var gridTableObject=null;
/*
 * Инициализация формы - редактора свойств отображения
 */
function gridViewInit() {
    // готовим исходную таблицу
    gridTableObject=new gridTable('gridTable');

    // достаём описание существующих свойств, которые получены от сервера
    var value=$("gridPropertyViewJSON").value;
    if(!value) return false;
    // превращаем строку в объект
    eval("gridViewProperties="+value+";");
    if(!gridViewProperties) return false;
    // рисуем элементы формы
    drawViewEditorForm();
    //    // выстраиваем элементы формы в правильном порядке
    //    reorderGridViewBlocks();

    // включаем возможность перетаскивания элементов формы
    var keys=Object.keys(gridViewProperties);
    Sortable.create($('gridPropertyViewEditor').identify(),{
        tag:'div',
        overlap:'vertical',
        constraint:'vertical',
        starteffect: function (element) {
            element._opacity = Element.getOpacity(element);
            new Effect.Opacity(element, {duration:0.2, from:element._opacity, to:0.7});
            new Effect.Highlight(element, {});
        },
        onChange: function(){
            // присваиваем новые значения параметра ordering
            var maxColumns=keys.length;
            keys.each(function(key){
                gridViewProperties[key].ordering=maxColumns;
            });
            var npp=0;
            $$('div#gridPropertyViewEditor div').each(function(element){
                var id=element.identify().replace(/ViewContainer$/,'');
                gridViewProperties[id].ordering=npp++;
            });
            fireViewUpdatedEvent();
        }
    } );

    // сигнал всем остальным слушателям
    $('gridPropertyViewEditor').fire("viewmodel:setup");
    return true;
}

// =============================================================================
// после загрузки окна выполняем инициализацию
// формы редактирования условий сортировки
Event.observe(window, 'load',gridViewInit);


/**
 * Создание редактора свойств видимости
 */
function drawViewEditorForm(){
    // контейнер для редактора
    var parentId='gridPropertyViewEditor';
    var parent=$(parentId);
    Object.values(gridViewProperties).sort(viewModelComparator).each(function(element){
        // создаём контейнер для свойств одной колонки
        // console.log(element);
        var containerId=element.id+'ViewContainer';
        var container=new Element('div',{
            'id':containerId,
            'class':'viewElementContainer'
        });

        var elementId=element.id;
        // create "is visible" checkbox
        var isVisibleCheckbox=new Element('input',{'type':'checkbox','id':elementId+'isVisibleCheckbox'});
        if(element.isvisible){
            isVisibleCheckbox.writeAttribute('checked',true);
        }
        isVisibleCheckbox.observe('click',function(){
            // elementId
            gridViewProperties[elementId].isvisible=($(this).checked)?true:false;;
            fireViewUpdatedEvent();
        });
        container.insert(isVisibleCheckbox);

        // create "width" text field
        var textfield=new Element('input',{'type':'text','id':element.id+'Width','value':element.width, 'class':'viewElementWidth'});
        textfield.observe('keyup',function(){
            // elementId
            var width=$(this).getValue();
            if(width<=0) width=1;
            gridViewProperties[elementId].width=width;
            fireViewUpdatedEvent();
            this.focus();
        });
        container.insert(textfield);

        // label
        container.insert(element.label);
        parent.insert(container);
    });
}


function reorderGridViewBlocks(event){
    var parentId='gridPropertyViewEditor';
    var parent=$(parentId);

    var element;

    var gridViewModel=gridViewProperties;
    var maxordering=Object.keys(gridViewModel).length
    var minordering,
        minorderingLimit=-1,
        minorderingPosition;

    do{
        minordering=maxordering;
        minorderingPosition=false;
        // find minimal ordering
        for(var i in gridViewModel){
            if(gridViewModel[i].ordering>minorderingLimit && gridViewModel[i].ordering<minordering){
                minordering=gridViewModel[i].ordering;
                minorderingPosition=i;
            }
        }
        if(minorderingPosition){
          // cut
          element=$(minorderingPosition+'ViewContainer').remove();
          // paste at end of container
          parent.insert(element);

          minorderingLimit=minordering;
        }
    }while(minorderingPosition)
}
document.observe("viewmodel:changed", reorderGridViewBlocks);
document.observe("viewmodel:setup"  , reorderGridViewBlocks);


/*
 * Экспортирует условие отображения как строку в формате JSON
 */
function exportGridViewJSON(){
    $("gridPropertyViewJSON").value=Object.toJSON(gridViewProperties);
}
/*
 *  если в форме что-то изменилось, надо экспортировать JSON заново
 */
document.observe("viewmodel:changed", exportGridViewJSON);



/*
 * Функция для упорядочения правил сортировки
 */
function viewModelComparator(x, y) {
    if (x.ordering < y.ordering) return -1;
    else if (x.ordering == y.ordering) return 0;
    else    return 1;
}
/**
 * Создаёт событие "свойства отображения изменены"
 */
function fireViewUpdatedEvent(){
    $('gridPropertyViewEditor').fire("viewmodel:changed");
}



function updateWidthTextFields(){
    for(var id in gridViewProperties){
        //$(id+'Width').writeAttribute('value',gridViewProperties[id].width);
        $(id+'Width').value=gridViewProperties[id].width;
    }
}
document.observe("viewmodel:changed", updateWidthTextFields);
document.observe("viewmodel:setup", updateWidthTextFields);








// creating table object
function gridTable(gridId){

    this.gridId=gridId;
    var currentTable=this;
    this.table=$(gridId);



    /**
     * добавляем в заголовок таблицы элементы управления
     */
    // ------------------- перетаскиватели колонок - начало --------------------
    var npp=0;
    var ids={};
    this.table.select('th').each(function(element){
        var id=element.identify().replace(/ColumnHeader$/,'');
        ids[id]=id;
    });
    // console.log(ids);

    var row=new Element('tr',{'id':'gridColumnDragger'});
    Object.values(ids).each(function(id){
        var cell=new Element('td',{'id':id+'ColumnDragger'});
        cell.insert('&nbsp;');
        row.insert(cell);
    });
    
    // 
    this.table.select('thead').first().insert({'top':row});
    // this.table.insert({'top':row});
    // ------------------- перетаскиватели колонок - конец ---------------------




    // var firstRow=$(this.gridId).select('tr').first();

    // table attribute contains cell identifiers grouped by row
    var tmp;

    // загружаем идентификаторы колонок
    tmp=[];
    $(gridId).select('th').each(function(cell, cellId){
        tmp[cell.identify().replace(/ColumnHeader$/,'')]=cellId;
    });
    this.colNames=tmp;
    // console.log('colnames load OK');  console.log(this.colNames);

    // сохраняем таблицу по строкам
    tmp={};
    $(gridId).select('tr').each(function(rowElement){
        var rowCells=[];
        $(rowElement).childElements().each(function(cellElement,columnId){
            rowCells[columnId]=$(cellElement).identify(); //
        });
        tmp[rowElement.identify()]=rowCells;
    });
    this.row=tmp;
    this.rowNames=Object.keys(this.row);
    // console.log('table load OK'); console.log(this.row);console.log(this.rowNames);

    var icol,irow;

    // сохраняем таблицу по колонкам
    // сначала - заголовки колонок
    this.column={};
    for(icol in this.colNames){
        this.column[icol]={};
    }

    // группируем ячейки по колонкам
    // для каждой строки ...
    for(irow =0;irow<this.rowNames.length;irow++){
        // для каждой колонки
        for(icol in this.colNames){
            this.column[icol][this.rowNames[irow]]=this.row[this.rowNames[irow]][this.colNames[icol]];
        }
    }
    // console.log('column grouping OK'); console.log(this.column);


    //console.log('this.hide_columns OK');

    // переставить колонки в заданном порядке
    // columnNames - имена колонок в правильном порядке
    // moveFirstRow - надо ли перемещать первую строку
    reorderColumnsFunction=function (){ //columnNames,moveFirstRow
        var moveFirstRow=true;
        var columnNames=[];
        Object.values(gridViewProperties).sort(viewModelComparator).each(function(el){
            columnNames[columnNames.length]=el.id;
        });
        var nRows=currentTable.rowNames.length;
        var nCols=columnNames.length;
        var row,cell,cellId,firstRow;
        // console.log(columnNames);
        if(moveFirstRow) firstRow=0; else firstRow=1;

        // for each row
        for(var iRow=firstRow;iRow<nRows;iRow++){
            // get TR element
            var rowId=currentTable.rowNames[iRow];
            row=$(rowId);

            // for each cell
            for(var iCol=nCols-1;iCol>=0;iCol--){
                cellId=false;
                if(currentTable.column[columnNames[iCol]]){
                    cellId=currentTable.column[columnNames[iCol]][rowId];
                }
                if(cellId){
                    cell=$(cellId).remove();
                    Element.insert(row,{
                        Top:cell
                    });
                }
            }
        }
    }
    document.observe("viewmodel:changed", reorderColumnsFunction);
    document.observe("viewmodel:setup", reorderColumnsFunction);
    this.reorder_columns=reorderColumnsFunction;
    //console.log('this.reorder_columns OK');

    // включаем возможность перетаскивания колонок таблицы
    Sortable.create('gridColumnDragger',{
        tag:'td',
        overlap:'horizontal',
        constraint:'horizontal',
        starteffect: function (element) {
            element._opacity = Element.getOpacity(element);
            new Effect.Opacity(element, {duration:0.2, from:element._opacity, to:0.7});
            new Effect.Highlight(element, {});
        },
        onChange: function(){
            // присваиваем новые значения параметра ordering
            var keys=Object.keys(gridViewProperties);
            var maxColumns=keys.length;
            keys.each(function(key){
                gridViewProperties[key].ordering=maxColumns;
            });
            var npp=0;
            $$('tr#gridColumnDragger td').each(function(element){
                var id=element.identify().replace(/ColumnDragger$/,'');
                gridViewProperties[id].ordering=npp++;
            });

            // вызываем перерисовку
            fireViewUpdatedEvent();
        }
    } );


    // показать/скрыть колонки, в зависимости от настроек
    // функция, которая делает видимой одну колонку
    this.show_one_column=function (columnId){
        if(!this.column[columnId]) return;
        for(var rowId in this.column[columnId]){
            $(this.column[columnId][rowId]).show();
        }
    }
    //console.log('this.show_one_column OK');

    // функция, которая делает видимыми несколько колонок
    this.show_columns=function (column_names){
        var cnt=column_names.length;
        for(var col=0; col<cnt;col++){
            this.show_one_column(column_names[col]);
        }
    }
    // console.log('this.show_columns OK');


    // функция, которая скрывает одну колонку
    this.hide_one_column=function (columnId){
        if(!this.column[columnId]) return;
        for(var rowId in this.column[columnId]){
            $(this.column[columnId][rowId]).hide();
        }
    }
    // console.log('this.hide_one_column OK');

    // функция, которая скрывает несколько колонок
    this.hide_columns=function (column_names){
        var cnt=column_names.length;
        for(var col=0; col<cnt;col++){
            this.hide_one_column(column_names[col]);
        }
    }

    var changeColumnVisibility=function(){
            // присваиваем новые значения параметра ordering
            var keys=Object.keys(gridViewProperties);
            var maxColumns=keys.length;
            keys.each(function(key){
                if(gridViewProperties[key].isvisible){
                    currentTable.show_one_column(key);
                }else{
                    currentTable.hide_one_column(key);
                }
            });
            var npp=0;
            $$('tr#gridColumnDragger td').each(function(element){
                var id=element.identify().replace(/ColumnDragger$/,'');
                gridViewProperties[id].ordering=npp++;
            });
    }
    document.observe("viewmodel:changed", changeColumnVisibility);
    document.observe("viewmodel:setup", changeColumnVisibility);




    // управление шириной колонок

    // рисуем элементы для управления шириной колонок
    // console.log(gridViewProperties);
    this.dragStartPosition=0;
    this.resizedColumnId=false;
    this.oldWidth=0;
    this.table.setStyle({tableLayout:'fixed'});

    //    // дорисовать внутри каждой ячейки
    //    // элемент <div> для управления шириной
    //    for(var columnid in currentTable.column){
    //        for(var rowid in currentTable.column[columnid]){
    //            var cell=$(currentTable.column[columnid][rowid]);
    //            if(!cell) continue;
    //            // cell.makeClipping();
    //            if(cell.select('div.celltext').first()) continue;
    //            var container=new Element('div',{'class':'celltext'});
    //            container.setStyle({overflow:'hidden'});
    //            
    //            // container.update(cell.innerHTML);
    //            cell.childElements().each(function(el){
    //                container.insert($(el));
    //            });
    //            container.insert(cell.innerHTML);
    //            cell.update(container);
    //            
    //        }
    //    }
    this.setColumnWidth=function (columnid,newwidth){
        var col;
        for(var i in currentTable.column[columnid]){
            col=$(currentTable.column[columnid][i]);
            if(col){
                col.setStyle({'width':newwidth+'px'});
                col.select('div.celltext').each(function(element){
                    $(element).setStyle({width:newwidth+'px'});
                    //console.log($(element));
                });
            }
        }
    }
    this.observeColumnWith=function(){
        for(var i in gridViewProperties){
            currentTable.setColumnWidth(i,gridViewProperties[i].width);
        }
    }
    document.observe("viewmodel:changed", this.observeColumnWith);
    document.observe("viewmodel:setup", this.observeColumnWith);

    this.table.select('th').each(function(container){
        var id=container.identify().replace(/ColumnHeader$/,'');
        // var h=container.getHeight();
        var tmp;
        var sliderId='slider_'+id;
        tmp=new Element('div',{'class':"grid-width-slider", "id":sliderId});
        // tmp.setStyle({height: h+'px'});
        //console.log(tmp);
        container.insert({Top:tmp});
        new Draggable(sliderId, {
            constraint: 'horizontal',
            revert:true,
            onStart:function (object,event){
                currentTable.dragStartPosition=event.clientX;
                currentTable.oldWidth=$(id+'ColumnHeader').getWidth();
            },
            onDrag:function (object,event){
                var newPosition=event.clientX;
                var newWidth=currentTable.oldWidth + (newPosition-currentTable.dragStartPosition);
                if(newWidth>30){
                    currentTable.setColumnWidth(id,newWidth);
                    gridViewProperties[id].width=newWidth;
                    fireViewUpdatedEvent();
                }
            }
        });
    });



    // ------------- контекстное меню в ячейках таблицы - начало ---------------
    //

    // context menu
    this.contextMenu={};

    // добавить пункт в контекстное меню
    this.addMenuItem=function (menuId,menuItem){
        if(!this.contextMenu[menuId]) this.contextMenu[menuId]=[];
        this.contextMenu[menuId].push(menuItem);
    }

    // контруктор пункта контекстного меню
    this.MenuItem = function (_href,_text,_attr){
        this.href=_href;
        this.text=_text;
        this.attr=_attr;
    }
    this.MenuItem.prototype.getHtml=function(){
        return "<a href=\""+this.href+"\" "+this.attr+">"+this.text+"</a>";
    }

    // функция, скрывающая контекстное меню при нажатии мимо открытого меню
    window.conextMenuFlag=0;
    Event.observe(window,'mouseup',function(){
        if(window.conextMenuFlag==0){
            $$('div.grid-context-menu').each(Element.hide);
        }else{
            window.conextMenuFlag=0;
        }
    });

    // add listeners to all table cells
    for(irow =0;irow<this.rowNames.length;irow++){
        // для каждой колонки
        for(icol=0; icol<this.row[this.rowNames[irow]].length;icol++){
            //console.log(this.row[this.rowNames[irow]][icol]);
            $(this.row[this.rowNames[irow]][icol]).observe('contextmenu',function (event){
                if(Event.isLeftClick(event)) return;
                $$('div.grid-context-menu').each(Element.hide);
                var pos=$(this).cumulativeOffset();

                menuId='menu_'+$(this).identify();

                var mg=currentTable.contextMenu[menuId];
                if(!mg) return;

                X=pos.left+10;
                Y=pos.top+10;
                if(!$(menuId)){
                    var mnu=new Element('div',{
                        'class':'grid-context-menu',
                        'id':menuId
                    });
                    mnu.hide();
                    Element.insert(currentTable.table,{after:mnu});
                    var str='';
                    for(var i=0;i<mg.length;i++){
                        str+=mg[i].getHtml();
                    }
                    //console.log(menuId+str);
                    mnu.update(str);
                }
                var e=$(menuId);
                e.setStyle({'top':Y+'px','left':X+'px'});
                e.show();
                Event.stop(event);
                conextMenuFlag=1;
            });
        }
    }


    // ссылка "Скрыть колонку" в контекстном меню заголовка таблицы
    var headerRow=this.row[this.rowNames[1]];
    for(icol=0; icol<headerRow.length;icol++){
        this.addMenuItem('menu_'+headerRow[icol],new this.MenuItem('javascript:void(menuItemHide(\''+headerRow[icol].replace(/ColumnHeader$/,'')+'\'))','Hide',''));
    }

     // add "Filter by value" items
    for(irow =2;irow<this.rowNames.length;irow++){
        var currentRow=this.row[this.rowNames[irow]];
        for(icol=0;icol<headerRow.length;icol++){
            var colName=headerRow[icol].replace(/ColumnHeader$/,'')
            // add item only if column values can be filtered
            var column_values_can_be_filtered=false;
            for(var elname in filterElementBuilderConfig){
                if(elname.toString().indexOf(colName)>=0){
                    column_values_can_be_filtered=true;
                    break;
                }
            }
            if(column_values_can_be_filtered){
               this.addMenuItem('menu_'+currentRow[icol],new this.MenuItem('javascript:void(menuItemFilterByValue(\''+colName+'\',\''+currentRow[icol]+'\'))','Filter by value',''));
            }
        }
    }



}


// Действие "Фильтровать по значению" в контекстном меню
function menuItemFilterByValue(col,cell){
    var childrenContainerId="filterrootchildren";
    var currentNode="filterroot";
    var childNode,cnf,nd,rootType, rootId;
    // get first child of filterrootchildren
    var firstChild=$(childrenContainerId).select('div.filterElementContainer').first();
    if(firstChild){
        rootId=firstChild.identify().replace(/container$/,'');
        rootType=nodeList[rootId].typeName;
    }else{
        rootType='';
    }
    
    
    if(rootType=='AND'){
        //add child to AND node
        currentNode=nodeList[firstChild.identify().replace(/container$/,'')].uid;
        childrenContainerId=currentNode+"children";
    }else{
        // remember previous node

        if(firstChild) {
            childNode=firstChild.remove();
        }

        // add root node AND
        cnf=filterElementBuilderConfig['AND'];
        nd=new ElementBuilder[cnf.guitype].create(cnf,childrenContainerId);
        nodeList[nd.uid]=nd;
        
        currentNode=nd.uid;
        childrenContainerId=currentNode+"children";

        // add previous node to new root
        if(firstChild){
            $(childrenContainerId).insert(childNode);
            nd.children[rootId]=nodeList[rootId];
        }
    }

    cnf=filterElementBuilderConfig[col+'EQ'];
    nd=new ElementBuilder[cnf.guitype].create(cnf,childrenContainerId);
    nodeList[nd.uid]=nd;
    if(nodeList[currentNode]){
       nodeList[currentNode].getChildren();
    }
    var vl=$(cell).innerHTML.replace(/<[^>]+>/gi,'');
    $(nd.uid+'text').value=vl.trim();
    nodeList[nd.uid].value=vl.trim();
    exportFilterJSON();
    return false;

}

function menuItemHide(id){
     gridViewProperties[id].isvisible=false;
     $(id+'isVisibleCheckbox').checked=false;
     fireViewUpdatedEvent();
}

