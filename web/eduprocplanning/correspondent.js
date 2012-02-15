$(document).ready(function(){
    $(".button").button();
    $("#toggleEduPlan").click(function(){
        $("#master-data").toggle("fold", {}, 1000);
    })
    var number=0;  
  
    
    
    function setCoursesData(data){
        var d = data.split(",");
        var result;
        result="<td><table><tr>";
        for (var i=0; i<d.length;i++){
            number++;
            result+="<td class='date-container'><input class=\"sessia_cell\" type=\"text\" id='cell_"+number+"' value='"+d[i]+"'></input></td>";
        }
        result+="</tr></table></td>";

        return result;
    }
     
    function getElements(){
        for (var i = 1 ; i<number; i+=2){
            getDatePickerRange(i, i+1);
        }
    }
    
    function getDatePickerRange(first, second){
        var dates = $("#cell_"+first+", #cell_"+second).datepicker({
            changeMonth: true,
            numberOfMonths: 3,
            dateFormat: "dd.mm.yy",
            onSelect: function( selectedDate ) {
                var option = this.id == "cell_"+first ? "minDate" : "maxDate",
                instance = $( this ).data( "datepicker" ),
                date = $.datepicker.parseDate(instance.settings.dateFormat||
                    $.datepicker._defaults.dateFormat,
                    selectedDate, instance.settings );
                dates.not( this ).datepicker( "option", option, date );
                sumDates();
            }
        });
    }
    
    function sumDates(){
        var j=1;
        jQuery("#days1").html("0")
        for (var i = 1 ; i<number; i+=2){
            if (i==9) {
                j=2;
                jQuery("#days2").html("0")
            }
            if (i==17) {
                j=3;
                jQuery("#days3").html("0")
            }
            if (i==25) {
                j=4;
                jQuery("#days4").html("0")
            }
            var date = getDate(jQuery("#cell_"+i).val(),jQuery("#cell_"+(i+1)).val());
            if (isNaN(date)){
                jQuery("#days"+j).html(parseInt(jQuery("#days"+j).html()));  
            }else{
                jQuery("#days"+j).html(parseInt(parseInt(jQuery("#days"+j).html())+date));
            }                
        }
    }
    
    function getDate(date1,date2){
        _p_st = date1.split('.');
        _p_fn = date2.split('.');
        date1 = new Date(_p_st[2], _p_st[1]-1, _p_st[0]);
        date2 = new Date(_p_fn[2], _p_fn[1]-1, _p_fn[0]);
        _diff = Math.abs(date1.getTime() - date2.getTime())/(24*60*60*1000);
        return _diff;
    }
     
    var courseData= new Array(4);
    courseData[0]= "<td>1</td>"+setCoursesData("10.11.2011,19.11.2011")+setCoursesData("10.01.2012,22.01.2012")+setCoursesData("05.05.2012,11.05.2012")+setCoursesData("01.06.2012,11.06.2012")+"<td id='days1'>0</td>";  
    courseData[1]= "<td>2</td>"+setCoursesData("11.11.2012,14.11.2012")+setCoursesData("13.01.2014,22.01.2014")+setCoursesData("08.05.2014,13.05.2014")+setCoursesData("01.06.2014,11.06.2014")+"<td id='days2'>0</td>";
    courseData[2]= "<td>3</td>"+setCoursesData("09.11.2013,18.11.2013")+setCoursesData("16.01.2015,24.01.2015")+setCoursesData("01.05.2015,14.05.2015")+setCoursesData("01.06.2015,11.06.2015")+"<td id='days3'>0</td>"; 
    courseData[3]= "<td>4</td>"+setCoursesData("06.11.2014,25.11.2014")+setCoursesData("19.01.2016,29.01.2016")+setCoursesData("02.05.2016,12.05.2016")+setCoursesData("01.06.2016,11.06.2016")+"<td id='days4'>0</td>";  
    $("#course-data-first").html(courseData[0]);
    $("#course-data-second").html(courseData[1]);
    $("#course-data-thirth").html(courseData[2]);
    $("#course-data-fourth").html(courseData[3]);
    getElements(); 
    sumDates();
});