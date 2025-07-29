# Mock O' Clock - Fantasy Football Mock Draft Simulator

A professional Fantasy Football Mock Draft Simulator built with Spring Boot, featuring real-time NFL player data, intelligent CPU opponents, and a modern web interface.

**Live Demo:** [View Application](https://www.mockoclockfantasy.com/)  
**Repository:** https://github.com/Carterc7/FantasyApp

## 🏈 Features

- **Real-time NFL Data**: Live player statistics and information via RapidAPI
- **Intelligent CPU Drafting**: AI-powered CPU opponents with position-based drafting strategies
- **Snake Draft System**: Traditional fantasy football draft format with automatic round reversal
- **User Authentication**: Secure login/registration with session management
- **Draft History**: Track and review your previous mock drafts
- **Responsive Design**: Modern, mobile-friendly interface
- **Production Ready**: Deployed on Heroku with MongoDB Atlas

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MongoDB Atlas account
- RapidAPI account

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Carterc7/FantasyApp.git
   cd FantasyApp
   ```

2. **Set up environment variables**
   Create a `.env` file in `src/main/resources/` with:
   ```env
   CONNECTION_STRING=your_mongodb_atlas_connection_string
   DATABASE_NAME_API=your_database_name
   API_KEY=your_rapidapi_key
   API_URL=https://tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com
   ```

3. **Get API Keys**
   - **RapidAPI Tank01 NFL API**: [Get Free Key](https://rapidapi.com/tank01/api/tank01-nfl-live-in-game-real-time-statistics-nfl)
   - **MongoDB Atlas**: [Create Free Cluster](https://www.mongodb.com/atlas)

4. **Populate Database**
   Run this endpoint to populate your MongoDB collection (may take 5-10 minutes for initial add):
   ```bash
   # Get all players
   GET /espn/add-all
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

6. **Access the application**
   Open http://localhost:8080 in your browser

## 🏗️ Architecture

### Technology Stack

- **Backend**: Spring Boot 3.1.1, Java 17
- **Database**: MongoDB Atlas
- **Frontend**: Thymeleaf, Bootstrap, JavaScript
- **APIs**: RapidAPI Tank01 NFL API
- **Deployment**: Heroku
- **Build Tool**: Maven

### Project Structure

```
src/main/java/com/fantasy/fantasyapi/
├── apiCalls/           # External API integration
├── controllers/        # Web controllers and endpoints
├── draft/             # Draft logic and services
├── leagueModels/      # User and team models
├── mongoServices/     # Database services
├── objectModels/      # Data transfer objects
├── repository/        # MongoDB repositories
└── security/          # Authentication and security
```

## 🎯 Core Features

### Mock Draft System

The application features a sophisticated mock draft system with:

- **Configurable Teams**: Set number of teams (4-16)
- **Snake Draft**: Automatic round reversal
- **CPU Intelligence**: Position-based drafting with ADP consideration
- **Real-time Updates**: Live draft board updates
- **Player Information**: Detailed stats and information on demand

### User Management

- **Secure Authentication**: BCrypt password hashing
- **Session Management**: Persistent user sessions
- **Draft History**: Track and review previous drafts
- **Team Management**: View and manage drafted teams

### Data Integration

- **Real-time NFL Data**: Live player statistics
- **ADP Integration**: Average Draft Position data
- **Player Profiles**: Comprehensive player information
- **Team Schedules**: Game schedules and statistics

## 🔧 Development

### Branch Strategy

- **`main`**: Development and testing branch
- **`production`**: Production deployment branch

### Building the Project

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package for deployment
mvn package

# Run locally
mvn spring-boot:run
```

### Code Quality

- **Java 17**: Modern Java features and performance
- **Spring Boot**: Rapid development and deployment
- **MongoDB**: Scalable NoSQL database
- **RESTful APIs**: Clean, maintainable endpoints

## 📊 Database Schema

### Collections

- **users**: User accounts and authentication
- **espnPlayers**: Player information and statistics
- **adpPlayers**: Average Draft Position data
- **teamSchedules**: Team schedules and game data
- **fantasyTeams**: User draft history and teams

## 🚀 Deployment

### Heroku Deployment

The application is configured for Heroku deployment with:

- **Java 17 Runtime**: Optimized for performance
- **MongoDB Atlas**: Cloud database integration
- **Environment Variables**: Secure configuration management
- **Auto-deployment**: Connected to production branch

### Environment Configuration

```bash
# Heroku environment variables
heroku config:set CONNECTION_STRING=your_mongodb_connection
heroku config:set DATABASE_NAME_API=your_database_name
heroku config:set API_KEY=your_rapidapi_key
heroku config:set API_URL=your_api_url
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request


## 👨‍💻 Author

**Carter Campbell**  
- GitHub: [@Carterc7](https://github.com/Carterc7)
- Email: lawsoncards.7@gmail.com

## 🙏 Acknowledgments

- **RapidAPI**: For providing NFL data APIs
- **MongoDB Atlas**: For cloud database hosting
- **Spring Boot**: For the excellent framework
- **Heroku**: For deployment platform

---

**Ready to start drafting?** [Launch the application](https://www.mockoclockfantasy.com/) and begin your fantasy football journey! 
