# Demo Web Chess Rating and Mobile Calculator Automation
## Video Demo & Reports
Demo Video: 
Report: 
## Setup & Run Instructions

### 1. Clone the Repository
```
git clone https://github.com/YOUR_GITHUB_USERNAME/chess-rating-automation.git
cd chess-rating-automation
mvn clean install
```
### 2. Run the Tests
- ChessRatingTest.java: **Extract data from chess.com** and updates the data in **Google Sheets**.
```

mvn clean verify "-Dit.test=ChessRatingTest"

```
