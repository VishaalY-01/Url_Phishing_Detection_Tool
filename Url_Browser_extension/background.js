
const scriptName = 'login.py';
let hasRun = false;

function myFunction() {
  if (!hasRun) {
    // Your code here
    script_runner();
    console.log("Script_runner executed");
    hasRun = true;
  }
}
// Call myFunction to execute it
myFunction();

function script_runner() {
  fetch('http://localhost:3000/api/extension-data', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      action: 'execute_python',
      scriptName,
    }),
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then(data => {
      console.log('Response from server:', data);
      // Process the response as needed
    })
    .catch(error => {
      console.error('Error sending request to server:', error);
    });
}


  chrome.extension.onRequest.addListener(function(prediction){
    if (prediction == 1){
        alert("Warning: Phishing detected!!");
    }
    else if (prediction == -1){
        alert("No phishing detected");
    }
});

// chrome.runtime..addListener(function(prediction,message,sender,) {
//     if (message.type === 'prediction') {
//         var xhr = new XMLHttpRequest();
//         xhr.open("POST", "http://localhost:3000/api/visited-sites", true); // Modify the URL as per your backend server
//         xhr.setRequestHeader("Content-Type", "application/json");
//         xhr.onreadystatechange = function() {
//             if (xhr.readyState === 4) {
//                 if (xhr.status === 201) {
//                     console.log("Data saved successfully!");
//                 } else {
//                     console.error("Error saving data:", xhr.status);
//                 }
//             }
//         };
//         xhr.send(JSON.stringify(message));

//         // Handle prediction here
//         if (prediction === 1) {
//             alert("Warning: Phishing detected!!");
//         } else if (prediction === -1) {
//             alert("No phishing detected");
//         }

//         // Send response if needed
//         sendResponse({ received: true });
//     }
// });

// chrome.extension.onRequest.addListener(function(prediction){
//     if (prediction == 1){
//         alert("Warning: Phishing detected!!");
//     }
//     else if (prediction == -1){
//         alert("No phishing detected");
//     }
//     var message = {
//         prediction: prediction,
//         url: sender.url
//     };
//     var xhr = new XMLHttpRequest();
//     xhr.open("POST", "http://localhost:3000/api/visited-sites", true); // Modify the URL as per your backend server
//     xhr.setRequestHeader("Content-Type", "application/json");
//     xhr.onreadystatechange = function() {
//         if (xhr.readyState === 4) {
//             if (xhr.status === 201) {
//                 console.log("Data saved successfully!");
//             } else {
//                 console.error("Error saving data:", xhr.status);
//             }
//         }
//     };
//     xhr.send(JSON.stringify(message));

// });