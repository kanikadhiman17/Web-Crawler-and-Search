# Web-Crawler-and-Search
This repository is made for Homework 1 of Advanced Internet Computing, Spring 2023 at Georgia Tech. It involves implementing a Toy Web Search using Apache Lucene where the Web Pages are craweled and scraped using Jsoup, and then indexed for the searching.

## Basic Information:
Number of web pages crawled: `1601`

Language: Java 8+ (`openjdk-18`), IDE: `IntelliJ`

Libraries Used: `Jsoup - 1.11.2` – for web crawling, `Apache Lucene - 7` – for Indexing and Searching, `OpenCSV - 4.4` – for writing the analytics data to CSV. The exact versions are included in `pom.xml`.

Analysers considered while indexing:
1.	Standard Analyser
2.	Stop Analyser
3.	Simple Analyser
4.	Standard Analyser

Tool for Visualisation: `Tableau`

## Setup: 

1. Run the crawler: `CrawlerMain.java`- Contains the seed URL `cc.gatech.edu ` and depth as `30`. 
> Note: This repository already contains the crawled files. This step can be skipped unless more or less number of web pages are to be included with a different seed url and depth. 

2. Create Indexes for Specific Tasks: `IndexMain.java`- It contains multiple index creation methods which that are tailored to the one-by-one execution needed to finish the tasks asked in the homework. Note, there are separate executor methods made for each part. 
> Note: This repository already contains the indexed files in the `index` folder for the crawled files from the step 1. 

3. Search the keywords: `SearchMain.java`- It include functionalities like keyword search, total number of keywords stored so far, and print top N keywords, which can be executed one by one to accomplish the tasks being asked.





