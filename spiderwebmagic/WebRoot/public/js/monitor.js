/**
 * 生意参谋--市场行情--
 */
var timeout;
var start=0;
var oldlist=[];
var $templates
getUrlList();
function getUrlList(){
	$.ajax({
		url:"list",
		type:"GET",
		dataType:"json",
		success:function(data){
			var arrTr="";
			if(start==0){
				$.each(data,function(i,item){
					oldlist.push(item.key.slice(0, item.key.lastIndexOf(",")));
	//				console.log(item);
	                arrTr+=createTr(i+1,item);
				});
				//主列表添加列
				if(arrTr!=""){
				   $("#mainTable tbody").empty().append(arrTr);
				}
				start=1;
		    }else if(start==1){
//		    	 console.log(oldlist);
		    	var newlist=[];
		    	$.each(data,function(i,item){
		    		newlist.push(item.key.slice(0, item.key.lastIndexOf(",")));
		    	});
		    	
		    	$.each(oldlist,function(i,value){
//		    		console.log(value);	
		    		if($.inArray(value,newlist)==-1){
		    			$("#mainTable tbody tr[data-key^='"+value+"']").remove();
		    		}
		    	});
		    	$.each(data,function(i,item){
                    
                     var key=item.key;
                     var keystart=key.slice(0, key.lastIndexOf(","));
                     // console.log(keystart);
                    if($.inArray(keystart,oldlist)!=-1){
//                       $("#mainTable tbody tr[data-key='"++"']") 
                        
                        var itemArr=key.split(",");
						var recordcount=itemArr[5];
						var faildcount=item.faildcount;
						var status=itemArr[7];
						var successcount=recordcount-faildcount;
						//进度条progress-bar class
						var progressbarclass="";
						var successRate=Math.round(successcount/recordcount*10000)/100.00;
						if(successRate<25){
							progressbarclass="progress-bar-danger";
						}else if(successRate>=25&&successRate<50){
							progressbarclass="progress-bar-warning";
						}else if(successRate>=50&&successRate<75){
							progressbarclass="progress-bar-info";
						}else if(successRate>=75&&successRate<=100){
							progressbarclass="progress-bar-success";
						}
                        $itemTr=$("#mainTable tbody tr[data-key^='"+keystart+"']");
                        $itemTr.attr("data-key",key);
                        $itemTr.find("td:eq(6) .progress-bar").
                        css("width",successRate+"%").removeClass(function(index, css){return (css.match (/\bprogress-bar-\S+/g) || []).join(' ');})
                        .addClass(progressbarclass).find("span").html(successcount).parent().next().html(faildcount);
                        
                        $itemTr.find("td:eq(7)").html(status=="active"?"正在执行":"执行完成");
						if(status=="active"){
						  	 $itemTr.find("td:eq(8) button").addClass("disabled");
						}else{
							 $itemTr.find("td:eq(8) button").removeClass("disabled");
						}
                   	}else{
						 $("#mainTable tbody").append(createTr($("#mainTable tbody tr").size()+1,item));
                    }
		    		// console.log(item);
		    	});
		    	
		    	oldlist=[];
		    	$.each(data,function(i,item){
		    		oldlist.push(item.key.slice(0, item.key.lastIndexOf(",")));
		    	});
		    	$("#mainTable tbody tr").each(function(i, el) {
					  $(this).find('td:eq(0)').html(i+1);
				});
		    }
			
		}
	});
//	timeout=window.set("getUrlList()", 3000);
	timeout=window.setTimeout("getUrlList()", 1000);
}

function createTr(i,item){
	var $templatetr=$("#demotr").clone();
	$templatetr.show();
	var itemArr=item.key.split(",");
	var accountname=itemArr[1];
	var executecycle=itemArr[4];
	var processordesc=itemArr[3];
	var recordcount=itemArr[5];
	
	var faildcount=item.faildcount;
	var startdate=itemArr[6];
	var status=itemArr[7];
//				console.log(status);
	var successcount=recordcount-faildcount;
	//进度条progress-bar class
	var progressbarclass="";
	var successRate=Math.round(successcount/recordcount*10000)/100.00;
	if(successRate<25){
		progressbarclass="progress-bar-danger";
	}else if(successRate>=25&&successRate<50){
		progressbarclass="progress-bar-warning";
	}else if(successRate>=50&&successRate<75){
		progressbarclass="progress-bar-info";
	}else if(successRate>=75&&successRate<=100){
		progressbarclass="progress-bar-success";
	}
	
	$templatetr.attr("data-key",item.key);
    $templatetr.find("td:eq(0)").html(i);
    $templatetr.find("td:eq(1)").html(accountname);	
    $templatetr.find("td:eq(2)").html(processordesc);	
    $templatetr.find("td:eq(3)").html(startdate);
    $templatetr.find("td:eq(4)").html(executecycle);
    $templatetr.find("td:eq(5)").html(recordcount);
    $templatetr.find("td:eq(6) .progress-bar").css("width",successRate+"%").addClass(progressbarclass).find("span").html(successcount).parent().next().html(faildcount);
    $templatetr.find("td:eq(7)").html(status=="active"?"正在执行":"执行完成");
    if(status=="active"){
    	$templatetr.find("td:eq(8) button").addClass("disabled");
    }
    return $templatetr.get(0).outerHTML;
}
//url列表点击详细展示
function showModel(object){
	var key=$(object).parents("tr").attr("data-key");
    $.ajax({
	   url:"detail",
	   data:{"key":key},
	   type:"post",
	   dataType:"json",
	   success:function(data){
		   var trStr="";
			 $.each(data,function(i,item){
				 trStr +="<tr><td><span title='"+item.targetUrl+"'>"+item.targetUrl+"</span></td><td>cookie失效</td></tr>";
			 });
		 $(".modal-body table tbody").empty().append(trStr);
	   }
   }); 
}
//重试按钮
function retry(object){
	var $tr=$(object).parents("tr:eq(0)");
	var key=$tr.attr("data-key");
	var $button=$(object);
	$button.addClass("disabled");
	$.ajax({
		url:"startDataGrap",
		type:"post",
		data:{"key":key},
		dataType:"text",
		success:function(data){
		    	$button.removeClass("disabled");
		 }
	});
}
//忽略按钮
function ignore(object){
	var $tr=$(object).parents("tr:eq(0)");
	var key=$tr.attr("data-key");
	$.ajax({
		url:"deleDataGrap",
		type:"post",
		data:{"key":key},
		dataType:"text",
		success:function(data){
			if(data=="success"){
				$tr.remove();
				$("#mainTable tbody tr").each(function(i, el) {
					  $(this).find('td:eq(0)').html(i+1);
				});
				
			}
		 }
	});
}
