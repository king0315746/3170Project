# 3170Project
以下是按照specification要求的顺序排序的sql文件以及其具体用法：

1.reload 创建表以及导入数据 [数据我放在了本地database对应的path:C:\ProgramData\MySQL\MySQL Server 8.0\Data\my_database,数据的形式是txt]

2.view 显示所有表

3.system_date 更改系统时间 
[input:yyyy-mm-dd]

CUSTOMER INTERFACE:


4.query by title/author name/ ISBN 
[input:  title(部分也可以），author_name(部分也可以),ISBN] 
作用：查询书本
[output:  book title, unit price no_of_copies]

5.create_orders
[input: customer_id, isbn, quantity]
作用:创建订单
[output:如果创建成功返回True,失败返回NuLL]

6.add to order 
[input: order id, isbn, quantity]
作用：给order 的特定书本加货 更新ordering 和 order表 中的对应quantity以及日期
[output:order id , order status, number of copies]

7.delete from order
[input: qiamtotu. order_od, isbn]
作用：从order中remove 书籍并且放回book里面

BOOK STORE INTERFACE:


8.order_query 
【input:customer_id,year (四位数)】
作用：输出这个customer这一年内的所有订单
[output: order id , order date, shipping status, charge]

9&10： order update1/2:
1:
[intput:order id]
output: shipping status, quantity

2: input :order id
作用：把这个order 的status 改成yes
output: None

11:book_store_order_query
input: yyyy-mm 代表一个月份
作用：返回这个月份内的所有order并且计算总价格
output:
table1: order id, customer id ,order date, charge
table2: tatal charge

 12;most_popular:
input : N 一个整数
作用：查询order中排名前N的书籍
output: book title, isbn, tatal ordered copies（一共卖出多少本）


具体操作：
java部分放进query就行
sql部分按照以下方法来运行
1.下载sql 下载完cmd 跑 mysql --version 检验
2.设置sql 数据库名称为root 表示本地数据库
3.打开cmd，运行 mysql -u root -p ,输入密码
4.输入:create database My_database;
5.把要测试的txt文件放到该database的目录下面
6. 使用命令：source xxx.sql依次运行即可
4.把data文件放到数据库的
