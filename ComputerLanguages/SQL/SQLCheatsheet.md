### SQL Cheatsheet

#### Querying

Pull column names only from a table:

`SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'table_name'`

`SELECT * FROM table_name WHERE 1=0`

Find all unique observations in a column:

`SELECT DISTINCT column_name FROM table_name`