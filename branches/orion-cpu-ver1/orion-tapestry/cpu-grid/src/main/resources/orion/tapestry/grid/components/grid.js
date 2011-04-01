// ======================== load table into array = begin ======================
var table,viewform,sortform;
Event.observe(window, 'load',function(){

    // parse HTML table
    table=new cpuGrid('grid');

    // load column widths
    var widthJSON;
    widthJSON=$('widthJSON').value;
    if(widthJSON!=''){
        var wdt;
        eval('wdt='+widthJSON);
        table.importColumnWidth(wdt);        
    }
    $('widthJSON').value=table.exportColumnWidth();
    table.onWidthAdjusted.push(function(){
        //console.log(table.exportColumnWidth());
        $('widthJSON').value=table.exportColumnWidth();
        grid_changed();
        //console.log('>>>'+$('widthJSON').value);
    });


    // create visibility setting form
    viewform = new viewForm('orderview', 'viewJSON');


    // show "Save changes" button
    viewform.onViewChanged.push(function(checked, unchecked){
        grid_changed();
    });

    // if grid properties form is changed
    // table must change also
    viewform.onViewChanged.push(function(checked, unchecked){
        table.show_columns(checked);
        table.hide_columns(unchecked);
        table.reorder_columns(checked,1);
    });

    // if columns are reordered
    // the grid properties form must be changed also
    table.onSorted.push(function(checked){
        viewform.updateViewSettings(checked);
        viewform.drawViewBlock(checked);
        $(viewform.textFieldId).value=viewform.viewSettings.toJSON();
        grid_changed();
    });

    // add context menu items
    // addMenuItem(menuId,menuItem)
    for(var cid in table.column){
        // add "Hide column" item
        table.addMenuItem('menu_'+cid,new table.MenuItem('javascript:menuItemHide(\''+cid+'\')','Hide',''));

    }
    // add "Filter by value" items
    for(var irow=1; irow< table.table.length; irow++){
        for(var icol=0;icol<table.table[0].length;icol++){
            table.addMenuItem('menu_'+table.table[irow][icol],new table.MenuItem('javascript:menuItemFilterByValue(\''+table.table[0][icol]+'\',\''+table.table[irow][icol]+'\')','Filter by cell value',''));
        }
    }


    // create form to change sort settings
    sortform=new  sortForm('ordersort','sortJSON');
});

// действие "Скрыть колонку" в котекстном меню
function menuItemHide(cid){
    table.hide_one_column(cid);
    var checked=table.getVisibleColumns();
    //console.log(checked);
    viewform.updateViewSettings(checked);
    viewform.drawViewBlock(checked);
    $(viewform.textFieldId).value=viewform.viewSettings.toJSON();
    grid_changed();
}


// Действие "Фильтровать по значению" в контекстном меню
function menuItemFilterByValue(col,cell){
    var fieldUid=col.replace(/^col_/,'');
    var newNodeId=filterNode['filterNode0'].createChild(filterNodeType[fieldUid+'EQ']);
    var vl=$(cell).innerHTML.replace(/<[^>]+>/gi,'');
    $(newNodeId+'parameter').value=vl;
    grid_changed();
    serializeTree();
}

// sorting properties form
function sortForm(containerId, textFieldId){
    this.containerId=containerId;
    this.textFieldId=textFieldId;
    var currentObject=this;
    this.sortSettings=[];

    this.importSortSettings=function(){
        var str=$(this.textFieldId).value;
        eval('var tmp='+str);
        this.sortSettings=tmp;

        // get order of visible form elements
        var cnt=this.sortSettings.length;
        this.sortSettings.sort(this.comparator);
        s_order=[];
        for(var i=0;i<cnt;i++){s_order[i]=this.sortSettings[i].uid;}

        // draw form elements
        this.drawSortBlock(s_order);
    }

    this.ordersortChanged=function (){
        currentObject.updateSortSettings();
        currentObject.drawSortIndicator();
        $(currentObject.textFieldId).value=currentObject.sortSettings.toJSON();
        grid_changed();
    }

    this.updateSortSettings=function(){
        var ordering=1;
        $(this.containerId).select('input[type="radio"]').each(function(element){
            if(element.checked){
                var uid=element.name.replace(/^gridsort_/,'');
                for(var i=0;i<currentObject.sortSettings.length;i++){
                    if(currentObject.sortSettings[i].uid==uid){
                        currentObject.sortSettings[i].ordering=ordering++;
                        currentObject.sortSettings[i].sortType=element.value;
                        return;
                    }
                }
            }
        });
        this.sortSettings.sort(this.comparator);
    }
    
    this.drawSortIndicator=function (){
        currentObject.sortSettings.sort(currentObject.comparator);
        var cnt=currentObject.sortSettings.length;
        var npp=0,a;
        for(var i = 0; i<cnt;i++){
            a=$('col_'+currentObject.sortSettings[i].uid);
            if(!a) continue;
            a=a.select('div.grid-cell').first();

            // remove old indicator
            a.select('span').each(function(col,index2){
                col.remove();
            });

            // draw new indicator
            if(currentObject.sortSettings[i].sortType==currentObject.sortSettings[i].sortTypeAsc){
                a.insert(new Element('span',{
                    'class':'downarrow'
                }));
                a.insert(new Element('span').update(++npp));
            }
            if(currentObject.sortSettings[i].sortType==currentObject.sortSettings[i].sortTypeDesc){
                a.insert(new Element('span',{
                    'class':'uparrow'
                }));
                a.insert(new Element('span').update(++npp));
            }
        }
    }

    // load list of checked elements
    this.getCheckedElements=function(){
        var new_order={};
        $(this.containerId).select('input[type="radio"]').each(function(element){
            if(element.checked){
                var tmp=element.name.replace(/^gridsort_/,'');
                new_order[tmp]=element.value;
            }
        });
        console.log(new_order);
        //alert(new_order);
        return new_order;
    }

    // draw GUI in the grid setting form
    this.drawSortBlock=function (order){
        var tmp,label,j,i,found;

        var ordersort=$(this.containerId);
        ordersort.update();

        var cnt=order.length;
        var cnt1=this.sortSettings.length;

        // update order
        for(j=0;j<cnt1;j++){
            found=false;
            for(i=0;i<cnt;i++){
                if(this.sortSettings[j].uid==order[i]){
                    found=true;
                }
            }
            if(!found){
                order[order.length]=this.sortSettings[j].uid;
            }
        }
        cnt=order.length;


        var sortradio,r_label;
        for(i=0;i<cnt;i++){

            for(j=0;j<cnt1;j++){
                if(this.sortSettings[j].uid==order[i]){
                    tmp=new Element('div',{
                        'class':"grid-sort-element"
                    });

                    r_label=new Element('label');
                    sortradio=new Element('input',{
                        'type':'radio',
                        'name':'gridsort_'+this.sortSettings[j].uid,
                        'checked':(this.sortSettings[j].sortType==this.sortSettings[j].sortTypeNone),
                        'value':this.sortSettings[j].sortTypeNone
                        });
                    Event.observe(sortradio, 'click',currentObject.ordersortChanged);
                    r_label.insert(sortradio);

                    label=new Element('span',{});
                    label.update('none');
                    r_label.insert(label);
                    tmp.insert(r_label);


                    r_label=new Element('label');
                    sortradio=new Element('input',{
                        'type':'radio',
                        'name':'gridsort_'+this.sortSettings[j].uid,
                        'checked':(this.sortSettings[j].sortType==this.sortSettings[j].sortTypeAsc),
                        'value':this.sortSettings[j].sortTypeAsc
                        });
                    Event.observe(sortradio, 'click',currentObject.ordersortChanged);
                    tmp.insert(sortradio);

                    label=new Element('span',{});
                    label.update('&darr;');
                    r_label.insert(label);
                    tmp.insert(r_label);


                    r_label=new Element('label');
                    sortradio=new Element('input',{
                        'type':'radio',
                        'name':'gridsort_'+this.sortSettings[j].uid,
                        'checked':(this.sortSettings[j].sortType==this.sortSettings[j].sortTypeDesc),
                        'value':this.sortSettings[j].sortTypeDesc
                        });
                    Event.observe(sortradio, 'click',currentObject.ordersortChanged);
                    r_label.insert(sortradio);

                    label=new Element('span',{});
                    label.update('&uarr;');
                    r_label.insert(label);
                    tmp.insert(r_label);

                    label=new Element('span',{});
                    label.update(this.sortSettings[j].label);

                    tmp.insert(label);
                    ordersort.insert(tmp);
                }
            }
        }

        // make elements sortable
        Sortable.create('ordersort',
        {
            tag:'div',
            overlap:'vertical',
            constraint:'vertical',
            starteffect: myStartEffect,
            onChange: currentObject.ordersortChanged
        });
    }

    // function to compare sortSettings elements
    this.comparator= function (x, y) {
        if (x.ordering < y.ordering) return -1;
        else if (x.ordering == y.ordering) return 0;
        else    return 1;
    }

    this.sortLinkClicked=function(event){
        

        var col_uid=$(this).ancestors().first().identify().replace(/^col_/,'');
        //console.log('sortlinkc:'+col_uid);
        var cnt=currentObject.sortSettings.length;
        var type='undefined';
        var i,pos;
        for(pos=0;pos<cnt;pos++){
            if(currentObject.sortSettings[pos].uid==col_uid){
                type=currentObject.sortSettings[pos].sortType;
                break;
            }
        }
        if(type=='undefined') return false;
        // get new sort type
        if(type==currentObject.sortSettings[pos].sortTypeNone)     type=currentObject.sortSettings[pos].sortTypeAsc;
        else if(type==currentObject.sortSettings[pos].sortTypeAsc) type=currentObject.sortSettings[pos].sortTypeDesc;
        else type=currentObject.sortSettings[pos].sortTypeNone;

        if(event.ctrlKey){
            //dopisat kolonku v pravilo sortirovki

            if(currentObject.sortSettings[pos].sortType==currentObject.sortSettings[pos].sortTypeNone){
                var max_ordering=0;
                for(i=0;i<cnt;i++){
                    if(currentObject.sortSettings[i].sortType==currentObject.sortSettings[i].sortTypeNone){
                        currentObject.sortSettings[i].ordering=cnt;
                    }else{
                        currentObject.sortSettings[i].ordering=++max_ordering;
                    }
                }
                currentObject.sortSettings[pos].sortType=type;
                currentObject.sortSettings[pos].ordering=++max_ordering;
            }else{
                currentObject.sortSettings[pos].sortType=type;
            //alert(sortSettings[pos].ordering);
            }
        }else{
            // udalit staroe pravilo sortirovki i sozdat novoe
            for(i=0;i<cnt;i++){
                currentObject.sortSettings[i].ordering=i+2;
                currentObject.sortSettings[i].sortType=currentObject.sortSettings[i].sortTypeNone;
            }
            currentObject.sortSettings[pos].ordering=1;
            currentObject.sortSettings[pos].sortType=type;
        }
        currentObject.sortSettings.sort(currentObject.comparator);

        // re-draw  sort indicator
        currentObject.drawSortIndicator();

        // redraw form elements
        currentObject.sortSettings.sort(currentObject.comparator);
        s_order=[];
        for(i=0;i<cnt;i++){
            s_order[i]=currentObject.sortSettings[i].uid;
        }
        currentObject.drawSortBlock(s_order);
        $(currentObject.textFieldId).value=currentObject.sortSettings.toJSON();
        //draw_sort_block('ordersort',sortSettings,s_order);
        //updateSortJSON('sortJSON',sortSettings,s_order);
        grid_changed();
    }


    this.importSortSettings();
    this.drawSortIndicator();


    // add sorting links to column headers
    var colh;
    for(var k=0;k<this.sortSettings.length;k++){
        colh=$('col_'+this.sortSettings[k].uid);
        if(!colh) continue;
        colh=colh.select('div.grid-cell').first();
        colh.addClassName('gridsortlink');
        Event.observe(colh, 'click',currentObject.sortLinkClicked);
    }
}


// creating table object
function cpuGrid(gridId){

    this.gridId=gridId;
    var currentTable=this;
    var firstRow=$(this.gridId).select('tr').first();

    // table attribute contains cell identifiers grouped by row
    var tmpTable=[];

    // load table cells into array
    $(gridId).select('tr').each(
        function(rowElement, rowId){
            var rowCells=[];
            $(rowElement).childElements().each(function(cellElement,columnId){
                rowCells[columnId]=$(cellElement).identify();
            });
            tmpTable[rowId]=rowCells;
        }
        );
    this.table=tmpTable;
    //console.log('table load OK');
    // extract columns
    // column attribute contains cell identifiers grouped by column
    this.column={};
    var icol,irow;
    for(icol=0;icol<this.table[0].length;icol++){
        this.column[this.table[0][icol]]={};
    }

    for(irow=0;irow<this.table.length;irow++){
        for(icol=0;icol<this.table[0].length;icol++){
            this.column[this.table[0][icol]][irow]=this.table[irow][icol];
        }
    }
    //console.log('column grouping OK');

    // show one column
    this.show_one_column=function (columnId){
        if(!this.column[columnId]) return;
        for(var rowId in this.column[columnId]){
            $(this.column[columnId][rowId]).show();
        }
    }
    //console.log('this.show_one_column OK');
    
    // show columns by field names
    this.show_columns=function (column_names){
        var cnt=column_names.length;
        for(var col=0; col<cnt;col++){
            if(this.column[column_names[col]]){
                this.show_one_column(column_names[col]);
            }
            if(this.column['col_'+column_names[col]]){
                this.show_one_column('col_'+column_names[col]);
            }
        }
    }
    //console.log('this.show_columns OK');


    // hide one column
    this.hide_one_column=function (columnId){
        if(!this.column[columnId]) return;
        for(var rowId in this.column[columnId]){
            $(this.column[columnId][rowId]).hide();
        }
    }
    //console.log('this.hide_one_column OK');

    // hide columns by field names
    this.hide_columns=function (column_names){
        var cnt=column_names.length;
        for(var col=0; col<cnt;col++){
            if(this.column[column_names[col]]){
                this.hide_one_column(column_names[col]);
            }
            if(this.column['col_'+column_names[col]]){
                this.hide_one_column('col_'+column_names[col]);
            }
        }
    }
    //console.log('this.hide_columns OK');

    // reorder columns
    this.reorder_columns=function (columnNames,moveFirstRow){
        var nRows=this.table.length;
        var nCols=columnNames.length;
        var row,cell,cellId,firstRow;
        if(moveFirstRow) firstRow=0; else firstRow=1;

        // for each row
        for(var iRow=firstRow;iRow<nRows;iRow++){
            // get TR element
            row=$(this.table[iRow][0]).ancestors().first();

            // for each cell
            for(var iCol=nCols-1;iCol>=0;iCol--){
                cellId=false;
                if(this.column['col_'+columnNames[iCol]]){
                    cellId=this.column['col_'+columnNames[iCol]][iRow];
                }
                if(this.column[columnNames[iCol]]){
                    cellId=this.column[columnNames[iCol]][iRow];
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
    //console.log('this.reorder_columns OK');



    // onSorted listeners
    this.onSorted=[];

    // create sortable columns
    Sortable.create(firstRow.identify(),
    {
        tag:firstRow.childElements().first().nodeName,
        overlap:'horizontal',
        constraint:'horizontal',
        starteffect: myStartEffect,
        onChange: function(element){
            var new_col_order=currentTable.getVisibleColumns();
            //console.log(new_col_order);
            currentTable.reorder_columns(new_col_order,0);
            // run all other listeners
            for(var listenerId=0; listenerId<currentTable.onSorted.length; listenerId++){
                currentTable.onSorted[listenerId](new_col_order);
            }
            return true;
        }
    } );

    this.getVisibleColumns=function(){
        var new_col_order=[];
        firstRow.childElements().each(function(s,index){
            var e=$(s);
            if(e.visible()){
                var id=e.identify();
                new_col_order.push(id.replace(/^col_/,''));
            }
        });
        return new_col_order;
    }


    // resize columns
    this.columnWidth={};
    for(var columnId in this.column){
        this.columnWidth[columnId]=$(columnId).getWidth();
    }

    this.dragStartPosition=0;
    this.resizedColumnId=false;
    this.oldWidth=0;
    this.onStartDrag = function (object,event){
        currentTable.dragStartPosition=event.clientX;
        currentTable.resizedColumnId=$(object.element).identify().replace(/^slider(right|left)_/,'')
        currentTable.oldWidth=$(currentTable.resizedColumnId).getWidth();
    }


    this.onDragRight=function (object,event){
        var newPosition=event.clientX;
        var newWidth=currentTable.oldWidth + (newPosition-currentTable.dragStartPosition);
        if(newWidth>30){
            currentTable.setColumnWidth(currentTable.resizedColumnId,newWidth);
        }
    }

    this.onWidthAdjusted=[];
    this.setColumnWidth=function (columnid,newwidth){
        var col;
        for(var i in currentTable.column[columnid]){
            col=$(currentTable.column[columnid][i]).select('div.grid-cell');
            if(col && col.first()){
                col.first().setStyle({width:newwidth});
            }
        }
        currentTable.columnWidth[columnid]=newwidth;
        for(var i=0;i<currentTable.onWidthAdjusted.length;i++){
            currentTable.onWidthAdjusted[i]();
        }
    }

    firstRow.childElements().each(function(element){
        var container=$(element);
        var tmp;
        tmp=new Element('div',{'class':"grid-width-slider-right", id:'sliderright_'+container.identify()});
        Element.insert(container,{Top:tmp});
        new Draggable(tmp.identify(), {
            constraint: 'horizontal',
            revert:true,
            onStart:currentTable.onStartDrag,
            onDrag:currentTable.onDragRight
        });
    });


    // used to load saved column widths
    this.importColumnWidth=function(columnWidthObject){
        for(var i in columnWidthObject){
            this.setColumnWidth(i,columnWidthObject[i]);
        }
    }

    this.exportColumnWidth=function(){
        //console.log(Object.toJSON(this.columnWidth));
        return Object.toJSON(this.columnWidth);
    }


    // context menu
    this.contextMenu={};

    this.addMenuItem=function (menuId,menuItem){
        if(!this.contextMenu[menuId]) this.contextMenu[menuId]=[];
        this.contextMenu[menuId].push(menuItem);
    }

    this.MenuItem = function (_href,_text,_attr){
        this.href=_href;
        this.text=_text;
        this.attr=_attr;
    }
    this.MenuItem.prototype.getHtml=function(){
        return "<a href=\""+this.href+"\" "+this.attr+">"+this.text+"</a>";
    }

    window.conextMenuFlag=0;
    Event.observe(window,'mouseup',function(){
        if(window.conextMenuFlag==0){
            $$('div.grid-context-menu').each(function(el){
                el.hide();
            });
        }else{
            window.conextMenuFlag=0;
        }
    });

    // add listeners to all table cells
    for(irow=0;irow<this.table.length;irow++){
        for(icol=0;icol<this.table[0].length;icol++){
            $(this.table[irow][icol]).observe('contextmenu',function (event){
                    if(Event.isLeftClick(event)) return;
                    $$('div.grid-context-menu').each(function(el){ el.hide(); });
                    var pos=$(this).cumulativeOffset();

                    menuId='menu_'+$(this).identify();
                    X=pos.left+10;
                    Y=pos.top+10;
                    if(!$(menuId)){
                        var mnu=new Element('div',{
                            'class':'grid-context-menu',
                            'id':menuId
                        });
                        mnu.hide();
                        // mnu.update('lllala'+menuId);
                        Element.insert($('grid'),{after:mnu});
                        var mg=currentTable.contextMenu[menuId];
                        if(!mg) return;
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

}







// ======================== load table into array = end ========================































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


// show button if the grid properties should be saved
function grid_changed(){
    $('savechanges').show();
}


// open selected page, javascript paginig links
function goToPage(pgnum){
    $('pageNumber').value=pgnum;
    $('gridpropertiesform').submit();
}



// start effect for Sortable elements
function myStartEffect(element) {
    element._opacity = Element.getOpacity(element);
    new Effect.Opacity(element, {
        duration:0.2,
        from:element._opacity,
        to:0.7
    });
    new Effect.Highlight(element, {});
}






// =============================================================================
// ======================= view form = begin ===================================

function viewForm(containerId, textFieldId){
    this.containerId=containerId;
    this.textFieldId=textFieldId;
    var currentObject=this;
    this.viewSettings=[];


    this.comparer=function (x, y) {
        if (x.ordering < y.ordering) return -1;
        else if (x.ordering == y.ordering) return 0; else    return 1;
    };


    this.importViewSetting=function(){
        // load JSON data
        var str=$(this.textFieldId).value;
        eval('var tmp='+str);
        this.viewSettings=tmp;

        // get order of visible form elements
        var cnt=this.viewSettings.length;
        this.viewSettings.sort(this.comparer);
        order=[];
        for(var i=0;i<cnt;i++){order[i]=this.viewSettings[i].uid;}

        this.drawViewBlock(order);
    }




    // -------------------------------------------------------------------------
    // draws the form elements
    this.drawViewBlock = function (order){
        var tmp,label,j,found;

        var orderview=$(this.containerId);
        orderview.update();
        var cnt=order.length;
        var cnt1=this.viewSettings.length;

        // update order
        for(j=0;j<cnt1;j++){
            found=false;
            for(var i=0;i<cnt;i++){
                if(this.viewSettings[j].uid==order[i]){
                    found=true;
                }
            }
            if(!found){
                order[order.length]=this.viewSettings[j].uid;
            }
        }


        cnt=order.length;
        var vis;
        for(var i=0;i<cnt;i++){

            for(j=0;j<cnt1;j++){
                if(this.viewSettings[j].uid==order[i]){
                    tmp=new Element('div',{
                        'class':"grid-view-element"
                    });
                    //viewSettings[j].
                    vis=new Element('input',{
                        'type':'checkbox',
                        'checked':(this.viewSettings[j].isVisible=='true'),
                        'value':this.viewSettings[j].uid,
                        'id':'viewcheckbox_'+this.viewSettings[j].uid
                        });
                    Event.observe(vis, 'click',currentObject.orderviewChanged);
                    label=new Element('label',{'for':'viewcheckbox_'+this.viewSettings[j].uid});
                    label.update(this.viewSettings[j].label);
                    tmp.insert(vis);
                    tmp.insert(label);
                    orderview.insert(tmp);
                }
            }
        }
        // make elements sortable
        Sortable.create(currentObject.containerId,
                       {tag:'div',
                        overlap:'vertical',
                        constraint:'vertical',
                        starteffect: myStartEffect,
                        onChange: currentObject.orderviewChanged
        });
    }
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // runs if form elements are changed
    this.onViewChanged=[];
    this.orderviewChanged = function (){

        var checked=[];
        var unchecked=[];
        $(currentObject.containerId).select('input[type="checkbox"]').each(function(element){
            if(element.checked){
                // collect checked elements
                checked.push(element.value);
            }else{
                // collect unchecked elements
                unchecked.push(element.value);
            }
        });
        //console.log('checked:'+checked);
        //console.log('unchecked:'+unchecked);

        currentObject.updateViewSettings(checked);

        // run all other listeners
        for(var listenerId=0; listenerId<currentObject.onViewChanged.length; listenerId++){
            currentObject.onViewChanged[listenerId](checked, unchecked);
        }

        // update text field
        $(currentObject.textFieldId).value=currentObject.viewSettings.toJSON();
        return true;
    }
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // update JSON representation
    this.updateViewSettings=function (checked){
        // mark visible elements
        var found;
        for(var j=0;j<this.viewSettings.length;j++){
            found=-1;
            for(var i=0;i<checked.length;i++){
                if(this.viewSettings[j].uid==checked[i]){
                    found=i;
                }
            }
            if(found>=0){
                this.viewSettings[j].ordering=found+1;
                this.viewSettings[j].isVisible='true';
            }else{
                this.viewSettings[j].ordering=this.viewSettings.length+1;
                this.viewSettings[j].isVisible='false';
            }
        }
        this.viewSettings.sort(this.comparer);
    }
    // -------------------------------------------------------------------------


    // load and apply posted data
    this.importViewSetting();



}





// ======================= view form = end =====================================

