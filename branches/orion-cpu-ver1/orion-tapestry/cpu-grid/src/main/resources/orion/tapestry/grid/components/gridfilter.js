
// уникальный идентификатор
var filterNodeID=0;

// конструкторы узлов
var filterNodeType={};

// названия конструкторов узлов
var filterNodeTypeLabel={};

// реестр узлов
var filterNode={};

// корневой узел
var rootNode;

// генерирует уникальный идентификатор узла
function newFilterNodeID(){
   return 'filterNode'+(filterNodeID++);
}

// добавляет новый узел
function chooseNodeType(parentId){
   var link;

   // создаем новый элемент
   var dialog=new Element('div',{'id':'filterNodeType'});
   link=new Element('a',{'class':'closebutton','href':'#'}).update('&times;');
   link.observe('click',function(event){$('filterNodeType').update('').setStyle({display:'none'})});
   dialog.insert(link);

   // заполняем список типов узлов
   dialog.insert('Choose node type:');

   //
   var tmp=new Element('div',{'id':'filterNodeTypeList'});
   dialog.insert(tmp);
   for(var t in filterNodeType){
      // идентификатор ссылки содержит название типа узла и идентификатор узла-родителя
      link=new Element('a',{id:'link_'+t+'_'+parentId,'href':'#'}).update(filterNodeTypeLabel[t]);
      link.observe('click',
         function(event){
            var parameters=this.id.split(/_/);
            filterNode[parameters[2]].createChild(filterNodeType[parameters[1]]);
            $('filterNodeType').update('').setStyle({display:'none'});
         }
      );
      tmp.insert(link);
   }
   $('filterNodeType').insert(dialog);
   $('filterNodeType').setStyle({display:'block'})
}


// обработка нажатия на ссылку "Создать под-узел"
// общий метод для всех типов узлов
function thisNewChildClicked(event){
   var nodeId=this.id.replace(/newchild$/,'');
   // предложить список типов в диалоге
   chooseNodeType(nodeId);
}


// обработка нажатия на ссылку "удаление узла"
// общий метод для всех типов узлов
function thisRemoveClicked(event){
   var nodeId=this.id.replace(/delete$/,'');
   filterNode[nodeId].remove();
   serializeTree();
   grid_changed();
}

// создание под-узла
// общий метод для всех типов узлов
function thisCreateChild(nodeCreator){
   var newNode=new nodeCreator();
   this.children[newNode.id]=newNode;
   this.nodeChildrenDOMElement.insert(newNode.DOMElement);
   serializeTree();
   return newNode.id;
}


// функция удаления узла
// общий метод для всех типов узлов
function thisRemove(){
   // проверить существование узлов-детей
   for(var c1 in this.children){
      if(!filterNode[c1]) delete(this.children[c1]);
   }

   // персонально удалить каждый под-узел
   for(var c2 in this.children){
      this.children[c2].remove();
      delete(this.children[c2]);
   }
   this.DOMElement.remove();
   if(filterNode[this.id]) delete(filterNode[this.id]);
   grid_changed();
}

// функция сериализации детей
function thisSerializeChildren(){
   // проверить существование узлов-детей
   // и удалить несуществующие ссылки
   for(var c1 in this.children){
      if(!filterNode[c1]) delete(this.children[c1]);
   }
   var ch={};
   for(var c2 in this.children){
      ch[this.children[c2].id]=this.children[c2].serialize();
   }
   return ch;
}

// создание дерева DOM  из структуры JSON
function thisUnserializeChildren(_сhildren){
   var childId;
   for(var c2 in _сhildren){
      childId=this.createChild(filterNodeType[_сhildren[c2].type]);
      filterNode[childId].unserialize(_сhildren[c2]);
   }
}

// сохранение фильтра в виде текстовой строки
function serializeTree(){
   var tree=rootNode.serialize();//
   $('filterJSON').value=Object.toJSON(tree);
}


// =========================== корневой узел = начало ==========================
function NodeRoot(){
   // тип узла
   this.type='NodeAND';

   this.label='AND';

   // идентификатор узла
   this.id=newFilterNodeID();

   // регистрируемся в глобальном реестре узлов
   filterNode[this.id]=this;

   // пустой список под-узлов
   this.children={};

   // указатель на объект  DOM, содержащий описание узла
   this.nodeInfoDOMElement=new Element('div',{'class':'filterNodeInfo', 'id':this.id+'info'});

   // флажок "элемент активен"
   this.nodeIsActive=new Element('input',{'type':'checkbox', 'id':this.id+'isActive','checked':'true'});
   this.nodeIsActive.observe('click',serializeTree);
   this.nodeInfoDOMElement.insert(this.nodeIsActive);

   this.nodeInfoDOMElement.insert(this.label);

   // ссылка "добавить под-узел"
   this.createChildDOMElement=new Element('a',{id:this.id+'newchild', 'class':'filterNodeMenu','href':'#'}).update('New condition');
   this.createChildDOMElement.observe('click',thisNewChildClicked);

   this.nodeInfoDOMElement.insert(this.createChildDOMElement);

   // указатель на объект DOM, соответствующий всему узлу
   this.DOMElement=new Element('div',{'class':'filterNode','id':this.id});
   this.DOMElement.insert(this.nodeInfoDOMElement);

   // указатель на объект  DOM, содержащий под-узлы
   this.nodeChildrenDOMElement=new Element('div',{'class':'filterNodeChildren', 'id':this.id+'Children'});
   this.DOMElement.insert(this.nodeChildrenDOMElement);

   // функция создания под-узла
   this.createChild=thisCreateChild;

   // функция удаления узла
   this.remove=thisRemove;


   this.serializeChildren=thisSerializeChildren;

   // функция экспорта дерева в строку
   // в обратную (польскую?) бесскобочную форму
   // для отправки на сервер
   this.serialize=function(){
      var expression={};
      expression['type']=this.type;
      expression['isactive']=this.nodeIsActive.checked?'1':'0';
      expression['children']=this.serializeChildren();
      return expression;
   };

   // функция импорта из строки
   this.unserialize=function(str){
      eval('var tree='+str+';');
      this.unserializeChildren(tree.children);
      this.nodeIsActive.checked=(tree.isactive=='1');
      serializeTree();
   };

   this.unserializeChildren = thisUnserializeChildren;

}
// =========================== корневой узел = конец ===========================



// =========================== узел типа "список узлов" = начало ===============
function NodeList(_label,_type){
   // тип узла
   this.type=_type;

   // подпись
   this.label=_label;

   // идентификатор узла
   this.id=newFilterNodeID();

   // регистрируемся в глобальном реестре узлов
   filterNode[this.id]=this;

   // пустой список под-узлов
   this.children={};

   // указатель на объект DOM, соответствующий всему узлу
   this.DOMElement=new Element('div',{'class':'filterNode','id':this.id});


   // указатель на объект  DOM, содержащий описание узла
   this.nodeInfoDOMElement=new Element('div',{'class':'filterNodeInfo', 'id':this.id+'info'});

   // флажок "элемент активен"
   this.nodeIsActive=new Element('input',{'type':'checkbox', 'id':this.id+'isActive','checked':'true'});
   this.nodeIsActive.observe('click',serializeTree);
   this.nodeInfoDOMElement.insert(this.nodeIsActive);


   // видимый текст, название узла
   this.nodeInfoDOMElement.insert(this.label);

   var tmp;
   // ссылка "добавить под-узел"
   tmp=new Element('a',{id:this.id+'newchild', 'class':'filterNodeMenu','href':'#'}).update('new child');
   tmp.observe('click',thisNewChildClicked);
   this.nodeInfoDOMElement.insert(tmp);

   // ссылка "удалить узел"
   tmp=new Element('a',{id:this.id+'delete','class':'filterNodeMenu','href':'#'}).update('delete');
   tmp.observe('click',thisRemoveClicked);
   this.nodeInfoDOMElement.insert(tmp);

   this.DOMElement.insert(this.nodeInfoDOMElement);

   // указатель на объект  DOM, содержащий под-узлы
   this.nodeChildrenDOMElement=new Element('div',{'class':'filterNodeChildren', 'id':this.id+'Children'});
   this.DOMElement.insert(this.nodeChildrenDOMElement);

   // функция создания под-узла
   this.createChild=thisCreateChild;

   // функция удаления узла
   this.remove=thisRemove;

   this.serializeChildren=thisSerializeChildren;

   // функция экспорта дерева в строку
   this.serialize=function(){
      var expression={};
      expression['type']=this.type;
      expression['isactive']=this.nodeIsActive.checked?'1':'0';
      expression['children']=this.serializeChildren();
      return expression;
   };

   // функция импорта из массива элементов
   this.unserialize=function(tree){
      this.unserializeChildren(tree.children);
      this.nodeIsActive.checked=(tree.isactive=='1');
      serializeTree();
   };

   this.unserializeChildren = thisUnserializeChildren;

}
// =========================== узел типа "список узлов" = конец ================






// =========================== узел типа "НЕ" = начало =========================
function NodeNOT(){
   // тип узла
   this.type='NodeNOT';

   // подпись
   this.label='NOT';

   // идентификатор узла
   this.id=newFilterNodeID();

   // регистрируемся в глобальном реестре узлов
   filterNode[this.id]=this;

   // пустой список под-узлов
   this.children={};

   // указатель на объект  DOM, содержащий описание узла
   this.nodeInfoDOMElement=new Element('div',{'class':'filterNodeInfo', 'id':this.id+'info'});

   // флажок "элемент активен"
   this.nodeIsActive=new Element('input',{'type':'checkbox', 'id':this.id+'isActive','checked':'true'});
   this.nodeIsActive.observe('click',serializeTree);
   this.nodeInfoDOMElement.insert(this.nodeIsActive);

   // видимый текст, название узла
   this.nodeInfoDOMElement.insert(this.label);

   var tmp;
   // ссылка "добавить под-узел"
   tmp=new Element('a',{id:this.id+'newchild', 'class':'filterNodeMenu','href':'#'}).update('new child');
   tmp.observe('click',
      function(event){
         // проверить существование узлов-детей
         var n=0; // количество детей
         var nodeId=this.id.replace(/newchild$/,'');
         var children=filterNode[nodeId].children;
         for(var c1 in children){if(!filterNode[c1]) delete(children[c1]); else n++;}
         // удаляем старое условие
         for(var c2 in children){children[c2].remove();delete(children[c2]);}

         // создаём новое условие
         // предложить список типов в диалоге
         chooseNodeType(nodeId);
      }
   );
   this.nodeInfoDOMElement.insert(tmp);

   // ссылка "удалить узел"
   tmp=new Element('a',{id:this.id+'delete','class':'filterNodeMenu','href':'#'}).update('delete');
   tmp.observe('click',thisRemoveClicked);
   this.nodeInfoDOMElement.insert(tmp);

   // указатель на объект DOM, соответствующий всему узлу
   this.DOMElement=new Element('div',{'class':'filterNode','id':this.id});
   this.DOMElement.insert(this.nodeInfoDOMElement);

   // указатель на объект  DOM, содержащий под-узлы
   this.nodeChildrenDOMElement=new Element('div',{'class':'filterNodeChildren', 'id':this.id+'Children'});
   this.DOMElement.insert(this.nodeChildrenDOMElement);

   // функция создания под-узла
   this.createChild=thisCreateChild;

   // функция удаления узла
   this.remove=thisRemove;

   this.serializeChildren=thisSerializeChildren;

   // функция экспорта дерева в строку
   this.serialize=function(){
      var expression={};
      expression['type']=this.type;
      expression['isactive']=this.nodeIsActive.checked?'1':'0';
      expression['children']=this.serializeChildren();
      return expression;
   };

   // функция импорта из массива элементов
   this.unserialize=function(tree){
      this.unserializeChildren(tree.children);
      this.nodeIsActive.checked=(tree.isactive=='1');
      serializeTree();
   };

   this.unserializeChildren = thisUnserializeChildren;

}
// =========================== узел типа "НЕ" = конец ==========================





// =========================== узел типа "текстовое поле" = начало =============
function NodeText(label,type,parameter,validator){
   // тип узла
   this.type=type;
   this.label=label;
   this.parameter=parameter;

   // идентификатор узла
   this.id=newFilterNodeID();

   // регистрируемся в глобальном реестре узлов
   filterNode[this.id]=this;

   // пустой список под-узлов
   this.children={};

   // указатель на объект  DOM, содержащий описание узла
   this.nodeInfoDOMElement=new Element('div',{'class':'filterNodeInfo', 'id':this.id+'info'});

   // флажок "элемент активен"
   this.nodeIsActive=new Element('input',{'type':'checkbox', 'id':this.id+'isActive','checked':'true'});
   this.nodeIsActive.observe('click',serializeTree);
   this.nodeInfoDOMElement.insert(this.nodeIsActive);


   this.nodeInfoDOMElement.insert(this.label);

   this.parameterDOMElement=new Element('input',{'type':'text', 'name':this.id+'parameter', 'id':this.id+'parameter', 'value':''});
   this.nodeInfoDOMElement.insert(this.parameterDOMElement);
   this.parameterDOMElement.observe('keyup',serializeTree);
   if(validator && typeof(validator)=='function'){
       this.parameterDOMElement.observe('change',validator);
   }

   var tmp;
   // ссылка "удалить узел"
   tmp=new Element('a',{id:this.id+'delete','class':'filterNodeMenu','href':'#'}).update('delete');
   tmp.observe('click',thisRemoveClicked);
   this.nodeInfoDOMElement.insert(tmp);

   // указатель на объект DOM, соответствующий всему узлу
   this.DOMElement=new Element('div',{'class':'filterNode','id':this.id});
   this.DOMElement.insert(this.nodeInfoDOMElement);

   // функция удаления узла
   this.remove=thisRemove;

   // функция экспорта узла
   this.serialize=function(){
        var expression={};
        expression.type=this.type;
        expression['isactive']=this.nodeIsActive.checked?'1':'0';
        expression.value=$(this.id+'parameter').value;
        return expression;
   };

   // функция импорта из массива элементов
   this.unserialize=function(tree){
      $(this.id+'parameter').value=tree.value;
      this.nodeIsActive.checked=(tree.isactive=='1');
      serializeTree();
   };

};
// =========================== узел типа "текстовое поле" = конец ==============

// ================== узел типа "элементарное условие для даты" = начало =======
function NodeDate(label,type,parameter,validator){
   // тип узла
   this.type=type;
   this.label=label;
   this.parameter=parameter;

   // идентификатор узла
   this.id=newFilterNodeID();

   // регистрируемся в глобальном реестре узлов
   filterNode[this.id]=this;

   // пустой список под-узлов
   this.children={};

   // указатель на объект  DOM, содержащий описание узла
   this.nodeInfoDOMElement=new Element('div',{'class':'filterNodeInfo', 'id':this.id+'info'});

   // флажок "элемент активен"
   this.nodeIsActive=new Element('input',{'type':'checkbox', 'id':this.id+'isActive','checked':'true'});
   this.nodeIsActive.observe('click',serializeTree);
   this.nodeInfoDOMElement.insert(this.nodeIsActive);


   this.nodeInfoDOMElement.insert(this.label);

   this.parameterDOMElement=new Element('input',{'type':'text', 'name':this.id+'parameter', 'id':this.id+'parameter', 'value':''});
   this.nodeInfoDOMElement.insert(this.parameterDOMElement);
   this.parameterDOMElement.observe('keyup',serializeTree);
   if(validator && typeof(validator)=='function'){
       this.parameterDOMElement.observe('change',validator);
   }

   var tmp,tmp2;

   // ссылка "показать календарь"
   //tmp=new Element('a',{id:this.id+'calendar_open','class':'filterNodeMenu','href':'#'}).update('[...]');
   tmp=new Element('input',{id:this.id+'calendar_open','href':'#','type':'button','value':'...'});
   tmp2 ="var fun1=function(){\n";
   tmp2+="   window.c"+this.id+"calendar=new Calendar('"+this.id+"calendar','"+this.id+"parameter','c"+this.id+"calendar');\n";
   tmp2+="   window.c"+this.id+"calendar.draw_calendar(c"+this.id+"calendar.month_today, c"+this.id+"calendar.year_today);\n";
   tmp2+="   $('"+this.id+"calendar').show();\n";
   tmp2+="}";
   eval(tmp2);
   tmp.observe('click',fun1);
   this.nodeInfoDOMElement.insert(tmp);

   // ссылка "удалить узел"
   tmp=new Element('a',{id:this.id+'delete','class':'filterNodeMenu','href':'#'}).update('delete');
   tmp.observe('click',thisRemoveClicked);
   this.nodeInfoDOMElement.insert(tmp);

   // контейнер для календаря
   tmp=new Element('div',{id:this.id+'calendar'}).update('&nbsp;');
   tmp.setStyle({display:'none'}).addClassName('calendar');
   this.nodeInfoDOMElement.insert(tmp);

   // закрываем календарь при нажатии мимо контейнера для календаря
   tmp ="var fun2=function(event){\n";
   tmp+="   var id='"+this.id+"calendar';\n";
   tmp+="   var elt;\n";
   tmp+="   elt = $(Event.element(event));\n";
   tmp+="   if(elt.up('#'+id)) return true;\n";
   tmp+="   if(elt.identify()==id) return true;";
   tmp+="   elt = $(Event.element(event))//.up('#'+id+'_open');\n";
   tmp+="   if(elt.identify()==id+'_open') return true;";
   tmp+="   $(id).update('&nbsp;').hide();};";
   eval(tmp);
   Event.observe(window, 'click', fun2);

/*
      <a id=calendar1_open class=menu_btn href=# onclick="cs('calendar1'); return false;">[+]</a>
      <div id=calendar1 class=menu_block style='display:none;'>&nbsp;</div>
      <script type="text/javascript">
      <!--
      c1=new Calendar('calendar1','filter_date_created_min','c1');
      c1.draw_calendar(c1.month_today, c1.year_today);
      // -->
      </script>
 */


   // указатель на объект DOM, соответствующий всему узлу
   this.DOMElement=new Element('div',{'class':'filterNode','id':this.id});
   this.DOMElement.insert(this.nodeInfoDOMElement);

   // функция удаления узла
   this.remove=thisRemove;

   // функция экспорта узла
   this.serialize=function(){
        var expression={};
        expression.type=this.type;
        expression['isactive']=this.nodeIsActive.checked?'1':'0';
        expression.value=$(this.id+'parameter').value;
        return expression;
   };

   // функция импорта из массива элементов
   this.unserialize=function(tree){
      $(this.id+'parameter').value=tree.value;
      this.nodeIsActive.checked=(tree.isactive=='1');
      serializeTree();
   };

};
// ================== узел типа "'элементарное условие для даты" = конец =======


// ================== узел типа "условие без параметра" = начало ===============
// применяется для условий типа IS NULL, IS NOT NULL
//
function NodeCheckbox(label,type,parameter){
   // тип узла
   this.type=type;
   this.label=label;
   this.parameter=parameter;

   // идентификатор узла
   this.id=newFilterNodeID();

   // регистрируемся в глобальном реестре узлов
   filterNode[this.id]=this;

   // пустой список под-узлов
   this.children={};

   // указатель на объект  DOM, содержащий описание узла
   this.nodeInfoDOMElement=new Element('div',{'class':'filterNodeInfo', 'id':this.id+'info'});

   // флажок "элемент активен"
   this.nodeIsActive=new Element('input',{'type':'checkbox', 'id':this.id+'isActive','checked':'true'});
   this.nodeIsActive.observe('click',serializeTree);
   this.nodeInfoDOMElement.insert(this.nodeIsActive);


   this.nodeInfoDOMElement.insert(this.label);

   var tmp;
   // ссылка "удалить узел"
   tmp=new Element('a',{id:this.id+'delete','class':'filterNodeMenu','href':'#'}).update('delete');
   tmp.observe('click',thisRemoveClicked);
   this.nodeInfoDOMElement.insert(tmp);

   // указатель на объект DOM, соответствующий всему узлу
   this.DOMElement=new Element('div',{'class':'filterNode','id':this.id});
   this.DOMElement.insert(this.nodeInfoDOMElement);

   // функция удаления узла
   this.remove=thisRemove;

   // функция экспорта узла
   this.serialize=function(){
        var expression={};
        expression.type=this.type;
        expression['isactive']=this.nodeIsActive.checked?'1':'0';
        return expression;
   };

   // функция импорта из массива элементов
   this.unserialize=function(tree){
      this.nodeIsActive.checked=(tree.isactive=='1');
      serializeTree();
   };
};
// ================== узел типа "условие без параметра" = конец ================


// ================== типичные валидаторы = начало =============================
function validator_require_int(){
   var isValid=this.value.match(/^[+-]?[0-9]+$/);

   // индикация ошибки
   if(!isValid) $(this).setStyle({'color':'white','backgroundColor':'red'});
   else $(this).setStyle({'color':'black','backgroundColor':'white'});
}
function validator_require_float(){
   var x = new Number(this.value.replace(/,/,'.'));
   var isValid=!isNaN(x);

   // индикация ошибки
   if(!isValid) $(this).setStyle({'color':'white','backgroundColor':'red'});
   else $(this).setStyle({'color':'black','backgroundColor':'white'});
}

// TODO проверить заполненность поля
function validator_deny_empty(){
   alert(this.value);
}

// сделать валидатор даты на JavaScript
function validator_require_date(){
   //alert("validator_require_date:"+this.value);
   //var ms=Date.parse(stringDate);
   var isValid=checkdate(this.value);

   //if(isValid) {var date=new Date(ms); alert(date);}

   // индикация ошибки
   if(isValid) $(this).setStyle({'color':'black','backgroundColor':'white'});
   else $(this).setStyle({'color':'white','backgroundColor':'red'});
}

function checkdate(strDate) {

    var pattern=[
        {regexp:/^(\d{4})-(\d{2})-(\d{2})'T'(\d{2}):(\d{2}):(\d{2})$/,year:1,month:2,day:3,hour:4,minute:5,second:6}

       ,{regexp:/^(\d{4})-(\d{2})-(\d{2})\s+(\d{2}):(\d{2}):(\d{2})$/,year:1,month:2,day:3,hour:4,minute:5,second:6}
       ,{regexp:/^(\d{4})-(\d{2})-(\d{2})\s+(\d{2}):(\d{2})/,year:1,month:2,day:3,hour:4,minute:5}

       ,{regexp:/^(\d{2}):(\d{2}):(\d{2})\s+(\d{4})-(\d{2})-(\d{2})$/,year:4,month:5,day:6,hour:1,minute:2,second:3}
       ,{regexp:/^(\d{2}):(\d{2})\s+(\d{4})-(\d{2})-(\d{2})$/,year:3,month:4,day:5,hour:1,minute:2}

       ,{regexp:/^(\d{2}):(\d{2}):(\d{2})\s+(\d{2})\.(\d{2})\.(\d{4})$/,year:6,month:5,day:4,hour:1,minute:2,second:3}
       ,{regexp:/^(\d{2}):(\d{2})\s+(\d{2})\.(\d{2})\.(\d{4})$/,year:5,month:4,day:3,hour:1,minute:2}

       ,{regexp:/^(\d{2}):(\d{2}):(\d{2})\s+(\d{2})\.(\d{2})\.(\d{2})$/,year:6,month:5,day:4,hour:1,minute:2,second:3}
       ,{regexp:/^(\d{2}):(\d{2})\s+(\d{2})\.(\d{2})\.(\d{2})$/,year:5,month:4,day:3,hour:1,minute:2}

       ,{regexp:/^(\d{2})\.(\d{2})\.(\d{2})\s+(\d{2}):(\d{2}):(\d{2})$/,year:3,month:2,day:1,hour:4,minute:5,second:6}
       ,{regexp:/^(\d{2})\.(\d{2})\.(\d{2})\s+(\d{2}):(\d{2})$/,year:3,month:2,day:1,hour:4,minute:5}

       ,{regexp:/^(\d{2})\.(\d{2})\.(\d{4})\s+(\d{2}):(\d{2}):(\d{2})$/,year:3,month:2,day:1,hour:4,minute:5,second:6}
       ,{regexp:/^(\d{2})\.(\d{2})\.(\d{4})\s+(\d{2}):(\d{2})$/,year:3,month:2,day:1,hour:4,minute:5}

       ,{regexp:/^(\d{2})\.(\d{2})\.(\d{4})$/,year:3,month:2,day:1}
       ,{regexp:/^(\d{2})\.(\d{2})\.(\d{2})$/,year:3,month:2,day:1}
       ,{regexp:/^(\d{4})-(\d{2})-(\d{2})$/,year:1,month:2,day:3}

       ,{regexp:/^(\d{2})\/(\d{2})\/(\d{4})\s+(\d{2}):(\d{2}):(\d{2})$/,year:3,month:1,day:2,hour:4,minute:5,second:6}
       ,{regexp:/^(\d{2})\/(\d{2})\/(\d{4})\s+(\d{2}):(\d{2})$/,year:3,month:1,day:2,hour:4,minute:5}
       ,{regexp:/^(\d{2})\/(\d{2})\/(\d{4})$/,year:3,month:1,day:2}

       ,{regexp:/^(\d{2})\/(\d{2})\/(\d{2})\s+(\d{2}):(\d{2}):(\d{2})$/,year:3,month:1,day:2,hour:4,minute:5,second:6}
       ,{regexp:/^(\d{2})\/(\d{2})\/(\d{2})\s+(\d{2}):(\d{2})$/,year:3,month:1,day:2,hour:4,minute:5}
       ,{regexp:/^(\d{2})\/(\d{2})\/(\d{2})$/,year:3,month:1,day:2}
    ];
    var str=strDate.replace(/^\s+|\s+$/,'');
    for(var i=0;i<pattern.length;i++){
        if(matches=strDate.match(pattern[i].regexp)){
            var year=matches[pattern[i].year];
            var month=matches[pattern[i].month];
            var day=matches[pattern[i].day];
            var hour=(pattern[i].hour)?matches[pattern[i].hour]:00;
            var minute=(pattern[i].minute)?matches[pattern[i].minute]:00;
            var second=(pattern[i].second)?matches[pattern[i].second]:00;
            return {'year':year,'month':month,'day':day,'hour':hour,'minute':minute,'second':second};
        }
    }
    return false;
}




// ================== типичные валидаторы = конец ==============================
// создаём список конструкторов узлов

// узел типа AND
filterNodeType['NodeAND']=function(){var tmp=new NodeList('AND','NodeAND');for(var i in tmp) this[i]=tmp[i];};
filterNodeTypeLabel['NodeAND']='AND...';

// узел типа OR
filterNodeType['NodeOR']=function(){var tmp=new NodeList('OR','NodeOR');for(var i in tmp) this[i]=tmp[i];};
filterNodeTypeLabel['NodeOR']='OR...';

// узел типа NOT
filterNodeType['NodeNOT']=NodeNOT;
filterNodeTypeLabel['NodeNOT']='NOT...';







Event.observe(window, 'load', function() {
  rootNode=new NodeRoot();
  $('filtergui').insert(rootNode.DOMElement);
  var str=$('filterJSON').value;
  if(str) rootNode.unserialize(str);
  $('filterJSON').up('form').observe('submit',serializeTree);
});




// это примеры узлов с элементарными условиями
//filterNodeType['NodeAgeGT']=function(){var tmp=new NodeCondition('Age &gt;','NodeAgeGT','');for(var i in tmp) this[i]=tmp[i];};
//filterNodeType['NodeAgeLT']=function(){var tmp=new NodeCondition('Age &lt;','NodeAgeLT','');   for(var i in tmp) this[i]=tmp[i];};
//
//
//
//
//  Это код HTML, с которым работает этот JavaScript
//
//<div style='position:relative;'>
//   <input type="text" name="filterJSON" id="filterjson" t:type="TextField" t:id="filterJSON"/>
//   <div id="filtergui"></div>
//   <div id="filterNodeType" style='display:none;'></div>
//</div>