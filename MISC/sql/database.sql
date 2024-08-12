DROP TABLE IF EXISTS InvoiceItem;
DROP TABLE IF EXISTS Item;
DROP TABLE IF EXISTS Invoice;
DROP TABLE IF EXISTS Store;
DROP TABLE IF EXISTS Email;
DROP TABLE IF EXISTS Person;
DROP TABLE IF EXISTS Address;
DROP TABLE IF EXISTS State;
DROP TABLE IF EXISTS Country;
DROP FUNCTION IF EXISTS get_address_id;

-- Address normalization --

DELIMITER //

CREATE FUNCTION get_address_id 
	(street VARCHAR(255), city VARCHAR(255), zip_code INT, state VARCHAR(2), country VARCHAR(2)) 
	RETURNS INT DETERMINISTIC
BEGIN

	SET @state_id = (SELECT State.id FROM State WHERE State.name = state);

	SET @country_id = (SELECT Country.id FROM Country WHERE Country.name = country);

	RETURN (SELECT Address.id FROM Address WHERE Address.street = street AND Address.city = city AND Address.zip_code = zip_code AND Address.state_id = @state_id AND Address.country_id = @country_id);

END; //

DELIMITER ;

CREATE TABLE State (
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	name CHAR(255) NOT NULL,
    UNIQUE (name)
);

CREATE TABLE Country (
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	name CHAR(255) NOT NULL,
    UNIQUE (name)
);

CREATE TABLE Address (
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	street VARCHAR(255) NOT NULL,
	zip_code INT NOT NULL,
	city VARCHAR(255) NOT NULL,
	state_id INT NOT NULL,
	country_id INT NOT NULL,
	FOREIGN KEY (state_id)
		REFERENCES State(id),
	FOREIGN KEY (country_id)
		REFERENCES Country(id)
);

-- Person stuff --

CREATE TABLE Person (
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	code VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	first_name VARCHAR(255) NOT NULL,
	address_id INT NOT NULL,
    UNIQUE (code),
	FOREIGN KEY (address_id)
		REFERENCES Address(id)
);

CREATE TABLE Email (
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	email VARCHAR(255) NOT NULL,
	person_id INT NOT NULL,
	UNIQUE (email),
	FOREIGN KEY (person_id)
		REFERENCES Person(id)
        ON DELETE CASCADE
);

CREATE TABLE Store (
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	code VARCHAR(255) NOT NULL,
	address_id INT,
	manager_id INT,
	UNIQUE (code),
	FOREIGN KEY (address_id)
		REFERENCES Address(id),
	FOREIGN KEY (manager_id)
		REFERENCES Person(id)
);

CREATE TABLE Invoice (
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	code VARCHAR(255) NOT NULL,
	store_id INT NOT NULL,
	customer_id INT,
    salesperson_id INT,
	invoice_date VARCHAR(255) NOT NULL,
	UNIQUE (code),
	FOREIGN KEY (store_id)
		REFERENCES Store(id),
	FOREIGN KEY (customer_id)
		REFERENCES Person(id),
	FOREIGN KEY (salesperson_id)
		REFERENCES Person(id)
);

CREATE TABLE Item (
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	code VARCHAR(255) NOT NULL,
	name VARCHAR(255) NOT NULL,
	-- EquipmentItem --
	model VARCHAR(255),
	-- ProductItem --
	unit VARCHAR(255),
	unit_price FLOAT,
	-- ServiceItem --
	hourly_rate FLOAT,
	-- --
	discriminator ENUM("E", "P", "S") NOT NULL,
	UNIQUE (code)
);

CREATE TABLE InvoiceItem (
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	invoice_id INT NOT NULL,
	item_id INT NOT NULL,
	-- ProductInvoiceItem --
	quantity FLOAT,
	-- PurchasedEquipmentInvoiceItem --
	purchase_price FLOAT,
	-- ServiceInvoiceItem --
	hours_billed FLOAT,
	-- LeasedEquipmentInvoiceItem --
	fee FLOAT,
	start_date VARCHAR(255),
	end_date VARCHAR(255),
	-- --
	discriminator ENUM("P", "PE", "S", "L"),
    FOREIGN KEY (Invoice_id)
		REFERENCES Invoice(id)
		ON DELETE CASCADE,
	FOREIGN KEY (item_id)
		REFERENCES Item(id),
	FOREIGN KEY (item_id)
		REFERENCES Item(id)
);

-- Create and normalize Addresses --


INSERT INTO State (name) VALUES ("OR");
INSERT INTO State (name) VALUES ("ID");
INSERT INTO State (name) VALUES ("NY");
INSERT INTO State (name) VALUES ("PA");
INSERT INTO State (name) VALUES ("PW");
INSERT INTO State (name) VALUES ("AL");

INSERT INTO State (name) VALUES ("CT");
INSERT INTO State (name) VALUES ("NM");
INSERT INTO State (name) VALUES ("NC");
INSERT INTO State (name) VALUES ("DE");
INSERT INTO State (name) VALUES ("MS");



INSERT INTO Country (name) VALUES ("NZ");
INSERT INTO Country (name) VALUES ("FX");
INSERT INTO Country (name) VALUES ("GS");
INSERT INTO Country (name) VALUES ("NI");
INSERT INTO Country (name) VALUES ("EE");
INSERT INTO Country (name) VALUES ("IL");

INSERT INTO Country (name) VALUES ("LV");
INSERT INTO Country (name) VALUES ("KW");
INSERT INTO Country (name) VALUES ("LC");
INSERT INTO Country (name) VALUES ("VC");
INSERT INTO Country (name) VALUES ("MT");
INSERT INTO Country (name) VALUES ("PW");


INSERT INTO Address 
	(street, zip_code, city, state_id, country_id) 
VALUES
	("Caton Place", 88025, "Alleghenyville", (SELECT State.id FROM State WHERE State.name = "OR"), (SELECT Country.id FROM Country WHERE Country.name = "NZ"));
	
INSERT INTO Address 
	(street, zip_code, city, state_id, country_id) 
VALUES
	("Ocean Avenue", 58753, "Foscoe", (SELECT State.id FROM State WHERE State.name = "ID"), (SELECT Country.id FROM Country WHERE Country.name = "FX"));
	
INSERT INTO Address 
	(street, zip_code, city, state_id, country_id) 
VALUES
	("Engert Avenue", 59430, "Jessie", (SELECT State.id FROM State WHERE State.name = "NY"), (SELECT Country.id FROM Country WHERE Country.name = "GS"));

INSERT INTO Address 
	(street, zip_code, city, state_id, country_id) 
VALUES
	("Centre Street", 54543, "Tedrow", (SELECT State.id FROM State WHERE State.name = "PA"), (SELECT Country.id FROM Country WHERE Country.name = "NI"));
	
INSERT INTO Address 
	(street, zip_code, city, state_id, country_id) 
VALUES
	("Madeline Court", 76048, "Graniteville", (SELECT State.id FROM State WHERE State.name = "PW"), (SELECT Country.id FROM Country WHERE Country.name = "EE"));
	
INSERT INTO Address 
	(street, zip_code, city, state_id, country_id) 
VALUES
	("Midwood Street", 42547, "Hessville", (SELECT State.id FROM State WHERE State.name = "AL"), (SELECT Country.id FROM Country WHERE Country.name = "IL"));
	
	

INSERT INTO Address 
	(street, zip_code, city, state_id, country_id) 
VALUES
	("Wolcott Street", 78052, "Blodgett", (SELECT State.id FROM State WHERE State.name = "CT"), (SELECT Country.id FROM Country WHERE Country.name = "LV"));
	
INSERT INTO Address 
	(street, zip_code, city, state_id, country_id) 
VALUES
	("Landis Court", 53308, "Canby", (SELECT State.id FROM State WHERE State.name = "NM"), (SELECT Country.id FROM Country WHERE Country.name = "KW"));

INSERT INTO Address 
	(street, zip_code, city, state_id, country_id) 
VALUES
	("College Place", 51935, "Rossmore", (SELECT State.id FROM State WHERE State.name = "NC"), (SELECT Country.id FROM Country WHERE Country.name = "LC"));
	
INSERT INTO Address 
	(street, zip_code, city, state_id, country_id) 
VALUES
	("Pulaski Street", 71537, "Coalmont", (SELECT State.id FROM State WHERE State.name = "CT"), (SELECT Country.id FROM Country WHERE Country.name = "VC"));
	
INSERT INTO Address 
	(street, zip_code, city, state_id, country_id) 
VALUES
	("Burnett Street", 38345, "Mulberry", (SELECT State.id FROM State WHERE State.name = "DE"), (SELECT Country.id FROM Country WHERE Country.name = "MT"));

	
INSERT INTO Address 
	(street, zip_code, city, state_id, country_id) 
VALUES
	("Lamont Court", 88881, "Bayview", (SELECT State.id FROM State WHERE State.name = "MS"), (SELECT Country.id FROM Country WHERE Country.name = "PW"));

	
-- Create people --

INSERT INTO Person
	(code, last_name, first_name, address_id)
VALUES
	("cb9e5d00", "Booker", "Beard", (SELECT get_address_id("Caton Place", "Alleghenyville", 88025, "OR", "NZ")));
	
INSERT INTO Person
	(code, last_name, first_name, address_id)
VALUES
	("1f65ea36", "Garcia", "Edith", (SELECT get_address_id("Ocean Avenue", "Foscoe", 58753, "ID", "FX")));

INSERT INTO Person
	(code, last_name, first_name, address_id)
VALUES
	("924725d8", "Arnold", "Lessie", (SELECT get_address_id("Engert Avenue", "Jessie", 59430, "NY", "GS")));

INSERT INTO Person
	(code, last_name, first_name, address_id)
VALUES
	("26d41b81", "Vega", "Byers", (SELECT get_address_id("Centre Street" , "Tedrow", 54543, "PA", "NI")));
	
INSERT INTO Person
	(code, last_name, first_name, address_id)
VALUES
	("63407d13", "Schwartz", "Eunice", (SELECT get_address_id("Madeline Court", "Graniteville", 76048, "PW", "EE")));
	
INSERT INTO Person
	(code, last_name, first_name, address_id)
VALUES
	("7c83e390", "Lane", "Lenora", (SELECT  get_address_id("Midwood Street", "Hessville", 42547, "AL", "IL")));

-- Set up Emails --

INSERT INTO Email
	(email, person_id)
VALUES
	("beardbooker@opticom.com", (SELECT id FROM Person WHERE Person.code = "cb9e5d00"));

INSERT INTO Email
	(email, person_id)
VALUES
	("edithgarcia@opticom.com", (SELECT id FROM Person WHERE Person.code = "1f65ea36"));

INSERT INTO Email
	(email, person_id)
VALUES
	("lessiearnold@opticom.com", (SELECT id FROM Person WHERE Person.code = "924725d8"));

INSERT INTO Email
	(email, person_id)
VALUES
	("byersvega@opticom.com", (SELECT id FROM Person WHERE Person.code = "26d41b81"));
	
-- Insert Stores --

INSERT INTO Store
	(code, address_id, manager_id)
VALUES
	("d36421b9", 
    (SELECT get_address_id("Wolcott Street", "Blodgett", 78052, "CT", "LV")), 
    (SELECT id FROM Person WHERE Person.code = "cb9e5d00"));
    
INSERT INTO Store
	(code, address_id, manager_id)
VALUES
	("ff772d5e", 
    (SELECT get_address_id("Landis Court", "Canby", 53308, "NM", "KW")), 
    (SELECT id FROM Person WHERE Person.code = "1f65ea36"));
    
INSERT INTO Store
	(code, address_id, manager_id)
VALUES
	("d7dd7ff0", 
    (SELECT get_address_id("College Place", "Rossmore", 51935, "NC", "LC")), 
    (SELECT id FROM Person WHERE Person.code = "924725d8"));    

INSERT INTO Store
	(code, address_id, manager_id)
VALUES
	("9d0344d9", 
    (SELECT get_address_id("Pulaski Street", "Coalmont", 71537, "CT", "VC")), 
    (SELECT id FROM Person WHERE Person.code = "26d41b81"));    

INSERT INTO Store
	(code, address_id, manager_id)
VALUES
	("ec29fc50", 
    (SELECT get_address_id("Burnett Street", "Mulberry", 38345, "DE", "MT")), 
    (SELECT id FROM Person WHERE Person.code = "63407d13"));    

INSERT INTO Store
	(code, address_id, manager_id)
VALUES
	("3c72a849", 
    (SELECT get_address_id("Lamont Court", "Bayview", 88881, "MS", "PW")), 
    (SELECT id FROM Person WHERE Person.code = "7c83e390"));    

-- Create Items --

INSERT INTO Item
	(code, name, model, discriminator)
VALUES
	("123ef", "Truck", "250H", 'E');
    
INSERT INTO Item
	(code, name, model, discriminator)
VALUES
	("abc123", "Trailer", "SE 1000", 'E');

INSERT INTO Item
	(code, name, unit, unit_price, discriminator)
VALUES
	("47365a", "Corn", "stone", 100, 'P');
    
INSERT INTO Item
	(code, name, unit, unit_price, discriminator)
VALUES
	("asdf45", "Dirt", "ton", 20000, 'P');
    
INSERT INTO Item
	(code, name, hourly_rate, discriminator)
VALUES
	("abfg467", "Hauling", 99.99, 'S');

INSERT INTO Item
	(code, name, hourly_rate, discriminator)
VALUES
	("54663a", "Consultation", 10000, 'S');

-- Create Invoices --

INSERT INTO Invoice
	(code, store_id, customer_id, salesperson_id, invoice_date)
VALUES
	("INV001", 
	(SELECT Store.id FROM Store WHERE Store.code = "d36421b9"),
    (SELECT Person.id FROM Person WHERE Person.code = "cb9e5d00"),
	(SELECT Person.id FROM Person WHERE Person.code = "1f65ea36"),
	"2021-02-11"
);
    
INSERT INTO Invoice
	(code, store_id, customer_id, salesperson_id, invoice_date)
VALUES
	("INV002", 
	(SELECT Store.id FROM Store WHERE Store.code = "ff772d5e"),
    (SELECT Person.id FROM Person WHERE Person.code = "924725d8"),
	(SELECT Person.id FROM Person WHERE Person.code = "26d41b81"),
	"2021-01-01");

INSERT INTO Invoice
	(code, store_id, customer_id, salesperson_id, invoice_date)
VALUES
	("INV003", 
	(SELECT Store.id FROM Store WHERE Store.code = "ff772d5e"),
    (SELECT Person.id FROM Person WHERE Person.code = "63407d13"),
	(SELECT Person.id FROM Person WHERE Person.code = "7c83e390"),
	"2021-02-01");

INSERT INTO Invoice
	(code, store_id, customer_id, salesperson_id, invoice_date)
VALUES
	("INV004", 
	(SELECT Store.id FROM Store WHERE Store.code = "9d0344d9"),
    (SELECT Person.id FROM Person WHERE Person.code = "cb9e5d00"),
	(SELECT Person.id FROM Person WHERE Person.code = "7c83e390"),
	"2020-11-11");
    
-- Create Invoice Items --

INSERT INTO InvoiceItem
	(invoice_id, item_id, fee, start_date, end_date, discriminator)
VALUES
	((SELECT Invoice.id FROM Invoice WHERE Invoice.code = "INV001"),
    (SELECT Item.id FROM Item WHERE Item.code = "123ef"),
    2000,
    "2021-07-09",
    "2022-04-09",
    'L');

INSERT INTO InvoiceItem
	(invoice_id, item_id, purchase_price, discriminator)
VALUES
	((SELECT Invoice.id FROM Invoice WHERE Invoice.code = "INV002"),
    (SELECT Item.id FROM Item WHERE Item.code = "abc123"),
    11000,
    'PE');

INSERT INTO InvoiceItem
	(invoice_id, item_id, quantity, discriminator)
VALUES
	((SELECT Invoice.id FROM Invoice WHERE Invoice.code = "INV002"),
    (SELECT Item.id FROM Item WHERE Item.code = "asdf45"),
    2,
    'P');

INSERT INTO InvoiceItem
	(invoice_id, item_id, quantity, discriminator)
VALUES
	((SELECT Invoice.id FROM Invoice WHERE Invoice.code = "INV003"),
    (SELECT Item.id FROM Item WHERE Item.code = "47365a"),
    100,
    'P');

INSERT INTO InvoiceItem
	(invoice_id, item_id, hours_billed, discriminator)
VALUES
	((SELECT Invoice.id FROM Invoice WHERE Invoice.code = "INV004"),
    (SELECT Item.id FROM Item WHERE Item.code = "abfg467"),
    56,
    'S');
    
INSERT INTO InvoiceItem
	(invoice_id, item_id, hours_billed, discriminator)
VALUES
	((SELECT Invoice.id FROM Invoice WHERE Invoice.code = "INV004"),
    (SELECT Item.id FROM Item WHERE Item.code = "54663a"),
    27,
    'S');
