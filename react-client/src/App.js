import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Banking from './components/banking.component';

class App extends Component {
  render() {
    return (
      <div className="App">
        <h1>Banking</h1>
        <Banking/>
        <hr/>
        <h1>Insurance</h1>
        <hr/>
        
        <h1>Activity</h1>
        <hr/>

      </div>
    );
  }
}

export default App;
