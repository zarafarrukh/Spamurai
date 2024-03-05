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

        // Loop through each object in the array and insert its properties into a new table row
        jsonData.forEach(obj => {
          let row = table.insertRow();
          let filenameCell = row.insertCell();
          let spamProbabilityCell = row.insertCell();
          let actualClassCell = row.insertCell();

          filenameCell.innerHTML = obj.filename;
          spamProbabilityCell.innerHTML = obj.spamProbability;
          actualClassCell.innerHTML = obj.actualClass;
        });
      });
  }

  /*==================== toggle icon navbar ====================*/
  let menuIcon = document.querySelector('#menu-icon');
  let navbar = document.querySelector('.navbar');

  menuIcon.onclick = () => {
    menuIcon.classList.toggle('bx-x');
    navbar.classList.toggle('active');
  };


  /*==================== scroll sections active link ====================*/
  let sections = document.querySelectorAll('section');
  let navLinks = document.querySelectorAll('header nav a');

  window.onscroll = () => {
    sections.forEach(sec => {
      let top = window.scrollY;
      let offset = sec.offsetTop - 150;
      let height = sec.offsetHeight;
      let id = sec.getAttribute('id');

      if(top >= offset && top < offset + height) {
        navLinks.forEach(links => {
          links.classList.remove('active');
          document.querySelector('header nav a[href*=' + id + ']').classList.add('active');
        });
      };
    });
    /*==================== sticky navbar ====================*/
    let header = document.querySelector('header');

    header.classList.toggle('sticky', window.scrollY > 100);

    /*==================== remove toggle icon and navbar when click navbar link (scroll) ====================*/
    menuIcon.classList.remove('bx-x');
    navbar.classList.remove('active');
  };


  /*==================== scroll reveal ====================*/
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


  /*==================== typed js ====================*/
  const typed = new Typed('.multiple-text', {
    strings: ['Zara Farrukh', 'Manal Afzal', 'Syeda Bisha Fatima', 'Rabia Chattha'],
    typeSpeed: 100,
    backSpeed: 100,
    backDelay: 1000,
    loop: true
  });
