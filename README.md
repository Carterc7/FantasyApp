<div style="background-color: #2e2e2e; padding: 20px; color: #e0e0e0; font-family: Arial, sans-serif;">

# **Carter Campbell's Fantasy Football Mock Draft Simulator**

**View this repository:** [FantasyApp](https://github.com/Carterc7/FantasyApp)  
This website is a **Fantasy Football Mock Draft Simulator**, where users can compete in mock drafts against a set number of CPUs. Login to draft today!

---

## **Documentation**

### **Logical System Design**
The logical system design shows data flow in each application feature, and where that data comes from. Users will enter from the web under the `HomeController`, which contains a navigation bar to access any application feature.

The login module uses the `UserModel` and `usersDAO` to validate user input against database values. Once logged in, users can draft, log out, or view completed drafts. The registration model also uses `UserModel`, creating a `UserModel` object before sending a new user object to the database via a `POST` request.

The ADP (Average Draft Position) module shows how JSON data flows from the FantasyData API, maps to the `AdpPlayer` object model, and is accessed by the `AdpController` through `adpDAO`. The Mock Draft module fetches JSON data from external APIs, processes ADP from the FantasyData API, and maps stats and general player information from the RapidAPI.

<div align="center">
<img width="468" src="https://github.com/user-attachments/assets/364456af-feae-49a2-b921-7c85607892a0" alt="Logical System Design">
</div>

---

### **Physical System Design**
The physical system design outlines the application architecture, including data storage, access, and front-end management. This application uses 4 MongoDB collections for storing user data, general player information, team schedules, and ADP. Stats are fetched directly from APIs and mapped to objects, avoiding database storage.

Front-end controllers handle application flow, with clients accessing the web directly. This design follows MVC architecture principles, ensuring organized data flow.

<div align="center">
<img width="468" src="https://github.com/user-attachments/assets/c59bf485-7a78-4be9-8a5f-af763b6f72ef" alt="Physical System Design">
</div>

---

### **UMLs**
The model classes represent data from API endpoints such as `StatsPlayer`, `EspnPlayer`, and `AdpPlayer`. These classes store various player details, like stats, general information, and average draft positions (ADP). Additional models like `Team`, `League`, and `CPU` manage the mock draft process.

<div align="center">
<img width="700" src="https://github.com/user-attachments/assets/bd0427f2-cc1b-4509-ab32-b7b0625dd068" alt="Model Classes">
</div>

API call classes contain methods to fetch and map data from external APIs. These classes call the APIs using a `sendRequest()` method and map JSON to models using `mapJson()`.

<div align="center">
<img width="700" src="https://github.com/user-attachments/assets/d81596a8-3121-42c3-8c20-2c7a326e7897" alt="API Classes">
</div>

Service and repository interfaces handle database management and CRUD operations. Repositories extend `MongoRepository<Object>` and define extra operations as needed, while service classes instantiate repositories and implement logic.

<div align="center">
<img width="700" src="https://github.com/user-attachments/assets/9495e114-a4cf-4829-bea2-cb409d3da755" alt="Service and Repository Classes">
</div>

Controllers handle user interaction, application flow, and data exchange. These controllers display views, manage drafts, and communicate with DAOs for data access. Example controllers include `HomeController`, `LoginController`, `DraftController`, and `StatsController`.

<div align="center">
<img width="700" src="https://github.com/user-attachments/assets/a750a850-7483-4f46-a50e-e0e81c471988" alt="Controller Classes">
</div>

---

### **Code**

#### **Draft Functionality**
The `showDraftBoard` method (below) creates `<FantasyTeam>` objects for user and CPU teams based on draft settings, sending data to `draftBoard.html`.

<div align="center">
<img width="900" src="https://github.com/user-attachments/assets/2961b57a-7005-4462-bd28-8af51806ffc0" alt="showDraftBoard Code">
</div>

The `selectPlayer` method handles player selections, ensuring CPU teams fill each position while selecting one of the top available players.

<div align="center">
<img width="900" src="https://github.com/user-attachments/assets/a5ead1bf-bd99-4124-8737-e8ab5cfda0ff" alt="selectPlayer Code">
</div>

The code snippet below demonstrates how the snake draft mechanism is implemented using the `isReversed` boolean.

<div align="center">
<img width="900" src="https://github.com/user-attachments/assets/7dbffae4-13e0-48bf-b105-14c51c1a3e84" alt="Snake Draft Functionality">
</div>

---

### **User Session**
The `authenticateUser` method validates user credentials and adds them to the session for managing previously drafted teams.

<div align="center">
<img width="900" src="https://github.com/user-attachments/assets/4d771e74-37af-4660-8e5f-3372fb3eac33" alt="authenticateUser Code">
</div>

The `getUserTeams` method retrieves previously drafted teams from the session and displays them to the user.

<div align="center">
<img width="900" src="https://github.com/user-attachments/assets/dcc1da91-6ccd-408b-8fd7-48bb432eb2a0" alt="getUserTeams Code">
</div>

---

### **API Calls**
NFL player data is fetched using external APIs to ensure data authenticity. The `sendRequestGetAllPlayers` method retrieves JSON player data, and the API key is stored securely.

<div align="center">
<img width="900" src="https://github.com/user-attachments/assets/9813361d-cdca-43a6-9405-0b56e7f604f4" alt="sendRequestGetAllPlayers Code">
</div>

The `mapJsonToPlayerObject` method converts the JSON payload into `EspnPlayer` objects for use within the application. This is done similarly for `AdpPlayer`, `StatsPlayer`, and `TeamSchedule`.

<div align="center">
<img width="900" src="https://github.com/user-attachments/assets/e815bd55-e6b5-4a3c-80ac-0d1ef2887fde" alt="mapJsonToPlayerObject Code">
</div>

</div>

