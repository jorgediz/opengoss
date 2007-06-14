/**
 * 表单绑定的工具类
 * 现支持的元素只有input select
 * 绑定对象只支持一级嵌套如 
 *              var stock = new Object();
 *			    stock['symbol']= "ALLEN";
 *			    stock['price']=10;
 *			    stock['companyName']="ALLEN INFO";
 *			    stock['user']={
 *				    name:"zf",
 *				    age:25
 *			    };
 * <input name="price" > 绑定到stock['price']上
 * <input name="user.age" > 绑定到 stock['user']['age']上
 * <input name="user.age.xx" > 是不支持的。
 * 
 * ui 的表单控件多样。可以加入自定义的表单解析器
 * 如 binder.resolvers["s1"]= new SelectResolver();
 * 
 * 这里只默认提供几个input,multipleSelect,singleSelect这三个resolver
 * 
 * resolver的数据结构如下
 * function SelectResolver(){
 *   this.tagName="select";   可被解析的tagName
 *   }
 *  根据给定的fieldValue对象，为相应的widget赋值 由bindDataToUI这个方法内调用                     
 *  SelectResolver.prototype.setWidgetValue = function(widget,fieldValue){}
 *  把widget的值赋给相应的data对象,由bindUItoData这个方法内调用
 *  SelectResolver.prototype.setObjectValue = function (widget,data,fieldName){}
 *  判断widget是否为空
 *  SelectResolver.prototype.isEmpty = function (widget){}
 * 
 * 	provided by zhufan
 * 
*/
function FormBinder(def){
	if(def){
		this.fieldDef = def;
	}else{
		this.fieldDef="fieldName";
	}
	
	this.resolvers={
		input : new InputResolver(),
//		multipleSelect : new MultipleSelectResolver(), //默认的multiple 的select的解析器
//		singleSelect: new SelectResolver()
	}
}

FormBinder.prototype.bindDataToUI = function(formId,data)
           {
           	    this.doBinding(formId,data,this.resolveDataToUI);
           }
            
FormBinder.prototype.resolveDataToUI = function (resolver,widget,data,fieldName){
            	if( isNested( fieldName ) ){
             	    var nestedObjectName = fieldName.split(".")[0];
             		if(!data[nestedObjectName]){ //对象无值则返回
             		    return;
             		}
            	}
            	var fieldValue = data.eval(fieldName);
            	if(!(fieldValue==null)){
            		resolver.setWidgetValue(widget,fieldValue);
            	}
            }
            
FormBinder.prototype.doBinding = function (formId,data,resolvePolicy)
           {
            	
            	var formUI = document.getElementById(formId);
            	            	
            	for(var resolverName in this.resolvers){
            		var resolver = this.resolvers[resolverName];
            		var widgets = formUI.getElementsByTagName(resolver.tagName);
            	
            	    for(var index=0; index<widgets.length; index++) {
             		    var widget = widgets[index];
             		    var fieldName = widget.getAttribute( this.fieldDef );
             	
             		    if( fieldName != null  && fieldName!="" ){
             			    resolvePolicy(resolver,widget,data,fieldName);
             		    }
                    }
            	}
            }

FormBinder.prototype.bindUIToData = function(formId) 
            {
                var data = new Object();
                this.doBinding(formId,data,this.resolveUItoData);
             	return data;
             }
             
FormBinder.prototype.resolveUItoData = function (resolver,widget,data,fieldName){
             	 if( resolver.isEmpty(widget) ){
             	 	return ;
             	 }
                 if( isNested( fieldName ) ){  //内嵌对象设置一个空对象,以便下面操作
             	     var nestedObjectName = fieldName.split(".")[0];
             		 var nestedFieldName = fieldName.split(".")[1];
             		 	    
             	     if( !data[nestedObjectName] ){
             		     data[nestedObjectName] = new Object();
             	     }
             	     resolver.setObjectValue(widget,data[nestedObjectName],nestedFieldName);
                 }else{
             		 resolver.setObjectValue(widget,data,fieldName);
                 }
             }
             
            function isNested(fieldName) 
            {
              	return -1 != fieldName.indexOf(".");
            }
             
//------------------------------------------------------------------------------------------------

function InputResolver(){
    this.tagName="input";
}
InputResolver.prototype.setWidgetValue = function(widget,fieldValue){
            		widget.value = fieldValue;
}
InputResolver.prototype.setObjectValue = function (widget,data,fieldName){
             	data[fieldName] = widget.value;
}
InputResolver.prototype.isEmpty = function (widget){
             	return widget.value =="";
}

//------------------------------------------------------------------------------------------------

function MultipleSelectResolver(){
    this.tagName="select";
}
MultipleSelectResolver.prototype.setWidgetValue = function(widget,fieldValue){
	            
            	if(widget.getAttribute("multiple")){            		
            		widget.options.length=null; //清空select
            		
            		if(fieldValue.list.constructor == Array){
            			for(var index in fieldValue.list){
            			    var item =document.createElement('option');
            				item.text = fieldValue.list[index];
            				item.value = fieldValue.list[index];
            				try {
            					widget.add(item,null);
            				} catch (e) {
            					 widget.add(item);
            				}
            			}
            		}
            	}
}
MultipleSelectResolver.prototype.setObjectValue = function (widget,data,fieldName){
	            if( widget.getAttribute("multiple") ){
	            	var list = new Array();
	                for(var index=0; index<widget.length; index++) {	  
			    	   list.push( widget.options[index].value );
			        } 
             	    data[fieldName] = new Object();
             	    data[fieldName]['list']=list;
             	    data[fieldName]['javaClass']="java.util.ArrayList";
	            }
	            
}
MultipleSelectResolver.prototype.isEmpty = function (widget){
             	return widget.length ==0;
}

//------------------------------------------------------------------------------------------------

function SelectResolver(){
    this.tagName="select";
}
SelectResolver.prototype.setWidgetValue = function(widget,fieldValue){
	            
            	if( !widget.getAttribute("multiple") ){
            		widget.options.length=null;
            		
            		var item =document.createElement('option');
            		item.text = fieldValue;
            		item.value = fieldValue;
            		try {
            		    widget.add(item,null);
            		} catch (e) {
            			widget.add(item);
            		}
            	}
}
SelectResolver.prototype.setObjectValue = function (widget,data,fieldName){
	            if( !widget.getAttribute("multiple") ){
	            	data[fieldName] = widget.options[widget.selectedIndex].value;
	            }
}
SelectResolver.prototype.isEmpty = function (widget){
             	return widget.length ==0;
}
             