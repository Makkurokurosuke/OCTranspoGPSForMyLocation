OCTranspoGPSForMyLocation
=========================


----------------------------------------------------------------------

There are already many nice applications to track Ottawa buses, 
but I couldn't find any apps that allow us to search more than one bus route at the same time. 

So, I created this for my convenience.

Note: Google API key and OC Transpo API key are removed from the source code. 


How to use the app.


Step 1: Click the bus icon.


![screen shot of the appliation icon](https://raw.github.com/Makkurokurosuke/OCTranspoGPSForMyLocation/master/screenshot/Screenshot_1.png)


Step 2: Press Search by my location.


![screen shot of the menu](https://raw.github.com/Makkurokurosuke/OCTranspoGPSForMyLocation/master/screenshot/Screenshot_2.png)


Step 3: Press the my location button on the top right corner. (It will get your location without pressing the button, but it takes time.) Press a bus stop marker.


![screen shot of the map](https://raw.github.com/Makkurokurosuke/OCTranspoGPSForMyLocation/master/screenshot/Screenshot_3.png)


Step 4: Select bus routes and press the Search button.


![screen shot of the map](https://raw.github.com/Makkurokurosuke/OCTranspoGPSForMyLocation/master/screenshot/Screenshot_4.png)


Step 5: Now you see a list of bus route schedules. You can know which one comes first.


![screen shot of the map](https://raw.github.com/Makkurokurosuke/OCTranspoGPSForMyLocation/master/screenshot/Screenshot_5.png)



<b>Data and technology used in the application</b>
<ul>
<li>Java</li>
<li>Google Map v2</li>
<li>Android 4.0</li>
<li>OC Transpo Live Next Bus Arrival Data Feed API</li>
<li>OC Transpo Schedules</li>
<li>SQLite</li>
</ul>



<b>My future plan - There is still a lot to add and fix.</b>
<ul>
<li>Add better exception error handling</li>
<li>Add an option to keep updaing the result every minute for the next 5 minutes</li>
<li>Add a loading message while processing</li>
<li>Update bus stop location, because it may be old</li>
<li>Test more</li>
</ul>
