/*
   author: Gennadiy Dobrovolsky
   date:   September 27, 2006
*/
var month_names=["Січень","Лютий","Березень","Квітень","Травень","Червень","Липень","Серпень","Вересень","Жовтень","Листопад","Грудень"];
var weekday_names=['Нд','Пн','Вт','Ср','Чт','Пт','Сб'];
/**
 * requires prototype.js
 * draw calendar
 * input parameters
 *   blockId - identifier of the text field
 *   onDateSelected - function to handle clicks
 */

function Calendar(textfieldId, onDateSelected){
    var calendarObject=this;

    this.textfieldId=textfieldId;
    this.onDateSelected=onDateSelected;

    // get current date
    var now=new Date();
    this.month_today = now.getMonth()+1;
    this.year_today  = now.getYear();
    if(this.year_today<1000) this.year_today+=1900;
    // create button
    var button=new Element('input',{'type':'button','value':'...', 'id':textfieldId+'button'});
    button.observe('click',function(){
      if(!calendarObject.calendarContainer){
	 var textfield=$(textfieldId);
	 var position=textfield.cumulativeOffset();
	 var left=position[0];
	 var top=position[1]+textfield.getHeight();
	 calendarObject.calendarContainer=new Element('span',{'class':'calendarContainer', 'id':textfieldId+'calendarContainer','style':'display:inline-block; border:1px solid blue; background-color:white; z-index:1000;position:absolute;top:'+top+'px;left:'+left+'px;'});
	 button.insert({'after':calendarObject.calendarContainer});
      }
      calendarObject.setDate(calendarObject.month_today,calendarObject.year_today);
    });
    $(textfieldId).insert({'after':button});


    this.setDate=function(month, year){
      this.calendarContainer.update();
      this.calendarContainer.insert(this.getCalendar(month, year));
    }
    this.getCalendar=function(month, year){


	 var row, cell, table;

	 table=new Element('table',{'class':'grid-calendar','border':'0px'});

	 row=new Element('tr');
	 table.insert(row);
         cell=new Element('td',{'colspan':'7','align':'right','style':'background-color:blue;'});
         row.insert(cell);

	 var link_close=new Element('a',{'href':'javascript:void(0)','style':'display:inline-block;padding:2px;background-color:yellow; color:blue;text-decoration:none;'});
	 link_close.insert('&times;');
	 link_close.observe('click',function(){
	    calendarObject.calendarContainer.update();
	 });
	 cell.insert(link_close);

 	 row=new Element('tr');
	 table.insert(row);

	 cell=new Element('td');
	 row.insert(cell);
	 var prev_year  = year;
	 var prev_month = month-1;
	 if(prev_month==0) {
	    prev_month=12;
	    prev_year--;
	 }
	 var link_prev_month=new Element('a',{'href':'javascript:void(0)','style':'text-decoration:none;','class':'calendar-link'});
	 link_prev_month.insert('&lt;');
	 link_prev_month.observe('click',function(){
	    calendarObject.setDate(prev_month,prev_year);
	 });
	 cell.insert(link_prev_month);

	 cell=new Element('td',{'colspan':'3'});
	 row.insert(cell);
	 cell.insert(month_names[month-1]);

	 cell=new Element('td');
	 row.insert(cell);
	 var next_year  = year;
	 var next_month = month+1;
	 if(next_month==13) {
	    next_month=1;
	    next_year++;
	 }
	 var link_next_month=new Element('a',{'href':'javascript:void(0)','style':'text-decoration:none;','class':'calendar-link'});
	 link_next_month.insert('&gt;');
	 link_next_month.observe('click',function(){
	    calendarObject.setDate(next_month,next_year);
	 });
	 cell.insert(link_next_month);

         cell=new Element('td',{'colspan':'2'});
	 row.insert(cell);

         var link_prev_year=new Element('a',{'href':'javascript:void(0)','style':'text-decoration:none;','class':'calendar-link'});
	 link_prev_year.insert('&lt;');
	 link_prev_year.observe('click',function(){
	    calendarObject.setDate(month,year-1);
	 });
	 cell.insert(link_prev_year);

	 cell.insert(year);

         var link_next_year=new Element('a',{'href':'javascript:void(0)','style':'text-decoration:none;','class':'calendar-link'});
	 link_next_year.insert('&gt;');
	 link_next_year.observe('click',function(){
	    calendarObject.setDate(month,year+1);
	 });
	 cell.insert(link_next_year);

	 row=new Element('tr');
	 table.insert(row);
	 row.insert( (new Element('td')).insert(weekday_names[0]));
	 row.insert( (new Element('td')).insert(weekday_names[1]));
	 row.insert( (new Element('td')).insert(weekday_names[2]));
	 row.insert( (new Element('td')).insert(weekday_names[3]));
	 row.insert( (new Element('td')).insert(weekday_names[4]));
	 row.insert( (new Element('td')).insert(weekday_names[5]));
	 row.insert( (new Element('td')).insert(weekday_names[6]));


         // показує кількість днів у місяці
	 var n_days=new Date(year,month-1,32);
	 n_days=32-n_days.getDate();
	 var fst=new Date(year,month-1,1);
	 var cnt=fst.getDay();
	 var i,j;

	 row=new Element('tr');
	 table.insert(row);
	 for(j=0;j<cnt;j++){
	    row.insert( (new Element('td')).insert('&nbsp;'));
	 }
	 for(i=1;i<=n_days;i++){
	    if(j%7==0) {
		  row=new Element('tr');
	          table.insert(row);
		  j=0;
	    }
	    cell=new Element('td');
	    row.insert(cell);
	    var link=new Element('a',{'href':'javascript:void(0)','class':'calendar-link'});
	    link.insert(i);
	    link.observe('click',function(){
	        var d=this.innerHTML;
		if(d<10) d='0'+d;
		var m=month;
		if(m<10) m='0'+m;

	        $(calendarObject.textfieldId).writeAttribute('value',year+'-'+m+'-'+d);
	        if(onDateSelected) onDateSelected(i,month,year);
	    });
	    cell.insert(link);
	    j++;
	 }
	 for(i=7-j;i>0;i--)  row.insert( (new Element('td')).insert('&nbsp;'));

	 return table;
    }

}
/*
Sample usage:
<html>
<body>
<script src="prototype.js"></script>
<script src="dateselector.js"></script>
<script>
Event.observe(window,'load',function(){
   new Calendar('someDate', function(){
   })
});
</script>
<input type=text id=someDate>
</body>
</html>

*/