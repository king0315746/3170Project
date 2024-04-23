SET @N = 5;

SELECT sq.Book_Title, sq.ISBN, sq.Total_Ordered_Copies
FROM (
    SELECT b.title AS "Book_Title", b.isbn AS "ISBN", SUM(od.quantity) AS "Total_Ordered_Copies",
           ROW_NUMBER() OVER (ORDER BY SUM(od.quantity) DESC, b.title ASC, b.isbn ASC) AS rn
    FROM book b
    JOIN ordering od ON b.isbn = od.isbn
    WHERE od.quantity >= 1
    GROUP BY b.title, b.isbn
) sq
WHERE rn <= @N
ORDER BY "Total Ordered Copies" DESC, "Book Title" ASC, "ISBN" ASC;