package org.opengoss.core.data;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

 public interface DataSet extends Iterator, Map  
 {  
     public abstract String getDataSetName();  
     public abstract void setDataSetName(String name);  
     public abstract DataSetMetaData getMetaData();  
     public abstract void setMetaData(DataSetMetaData md);  
   
     public abstract DataSet getDataSet(int column);  
     public abstract void setDataSet(int column, DataSet ds);  
     public abstract DataSet getDataSet(String path);  
     public abstract void setDataSet(String path, DataSet ds);  
   
     public abstract String getValue(int column);  
     public abstract void setValue(int column, String value);  
     public abstract String getValue(String path);  
     public abstract void setValue(String path, String value);  
 }  