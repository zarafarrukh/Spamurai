# Spam Detector SPAMURAI - Assignment 01
<p align="center">
  <img src="SpamDetectorClient/img/spamurai.png" alt="Spamurai Logo" width="300">
</p>

## Project Information
We've taken the initiative to develop a program designed to sift through and eliminate spam emails. Our spam detector, <b><i>SPAMURAI</i></b>, employs a unigram approach, meticulously analyzing each word within an email to ascertain its spam status. SPAMURAI exhibits a remarkable ability to differentiate between spam & legitimate emails by calculating probabilities based on word frequency, ensuring high accuracy in its assessments. The system has been trained using a diverse dataset encompassing both spam and non-spam emails, and the SpamDetector class has been crafted to facilitate model reading, testing, and training. With a robust set of samples at its disposal, the program excels at sieving out unwanted messages. The algorithm boasts a % accuracy rate and a % precision rate, providing assurance of SPAMURAI's reliability.
### A product of the combined skills of Manal Afzal, Zara Farrukh, Syeda Bisha Fatima & Rabia Chattha
<p align="center">
   <img src="Spamurai_navBar.png" alt="Spamurai Working Interface" width="700">
  <img src="Spamurai_About.png" alt="Spamurai Working Interface" width="700">
</p>

## Improvements
We enhanced the code by incorporating HashMaps and ArrayLists for efficient storage of data values, resulting in a time complexity of O(n) for value searches. Additionally, during the optimization stage, we found a way to eliminate an entire testing function and implemented the storage of test results (table) within the testAndTrain method, reducing code lines and redundancy. We also generalized a duplicate method and employed getters and setters for Accuracy and Precision to prevent the use of multiple variables. Furthermore, we introduced an isWord function to handle unique words and symbols, streamlining the code.

Given more time, we would further refine our algorithm by implementing Laplace Smoothing to effectively manage unseen words. Additionally, we aim to enhance the software to accommodate special characters and symbols.

## How to run
### Step-by-Step Instructions:

1. Clone the repository: `git clone https://github.com/OntarioTech-CS-program/w24-csci2020u-assignment01-fatima-afzal-chattha-farrukh.git`
2. Launch intelliJ IDEA ULTIMATE and navigate to project directory 
3. Configure glassfish and edit run configurations
   - Set default URL as `http://localhost:8080/spamDetector-1.0/api/spam`
4. Start GlassFish server and deploy application
5. View dashboard with precision, accuracy and data values displayed 
## Other Resources
[1] Font: [Google Fonts - Poppins](https://fonts.google.com/specimen/Poppins)

[2] Font Awesome Icons: [Font Awesome](https://fontawesome.com/)
  - The application uses Font Awesome icons for enhanced visual elements. You can explore and customize the available icons on the Font Awesome website: [Font Awesome Icons](https://fontawesome.com/icons)

[3]  Jakarta EE (Java EE): [Jakarta EE](https://jakarta.ee/)
   - The application uses Jakarta EE, specifically the `jakarta.ws.rs` package for handling RESTful web services.

[4] Jackson Library: [Jackson - JSON Processor](https://github.com/FasterXML/jackson)
    - The application utilizes the Jackson library for JSON processing, including `com.fasterxml.jackson.annotation`, `com.fasterxml.jackson.core`, and `com.fasterxml.jackson.databind` packages.
    
[5] https://en.wikipedia.org/wiki/Bag-of-words_model 

[6] https://en.wikipedia.org/wiki/Naive_Bayes_spam_filtering 
