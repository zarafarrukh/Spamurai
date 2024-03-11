# Spam Detector SPAMURAI - Assignment 01
<p align="center">
  <img src="SpamDetectorClient/img/spamurai.png" alt="Spamurai Logo" width="300">
</p>

## Project Information
In today's digital age, the inundation of spam emails pose a significant challenge for individuals and organizations alike. To tackle this persistent challenge, we present SPAMURAI  - a program designed to sift through and eliminate spam emails. Our spam detector, <b><i>SPAMURAI</i></b>, employs a unigram approach, meticulously analyzing each word within an email to ascertain its spam status. SPAMURAI exhibits a remarkable ability to differentiate between spam & legitimate emails by calculating probabilities based on word frequency, ensuring high accuracy in its assessments. The system has been trained using a diverse dataset encompassing both spam and non-spam emails, and the SpamDetector class has been crafted to facilitate model reading, testing, and training. With a robust set of samples at its disposal, the program excels at sieving out unwanted messages. The algorithm boasts a 76.6% accuracy rate and an 80.7% precision rate, providing assurance of SPAMURAI's reliability.
### A product of the combined skills of Manal Afzal, Zara Farrukh, Syeda Bisha Fatima & Rabia Chattha
<p align="center">
   <img src="Spamurai_navBar.png" alt="Spamurai Working Interface" width="600">
  <img src="Spamurai_About.png" alt="Spamurai Working Interface" width="600">
</p>

## Improvements
We optimized the SpamDetector class by using HashMaps and ArrayLists to efficiently store and retrieve data values, reducing the search complexity to O(n). We simplified the code by eliminating a redundant testing function, integrating test results directly into the trainAndTest method, and reducing code duplication by generating a duplicate method. We simplified variable control by using getters and setters for accuracy and precision, as well as used an isWord function to simplify the handling of unique words and symbols. These optimizations all together enhanced the algorithm's performance, readability, and maintainability. Notably, SPAMURAI boosts an impressive 76.6 % accuracy rate and an 80.7 % precision rate, which underscores its reliability and effectiveness in combating spam.

This HTML document structures a webpage dedicated to a spam detector project, featuring sections like the header, home, about, project, team, and footer. The CSS code ensures a modern and responsive visual experience, incorporating custom styles, 'Poppins' font, and consistent color variables. The JavaScript code adds functionality, including toggling the navbar icon, highlighting active sections, implementing a sticky navbar, and utilizing ScrollReveal for animations. Additionally, the code fetches accuracy and precision data from a server using the fetch API, dynamically displaying it on the webpage. The well-organized code allows for easy customization and future development.

The HTML structures a webpage, featuring sections like a header, home, about and a data table. The CSS ensures a modern and responsive visual experience, incorporating custom style 'Poppins' font and consistent color variables. The JavaScript code adds functionality, including implementing a sticky navbar, and utilizing dashboard and about page animations. Additionally, the code fetches accuracy and precision data from a server using the fetch API, dynamically displaying it on the webpage. The well-organized code allows for easy customization and future development.

Moving forward we would focus on enhancing our algorithm by implementing Laplace Smoothing, a technique renowned for handling unseen words, thereby enhancing the model's predictive accuracy and precision. We would also enhance our algorithm to handle a diverse range of special characters and symbols to ensure optimal performance across varied input scenarios

## How to run
### Step-by-Step Instructions:

1. Clone the repository: `git clone https://github.com/OntarioTech-CS-program/w24-csci2020u-assignment01-fatima-afzal-chattha-farrukh.git`
2. Launch intelliJ IDEA ULTIMATE and navigate to project directory
3. Configure glassfish and edit run configurations
    - Set default URL as `http://localhost:8080/spamDetector-1.0/api/spam`
4. Start GlassFish server and deploy application on selecting ▶
5. View dashboard with precision, accuracy and data values displayed

For a more detailed step-by-step instruction on how to run the glassfish server, watch this video:

[Spamurai.mp4](https://drive.google.com/file/d/15smaeTVAwavbi1pRPwntUZDA-OMa6jvt/view?usp=sharing)

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
