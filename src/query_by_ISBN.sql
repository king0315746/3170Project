SELECT b.title AS "Book Title", b.ISBN, b.unit_price AS "Unit Price", b.no_of_copies AS "No of Copies Available",
       GROUP_CONCAT(DISTINCT ba.author_name ORDER BY ba.author_name ASC SEPARATOR ', ') AS "A List of Authors"
FROM book b
LEFT JOIN book_author ba ON b.ISBN = ba.ISBN
WHERE b.ISBN = '1-1234-1234-1'/* parameter import by java*/
GROUP BY b.title, b.ISBN, b.unit_price, b.no_of_copies
ORDER BY b.title ASC, b.ISBN ASC;