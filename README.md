<div style="background-color: #2e2e2e; padding: 20px; color: #e0e0e0; font-family: Arial, sans-serif;">
  
# Carter Campbell's Fantasy Football Mock Draft Simulator
View this repository: https://github.com/Carterc7/FantasyApp
<br/>
This website is a Fantasy Football Mock Draft Simulator, where users can compete in mock drafts against a set number of CPUs. Login to draft today!

# Documentation
### Logical System Design
The logical system design shows data flow in each application feature, and where that data comes from. Users will enter from the web under the HomeController, which contains a nav bar to access any application feature. The login module utilizes the UserModel and the usersDAO to validate user input against database values. Once the user is logged in, they may either draft, logout, or view previously completed drafts. The registration model utilizes the UserModel as well and will create a UserModel object before a HTTP POST will send the new user object to the database. The ADP (Average Draft Position) module shows the flow of JSON data from the FantasyData API, which is then mapped to the AdpPlayer object model, and then accessed by the AdpController through the adpDAO to display the list of players with adp. Finally, the Mock Draft module shows the flow of JSON data from both our external APIs. The FantasyData API is used to fetch the adp, map to AdpPlayer object models, and then accessed by the DraftController through the adpDAO to create a draft board where users can select players ordered by adp. The RapidAPI is used to obtain JSON data for general information and stats of players, which is mapped to their respective object model and used by the DraftController through the playerDAO to dynamically show player stats and information whenever the user expands a player view.

<img width="468" alt="image" src="https://github.com/user-attachments/assets/364456af-feae-49a2-b921-7c85607892a0">

### Physical System Design
The physical system design shows the overall application architecture including data storage, data access, front-end management, and web access. My application contains 4 mongo collections to store user data, general player information, team schedules, and the adp. Stats will not be stored in a database and rather pulled directly from the API, mapped to objects, and then sent to the intended controller using the data access classes. The back-end server is the only system that can access our data through DAOs, and the front-end server must request data from the backend. Clients will access the front-end directly through the web and front-end controllers will handle the application flow. This physical design adheres to the rules of MVC architecture and ensures proper data and application flow.

<img width="468" alt="image" src="https://github.com/user-attachments/assets/c59bf485-7a78-4be9-8a5f-af763b6f72ef">

### UMLs

First, we have our model classes for each API endpoint we are going to be receiving data from (imaged below). The StatsPlayer, EspnPlayer, and TeamSchedule models are needed to store data that comes from endpoints on RapidAPI, and the AdpPlayer models data from an endpoint on FantasyData. The StatsPlayer model has a property for any kind of stat we may need to show, including passing, receiving, rushing, and defensive statistics. All properties are nullable since players will not have entries for specific stats, like a quarterback not having any defensive stats. The next model class is EspnPlayer which contains all our general information about a player like their height, weight, team, age, position, and college. The AdpPlayer model provides average draft position values for multiple formats, and the TeamSchedule model provides team schedules and gameIDs that will be used to search for stats of a player within a specific game. These model classes give us access to comprehensive data on almost every player in the NFL to provide a realistic and statistically accurate fantasy football experience. Other models that may need to be added during implementation will be Team, League, and CPU models that contain logic methods and properties for the mock draft process.
<br/>

<img width="700" alt="image" src="https://github.com/user-attachments/assets/bd0427f2-cc1b-4509-ab32-b7b0625dd068">

Next is our API call classes (imaged below), which contain the methods to call the APIs and map data to the correct class model. Each class has a “sendRequest()” method that is used to call the external API at a specific endpoint, and the JSON data returned from the API is returned from the method as a string. Then, each class contains a “mapJson()” method to map the JSON string into a class object to one of the respective models discussed above. These classes finally contain business logic methods to create a list of objects from our API calls and return a filtered list.
<br/>

<img width="700" alt="image" src="https://github.com/user-attachments/assets/d81596a8-3121-42c3-8c20-2c7a326e7897">

Moving on to discuss the service classes and repository interfaces (imaged below), which will handle database management and support CRUD operations. The repository class are interfaces that extend MongoRepository<Object>, which will be used to open a connection to our database tables and define any extra CRUD operations we may need to perform on the data outside the default options provided by the library. For example, we can find a player’s ADP by searching a playerId or find a team schedule by their team’s name and a specific game week. The service classes are used to instantiate our repositories and provide definitions to our interface methods.
<br/>

<img width="700" alt="image" src="https://github.com/user-attachments/assets/9495e114-a4cf-4829-bea2-cb409d3da755">

Now to one of the most important sections of the applications, the controller classes (imaged below) instantiate any business and data access classes, send and request data between layers, and display views based on user input. The HomeController simply shows the home page view, the LoginController shows the login form and processes login by instantiating DAO methods, the RegistrationController shows the registration form and processes registration by creating a user and adding it to the database, the DraftController contains draft logic methods such as showDraftBoard and selectPlayer and displays draft views, and also shows previously completed mocks and leagues, the AdpController shows the adp list page and can update the adp list in the database, the EspnController instantiates DAO methods to return a list as needed for general info on players, the ScheduleController instantiates DAO methods to return gameIDs as needed to search for specific stats, and finally the StatsController instantiates DAO methods to return specific stats based on gameIDs and player names. These controllers handle any situation or event a user may encounter when accessing the site and provide access to all our data through REST API endpoints and views.
<br/>

<img width="700" alt="image" src="https://github.com/user-attachments/assets/a750a850-7483-4f46-a50e-e0e81c471988">

Finally, the last model class for the application is the User and SecurityService classes (imaged below). The User model contains properties like a username, password, and a list of teams from completed mock drafts. This model is used as a parameter in our SecurityService class methods, which contain business logic to authenticate logins and add users to the database. Any additional security methods will be added to this class as needed, but there is little “valuable” data that needs protected in this format and throughout the application.
<br/>

<img width="400" alt="image" src="https://github.com/user-attachments/assets/5b5d900c-47c1-4930-9911-940870a755b9">

The NoSQL collections (imaged below) store all our data from the external APIs, along with the user collection that stores usernames, passwords, and previously completed mock draft team data. As mentioned earlier, these collections will be accessed through the MongoRepository<> extension in each repository class. Searching through these collections is normally done by the “_id” property, but I have written extra DAO methods within the repository and service classes to search off certain game schedules, player names, and adp.
<br/>

<img width="700" alt="image" src="https://github.com/user-attachments/assets/5559d6f2-3063-491e-8387-297a3116234d">

# Code

### Draft Functionality

This code snippet "showDraftBoard" shows the controller endpoint to start a draft, where the user will input a number of teams, a team name, a draft position, and a number of rounds. With this, we will create <FantasyTeam> objects that will hold the user's player selections, as well as CPUs. This information is sent to draftBoard.html, which begins the draft.
<br/>

<img width="900" alt="image" src="https://github.com/user-attachments/assets/2961b57a-7005-4462-bd28-8af51806ffc0">

This code snippet "selectPlayer" shows the controller endpoint when a player selection is made, and specifically shows the logic for auto-drafting a player when it is a CPU selection. This algorithm makes sure that by the end of the draft, every CPU team will have a player at each position, while randomly selecting one of the top 3 players available each time.
<br/>

<img width="900" alt="image" src="https://github.com/user-attachments/assets/a5ead1bf-bd99-4124-8737-e8ab5cfda0ff">

The below code snippet is also from the "selectPlayer" controller method and shows how the snake draft (First pick of 1st round has last pick of 2nd round and first pick of 3rd round) functionality works using the "isReversed" boolean and the currentTeamId. The boolean will be flipped dynamically when at the end of each round.
<br/>

<img width="900" alt="image" src="https://github.com/user-attachments/assets/7dbffae4-13e0-48bf-b105-14c51c1a3e84">

# User Session

This code snippet "authenticateUser" shows the controller endpoint to validate a user login and add them to the session. Adding the user to the session allows us to show their previously drafted teams.
<br/>

<img width="900" alt="image" src="https://github.com/user-attachments/assets/4d771e74-37af-4660-8e5f-3372fb3eac33">

This code snippet "getUserTeams" shows how we use the user session variable to retrieve the previously drafted teams and send them to a template "userTeams". This page filters out CPU drafted teams, but they can be viewed through another endpoint.
<br/>

<img width="900" alt="image" src="https://github.com/user-attachments/assets/dcc1da91-6ccd-408b-8fd7-48bb432eb2a0">

# API Calls

As previously stated, all NFL player information is retrieved from external APIs to ensure the data is accurate and authentic. This code snippet "sendRequestGetAllPlayers" shows how we make an API call and receive a JSON payload of player information. The API key is stored in the .env for security purposes.
<br/>

<img width="900" alt="image" src="https://github.com/user-attachments/assets/9813361d-cdca-43a6-9405-0b56e7f604f4">

After we make a call to receive the JSON payload, the code snippet "mapJsonToPlayerObject" shows how we convert that data into a list of objects that can be used throughout the application. In this scenario, we are mapping to the EspnPlayer object which contains player information like age, DOB, jersey number, college, etc. This process is repeated for the AdpPlayer, StatsPlayer, and TeamSchedule objects.
<br/>

<img width="900" alt="image" src="https://github.com/user-attachments/assets/e815bd55-e6b5-4a3c-80ac-0d1ef2887fde"> 

</div>

