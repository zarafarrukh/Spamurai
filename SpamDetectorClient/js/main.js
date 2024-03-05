// TODO: onload function should retrieve the data needed to populate the UI
window.onload = function() {
    // Retrieve accuracy and precision from the server
    fetch('http://localhost:8080/spamDetector-1.0/api/spam/accuracy')
      .then(response => response.json())
      .then(data => {
        document.getElementById('accuracy').innerHTML = data;
      });
  
    fetch('http://localhost:8080/spamDetector-1.0/api/spam/precision')
      .then(response => response.json())
      .then(data => {
        document.getElementById('precision').innerHTML = data;
      });
    fetch('http://localhost:8080/spamDetector-1.0/api/spam/data')
      .then(response => response.json())
      .then(jsonData => {

        let table = document.getElementById('chart');
  
        // Loop through every object in array inserting properties into a new table row
        jsonData.forEach(obj => {
          let row = table.insertRow();
          let filenameCell = row.insertCell();
          let spamProbCell = row.insertCell();
          let actualClassCell = row.insertCell();
  
          filenameCell.innerHTML = obj.filename;
          spamProbCell.innerHTML = obj.spamProb;
          actualClassCell.innerHTML = obj.actualClass;
        });
      });
  }
  
  /*toggle icon navbar*/
  let menuIcon = document.querySelector('#menu-icon');
  let navbar = document.querySelector('.navbar');
  
  menuIcon.onclick = () => {
    menuIcon.classList.toggle('bx-x');
    navbar.classList.toggle('active');
  };
  
  
  /*scroll sections  */
  let sections = document.querySelectorAll('section');
  let naviLinks = document.querySelectorAll('header nav a');
  
  window.onscroll = () => {
    sections.forEach(sec => {
      let top = window.scrollY;
      let offset = sec.offsetTop - 150;
      let height = sec.offsetHeight;
      let id = sec.getAttribute('id');
  
      if(top >= offset && top < offset + height) {
        naviLinks.forEach(links => {
          links.classList.remove('active');
          document.querySelector('header nav ahref*=' + id + ']').classList.add('active');
        });
      };
    });
    /*sticky navbar */
    let header = document.querySelector('header');
  
    header.classList.toggle('sticky', window.scrollY > 100);
  
    /* remove toggle icon and navbar on navbar link click */
    menuIcon.classList.remove('bx-x');
    navbar.classList.remove('active');
  };
  
  
  /*scrollreveal*/
  ScrollReveal({
    // reset: true,
    distance: '80px',
    duration: 2000,
    delay: 200
  });
  
  ScrollReveal().reveal('.home-content, .heading', { origin: 'top' });
  ScrollReveal().reveal('.home-img', { origin: 'bottom' });
  ScrollReveal().reveal('.home-content h1, .about-img', { origin: 'left' });
  ScrollReveal().reveal('.home-content p, .about-content', { origin: 'right' });
  
  
  /* typed js */
  const typed = new Typed('.multiple-text', {
    strings: ['Zara Farrukh', 'Manal Afzal', 'Syeda Bisha Fatima', 'Rabia Chattha'],
    typeSpeed: 100,
    backSpeed: 100,
    backDelay: 1000,
    loop: true
  });