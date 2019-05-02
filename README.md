# Food Sharing App
## User stories
##### For restaurants and dorm rooms with leftover foods: they can share food with people in starving, home shelters and any organizations that will use the food for the needy
##### For hungry people, home shelters and any organization in need of food: they can make a food request with a description of how much food in need

## Modular architecture
<p align="center"> 
<img src="https://github.com/ec500-software-engineering/project-22-food-sharing-app/blob/master/pics/diagram.png">
</p>

## Technologies used
### FrontEnd:
Display: Recycler View
<br>Map: Google Map API
<br>Platform: Android Studio
<br>Database: Parse(Based on MongoDB)
<br>Structure: MVP structure
<br>Debug: Setho


### BackEnd
Cloud: AWS/Parse
<br>ElasticSearch to store the post/request and support geo search
<br>Parse server to mange all the backend data including requests, user info and images.

## APIs
Google map API to display nearby food post and request on map
<br>Parse API to store data on cloud

## Task assignments
He Li: Overall layout, Recycler View, Goole Map API, MVP structure
<br>ZBY: AWS server & backend deployment, login interface, image & data storage

## Definition of Sprint 1, 2 and 3
Sprint 1: Research on how to use recycler view, room and google map API. Optimize design, specify what tool to use.
<br>Sprint 2: Overall layout and simple post feature done
<br>Sprint 3: Use AWS for server deployment and Parse for cloud database

## Project infrastructure
One main activity, with a top bar and bottom navigator bar and a view pager in the mid.
<br>Use view pager and container fragment to hold fragments on different pages.

## Front End implementation

### Recycler view
The RecyclerView widget is a more advanced and flexible version of ListView.
In the RecyclerView model, several different components work together to display your data. 

The RecyclerView creates only as many view holders as are needed to display the on-screen portion of the dynamic content, plus a few extra. As the user scrolls through the list, the RecyclerView takes the off-screen views and rebinds them to the data which is scrolling onto the screen.

### The MVP structure
Model–view–presenter (MVP) is a derivation of the model–view–controller (MVC) architectural pattern, and is used mostly for building user interfaces.
In MVP, the presenter assumes the functionality of the "middle-man". In MVP, all presentation logic is pushed to the presenter.

### Basic Structure
Three fragments: Food list, Food location via Google Map and login.
One Main activity with Viewpager, binding with 3 alive container fragments. 
<br>3 pages corresponding to 3 container fragments.
<br>Food list fragment
<br>Map fragment
<br>Setting fragment

<div align=center><img src="https://github.com/ec500-software-engineering/project-22-food-sharing-app/blob/master/pics/overall.gif"></div>

#### Login Fragment
For login, we support register/login with Parse or Google sign in api. And it will display google account info when sign in with Google.
<div align=center><img src="https://github.com/ec500-software-engineering/project-22-food-sharing-app/blob/master/pics/signin.gif"></div>


#### Food List Fragment
This part will fetch data from Parse Database, use RecyclerView to display, and MVP structure to separate model and view logic. 
<br> When click on the listitem, will get into the detail information of the item.
##### Display list
<div align=center><img src="https://github.com/ec500-software-engineering/project-22-food-sharing-app/blob/master/pics/ItemList.gif"></div>

##### Post Fragment:
Post Fragment to post/request food. Allow user to have access to local photo gallery on their phone.
And posts directly to Parse database. Also, you have to login if you want to post something

<div align=center><img src="https://github.com/ec500-software-engineering/project-22-food-sharing-app/blob/master/pics/post.gif"></div>

##### Refresh data
Refresh Data with refresh button. Will refresh list and markers on map at the same time.
<div align=center><img src="https://github.com/ec500-software-engineering/project-22-food-sharing-app/blob/master/pics/refresh.gif"></div>

#### Map Fragment
Google Map API is used here. Will get your current location with GPS and Network. Display the post/request data with markers and info windows. Will refresh the markers when click refresh on the first Page

<div align=center><img src="https://github.com/ec500-software-engineering/project-22-food-sharing-app/blob/master/pics/map.gif"></div>



## Backend deployment
### AWS EC2
Amazon Elastic Compute Cloud (Amazon EC2) provides scalable computing capacity in the Amazon Web Services (AWS) cloud.
<img src="https://github.com/ec500-software-engineering/project-22-food-sharing-app/blob/master/pics/AWS.jpg">
</p>

### Parse Server
Parse Server is an open source version of the Parse backend that can be deployed to any infrastructure that can run Node.js.

Parse Server works with the Express web application framework. It can be added to existing web applications, or run by itself.
<img src="https://github.com/ec500-software-engineering/project-22-food-sharing-app/blob/master/pics/Parse.jpg">

As shown in the picture, all the data including user info, user requests and images are stored by category in our backend database.

### Why we choose Parse
#### Easy to deploy
Once you have the data ready in the database, you don’t need any significant changes to the client code to run your applications.
#### Data Storage
Parse used MongoDB to store data and Amazon S3 bucket to store file system. Parse Server has enhanced database functionalities such as performance refactoring, data backup and restore, and indexing.
#### Friendly dashboard
Parse was well-known for its dashboard and Parse Server has all the amazing dashboard features of Parse. The dashboard enables users to manage and configure their apps and send push notifications.
#### Live Queries
Users need not make the same queries every time they need data. Users can construct a query and Parse Server will fetch real-time data as and when the resulting data changes.

