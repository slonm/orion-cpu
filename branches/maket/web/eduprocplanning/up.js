$(document).ready(function(){
    var number=0;  
    
    function setCoursesData(data){
        var d = data.split(",");
        var result;
        result="<td><table><tr>";
        for (var i=0; i<d.length;i++){
            number++;
            result+="<td id=\"cell_"+number+"\" class=\"semestr_cell\">"+d[i]+"</td>";
        }
        result+="</tr></table></td>";
        return result;
    }
                  
    var courseData= new Array(4);
    courseData[0]= "<td>1</td>"+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,+,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,e")+setCoursesData("е,е,к,к,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,е,е")+setCoursesData("е,к,к,к")+setCoursesData("к,к,к,к")+setCoursesData("к,к,к,к,к")+"<td id=\"course_1_tn\"></td><td id=\"course_1_es\"></td><td  id=\"course_1_p\"></td><td id=\"course_1_p\"></td><td></td><td></td><td></td>";  
    courseData[1]= "<td>2</td>"+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,+,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,e")+setCoursesData("е,е,к,к,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,е,е")+setCoursesData("е,т,т,т")+setCoursesData("к,к,к,к")+setCoursesData("к,к,к,к,к")+"<td></td><td></td><td></td><td></td><td></td><td></td><td></td>";   
    courseData[2]= "<td>3</td>"+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,+,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,e")+setCoursesData("е,е,к,к,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,е,е")+setCoursesData("е,в,в,в")+setCoursesData("к,к,к,к")+setCoursesData("к,к,к,к,к")+"<td></td><td></td><td></td><td></td><td></td><td></td><td></td>";  
    courseData[3]= "<td>4</td>"+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,+,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,e")+setCoursesData("е,е,к,к,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,+")+setCoursesData("+,+,+,е,е")+setCoursesData("е,к,к,-")+setCoursesData("-,-,-,-")+setCoursesData("-,-,-,-,-")+"<td></td><td></td><td></td><td></td><td></td><td></td><td></td>";  
    $("#course-data-first").html(courseData[0]);
    $("#course-data-second").html(courseData[1]);
    $("#course-data-thirth").html(courseData[2]);
    $("#course-data-fourth").html(courseData[3]);
    
    $('#master-table').selectable({ 
        filter: ".semestr_cell"
    })
    
    $(".button").button();
    
    $("#getIds").click(function(){
        var result=""; 
        $( ".ui-selected" ).each(function() {
            var index = $(this).attr("id");
            result+=index+"; ";
        });
        createDialog(result);
    })
    
    function setCell(data){
        $( ".ui-selected" ).each(function() {
            var index = $(this).text(data);
        });
        updateLimits();
    };
    
    function updateLimits(){
        $("#course_1_tn").text($('#course-data-first td:contains("к")').size())
        $("#course_1_es").text($('#course-data-first td:contains("е")').size())
    }
    
    $("#setK").click(function(){
        setCell("к");
    })
    
    $("#setT").click(function(){
        setCell("+");
    })
    
    $("#setE").click(function(){
        setCell("е");
    })
    
    $("#setTP").click(function(){
        setCell("т");
    })
    
    $("#setVP").click(function(){
        setCell("в");
    })
  
    $("#setDP").click(function(){
        setCell("д");
    })
    
    $("#setDZ").click(function(){
        setCell("д/з");
    })
    
  
    $( "#dialog-modal" ).dialog({
        autoOpen: false
    });
    
    $("#toggleEduPlan").click(function(){
        $("#master-data").toggle("fold", {}, 1000);
    })
  
    function createDialog(html){
        $( "#dialog:ui-dialog" ).dialog( "destroy" );

        $( "#dialog-modal" ).html(html);
        
        $( "#dialog-modal" ).dialog({
            height: 140,
            modal: true,
            autoOpen: true
        });
    }
    
    $(".choosen-discipline-list").html("<option>Оберіть дисципліну...</option><option>Соціологія</option><option>Психологія</option><option>Етика і естетика</option><option>Релігієзнавство</option>");
    $(".choosen-discipline-list").change(function(){
        $(this).parent().find("a").after("<div><button class=\"ui-button save-discipline\">OK</button></div>");
        initializeButtons();
        $(".save-discipline").click(function(){
            $(this).fadeOut("slow").parent().html("Сохранено!").fadeOut("slow");  
        })
    }) 
//    $(".kind-status").hover(function(){
//        jQuery(this).addClass("ui-state-highlight")
//        },function(){
//        jQuery(this).removeClass("ui-state-highlight")
//        })


//    $(".kind-status").click(function(){
//        if ($(this).hasClass("kind-closed")){
//            $(this).attr("class","kind-opened kind-status").find("div").show("slow");
//        } else if ($(this).hasClass("kind-opened")){
//            $(this).attr("class","kind-closed kind-status").find("div").hide("slow");
//        }
//    })    
//    initializeUIComponents();
})


