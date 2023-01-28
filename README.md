# Web-Crawler-and-Search
This repository is made for Homework 1 for the Advanced Internet Computing, Spring 2023 at Georgia Tech. It involves implementing Web Search using Apache Lucene where the Web Pages are craweled and scraped using Jsoup.\


Number of web pages crawled: ~1600

Language: Java 8+ ('''openjdk-18'''), IDE: IntelliJ

Libraries Used: '''Jsoup''' – for web crawling, '''Apache Lucene''' – for Indexing and Searching, '''OpenCSV''' – for writing the analytics data to CSV

Analysers Considered while Indexing:
1.	Standard Analyser
2.	Stop Analyser
3.	Simple Analyser
4.	Standard Analyser

Tool for Visualisation: Tableau 

## Setup: 

1. Run the crawler: '''CrawlerMain.java'''- Contains the seed URL “cc.gatech.edu” and depth as 30. 
> Note: This repository already contains the crawled files. One can skip this unless more or less number of web pages are to be included with a different seed url. 

2. Create Indexes for Specific Tasks: '''IndexMain.java'''- The indexes It contains multiple  index creation methods which are custom to the one by one execution required to accomplish the tasks being asked. Note, the executor methods are made so that the homework tasks can be executed separately. 
> Note: This repository already contains the indexed files for the already crawled files in the step 1. 

3. Search the keywords: '''SearchMain.java'''- It functionalities like keyword search, total number of keywords stored so far, and print top N keywords, which can be executed one by one to accomplish the tasks being asked.





