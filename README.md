This project is a desktop program that helps the owner of a small campus tuckshop keep track of what is on the shelves, what has been sold and how much money has come in. We built it in Java with the Swing toolkit as our CSC 1102 practical exam project.
A small shop like this often relies on an exercise book and the shopkeeper's memory. Stock runs out without anyone noticing, prices get forgotten, and adding up a day's sales means counting by hand. We wanted a tool simple enough to learn in a few minutes that still answers the questions a shopkeeper actually asks: what do I have, what is running low, what did I sell, and how much did I make?
The project brief asked us to work without a database, so everything the program knows is held in arrays while it runs. That limit shaped many of our design choices, and we explain them in the sections that follow.
How it works 
1.	The user clicks a button in TuckshopApp.
2.	TuckshopApp reads the text fields and calls the matching method of Inventory, for example sellProduct.
3.	Inventory finds the right Product, asks it to reduceStock(), creates a new Sale object and stores it in the sales array.
4.	TuckshopApp refreshes the tables on screen so they show the new data.
The interface never changes the arrays directly. Keeping display and logic apart means we could change how the screen looks without touching the rules, and the rules can be tested without the screen.
# truckshop-management-system
Tuckshop Management System, CSC 1102, Group 9
