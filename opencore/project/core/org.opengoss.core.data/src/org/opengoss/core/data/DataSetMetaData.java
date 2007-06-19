package org.opengoss.core.data;

public interface DataSetMetaData {
	public abstract int getColumnCount();

	public abstract void setColumnCount(int count);

	public abstract int getColumnDisplaySize(int column);

	public abstract String getColumnLabel(int column); // optional

	public abstract String getColumnName(int column);

	public abstract int getColumnType(int column);

	public abstract String getColumnTypeName(int column);

	public abstract int getPrecision(int column); // optional

	public abstract int getScale(int column); // optional

	public abstract boolean isAutoIncrement(int column); // default "no"

	public abstract boolean isCaseSensitive(int column); // default "yes"

	public abstract int isNullable(int column); // default "yes"

	public abstract void setAutoIncrement(int columnIndex, boolean property);

	public abstract void setCaseSensitive(int columnIndex, boolean property);

	public abstract void setColumnDisplaySize(int columnIndex, int size);

	public abstract void setColumnLabel(int columnIndex, String label);

	public abstract void setColumnName(int columnIndex, String columnName);

	public abstract void setColumnType(int columnIndex, int DataType);

	public abstract void setColumnTypeName(int columnIndex, String typeName);

	public abstract void setNullable(int columnIndex, int property);

	public abstract void setPrecision(int columnIndex, int precision);

	public abstract void setScale(int columnIndex, int scale);
}
