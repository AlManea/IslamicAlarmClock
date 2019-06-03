# IslamicAlarmClock
This project illustrates the use of Java (1.4) to create an Islamic alarm clock

## I. Basic Description
A user would like to have a 5-time alarm clock application. The application should be a GUI digital clock that enables to set five different time alarms. While the application is running, the main window is displaying the current time. When the current time is equal to one of the set alarm, the application will play the sound file. The user may activate up to five alarms.


## II. Requirements List:
1.	The application displays the current time and date in a main window.
2.	The application has five alarms.
3.	Initially, the alarms are set the default value (00:00).
4.	The user can set any of the alarms to a specific time.
5.	While the application is running, it displays the current date and time.
6.	When one of the alarms is equal to the current time, the application plays a sound file.
7.	Initially, a specific sound file is set as the alarm sound file.
8.	The user can change the sound file to any valid sound file.
9.	The user can enable or disable any alarm.
10.	Some additional features—such as displaying prayer times in some specific cities and enabling the user to set his alarms to those prayer times—can be also added to this application.
 
## C.	The Program Design:
It is obvious from the problem summary statement that almost all the functions this application is to perform are homogenous, since they are all related to the time and hence act on almost the same attributes. Therefore, the easiest and most natural way of designing such an application is to have one class, call it IslamicAlarmClock, in which all the attributes and the operations performed on them are encapsulated. This kind of design, in which all the code is almost enclosed in one class, involves the use of many nested classes, either member, local, or anonymous classes. The type and number of those nested classes is determined during the implementation.

Moreover, this program needs to update its output every second, in addition to responding to events triggered by the user. This fact indicates that the program should be designed to have concurrency. Therefore, the program should have at least two concurrent threads.

With such the mentioned features, this program can be designed to have all the code that displays the GUI and responds to the events triggered by the user inside the constructor of the main class. Another method, call it refreshTime() should take care of refreshing the main window continuously. Such a feature needs an infinite loop with a thread that is executed every second. In addition, comparing the values of the alarms with the current time can be also performed inside this method. Of course, there is also a main() method.

If the application is to supply the times of prayers for some cities, then a text file, call it thePrayers.txt, having all the times of prayers for those cities must be used. Furthermore, finding the time of prayers for the specified city on the current date should be performed by another method, call it findCity().

Finally, if the application is to display the time in the analog form, in addition to the digital, then it is better use one or more other classes, since this functionality needs many special attributes and operations that has nothing to do with the main purpose of this application, which is to display the time and to operate the alarms. The program needs to have a special thread devoted for this functionality.

In summary, the program should have two classes, one for displaying the time and performing almost all the operations, which contains the main(), and the other is used only to display the analog clock. Inside the main class, almost all the code for displaying the GUI and responding to events is written inside the constructor. A method refreshTime() is used to refresh the main window every second and to check if any alarm is equal to the current time. Another method findCity() is used to search the thePrayers.txt file for the times of prayers of the chosen city at the current date

## IV. Implementation
The program was implemented using Java language. It has three files; one contains the main class, which is IslamicAlarmClock, while the second contains two classes for displaying the analog clock, which are Clock and ClockAnalogBuf. NOTE: The code of Clock and ClockAnalogBuf was taken as is from the website:
http://leepoint.net/notes-java/45examples/40animation/50analogclockbuf/analogclockbufexample.html

The third file is thePrayers.txt which is a text file containing the prayer times for some Saudi cities.

The code of the main class: IslamicAlarmClock and the two other classes: Clock and ClockAnalogBuf follows on the next pages.

## References

1.	 Java: Example – Buffered Analog Clock, 
http://leepoint.net/notes-java/45examples/40animation/50analogclockbuf/analogclockbufexample.html
 (Jan. 3, 2004).
