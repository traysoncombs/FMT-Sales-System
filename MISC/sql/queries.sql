-- 1. --
SELECT code, first_name, last_name FROM Person;
-- 2. --
SELECT 
	Person.code, 
    first_name, 
    last_name, 
    street, 
    city, 
    zip_code,
    State.name as state, 
    Country.name as country
FROM Person
JOIN Address ON address_id=Address.id
JOIN Country ON Address.country_id = Country.id
JOIN State ON Address.state_id = State.id;

-- 3. --

SELECT email FROM Email WHERE person_id = 1;

-- 4. --

UPDATE 
	Email
SET
	Email.email = "beardbooker@opticom.com"
WHERE
	Email.person_id = 1;

-- 5. --

-- UPDATE Store SET manager_id = null WHERE manager_id = 1;
--  UPDATE Invoice SET salesperson_id = null WHERE salesperson_id = 1;
--  UPDATE Invoice SET customer_id = null WHERE customer_id = 1;
--  DELETE FROM Person WHERE Person.id = 1;

-- 6. --

SELECT 
	Invoice.code,
	Item.*
FROM 
	Invoice
JOIN InvoiceItem ON InvoiceItem.invoice_id = Invoice.id
JOIN Item ON Item.id = InvoiceItem.item_id
WHERE
	Invoice.id = 2;

-- 7. --

SELECT 
	Item.*
FROM 
	Invoice
JOIN InvoiceItem ON InvoiceItem.invoice_id = Invoice.id
JOIN Item ON Item.id = InvoiceItem.item_id
WHERE
	Invoice.customer_id = 1;

-- 8. --

SELECT
	Invoice.store_id,
	COUNT(*) as number_sales
FROM 
	InvoiceItem
JOIN Invoice ON InvoiceItem.invoice_id = Invoice.id
GROUP BY Invoice.store_id;

-- 9. --

SELECT
	Invoice.salesperson_id,
	COUNT(*) as number_sales
FROM 
	InvoiceItem
JOIN Invoice ON InvoiceItem.invoice_id = Invoice.id
GROUP BY Invoice.salesperson_id;

-- 10. --

SELECT
	Invoice.id,
	SUM(Item.unit_price * InvoiceItem.quantity) as subtotal
FROM 
	Invoice
JOIN InvoiceItem ON InvoiceItem.invoice_id = Invoice.id
JOIN Item ON InvoiceItem.id = Item.id
WHERE InvoiceItem.discriminator = 'P'
GROUP BY invoice_id;

-- 11. --
SELECT 
	Invoice.id as invoice_id,
    InvoiceItem.item_id as item_id
FROM 
	Invoice
JOIN InvoiceItem ON InvoiceItem.invoice_id = Invoice.id
WHERE InvoiceItem.discriminator = 'P'
GROUP BY Invoice.id
HAVING COUNT(InvoiceItem.item_id) > 1;


-- 12. --

SELECT
	*
FROM 
	Invoice
WHERE
	salesperson_id = customer_id;