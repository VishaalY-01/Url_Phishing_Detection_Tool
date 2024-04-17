var testdata;
var prediction;
var url = window.location.href;
var value;
var FLAG;
var data2;
var dony;
let hasRun1 = false;
var user;
const backendURL = 'http://localhost:3000/api/get-data'; // Use the GET endpoint URL
const backendURL1 = 'http://localhost:3000/api/query'; // Use the GET endpoint URL

async function fetchMessagesFromServer() {
    try {
        const response = await fetch(backendURL, {
            method: 'GET', // Change method to GET
        });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        console.log(data.message);
        if (data && data.message === 'continue_without_login') {
            console.log('User continued without Logging in');
            dony = -1;
            return dony;
        } else if (data && data.message === 'login_successful') {
            console.log('User Logged in');
            dony = data.data.username; // Get the username from data
            console.log('Username:', dony); // Log the username
            return dony;
        }
    } catch (error) {
        console.error('Error fetching data from server:', error);
        return null; // Handle error condition
    }
}

async function myFunction1() {
    if (!hasRun1) {
        try {
            value = await fetchMessagesFromServer();
            console.log("fetch_messager executed");
            hasRun1 = true;
            return value;
        } catch (error) {
            console.error('Error in myFunction1:', error);
            return null; // Handle error condition
        }
    }
    return value;
}

async function processData() {
    try {
        FLAG = await myFunction1();
        if (typeof (FLAG) == 'number') {
            prediction_without_login();
        } else if (typeof (FLAG) == 'string') {
            user = FLAG;
            function predict(data,weight){
                var f = 0;
                weight = [3.33346292e-01,-1.11200396e-01,-7.77821806e-01,1.11058590e-01,3.89430647e-01,1.99992062e+00,4.44366975e-01,-2.77951957e-01,-6.00531647e-05,3.33200243e-01,2.66644002e+00,6.66735991e-01,5.55496098e-01,5.57022408e-02,2.22225591e-01,-1.66678858e-01];
                for(var j=0;j<data.length;j++) {
                  f += data[j] * weight[j];
                }
                return f > 0 ? 1 : -1;
            }
            
            function isIPInURL(){
                var reg =/\d{1,3}[\.]{1}\d{1,3}[\.]{1}\d{1,3}[\.]{1}\d{1,3}/;
                var url = window.location.href
                if(reg.exec(url)==null){
                    console.log("NP");
                    return -1;
                }
                else{
                    console.log("P");
                    return 1;
                }
            }
            
            function isLongURL(){
                var url = window.location.href;    
                if(url.length<54){
                    console.log("NP");
                    return -1;
                } 
                else if(url.length>=54 && url.length<=75){
                    console.log("Maybe");
                    return 0;
                }
                else{
                    console.log("P");
                    return 1;
                }
            }
            function isTinyURL(){
                var url = window.location.href;    
                if(url.length>20){
                    console.log("NP");
                    return -1;
                } 
                else{
                    console.log("P");
                    return 1;
                }
            }
            function isAlphaNumericURL(){
                var search ="@";
                var url = window.location.href; 
                if(url.match(search)==null){
                    console.log("NP");
                    return -1;
                }
                else{
                    console.log("P");
                    return 1;
                }
            }
            function isRedirectingURL(){
                var reg1 = /^http:/
                var reg2 = /^https:/
                var srch ="//";
                var url = window.location.href; 
                if(url.search(srch)==5 && reg1.exec(url)!=null && (url.substring(7)).match(srch)==null){
                    console.log("NP");
                    return -1;
                }
                else if(url.search(srch)==6 && reg2.exec(url)!=null && (url.substring(8)).match(srch)==null){
                    console.log("NP");
                    return -1;
                }
                else{
                    console.log("P");
                    return 1;
                }
            }
            function isHypenURL(){
                var reg = /[a-zA-Z]\//;
                var srch ="-";
                var url = window.location.href; 
                if(((url.substring(0,url.search(reg)+1)).match(srch))==null){
                    console.log("NP");
                    return -1;
                }    
                else{
                    console.log("P");
                    return 1;
                }
            }
            function isMultiDomainURL(){
                var reg = /[a-zA-Z]\//;
                var srch ="-";
                var url = window.location.href; 
                if((url.substring(0,url.search(reg)+1)).split('.').length < 5){
                    console.log("NP");
                    return -1;
                }    
                else{
                    console.log("P");
                    return 1;
                }
            }
            function isFaviconDomainUnidentical(){
                var reg = /[a-zA-Z]\//;
                var url = window.location.href; 
                if(document.querySelectorAll("link[rel*='shortcut icon']").length>0){            
                    var faviconurl = document.querySelectorAll("link[rel*='shortcut icon']")[0].href;
                    if((url.substring(0,url.search(reg)+1))==(faviconurl.substring(0,faviconurl.search(reg)+1))){
                        console.log("NP");
                        return -1;
                    }    
                    else{
                        console.log("P");
                        return 1;
                    }
                }
                else{
                    console.log("NP");
                    return -1;
                }
            }
            
            function isIllegalHttpsURL(){
                var srch1 ="//";   
                var srch2 = "https";   
                var url = window.location.href; 
                if(((url.substring(url.search(srch1))).match(srch2))==null){
                    console.log("NP");
                    return -1;
                }    
                else{
                    console.log("P");
                    return 1;
                }
            }
            function isImgFromDifferentDomain(){
                var totalCount = document.querySelectorAll("img").length
                var identicalCount = getIdenticalDomainCount("img");
                if(((totalCount-identicalCount)/totalCount)<0.22){
                    console.log("NP");
                    return -1;
                } 
                else if((((totalCount-identicalCount)/totalCount)>=0.22) && (((totalCount-identicalCount)/totalCount)<=0.61)){
                    console.log("Maybe");
                    return 0;
                } 	
                else{
                    console.log("P");
                    return 1;
                }
            }
            function isAnchorFromDifferentDomain(){
                var totalCount = document.querySelectorAll("a").length
                var identicalCount = getIdenticalDomainCount("a");
                if(((totalCount-identicalCount)/totalCount)<0.31){
                    console.log("NP");
                    return -1;
                } 
                else if((((totalCount-identicalCount)/totalCount)>=0.31) && (((totalCount-identicalCount)/totalCount)<=0.67)){
                    console.log("Maybe");
                    return 0;
                } 	
                else{
                    console.log("P");
                    return 1;
                }
            }
            function isScLnkFromDifferentDomain(){
                var totalCount = document.querySelectorAll("script").length + document.querySelectorAll("link").length
                var identicalCount = getIdenticalDomainCount("script") + getIdenticalDomainCount("link");
                if(((totalCount-identicalCount)/totalCount)<0.17){
                    console.log("NP");
                    return -1;
                } 
                else if((((totalCount-identicalCount)/totalCount)>=0.17) && (((totalCount-identicalCount)/totalCount)<=0.81)){
                    console.log("Maybe");
                    return 0;
                } 	
                else{
                    console.log("P");
                    return 1;
                }
            }
            
            function isFormActionInvalid(){
                var totalCount = document.querySelectorAll("form").length
                var identicalCount = getIdenticalDomainCount("form");
                if(document.querySelectorAll('form[action]').length<=0){
                    console.log("NP");
                    return -1;
                }	
                else if(identicalCount!=totalCount){
                    console.log("Maybe");
                    return 0;
                } 	
                else if(document.querySelectorAll('form[action*=""]').length>0){
                    console.log("P");
                    return 1;
                } 
                else{
                    console.log("NP");
                    return -1;
                } 
            }
            
            function isMailToAvailable(){
                if(document.querySelectorAll('a[href^=mailto]').length<=0){
                    console.log("NP");
                    return -1;
                } 	
                else{
                    console.log("P");
                    return 1;
                }
            }
            
            function isStatusBarTampered(){
                if((document.querySelectorAll("a[onmouseover*='window.status']").length<=0) || (document.querySelectorAll("a[onclick*='location.href']").length<=0)){
                    console.log("NP");
                    return -1;
                }
                else{
                    console.log("P");
                    return 1;
                } 
            }
            
            function isIframePresent(){
                if(document.querySelectorAll('iframe').length<=0){
                    console.log("NP");
                    return -1;
                }
                else{
                    console.log("P");
                    return 1;
                }
            }
            
            function getIdenticalDomainCount(tag){    
                var i;
                var identicalCount=0;
                var reg = /[a-zA-Z]\//;    
                var url = window.location.href;
                var mainDomain = url.substring(0,url.search(reg)+1);    
                var nodeList = document.querySelectorAll(tag);
                if(tag=="img" || tag=="script"){
                    nodeList.forEach(function(element,index) {        
                    i = nodeList[index].src
                    if(mainDomain==(i.substring(0,i.search(reg)+1))){
                       identicalCount++;
                    }   
                  });
                }  
                else if(tag=="form"){
                    nodeList.forEach(function(element,index) {        
                    i = nodeList[index].action
                    if(mainDomain==(i.substring(0,i.search(reg)+1))){
                       identicalCount++;
                    }   
                  });
                }  
                else if(tag=="a"){
                    nodeList.forEach(function(element,index) {        
                    i = nodeList[index].href
                    if((mainDomain==(i.substring(0,i.search(reg)+1))) && ((i.substring(0,i.search(reg)+1))!=null) && ((i.substring(0,i.search(reg)+1))!="")){
                       identicalCount++;
                    }    
                  });
                } 
                else{
                    nodeList.forEach(function(element,index) {        
                    i = nodeList[index].href
                    if(mainDomain==(i.substring(0,i.search(reg)+1))){
                       identicalCount++;
                    }    
                  });
                }  
                return identicalCount;
            } 
            
            testdata = [isIPInURL(),isLongURL(),isTinyURL(),isAlphaNumericURL(),isRedirectingURL(),isHypenURL(),isMultiDomainURL(),isFaviconDomainUnidentical(),isIllegalHttpsURL(),isImgFromDifferentDomain(),isAnchorFromDifferentDomain(),isScLnkFromDifferentDomain(),isFormActionInvalid(),isMailToAvailable(),isStatusBarTampered(),isIframePresent()];
            
            prediction = predict(testdata);
            
            chrome.extension.sendRequest(prediction);
            
            
            
            function get_predict(prediction){
                if (prediction == 1){
                    data1=("Phishing");
                }
                else if (prediction == -1){
                    data1=("No Phishing");
                }
                return data1;
            }
            
            data2 = url;
        
            fetch(backendURL1, {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
              },
              body: JSON.stringify({
                action: 'insert_data', // Specify the action for data insertion
                username: user, // Assuming 'user' is the username
                url: data2,
                prediction: get_predict(prediction),
              }),
            })
              .then(response => {
                if (!response.ok) {
                  throw new Error('Network response was not ok');
                }
                return response.json();
              })
              .then(data => {
                console.log('Response from backend:', data);
                // Handle response from backend if needed
              })
              .catch(error => {
                console.log('Error:', error);
              });
            
        }
    } catch (error) {
        console.error('Error in processData:', error);
    }
}

function prediction_without_login(){
    
    function predict(data,weight){
        var f = 0;
        weight = [3.33346292e-01,-1.11200396e-01,-7.77821806e-01,1.11058590e-01,3.89430647e-01,1.99992062e+00,4.44366975e-01,-2.77951957e-01,-6.00531647e-05,3.33200243e-01,2.66644002e+00,6.66735991e-01,5.55496098e-01,5.57022408e-02,2.22225591e-01,-1.66678858e-01];
        for(var j=0;j<data.length;j++) {
          f += data[j] * weight[j];
        }
        return f > 0 ? 1 : -1;
    }
    
    function isIPInURL(){
        var reg =/\d{1,3}[\.]{1}\d{1,3}[\.]{1}\d{1,3}[\.]{1}\d{1,3}/;
        var url = window.location.href
        if(reg.exec(url)==null){
            console.log("NP");
            return -1;
        }
        else{
            console.log("P");
            return 1;
        }
    }
    
    function isLongURL(){
        var url = window.location.href;    
        if(url.length<54){
            console.log("NP");
            return -1;
        } 
        else if(url.length>=54 && url.length<=75){
            console.log("Maybe");
            return 0;
        }
        else{
            console.log("P");
            return 1;
        }
    }
    function isTinyURL(){
        var url = window.location.href;    
        if(url.length>20){
            console.log("NP");
            return -1;
        } 
        else{
            console.log("P");
            return 1;
        }
    }
    function isAlphaNumericURL(){
        var search ="@";
        var url = window.location.href; 
        if(url.match(search)==null){
            console.log("NP");
            return -1;
        }
        else{
            console.log("P");
            return 1;
        }
    }
    function isRedirectingURL(){
        var reg1 = /^http:/
        var reg2 = /^https:/
        var srch ="//";
        var url = window.location.href; 
        if(url.search(srch)==5 && reg1.exec(url)!=null && (url.substring(7)).match(srch)==null){
            console.log("NP");
            return -1;
        }
        else if(url.search(srch)==6 && reg2.exec(url)!=null && (url.substring(8)).match(srch)==null){
            console.log("NP");
            return -1;
        }
        else{
            console.log("P");
            return 1;
        }
    }
    function isHypenURL(){
        var reg = /[a-zA-Z]\//;
        var srch ="-";
        var url = window.location.href; 
        if(((url.substring(0,url.search(reg)+1)).match(srch))==null){
            console.log("NP");
            return -1;
        }    
        else{
            console.log("P");
            return 1;
        }
    }
    function isMultiDomainURL(){
        var reg = /[a-zA-Z]\//;
        var srch ="-";
        var url = window.location.href; 
        if((url.substring(0,url.search(reg)+1)).split('.').length < 5){
            console.log("NP");
            return -1;
        }    
        else{
            console.log("P");
            return 1;
        }
    }
    function isFaviconDomainUnidentical(){
        var reg = /[a-zA-Z]\//;
        var url = window.location.href; 
        if(document.querySelectorAll("link[rel*='shortcut icon']").length>0){            
            var faviconurl = document.querySelectorAll("link[rel*='shortcut icon']")[0].href;
            if((url.substring(0,url.search(reg)+1))==(faviconurl.substring(0,faviconurl.search(reg)+1))){
                console.log("NP");
                return -1;
            }    
            else{
                console.log("P");
                return 1;
            }
        }
        else{
            console.log("NP");
            return -1;
        }
    }
    
    function isIllegalHttpsURL(){
        var srch1 ="//";   
        var srch2 = "https";   
        var url = window.location.href; 
        if(((url.substring(url.search(srch1))).match(srch2))==null){
            console.log("NP");
            return -1;
        }    
        else{
            console.log("P");
            return 1;
        }
    }
    function isImgFromDifferentDomain(){
        var totalCount = document.querySelectorAll("img").length
        var identicalCount = getIdenticalDomainCount("img");
        if(((totalCount-identicalCount)/totalCount)<0.22){
            console.log("NP");
            return -1;
        } 
        else if((((totalCount-identicalCount)/totalCount)>=0.22) && (((totalCount-identicalCount)/totalCount)<=0.61)){
            console.log("Maybe");
            return 0;
        } 	
        else{
            console.log("P");
            return 1;
        }
    }
    function isAnchorFromDifferentDomain(){
        var totalCount = document.querySelectorAll("a").length
        var identicalCount = getIdenticalDomainCount("a");
        if(((totalCount-identicalCount)/totalCount)<0.31){
            console.log("NP");
            return -1;
        } 
        else if((((totalCount-identicalCount)/totalCount)>=0.31) && (((totalCount-identicalCount)/totalCount)<=0.67)){
            console.log("Maybe");
            return 0;
        } 	
        else{
            console.log("P");
            return 1;
        }
    }
    function isScLnkFromDifferentDomain(){
        var totalCount = document.querySelectorAll("script").length + document.querySelectorAll("link").length
        var identicalCount = getIdenticalDomainCount("script") + getIdenticalDomainCount("link");
        if(((totalCount-identicalCount)/totalCount)<0.17){
            console.log("NP");
            return -1;
        } 
        else if((((totalCount-identicalCount)/totalCount)>=0.17) && (((totalCount-identicalCount)/totalCount)<=0.81)){
            console.log("Maybe");
            return 0;
        } 	
        else{
            console.log("P");
            return 1;
        }
    }
    
    function isFormActionInvalid(){
        var totalCount = document.querySelectorAll("form").length
        var identicalCount = getIdenticalDomainCount("form");
        if(document.querySelectorAll('form[action]').length<=0){
            console.log("NP");
            return -1;
        }	
        else if(identicalCount!=totalCount){
            console.log("Maybe");
            return 0;
        } 	
        else if(document.querySelectorAll('form[action*=""]').length>0){
            console.log("P");
            return 1;
        } 
        else{
            console.log("NP");
            return -1;
        } 
    }
    
    function isMailToAvailable(){
        if(document.querySelectorAll('a[href^=mailto]').length<=0){
            console.log("NP");
            return -1;
        } 	
        else{
            console.log("P");
            return 1;
        }
    }
    
    function isStatusBarTampered(){
        if((document.querySelectorAll("a[onmouseover*='window.status']").length<=0) || (document.querySelectorAll("a[onclick*='location.href']").length<=0)){
            console.log("NP");
            return -1;
        }
        else{
            console.log("P");
            return 1;
        } 
    }
    
    function isIframePresent(){
        if(document.querySelectorAll('iframe').length<=0){
            console.log("NP");
            return -1;
        }
        else{
            console.log("P");
            return 1;
        }
    }
    
    function getIdenticalDomainCount(tag){    
        var i;
        var identicalCount=0;
        var reg = /[a-zA-Z]\//;    
        var url = window.location.href;
        var mainDomain = url.substring(0,url.search(reg)+1);    
        var nodeList = document.querySelectorAll(tag);
        if(tag=="img" || tag=="script"){
            nodeList.forEach(function(element,index) {        
            i = nodeList[index].src
            if(mainDomain==(i.substring(0,i.search(reg)+1))){
               identicalCount++;
            }   
          });
        }  
        else if(tag=="form"){
            nodeList.forEach(function(element,index) {        
            i = nodeList[index].action
            if(mainDomain==(i.substring(0,i.search(reg)+1))){
               identicalCount++;
            }   
          });
        }  
        else if(tag=="a"){
            nodeList.forEach(function(element,index) {        
            i = nodeList[index].href
            if((mainDomain==(i.substring(0,i.search(reg)+1))) && ((i.substring(0,i.search(reg)+1))!=null) && ((i.substring(0,i.search(reg)+1))!="")){
               identicalCount++;
            }    
          });
        } 
        else{
            nodeList.forEach(function(element,index) {        
            i = nodeList[index].href
            if(mainDomain==(i.substring(0,i.search(reg)+1))){
               identicalCount++;
            }    
          });
        }  
        return identicalCount;
    } 
    
    testdata = [isIPInURL(),isLongURL(),isTinyURL(),isAlphaNumericURL(),isRedirectingURL(),isHypenURL(),isMultiDomainURL(),isFaviconDomainUnidentical(),isIllegalHttpsURL(),isImgFromDifferentDomain(),isAnchorFromDifferentDomain(),isScLnkFromDifferentDomain(),isFormActionInvalid(),isMailToAvailable(),isStatusBarTampered(),isIframePresent()];
    
    prediction = predict(testdata);
    
    chrome.extension.sendRequest(prediction);
    
    
    console.log(prediction);
        }


processData(); // Call processData to start the data processing

