var nodeList={};
var rootFilterNode;
Event.observe(window, 'load', function() {
    filterElementBuilderConfig["AND"]={
        "guitype":"AND" ,
        "uid":"",
        "typeName":"AND",
        "isactive":"1",
        "validator":"",
        "value":"",
        "label":"AND"
    };
    filterElementBuilderConfig["OR"] ={
        "guitype":"OR"  ,
        "uid":"",
        "typeName":"OR" ,
        "isactive":"1",
        "validator":"",
        "value":"",
        "label":"OR"
    };


    // link to create root filter element
    $('filterCreateNew').observe('click',function(){
        $('filterrootchildren').update();
        addSubElementTo('filterroot');
    });

    // draw filter tree
    var value=$("gridFilterJSON").value;
    if(value){
        eval("var oldFilterTree="+value+";");
        if(oldFilterTree && oldFilterTree.guitype){
            var nd=new ElementBuilder[oldFilterTree.guitype].create(oldFilterTree,'filterrootchildren');
            nodeList[nd.uid]=nd;
        }
    }
});

// ======================== element buiders = begin ============================
var ElementBuilder={
    "TEXT":{
        "create":TextElementCreate
    },
    "CHECKBOX":{
        "create":CheckboxElementCreate
    },
    "DATE":{
        "create":DateElementCreate
    },
    "AND":{
        "create":ListElementCreate
    },
    "OR":{
        "create":ListElementCreate
    }
};



function exportFilterJSON(){
    var children=$('filterrootchildren').childElements();
    if(children && children.length>0){
        var rootId=children.first().identify().replace(/container$/,'' );
        $('gridFilterJSON').value=Object.toJSON(nodeList[rootId]);
    }else{
        $('gridFilterJSON').value="{}";
    }
    $('gridFilterJSON').fire("filtermodel:changed");
}


//
// create textfield object
//
function TextElementCreate(config, parentId){
    // element type name
    // config.typeName
    this.typeName=config.typeName;

    // config.uid
    // identifier of the element
    this.uid=this.typeName+getUID();

    // config.isactive
    // if element is active
    this.isactive=config.isactive;

    // config.value
    // filter value
    this.value=config.value;

    // config.typeName
    this.guitype=config.guitype;

    // config.validator
    this.validator=config.validator;

    // config.label
    this.label=config.label;

    // parent identifier
    this.parentId=parentId.replace(/children$/,'');




    // create div inside element parentId
    var containerId=this.uid+'container';
    var container=new Element('div',{
        'id':containerId,
        'class':'filterElementContainer'
    });

    var checkboxID=this.uid+'isActiveCheckbox';
    var textfieldID=this.uid+'text';
    var currentObject=this;
    var onupdate=function(){
        currentObject.value=$(textfieldID).getValue();
        currentObject.isactive=$(checkboxID).checked;
        exportFilterJSON();
    // console.log(currentObject);
    }
    // create "is Active" checkbox
    var nodeIsActive=new Element('input',{
        'type':'checkbox',
        'id':this.uid+'isActiveCheckbox'
    });
    if(this.isactive){
        nodeIsActive.writeAttribute('checked',true);
    }
    nodeIsActive.observe('click',onupdate);
    container.insert(nodeIsActive);

    // create label
    var label=new Element('label',{
        'id':this.uid+'label'
    });
    label.insert(config.label);
    container.insert(label);

    // create text field
    var textfield=new Element('input',{
        'type':'text',
        'id':textfieldID,
        'value':(config.value?config.value:'')
    });
    textfield.observe('keyup',onupdate);
    container.insert(textfield);



    // function to remove node
    this.demontage=function(){
        // delete current object
        delete(nodeList[currentObject.uid]);

        // delete DOM node
        if($(containerId)) container.remove();

        // delete this object from parent node
        try { 
          delete(nodeList[currentObject.parentId].children[currentObject.uid]);
        }catch (theException) { }
        

        // reexport filter tree
        exportFilterJSON();
    };

    // link to delete element
    var deleteLink=new Element('a',{'href':'javascript:void(0)'});
    deleteLink.update('del');
    deleteLink.observe('click',this.demontage);
    container.insert(deleteLink);


    // get parent node object
    $(parentId).insert(container);

}









//
// create checkbox object
//
function CheckboxElementCreate(config, parentId){

    // element type name
    // config.typeName
    this.typeName=config.typeName;

    // config.uid
    // identifier of the element
    this.uid=this.typeName+getUID();

    // config.isactive
    // if element is active
    this.isactive=config.isactive;

    // config.value
    // filter value
    this.value=config.value;

    // config.typeName
    this.guitype=config.guitype;

    // config.validator
    this.validator=config.validator;

    // config.label
    this.label=config.label;

    // parent identifier
    this.parentId=parentId.replace(/children$/,'');


    // create div inside element parentId
    var containerId=this.uid+'container';
    var container=new Element('div',{
        'id':containerId,
        'class':'filterElementContainer'
    });

    var checkboxID=this.uid+'isActiveCheckbox';
    var textfieldID=this.uid+'text';
    var currentObject=this;
    var onupdate=function(){
        // currentObject.value=$(textfieldID).getValue();
        currentObject.isactive=$(checkboxID).checked;
        exportFilterJSON();
    //console.log(currentObject);
    //re-create JSON here
    }
    // create "is Active" checkbox
    var nodeIsActive=new Element('input',{
        'type':'checkbox',
        'id':this.uid+'isActiveCheckbox'
    });
    if(this.isactive){
        nodeIsActive.writeAttribute('checked',true);
    }
    nodeIsActive.observe('click',onupdate);
    container.insert(nodeIsActive);

    // create label
    var label=new Element('label',{
        'id':this.uid+'label'
    });
    label.insert(config.label);
    container.insert(label);

    // link to delete element
    // function to remove node
    this.demontage=function(){
        // delete current object
        delete(nodeList[currentObject.uid]);

        // delete DOM node
        if($(containerId)){
             container.remove();
        }

        // delete this object from parent node
        try { 
          delete(nodeList[currentObject.parentId].children[currentObject.uid]);
        }catch (theException) { }

        // reexport filter tree
        exportFilterJSON();
    };
    var deleteLink=new Element('a',{
        'href':'javascript:void(0)'
    });
    deleteLink.update('del');
    deleteLink.observe('click',this.demontage);
    container.insert(deleteLink);

    // get parent node object
    $(parentId).insert(container);


}






//
// create date selector
//
function DateElementCreate(config, parentId){

    // element type name
    // config.typeName
    this.typeName=config.typeName;

    // config.uid
    // identifier of the element
    this.uid=this.typeName+getUID();

    // config.isactive
    // if element is active
    this.isactive=config.isactive;

    // config.value
    // filter value
    this.value=config.value;

    // config.typeName
    this.guitype=config.guitype;

    // config.validator
    this.validator=config.validator;

    // config.label
    this.label=config.label;

    // parent identifier
    this.parentId=parentId.replace(/children$/,'');

    // create div inside element parentId
    var containerId=this.uid+'container';
    var container=new Element('div',{
        'id':containerId,
        'class':'filterElementContainer'
    });

    var checkboxID=this.uid+'isActiveCheckbox';
    var textfieldID=this.uid+'text';
    var currentObject=this;
    var onupdate=function(){
        currentObject.value=$(textfieldID).getValue();
        currentObject.isactive=$(checkboxID).checked;
        exportFilterJSON();
    //console.log(currentObject);
    //re-create JSON here
    }
    // create "is Active" checkbox
    var nodeIsActive=new Element('input',{
        'type':'checkbox',
        'id':this.uid+'isActiveCheckbox'
    });
    if(this.isactive){
        nodeIsActive.writeAttribute('checked',true);
    }
    nodeIsActive.observe('click',onupdate);
    container.insert(nodeIsActive);

    // create label
    var label=new Element('label',{
        'id':this.uid+'label'
    });
    label.insert(config.label);
    container.insert(label);

    // create text field
    var textfield=new Element('input',{
        'type':'text',
        'id':textfieldID,
        'value':this.value
    });
    textfield.observe('keyup',onupdate);
    textfield.observe('change',onupdate);
    container.insert(textfield);



    // идентификаторы для календаря
    var calendarContainerId=this.uid+'calendarContainer';
    var calendarButtonId=this.uid+'button';

    
    // закрываем календарь при нажатии мимо контейнера для календаря
    var calendar_closer=function(event){
        var elt = $(Event.element(event));
        if(elt.up('#'+calendarContainerId)) return true;
        if(elt.identify()==calendarContainerId) return true;
        elt = $(Event.element(event));
        if(elt.identify()==calendarButtonId) return true;
        var cc=$(calendarContainerId);
        if(cc){
            cc.update('&nbsp;').hide();
        }
        return false;
    }
    Event.observe(window, 'click', calendar_closer);

    // function to remove node
    this.demontage=function(){
        Event.stopObserving(window, 'click', calendar_closer);

        // delete current object
        delete(nodeList[currentObject.uid]);

        // delete DOM node
        if($(containerId)) container.remove();

        // delete this object from parent node
        try { 
          delete(nodeList[currentObject.parentId].children[currentObject.uid]);
        }catch (theException) { }

        // reexport filter tree
        exportFilterJSON();
    };
    // link to delete element
    var deleteLink=new Element('a',{
        'href':'javascript:void(0)'
    });
    deleteLink.update('del');
    deleteLink.observe('click',this.demontage);
    container.insert(deleteLink);

    // get parent node object
    $(parentId).insert(container);

    new Calendar(textfieldID, onupdate);
}




// =========================== узел типа "список узлов" = начало ===============
function ListElementCreate(config, parentId){


    // element type name
    // config.typeName
    this.typeName=config.typeName;

    // config.uid
    // identifier of the element
    this.uid=this.typeName+getUID();

    // config.isactive
    // if element is active
    this.isactive=config.isactive;

    // config.value
    // filter value
    this.value=config.value;

    // config.typeName
    this.guitype=config.guitype;

    // config.validator
    this.validator=config.validator;

    // config.label
    this.label=config.label;

    // parent identifier
    this.parentId=parentId.replace(/children$/,'');

    // children
    if(config.children){
        this.children=config.children;
    }else{
        this.children={};
    }
    
    // create div inside element parentId
    var containerId=this.uid+'container';
    var container=new Element('div',{
        'id':containerId,
        'class':'filterElementContainer'
    });
    var uid=this.uid;
    var checkboxID=this.uid+'isActiveCheckbox';
    var textfieldID=this.uid+'text';
    var childrenContainerId=this.uid+"children";
    var currentObject=this;

    var onupdate=function(){
        // currentObject.value=$(textfieldID).getValue();
        currentObject.isactive=$(checkboxID).checked;
        exportFilterJSON();
    // console.log(currentObject);
    //re-create JSON here
    }
    // create "is Active" checkbox
    var nodeIsActive=new Element('input',{
        'type':'checkbox',
        'id':this.uid+'isActiveCheckbox'
        });
    if(this.isactive){
        nodeIsActive.writeAttribute('checked',true);
    }
    nodeIsActive.observe('click',onupdate);
    container.insert(nodeIsActive);

    // create label
    var label=new Element('label',{
        'id':this.uid+'label'
    });
    label.insert(config.label);
    container.insert(label);

    // link to add new element
    var addElementLink=new Element('a',{
        'href':'javascript:void(0)'
    });
    addElementLink.update('add');
    addElementLink.observe('click',function(){
        addSubElementTo(uid);
        return false;
    });
    container.insert(addElementLink);


    // function to remove node
    this.demontage=function(){
        // remove children
        if(currentObject.children){
            Object.values(currentObject.children).each(function(nd){
                nd.demontage();
            });
        }
        // delete current object
        delete(nodeList[currentObject.uid]);

        // delete DOM node
        if($(containerId)) container.remove();

        // delete this object from parent node
        try { 
            delete(nodeList[currentObject.parentId].children[currentObject.uid]);
        }catch (theException) { }
        

        // reexport filter tree
        exportFilterJSON();
    };
    // link to delete element
    var deleteLink=new Element('a',{
        'href':'javascript:void(0)'
    });
    deleteLink.update('del');
    deleteLink.observe('click',this.demontage);
    container.insert(deleteLink);

    // children container
    var childrenContainer=new Element('div',{
        'id':childrenContainerId,
        'class':'filterElementContainer'
    });
    container.insert(childrenContainer);

    // get parent node object
    $(parentId).insert(container);

    // function to get children
    this.getChildren=function(){
        var childUidList=[];
        $(this.uid+'children').childElements().each(function(s, index) {
            childUidList[childUidList.length]= s.identify().replace(/container$/,'');
        });
        this.children={};
        for(var j=0;j<childUidList.length;j++){
            if(nodeList[childUidList[j]]){
                this.children[childUidList[j]]=nodeList[childUidList[j]];
            }
        }
    }



    // create children
    if(this.children){
        Object.values(this.children).each(function(cnf){
            var nd=new ElementBuilder[cnf.guitype].create(cnf,childrenContainerId);
            nodeList[nd.uid]=nd;
        });
        this.getChildren();
    }


}


//
//  блок выбора под-узла
//
function addSubElementTo(nodeId){
    var childrenContainerId=nodeId+"children";
    //console.log('add subelement to '+childrenContainerId);

    // draw element selector
    var variantsContainerId=childrenContainerId+"variants";
    var variantsContainer=new Element('div',{
        'id':variantsContainerId,
        'class':'childrenvariants'
    })
    var node;

    node=new Element('a',{
        'href':"javascript:void(0)",
        'style':'display:block; float:right;width:20px;padding:3px;background-color:red;color:white;text-decoration:none;text-align:center;font-weight:bold;'
    });
    node.insert('&times;');
    node.observe('click',function(){
        $(variantsContainerId).remove();
    });
    variantsContainer.insert(node);


    var cnf=[];
    var currentNode=nodeId;
    // sort element names
    var names=Object.keys(filterElementBuilderConfig);
    names.sort();
    // draw elements;
    var element;

    for(var n=0;n<names.length;n++){
        element=filterElementBuilderConfig[names[n]];
        if(ElementBuilder[element.guitype]){
            node=new Element('a',{
                'href':"javascript:void(0)",
                'class':'childrenvariant',
                'id':names[n]+'_creator'
            });
            node.insert(element.label);
            node.observe('click',function(){
                var creator=this.id.replace(/_creator$/,'');
                var cnf=filterElementBuilderConfig[creator];
                var nd=new ElementBuilder[cnf.guitype].create(cnf,childrenContainerId);
                nodeList[nd.uid]=nd;
                if(nodeList[currentNode]){
                    nodeList[currentNode].getChildren();
                }
                $(variantsContainerId).remove();
                exportFilterJSON();
                return false;
            });
            variantsContainer.insert(node);
        }
    }
    $(childrenContainerId).insert({
        top:variantsContainer
    });
}
// =========================== узел типа "список узлов" = конец ================

// ======================== element buiders = end ==============================

// ======================== utilities = begin ==================================
var uid=0;
function getUID(){
    return ++uid;
}
// ======================== utilities = end ====================================
/*

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
*/