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

  // Fetch data from the /spam endpoint, assuming it returns an array of objects
  fetch('http://localhost:8080/spamDetector-1.0/api/spam/files')
    .then(response => response.json())
    .then(jsonData => {
      let table = document.getElementById('chart');
      console.log("data",jsonData);
      add_to_table(jsonData);


    });

}

function add_to_table(jsonData) {
  for (let i = 0; i < jsonData.length; i++) {
    let fileName = jsonData[i].file;
    let spamProb = jsonData[i].spamProbability;
    let actualClass = jsonData[i].actualClass;

    let tableRef = document.querySelector("#chart");
    let newRowRef = tableRef.insertRow(-1);
    newRowRef.className = "entry entry-enter"

    let dataRef1 = newRowRef.insertCell(0);
    let dataRef2 = newRowRef.insertCell(1);
    let dataRef3 = newRowRef.insertCell(2);

    dataRef1.innerText = fileName;
    dataRef2.innerText = spamProb;
    dataRef3.innerText = actualClass;
  }
}
/*toggle icon navbar*/
let menuIcon = document.querySelector('#menu-icon');
let navbar = document.querySelector('.navbar');

menuIcon.onclick = () => {
  menuIcon.classList.toggle('bx-x');
  navbar.classList.toggle('active');
};



/*scroll sections active link*/
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
    }
  });
  /*sticky navbar */
  let header = document.querySelector('header');

  header.classList.toggle('sticky', window.scrollY > 100);


  /* remove toggle icon and navbar on navbar link click */
  menuIcon.classList.remove('bx-x');
  navbar.classList.remove('active');
};
