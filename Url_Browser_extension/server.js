const express = require('express');
const cors = require('cors');
const oracledb = require('oracledb');
const bodyParser = require('body-parser');
const { exec } = require('child_process');

const app = express();
const port = 3000;

// Middleware
app.use(cors());
app.use(bodyParser.json());

// OracleDB connection setup
const connectionConfig = {
  user: 'c##vishaal',
  password: 'shiva123',
  connectString: 'localhost:1521/XE',
};

oracledb.getConnection(connectionConfig, (err, connection) => {
  if (err) {
    console.error('Error connecting to Oracle Database:', err);
    return;
  }
  console.log('Connected to Oracle Database');

  app.post('/api/query', (req, res) => {
    const { action, username, url, prediction } = req.body;
    if (action === 'insert_data') {
      const sql = 'INSERT INTO extension_data (id, url, prediction) VALUES (:id, :url, :prediction)';
      connection.execute(sql, { id: username, url, prediction }, (err, result) => {
        if (err) {
          console.error('Error inserting data:', err);
          res.status(500).json({ error: 'Error inserting data' });
          return;
        }
        console.log('Data inserted:', result);
        
        // Commit the transaction to persist the changes
        connection.commit((commitErr) => {
          if (commitErr) {
            console.error('Error committing transaction:', commitErr);
            res.status(500).json({ error: 'Error committing transaction' });
            return;
          }
          console.log('Transaction committed successfully');
          res.status(200).json({ message: 'Data inserted and committed successfully' });
        });
      });
    }
    
  });
  // API endpoint for handling extension data
  app.post('/api/extension-data', (req, res) => {
    const { action, username, url, prediction } = req.body;
    realTimeMessage = action;
    realTimeData = { username };
    if (action === 'continue_without_login') {
      console.log('Received message: Continue without login');
      res.status(200).json({ message: 'User continued without logging in' });
    } else if (action === 'login_successful') {
      console.log('Received message: Login successful');
      console.log('Username:', username);
      // Perform database operations based on the username if needed

      // Send response with username
      res.status(200).json({ message: 'Login successful', username: username });
    } else if (action === 'execute_python') {
      const { scriptName } = req.body;
      const command = `python ${scriptName}`;

      // Execute Python script
      exec(command, (error, stdout, stderr) => {
        if (error) {
          console.error('Error executing Python script:', error);
          res.status(500).json({ error: 'Error executing Python script' });
        } else {
          console.log('Python script executed successfully');
          res.status(200).json({ message: 'Python script executed successfully', output: stdout });
        }
      });
    } else {
      res.status(400).json({ error: 'Invalid action' });
    }
  });
  app.get('/api/get-data', (req, res) => {
    // Send real-time message and data back to content.js
    res.status(200).json({ message: realTimeMessage, data: realTimeData });
  });
});


app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});
